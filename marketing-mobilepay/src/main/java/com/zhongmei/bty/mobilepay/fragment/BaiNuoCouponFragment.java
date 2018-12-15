package com.zhongmei.bty.mobilepay.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.dialog.PayDepositPromptDialog;
import com.zhongmei.bty.mobilepay.fragment.com.BasePayFragment;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponDetail;
import com.zhongmei.bty.mobilepay.operate.TuanGouOperates;
import com.zhongmei.bty.mobilepay.operate.TuanGouOperatesImpl;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;

import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.mobilepay.bean.GroupPay;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.basemodule.commonbusiness.adapter.BaseListAdapter;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.commonbusiness.view.CommonCheckCouponsView;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.mobilepay.enums.TicketInfoStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.mobilepay.message.TuanGouCouponReq;
import com.zhongmei.bty.mobilepay.IPayOverCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 百度糯米券、口碑券支付界面
 */
public class BaiNuoCouponFragment extends BasePayFragment implements View.OnClickListener, CommonCheckCouponsView.CouponsListener {
    final String TAG = BaiNuoCouponFragment.class.getSimpleName();
    private final static int MAX_COUPON_SIZE = 5;//add v8.9

    private TextView mPayAlterTV;
    private Button mPay;
    private CommonCheckCouponsView mCheckCouponsView;
    private LinearLayout mInputLayout;
    private LinearLayout mEmptyLayout;
    private ListView mListView;
    private GroupPay mGroupPay;//已经加入的券金额
    private TuanGouCouponAdapter mTuanGouCouponAdapter;
    private PayModeId mCurrentPayMode = PayModeId.BAINUO_TUANGOU; //当前字符方式//add v8.9


    public void setPayMode(PayModeId payMode) {
        this.mCurrentPayMode = payMode;
    }


    public static BaiNuoCouponFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        BaiNuoCouponFragment f = new BaiNuoCouponFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupPay = new GroupPay();
        mTuanGouCouponAdapter = new TuanGouCouponAdapter(this.getActivity(), mGroupPay, mTuanGouCouponListenser);
        registerEventBus();
        if (!mPaymentInfo.isDinner() || mPaymentInfo.isOrderCenter())
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.pay_bainuocoupon_fragment_layout, container, false);
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
        mListView = (ListView) view.findViewById(R.id.pay_bainuo_ticket_list);
        mPay = (Button) view.findViewById(R.id.pay);
    }

    private void initView() {
        mPay.setOnClickListener(this);
        mCheckCouponsView.setCouponsListener(this);
        mCheckCouponsView.setKeyBoardValue(true);
        mCheckCouponsView.getEdit().setInputType(InputType.TYPE_CLASS_NUMBER);
        mCheckCouponsView.getEdit().setHint(getString(R.string.pay_input_meituan_code_alter));
        mCheckCouponsView.getEdit().setHintTextColor(getResources().getColor(R.color.print_line_gray));
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
        mListView.setAdapter(mTuanGouCouponAdapter);
        updateView();
        if (mPaymentInfo != null && mPaymentInfo.getQuickPayType() == IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    View view = new View(getContext());
                    view.setId(R.id.scan_btn);
                    mCheckCouponsView.onClick(view);

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
            mGroupPay.clear();
            mPaymentInfo.getOtherPay().clear();
            mTuanGouCouponAdapter.updateData();
            /*DisplayServiceManager.updateDisplay(getActivity().getApplicationContext(),
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
                    mGroupPay.clear();
                    mPaymentInfo.getOtherPay().clear();
                    mTuanGouCouponAdapter.updateData();
                    updateView();
                } else {
                    updateNotPayMent();
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };

    @Override
    public void onDestroy() {
        this.unregisterEventBus();
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

    //add begin 20170424 for 组合支付开关
  /*  public boolean enablePay() {
        if (isSuportGroupPay()) {//如果支持组合支付
            return mGroupPay.getGroupAmount() > 0;
        } else {
            return mGroupPay.getGroupAmount() >= mPaymentInfo.getActualAmount();//不分步支付，输入金额必须大于等于应付金额
        }
    }*/

    //add end 20170424 for 组合支付开关
    private double getRestAmount() {
        return mPaymentInfo.getActualAmount() - mGroupPay.getGroupAmount();
    }

    /***
     * 监听抹零事件,抹零后清空所有输入金额
     */
    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && !this.isHidden()) {
            updateNotPayMent();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay) {
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
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_BAIDU_COUPON);
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

    @Override
    public void getCouponsNo(String ticketNo) {
        if (this.mPaymentInfo.isNeedToPayDeposit()) {//验券前先判断是否交押金 add 8.14
            PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
        } else if (this.isNeedToDeductionEarnest()) {//验券前先判断是否抵扣预付金 add 8.14
            this.showBookingDeductionDialog();
        } else {
            if (!TextUtils.isEmpty(ticketNo) && Utils.isNum(ticketNo)) {
                if (mGroupPay.size() >= MAX_COUPON_SIZE) {
                    ToastUtil.showLongToast(String.format(getResources().getString(R.string.coupon_page_size_limit_to_pay), MAX_COUPON_SIZE + ""));
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
                TuanGouOperates couponsOperates = new TuanGouOperatesImpl(getActivity());
                TuanGouCouponReq req = new TuanGouCouponReq(mCurrentPayMode.value(), ticketNo);
                couponsOperates.getTuanGouCouponsDetail(req, LoadingResponseListener.ensure(mListener, getFragmentManager()));

            } else {
                ToastUtil.showLongToast(R.string.please_input_coupon_code);
            }
        }
    }


    private ResponseListener mListener = new ResponseListener<TuanGouCouponDetail>() {

        @Override
        public void onResponse(ResponseObject<TuanGouCouponDetail> response) {
            try {
                if (ResponseObject.isOk(response)) {//成功
                    {
                        TuanGouCouponDetail tuanGouCouponDetail = response.getContent();
                        //如果可用
                        if (isEnableCoupon(tuanGouCouponDetail)) {
                            /* double restAmout = getRestAmount();
                            //已经溢收了，不容许添加
                           if (tuanGouCouponDetail.getMarketPrice().doubleValue() > restAmout) {
                                ToastUtil.showLongToast(getString(R.string.baidu_nuomi_coupon_not_support_more));
                            } else */
                            {
                                PayModelItem item = new PayModelItem(mCurrentPayMode, tuanGouCouponDetail);
                                item.setUsedCount(1);
                                item.setUsedValue(tuanGouCouponDetail.getMarketPrice());
                                mGroupPay.addPayModelItem(item);
                                mTuanGouCouponAdapter.updateData();
                                mCheckCouponsView.getEdit().setText("");
                                updateView();
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

    private boolean isEnableCoupon(TuanGouCouponDetail tuanGouCouponDetail) {
        if (tuanGouCouponDetail != null) {
            if (tuanGouCouponDetail.getStatus() == TicketInfoStatus.NOT_USED.value()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public interface TuanGouCouponListenser {

        void removeCoupon(PayModelItem item);
    }

    private static class ViewHolder {
        TextView serialNumberTV;//序列号

        TextView countTV;//张数

        TextView amountTV;//金额

        ImageButton removeBT;//删除
    }

    private static class TuanGouCouponAdapter extends BaseListAdapter<ViewHolder, PayModelItem> {
        private GroupPay mGroupPay;//已经加入的券金额
        private TuanGouCouponListenser mTuanGouCouponListenser;

        /**
         * @param context
         */
        public TuanGouCouponAdapter(Context context, GroupPay groupPay, TuanGouCouponListenser listenser) {
            super(context, R.layout.pay_item_bainuocpupon_layout);
            mTuanGouCouponListenser = listenser;
            mGroupPay = groupPay;
        }

        public void updateData() {
            this.clear();
            this.addList(mGroupPay.getAllPayModelItems());
        }

        @Override
        public ViewHolder initViewHodler(View convertView) {
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.serialNumberTV = (TextView) convertView.findViewById(R.id.ticket_serialNumber);
            viewHolder.countTV = (TextView) convertView.findViewById(R.id.ticket_count);
            viewHolder.amountTV = (TextView) convertView.findViewById(R.id.ticket_amount);
            viewHolder.removeBT = (ImageButton) convertView.findViewById(R.id.ticket_button_remove);
            return viewHolder;
        }

        @Override
        public void setViewHodler(ViewHolder viewHolder, int position) {
            final PayModelItem item = getItem(position);
            viewHolder.serialNumberTV.setText(item.getTuanGouCouponDetail().getSerialNumber());
            viewHolder.countTV.setText(item.getUsedCount() + "");
            viewHolder.amountTV.setText(Utils.formatPrice(item.getUsedValue().doubleValue()));
            viewHolder.removeBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTuanGouCouponListenser != null)
                        mTuanGouCouponListenser.removeCoupon(item);
                }
            });
        }
    }
}
