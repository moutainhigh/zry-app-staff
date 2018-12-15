package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.log.RLog;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.booking.ui.DrawableCenterTextView;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.view.VerticalViewPager;
import com.zhongmei.bty.dinner.Listener.BrandTypeListener;
import com.zhongmei.bty.snack.orderdish.adapter.DishTypeInflate;

import java.util.Iterator;
import java.util.List;

/**
 * @Date： 17/6/7
 * @Description: 二级菜单选择器
 * @Version: 1.0
 */
public class BrandTypeView extends LinearLayout implements DishTypeInflate.ChangeTypeListener, View.OnClickListener {

    private Context mContext;
    private DishManager mDishManager;
    private DishTypeInflate mDishTypeInflate;
    private BrandTypeListener mListerer;


    protected DrawableCenterTextView mBtnSearchGoods;
    private VerticalViewPager mViewPagerDishType;
    private ImageButton mArrowUp;
    private ImageButton mArrowDown;

    private void assignViews() {
        mBtnSearchGoods = (DrawableCenterTextView) findViewById(R.id.btn_search_goods);
        mViewPagerDishType = (VerticalViewPager) findViewById(R.id.viewPager_dish_type);
        mArrowUp = (ImageButton) findViewById(R.id.arrow_up);
        mArrowDown = (ImageButton) findViewById(R.id.arrow_down);
    }


    public BrandTypeView(Context context, DishManager dishManager, BrandTypeListener listener) {
        super(context);
        mDishManager = dishManager;
        mListerer = listener;
        initView(context);
    }

    public BrandTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BrandTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_brandtype_select, this, true);
        assignViews();
        mContext = context;
        mArrowDown.setOnClickListener(this);
        mArrowUp.setOnClickListener(this);
        mBtnSearchGoods.setOnClickListener(this);
        if (mDishTypeInflate == null) {
            mDishTypeInflate = new DishTypeInflate(context, this);
            mDishTypeInflate.setPageSize(8);
            mDishTypeInflate.setItemBg(R.drawable.dinner_dish_type_item_bg);
            mDishTypeInflate.setItemTextSize(16);
            mDishTypeInflate.setItemTextColor(R.drawable.dinnerdish_type_text_selector);
        }
        RLog.i(RLog.DISH_KEY_TAG, "## 正餐商品界面获取商品中类信息 begin" + "position : BrandTypeView -> initView()");
        List<DishBrandType> dishBrandTypes = mDishManager.loadData().dishTypeList;
        RLog.i(RLog.DISH_KEY_TAG, "## 正餐商品界面获取商品中类信息 end 个数为:" + Utils.size(dishBrandTypes) + "position : BrandTypeView -> initView()");
        Iterator<DishBrandType> iterator = dishBrandTypes.iterator();    //过滤不显示中类
        while (iterator.hasNext()) {
            DishBrandType type = iterator.next();
            if (type.isShow() != Bool.YES.value())
                iterator.remove();
        }
        mDishTypeInflate.setDinner(true);
        mDishTypeInflate.setData(dishBrandTypes);
        mDishTypeInflate.inflateView(mViewPagerDishType);
    }

    @Override
    public void onChangeTypeListener(DishBrandType dishBrandType) {
        if (mListerer != null)
            mListerer.onBrandTypeChange(dishBrandType, null, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow_up:
                mDishTypeInflate.scrollToPreviousPage();
                MobclickAgentEvent.onEvent(UserActionCode.ZC020027);
                break;
            case R.id.arrow_down:
                mDishTypeInflate.scrollToNextPage();
                MobclickAgentEvent.onEvent(UserActionCode.ZC020028);
                break;
            case R.id.btn_search_goods:
                MobclickAgentEvent.onEvent(UserActionCode.ZC020013);
                if (mListerer != null)
                    mListerer.onSearchClick();
                break;
            default:
                break;
        }
    }
}
