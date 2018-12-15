package com.zhongmei.bty.mobilepay.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.meituan.PaymentItemDishRelateImpl;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.bty.mobilepay.operate.TuanGouOperates;
import com.zhongmei.bty.mobilepay.operate.TuanGouOperatesImpl;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.views.MeiTuanCouponDishView;
import com.zhongmei.bty.mobilepay.bean.GroupPay;
import com.zhongmei.bty.mobilepay.bean.meituan.ICouponDishRelate;
import com.zhongmei.bty.mobilepay.bean.meituan.IGroupBuyingCoupon;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishItemVo;
import com.zhongmei.bty.mobilepay.bean.meituan.MeituanDishVo;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;
import com.zhongmei.bty.mobilepay.enums.TicketInfoStatus;
import com.zhongmei.bty.basemodule.pay.operates.PaymentItemDal;
import com.zhongmei.bty.mobilepay.utils.MeituanMathUtil;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.bty.basemodule.commonbusiness.view.CommonCheckCouponsView;

import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponReq;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.dialog.PayDepositPromptDialog;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.dialog.MeiTuanTicketDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 美团劵界面
 */
public class MeiTuanCouponFragment extends BasePayFragment implements View.OnClickListener, CommonCheckCouponsView.CouponsListener {

    private final String TAG = MeiTuanCouponFragment.class.getSimpleName();

    private final static int MAX_COUPON_SIZE = 1;


    private TextView mPayAlterTV;

    private Button mPay;

    private CommonCheckCouponsView mCheckCouponsView;

    private LinearLayout mInputLayout;

    private LinearLayout mEmptyLayout;

    private TextView mLimitTV;

    private LinearLayout contentLayout;

    private GroupPay mGroupPay;//已经加入的券金额

    private MeituanCouponDishAdapter mTuanGouCouponAdapter;

    private List<PaymentItemDishRelateImpl> mGroupDishes;//已经关联券的菜品

    private TuanGouOperates mCouponsOperates;

    private PaymentItemDal paymentItemDal;


    public static MeiTuanCouponFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        MeiTuanCouponFragment f = new MeiTuanCouponFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        mGroupPay = new GroupPay();
        mCouponsOperates = new TuanGouOperatesImpl(getActivity());
        paymentItemDal = OperatesFactory.create(PaymentItemDal.class);
        registerEventBus();
        if (!mPaymentInfo.isDinner() || mPaymentInfo.isOrderCenter()) {
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.pay_meituan_coupons_fragment, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        initView();
    }

    private void assignViews(View view) {
        mPayAlterTV = (TextView) view.findViewById(R.id.cash_pay_alerttext);
        mCheckCouponsView = (CommonCheckCouponsView) view.findViewById(R.id.check_coupon_view);
        mInputLayout = (LinearLayout) view.findViewById(R.id.input_layout);
        mEmptyLayout = (LinearLayout) view.findViewById(R.id.empty_layout);
        mLimitTV = (TextView) view.findViewById(R.id.pay_meituan_privilege_limit_tv);
        contentLayout = (LinearLayout) view.findViewById(R.id.coupon_dish_View_ll);
        mPay = (Button) view.findViewById(com.zhongmei.yunfu.mobilepay.R.id.pay);
    }

    public void initView() {
        if (mPaymentInfo.getTradeBusinessType() == BusinessType.DINNER) {
            mLimitTV.setVisibility(View.VISIBLE);
        } else {
            mLimitTV.setVisibility(View.GONE);
        }
        mPay.setOnClickListener(this);
        mTuanGouCouponAdapter = new MeituanCouponDishAdapter(this.getActivity(), contentLayout, mGroupPay, mTuanGouCouponListenser);
        mCheckCouponsView.setCouponsListener(this);
        mCheckCouponsView.setKeyBoardValue(true);
        mCheckCouponsView.getEdit().setInputType(InputType.TYPE_CLASS_NUMBER);
        mCheckCouponsView.getEdit().setHint(getString(R.string.pay_input_meituan_code_alter));
        mCheckCouponsView.getEdit().setHintTextColor(getResources().getColor(R.color.print_line_gray));
        // mCheckCouponsView.getEdit().setText("311374050992");
        mCheckCouponsView.getEdit().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                //限制输入长度16位
                if (!TextUtils.isEmpty(content)) {
                    mCheckCouponsView.getEdit().setSelection(content.length());
                    if (content.length() > 16) {
                        content = content.substring(0, content.length() - 1);
                        mCheckCouponsView.getEdit().setText(content);
                    }
                }
            }
        });
        updateView();
        findGroupDishs();
        // v8.6.0 快捷支付扫码界面默认弹出
        if (mPaymentInfo != null && mPaymentInfo.getQuickPayType() == IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCheckCouponsView.startScan();// modify v8.14
                    mPaymentInfo.setQuickPayType(-1);
                }
            });
        }
    }


    private void updateView() {
        if (!mGroupPay.isEmpty()) {
            mInputLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        } else {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mInputLayout.setVisibility(View.GONE);
        }
        updateNotPayMent();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            updateView();
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        } else {
            //隐藏时清空已输入金额
            clearInput();
           /* DisplayServiceManager.updateDisplay(getActivity().getApplicationContext(),
                    DisplayServiceManager.buildPayMessage(DisplayUserInfo.COMMAND_CACEL, ""));*/
        }
        super.onHiddenChanged(hidden);
    }

    IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOK, int statusCode) {
            try {
                if (isOK) {
                    //隐藏时清空已输入金额
                    clearInput();
                    updateView();
                    findGroupDishs();//查询菜品与券关联关系
                } else {
                    updateNotPayMent();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

    /**
     * @Title: updateNotPayMent
     * @Description: 刷新未支付或找零、溢收
     * @Return void 返回类型
     */
    private void updateNotPayMent() {
        if (mGroupPay.getGroupAmount() > 0) {
            double restAmout = getRestAmount();
            if (restAmout == 0) {
                mPayAlterTV.setVisibility(View.GONE);
            } else {
                //还剩
                if (restAmout > 0) {
                    mPayAlterTV.setText(this.getActivity().getString(R.string.pay_rest_payment_text) + CashInfoManager.formatCash(restAmout));
                } else {
                    //溢收
                    mPayAlterTV.setText(String.format(this.getActivity().getString(R.string.more_pay_cash_text), CashInfoManager.formatCash(0 - restAmout)));
                }
                mPayAlterTV.setVisibility(View.VISIBLE);
            }

        } else {
            mPayAlterTV.setVisibility(View.GONE);
        }
        DoPayUtils.updatePayEnable(getActivity(), mPay, enablePay());
    }

    //获取用户输入金额
    public double getInputValue() {
        return this.mGroupPay.getGroupAmount();
    }

    /*public boolean enablePay() {
        if (isSuportGroupPay()) {//如果支持组合支付
            return mGroupPay.getGroupAmount() > 0;
        } else {
            return mGroupPay.getGroupAmount() >= mPaymentInfo.getActualAmount();//不分步支付，输入金额必须大于等于应付金额
        }
    }*/

    private double getRestAmount() {
        return mPaymentInfo.getActualAmount() - mGroupPay.getGroupAmount();
    }

    //清空所有输入
    private void clearInput() {
        mGroupPay.clear();
        mPaymentInfo.getOtherPay().clear();
        mTuanGouCouponAdapter.updateData();
    }

    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            clearInput();
            updateView();
            //刷新副屏
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.pay == v.getId()) {
            if (!ClickManager.getInstance().isClicked()) {
                if (doPayChecked(false)) {
                    if (mGroupPay.getGroupAmount() > 0) {
                        final double restAmout = 0 - getRestAmount();//溢收金额
                        if (restAmout > 0) {//溢收
                            List<PayModelItem> items = mGroupPay.getAllPayModelItems();
                            //百糯券一张对应一个paymentitem
                            for (PayModelItem item : items) {
                                if (item.getTuanGouCouponDetail() != null && item.getTuanGouCouponDetail().getMarketPrice().doubleValue() < restAmout) {
                                    ToastUtil.showLongToast(getString(R.string.baidu_nuomi_coupon_not_support_more));
                                    return;
                                }
                            }
                            CommonDialogFragment.CommonDialogFragmentBuilder dialogBuilder = new CommonDialogFragment.CommonDialogFragmentBuilder(
                                    getActivity()).title(R.string.pay_more_attention_text)
                                    .iconType(CommonDialogFragment.ICON_WARNING)
                                    .positiveText(R.string.invoice_btn_ok)
                                    .positiveLinstner(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            mPaymentInfo.getOtherPay().setMorePayAmount(restAmout);
                                            doPay();
                                        }
                                    })
                                    .negativeText(R.string.invoice_btn_cancel);

                            dialogBuilder.build().show(this.getActivity().getSupportFragmentManager(), "paymoredialog");
                        } else {
                            doPay();
                        }
                    }
                }
            }
        }
    }

    private void doPay() {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_MEITUAN);
        mPay.setEnabled(false);
        mPaymentInfo.getOtherPay().clear();
        mPaymentInfo.getOtherPay().addPayModelItems(mGroupPay.getAllPayModelItems());
        if (mDoPayApi != null)
            mDoPayApi.doPay(getActivity(), mPaymentInfo, mPayOverCallback);
    }

    private TuanGouCouponListenser mTuanGouCouponListenser = new TuanGouCouponListenser() {
        @Override
        public void removeCoupon(PayModelItem item) {
            try {
                mGroupPay.removePayModelItem(item);
                mTuanGouCouponAdapter.updateData();
                updateView();
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };

    private boolean isEnableCoupon(IGroupBuyingCoupon tuanGouCouponDetail) {
        if (tuanGouCouponDetail != null) {
            if (tuanGouCouponDetail.getStatus() == TicketInfoStatus.NOT_USED.value() &&
                    tuanGouCouponDetail.getMaxConsume() > 0 &&
                    tuanGouCouponDetail.getMaxConsume() >= tuanGouCouponDetail.getMinConsume()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void getCouponsNo(String ticketNo) {
        if (this.mPaymentInfo.isNeedToPayDeposit()) {//验券前先判断是否交押金 add 20180202
            PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
        } else if (this.isNeedToDeductionEarnest()) {//验券前先判断是否抵扣预付金 add 8.14
            this.showBookingDeductionDialog();
        } else {
            if (!TextUtils.isEmpty(ticketNo) && Utils.isNum(ticketNo)) {
                //一次只能用一类
                if (mGroupPay.size() >= MAX_COUPON_SIZE) {
                    ToastUtil.showLongToastCenter(this.getActivity(), String.format(getResources().getString(R.string.coupon_size_limit_one_time), MAX_COUPON_SIZE + ""));
                    return;
                }
                if (mGroupPay.getPayModelItemByPayModeId(Long.valueOf(ticketNo)) != null) {
                    ToastUtil.showLongToast(getResources().getString(R.string.coupon_have_add_to_list));
                    return;
                }

                if (getRestAmount() <= 0) {
                    ToastUtil.showLongToastCenter(this.getActivity(), getResources().getString(R.string.pay_not_need_meituan_coupon_alter));
                    return;
                }
                requestCouponInfo(ticketNo);

            } else {
                ToastUtil.showLongToast(R.string.please_input_coupon_code);
            }
        }
    }


    private boolean isHavePrivilege() {
        if (mPaymentInfo != null) {
            if (mPaymentInfo.isDinner() && mPaymentInfo.getTradeVo() != null) {
                return mPaymentInfo.getTradeVo().isHavePrivilege();
            }
        }
        return false;
    }

    //显示设置券数量界面
    private void showSetCountDialog(final TuanGouCouponDetail meiTuanCouponDetail) {
        MeiTuanTicketDialog dialog = new MeiTuanTicketDialog(this.getActivity(), meiTuanCouponDetail.getMaxConsume(), mPaymentInfo.getNotPayMent(), meiTuanCouponDetail, mSetCouponsListenner);
        dialog.show();
    }

    //根据券号查询券信息
    private void requestCouponInfo(String serialNumber) {
        TuanGouCouponReq req = new TuanGouCouponReq(PayModeId.MEITUAN_TUANGOU.value(), serialNumber);
        req.setDishUuids(DoPayUtils.getTradeDishIds(mPaymentInfo.getTradeVo()));
        mCouponsOperates.getTuanGouCouponsDetail(req, LoadingResponseListener.ensure(mCheckCouponListener, this.getFragmentManager()));
    }

    private ResponseListener mCheckCouponListener = new ResponseListener<TuanGouCouponDetail>() {

        @Override
        public void onResponse(ResponseObject<TuanGouCouponDetail> response) {
            try {
                if (ResponseObject.isOk(response)) {//成功
                    {
                        TuanGouCouponDetail tuanGouCouponDetail = response.getContent();
                        //如果可用
                        if (isEnableCoupon(tuanGouCouponDetail)) {
                            //如果团单号相同，算同一券
                            if (mGroupPay.size() > 0 && mGroupPay.isContainsPayModel(PayModeId.MEITUAN_TUANGOU)) {
                                for (PayModelItem item : mGroupPay.getAllPayModelItems()) {
                                    if (item.getPayMode() == PayModeId.MEITUAN_TUANGOU && item.getTuanGouCouponDetail() != null) {
                                        if (tuanGouCouponDetail.getDealId() != null && tuanGouCouponDetail.getDealId().equals(item.getTuanGouCouponDetail().getDealId())) {
                                            ToastUtil.showLongToast(getResources().getString(R.string.coupon_have_add_to_list));
                                            return;
                                        }
                                    }
                                }
                            }
                            //正餐添加菜品限制
                            if (mPaymentInfo.getTradeBusinessType() == BusinessType.DINNER) {
                                //判断是否有优惠
                                if (isHavePrivilege()) {
                                    //如果有优惠已经支付,且有支付记录，不容许使用美团券
                                    if (mPaymentInfo.getPaidAmount() > 0) {
                                        {
                                            ToastUtil.showLongToast(R.string.coupon_use_limit_alter);
                                            return;
                                        }
                                    }
                                }
                                //直接移除所有优惠
                                if (mPaymentInfo.isSplit()) {
                                    SeparateShoppingCart.getInstance().removeAllTradeVoPrivileges();
                                } else {
                                    DinnerShoppingCart.getInstance().removeAllTradeVoPrivileges();
                                }
                                //如果有多张券可以选择，弹出券数量对话框
                                showSetCountDialog(tuanGouCouponDetail);
                            } else {
                                //显示设置券数量对话框
                                showSetCountDialog(tuanGouCouponDetail);
                            }
                        } else {
                            if (tuanGouCouponDetail != null)
                                ToastUtil.showLongToast(tuanGouCouponDetail.getStatusDesc());
                            else
                                ToastUtil.showLongToast(response.getMessage());
                        }
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }

        @Override
        public void onError(VolleyError error) {
            try {
                ToastUtil.showLongToast(error.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };
    //点击确定后的回调操作
    MeiTuanTicketDialog.SetMeiTuanCouponsListener mSetCouponsListenner = new MeiTuanTicketDialog.SetMeiTuanCouponsListener() {

        @Override
        public void setMeiTuanCoupons(TuanGouCouponDetail tuanGouCouponDetail, int usedCount) {
            checkCoupon(tuanGouCouponDetail, usedCount);
        }
    };

    //获取菜品与券关联关系
    public List<ICouponDishRelate> getCouponDishRelates() {
        List<ICouponDishRelate> couponDishRelateList = new ArrayList<ICouponDishRelate>();
        if (mGroupDishes != null && mGroupDishes.size() > 0) {
            couponDishRelateList.addAll(mGroupDishes);
        }
        if (mGroupPay.size() > 0) {
            MeituanDishVo meituanDishVo = null;
            for (PayModelItem item : mGroupPay.getAllPayModelItems()) {
                meituanDishVo = item.getMeituanDishVo();
                if (meituanDishVo.getCouponDishRelates() != null) {
                    couponDishRelateList.addAll(meituanDishVo.getCouponDishRelates());
                }
            }
        }
        return couponDishRelateList;
    }

    private void checkCoupon(TuanGouCouponDetail tuanGouCouponDetail, int usedCount) {
        if (mPaymentInfo.getTradeBusinessType() == BusinessType.DINNER) {
            //如果限制菜品
            if (tuanGouCouponDetail.getLimitDish() != null && tuanGouCouponDetail.getLimitDish().size() > 0) {
                MeituanDishVo meituanDishVo = MeituanMathUtil.matchAndMath(mPaymentInfo.getTradeVo(), tuanGouCouponDetail, usedCount, getCouponDishRelates());
                if (meituanDishVo == null || meituanDishVo.isMatchEnable == false) {
                    ToastUtil.showShortToast(R.string.coupon_is_not_enable_used);
                    return;
                }
                createMeituanPayModeItem(tuanGouCouponDetail, meituanDishVo.matchCount, meituanDishVo.matchAmount, meituanDishVo);
            } else {//如果不限制就取面值
                BigDecimal usedValue = tuanGouCouponDetail.getMarketPrice().multiply(new BigDecimal(usedCount));
                createMeituanPayModeItem(tuanGouCouponDetail, usedCount, usedValue, null);
            }
        } else {//快餐不用判断产品限制
            BigDecimal usedValue = tuanGouCouponDetail.getMarketPrice().multiply(new BigDecimal(usedCount));
            createMeituanPayModeItem(tuanGouCouponDetail, usedCount, usedValue, null);
        }
    }

    private void createMeituanPayModeItem(TuanGouCouponDetail tuanGouCouponDetail, int usedCount, BigDecimal usedAmount, MeituanDishVo meituanDishVo) {
        PayModelItem item = new PayModelItem(PayModeId.MEITUAN_TUANGOU, tuanGouCouponDetail);
        item.setUsedCount(usedCount);
        item.setUsedValue(usedAmount);
        if (meituanDishVo != null)
            item.setMeituanDishVo(meituanDishVo);
        mGroupPay.addPayModelItem(item);
        mTuanGouCouponAdapter.updateData();
        mCheckCouponsView.getEdit().setText("");
        updateView();
    }

    private void findGroupDishs() {
        if (mPaymentInfo != null && mPaymentInfo.getTradeVo() != null && mPaymentInfo.getTradeVo().getTrade() != null && mPaymentInfo.getTradeVo().getTrade().getId() != null)
            ThreadUtils.runOnWorkThread(new Runnable() {
                @Override
                public void run() {//modify v8.8 解耦合
                    List<PaymentItemGrouponDish> paymentItemGrouponDishList = paymentItemDal.findPaymentItemGroupDishsByTradeId(mPaymentInfo.getTradeVo().getTrade().getId());
                    if (paymentItemGrouponDishList != null && !paymentItemGrouponDishList.isEmpty()) {
                        mGroupDishes = new ArrayList<PaymentItemDishRelateImpl>();
                        for (PaymentItemGrouponDish paymentItemGrouponDish : paymentItemGrouponDishList) {
                            mGroupDishes.add(new PaymentItemDishRelateImpl(paymentItemGrouponDish));
                        }
                    }
                }
            });
    }

    public interface TuanGouCouponListenser {

        void removeCoupon(PayModelItem item);
    }

    private static class MeituanCouponViewHolder {
        TextView serialNumberTV;//序列号
        TextView couponType;//券类别
        TextView countTV;//张数
        TextView faceAmountTV;// 面额
        TextView amountTV;//金额
        ImageButton removeBT;//删除
        LinearLayout dishInfoView;//菜品详情
    }

    private static class MeituanCouponDishAdapter {
        private GroupPay mGroupPay;//已经加入的券金额
        private LinearLayout contentView;
        private Context mContext;
        private TuanGouCouponListenser mTuanGouCouponListenser;

        public MeituanCouponDishAdapter(Context context, LinearLayout view, GroupPay pay, TuanGouCouponListenser listenser) {
            mContext = context;
            mGroupPay = pay;
            contentView = view;
            mTuanGouCouponListenser = listenser;
        }

        public View createItemView(Context context, final PayModelItem item) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View convertView = layoutInflater.inflate(R.layout.pay_meituan_coupon_item_layout, null);
            MeituanCouponViewHolder viewHolder = new MeituanCouponViewHolder();
            viewHolder.serialNumberTV = (TextView) convertView.findViewById(R.id.ticket_serialNumber);
            viewHolder.couponType = (TextView) convertView.findViewById(R.id.ticket_type);
            viewHolder.countTV = (TextView) convertView.findViewById(R.id.ticket_count);
            viewHolder.faceAmountTV = (TextView) convertView.findViewById(R.id.ticket_face_amount);
            viewHolder.amountTV = (TextView) convertView.findViewById(R.id.ticket_amount);
            viewHolder.removeBT = (ImageButton) convertView.findViewById(R.id.ticket_button_remove);
            viewHolder.dishInfoView = (LinearLayout) convertView.findViewById(R.id.dish_View_ll);
            setViewHodler(viewHolder, item);
            return convertView;
        }

        public void setViewHodler(MeituanCouponViewHolder viewHolder, final PayModelItem item) {
            if (item != null) {
                viewHolder.serialNumberTV.setText(item.getTuanGouCouponDetail().getSerialNumber());
                //券类型
                if (item.getTuanGouCouponDetail().getCouponType() == 1) {//代金券
                    viewHolder.couponType.setText(mContext.getString(R.string.coupon_type_cash_label));
                } else if (item.getTuanGouCouponDetail().getCouponType() == 2) {//套餐券
                    viewHolder.couponType.setText(mContext.getString(R.string.coupon_type_group_dish_label));
                } else {
                    viewHolder.couponType.setText("");
                }
                viewHolder.countTV.setText(item.getUsedCount() + "");
                viewHolder.faceAmountTV.setText(Utils.formatPrice(item.getFaceValue().doubleValue()));
                viewHolder.amountTV.setText(Utils.formatPrice(item.getUsedValue().doubleValue()));
                viewHolder.removeBT.setTag(item);
                viewHolder.removeBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTuanGouCouponListenser != null)
                            mTuanGouCouponListenser.removeCoupon((PayModelItem) view.getTag());
                    }
                });
                viewHolder.dishInfoView.removeAllViews();
                MeituanDishVo meituanDishVo = item.getMeituanDishVo();
                if (meituanDishVo == null) {
                    viewHolder.dishInfoView.addView(new MeiTuanCouponDishView(this.mContext, item.getTuanGouCouponDetail(), null));
                } else {
                    if (meituanDishVo.matchDishItemVoList != null && !meituanDishVo.matchDishItemVoList.isEmpty()) {
                        for (MeituanDishItemVo dishItemVo : meituanDishVo.matchDishItemVoList) {
                            viewHolder.dishInfoView.addView(new MeiTuanCouponDishView(this.mContext, item.getTuanGouCouponDetail(), dishItemVo));
                        }
                    } else {
                        viewHolder.dishInfoView.addView(new MeiTuanCouponDishView(this.mContext, item.getTuanGouCouponDetail(), null));
                    }
                }
            }
        }

        public void updateData() {
            contentView.removeAllViews();
            List<PayModelItem> ls = mGroupPay.getAllPayModelItems();
            if (ls != null && ls.size() > 0) {
                int index = 0;
                for (PayModelItem it : ls) {
                    if (index > 0) {
                        contentView.addView(getEmptyView());
                    }
                    contentView.addView(createItemView(mContext, it));
                    index++;
                }
            }
        }

        private View getEmptyView() {
            LinearLayout.LayoutParams param =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 12, 1);
            View emptyView = new View(mContext);
            emptyView.setLayoutParams(param);
            return emptyView;
        }
    }
}
