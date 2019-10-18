package com.zhongmei.bty.dinner.orderdish;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.dinner.adapter.DinnerDishListPagerAdapter;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.data.operates.DishDal;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.context.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@EFragment(R.layout.dinner_dish_batchupdatestatus_fragment)
public class DinnerDishBatchUpdateDishStatusFragment extends BasicDialogFragment implements ViewPager.OnPageChangeListener {

    @ViewById(R.id.vp_dish_list)
    protected ViewPager vpDishList;

    @ViewById(R.id.iv_dish_type_empty)
    protected ImageView ivDishTypeEmpty;

    @ViewById(R.id.ll_dots)
    protected LinearLayout llDots;

    @ViewById(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @ViewById(R.id.batch_update_button)
    Button btnBottom;    protected DinnerDishListPagerAdapter mAdapter;

    private int mCurrentIndex = 0;

    private DishInfo mDishInfo;
    private DishManager mDishManager;

    private List<DishShop> selectedDishs = new LinkedList<DishShop>();
    private OnCloseListener mOnCloseListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDishManager = new DishManager();
        mDishManager.registerObserver();
    }

    @Override
    public void onDestroy() {
        if (mDishManager != null) {
            mDishManager.unregisterObserver();
        }
        super.onDestroy();
    }

    public void initDishInfos(DishInfo dishInfo) {
        mDishInfo = dishInfo;
    }

    @SuppressLint("WrongConstant")
    @AfterViews
    protected void initView() {

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        vpDishList.setOnPageChangeListener(this);
        cbSelectAll.setChecked(false);
        cbSelectAll.setText(R.string.selectAll);
        cbSelectAll.setOnCheckedChangeListener(mCheckedChangeListener);
        mAdapter = new DinnerDishListPagerAdapter(getActivity(), new ArrayList<DishVo>()) {

            @Override
            public void doItemTouch(DishVo dishVo) {
                                myGridItemClicked(dishVo);
            }

            @Override
            public void doItemLongClick(DishVo dishVo) {
            }

            @Override
            protected int getNumRows() {
                return 3;
            }
        };
        mAdapter.setEditMode(true);
        vpDishList.setAdapter(mAdapter);
        loadData();
    }

    @Click(R.id.close_button)
    void closeDialog() {
        this.doCancelSelectedAll();
        if (mOnCloseListener != null) {
            mOnCloseListener.onClose(false, null);
        }
        this.dismiss();
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

        OnCloseListener mOnSelectedDialogCloseListener = new OnCloseListener() {

        @Override
        public void onClose(boolean isEnsure, Object obj) {
            if (isEnsure) {
                DishVo vo = (DishVo) obj;
                if (vo != null && vo.getSelectedDishs() != null) {
                    for (DishShop dishShop : vo.getSelectedDishs()) {
                        if (!selectedDishs.contains(dishShop)) {
                            selectedDishs.add(dishShop);
                        }
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            updatCheckView();
        }
    };

    private void myGridItemClicked(final DishVo dishVo) {
        if (dishVo.isContainProperties()) {
                        OrderDishSelectedClearedDishFragment orderDishClearStatusFragment = new OrderDishSelectedClearedDishFragment_();
            orderDishClearStatusFragment.setData(dishVo);
            orderDishClearStatusFragment.setOnCloseListener(mOnSelectedDialogCloseListener);
            orderDishClearStatusFragment.setCancelSelectedListener(mCancelSelectedListener);
            orderDishClearStatusFragment.show(getFragmentManager(), "OrderDishSelectedClearedDishFragment");

        } else {
            if (dishVo.isSelected()) {
                dishVo.setSelected(false);
                selectedDishs.remove(dishVo.getDishShop());
            } else {
                dishVo.setSelected(true);
                selectedDishs.add(dishVo.getDishShop());
            }
            updatCheckView();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updatCheckView() {
        cbSelectAll.setOnCheckedChangeListener(null);
        if (isSelectedAll()) {
            cbSelectAll.setChecked(true);
            cbSelectAll.setText(R.string.order_dish_cancel_select_all);
        } else {
            cbSelectAll.setChecked(false);
            cbSelectAll.setText(R.string.dinner_check_all);
        }
        cbSelectAll.setOnCheckedChangeListener(mCheckedChangeListener);
        if (!this.selectedDishs.isEmpty()) {
            btnBottom.setEnabled(true);
        } else {
            btnBottom.setEnabled(false);
        }
    }

        private void doSelectedAll() {
        if (mDishInfo != null && mDishInfo.dishList != null) {
            selectedDishs.clear();
            DishShop dish = null;
            for (DishVo dishVo : mDishInfo.dishList) {
                dishVo.setSelected(true);
                if (dishVo.isContainProperties()) {
                    List<DishShop> list = new ArrayList<DishShop>();
                                        if (dishVo.getDishShop().getClearStatus() == ClearStatus.CLEAR) {
                        list.add(dishVo.getDishShop());
                        selectedDishs.add(dishVo.getDishShop());
                    }
                                        if (dishVo.getOtherDishs() != null) {
                        for (Map.Entry<String, DishShop> entry : dishVo.getOtherDishs().entrySet()) {
                            dish = entry.getValue();
                            if (dish.getClearStatus() == ClearStatus.CLEAR) {
                                selectedDishs.add(dish);
                                list.add(dish);
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        dishVo.setSelectedDishs(list);
                    }
                } else {
                    selectedDishs.add(dishVo.getDishShop());
                }
            }
        }
    }

        private void doCancelSelectedAll() {
        selectedDishs.clear();
        for (DishVo dishVo : mDishInfo.dishList) {
            if (dishVo.isSelected()) {
                dishVo.setSelected(false);
                dishVo.setSelectedDishs(null);
            }
        }
    }

        private boolean isSelectedAll() {
        for (DishVo dishVo : mDishInfo.dishList) {
            if (!dishVo.isSelected()) {
                return false;
            }
        }
        return true;
    }


    @Click(R.id.batch_update_button)
    protected void batchClearStatus() {
        if (this.selectedDishs.isEmpty()) {
            ToastUtil.showLongToast(R.string.please_select_food);
            return;
        }
        List<String> dishUuids = new ArrayList<String>(selectedDishs.size());
        for (DishShop dish : this.selectedDishs) {

            dishUuids.add(dish.getUuid());
        }
        final ClearStatus newValue = ClearStatus.SALE;

        ResponseListener<Boolean> listener = new ResponseListener<Boolean>() {

            @Override
            public void onResponse(ResponseObject<Boolean> response) {
                try {
                    if (ResponseObject.isOk(response) && response.getContent()) {
                        if (mOnCloseListener != null) {
                            mOnCloseListener.onClose(true, null);
                        }
                        for (DishShop dish : selectedDishs) {
                            dish.setClearStatus(newValue);
                        }
                        ToastUtil.showLongToast(getString(R.string.selected_goods) + getActivity().getString(R.string.nClearstatus));
                        dismiss();
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
                } catch (Exception e) {
                    e.printStackTrace();
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


    private void loadData() {
        if (isAdded() && mDishInfo != null && mAdapter != null) {
            if (Utils.isNotEmpty(mDishInfo.dishList)) {
                mAdapter.setDataSet(mDishInfo.dishList);
                createIndex(mCurrentIndex, mAdapter.getCount());

                vpDishList.setVisibility(View.VISIBLE);
                llDots.setVisibility(View.VISIBLE);
                ivDishTypeEmpty.setVisibility(View.GONE);
            } else {
                vpDishList.setVisibility(View.GONE);
                llDots.setVisibility(View.GONE);
                ivDishTypeEmpty.setVisibility(View.VISIBLE);
            }
            updatCheckView();
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

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        mOnCloseListener = onCloseListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!selectedDishs.isEmpty()) {
            this.doCancelSelectedAll();        }
        super.onDismiss(dialog);
    }

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbSelectAll.setText(R.string.order_dish_cancel_select_all);
                                                doSelectedAll();
                        updatCheckView();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        cbSelectAll.setText(R.string.selectAll);
                                                doCancelSelectedAll();
                        updatCheckView();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            };
    private CancelSelectedListener mCancelSelectedListener = new CancelSelectedListener() {

        @Override
        public void doCancelSelected(DishShop dishShop) {
            if (selectedDishs.contains(dishShop)) {
                selectedDishs.remove(dishShop);
            }
        }
    };

    public interface CancelSelectedListener {

        public void doCancelSelected(DishShop dishShop);
    }
}
