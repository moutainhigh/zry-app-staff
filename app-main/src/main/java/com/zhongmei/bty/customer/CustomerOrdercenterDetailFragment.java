package com.zhongmei.bty.customer;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.customer.bean.CustomerOrderBean;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.message.CustomerCardInfoResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerCardInfoResp.CustomerCardInfoResult;
import com.zhongmei.bty.basemodule.customer.message.CustomerMemberStoreValueRevokeResp;
import com.zhongmei.bty.basemodule.customer.message.PaymentAndMemberReq;
import com.zhongmei.bty.basemodule.customer.message.PaymentAndMemberResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.ReturnCardDataModel;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardAccountResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardSingleSearchByTransReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSaleCardInvalidReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSaleCardInvalidResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardDetailReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardDetailResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.EntityCardChangeDetailResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.SalesCardReturnResp;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment.OperateCloseListener;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment.OperateListener;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment.OperateResult;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.OrderCategory;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.WindowToken;
import com.zhongmei.bty.customer.adapter.CustomerOrdercenterDetaillAdapter;
import com.zhongmei.bty.customer.bean.CustomerOrdercenterDetailCommonBean;
import com.zhongmei.bty.customer.event.EventClickOrdercenterListItem;
import com.zhongmei.bty.manager.EntityCardMananger;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.TransferResp;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.ValueEnums;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@EFragment(R.layout.customer_ordercenter_detail)
public class CustomerOrdercenterDetailFragment extends BasicFragment {
    public static final String TAG = CustomerOrdercenterDetailFragment.class.getName();

    @ViewById(R.id.customer_detail_frame)
    LinearLayout mDetailFrame;

    @ViewById(R.id.customer_centerorder_empty)
    ImageView ivCustomerCenterOrderEmpty;

    @ViewById(R.id.order_detail_list)
    ListView orderDetailList;

    @ViewById(R.id.left_tab_rl)
    RelativeLayout leftTabRL;
    @ViewById(R.id.left_tab_tv)
    TextView leftTabTv;
    @ViewById(R.id.left_tab_line)
    View leftTabLine;
    @ViewById(R.id.middle_tab_rl)
    RelativeLayout middleTabRL;
    @ViewById(R.id.middle_tab_tv)
    TextView middleTabTv;
    @ViewById(R.id.middle_tab_line)
    View middleTabLine;
    @ViewById(R.id.right_tab_rl)
    RelativeLayout rightTabRL;
    @ViewById(R.id.right_tab_tv)
    TextView rightTabTv;
    @ViewById(R.id.right_tab_line)
    View rightTablLine;
    @ViewById(R.id.table_head_ll)
    LinearLayout tableHeadLL;
    @ViewById(R.id.head)
    ImageView headIv;

    @ViewById(R.id.name)
    TextView nameTv;

    @ViewById(R.id.sex)
    TextView sexTv;

    @ViewById(R.id.member_level)
    TextView memberLevelTv;

    @ViewById(R.id.grow_value)
    TextView growValueTv;

    @ViewById(R.id.member_head_info)
    RelativeLayout memberHeadInfo;

    @ViewById(R.id.operate_ll)
    LinearLayout operateLL;

    @ViewById(R.id.refund_btn)
    Button refundBtn;
    @ViewById(R.id.print_btn)
    Button printBtn;
    @ViewById(R.id.recision_btn)
    Button recisionBtn;
    @ViewById(R.id.cancel_btn)
    Button cancel_btn;
    @ViewById(R.id.select_head_ll)
    LinearLayout selcetHeadLL;

    @ViewById(R.id.allcheck_cb)
    CheckBox allCheckBox;

    @ViewById(R.id.check_number_tv)
    TextView checkNumberTv;

    @ViewById(R.id.customer_memeber_tile)
    LinearLayout customerMemberTitle;
    @ViewById(R.id.customer_entity_card_change_info_ll)
    LinearLayout llCustomerEntityCardChangeInfo;

    @ViewById(R.id.order_no_tv)
    TextView tvOrderNo;

    @ViewById(R.id.old_card_num_tv)
    TextView tvOldCardNo;

    @ViewById(R.id.old_card_status_tv)
    TextView tvOldCardStatus;

    @ViewById(R.id.new_card_num_tv)
    TextView tvNewCardNo;

    @ViewById(R.id.new_card_status_tv)
    TextView tvNewCardStatus;

    private CustomerOrdercenterDetaillAdapter adapter;

    private List<CustomerOrdercenterDetailCommonBean> leftCommonBeans;


    private List<CustomerOrdercenterDetailCommonBean> rightCommonBeans;
    private List<CustomerOrdercenterDetailCommonBean> extraCommonBeans;

    private int currentTabId = R.id.left_tab_rl;
    private WindowToken windowToken;
    private OrderCategory orderCategory;
    private EntityCardType entityCardType;
    private boolean isCheckMode = false;
    private Trade currentTrade;
    private CustomerOrderBean mOrderBean;

    private ReturnCardDataModel mReturnCardDataModel;

    private TradeOperates mTradeOperate;

    private CustomerSellcardDetailResp sellcardDetailResp;
    private PaymentAndMemberResp paymentAndMemberResp;
    @AfterViews
    protected void initView() {
        setupView();
        CustomerDal customerDal = OperatesFactory.create(CustomerDal.class);
        mReturnCardDataModel = new ReturnCardDataModel(customerDal);
        mTradeOperate = OperatesFactory.create(TradeOperates.class);
        registerEventBus();
        mDetailFrame.setVisibility(View.GONE);
        ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
        allCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapter.checkAll(allCheckBox.isChecked());
            }
        });
    }

    private void setupView() {
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            leftTabLine.setBackgroundColor(getResources().getColor(R.color.beauty_color_FF2283));
            middleTabLine.setBackgroundColor(getResources().getColor(R.color.beauty_color_FF2283));
            rightTablLine.setBackgroundColor(getResources().getColor(R.color.beauty_color_FF2283));
        }
    }

    private void update(WindowToken windowToken, int tabId) {
        if (R.id.left_tab_rl == tabId) {
            adapter = new CustomerOrdercenterDetaillAdapter(getActivity(), leftCommonBeans, CustomerOrdercenterDetailCommonBean.class);
            orderDetailList.setAdapter(adapter);
            orderDetailList.setVisibility(View.VISIBLE);
            llCustomerEntityCardChangeInfo.setVisibility(View.GONE);
        } else if (R.id.middle_tab_rl == tabId) {
            orderDetailList.setVisibility(View.GONE);
            llCustomerEntityCardChangeInfo.setVisibility(View.VISIBLE);

        } else {

            if (windowToken == WindowToken.MEMBER_STORE_VALUE) {                rightCommonBeans = filterMemberinfo(extraCommonBeans);
            }
            adapter = new CustomerOrdercenterDetaillAdapter(getActivity(), rightCommonBeans, CustomerOrdercenterDetailCommonBean.class);
                        orderDetailList.setAdapter(adapter);
            orderDetailList.setVisibility(View.VISIBLE);
            llCustomerEntityCardChangeInfo.setVisibility(View.GONE);
        }
    }

    @Click({R.id.left_tab_rl, R.id.right_tab_rl, R.id.middle_tab_rl})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.left_tab_rl:
                updateTab(R.id.left_tab_rl);
                break;
            case R.id.right_tab_rl:
                updateTab(R.id.right_tab_rl);
                break;
            case R.id.middle_tab_rl:
                updateTab(R.id.middle_tab_rl);
                break;
            default:
                break;
        }
        update(windowToken, currentTabId);
    }

        private void updateTab(int tabId) {
        currentTabId = tabId;
        if (R.id.right_tab_rl == tabId) {
            leftTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tab_notchoose));
            leftTabLine.setVisibility(View.INVISIBLE);
            if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                rightTabTv.setTextColor(getResources().getColor(R.color.beauty_color_FF2283));
            } else {
                rightTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tabchoose));
            }
            rightTablLine.setVisibility(View.VISIBLE);
            middleTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tab_notchoose));
            middleTabLine.setVisibility(View.INVISIBLE);
            if (windowToken == WindowToken.CARD_SALE && !isCheckMode) {
                tableHeadLL.setVisibility(View.VISIBLE);
            } else {
                tableHeadLL.setVisibility(View.GONE);
            }
        } else if (R.id.middle_tab_rl == tabId) {
            leftTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tab_notchoose));
            leftTabLine.setVisibility(View.INVISIBLE);
            rightTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tab_notchoose));
            rightTablLine.setVisibility(View.INVISIBLE);
            if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                middleTabTv.setTextColor(getResources().getColor(R.color.beauty_color_FF2283));
            } else {
                middleTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tabchoose));
            }
            middleTabLine.setVisibility(View.VISIBLE);
            tableHeadLL.setVisibility(View.GONE);
        } else {
            if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                leftTabTv.setTextColor(getResources().getColor(R.color.beauty_color_FF2283));
            } else {
                leftTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tabchoose));
            }
            leftTabLine.setVisibility(View.VISIBLE);
            rightTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tab_notchoose));
            rightTablLine.setVisibility(View.INVISIBLE);
            middleTabTv.setTextColor(getResources().getColor(R.color.customer_ordercenter_detail_tab_notchoose));
            middleTabLine.setVisibility(View.INVISIBLE);
            tableHeadLL.setVisibility(View.GONE);
        }
    }


    private void getCustomerSellCardDetailInfo(long tradeId) {
        CustomerSellcardDetailReq customerSellCardDetailReq = new CustomerSellcardDetailReq();
        Long userId = null;
        if (Session.getAuthUser() != null) {
            userId = Session.getAuthUser().getId();
        }
        customerSellCardDetailReq.setUserId(userId);
        customerSellCardDetailReq.setTradeId(tradeId);

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CustomerSellcardDetailResp> listener = new ResponseListener<CustomerSellcardDetailResp>() {

            @Override
            public void onResponse(ResponseObject<CustomerSellcardDetailResp> response) {
                if (ResponseObject.isOk(response)) {
                    sellcardDetailResp = response.getContent();
                    ToastUtil.showLongToast(response.getMessage());
                    sellCardDetailResponseConvert(response.getContent());
                    adapter =
                            new CustomerOrdercenterDetaillAdapter(getActivity(), leftCommonBeans,
                                    CustomerOrdercenterDetailCommonBean.class);

                    orderDetailList.setVisibility(View.VISIBLE);
                    llCustomerEntityCardChangeInfo.setVisibility(View.GONE);
                    orderDetailList.setAdapter(adapter);

                    showCheckMode(false);
                    updateTab(R.id.left_tab_rl);
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                    mDetailFrame.setVisibility(View.GONE);
                    ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mDetailFrame.setVisibility(View.GONE);
                ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
            }
        };
        customerOperates.getCustomerSellCardDetailInfo(customerSellCardDetailReq,
                LoadingResponseListener.ensure(listener, getFragmentManager()));
    }


    private void getAnonymousEntityCardSellDetailInfo(Long tradeId) {
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CustomerSellcardDetailResp> listener = new ResponseListener<CustomerSellcardDetailResp>() {

            @Override
            public void onResponse(ResponseObject<CustomerSellcardDetailResp> response) {
                if (ResponseObject.isOk(response)) {
                    sellcardDetailResp = response.getContent();
                    ToastUtil.showLongToast(response.getMessage());
                    sellCardDetailResponseConvert(response.getContent());
                    adapter =
                            new CustomerOrdercenterDetaillAdapter(getActivity(), leftCommonBeans,
                                    CustomerOrdercenterDetailCommonBean.class);

                    orderDetailList.setVisibility(View.VISIBLE);
                    llCustomerEntityCardChangeInfo.setVisibility(View.GONE);
                    orderDetailList.setAdapter(adapter);

                    showCheckMode(false);
                    updateTab(R.id.left_tab_rl);
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                    mDetailFrame.setVisibility(View.GONE);
                    ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mDetailFrame.setVisibility(View.GONE);
                ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
            }
        };
        customerOperates.getAnonymousEntityCardSellDetailInfo(tradeId, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }


    public void onEventMainThread(final EventClickOrdercenterListItem event) {
        if (event.isEmpty) {
            mDetailFrame.setVisibility(View.GONE);
            ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
            return;
        } else {
            mDetailFrame.setVisibility(View.VISIBLE);
            ivCustomerCenterOrderEmpty.setVisibility(View.GONE);
        }

        mOrderBean = event.orderBean;
        windowToken = event.windowToken;
        orderCategory = event.orderCategory;
        entityCardType = event.getEntityCardType();

        String[] tabStr = getResources().getStringArray(R.array.customer_ordercenter_detail_tab_text);

        if (event.windowToken == WindowToken.CARD_SALE) {            memberHeadInfo.setVisibility(View.GONE);
            if (R.id.left_tab_rl == currentTabId || isCheckMode) {
                tableHeadLL.setVisibility(View.GONE);
            } else {
                tableHeadLL.setVisibility(View.VISIBLE);
            }
            rightTabTv.setText(tabStr[0]);
            if (event.tradeId > 0) {
                initalOperateToolBar();
                if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                    getAnonymousEntityCardSellDetailInfo(event.tradeId);
                } else {
                    getCustomerSellCardDetailInfo(event.tradeId);
                }
            } else {
                mDetailFrame.setVisibility(View.GONE);
                ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
            }
        } else if (windowToken == WindowToken.ENTITY_CARD_CHANGE) {
            memberHeadInfo.setVisibility(View.GONE);
            tableHeadLL.setVisibility(View.GONE);
            rightTabTv.setText(tabStr[0]);
            if (event.tradeId > 0) {
                initalOperateToolBar();
                getEntityCardChangeDetailInfo(event.tradeId);
            } else {
                mDetailFrame.setVisibility(View.GONE);
                ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
            }
        } else {

            switch (event.windowToken) {
                case CARD_STORE_VALUE:
                    rightTabTv.setText(tabStr[1]);
                    memberAndCard(event, new OnPaymentAndMemberCallback() {
                        @Override
                        public void onPaymentAndMember(boolean result, String message) {
                            if (result) {
                                setCardAccountInfo(event);
                            }
                        }
                    });
                    break;
                case MEMBER_STORE_VALUE:
                    rightTabTv.setText(tabStr[2]);
                    memberAndCard(event, null);
                    break;
            }
        }
        refreshMiddleTab();
    }

    private void setCardAccountInfo(EventClickOrdercenterListItem event) {
        if (event.orderBean != null) {
            final CustomerOrderBean bean = event.orderBean;
            CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
            customerOperates.getCardAccount(event.orderBean.getCardNo(), new ResponseListener<CardAccountResp>() {
                @Override
                public void onResponse(ResponseObject<CardAccountResp> response) {
                    if (!ResponseObject.isOk(response)) {
                        ToastUtil.showLongToast(response.getMessage());
                        return;
                    }

                    if (!TransferResp.isOk(response.getContent())) {
                        ToastUtil.showLongToast(response.getMessage());
                        return;
                    }

                    CardAccountResp.CardAccountItem result = response.getContent().getResult();
                    bean.setCardType(result.getCardType());
                    bean.setIntegral(result.getIntegral());
                    bean.setCardKindId(result.cardBaseInfo.cardKindId);
                    saveCardInfo(result);
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showLongToast(error.getMessage());
                }
            });
        }
    }

    interface OnPaymentAndMemberCallback {
        void onPaymentAndMember(boolean result, String message);
    }

    private void memberAndCard(EventClickOrdercenterListItem event, OnPaymentAndMemberCallback callback) {
        tableHeadLL.setVisibility(View.GONE);
        selcetHeadLL.setVisibility(View.GONE);
        customerMemberTitle.setVisibility(View.VISIBLE);



        if (event.orderBean != null) {
            initalOperateToolBar();
            if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                memberHeadInfo.setVisibility(View.GONE);
                queryPayment(event.orderBean.getPaymentUuid(), callback);
            } else {
                memberHeadInfo.setVisibility(View.VISIBLE);
                queryPaymentAndMember(event.orderBean.getPaymentUuid(),
                        event.orderBean.getCustomerId(), callback);
            }
        } else {
            mDetailFrame.setVisibility(View.GONE);
            ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
        }
    }

        private void refreshMiddleTab() {
        if (windowToken != null && WindowToken.ENTITY_CARD_CHANGE == windowToken) {
            middleTabRL.setVisibility(View.VISIBLE);
        } else {
            middleTabRL.setVisibility(View.GONE);
        }
    }


    void sellCardDetailResponseConvert(CustomerSellcardDetailResp response) {

        if (response.getTrades() != null) {
            currentTrade = response.getTrades().get(0);
        }
        if (leftCommonBeans == null) {
            leftCommonBeans = new ArrayList<CustomerOrdercenterDetailCommonBean>();
        } else {
            leftCommonBeans.clear();
        }

        CustomerOrdercenterDetailCommonBean bean = null;
        Payment payment = null;
        if (response.getPayments() != null) {
            payment = response.getPayments().get(0);

            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.customer_order_center_receivable));
            bean.setValue(payment.getReceivableAmount().toString());
            leftCommonBeans.add(bean);
                        BusinessType type = null;
            if (currentTrade != null) {
                type = currentTrade.getBusinessType();
            }
            if (type == null || (type != BusinessType.ANONYMOUS_ENTITY_CARD_SELL && type != BusinessType.ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE)) {
                bean = new CustomerOrdercenterDetailCommonBean();
                bean.setName(getString(R.string.customer_order_center_moling));
                bean.setValue(payment.getExemptAmount().toString());
                leftCommonBeans.add(bean);
            }
        }

                if (response.getPaymentItems() != null) {
            for (PaymentItem item : response.getPaymentItems()) {
                if (item.getPayStatus() == TradePayStatus.PAID || item.getPayStatus() == TradePayStatus.REFUNDED) {
                    bean = new CustomerOrdercenterDetailCommonBean();
                    bean.setName(PaySettingCache.getPayModeNameByModeId(item.getPayModeId()) + "：");                    bean.setValue(item.getUsefulAmount().toString());
                    leftCommonBeans.add(bean);
                }

            }
        }

        if (payment != null) {
            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.customer_order_center_total_amount));
            bean.setValue(payment.getActualAmount().toString());
            leftCommonBeans.add(bean);
        }


    }


    private String getDealStatus(CustomerSaleCardInfo cardSaleInfo) {
        int cardStatus = cardSaleInfo.getCardStatus();
        String result = null;
        String[] dealStatus = getResources().getStringArray(R.array.customer_ordercenter_detail_card_deal_status);

        if (cardStatus == 1) {
            result = dealStatus[0];
        } else if (cardStatus == 2) {
            result = dealStatus[1];
        } else if (cardStatus == 3) {
            result = dealStatus[2];
        }
        return result;

    }


    private void queryPaymentAndMember(String paymentUuid, long memberId, final OnPaymentAndMemberCallback callback) {
        PaymentAndMemberReq req = new PaymentAndMemberReq();
        req.setPaymentUuid(paymentUuid);
        req.setMemberId(memberId);
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            req.setUserId(authUser.getId());
        }

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<PaymentAndMemberResp> listener = new ResponseListener<PaymentAndMemberResp>() {

            @Override
            public void onResponse(ResponseObject<PaymentAndMemberResp> response) {
                if (ResponseObject.isOk(response)) {
                    paymentAndMemberResp = response.getContent();
                    queryPaymentAndMemberResponseConvert(response.getContent());

                                        nameTv.setText(extraCommonBeans.get(0).getValue());
                    sexTv.setText(extraCommonBeans.get(1).getValue());
                    memberLevelTv.setText(extraCommonBeans.get(2).getValue());

                                        if (TextUtils.isEmpty(extraCommonBeans.get(3).getValue())) {
                        growValueTv.setText(getString(R.string.customer_ordercenter_detail_reach_top_level));
                    } else {
                        growValueTv.setText(getString(R.string.customer_ordercenter_detail_memberinfo_growvalue,
                                extraCommonBeans.get(3).getValue(),
                                extraCommonBeans.get(4).getValue()));
                    }

                    adapter =
                            new CustomerOrdercenterDetaillAdapter(getActivity(), leftCommonBeans,
                                    CustomerOrdercenterDetailCommonBean.class);

                    orderDetailList.setVisibility(View.VISIBLE);
                    llCustomerEntityCardChangeInfo.setVisibility(View.GONE);
                    orderDetailList.setAdapter(adapter);
                    showCheckMode(false);
                    updateTab(R.id.left_tab_rl);
                    if (callback != null) {
                        callback.onPaymentAndMember(true, null);
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                    mDetailFrame.setVisibility(View.GONE);
                    ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
                    if (callback != null) {
                        callback.onPaymentAndMember(false, response.getMessage());
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mDetailFrame.setVisibility(View.GONE);
                ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
                if (callback != null) {
                    callback.onPaymentAndMember(false, error.getMessage());
                }
            }
        };
        customerOperates.queryPaymentAndMember(req, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }


    private void queryPayment(String paymentUuid, final OnPaymentAndMemberCallback callback) {
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<PaymentAndMemberResp> listener = new ResponseListener<PaymentAndMemberResp>() {

            @Override
            public void onResponse(ResponseObject<PaymentAndMemberResp> response) {
                if (ResponseObject.isOk(response)) {
                    paymentAndMemberResp = response.getContent();
                    queryPaymentAndMemberResponseConvert(response.getContent());
                    adapter =
                            new CustomerOrdercenterDetaillAdapter(getActivity(), leftCommonBeans,
                                    CustomerOrdercenterDetailCommonBean.class);
                    orderDetailList.setVisibility(View.VISIBLE);
                    llCustomerEntityCardChangeInfo.setVisibility(View.GONE);
                    orderDetailList.setAdapter(adapter);
                    showCheckMode(false);
                    updateTab(R.id.left_tab_rl);
                    if (callback != null) {
                        callback.onPaymentAndMember(true, null);
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                    mDetailFrame.setVisibility(View.GONE);
                    ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
                    if (callback != null) {
                        callback.onPaymentAndMember(false, response.getMessage());
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mDetailFrame.setVisibility(View.GONE);
                ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
                if (callback != null) {
                    callback.onPaymentAndMember(false, error.getMessage());
                }
            }
        };
        customerOperates.queryPayment(paymentUuid, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    void queryPaymentAndMemberResponseConvert(PaymentAndMemberResp response) {
        if (leftCommonBeans == null) {
            leftCommonBeans = new ArrayList<CustomerOrdercenterDetailCommonBean>();
        } else {
            leftCommonBeans.clear();
        }

        CustomerOrdercenterDetailCommonBean bean = null;
        Payment payment = null;
        if (response.getPayments() != null) {
            payment = response.getPayments().get(0);

            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.customer_order_center_receivable));
            bean.setValue(payment.getReceivableAmount().toString());
            leftCommonBeans.add(bean);
                        BusinessType type = null;
            if (currentTrade != null) {
                type = currentTrade.getBusinessType();
            }
            if (type == null || (type != BusinessType.ANONYMOUS_ENTITY_CARD_SELL && type != BusinessType.ANONYMOUS_ENTITY_CARD_SELL_AND_RECHARGE)) {
                bean = new CustomerOrdercenterDetailCommonBean();
                bean.setName(getString(R.string.customer_order_center_moling));
                bean.setValue(payment.getExemptAmount().toString());
                leftCommonBeans.add(bean);
            }
        }

                if (response.getPaymentItems() != null) {
            for (PaymentItem item : response.getPaymentItems()) {
                if (item.getPayStatus() == TradePayStatus.PAID || item.getPayStatus() == TradePayStatus.REFUNDED) {
                    bean = new CustomerOrdercenterDetailCommonBean();
                    bean.setName(PaySettingCache.getPayModeNameByModeId(item.getPayModeId()) + "：");                    bean.setValue(item.getUsefulAmount().toString());
                    leftCommonBeans.add(bean);
                }

            }
        }

        if (payment != null) {
            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.customer_order_center_total_amount));
            bean.setValue(payment.getActualAmount().toString());
            leftCommonBeans.add(bean);
        }

                if (response.getMembers() != null) {
            if (extraCommonBeans == null) {
                extraCommonBeans = new ArrayList<CustomerOrdercenterDetailCommonBean>();
            } else {
                extraCommonBeans.clear();
            }
            PaymentAndMemberResp.Member member = response.getMembers().get(0);

            CustomerOrdercenterDetailCommonBean tempBean = new CustomerOrdercenterDetailCommonBean();
            tempBean.setName("name");
            tempBean.setValue(member.getName());
            extraCommonBeans.add(tempBean);

            tempBean = new CustomerOrdercenterDetailCommonBean();
            tempBean.setName("sex");
            if (member.getSex() == null) {                tempBean.setValue(getString(R.string.gender_unknow));
            } else if (member.getSex().equals("0")) {
                tempBean.setValue(getString(R.string.lady));
            } else if (member.getSex().equals("1")) {
                tempBean.setValue(getString(R.string.mr));
            } else {
                tempBean.setValue(getString(R.string.gender_unknow));
            }
            extraCommonBeans.add(tempBean);

            tempBean = new CustomerOrdercenterDetailCommonBean();
            tempBean.setName("levelName");
            tempBean.setValue(member.getLevelName());
            extraCommonBeans.add(tempBean);

            tempBean = new CustomerOrdercenterDetailCommonBean();
            tempBean.setName("nextLevelName");
            tempBean.setValue(member.getNextLevelName());
            extraCommonBeans.add(tempBean);

            tempBean = new CustomerOrdercenterDetailCommonBean();
            tempBean.setName("nextNdGrownVaule");
            tempBean.setValue(member.getNextNdGrownVaule());
            extraCommonBeans.add(tempBean);

            tempBean = new CustomerOrdercenterDetailCommonBean();
            tempBean.setName(getString(R.string.account_balance));
            tempBean.setValue(String.valueOf(member.getRemainValue()));
            extraCommonBeans.add(tempBean);

            tempBean = new CustomerOrdercenterDetailCommonBean();
            tempBean.setName(getString(R.string.available_points));
            tempBean.setValue(String.valueOf(member.getIntegral()));
            extraCommonBeans.add(tempBean);
        }

    }

    private String getCardStatusName(int status) {
        Resources res = MainApplication.getInstance().getResources();
        switch (status) {
            case 0:
                return res.getString(R.string.eccard_not_make_card);
            case 1:
                return res.getString(R.string.eccard_unsale);
            case 2:
                return res.getString(R.string.eccard_unactive);
            case 3:
                return res.getString(R.string.eccard_actived);
            case 4:
                return res.getString(R.string.eccard_disabled);
            case 5:
                return res.getString(R.string.eccard_invalided);
        }
        return res.getString(R.string.eccard_not_make_card);

    }


    void saveCardInfo(CardAccountResp.CardAccountItem orderBean) {
        if (rightCommonBeans == null) {
            rightCommonBeans = new ArrayList<CustomerOrdercenterDetailCommonBean>();
        } else {
            rightCommonBeans.clear();
        }

        CustomerOrdercenterDetailCommonBean bean = new CustomerOrdercenterDetailCommonBean();
        bean.setName(getString(R.string.account_balance));
        bean.setValue(String.valueOf(orderBean.getRemainValue()));
        rightCommonBeans.add(bean);

        if (orderBean.getCardType() != EntityCardType.ANONYMOUS_ENTITY_CARD) {
            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.available_points));
            bean.setValue(String.valueOf(orderBean.getIntegral() != null ? orderBean.getIntegral() : 0));
            rightCommonBeans.add(bean);
        }

        bean = new CustomerOrdercenterDetailCommonBean();
        bean.setName(getString(R.string.card_kind));
        bean.setValue(String.valueOf(orderBean.getCardKindKame()));
        rightCommonBeans.add(bean);

        bean = new CustomerOrdercenterDetailCommonBean();
        bean.setName(getString(R.string.customer_state));
        bean.setValue(String.valueOf(getCardStatusName(orderBean.getCardStatus())));
        rightCommonBeans.add(bean);
    }


    private List<CustomerOrdercenterDetailCommonBean> filterMemberinfo(
            List<CustomerOrdercenterDetailCommonBean> inputBeans) {
        if (inputBeans == null) {
            return null;
        }
        List<CustomerOrdercenterDetailCommonBean> result = new ArrayList<CustomerOrdercenterDetailCommonBean>();
        for (int i = 5; i < inputBeans.size(); i++) {
            CustomerOrdercenterDetailCommonBean bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(inputBeans.get(i).getName());
            bean.setValue(inputBeans.get(i).getValue());
            result.add(bean);
        }
        return result;
    }


    private void showOperateButtons(View... views) {
        int count = operateLL.getChildCount();

        for (int i = 0; i < count; i++) {
            operateLL.getChildAt(i).setVisibility(View.GONE);
        }

        if (views != null && views.length > 0) {
            if (views.length == 1) {
                views[0].setVisibility(View.VISIBLE);
                views[0].setBackgroundResource(R.drawable.customer_create_order_btn2);
            } else {
                for (int i = 0; i < views.length; i++) {
                    if (i == 0) {
                        views[i].setBackgroundResource(R.drawable.btn_green_customer);
                    } else if (i == views.length - 1) {
                        views[i].setBackgroundResource(R.drawable.customer_create_order_btn);
                    } else {
                                            }
                    views[i].setVisibility(View.VISIBLE);
                }
            }
            operateLL.setVisibility(View.VISIBLE);
        } else {
            operateLL.setVisibility(View.GONE);
        }
    }


    private void initalOperateToolBar() {
        switch (orderCategory) {
            case NOT_PAYED:
                refreshNotPayedOperateToolBar();
                break;
            case PAYED:
                refreshPayedOperateToolBar();
                break;
            case INVALID:
                refreshInvalidOperateToolBar();
                break;
            case RETURNED:
                refreshRefundOperateToolBar();
                break;
            default:
                break;
        }
    }

        private void refreshNotPayedOperateToolBar() {


        if (windowToken == WindowToken.CARD_SALE && orderCategory == OrderCategory.NOT_PAYED && currentTrade != null) {
            showOperateButtons(recisionBtn);
        } else {
            showOperateButtons(new View[]{});
        }
    }

        private void refreshPayedOperateToolBar() {
        if (WindowToken.ENTITY_CARD_CHANGE == windowToken) {
            showOperateButtons(new View[]{});
        } else {
                        if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                showOperateButtons(printBtn);
            } else {
                                if ((windowToken != null && windowToken == WindowToken.CARD_STORE_VALUE) && (mOrderBean != null && mOrderBean.getStoreType() != null
                        && mOrderBean.getStoreType() == 2)) {
                    showOperateButtons(printBtn);
                } else {
                    showOperateButtons(refundBtn, printBtn);
                }
            }
        }
    }

        private void refreshInvalidOperateToolBar() {
        if (WindowToken.ENTITY_CARD_CHANGE == windowToken) {
            showOperateButtons(new View[]{});
        } else {
                        if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                showOperateButtons(new View[]{});
            } else {
                showOperateButtons(printBtn);
            }
        }
    }

        private void refreshRefundOperateToolBar() {
        if (WindowToken.ENTITY_CARD_CHANGE == windowToken) {
            showOperateButtons(new View[]{});
        } else {
            if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                showOperateButtons(printBtn);
            } else {
                showOperateButtons(printBtn);
            }
        }
    }

    @Click({R.id.refund_btn, R.id.print_btn, R.id.recision_btn, R.id.cancel_btn,})
    protected void clickBottomButtons(View view) {
        switch (view.getId()) {
            case R.id.refund_btn:
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }
                if (windowToken == WindowToken.CARD_SALE && orderCategory == OrderCategory.PAYED) {                    if (isCheckMode) {
                        VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_STORE_CANCEL,
                                new VerifyHelper.Callback() {
                                    @Override
                                    public void onPositive(User user, String code, Auth.Filter filter) {
                                        super.onPositive(user, code, filter);
                                        Log.i(TAG, getString(R.string.call_return_interface));
                                        final List<CustomerSaleCardInfo> selectedCards = adapter.getSelectItems();
                                        if (selectedCards == null || selectedCards.size() == 0) {
                                            ToastUtil.showShortToast(getString(R.string.select_check_card));
                                            return;
                                        }
                                        showReasonDialog(new OperateListener() {
                                            @Override
                                            public boolean onSuccess(OperateResult result) {
                                                doReturnCards(result.reason, selectedCards);
                                                return true;
                                            }
                                        }, "memberStoreRevoke");
                                    }
                                });
                    } else {
                        rightTabRL.performClick();
                        if (adapter.getCanRefundNumber() == 0) {
                            ToastUtil.showLongToast(R.string.customer_ordercenter_detail_cannot_refund_tip);
                        } else if (adapter.getCanRefundNumber() == 1) {
                            showReasonDialog(new OperateListener() {
                                @Override
                                public boolean onSuccess(OperateResult result) {
                                    CustomerSaleCardInfo cardSaleInfo = adapter.getRefundSingleCard();
                                    doReturnCards(result.reason, Collections.singletonList(cardSaleInfo));
                                    return true;
                                }
                            }, "memberStoreRevoke");
                        } else {
                            showCheckMode(true);
                        }
                    }
                } else if (windowToken == WindowToken.MEMBER_STORE_VALUE && orderCategory == OrderCategory.PAYED) {
                    VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_STORE_CANCEL,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    Log.i(TAG, "会员储值撤销");
                                    doReterMember();
                                }
                            });
                } else if (windowToken == WindowToken.CARD_STORE_VALUE && orderCategory == OrderCategory.PAYED) {
                    VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_STORE_CANCEL,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    showReasonDialog(new OperateListener() {

                                        @Override
                                        public boolean onSuccess(OperateResult result) {
                                            cardStoreValueRevoke(result.reason);
                                            return true;
                                        }
                                    }, "cardStoreValueRevoke");
                                }
                            });

                }
                break;
            case R.id.print_btn:
                if (!ClickManager.getInstance().isClicked()) {
                    doEntityCardTicketReprint();
                }
                break;
            case R.id.recision_btn:
                if (!ClickManager.getInstance().isClicked()) {
                    doEntityCardSaleRecordRecision();
                }
                break;
            case R.id.cancel_btn:
                showCheckMode(false);
                break;

            default:
                break;
        }
    }


    private void doEntityCardTicketReprint() {
        if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {

        } else {
            if (windowToken == WindowToken.MEMBER_STORE_VALUE || windowToken == WindowToken.CARD_STORE_VALUE) {
                if (orderCategory == OrderCategory.PAYED) {
                    EntityCardMananger.printStoreValue(mOrderBean, paymentAndMemberResp == null ? null : paymentAndMemberResp.getPaymentItems(), 0, true);
                } else if (orderCategory == OrderCategory.RETURNED) {
                    EntityCardMananger.printStoreValue(mOrderBean, paymentAndMemberResp == null ? null : paymentAndMemberResp.getPaymentItems(), 1, true);
                }
            } else if (windowToken == WindowToken.CARD_SALE) {
                EntityCardMananger.printSaleCard(sellcardDetailResp);
            }
        }
    }


    private void doEntityCardSaleRecordRecision() {
        if (windowToken == WindowToken.CARD_SALE && orderCategory == OrderCategory.NOT_PAYED && currentTrade != null) {
            if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                anonymousEntityCardSaleRecordInvalid(currentTrade.getId());
            } else {
                saleCardRecordInvaliate(currentTrade.getId(), currentTrade.getServerUpdateTime());
            }
        }
    }


    private void doReterMember() {
        showReasonDialog(new OperateListener() {
            @Override
            public boolean onSuccess(OperateResult result) {
                Reason mReason = result.reason;
                ResponseListener<CustomerMemberStoreValueRevokeResp> listener =
                        new ResponseListener<CustomerMemberStoreValueRevokeResp>() {

                            @Override
                            public void onResponse(ResponseObject<CustomerMemberStoreValueRevokeResp> response) {
                                if (ResponseObject.isOk(response)) {
                                    ToastUtil.showLongToast(response.getMessage());

                                    EntityCardMananger.printStoreValue(mOrderBean, paymentAndMemberResp == null ? null : paymentAndMemberResp.getPaymentItems(), 1, false);
                                    updateParentFragment();
                                } else {
                                    ToastUtil.showLongToast(response.getMessage());
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                ToastUtil.showLongToast(error.getMessage());
                            }
                        };

                EntityCardMananger.memberStoreRevoke(mOrderBean, mReason, listener, getFragmentManager());
                return true;
            }
        }, "");
    }


    private void doReturnCards(Reason reason, List<CustomerSaleCardInfo> selectedCards) {
        boolean isReturnAll = adapter.isRefundAll(selectedCards);
        mReturnCardDataModel.initData(selectedCards);
        TradeVo tradeVo = mReturnCardDataModel.createTradeVo(CustomerApplication.mCustomerBussinessType);
        List<PaymentVo> paymentVoList = mReturnCardDataModel.getReturnPayment(tradeVo.getTrade());

        ResponseListener<SalesCardReturnResp> listener = new ResponseListener<SalesCardReturnResp>() {

            @Override
            public void onResponse(ResponseObject<SalesCardReturnResp> response) {
                try {
                                        if (ResponseObject.isOk(response)) {
                        ToastUtil.showShortToast(getString(R.string.back_card_success));
                        EntityCardMananger.printSaleCard(response.getContent());
                        showCheckMode(false);
                        updateParentFragment();
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }

                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                try {
                                        ToastUtil.showShortToast(error.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        };
        mTradeOperate.returnSellCards(currentTrade,
                tradeVo,
                paymentVoList,
                mReturnCardDataModel.getCardList(),
                reason,
                isReturnAll,
                LoadingResponseListener.ensure(listener, getFragmentManager()));

    }

    private void showCheckMode(boolean show) {
        isCheckMode = show;

        selcetHeadLL.setVisibility(show ? View.VISIBLE : View.GONE);
                if (show) {
            allCheckBox.setChecked(false);
            checkNumberTv.setText(getString(R.string.customer_ordercenter_detail_checktip, "0"));
        } else {
            adapter.resetSelectStatus();
        }

        tableHeadLL.setVisibility(show ? View.GONE : View.VISIBLE);
        customerMemberTitle.setVisibility(show ? View.GONE : View.VISIBLE);
        adapter.showCheckBox(show);
        if (show) {
            showOperateButtons(cancel_btn, refundBtn);
        } else {
            initalOperateToolBar();
        }

    }


    private void saleCardRecordInvaliate(long tradeId, long serverUpdateTime) {
        CustomerSaleCardInvalidReq req = new CustomerSaleCardInvalidReq();
        req.setTradeId(tradeId);
        req.setServerUpdateTime(serverUpdateTime);

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CustomerSaleCardInvalidResp> listener = new ResponseListener<CustomerSaleCardInvalidResp>() {

            @Override
            public void onResponse(ResponseObject<CustomerSaleCardInvalidResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(response.getMessage());
                    EntityCardMananger.printSaleCard(response.getContent());
                    updateParentFragment();
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };

        customerOperates.saleCardRecordInvaliate(req, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }


    private void anonymousEntityCardSaleRecordInvalid(Long tradeId) {
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CustomerSaleCardInvalidResp> listener = new ResponseListener<CustomerSaleCardInvalidResp>() {

            @Override
            public void onResponse(ResponseObject<CustomerSaleCardInvalidResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(response.getMessage());
                    EntityCardMananger.printSaleCard(response.getContent());
                    updateParentFragment();
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };

        customerOperates.anonymousEntityCardSaleRecordInvalid(tradeId, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }


    private void cardStoreValueRevoke(Reason reason) {
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<Object> listener = new ResponseListener<Object>() {

            @Override
            public void onResponse(ResponseObject<Object> response) {
                if (ResponseObject.isOk(response)) {
                    updateParentFragment();
                    EntityCardMananger
                            .printStoreValue(mOrderBean, paymentAndMemberResp == null ? null : paymentAndMemberResp.getPaymentItems(), 1, false);
                }
                ToastUtil.showLongToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };

        customerOperates.cardStoreValueRevokeReq(mOrderBean, reason, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }


    public void updateParentFragment() {
        CustomerOrdercenterFragment parentFragment =
                (CustomerOrdercenterFragment) getFragmentManager().findFragmentByTag("CustomerOrdercenterFragment");
        parentFragment.update(windowToken, orderCategory);
    }

    public void showReasonDialog(OperateListener listener, String tag) {
        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", ReasonType.TRADE_RETURNED.value());
        dialog.setArguments(bundle);
        dialog.registerListener(listener);
        dialog.isShowPrint(false);
        dialog.registerCloseListener(new OperateCloseListener() {

            @Override
            public void onClose(OperateResult result) {
            }
        });
        dialog.show(getFragmentManager(), tag);
    }


    private String getCardStatus(String cardNumber, List<CardInfo> cardInfos) {
        String result = "";
        String cardStatusCode = "";
        if (cardInfos != null) {
            for (CardInfo temp : cardInfos) {
                if (temp.getCardNum().equals(cardNumber)) {
                    cardStatusCode = temp.getCardStatus();
                }
            }

            String[] arrays = getResources().getStringArray(R.array.customer_ordercenter_detail_card_status);
            if (cardStatusCode.equals("0")) {
                result = arrays[0];
            } else if (cardStatusCode.equals("1")) {
                result = arrays[1];
            } else if (cardStatusCode.equals("2")) {
                result = arrays[2];
            } else if (cardStatusCode.equals("3")) {
                result = arrays[3];
            } else if (cardStatusCode.equals("4")) {
                result = arrays[4];
            } else if (cardStatusCode.equals("5")) {
                result = arrays[5];
            }

        }

        return result;

    }

    private void getEntityCardChangeDetailInfo(Long tradeId) {
        ResponseListener<EntityCardChangeDetailResp> listener = new ResponseListener<EntityCardChangeDetailResp>() {
            @Override
            public void onResponse(ResponseObject<EntityCardChangeDetailResp> response
            ) {
                if (ResponseObject.isOk(response)) {
                    entityCardChangeDetailResponseConvert(response.getContent());
                    adapter =
                            new CustomerOrdercenterDetaillAdapter(getActivity(), leftCommonBeans,
                                    CustomerOrdercenterDetailCommonBean.class);

                    orderDetailList.setVisibility(View.VISIBLE);
                    llCustomerEntityCardChangeInfo.setVisibility(View.GONE);
                    orderDetailList.setAdapter(adapter);

                    showCheckMode(false);
                    updateTab(R.id.left_tab_rl);
                } else {
                    mDetailFrame.setVisibility(View.GONE);
                    ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
                }
                ToastUtil.showLongToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mDetailFrame.setVisibility(View.GONE);
                ivCustomerCenterOrderEmpty.setVisibility(View.VISIBLE);
            }
        };

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates
                .getEntityCardChangeDetailInfo(tradeId, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    private void entityCardChangeDetailResponseConvert(EntityCardChangeDetailResp response) {
        if (Utils.isNotEmpty(response.getTrades())) {
            currentTrade = response.getTrades().get(0);
        }

        if (leftCommonBeans == null) {
            leftCommonBeans = new ArrayList<CustomerOrdercenterDetailCommonBean>();
        } else {
            leftCommonBeans.clear();
        }

        CustomerOrdercenterDetailCommonBean bean;
        if (Utils.isNotEmpty(response.getPayments())) {
            Payment payment = response.getPayments().get(0);

            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.customer_order_center_new_card_fee));
            bean.setValue(payment.getReceivableAmount().toString());
            leftCommonBeans.add(bean);
        }


                List<PaymentItem> paymentItems = response.getPaymentItems();
        BigDecimal actualAmount = BigDecimal.ZERO;        if (paymentItems != null) {
            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.customer_order_center_pay_mode));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < paymentItems.size(); i++) {
                PaymentItem item = paymentItems.get(i);
                if (item.getPayStatus() == TradePayStatus.PAID || item.getPayStatus() == TradePayStatus.REFUNDED) {
                    if (!sb.toString().contains(PaySettingCache.getPayModeNameByModeId(item.getPayModeId()))) {
                        sb.append(PaySettingCache.getPayModeNameByModeId(item.getPayModeId()));
                        sb.append("、");
                    }

                    actualAmount = actualAmount.add(item.getUsefulAmount());
                }
            }
            String payModeNames = sb.toString();
            if (payModeNames.endsWith("、")) {
                payModeNames = payModeNames.substring(0, payModeNames.length() - 1);
            }
            bean.setValue(payModeNames);
            leftCommonBeans.add(bean);

            bean = new CustomerOrdercenterDetailCommonBean();
            bean.setName(getString(R.string.customer_order_center_actual_amount));
            bean.setValue(actualAmount.toString());
            leftCommonBeans.add(bean);
        }




    }

    private void getCardDetailInfo(String cardNo) {
        ResponseListener<CustomerCardInfoResp> listener =
                new ResponseListener<CustomerCardInfoResp>() {
                    @Override
                    public void onResponse(ResponseObject<CustomerCardInfoResp> response) {
                        if (ResponseObject.isOk(response)) {
                            if (response.getContent() != null
                                    && response.getContent().getResult() != null) {
                                CustomerCardInfoResult customerCardInfoResult = response.getContent().getResult();

                                if (rightCommonBeans == null) {
                                    rightCommonBeans = new ArrayList<CustomerOrdercenterDetailCommonBean>();
                                } else {
                                    rightCommonBeans.clear();
                                }

                                CustomerOrdercenterDetailCommonBean bean = new CustomerOrdercenterDetailCommonBean();
                                bean.setName(getString(R.string.customer_entity_card_remain_value));
                                bean.setValue(String.valueOf(
                                        customerCardInfoResult.getRemainValue() != null ? customerCardInfoResult.getRemainValue() : 0));
                                rightCommonBeans.add(bean);

                                bean = new CustomerOrdercenterDetailCommonBean();
                                bean.setName(getString(R.string.available_points));
                                bean.setValue(
                                        String.valueOf(customerCardInfoResult.getIntegral() != null ? customerCardInfoResult.getIntegral() : 0));
                                rightCommonBeans.add(bean);

                                bean = new CustomerOrdercenterDetailCommonBean();
                                bean.setName(getString(R.string.customer_entity_card_type));
                                EntityCardType cardType = ValueEnums.toEnum(EntityCardType.class, customerCardInfoResult.getCardType());
                                if (EntityCardType.CUSTOMER_ENTITY_CARD == cardType) {
                                    bean.setValue(getString(R.string.customer_entity_card_type_customer));
                                } else if (EntityCardType.ANONYMOUS_ENTITY_CARD == cardType) {
                                    bean.setValue(getString(R.string.customer_entity_card_type_anonymous));
                                } else if (EntityCardType.GENERAL_CUSTOMER_CARD == cardType) {
                                    bean.setValue(getString(R.string.customer_entity_card_type_general));
                                }
                                rightCommonBeans.add(bean);
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                };
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        operates.getCardInfoByNum(toCardSingleSearchByTransReq(cardNo), listener);
    }

    private CardSingleSearchByTransReq toCardSingleSearchByTransReq(String cardNo) {

        return null;
    }
}
