package com.zhongmei.bty.snack.orderdish;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.cashier.orderdishmanager.DishClearStatusManager;
import com.zhongmei.bty.cashier.orderdishmanager.DishClearStatusManager.IViewer;
import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.bty.data.operates.DishDal;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishClearStatusAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishStandardAdapter;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;
import com.zhongmei.bty.snack.orderdish.view.TouchGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EFragment(R.layout.order_dish_clear_status)
public class OrderDishClearStatusFragment extends BasicDialogFragment implements IViewer, OnCheckedChangeListener {
    @ViewById(R.id.rg_clear_status_tab)
    RadioGroup rgClearStatusTab;

    @ViewById(R.id.ll_standard_type)
    LinearLayout llStandardType;

    @ViewById(R.id.gv_content)
    TouchGridView gvContent;

    @ViewById(R.id.cb_select_all)
    CheckBox cbSelectAll;

    @ViewById(R.id.btn_bottom)
    Button btnBottom;

    private PopupWindow clearStatusPopupWindow;

    private DishClearStatusManager mDishClearStatusManager;

    private OrderDishClearStatusAdapter mAdapter;

    private DishVo mDishVo;

    private OnCloseListener mOnCloseListener;

    private List<PropertyGroupVo<DishStandardVo>> mStandardGroupList;
    private ClearStatus mClearStatus = ClearStatus.SALE;

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
                        setClearButtonStatus();
                    }
                }
            };

    public void setData(DishVo dishVo) {
        mDishVo = dishVo;
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

    @AfterViews
    void init() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);

        mAdapter = new OrderDishClearStatusAdapter(getActivity());
        gvContent.setAdapter(mAdapter);

        rgClearStatusTab.setOnCheckedChangeListener(this);
        cbSelectAll.setChecked(false);
        cbSelectAll.setText(R.string.dinner_check_all);
        cbSelectAll.setOnCheckedChangeListener(mCheckedChangeListener);

        mDishClearStatusManager = new DishClearStatusManager(this);
        mDishClearStatusManager.loadData(mDishVo);
    }

    @Click(R.id.btn_close)
    void clickCloseButton() {
        dismiss();
    }

    @Click(R.id.btn_bottom)
    void clickBottomButton() {
        if (mAdapter != null) {
            List<DishAndStandards> selectedProperty = mAdapter.getSelectedProperty();
            if (selectedProperty != null && !selectedProperty.isEmpty()) {
                ClearStatus newValue =
                        rgClearStatusTab.getCheckedRadioButtonId() == R.id.rb_sale ? ClearStatus.CLEAR : ClearStatus.SALE;
                requestClearStatus(selectedProperty, newValue);
            }
        }
    }


    private void requestClearStatus(List<DishAndStandards> selectedProperty, final ClearStatus newValue) {
        List<String> dishUuids = new ArrayList<String>();
        for (DishAndStandards dishAndStandards : selectedProperty) {
            dishUuids.add(dishAndStandards.getDishShop().getUuid());
        }


        ResponseListener<Boolean> listener = new ResponseListener<Boolean>() {

            @Override
            public void onResponse(ResponseObject<Boolean> response) {
                if (ResponseObject.isOk(response)) {
                    if (mAdapter != null) {
                        List<DishAndStandards> dataSet = mAdapter.getDataSet();
                        List<DishAndStandards> newDataSet = new ArrayList<DishAndStandards>();
                        for (DishAndStandards dishAndStandards : dataSet) {
                            if (!mAdapter.isSelected(dishAndStandards)) {
                                newDataSet.add(dishAndStandards);
                            } else {
                                dishAndStandards.getDishShop().setClearStatus(newValue);
                            }
                        }
                        mAdapter.setDataSet(newDataSet);
                        mAdapter.getSelectedProperty().clear();
                        mDishClearStatusManager.filter(mClearStatus, getCurrentStandards());
                    }


                    String name = mDishVo.getDishShop().getName();
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

    @ItemClick(R.id.gv_content)
    public void myGridItemClicked(DishAndStandards dishAndStandards) {
        if (mAdapter != null) {
            List<DishAndStandards> selectProperty = new ArrayList<DishAndStandards>(mAdapter.getSelectedProperty());
            if (mAdapter.isSelected(dishAndStandards)) {
                selectProperty.remove(dishAndStandards);
            } else {
                selectProperty.add(dishAndStandards);
            }
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
            mDishClearStatusManager.filter(mClearStatus, getCurrentStandards());
        }
    }

    @Override
    public void onLoadData(List<PropertyGroupVo<DishStandardVo>> standardGroupList) {
        mStandardGroupList = standardGroupList;

        initStandardTypeGroup(standardGroupList);

        ClearStatus clearStatus =
                rgClearStatusTab.getCheckedRadioButtonId() == R.id.rb_sale ? ClearStatus.SALE : ClearStatus.CLEAR;
        mDishClearStatusManager.filter(clearStatus, getCurrentStandards());
    }

    @Override
    public void onFilter(List<DishAndStandards> dishList) {
        if (mAdapter != null) {
            mAdapter.setDataSet(dishList);
        }
        setClearButtonStatus();
        setCheckAllButtonStatus(dishList);
    }


    private void setClearButtonStatus() {
        if (mAdapter != null && Utils.isNotEmpty(mAdapter.getSelectedProperty())) {
            btnBottom.setEnabled(true);
        } else {
            btnBottom.setEnabled(false);
        }
    }


    private void setCheckAllButtonStatus(List<DishAndStandards> dishList) {
                cbSelectAll.setEnabled(Utils.isNotEmpty(dishList));
                cbSelectAll.setOnCheckedChangeListener(null);
        if (mAdapter != null && mAdapter.isAllSelected(dishList)) {
            cbSelectAll.setChecked(true);
            cbSelectAll.setText(R.string.order_dish_cancel_select_all);
        } else {
            cbSelectAll.setChecked(false);
            cbSelectAll.setText(R.string.dinner_check_all);
        }
        cbSelectAll.setOnCheckedChangeListener(mCheckedChangeListener);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        dismissPopupWindow();
        mAdapter.getSelectedProperty().clear();
        btnBottom.setText(checkedId == R.id.rb_sale ? R.string.hadClear : R.string.toSell);
        initStandardTypeGroup(mStandardGroupList);
        cbSelectAll.setOnCheckedChangeListener(null);
        cbSelectAll.setChecked(false);
        cbSelectAll.setOnCheckedChangeListener(mCheckedChangeListener);
        ClearStatus clearStatus = checkedId == R.id.rb_sale ? ClearStatus.SALE : ClearStatus.CLEAR;
        mClearStatus = clearStatus;
        mDishClearStatusManager.filter(clearStatus, getCurrentStandards());
    }

    private void initClearStatusPopupWindow(final TextView anchor, final PropertyGroupVo<DishStandardVo> propertyGroupVo) {
        if (clearStatusPopupWindow == null) {
            clearStatusPopupWindow = new PopupWindow(DensityUtil.dip2px(MainApplication.getInstance(), 194), LinearLayout.LayoutParams.WRAP_CONTENT);
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.clear_status_popupwindow, null);
            clearStatusPopupWindow.setContentView(contentView);
            clearStatusPopupWindow.setFocusable(true);
            clearStatusPopupWindow.setOutsideTouchable(true);
            clearStatusPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
                if (propertyGroupVo.getPropertyList().size() >= 4) {
            clearStatusPopupWindow.setHeight(DensityUtil.dip2px(MainApplication.getInstance(), 227));
        } else {
            clearStatusPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        }
                int x = -((DensityUtil.dip2px(MainApplication.getInstance(), 194) - anchor.getWidth()) / 2);
        int y = DensityUtil.dip2px(MainApplication.getInstance(), 5);
        clearStatusPopupWindow.showAsDropDown(anchor, x, y);

        ListView lvContent = (ListView) clearStatusPopupWindow.getContentView().findViewById(R.id.lv_content);
        final OrderDishStandardAdapter adapter =
                new OrderDishStandardAdapter(getActivity(), propertyGroupVo.getPropertyType().getName(),
                        propertyGroupVo.getPropertyList());
        if (anchor.getTag() != null) {
            adapter.setSelectedItem((DishStandardVo) anchor.getTag());
        }
        lvContent.setAdapter(adapter);
        lvContent.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dismissPopupWindow();

                Object object = adapter.getItem(arg2);
                if (object == null) {
                    anchor.setTag(null);
                    anchor.setText(propertyGroupVo.getPropertyType().getName());
                    anchor.setSelected(false);
                } else {
                    DishStandardVo dishStandardVo = (DishStandardVo) object;
                    anchor.setTag(dishStandardVo);
                    anchor.setText(dishStandardVo.getProperty().getName());
                    anchor.setSelected(true);
                }

                Set<DishProperty> standards = getCurrentStandards();
                ClearStatus clearStatus =
                        rgClearStatusTab.getCheckedRadioButtonId() == R.id.rb_sale ? ClearStatus.SALE : ClearStatus.CLEAR;
                mDishClearStatusManager.filter(clearStatus, standards.isEmpty() ? null : standards);
            }
        });
    }


    private Set<DishProperty> getCurrentStandards() {
        Set<DishProperty> standards = new HashSet<DishProperty>();
        int size = llStandardType.getChildCount();
        for (int i = 0; i < size; i++) {
            View view = llStandardType.getChildAt(i);
            Object tag = view.getTag();
            if (tag != null) {
                DishStandardVo dishProperty = (DishStandardVo) tag;
                standards.add(dishProperty.getProperty());
            }
        }

        return standards;
    }

    private void dismissPopupWindow() {
        if (clearStatusPopupWindow != null && clearStatusPopupWindow.isShowing()) {
            clearStatusPopupWindow.dismiss();
        }
    }

    private void initStandardTypeGroup(List<PropertyGroupVo<DishStandardVo>> standardGroupList) {
        llStandardType.removeAllViews();
        if (standardGroupList != null) {
            int size = standardGroupList.size();
            for (int i = 0; i < size; i++) {
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(DensityUtil.dip2px(MainApplication.getInstance(), 140), LinearLayout.LayoutParams.MATCH_PARENT);
                                if (i < size - 1) {
                    layoutParams.rightMargin = DensityUtil.dip2px(MainApplication.getInstance(), 18);
                }

                final PropertyGroupVo<DishStandardVo> propertyGroupVo = standardGroupList.get(i);

                final TextView tv = new TextView(getActivity());
                tv.setBackgroundResource(R.drawable.orderdish_clear_status_item_bg_sale_n);
                Drawable right = getActivity().getResources().getDrawable(R.drawable.orderdish_clear_status_arrow);
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
                tv.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 18), 0, DensityUtil.dip2px(MainApplication.getInstance(), 12), 0);
                tv.setTextSize(18);
                tv.setTextColor(getActivity().getResources().getColorStateList(R.color.choice_clear_status_title));
                tv.setLayoutParams(layoutParams);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setSelected(false);
                tv.setSingleLine(true);
                tv.setEllipsize(TruncateAt.END);
                tv.setText(propertyGroupVo.getPropertyType().getName());
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissPopupWindow();
                        initClearStatusPopupWindow(tv, propertyGroupVo);
                    }
                });

                llStandardType.addView(tv);
            }
        }
    }
}
