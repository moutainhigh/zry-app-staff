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

/**
 * @Date：2016年3月14日 @Description:订单详情
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
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
    RelativeLayout leftTabRL;// 左边tab

    @ViewById(R.id.left_tab_tv)
    TextView leftTabTv;// 左边tab文字

    @ViewById(R.id.left_tab_line)
    View leftTabLine;// 左边tab指示线

    @ViewById(R.id.middle_tab_rl)
    RelativeLayout middleTabRL;// 中间tab

    @ViewById(R.id.middle_tab_tv)
    TextView middleTabTv;// 中间tab文字

    @ViewById(R.id.middle_tab_line)
    View middleTabLine;// 中间tab指示线

    @ViewById(R.id.right_tab_rl)
    RelativeLayout rightTabRL;// 右边tab

    @ViewById(R.id.right_tab_tv)
    TextView rightTabTv;// 右边tab文字

    @ViewById(R.id.right_tab_line)
    View rightTablLine;// 右边tab指示线

    @ViewById(R.id.table_head_ll)
    LinearLayout tableHeadLL;// 表头

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
    Button refundBtn;// 退货

    @ViewById(R.id.print_btn)
    Button printBtn;// 打印

    @ViewById(R.id.recision_btn)
    Button recisionBtn;// 作废

    @ViewById(R.id.cancel_btn)
    Button cancel_btn;// 取消按钮

    @ViewById(R.id.select_head_ll)
    LinearLayout selcetHeadLL;

    @ViewById(R.id.allcheck_cb)
    CheckBox allCheckBox;

    @ViewById(R.id.check_number_tv)
    TextView checkNumberTv;

    @ViewById(R.id.customer_memeber_tile)
    LinearLayout customerMemberTitle;// 选项栏

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

    private List<CustomerOrdercenterDetailCommonBean> leftCommonBeans;// 左tab数据

    //private EntityCardChangeDetailBean middleBean;//中间tab数据(目前仅换卡存在中间tab)

    //private List<CardSellCardsDetail> rightCardBeans;// 售卡数据

    private List<CustomerOrdercenterDetailCommonBean> rightCommonBeans; // 右tab数据

    private List<CustomerOrdercenterDetailCommonBean> extraCommonBeans;

    private int currentTabId = R.id.left_tab_rl;//当前选中的分栏Id

    private WindowToken windowToken;// 属于哪个标题tab

    private OrderCategory orderCategory;// 订单类别

    private EntityCardType entityCardType; //实体卡类别

    private boolean isCheckMode = false;// 是否是选择模式

    private Trade currentTrade;// 保存当前订单信息（售卡记录详情）

    private CustomerOrderBean mOrderBean;

    private ReturnCardDataModel mReturnCardDataModel;

    private TradeOperates mTradeOperate;

    private CustomerSellcardDetailResp sellcardDetailResp;// 售卡订单详情数据

    private PaymentAndMemberResp paymentAndMemberResp;// 会员储值或者实体储值详情数据

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
            /*String orderNo = middleBean.getOrderNo();
            if (TextUtils.isEmpty(orderNo)) {
                tvOrderNo.setText(getString(R.string.customer_entity_card_change_info_order_no, ""));
            } else {
                tvOrderNo.setText(getString(R.string.customer_entity_card_change_info_order_no, middleBean.getOrderNo()));
            }

            String oldCardNo = middleBean.getOldCardNum();
            if (TextUtils.isEmpty(oldCardNo)) {
                tvOldCardNo.setText(getString(R.string.customer_entity_card_change_info_old_card_num, ""));
            } else {
                tvOldCardNo.setText(getString(R.string.customer_entity_card_change_info_old_card_num, middleBean.getOldCardNum()));
            }

            String oldCardStatus = middleBean.getOldCardStatus();
            if (TextUtils.isEmpty(oldCardStatus)) {
                tvOldCardStatus.setText(getString(R.string.customer_entity_card_change_info_old_card_status, ""));
            } else {
                tvOldCardStatus.setText(getString(R.string.customer_entity_card_change_info_old_card_status, middleBean.getOldCardStatus()));
            }

            String newCardNum = middleBean.getNewCardNum();
            if (TextUtils.isEmpty(newCardNum)) {
                tvNewCardNo.setText(getString(R.string.customer_entity_card_change_info_new_card_num, ""));
            } else {
                tvNewCardNo.setText(getString(R.string.customer_entity_card_change_info_new_card_num, middleBean.getNewCardNum()));
            }

            String newCardStatus = middleBean.getNewCardStatus();
            if (TextUtils.isEmpty(newCardStatus)) {
                tvNewCardStatus.setText(getString(R.string.customer_entity_card_change_info_new_card_status, ""));
            } else {
                tvNewCardStatus.setText(getString(R.string.customer_entity_card_change_info_new_card_status, middleBean.getNewCardStatus()));
            }*/
        } else {
            /*if (windowToken == WindowToken.CARD_SALE) {// 售卡详情
                adapter = new CustomerOrdercenterDetaillAdapter(getActivity(), *//*rightCardBeans, CardSellCardsDetail.class*//*);
            } else {*/
            if (windowToken == WindowToken.MEMBER_STORE_VALUE) {// 会员储值
                rightCommonBeans = filterMemberinfo(extraCommonBeans);
            }
            adapter = new CustomerOrdercenterDetaillAdapter(getActivity(), rightCommonBeans, CustomerOrdercenterDetailCommonBean.class);
            //}
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

    //更新Tab
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

    /**
     * @Date 2016年3月16日
     * @Description: 获取售卡详情接口
     * @Return void
     */
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

    /**
     * 获取匿名实体卡销售详情
     *
     * @param tradeId
     */
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

    /**
     * @param event
     * @Date 2016年3月16日
     * @Description: 售卡记录点击事件接收
     * @Return void
     */
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

        if (event.windowToken == WindowToken.CARD_SALE) {// 售卡详情
            memberHeadInfo.setVisibility(View.GONE);
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

			/*if (event.windowToken == WindowToken.CARD_STORE_VALUE) {// 实体卡记录详情
				if (event.orderBean != null) {
                    final CustomerSellOrderBean bean = event.orderBean;
                    getCardAccount(event.orderBean.getCardNo(), new ResponseListener<CardAccountResp>() {
                        @Override
                        public void onResponse(ResponseObject<CardAccountResp> response) {
                            CardAccountResp.CardAccountItem result = response.getContent().getResult();
                            bean.setCardType(ValueEnums.toEnum(EntityCardType.class, result.cardBaseInfo.cardType));
                            bean.setRemainValue(result.cardStoreAccount.remainValue);
                            bean.setIntegral(result.cardIntegralAccount.integral);
                            bean.setCardKindKame(result.cardBaseInfo.cardKindName);
                            bean.setCardStatus(result.cardBaseInfo.cardStatus);
                            saveCardInfo(bean);
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
				}
				rightTabTv.setText(tabStr[1]);
			} else {// 会员储值详情
				rightTabTv.setText(tabStr[2]);
			}*/

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

    //刷新中间的Tab
    private void refreshMiddleTab() {
        if (windowToken != null && WindowToken.ENTITY_CARD_CHANGE == windowToken) {
            middleTabRL.setVisibility(View.VISIBLE);
        } else {
            middleTabRL.setVisibility(View.GONE);
        }
    }

    /**
     * @Date 2016年3月17日
     * @Description: 售卡记录详情数据转换
     * @Return void
     */
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
            // 虚拟会员无显示问题
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

        // 支付明细
        if (response.getPaymentItems() != null) {
            for (PaymentItem item : response.getPaymentItems()) {
                if (item.getPayStatus() == TradePayStatus.PAID || item.getPayStatus() == TradePayStatus.REFUNDED) {
                    bean = new CustomerOrdercenterDetailCommonBean();
                    bean.setName(PaySettingCache.getPayModeNameByModeId(item.getPayModeId()) + "：");// 用paymodeid去缓存里取支付方式的名称
                    bean.setValue(item.getUsefulAmount().toString());
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

        /*if (rightCardBeans == null) {
            rightCardBeans = new ArrayList<CardSellCardsDetail>();
        } else {
            rightCardBeans.clear();
        }
        // 解析卡状态列表
        List<CardInfo> cardInfos = response.getCardInfos();
        // 解析卡号详情
        List<CustomerSaleCardInfo> cardSaleInfos = response.getCardSaleInfos();
        if (cardSaleInfos != null) {
            int size = cardSaleInfos.size();
            for (int i = 0; i < size; i++) {
                CardSellCardsDetail sellCardsDetail = new CardSellCardsDetail();
                sellCardsDetail.setSerialNumber(String.valueOf(i));
                CustomerSaleCardInfo tempInfo = cardSaleInfos.get(i);
                sellCardsDetail.setCardNumber(tempInfo.getCardNo());
                sellCardsDetail.setCardCategory(tempInfo.getCardKind());
                sellCardsDetail.setCardStatus(getCardStatus(tempInfo.getCardNo(), cardInfos));
                sellCardsDetail.setSellPrice(String.valueOf(tempInfo.getCardCost()));
                sellCardsDetail.setDealStatus(getDealStatus(tempInfo));

                if (sellCardsDetail.getCardStatus().equals(getString(R.string.eccard_unactive))
                        && response.getTrades().get(0).getTradePayStatus() == TradePayStatus.PAID) {// 可选
                    sellCardsDetail.setSelctType(SelectType.NOT_SELECT);
                } else {// 不可选
                    sellCardsDetail.setSelctType(SelectType.SELECT_INVALIATE);
                }

                sellCardsDetail.setCardSaleInfo(tempInfo);
                rightCardBeans.add(sellCardsDetail);
            }

        }*/
    }

    /**
     * @param cardSaleInfo
     * @return
     * @Date 2016年4月7日
     * @Description: 解析卡交易状态
     * @Return String
     */
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

    /**
     * @param paymentUuid
     * @param memberId
     * @Date 2016年3月23日
     * @Description: 查询支付和会员信息
     * @Return void
     */
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

                    // 刷新head
                    nameTv.setText(extraCommonBeans.get(0).getValue());
                    sexTv.setText(extraCommonBeans.get(1).getValue());
                    memberLevelTv.setText(extraCommonBeans.get(2).getValue());

                    //下个级别为null时改提示
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

    /**
     * 根据paymentUuid查询支付信息
     *
     * @param paymentUuid
     */
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
            // 虚拟会员无显示问题
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

        // 支付明细
        if (response.getPaymentItems() != null) {
            for (PaymentItem item : response.getPaymentItems()) {
                if (item.getPayStatus() == TradePayStatus.PAID || item.getPayStatus() == TradePayStatus.REFUNDED) {
                    bean = new CustomerOrdercenterDetailCommonBean();
                    bean.setName(PaySettingCache.getPayModeNameByModeId(item.getPayModeId()) + "：");// 用paymodeid去缓存里取支付方式的名称
                    bean.setValue(item.getUsefulAmount().toString());
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

        // 解析额外的会员信息
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
            if (member.getSex() == null) {//modify by zhubo 2016-8-1解决空指针bug
                tempBean.setValue(getString(R.string.gender_unknow));
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

    /**
     * @param orderBean
     * @Date 2016年3月22日
     * @Description:实体卡储值需保存实体卡信息 (点击列表传过来)
     * @Return void
     */
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

    /**
     * @param inputBeans
     * @return
     * @Date 2016年3月23日
     * @Description: 过滤会员信息，去除头部信息
     * @Return List<CustomerOrdercenterDetailCommonBean>
     */
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

    /**
     * @param views
     * @Date 2016年3月31日
     * @Description: 展示操作按钮
     * @Return void
     */
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
                        //TODO 需要一个中间颜色
                    }
                    views[i].setVisibility(View.VISIBLE);
                }
            }
            operateLL.setVisibility(View.VISIBLE);
        } else {
            operateLL.setVisibility(View.GONE);
        }
    }

    /**
     * @Date 2016年3月31日
     * @Description: 根据界面和订单类别显示操作按钮
     * @Return void
     */
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

    //刷新未支付底部操作栏
    private void refreshNotPayedOperateToolBar() {
        /*if (WindowToken.ENTITY_CARD_CHANGE == windowToken
                || (windowToken == WindowToken.CARD_STORE_VALUE && entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD)) {
            showOperateButtons(new View[]{});
        } else {
            showOperateButtons(recisionBtn);
        }*/

        if (windowToken == WindowToken.CARD_SALE && orderCategory == OrderCategory.NOT_PAYED && currentTrade != null) {
            showOperateButtons(recisionBtn);
        } else {
            showOperateButtons(new View[]{});
        }
    }

    //刷新已支付底部操作栏
    private void refreshPayedOperateToolBar() {
        if (WindowToken.ENTITY_CARD_CHANGE == windowToken) {
            showOperateButtons(new View[]{});
        } else {
            //已支付匿名实体卡销售或储值记录无法退货、打印
            if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                showOperateButtons(printBtn);
            } else {
                //实体卡储值已结账记录如果是消费数据则只显示打印按钮
                if ((windowToken != null && windowToken == WindowToken.CARD_STORE_VALUE) && (mOrderBean != null && mOrderBean.getStoreType() != null
                        && mOrderBean.getStoreType() == 2)) {
                    showOperateButtons(printBtn);
                } else {
                    showOperateButtons(refundBtn, printBtn);
                }
            }
        }
    }

    //刷新已作废底部操作栏
    private void refreshInvalidOperateToolBar() {
        if (WindowToken.ENTITY_CARD_CHANGE == windowToken) {
            showOperateButtons(new View[]{});
        } else {
            //已作废匿名实体卡销售或储值记录无法打印
            if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                showOperateButtons(new View[]{});
            } else {
                showOperateButtons(printBtn);
            }
        }
    }

    //刷新已退货底部操作栏
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
                if (windowToken == WindowToken.CARD_SALE && orderCategory == OrderCategory.PAYED) {// 售卡-已结账
                    if (isCheckMode) {
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

    /**
     * 实体卡票据补打
     */
    private void doEntityCardTicketReprint() {
        if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
            /*if (windowToken == WindowToken.CARD_SALE) {
                if (orderCategory == OrderCategory.PAYED) {
                    PrintTool.printAnonyCardTicket(sellcardDetailResp, false);
                } else if (orderCategory == OrderCategory.RETURNED) {
                    PrintTool.printAnonyCardTicket(sellcardDetailResp, true);
                }
            } else if (windowToken == WindowToken.CARD_STORE_VALUE) {
                PrintTool.printAnonyCardTicket(mOrderBean.getCardNo(), null, paymentAndMemberResp);
            }*/
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

    /**
     * 实体卡销售记录作废
     */
    private void doEntityCardSaleRecordRecision() {
        if (windowToken == WindowToken.CARD_SALE && orderCategory == OrderCategory.NOT_PAYED && currentTrade != null) {
            if (entityCardType == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                anonymousEntityCardSaleRecordInvalid(currentTrade.getId());
            } else {
                saleCardRecordInvaliate(currentTrade.getId(), currentTrade.getServerUpdateTime());
            }
        }
    }

    /**
     * @Title: doReterMember
     * @Description: 会员撤销
     * @Param TODO
     * @Return void 返回类型
     */
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

    /**
     * @Description: 退卡处理
     */
    private void doReturnCards(Reason reason, List<CustomerSaleCardInfo> selectedCards) {
        boolean isReturnAll = adapter.isRefundAll(selectedCards);
        mReturnCardDataModel.initData(selectedCards);
        TradeVo tradeVo = mReturnCardDataModel.createTradeVo(CustomerApplication.mCustomerBussinessType);
        List<PaymentVo> paymentVoList = mReturnCardDataModel.getReturnPayment(tradeVo.getTrade());

        ResponseListener<SalesCardReturnResp> listener = new ResponseListener<SalesCardReturnResp>() {

            @Override
            public void onResponse(ResponseObject<SalesCardReturnResp> response) {
                try {
                    // 退卡成功
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
                    // 退卡出错
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
        //初始化checkbox显示，解决切换item时，初始化错误的bug
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

    /**
     * @param tradeId
     * @Date 2016年4月5日
     * @Description: 售卡记录作废
     * @Return void
     */
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

    /**
     * 匿名实体卡销售记录作废
     *
     * @param tradeId 销售记录Trade Id
     */
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

    /**
     * @Date 2016年4月5日
     * @Description: 实体卡储值撤销
     * @Return void
     */
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

    /**
     * @Date 2016年4月5日
     * @Description: 更新父界面UI
     * @Return void
     */
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

    /**
     * @param cardNumber
     * @return
     * @Date 2016年4月7日
     * @Description: 通过卡号获取卡状态
     * @Return String
     */
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


        // 支付明细
        List<PaymentItem> paymentItems = response.getPaymentItems();
        BigDecimal actualAmount = BigDecimal.ZERO;//实付金额
        if (paymentItems != null) {
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

        /*if (rightCardBeans == null) {
            rightCardBeans = new ArrayList<CardSellCardsDetail>();
        } else {
            rightCardBeans.clear();
        }

        // 解析卡状态列表
        List<CardInfo> cardInfos = response.getCardInfos();
        //转换中间tab数据，即换卡数据
        middleBean = new EntityCardChangeDetailBean();
        if (currentTrade != null) {
            middleBean.setOrderNo(currentTrade.getTradeNo());
        }
        List<CardChangeInfo> cardChangeInfos = response.getCardChangeInfos();
        if (Utils.isNotEmpty(cardChangeInfos)) {
            //由于目前仅支持单张换卡，故此处仅取第一条数据即可
            CardChangeInfo cardChangeInfo = cardChangeInfos.get(0);
            middleBean.setOldCardNum(cardChangeInfo.getOldCardNum());
            middleBean.setOldCardStatus(getCardStatus(cardChangeInfo.getOldCardNum(), cardInfos));
            middleBean.setNewCardNum(cardChangeInfo.getNewCardNum());
            middleBean.setNewCardStatus(getCardStatus(cardChangeInfo.getNewCardNum(), cardInfos));
        }*/

        //获取状态为激活的那张卡
        /*if (Utils.isNotEmpty(cardInfos)) {
            String activatedCardNum = "";
            for (CardInfo cardInfo : cardInfos) {
                if ("3".equals(cardInfo.getCardStatus()) && middleBean.getNewCardNum().equals(cardInfo.getCardNum())) {
                    activatedCardNum = cardInfo.getCardNum();
                    break;
                }
            }

            if (!TextUtils.isEmpty(activatedCardNum)) {
                getCardDetailInfo(activatedCardNum);
            }
        }*/
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
        /*CardSingleSearchByTransReq singleSearchReq = SearchCardDataModel
                .createCardSingleSearchByTransReq(100, CardStatus.UNSELL, cardNo, EntityCardCommercialType.SELL_SHOP.value(),
                        EntityCardType.CUSTOMER_ENTITY_CARD.value(), EntityCardType.GENERAL_CUSTOMER_CARD.value());
        return singleSearchReq;*/
        return null;
    }
}
