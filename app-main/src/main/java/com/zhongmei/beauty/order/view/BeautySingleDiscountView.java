package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;
import com.zhongmei.bty.basemodule.discount.enums.DiscountType;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.session.SessionImpl;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathManualMarketTool;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.common.view.CustomDiscountDialog;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.action.ActionDinnerBatchDiscount;
import com.zhongmei.bty.dinner.action.ActionDinnerBatchFree;
import com.zhongmei.bty.dinner.action.ActionDinnerFinished;
import com.zhongmei.bty.dinner.adapter.DinnerDiscountAdapter;
import com.zhongmei.bty.dinner.vo.DiscountShopVo;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.WrapGridView;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;



@EViewGroup(R.layout.beauty_single_discount_layout)
public class BeautySingleDiscountView extends LinearLayout implements OnItemClickListener {
    private static final String TAG = BeautySingleDiscountView.class.getSimpleName();

    @ViewById(R.id.dinner_batch_discount)
    public Button dinner_batch_discount;

    @ViewById(R.id.btn_user_define)
    Button btn_user_define;

    @ViewById(R.id.dinner_tab_discount)
    TextView tab_discount;

    @ViewById(R.id.dinner_tab_let)
    TextView tab_let;
        @ViewById(R.id.dinner_tab_free)
    TextView tab_free;

    @ViewById(R.id.ll_free)
    LinearLayout ll_free;

    @ViewById(R.id.btn_free)
    Button btn_free;

    @ViewById(R.id.ll_fete)
    LinearLayout ll_fete;

    @ViewById(R.id.btn_fete)
    Button btn_fete;

    @ViewById(R.id.tv_fete)
    TextView tv_fete;

    @ViewById(R.id.btn_clear_discount)
    Button btn_clear;

    @ViewById(R.id.tv_free_hint)
    TextView tv_free_hint;

    @ViewById(R.id.dinner_discount_gridView)
    public WrapGridView gridView_discount;

    private DinnerDiscountAdapter discountAdapter;

    private DishDataItem mDishDataItem;

    private int HAVENO_SELECT = 0;
        private final int ALL_MODE = 1;
        private final int BATCH_MODE = 2;


    private DiscountType currentDiscountType = DiscountType.BATCHDISCOUNT;

    private int privilegeMode;

    private List<DiscountShopVo> discountList = new ArrayList<DiscountShopVo>();

        private boolean isNeedRemove = false;

        public static final double DISCOUNT_FREE_VALUE = 0d;

    private CustomDiscountDialog dialog;
    private FragmentActivity mActivity;

    private int currentFreePosition = -1;

    public BeautySingleDiscountView(Context context,DishDataItem dishDataItem) {
        super(context);
        mActivity = (FragmentActivity) context;
        this.mDishDataItem=dishDataItem;
    }

    @AfterViews
    void init() {
        discountAdapter = new DinnerDiscountAdapter(mActivity, discountList);
        discountAdapter.setItemBg(R.drawable.beauty_dish_type_item_bg);
        gridView_discount.setAdapter(discountAdapter);
        if (Utils.isEmpty(discountList)) {
            gridView_discount.setVisibility(View.GONE);
        } else {
            gridView_discount.setVisibility(View.VISIBLE);
        }
        gridView_discount.setOnItemClickListener(this);
        isBatchDiscountMode();
    }


    void loadDefaultDiscount(DiscountType discountType) {
                changeTab(discountType);
    }

    public void loadDatabaseDicount(DiscountType discountType) {
                if (discountType == DiscountType.ALL_FREE || discountType == DiscountType.BATCH_GIVE || discountType == DiscountType.BATCH_PROBLEM) {
            discountAdapter.resetData(null);
            gridView_discount.setVisibility(View.GONE);
            return;
        }
        loadData(discountType);
    }

    @Background
    public void loadData(DiscountType discountType) {
        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        try {
            List<DiscountShop> discountShopList = tradeDal.findDiscountByType(discountType);
            resetAdaper(discountShopList);
        } catch (Exception e) {
                        e.printStackTrace();
        }
    }

    @UiThread
    public void resetAdaper(List<DiscountShop> discountShopList) {
        discountAdapter.resetData(buildDiscountListVo(discountShopList));
        if (Utils.isEmpty(discountShopList)) {
            gridView_discount.setVisibility(View.GONE);
        } else {
            gridView_discount.setVisibility(View.VISIBLE);
        }
    }


    private List<DiscountShopVo> buildDiscountListVo(List<DiscountShop> discountShopList) {
        if (discountShopList == null) {
            return null;
        }
        discountList.clear();
        for (DiscountShop discountShop : discountShopList) {
            DiscountShopVo discountShopVo = new DiscountShopVo();
            discountShopVo.setDiscountShop(discountShop);
            discountShopVo.setSelected(false);
            discountList.add(discountShopVo);
        }
        return discountList;
    }


    private void isBatchDiscountMode() {
        privilegeMode = BATCH_MODE;
        changeTab(DiscountType.BATCHDISCOUNT);
            }

        private void changeTab(DiscountType type) {
        currentDiscountType = type;
        resetTabSelected(type);
        loadDatabaseDicount(currentDiscountType);
    }

        private void resetTabSelected(DiscountType type) {
                if (type == DiscountType.ALLDISCOUNT || type == DiscountType.BATCHDISCOUNT) {
            tab_discount.setSelected(true);
            tab_let.setSelected(false);
            tab_free.setSelected(false);
            btn_user_define.setText(R.string.user_define_discount);
            setFreeBtnVisible(false);
            changeItemCanSelected(false);
        } else if (type == DiscountType.ALL_FREE || type == DiscountType.BATCH_GIVE) {
            setFreeBtnVisible(true);
            tab_discount.setSelected(false);
            tab_let.setSelected(false);
            tab_free.setSelected(true);
        } else if (type == DiscountType.BATCH_PROBLEM) {
            tab_discount.setSelected(false);
            tab_let.setSelected(false);
            tab_free.setSelected(false);
            btn_user_define.setText(R.string.user_define_let);
            setFreeBtnVisible(false);
        } else {
            tab_discount.setSelected(false);
            tab_let.setSelected(true);
            tab_free.setSelected(false);
            btn_user_define.setText(R.string.user_define_let);
            setFreeBtnVisible(false);
            changeItemCanSelected(false);
        }
                if (privilegeMode == ALL_MODE) {
            tab_free.setText(R.string.freemeal);
            btn_free.setText(R.string.freemeal);
            tv_free_hint.setText(R.string.tv_free_hint);
        } else {
            DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().setDishTradePrivilege(null);
            tab_free.setText(R.string.give);
            btn_free.setText(R.string.give);
            tv_free_hint.setText(R.string.tv_give_hint);
        }

    }

    @Click({
            R.id.btn_clear_discount,
            R.id.dinner_tab_discount,
            R.id.dinner_tab_let,
            R.id.btn_user_define,
            R.id.dinner_tab_free,
            R.id.btn_free,
            R.id.btn_fete})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear_discount:
                unselectAllDiscount();
                if (privilegeMode == ALL_MODE) {
                    DinnerShopManager.getInstance().getShoppingCart().removeOrderPrivilege();
                    if (currentDiscountType == DiscountType.ALL_FREE) {
                        DinnerShopManager.getInstance().getShoppingCart().removeBanquet();
                    }
                } else {
                    DinnerShopManager.getInstance().getShoppingCart().removedPrivilege(mDishDataItem.getBase().getUuid());
                    DinnerShopManager.getInstance().getShoppingCart().setDishPrivilege(null, null,null,mDishDataItem.getBase().getUuid());
                                        if (DinnerShopManager.getInstance().getLoginCustomer() != null) {
                        DinnerShopManager.getInstance().getShoppingCart().memberPrivilegeForSelected();
                    }
                }

                break;
            case R.id.dinner_tab_discount:
                if (privilegeMode == ALL_MODE) {
                    changeTab(DiscountType.ALLDISCOUNT);
                } else {
                    changeTab(DiscountType.BATCHDISCOUNT);
                }
                break;
            case R.id.dinner_tab_let:
                if (privilegeMode == ALL_MODE) {
                    changeTab(DiscountType.ALLLET);
                } else {
                    changeTab(DiscountType.BATCHLET);
                }
                break;
            case R.id.dinner_tab_free:
                if (privilegeMode == ALL_MODE) {
                    changeTab(DiscountType.ALL_FREE);
                    changeItemCanSelected(false);
                } else {
                    changeTab(DiscountType.BATCH_GIVE);
                    changeItemCanSelected(false);
                }
                break;
            case R.id.btn_user_define:                 unselectAllDiscount();
                if (privilegeMode == BATCH_MODE && mDishDataItem==null) {
                    ToastUtil.showLongToast(R.string.pleanse_select_discount);
                    return;
                }
                                if (MathManualMarketTool.isHasTradePlan(DinnerShopManager.getInstance().getShoppingCart().getOrder()) && privilegeMode == ALL_MODE) {
                    showNoDicountDialog();
                    return;
                }
                int disocuntType = CustomDiscountDialog.TYPE_DISCOUNT;
                float amount = 0;
                if (!isDiscout()) {
                    amount = getCompareAmount().floatValue();
                    disocuntType = CustomDiscountDialog.TYPE_REBATE;
                }
                dialog = CustomDiscountDialog.newInstance(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT,
                        BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_REBATE, disocuntType, amount, currentDiscountType == DiscountType.BATCH_PROBLEM);

                dialog.setListener(discountListener);
                dialog.show(mActivity.getSupportFragmentManager(), "DinnerDiscount");
                break;
            case R.id.btn_free:
                                MobclickAgentEvent.onEvent(UserActionCode.ZC030014);
                if (MathManualMarketTool.isHasTradePlan(DinnerShopManager.getInstance().getShoppingCart().getOrder()) && privilegeMode == ALL_MODE) {
                    showNoDicountDialog();
                    return;
                }
                validateFreePermission();
                break;
            case R.id.btn_fete:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030015);
                                VerifyHelper.verifyAlert(mActivity, BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_BANQUET,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                DialogUtil.showHintConfirmDialog(mActivity.getSupportFragmentManager(), R.string.fete_notice, R.string.common_submit,
                                        R.string.common_cancel,
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                doBanquet();
                                            }
                                        }, null, "fete_confirm");
                            }
                        });
                break;
            default:
                break;
        }
    }

    private void doBanquet() {
        setReasonDialog(ReasonType.TRADE_BANQUET, new OrderCenterOperateDialogFragment.OperateListener() {
            @Override
            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                DinnerShopManager.getInstance().getShoppingCart().doBanquet(result.reason);
                return true;
            }
        });
    }


    private void changeItemCanSelected(boolean isBatchFree) {
    }

        private void setFreeBtnVisible(boolean isVisible) {
        if (isVisible) {
            ll_free.setVisibility(View.VISIBLE);
            btn_user_define.setVisibility(View.GONE);
        } else {
            ll_free.setVisibility(View.GONE);
            btn_user_define.setVisibility(View.VISIBLE);
        }
    }

    CustomDiscountDialog.CustomDiscountListener discountListener = new CustomDiscountDialog.CustomDiscountListener() {

        @Override
        public void onCustomDiscount(final float discount) {
                                    if (privilegeMode == BATCH_MODE && mDishDataItem==null) {
                ToastUtil.showLongToast(R.string.pleanse_select_discount);
                return;
            }
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try {

                        if (mActivity == null || mActivity.isDestroyed()) {
                            return;
                        }
                        setReasonResult(String.valueOf(discount), getUserDefinedPrivilegeName(discount));
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            };
            BigDecimal discountValue = new BigDecimal(String.valueOf(discount));
            validatePermission(getDiscountPerName(), discountValue, runnable);
        }
    };


    private String getDiscountPerName() {
        if (currentDiscountType == DiscountType.ALLLET || currentDiscountType == DiscountType.BATCHLET) {
            return BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_REBATE;
        } else {
            return BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT;
        }
    }


        void afterTextChange(TextView tv, Editable s) {
                if (TextUtils.isEmpty(s) && isNeedRemove) {
            if (privilegeMode == ALL_MODE) {
                DinnerShopManager.getInstance().getShoppingCart().removeOrderPrivilege();
            } else if (privilegeMode == BATCH_MODE) {
                DinnerShopManager.getInstance().getShoppingCart().setDishPrivilege(null, null);
                DinnerShopManager.getInstance().getShoppingCart().removeAllSelectedPrivilege();
            }
            isNeedRemove = true;
            return;
        }
        clearDiscountItems();
        PrivilegeType privilegeType = getPrivilegeType();
        if (!TextUtils.isEmpty(s) && !s.toString().equals("")) {
            String value = s.toString().trim();
            if (!checkDiscountIsvalid(value, privilegeType)) {
                String inputValue = s.toString().substring(0, s.toString().length() - 1);
                setText(tv, inputValue);
                return;
            }
            if (s.toString().equals("0") || s.toString().equals("0.")) {
                return;
            }
        } else {
            return;
        }

                if (s.toString().startsWith("")) {
            setText(tv, "0" + s);
            return;
        }

        buildPrivilege(s.toString(), getUserDefinedPrivilegeName(0), privilegeType, null);

    }


    @FocusChange({R.id.btn_user_define})
    void focusChange() {
        if (btn_user_define.isFocused())
            unselectAllDiscount();
    }


    private PrivilegeType getPrivilegeType() {
        if (currentDiscountType == DiscountType.BATCH_PROBLEM) {
            return PrivilegeType.PROBLEM;
        }

        if (isDiscout()) {
            return PrivilegeType.DISCOUNT;
        } else {
            return PrivilegeType.REBATE;
        }
    }


    private void setText(TextView tv, CharSequence text) {
        tv.setText(text);
        if (tv instanceof EditText && text != null && text.length() > 0) {
            try {
                ((EditText) tv).setSelection(text.length());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }



    private void unselectAllDiscount() {
        clearDiscountItems();
    }


    private boolean checkDiscountIsvalid(String privilege, PrivilegeType mPrivilegeType) {
        DinnerShoppingCart shoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        Double privilegeValue = Double.parseDouble(privilege);
                if (privilegeMode == BATCH_MODE && mPrivilegeType == PrivilegeType.REBATE) {
            if (!Utils.checkPointTwoIndex(privilege)) {
                ToastUtil.showShortToast(R.string.inputTwoADecimal);
                return false;
            }

            BigDecimal minPrice = shoppingCart.getMinPrice(shoppingCart.getShoppingCartVo());
            BigDecimal saleAmount = shoppingCart.getOrder().getTrade().getSaleAmount();

            BigDecimal value = new BigDecimal(privilege);

            if (minPrice != null && value.compareTo(minPrice) >= 0) {
                ToastUtil.showShortToast(R.string.privilegeError);
                return false;
            }

        } else if (privilegeMode == ALL_MODE && mPrivilegeType == PrivilegeType.REBATE) {            if (!Utils.checkPointTwoIndex(privilege)) {
                ToastUtil.showShortToast(R.string.inputTwoADecimal);
                return false;
            }
            BigDecimal value = new BigDecimal(privilege);
            BigDecimal tradeAmount = shoppingCart.getTradeAmoutCanDiscount(shoppingCart.getShoppingCartVo());
            if (value.compareTo(tradeAmount) >= 0) {
                ToastUtil.showShortToast(R.string.privilegeError);
                return false;
            }

        } else if (mPrivilegeType == PrivilegeType.DISCOUNT) {
                        if (!Utils.checkPointOneIndex(privilege)) {
                ToastUtil.showShortToast(R.string.inputADecimal);
                return false;
            }
                        if (privilegeValue < 0) {
                return false;
            } else if (privilegeValue >= 10) {
                ToastUtil.showShortToast(R.string.discountError);
                return false;
            }

        }

        return true;
    }


    private BigDecimal getCompareAmount() {
        DinnerShoppingCart shoppingCart = DinnerShopManager.getInstance().getShoppingCart();
                if (privilegeMode == BATCH_MODE) {
            BigDecimal minPrice = mDishDataItem.getBase().getActualAmount();
            return minPrice;
        } else if (privilegeMode == ALL_MODE) {            BigDecimal tradeAumont = mDishDataItem.getBase().getActualAmount();
            return tradeAumont;
        }
        return BigDecimal.ZERO;
    }


    private Boolean setFreePrivilege(PrivilegeType type, Reason mReason, boolean isDefine) {
        if (isDefine) {
            buildPrivilege("0", mActivity.getString(R.string.freethisOrder), type, mReason);
        } else {
            buildPrivilege("0", mActivity.getString(R.string.give), type, mReason);
        }
        return true;
    }

    private void buildPrivilege(String privateValue, String privilegeName, PrivilegeType mPrivilegeType, Reason mReason) {

        if (!checkDiscountIsvalid(privateValue, mPrivilegeType)) {
            return;
        }

        BigDecimal privilegeValue = new BigDecimal(String.valueOf(privateValue));

        if (mPrivilegeType == PrivilegeType.DISCOUNT) {
                        privilegeValue = getReviseValue(privilegeValue);
        }
        TradePrivilege privilege = new TradePrivilege();
        privilege.setPrivilegeType(mPrivilegeType);
        privilege.setPrivilegeValue(privilegeValue);
        privilege.setPrivilegeName(privilegeName);
        if (privilegeMode == ALL_MODE) {
            DinnerShopManager.getInstance().getShoppingCart().setDefineTradePrivilege(privilege, mReason, true, true);
        } else {
            OperateType operateType = DiscountType.getOperateType(currentDiscountType);
            DinnerShopManager.getInstance().getShoppingCart().setDishPrivilege(privilege, mReason, operateType,mDishDataItem.getBase().getUuid());
        }
    }

    private void setReasonResult(final String privateValue, final String privilegeName) {
        ReasonType reasonType = DiscountType.getReasonType(currentDiscountType);
        setReasonDialog(reasonType, new OrderCenterOperateDialogFragment.OperateListener() {
            @Override
            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                final PrivilegeType privilegeType = getPrivilegeType();
                buildPrivilege(privateValue, privilegeName, privilegeType, result.reason);
                return true;
            }
        });
    }

    private void setReasonDialog(ReasonType type, OrderCenterOperateDialogFragment.OperateListener listener) {
        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type.value());
        dialog.setArguments(bundle);
        dialog.registerListener(listener);
        dialog.registerCloseListener(new OrderCenterOperateDialogFragment.OperateCloseListener() {

            @Override
            public void onClose(OrderCenterOperateDialogFragment.OperateResult result) {
            }
        });
        dialog.show(mActivity.getSupportFragmentManager(), "discount");
    }

        private void clearDiscountItems() {
        discountAdapter.changeSelected(-1);
    }


    private BigDecimal getFreeDiscountLe() {
                if (!isDiscout()) {
            BigDecimal tradeAumont = DinnerShopManager.getInstance().getShoppingCart().getOrder().getTrade().getTradeAmount();
            return tradeAumont;
        } else {
            return new BigDecimal(0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        DinnerShoppingCart shoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        final DiscountShopVo discountShopVo = discountList.get(position);
                if (MathManualMarketTool.isHasTradePlan(shoppingCart.getOrder()) && privilegeMode == ALL_MODE) {
            showNoDicountDialog();
            return;
        }
                if (privilegeMode == BATCH_MODE && shoppingCart.getShoppingCartVo().getDinnerListShopcartItem().isEmpty()) {
            ToastUtil.showLongToast(R.string.pleanse_select_discount);
            return;
        }
        validateCommonPermission(discountShopVo, position);
    }


    private void validateFreePermission() {
                if (privilegeMode == BATCH_MODE && mDishDataItem==null) {
            ToastUtil.showLongToast(R.string.pleanse_select_discount);
            return;
        }
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                                        if (privilegeMode == ALL_MODE) {
                        setFreePrivilege(PrivilegeType.FREE, null, true);
                    } else {
                                                discountAdapter.changeSelected(currentFreePosition);
                        setFreePrivilege(PrivilegeType.GIVE, null, false);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        };
        String permissionCode;
        if (privilegeMode == ALL_MODE) {
            permissionCode = BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_FREE;
            VerifyHelper.verifyAlert(mActivity, permissionCode, new VerifyHelper.Callback() {
                @Override
                public void onPositive(User user, String code, Auth.Filter filter) {
                    super.onPositive(user, code, filter);
                    runnable.run();
                }
            });
        } else {
            permissionCode = BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_PRESENT;
            VerifyHelper.verifyAlert(mActivity, permissionCode, new VerifyHelper.Callback() {
                @Override
                public void onPositive(User user, String code, Auth.Filter filter) {
                    super.onPositive(user, code, filter);
                    runnable.run();
                }
            });
        }


    }


    private void validateCommonPermission(final DiscountShopVo discountShopVo, final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                                doClick(discountShopVo, position);
            }
        };
        String name = getDiscountPerName();
        BigDecimal discountValue;
        if (discountShopVo.getDiscountType() == DiscountShopVo.FREE) {
            discountValue = getFreeDiscountLe();
        } else {
            discountValue = new BigDecimal(discountShopVo.getDiscountShop().getContent().toString());
        }
        validatePermission(name, discountValue, runnable);
    }


    private BigDecimal getReviseValue(BigDecimal discountValue) {
        discountValue = MathDecimal.mul(discountValue, 10);
        return discountValue;
    }


    private boolean isDiscout() {
        if (currentDiscountType == DiscountType.ALLDISCOUNT || currentDiscountType == DiscountType.BATCHDISCOUNT) {
            return true;
        }
        return false;
    }


    private void validatePermission(final String discountName, final BigDecimal value, final Runnable runnable) {
        Auth.Filter filter;
        String permission;
        if (isDiscout()) {
            if (Session.getAuthVersion() == SessionImpl.AV1) {
                filter = VerifyHelper.createMin(discountName, value);
            } else {
                filter = VerifyHelper.createMin(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT, value);
            }
            permission = BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT;
        } else {
            if (Session.getAuthVersion() == SessionImpl.AV1) {
                filter = VerifyHelper.createMax(discountName, value);
            } else {
                filter = VerifyHelper.createMax(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_REBATE, value);
            }
            permission = BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_REBATE;
        }
        VerifyHelper.verifyAlert(mActivity, permission, new VerifyHelper.Callback() {
            @Override
            public void onPositive(User user, String code, Auth.Filter filter) {
                super.onPositive(user, code, filter);
                PrivilegeType privilegeType = getPrivilegeType();
                buildPrivilege(String.valueOf(value), getUserDefinedPrivilegeName(value.floatValue()), privilegeType, null);
            }
        });
    }

    private void doClick(DiscountShopVo discountShopVo, int position) {
        discountAdapter.changeSelected(position);
        final DiscountShop discountShop = discountShopVo.getDiscountShop();
        setReasonResult(discountShop.getContent().toString(), discountShop.getName());

                unselectAllDiscount();
    }


    private String getUserDefinedPrivilegeName(float discount) {
        String name = null;
        if (currentDiscountType == DiscountType.ALLDISCOUNT) {
            name = String.format(mActivity.getString(R.string.order_discount_format), String.valueOf(discount));
        } else if (currentDiscountType == DiscountType.ALLLET) {
            name = mActivity.getString(R.string.dinner_all_discount_let);
        } else if (currentDiscountType == DiscountType.BATCHDISCOUNT) {
            name = String.format(mActivity.getString(R.string.dish_discount_format), String.valueOf(discount));
        } else if (currentDiscountType == DiscountType.BATCHLET) {
            name = mActivity.getString(R.string.dish_rebate);
        }
        return name;
    }

    private void showNoDicountDialog() {
        new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance()).title(getResources().getString(R.string.dinner_discountandplanhint))
                .iconType(R.drawable.commonmodule_dialog_icon_warning)
                .negativeText(R.string.know)
                .negativeLisnter(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                })
                .build()
                .show(mActivity.getSupportFragmentManager(), TAG);
    }
}
