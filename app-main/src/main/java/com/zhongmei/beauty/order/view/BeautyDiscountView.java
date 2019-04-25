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

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.enums.DiscountType;
import com.zhongmei.bty.basemodule.session.SessionImpl;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathManualMarketTool;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.common.view.CustomDiscountDialog;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.WrapGridView;
import com.zhongmei.bty.dinner.action.ActionDinnerBatchDiscount;
import com.zhongmei.bty.dinner.action.ActionDinnerBatchFree;
import com.zhongmei.bty.dinner.action.ActionDinnerFinished;
import com.zhongmei.bty.dinner.adapter.DinnerDiscountAdapter;
import com.zhongmei.bty.dinner.vo.DiscountShopVo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;

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


/**
 * 折扣自定义控件
 */
@EViewGroup(R.layout.beauty_discount_layout)
public class BeautyDiscountView extends LinearLayout implements OnItemClickListener {
    private static final String TAG = BeautyDiscountView.class.getSimpleName();

    @ViewById(R.id.dinner_all_discount)
    public Button dinner_all_discount;

    @ViewById(R.id.dinner_batch_discount)
    public Button dinner_batch_discount;

    @ViewById(R.id.btn_user_define)
    Button btn_user_define;

    @ViewById(R.id.dinner_tab_discount)
    TextView tab_discount;

    @ViewById(R.id.dinner_tab_let)
    TextView tab_let;
    //赠送
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

    private int HAVENO_SELECT = 0;
    //整单折扣或者折让或赠送
    private final int ALL_MODE = 1;
    //	批量折扣或者折让
    private final int BATCH_MODE = 2;


    private DiscountType currentDiscountType = DiscountType.ALLDISCOUNT;

    private int privilegeMode;

    private List<DiscountShopVo> discountList = new ArrayList<DiscountShopVo>();

    // edittext 设置时，是否需要移除折扣
    private boolean isNeedRemove = false;

    // 此值时为赠送
    public static final double DISCOUNT_FREE_VALUE = 0d;

    private CustomDiscountDialog dialog;
    private FragmentActivity mActivity;

    private int currentFreePosition = -1;

    public BeautyDiscountView(Context context,DiscountType modeType) {
        super(context);
        mActivity = (FragmentActivity) context;
        currentDiscountType = modeType;
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
        isAllDiscountMode();
//        loadDefaultDiscount(currentDiscountType);
    }


    void loadDefaultDiscount(DiscountType discountType) {
        // TODO Auto-generated method stub
        changeTab(discountType);
    }

    public void loadDatabaseDicount(DiscountType discountType) {
        //赠送
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
            // TODO Auto-generated catch block
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

    private void isAllDiscountMode() {
        privilegeMode = ALL_MODE;
        dinner_all_discount.setSelected(true);
        dinner_batch_discount.setSelected(false);
        changeTab(DiscountType.ALLDISCOUNT);
        // 通知购物车刷新视图
        EventBus.getDefault().post(new ActionDinnerBatchDiscount(false, true));
    }

    private void isBatchDiscountMode() {
        privilegeMode = BATCH_MODE;
        dinner_all_discount.setSelected(false);
        dinner_batch_discount.setSelected(true);
        changeTab(DiscountType.BATCHDISCOUNT);
        // 通知购物车刷新视图
        EventBus.getDefault().post(new ActionDinnerBatchDiscount(true, false));
    }

    // 改变折扣 让价标签
    private void changeTab(DiscountType type) {
        currentDiscountType = type;
        resetTabSelected(type);
        loadDatabaseDicount(currentDiscountType);
    }

    // 改变标签文字和选择状态
    private void resetTabSelected(DiscountType type) {
        // TODO Auto-generated method stub
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
        //改变title文字
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

    @Click({R.id.dinner_all_discount,
            R.id.dinner_batch_discount,
            R.id.btn_clear_discount,
            R.id.dinner_tab_discount,
            R.id.dinner_tab_let,
            R.id.btn_user_define,
            R.id.dinner_tab_free,
            R.id.btn_free,
            R.id.btn_fete})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.dinner_all_discount:
                privilegeMode = HAVENO_SELECT;
                unselectAllDiscount();
                isAllDiscountMode();
                break;
            case R.id.dinner_batch_discount:
                privilegeMode = HAVENO_SELECT;
                // 清除之前设置的值
                DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().setDishTradePrivilege(null);
                unselectAllDiscount();
                isBatchDiscountMode();
                break;
            case R.id.btn_clear_discount:
                unselectAllDiscount();
                if (privilegeMode == ALL_MODE) {
                    DinnerShopManager.getInstance().getShoppingCart().removeOrderPrivilege();
                    if (currentDiscountType == DiscountType.ALL_FREE) {
                        DinnerShopManager.getInstance().getShoppingCart().removeBanquet();
                    }
                } else {
                    DinnerShopManager.getInstance().getShoppingCart().removeAllSelectedPrivilege();
                    DinnerShopManager.getInstance().getShoppingCart().setDishPrivilege(null, null);
                    // 自动添加会员折扣
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
//                    ll_fete.setVisibility(View.VISIBLE);
                } else {
                    changeTab(DiscountType.BATCH_GIVE);
                    changeItemCanSelected(false);
//                    ll_fete.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_user_define:
                unselectAllDiscount();
                if (privilegeMode == BATCH_MODE && DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().getDinnerListShopcartItem().isEmpty()) {
                    ToastUtil.showLongToast(R.string.pleanse_select_discount);
                    return;
                }
                //输入折扣
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
                //赠送、免单
                MobclickAgentEvent.onEvent(UserActionCode.ZC030014);
                if (MathManualMarketTool.isHasTradePlan(DinnerShopManager.getInstance().getShoppingCart().getOrder()) && privilegeMode == ALL_MODE) {
                    showNoDicountDialog();
                    return;
                }
                validateFreePermission();
                break;
            case R.id.btn_fete:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030015);
                //宴请按钮
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

    /**
     * @Title: changeItemState
     * @Description: 点击批量赠送按钮时，让不打折商品也可以选
     * @Param TODO
     * @Return void 返回类型
     */
    private void changeItemCanSelected(boolean isBatchFree) {
        EventBus.getDefault().post(new ActionDinnerBatchFree(isBatchFree));
    }

    //控制按钮显示隐藏
    private void setFreeBtnVisible(boolean isVisible) {
        if (isVisible) {
            ll_free.setVisibility(View.VISIBLE);
//            ll_fete.setVisibility(View.VISIBLE);
            btn_user_define.setVisibility(View.GONE);
//			btn_clear.setVisibility(View.GONE);
        } else {
            ll_free.setVisibility(View.GONE);
//            ll_fete.setVisibility(View.GONE);
            btn_user_define.setVisibility(View.VISIBLE);
//			btn_clear.setVisibility(View.VISIBLE);
        }
    }

    CustomDiscountDialog.CustomDiscountListener discountListener = new CustomDiscountDialog.CustomDiscountListener() {

        @Override
        public void onCustomDiscount(final float discount) {
            // TODO Auto-generated method stub
            //没有选中菜品不能点击打折
            if (privilegeMode == BATCH_MODE && DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().getDinnerListShopcartItem().isEmpty()) {
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

    /**
     * @Title: getDiscountPerName
     * @Description: 获得折扣权限需要的名字
     * @Param @return TODO
     * @Return String 返回类型
     */
    private String getDiscountPerName() {
        if (currentDiscountType == DiscountType.ALLLET || currentDiscountType == DiscountType.BATCHLET) {
            return BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_REBATE;
        } else {
            return BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT;
        }
    }

    /**
     * @Title: afterTextChange
     * @Description: TODO
     * @Param @param tv
     * @Param @param s TODO
     * @Return void 返回类型
     * @deprecated 以前的计算方式，未使用
     */
    // @AfterTextChange({R.id.user_define})
    void afterTextChange(TextView tv, Editable s) {
        // 手动清空自定义输入框时，移除优惠
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

        // 小数点开始的，先补零
        if (s.toString().startsWith("")) {
            setText(tv, "0" + s);
            return;
        }

        buildPrivilege(s.toString(), getUserDefinedPrivilegeName(0), privilegeType, null);

    }

    /**
     * 当用户点击输入框后其它按钮的选中状态清空
     *
     * @Title: focusChange
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    @FocusChange({R.id.btn_user_define})
    void focusChange() {
        if (btn_user_define.isFocused())
            unselectAllDiscount();
    }

    /**
     * @Title: getPrivilegeType
     * @Description: 根据整单、批量的标签切换折扣，返回折扣类型
     * @Param @return TODO
     * @Return PrivilegeType 返回类型
     */
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

    /**
     * 设置文字，并将光标移动到末尾
     *
     * @param tv
     * @param text
     */
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


    /**
     * 取消所有折扣按钮的选中状态
     */
    private void unselectAllDiscount() {
        clearDiscountItems();
    }

    /**
     * @Title: checkDiscountIsvalid
     * @Description: TODO
     * @Param @param cs
     * @Param @param isDiscount:true打折操作.false:让价操作
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    private boolean checkDiscountIsvalid(String privilege, PrivilegeType mPrivilegeType) {
        DinnerShoppingCart shoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        Double privilegeValue = Double.parseDouble(privilege);
        // 批量折扣和让价
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

        } else if (privilegeMode == ALL_MODE && mPrivilegeType == PrivilegeType.REBATE) {// 整单让价
            if (!Utils.checkPointTwoIndex(privilege)) {
                ToastUtil.showShortToast(R.string.inputTwoADecimal);
                return false;
            }
            BigDecimal value = new BigDecimal(privilege);
            BigDecimal tradeAmount = shoppingCart.getTradeAmoutCanDiscount(shoppingCart.getShoppingCartVo());
            if (value.compareTo(tradeAmount) >= 0) {
                ToastUtil.showShortToast(R.string.privilegeError);
                return false;
            }

        } else if (mPrivilegeType == PrivilegeType.DISCOUNT) {// 单菜、整单折扣

            // 验证输入格式
            if (!Utils.checkPointOneIndex(privilege)) {
                ToastUtil.showShortToast(R.string.inputADecimal);
                return false;
            }
            //折扣在0，到10之间
            if (privilegeValue < 0) {
                return false;
            } else if (privilegeValue >= 10) {
                ToastUtil.showShortToast(R.string.discountError);
                return false;
            }

        }

        return true;
    }

    /**
     * @Title: getCompareAmount
     * @Description: 获得需要比较的价格, 只用于输入的折让
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    private BigDecimal getCompareAmount() {
        DinnerShoppingCart shoppingCart = DinnerShopManager.getInstance().getShoppingCart();
        // 批量时让价最大金额
        if (privilegeMode == BATCH_MODE) {
            BigDecimal minPrice = shoppingCart.getMinPrice(shoppingCart.getShoppingCartVo());
            return minPrice;
        } else if (privilegeMode == ALL_MODE) {// 整单让价
            BigDecimal tradeAumont = shoppingCart.getTradeAmoutCanDiscount(shoppingCart.getShoppingCartVo());
            return tradeAumont;
        }
        return BigDecimal.ZERO;
    }

    /**
     * @param type
     * @param mReason
     * @param isDefine 是否整单
     * @return
     */
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
            // 因折扣信息保存格式为：8折-80 所以需要x10
            privilegeValue = getReviseValue(privilegeValue);
        }
        TradePrivilege privilege = new TradePrivilege();
        privilege.setPrivilegeType(mPrivilegeType);
        privilege.setPrivilegeValue(privilegeValue);
        privilege.setPrivilegeName(privilegeName);//传折扣名称给购物车

        if (privilegeMode == ALL_MODE) {
            DinnerShopManager.getInstance().getShoppingCart().setDefineTradePrivilege(privilege, mReason, true, true);
        } else {
            OperateType operateType = DiscountType.getOperateType(currentDiscountType);
            DinnerShopManager.getInstance().getShoppingCart().setDishPrivilege(privilege, mReason, operateType);
            EventBus.getDefault().post(new ActionDinnerFinished());
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

    // 清空自动生成的折扣的item选择
    private void clearDiscountItems() {
        discountAdapter.changeSelected(-1);
    }

    /**
     * @Title: getFreeDiscountLe
     * @Description: 获取折扣时能
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    private BigDecimal getFreeDiscountLe() {
        // 让价
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
        //免单、赠送权限
        if (MathManualMarketTool.isHasTradePlan(shoppingCart.getOrder()) && privilegeMode == ALL_MODE) {
            showNoDicountDialog();
            return;
        }
        //没有选中菜品不能点击打折
        if (privilegeMode == BATCH_MODE && shoppingCart.getShoppingCartVo().getDinnerListShopcartItem().isEmpty()) {
            ToastUtil.showLongToast(R.string.pleanse_select_discount);
            return;
        }
        validateCommonPermission(discountShopVo, position);
    }

    /**
     * @Title: validateFreePermission
     * @Description: 验证免单、赠送权限
     * @Param @param position TODO
     * @Return void 返回类型
     */
    private void validateFreePermission() {
        //没有选中菜品不能点击打折
        if (privilegeMode == BATCH_MODE && DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().getDinnerListShopcartItem().isEmpty()) {
            ToastUtil.showLongToast(R.string.pleanse_select_discount);
            return;
        }
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    //整单免单
                    if (privilegeMode == ALL_MODE) {
                        setFreePrivilege(PrivilegeType.FREE, null, true);
//                        setReasonDialog(ReasonType.TRADE_FREE, new OrderCenterOperateDialogFragment.OperateListener() {
//                            @Override
//                            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
//                                setFreePrivilege(PrivilegeType.FREE, result.reason, true);
//                                return true;
//                            }
//                        });
                    } else {
                        //批量赠送
                        discountAdapter.changeSelected(currentFreePosition);
                        setFreePrivilege(PrivilegeType.GIVE, null, false);
//                        setReasonDialog(ReasonType.ITEM_GIVE, new OrderCenterOperateDialogFragment.OperateListener() {
//                            @Override
//                            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
//                                setFreePrivilege(PrivilegeType.GIVE, result.reason, false);
//                                return true;
//                            }
//                        });
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

    /**
     * @Title: validateCommonPermission
     * @Description: TODO
     * @Param @param discountShopVo TODO
     * @Return void 返回类型
     */
    private void validateCommonPermission(final DiscountShopVo discountShopVo, final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
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

    /**
     * @Title: getReviseValue
     * @Description: 获得修正后的折扣值 因折扣信息保存格式为：8折-80 所以需要x10
     * @Param @param discountValue
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    private BigDecimal getReviseValue(BigDecimal discountValue) {
        discountValue = MathDecimal.mul(discountValue, 10);
        return discountValue;
    }

    /**
     * @Title: isDiscout
     * @Description: 判断是否是折扣（整单、批量）折扣
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    private boolean isDiscout() {
        if (currentDiscountType == DiscountType.ALLDISCOUNT || currentDiscountType == DiscountType.BATCHDISCOUNT) {
            return true;
        }
        return false;
    }

    /**
     * @Title: validatePermission
     * @Description: 折扣权限验证
     * @Param @param discountName
     * @Param @param value
     * @Param @param runnable TODO
     * @Return void 返回类型
     */
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
//                runnable.run();
                PrivilegeType privilegeType = getPrivilegeType();
                buildPrivilege(String.valueOf(value), getUserDefinedPrivilegeName(value.floatValue()), privilegeType, null);
            }
        });
    }

    private void doClick(DiscountShopVo discountShopVo, int position) {
        discountAdapter.changeSelected(position);
        final DiscountShop discountShop = discountShopVo.getDiscountShop();
        setReasonResult(discountShop.getContent().toString(), discountShop.getName());

        //选择完后要清空选择
        unselectAllDiscount();
    }

    /**
     * @return
     * @Date 2016年5月20日
     * @Description: 获取自定义优惠名称
     * @Return String
     */
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
