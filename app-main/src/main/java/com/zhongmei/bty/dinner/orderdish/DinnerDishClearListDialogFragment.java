package com.zhongmei.bty.dinner.orderdish;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.orderdish.bean.DishPageInfo;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.bty.data.operates.DishDal;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.dinner.adapter.DinnerDishListPagerAdapter;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.basemodule.orderdish.event.EventDishChangedNotice;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.snack.orderdish.OrderDishClearStatusFragment;
import com.zhongmei.bty.snack.orderdish.OrderDishClearStatusFragment_;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.fragment_dinner_dishclear_list)
public class DinnerDishClearListDialogFragment extends BasicDialogFragment implements ViewPager.OnPageChangeListener {

    @ViewById(R.id.btn_search_goods)
    protected ImageButton btnSearchGoods;

    @ViewById(R.id.vp_dish_list)
    protected ViewPager vpDishList;

    @ViewById(R.id.iv_dish_type_empty)
    protected ImageView ivDishTypeEmpty;

    @ViewById(R.id.ll_dots)
    protected LinearLayout llDots;

    @ViewById(R.id.close_button)
    protected Button close_button;

    @ViewById(R.id.batchupdate_button)
    protected Button batchUpdateButton;
    @ViewById(R.id.descipe)
    protected TextView descipe;
    protected DinnerDishListPagerAdapter mAdapter;

    private LoadDishTask mTask;

    private int mCurrentIndex = 0;
    private DishInfo mDishInfo;
    protected DishManager mDishManager;
    private OnCloseListener mOnCloseListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDishManager = new DishManager();
        mDishManager.registerObserver();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        if (mDishManager != null) {
            mDishManager.unregisterObserver();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @SuppressLint("WrongConstant")
    @AfterViews
    protected void initView() {

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        vpDishList.setOnPageChangeListener(this);

        mAdapter = new DinnerDishListPagerAdapter(getActivity(), new ArrayList<DishPageInfo>()) {

            @Override
            public void doItemTouch(DishVo dishVo) {
            }

            @Override
            public void doItemLongClick(DishVo dishVo) {
                myGridItemLongClicked(dishVo);
            }

            @Override
            protected int getNumRows() {
                return 3;
            }
        };
        vpDishList.setAdapter(mAdapter);
        mAdapter.setHidClearNumber(true);
        loadData();

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < llDots.getChildCount(); i++) {
            llDots.getChildAt(i).setSelected(false);
        }
        mCurrentIndex = position;
        if (mCurrentIndex < llDots.getChildCount()) {
            llDots.getChildAt(mCurrentIndex).setSelected(true);
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void myGridItemLongClicked(final DishVo dishVo) {
        if (dishVo.isContainProperties()) {
            OrderDishClearStatusFragment orderDishClearStatusFragment = new OrderDishClearStatusFragment_();
            orderDishClearStatusFragment.setData(dishVo);
            orderDishClearStatusFragment.setOnCloseListener(new OnCloseListener() {

                @Override
                public void onClose(boolean isEnsure, Object obj) {
                    loadData();

                }
            });
            orderDishClearStatusFragment.show(getFragmentManager(), "clearStatusFragment");
        } else {
            int title = dishVo.isClear() ? R.string.checkUClearDishStatus : R.string.checkClearDishStatus;
            new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance()).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .negativeText(R.string.common_cancel)
                    .positiveText(R.string.common_submit)
                    .positiveLinstner(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestClearStatus(dishVo, dishVo.isClear() ? ClearStatus.SALE : ClearStatus.CLEAR);
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), "clearDishStatus");
        }
    }


    private void requestClearStatus(final DishVo dishVo, final ClearStatus newValue) {
        List<String> dishUuids = new ArrayList<String>();
        dishUuids.add(dishVo.getDishShop().getUuid());


        ResponseListener<Boolean> listener = new ResponseListener<Boolean>() {

            @Override
            public void onResponse(ResponseObject<Boolean> response) {
                if (ResponseObject.isOk(response) && response.getContent()) {
                    dishVo.getDishShop().setClearStatus(newValue);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    String name = dishVo.getDishShop().getName();
                    if (TextUtils.isEmpty(name)) {
                        name = "";
                    }
                    if (newValue == ClearStatus.SALE) {

                        String info = name + getString(R.string.nClearstatus);
                        ToastUtil.showLongToast(info);
                    } else {
                        String info = name + getString(R.string.tClearstatus);
                        ToastUtil.showLongToast(info);
                    }
                    loadData();
                } else if (1100 == response.getStatusCode()) {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusNeedSync);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusNeedSync);
                    }
                } else {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusFailure);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusFailure);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (newValue == ClearStatus.SALE) {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.nClearstatusFailure) + error.getMessage());
                } else {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.yClearstatusFailure) + error.getMessage());
                }
            }

        };

        DishDal dishDal = OperatesFactory.create(DishDal.class);
        dishDal.clearStatus(newValue, dishUuids, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    public void onEventMainThread(EventDishChangedNotice eventDishChangedNotice) {
        Log.e("onEventMainThread", " received  EventDishChangedNotice");
        loadData();
    }


    private void loadData() {
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new LoadDishTask();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class LoadDishTask extends AsyncTask<Void, Void, DishInfo> {


        LoadDishTask() {

        }

        @Override
        protected DishInfo doInBackground(Void... params) {
            return mDishManager.loadDishInfoHaveClear();
        }

        @Override
        protected void onPostExecute(DishInfo dishInfo) {
            if (isAdded() && dishInfo != null && mAdapter != null) {
                if (Utils.isNotEmpty(dishInfo.dishList)) {
                    mDishInfo = dishInfo;
//                    mAdapter.setDataSet(dishInfo.dishList);
                    createIndex(mCurrentIndex, mAdapter.getCount());

                    vpDishList.setVisibility(View.VISIBLE);
                    llDots.setVisibility(View.VISIBLE);
                    ivDishTypeEmpty.setVisibility(View.GONE);
                    descipe.setVisibility(View.VISIBLE);
                    batchUpdateButton.setVisibility(View.VISIBLE);
                } else {
                    vpDishList.setVisibility(View.GONE);
                    llDots.setVisibility(View.GONE);
                    ivDishTypeEmpty.setVisibility(View.VISIBLE);
                    descipe.setVisibility(View.INVISIBLE);
                    batchUpdateButton.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    private void createIndex(int currentIndex, int totalSize) {
        llDots.removeAllViews();

        LinearLayout.LayoutParams indexParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        indexParams.setMargins(DensityUtil.dip2px(MainApplication.getInstance(), 2), 0, DensityUtil.dip2px(MainApplication.getInstance(), 2), 0);
        for (int i = 0; i < totalSize; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(indexParams);
            imageView.setBackgroundResource(R.drawable.dinnerdish_list_index_selector);
            llDots.addView(imageView, i);
        }

        if (currentIndex < totalSize) {
            llDots.getChildAt(currentIndex).setSelected(true);
        }
    }

    @Click(R.id.close_button)
    void closeDialog() {
        dismissAllowingStateLoss();
    }

    @Click(R.id.batchupdate_button)
    protected void showBatchUpdateDialog() {
        if (!ClickManager.getInstance().isClicked()) {
            DinnerDishBatchUpdateDishStatusFragment dialogFragment = new DinnerDishBatchUpdateDishStatusFragment_();
            dialogFragment.initDishInfos(this.mDishInfo);
            dialogFragment.setOnCloseListener(new OnCloseListener() {
                @Override
                public void onClose(boolean isEnsure, Object obj) {
                    if (isEnsure)
                        loadData();
                }
            });
            dialogFragment.show(getActivity().getSupportFragmentManager(), "DinnerDishBatchUpdateStatusDialogFragment");
            MobclickAgentEvent.onEvent(UserActionCode.ZC020014);
        }
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        mOnCloseListener = onCloseListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mOnCloseListener != null) {
            mOnCloseListener.onClose(true, null);
        }
        super.onDismiss(dialog);
    }
}
