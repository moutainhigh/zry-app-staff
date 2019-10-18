package com.zhongmei.bty.dinner.orderdish;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishClearStatusAdapter;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;
import com.zhongmei.bty.snack.orderdish.view.TouchGridView;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.cashier.orderdishmanager.DishClearStatusManager;

import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EFragment(R.layout.dinner_orderdish_selected_cleared_dish)
public class OrderDishSelectedClearedDishFragment extends BasicDialogFragment implements DishClearStatusManager.IViewer {
    @ViewById(R.id.gv_content)
    TouchGridView gvContent;
    @ViewById(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @ViewById(R.id.title)
    TextView titleTV;
    @ViewById(R.id.btn_bottom)
    Button btnBottom;
    private DishClearStatusManager mDishClearStatusManager;

    private OrderDishClearStatusAdapter mAdapter;

    private DishVo mDishVo;

    private List<DishShop> oldSelectedDishShop = new ArrayList<DishShop>();

    private OnCloseListener mOnCloseListener;

    private DinnerDishBatchUpdateDishStatusFragment.CancelSelectedListener mCancelSelectedListener;

    private List<PropertyGroupVo<DishStandardVo>> mStandardGroupList;

        public void setData(DishVo dishVo) {
        mDishVo = dishVo;
        if (dishVo != null && dishVo.getSelectedDishs() != null) {
            oldSelectedDishShop.clear();
            oldSelectedDishShop.addAll(dishVo.getSelectedDishs());
        }
    }

        public void setOnCloseListener(OnCloseListener onCloseListener) {
        mOnCloseListener = onCloseListener;
    }

        public void setCancelSelectedListener(DinnerDishBatchUpdateDishStatusFragment.CancelSelectedListener cancelSelectedListener) {
        this.mCancelSelectedListener = cancelSelectedListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @AfterViews
    void init() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);

        mAdapter = new OrderDishClearStatusAdapter(getActivity());
        gvContent.setAdapter(mAdapter);

        cbSelectAll.setChecked(false);
        cbSelectAll.setText(R.string.dinner_check_all);
        cbSelectAll.setOnCheckedChangeListener(mCheckedChangeListener);

        mDishClearStatusManager = new DishClearStatusManager(this);
        if (mDishVo != null) {
            titleTV.setText(String.format(getString(R.string.please_select_goods_to_self_format), mDishVo.getShortName()));
            mDishClearStatusManager.loadData(mDishVo);
        }
    }

    @Click(R.id.close_button)
    void clickCloseButton() {
        dismiss();
    }

    @Click(R.id.btn_bottom)
    void clickBottomButton() {
        if (mAdapter != null) {
            List<DishAndStandards> selectedProperty = mAdapter.getSelectedProperty();
            clearOldData();            if (selectedProperty != null && selectedProperty.size() > 0) {                List<DishShop> list = new ArrayList<DishShop>(selectedProperty.size());
                for (DishAndStandards dishStandard : selectedProperty) {
                    list.add(dishStandard.getDishShop());
                }
                if (!list.isEmpty()) {
                    this.mDishVo.setSelected(true);
                    this.mDishVo.setSelectedDishs(list);
                }
            } else {                this.mDishVo.setSelected(false);
                this.mDishVo.setSelectedDishs(null);
            }
            if (this.mOnCloseListener != null) {
                this.mOnCloseListener.onClose(true, this.mDishVo);
            }
            this.dismiss();
        }
    }

        private void clearOldData() {
        if (!oldSelectedDishShop.isEmpty()) {
            for (DishShop shop : oldSelectedDishShop) {
                if (mCancelSelectedListener != null)
                    mCancelSelectedListener.doCancelSelected(shop);

            }
        }
    }

    @ItemClick(R.id.gv_content)
    public void myGridItemClicked(DishAndStandards dishAndStandards) {
        if (mAdapter != null) {
            List<DishAndStandards> selectProperty = new ArrayList<DishAndStandards>(mAdapter.getSelectedProperty());
            if (mAdapter.isSelected(dishAndStandards)) {
                selectProperty.remove(dishAndStandards);
            } else {
                selectProperty.add(dishAndStandards);
            }
            btnBottom.setEnabled(true);
            mAdapter.setSelectedProperty(selectProperty);

                        cbSelectAll.setOnCheckedChangeListener(null);
            if (selectProperty.size() == mAdapter.getCount()) {
                cbSelectAll.setChecked(true);
                cbSelectAll.setText(R.string.order_dish_cancel_select_all);
            } else {
                cbSelectAll.setChecked(false);
                cbSelectAll.setText(R.string.dinner_check_all);
            }
            cbSelectAll.setOnCheckedChangeListener(mCheckedChangeListener);
        }
    }

    @Override
    public void onLoadData(List<PropertyGroupVo<DishStandardVo>> standardGroupList) {
        mStandardGroupList = standardGroupList;

        mDishClearStatusManager.filter(ClearStatus.CLEAR, null);
    }

    private void setCheckAllAndClearButtonStatus() {
        List<DishAndStandards> selectProperty = new ArrayList<DishAndStandards>(mAdapter.getSelectedProperty());
        if (selectProperty.size() == mAdapter.getCount()) {
            cbSelectAll.setChecked(true);
            cbSelectAll.setText(R.string.order_dish_cancel_select_all);
        } else {
            cbSelectAll.setChecked(false);
            cbSelectAll.setText(R.string.dinner_check_all);
        }
    }

    @Override
    public void onFilter(List<DishAndStandards> dishList) {
        if (dishList != null) {
            if (this.mDishVo != null && this.mDishVo.getSelectedDishs() != null && !this.mDishVo.getSelectedDishs().isEmpty()) {
                List<DishAndStandards> dataList = new ArrayList<DishAndStandards>();
                for (DishAndStandards dishStand : dishList) {
                    if (this.mDishVo.getSelectedDishs().contains(dishStand.getDishShop())) {
                        dataList.add(dishStand);
                    }
                }
                                if (!dataList.isEmpty()) {
                    mAdapter.setSelectedProperty(dataList);
                }
            }
        }
        if (mAdapter != null) {
            mAdapter.setDataSet(dishList);
        }
        setCheckAllAndClearButtonStatus();
    }

        private CompoundButton.OnCheckedChangeListener mCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cbSelectAll.setText(R.string.order_dish_cancel_select_all);
                    } else {
                        cbSelectAll.setText(R.string.dinner_check_all);
                    }
                    if (mAdapter != null) {
                        List<DishAndStandards> dataSet = null;
                                                if (isChecked) {
                            dataSet = new ArrayList<DishAndStandards>(mAdapter.getDataSet());
                        }
                        mAdapter.setSelectedProperty(dataSet);
                                            }
                }
            };
}
