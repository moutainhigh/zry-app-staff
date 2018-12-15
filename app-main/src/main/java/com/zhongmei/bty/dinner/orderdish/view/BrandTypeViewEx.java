package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.booking.ui.DrawableCenterTextView;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishBrandTypes;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.dinner.Listener.BrandTypeListener;
import com.zhongmei.bty.dinner.adapter.DishTwoTypeAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.DishTypeInflateEx;
import com.zhongmei.bty.commonmodule.view.VerticalViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Date： 17/6/7
 * @Description: 三级菜单选择器
 * @Version: 1.0
 */
public class BrandTypeViewEx extends LinearLayout implements DishTypeInflateEx.ChangeTypeListener, View.OnClickListener, AdapterView.OnItemClickListener {


    private View mPopupView = null;
    private Context mContext;
    private DishManager mDishManager;
    private DishTypeInflateEx mDishTypeInflate;
    private BrandTypeListener mListerer;
    private HashMap<Long, List<DishBrandType>> mTwoTypeMap;
    private LoadDishTwoType mTypeTask;
    private DishBrandType mLastDishType = null;
    private DishTwoTypeAdapter mDishTwoTypeAdapter = null;
    private List<DishBrandType> mTwoTypeList = new ArrayList<>();
    private PopupWindow mPopupWindow;
    private ListView mLvTwoTypes;
    private Handler mHandler;


    private DrawableCenterTextView mBtnSearchGoods;
    private VerticalViewPager mViewPagerDishType;
    private ImageButton mArrowUp;
    private ImageButton mArrowDown;

    private void assignViews() {
        mBtnSearchGoods = (DrawableCenterTextView) findViewById(R.id.btn_search_goods);
        mViewPagerDishType = (VerticalViewPager) findViewById(R.id.viewPager_dish_type);
        mArrowUp = (ImageButton) findViewById(R.id.arrow_up);
        mArrowDown = (ImageButton) findViewById(R.id.arrow_down);
    }


    public BrandTypeViewEx(Context context, DishManager dishManager, BrandTypeListener listener) {
        super(context);
        mDishManager = dishManager;
        mListerer = listener;
        initView(context);
    }

    public BrandTypeViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BrandTypeViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_brandtype_select, this, true);
        assignViews();

        mPopupView = inflater.inflate(R.layout.popuwindow_brandtype_viewex, null);
        mLvTwoTypes = (ListView) mPopupView.findViewById(R.id.lv_types);
        mDishTwoTypeAdapter = new DishTwoTypeAdapter(mTwoTypeList, context);
        mLvTwoTypes.setAdapter(mDishTwoTypeAdapter);
        mLvTwoTypes.setItemChecked(0, true);
        mLvTwoTypes.setOnItemClickListener(this);

        mContext = context;
        mArrowDown.setOnClickListener(this);
        mArrowUp.setOnClickListener(this);
        mBtnSearchGoods.setOnClickListener(this);

        final DishBrandTypes dishBrandTypes = mDishManager.getSupperDishTypes();
        if (dishBrandTypes == null || Utils.isEmpty(dishBrandTypes.dishTypeList)) {
            return;
        }

        if (mDishTypeInflate == null) {
            mDishTypeInflate = new DishTypeInflateEx(context, this);
            mDishTypeInflate.setPageSize(8);
            mDishTypeInflate.setItemBg(R.drawable.dinner_dish_type_item_bg);
            mDishTypeInflate.setItemTextSize(16);
            mDishTypeInflate.setItemTextColor(R.drawable.dinnerdish_type_text_selector);
        }

        mDishTypeInflate.setData(dishBrandTypes.dishTypeList);
        mDishTypeInflate.inflateView(mViewPagerDishType);
        mDishTypeInflate.hideSelectedViewArr();
        mHandler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mTypeTask = new LoadDishTwoType(dishBrandTypes.dishTypeList.get(0), false);
                mTypeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                mTwoTypeMap = new HashMap<>();
                DishBrandTypes dishBrandTypes1 = null;
                for (DishBrandType type : dishBrandTypes.dishTypeList) {
                    dishBrandTypes1 = mDishManager.getSecondDishTypes(type);
                    if (dishBrandTypes1 == null)
                        continue;
                    mTwoTypeMap.put(type.getId(), dishBrandTypes1.dishTypeList);
                }
            }
        });

    }

    @Override
    public void onChangeTypeListener(DishBrandType dishBrandType, boolean sw) {
        if (mTypeTask != null)
            mTypeTask.cancel(true);
        if (sw) {
            mLvTwoTypes.setItemChecked(0, true);
            if (mTwoTypeMap == null)
                return;
            List<DishBrandType> dishBrandTypes = mTwoTypeMap.get(dishBrandType.getId());
            if (Utils.isNotEmpty(dishBrandTypes)) {
                mLastDishType = dishBrandTypes.get(0);
                mTwoTypeList.clear();
                mTwoTypeList.addAll(dishBrandTypes);

                showWindow();
                mDishTwoTypeAdapter.notifyDataSetChanged();

                if (mListerer != null)
                    mListerer.onBrandTypeChange(mLastDishType, null, false);
            }
        } else {
            if (Utils.isNotEmpty(mTwoTypeList)) {
                showWindow();
            }
        }
    }

    class LoadDishTwoType extends AsyncTask<Void, Void, DishBrandTypes> {

        DishBrandType dishBrandType;
        boolean showView = true;

        public LoadDishTwoType(DishBrandType dishBrandType, boolean show) {
            this.dishBrandType = dishBrandType;
            showView = show;
        }

        @Override
        protected DishBrandTypes doInBackground(Void... params) {
            return mDishManager.getSecondDishTypes(dishBrandType);
        }

        @Override
        protected void onPostExecute(DishBrandTypes dishBrandTypes) {
            if (mDishTypeInflate.getLastType().getId().longValue() != dishBrandType.getId().longValue())
                return; //防点击错乱

            if (dishBrandTypes != null && mDishTwoTypeAdapter != null
                    && dishBrandTypes.dishTypeList != null && Utils.isNotEmpty(dishBrandTypes.dishTypeList)) {
                mLastDishType = dishBrandTypes.dishTypeList.get(0);
                mTwoTypeList.clear();
                mTwoTypeList.addAll(dishBrandTypes.dishTypeList);
                if (showView)
                    showWindow();
                else
                    dismissWindow();
                mDishTwoTypeAdapter.notifyDataSetChanged();

                if (mListerer != null)
                    mListerer.onBrandTypeChange(mLastDishType, null, false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_up:
                if (mDishTypeInflate != null)
                    mDishTypeInflate.scrollToPreviousPage();
                MobclickAgentEvent.onEvent(mContext, MobclickAgentEvent.dinnerOrderDishTypePageUp);
                break;
            case R.id.arrow_down:
                if (mDishTypeInflate != null)
                    mDishTypeInflate.scrollToNextPage();
                MobclickAgentEvent.onEvent(mContext, MobclickAgentEvent.dinnerOrderDishTypePageDown);
                break;
            case R.id.btn_search_goods:
                if (mListerer != null)
                    mListerer.onSearchClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mLastDishType = mTwoTypeList.get(position);
        if (mListerer != null)
            mListerer.onBrandTypeChange(mLastDishType, null, false);
        dismissWindow();
        mDishTypeInflate.setSelectTxt(mTwoTypeList.get(position).getName());
        mDishTypeInflate.hideSelectedViewArr();
    }

    private void showWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mPopupView, LayoutParams.WRAP_CONTENT, getHeight());
        }
        if (mPopupWindow.isShowing())
            return;
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDishTypeInflate.hideSelectedViewArr();
            }
        });
        int[] location = new int[2];
        getLocationInWindow(location);
        mPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] - DensityUtil.dip2px(mContext, 166f), location[1]);
    }

    private void dismissWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }
}
