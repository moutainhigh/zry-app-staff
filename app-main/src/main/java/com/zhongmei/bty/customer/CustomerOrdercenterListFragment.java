package com.zhongmei.bty.customer;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.customer.bean.CustomerOrderBean;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardResp;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.OrderCategory;
import com.zhongmei.bty.customer.CustomerOrdercenterFragment.WindowToken;
import com.zhongmei.bty.customer.adapter.CustomerOrdercenterAdapter;
import com.zhongmei.bty.customer.event.EventClickOrdercenterListItem;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.bean.req.CustomerCardTimeStoreReq;
import com.zhongmei.yunfu.bean.req.CustomerCardTimeStoreResp;
import com.zhongmei.yunfu.bean.req.CustomerMemberStoreReq;
import com.zhongmei.yunfu.bean.req.CustomerMemberStoreResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.customer_ordercenter_list)
public class CustomerOrdercenterListFragment extends BasicFragment implements OnItemClickListener {
    private final String TAG = CustomerOrdercenterListFragment.class.getSimpleName();
        private static final int PAGE_SIZE = 50;

    @ViewById(R.id.order_list)
    ListView orderList;

    @ViewById(R.id.search_edit)
    EditText searchEdit;

    @ViewById(R.id.delete_content_iv)
    ImageView delteContentIv;

    @ViewById(R.id.read_card_btn)
    Button readCardBtn;

    @ViewById(R.id.search_btn)
    Button searchBtn;

    @ViewById(R.id.tab1_ll)
    LinearLayout tab1LL;

    @ViewById(R.id.tab1_tv)
    TextView tab1Tv;

    @ViewById(R.id.tab1_line)
    View tab1Line;

    @ViewById(R.id.tab2_ll)
    LinearLayout tab2LL;

    @ViewById(R.id.tab2_tv)
    TextView tab2Tv;

    @ViewById(R.id.tab2_line)
    View tab2Line;

    @ViewById(R.id.tab3_ll)
    LinearLayout tab3LL;

    @ViewById(R.id.tab3_tv)
    TextView tab3Tv;

    @ViewById(R.id.tab3_line)
    View tab3Line;

    @ViewById(R.id.tab4_ll)
    LinearLayout tab4LL;

    @ViewById(R.id.tab4_tv)
    TextView tab4Tv;

    @ViewById(R.id.tab4_line)
    View tab4Line;

    @ViewById(R.id.customer_ordercenter_empty)
    RelativeLayout mOrderCenter_empty;

    @ViewById(R.id.customer_ordercenter_head)
    View customer_ordercenter_head;

    private Context context;

    private CustomerOrdercenterAdapter adapter;

    private WindowToken windowToken = WindowToken.CARD_SALE;

    private OrderCategory orderCategory = OrderCategory.PAYED;
    @AfterViews
    protected void initView() {
        context = getActivity();
        windowToken = getWindowTokenFromArgs();
        adapter = new CustomerOrdercenterAdapter(context, windowToken);
        orderList.setAdapter(adapter);
        orderList.setOnItemClickListener(this);
                updateSearchEdit(windowToken);
        if (context != null)
            tab2Tv.setTextColor(context.getResources().getColor(R.color.customer_ordercenter_tradecategory_choose));
        tab2Line.setVisibility(View.VISIBLE);

        update(windowToken, orderCategory);
        setupView();
    }

    private void setupView() {
        if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
            tab1Line.setBackgroundColor(getActivity().getResources().getColor(R.color.beauty_color_FF2283));
            tab2Line.setBackgroundColor(getActivity().getResources().getColor(R.color.beauty_color_FF2283));
            tab3Line.setBackgroundColor(getActivity().getResources().getColor(R.color.beauty_color_FF2283));
            tab4Line.setBackgroundColor(getActivity().getResources().getColor(R.color.beauty_color_FF2283));
        }
    }

    private WindowToken getWindowTokenFromArgs() {
        if (getArguments() != null && getArguments().get("window_token") != null) {
            int windowTokenArg = getArguments().getInt("window_token");
            WindowToken token = WindowToken.valueOf(windowTokenArg);
            return token;
        }

        return WindowToken.CARD_SALE;
    }

    private void loadData(WindowToken window, String content, OrderCategory orderCategory) {
        resetData(orderCategory, null);
        switch (window) {
            case CARD_SALE:
                getCustomerSellCardOrders(content, orderCategory);
                break;
            case MEMBER_STORE_VALUE:
                getMemberStoreValueList(content, orderCategory);
                break;
            case CARD_STORE_VALUE:
                getCardStoreValueList(content, orderCategory);
                break;
            case ENTITY_CARD_CHANGE:
                getEntityCardChangeOrders(content, orderCategory);
                break;
            default:
                break;
        }
    }

    public void update(WindowToken window, OrderCategory orderCategory) {
        updateSearchEdit(window);
        windowToken = window;
        this.orderCategory = orderCategory;
        if (orderCategory == OrderCategory.NOT_PAYED) {
            onClickCategoryTab(tab1LL);
        } else if (orderCategory == OrderCategory.PAYED) {
            onClickCategoryTab(tab2LL);
        } else if (orderCategory == OrderCategory.RETURNED) {
            onClickCategoryTab(tab3LL);
        } else if (orderCategory == OrderCategory.INVALID) {
            onClickCategoryTab(tab4LL);
        }
    }


    void getCustomerSellCardOrders(String cardNumber, final OrderCategory orderCategory) {
        CustomerSellcardReq customerSellCardReq = new CustomerSellcardReq();
        customerSellCardReq.setPageSize(50);
        if (cardNumber != null) {
            customerSellCardReq.setCardNo(cardNumber);
        }
        customerSellCardReq.setTradeStatus(getTradeStatus(orderCategory));

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);

        ResponseListener<List<CustomerSellcardResp>> listener = new ResponseListener<List<CustomerSellcardResp>>() {

            @Override
            public void onResponse(ResponseObject<List<CustomerSellcardResp>> response) {
                try {
                    if (ResponseObject.isOk(response)) {
                        if (response.getContent() == null || response.getContent().size() == 0) {
                            ToastUtil.showLongToast(R.string.customer_ordercenter_query_no_data_toast);
                            mOrderCenter_empty.setVisibility(View.VISIBLE);
                            orderList.setVisibility(View.GONE);
                        } else {
                            ToastUtil.showLongToast(response.getMessage());
                            List<CustomerOrderBean> beans = sellCardResponseConvert(response.getContent());
                            resetData(orderCategory, beans);
                            if (beans != null && beans.size() > 0) {
                                mOrderCenter_empty.setVisibility(View.GONE);
                                orderList.setVisibility(View.VISIBLE);
                            } else {
                                mOrderCenter_empty.setVisibility(View.VISIBLE);
                                orderList.setVisibility(View.GONE);
                            }
                            if (beans != null && beans.size() > 0) {
                                onItemClick(orderList, null, 0, 0);
                                return;
                            }
                        }

                    } else {
                        ToastUtil.showLongToast(response.getMessage());
                        mOrderCenter_empty.setVisibility(View.VISIBLE);
                        orderList.setVisibility(View.GONE);
                    }
                    post(new EventClickOrdercenterListItem(windowToken, orderCategory, true));
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mOrderCenter_empty.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }
        };
        customerOperates.getCustomerSellCardOrders(customerSellCardReq,
                LoadingResponseListener.ensure(listener, getFragmentManager()));
    }


    private List<CustomerOrderBean> sellCardResponseConvert(List<CustomerSellcardResp> response) {
        if (response == null || response.size() == 0)
            return null;
        List<CustomerOrderBean> sellOrderBeans = new ArrayList<>();
        for (CustomerSellcardResp tempResp : response) {
            CustomerOrderBean bean = new CustomerOrderBean();
            Trade trade = tempResp.getTrade();
            String time = DateTimeUtils.formatDateTime(trade.getTradeTime(), DateTimeUtils.DATE_TIME_FORMAT2);

            bean.setSellTime(time);
            bean.setCardNo(getCardNumber(tempResp.getCardNos()));
            bean.setDeviceNo(tempResp.getPadNo() == null ? "" : tempResp.getPadNo().toString());
            bean.setCardType(tempResp.getCardType());
            bean.setSellMoney(trade.getTradeAmount().toString());
            bean.setOperater(trade.getUpdatorName());
            bean.setTradeId(trade.getId());
                        sellOrderBeans.add(bean);
        }
        return sellOrderBeans;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setChooseItem(position);
        CustomerOrderBean bean = adapter.getItem(position);
        if (bean != null) {
            EventClickOrdercenterListItem eventClickOrdercenterListItem;
            if (windowToken == WindowToken.CARD_SALE || windowToken == WindowToken.ENTITY_CARD_CHANGE) {
                eventClickOrdercenterListItem = new EventClickOrdercenterListItem(windowToken, orderCategory, bean.getTradeId());
            } else {
                eventClickOrdercenterListItem = new EventClickOrdercenterListItem(windowToken, orderCategory, bean);
            }
            eventClickOrdercenterListItem.setEntityCardType(bean.getCardType());
            EventBus.getDefault().post(eventClickOrdercenterListItem);
        }
    }


    private void getCardStoreValueList(String cardNumber, final OrderCategory orderCategory) {
        int tradeStatus = getTradeStatus(orderCategory);
        CustomerCardTimeStoreReq cardStoreValueReq = new CustomerCardTimeStoreReq(tradeStatus, cardNumber, 1, 50);

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        YFResponseListener<YFResponseList<CustomerCardTimeStoreResp>> listener = new YFResponseListener<YFResponseList<CustomerCardTimeStoreResp>>() {

            @Override
            public void onResponse(YFResponseList<CustomerCardTimeStoreResp> response) {
                try {
                    if (YFResponseList.isOk(response)) {
                        if (response.getContent() == null || response.getContent().size() == 0) {
                            ToastUtil.showLongToast(R.string.customer_ordercenter_query_no_data_toast);
                            mOrderCenter_empty.setVisibility(View.VISIBLE);
                            orderList.setVisibility(View.GONE);
                        } else {
                            ToastUtil.showLongToast(response.getMessage());
                            List<CustomerOrderBean> beans =
                                    cardStoreValueListConvert(response.getContent(), orderCategory);
                            resetData(orderCategory, beans);
                            if (beans != null && beans.size() > 0) {
                                mOrderCenter_empty.setVisibility(View.GONE);
                                orderList.setVisibility(View.VISIBLE);
                            } else {
                                mOrderCenter_empty.setVisibility(View.VISIBLE);
                                orderList.setVisibility(View.GONE);
                            }
                            if (beans != null && beans.size() > 0) {
                                onItemClick(orderList, null, 0, 0);
                                return;
                            }
                        }

                    } else {
                        ToastUtil.showLongToast(response.getMessage());
                        mOrderCenter_empty.setVisibility(View.VISIBLE);
                        orderList.setVisibility(View.GONE);
                    }
                    post(new EventClickOrdercenterListItem(windowToken, orderCategory, true));
                } catch (Exception e) {
                    mOrderCenter_empty.setVisibility(View.VISIBLE);
                    orderList.setVisibility(View.GONE);
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mOrderCenter_empty.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }
        };
        customerOperates.getEntityCardStoreHistory(cardStoreValueReq,
                LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }

    private void resetData(OrderCategory orderCategory, List<CustomerOrderBean> beans) {
        adapter.resetData(windowToken, orderCategory, beans);
        customer_ordercenter_head.setVisibility(adapter.isShowOrderStatus() ? View.VISIBLE : View.GONE);
    }





    private List<CustomerOrderBean> cardStoreValueListConvert(List<CustomerCardTimeStoreResp> cardStoreValueItems, OrderCategory orderCategory) {
        if (cardStoreValueItems == null || cardStoreValueItems.size() == 0)
            return null;

        List<CustomerOrderBean> beans = new ArrayList<>();
        for (CustomerCardTimeStoreResp item : cardStoreValueItems) {
            CustomerOrderBean bean = new CustomerOrderBean();
            bean.setTradeId(item.getTradeId());
            bean.setSellTime(DateTimeUtils.formatDateTime(item.getServerCreateTime(), DateTimeUtils.DATE_TIME_FORMAT2));
            bean.setCardNo(item.getCardNum());
            bean.setCardType(item.getCardType());

            Integer tempCardBizType = item.getStoreType();
            bean.setStoreType(tempCardBizType);
            if (tempCardBizType != null) {
                if (tempCardBizType == 0) {                    bean.setSellMoney(context.getString(R.string.customer_ordercenter_storevalue_money,
                            ShopInfoCfg.formatCurrencySymbol(item.getCurrentRealValue() == null ? BigDecimal.ZERO : item.getCurrentRealValue().toString()),
                            ShopInfoCfg.formatCurrencySymbol(item.getCurrentSendValue() == null ? BigDecimal.ZERO : item.getCurrentSendValue().toString())));
                } else if (tempCardBizType == 1) {                    bean.setSellMoney(context.getString(R.string.customer_ordercenter_refundvalue_money,
                            ShopInfoCfg.formatCurrencySymbol(item.getCurrentRealValue() == null ? BigDecimal.ZERO : item.getCurrentRealValue().toString())));
                }
            }

            bean.setOperater(item.getCreatorName());
            bean.setDeviceNo(item.getPadNo() == null ? "" : item.getPadNo().toString());

            bean.setAddValueType(item.getAddValueType());
            bean.setAddValue(item.getCurrentRealValue());
            bean.setSendValue(item.getCurrentSendValue());
                                    bean.setBeforeRealValue(item.getBeforeRealValue());
            bean.setBeforeSendValue(item.getBeforeSendValue());
            bean.setCurrentRealValue(item.getCurrentRealValue());
            bean.setCurrentSendValue(item.getCurrentRealValue());
            bean.setEndRealValue(item.getEndRealValue());
            bean.setEndSendValue(item.getEndSendValue());

            bean.setPaymentUuid(item.getPaymentUuid());
            bean.setCustomerId(item.getCustomerId());
            bean.setHistoryId(item.getHistoryId());
            bean.setClientCreateTime(item.getServerCreateTime());
            bean.setServerUpdateTime(item.getTradeServerUpdateTime());

            bean.setStoreId(item.getHistoryId());
            bean.setBizDate(item.getBizDate());

            bean.setAccountStatus(item.getAccountStatus());
            bean.setTradeStatus(item.getTradeStatus());
            bean.setTradePayStatus(item.getTradePayStatus());
            beans.add(bean);
        }
        return beans;
    }



    private BigDecimal calBeforeValue(BigDecimal endValue, BigDecimal addValue, BigDecimal sendValue) {
        if (endValue != null) {
            BigDecimal beforeValue = endValue.subtract(addValue);
            if (sendValue != null) {
                beforeValue = beforeValue.subtract(sendValue);
            }

            return beforeValue;
        }
        return BigDecimal.ZERO;
    }

    private void getMemberStoreValueList(String phoneNumber, final OrderCategory orderCategory) {
        int tradeStatus = getTradeStatus(orderCategory);
        CustomerMemberStoreReq memberStoreValueReq = new CustomerMemberStoreReq(tradeStatus, phoneNumber, 1, 50);

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        YFResponseListener<YFResponseList<CustomerMemberStoreResp>> listener = new YFResponseListener<YFResponseList<CustomerMemberStoreResp>>() {
            @Override
            public void onResponse(YFResponseList<CustomerMemberStoreResp> response) {
                try {
                    if (YFResponseList.isOk(response)) {
                        if (response.getContent() == null || response.getContent().size() == 0) {
                            ToastUtil.showLongToast(R.string.customer_ordercenter_query_no_data_toast);
                            mOrderCenter_empty.setVisibility(View.VISIBLE);
                            orderList.setVisibility(View.GONE);
                        } else {
                            ToastUtil.showLongToast(response.getMessage());
                            List<CustomerOrderBean> beans =
                                    memberStoreValueListConvert(response.getContent());
                            resetData(orderCategory, beans);
                            if (beans != null && beans.size() > 0) {
                                mOrderCenter_empty.setVisibility(View.GONE);
                                orderList.setVisibility(View.VISIBLE);
                            } else {
                                mOrderCenter_empty.setVisibility(View.VISIBLE);
                                orderList.setVisibility(View.GONE);
                            }
                            if (beans != null && beans.size() > 0) {
                                onItemClick(orderList, null, 0, 0);
                                return;
                            }
                        }
                    } else {
                        ToastUtil.showLongToast(response.getMessage());
                        mOrderCenter_empty.setVisibility(View.VISIBLE);
                        orderList.setVisibility(View.GONE);
                    }
                    post(new EventClickOrdercenterListItem(windowToken, orderCategory, true));
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mOrderCenter_empty.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }
        };
        customerOperates.getMemberStoreHistory(memberStoreValueReq,
                LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }


    private List<CustomerOrderBean> memberStoreValueListConvert(List<CustomerMemberStoreResp> cardStoreValueItems) {


        if (cardStoreValueItems == null || cardStoreValueItems.size() == 0)
            return null;

        List<CustomerOrderBean> beans = new ArrayList<>();
        for (CustomerMemberStoreResp item : cardStoreValueItems) {
            CustomerOrderBean bean = new CustomerOrderBean();
            bean.setTradeId(item.getTradeId());
            bean.setSellTime(DateTimeUtils.formatDateTime(item.getServerCreateTime(), DateTimeUtils.DATE_TIME_FORMAT2));
            bean.setMobile(item.getMobile());

            Integer tempCardBizType = item.getStoreType();
            bean.setStoreType(tempCardBizType);
            bean.setSellMoney(context.getString(R.string.customer_ordercenter_storevalue_money,
                    ShopInfoCfg.formatCurrencySymbol(item.getCurrentRealValue() == null ? BigDecimal.ZERO : item.getCurrentRealValue().toString()),
                    ShopInfoCfg.formatCurrencySymbol(item.getCurrentSendValue() == null ? BigDecimal.ZERO : item.getCurrentSendValue().toString())));
            bean.setOperater(item.getCreatorName());
            bean.setDeviceNo(item.getPadNo() == null ? "" : item.getPadNo().toString());

            bean.setAddValueType(item.getAddValueType());
            bean.setAddValue(item.getCurrentRealValue());
            bean.setSendValue(item.getCurrentSendValue());
                                    bean.setBeforeRealValue(item.getBeforeRealValue());
            bean.setBeforeSendValue(item.getBeforeSendValue());
            bean.setCurrentRealValue(item.getCurrentRealValue());
            bean.setCurrentSendValue(item.getCurrentRealValue());
            bean.setEndRealValue(item.getEndRealValue());
            bean.setEndSendValue(item.getEndSendValue());

            bean.setPaymentUuid(item.getPaymentUuid());
            bean.setCustomerId(item.getCustomerId());
            bean.setHistoryId(item.getHistoryId());
            bean.setClientCreateTime(item.getServerCreateTime());
            bean.setServerUpdateTime(item.getTradeServerUpdateTime());

            bean.setStoreId(item.getHistoryId());
            bean.setBizDate(item.getBizDate());

            bean.setAccountStatus(item.getAccountStatus());
            bean.setTradeStatus(item.getTradeStatus());
            bean.setTradePayStatus(item.getTradePayStatus());
            beans.add(bean);
        }
        return beans;
    }

    @Click({R.id.search_btn, R.id.read_card_btn, R.id.delete_content_iv})
    void onclick(View v) {
        if (v.getId() == R.id.search_btn) {
            String content = searchEdit.getText().toString();
            if (!TextUtils.isEmpty(content) && content.length() > 5) {                loadData(windowToken, content, orderCategory);
            } else {
                ToastUtil.showShortToast(R.string.customer_ordercenter_search_toast);
            }
        } else if (v.getId() == R.id.read_card_btn) {


        } else if (v.getId() == R.id.delete_content_iv) {
            searchEdit.setText("");
            loadData(windowToken, null, orderCategory);
        }

    }

    private void updateSearchEdit(WindowToken windowToken) {
        if (windowToken == windowToken.MEMBER_STORE_VALUE) {
            searchEdit.setHint(R.string.customer_ordercenter_search_hint1);
            readCardBtn.setVisibility(View.GONE);
        } else {
            searchEdit.setHint(R.string.customer_ordercenter_search_hint0);
            readCardBtn.setVisibility(View.VISIBLE);
        }
        searchEdit.setText("");
    }

    @Click({R.id.tab1_ll, R.id.tab2_ll, R.id.tab3_ll, R.id.tab4_ll})
    void onClickCategoryTab(View v) {
        resetCategoryTabs();
        switch (v.getId()) {
            case R.id.tab1_ll:
                orderCategory = OrderCategory.NOT_PAYED;
                if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                    tab1Tv.setTextColor(getResources().getColor(R.color.beauty_color_FF2283));
                } else {
                    tab1Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_choose));
                }
                tab1Line.setVisibility(View.VISIBLE);
                break;
            case R.id.tab2_ll:
                orderCategory = OrderCategory.PAYED;
                if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                    tab2Tv.setTextColor(getResources().getColor(R.color.beauty_color_FF2283));
                } else {
                    tab2Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_choose));
                }
                tab2Line.setVisibility(View.VISIBLE);
                break;
            case R.id.tab3_ll:
                orderCategory = OrderCategory.RETURNED;
                if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                    tab3Tv.setTextColor(getResources().getColor(R.color.beauty_color_FF2283));
                } else {
                    tab3Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_choose));
                }
                tab3Line.setVisibility(View.VISIBLE);
                break;
            case R.id.tab4_ll:
                orderCategory = OrderCategory.INVALID;
                if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                    tab4Tv.setTextColor(getResources().getColor(R.color.beauty_color_FF2283));
                } else {
                    tab4Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_choose));
                }
                tab4Line.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        loadData(windowToken, null, orderCategory);

    }


    private void resetCategoryTabs() {
        tab1Line.setVisibility(View.INVISIBLE);
        tab2Line.setVisibility(View.INVISIBLE);
        tab3Line.setVisibility(View.INVISIBLE);
        tab4Line.setVisibility(View.INVISIBLE);
        tab1Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_defalt));
        tab2Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_defalt));
        tab3Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_defalt));
        tab4Tv.setTextColor(getResources().getColor(R.color.customer_ordercenter_tradecategory_defalt));
    }

    private int getTradeStatus(OrderCategory category) {
        Integer status = 0;
        if (category == OrderCategory.NOT_PAYED) {
            status = TradeStatus.CONFIRMED.value();
        } else if (category == OrderCategory.PAYED) {
            status = TradeStatus.FINISH.value();
        } else if (category == OrderCategory.RETURNED) {
            status = TradeStatus.RETURNED.value();
        } else if (category == OrderCategory.INVALID) {
            status = TradeStatus.INVALID.value();
        }
        return status;

    }


    private String getCardNumber(String cardNumbers) {
        if (TextUtils.isEmpty(cardNumbers)) {
            return "";
        }
        String[] numbers = cardNumbers.split(",");
        if (numbers.length == 1) {
            return numbers[0];
        } else {
            return getString(R.string.many_card);
        }

    }

        private void getEntityCardChangeOrders(String content, final OrderCategory orderCategory) {
        CustomerSellcardReq queryEntityCardChangeOrdersReq = toQueryEntityCardChangeOrdersReq(content, orderCategory);
        ResponseListener<List<CustomerSellcardResp>> listener = new ResponseListener<List<CustomerSellcardResp>>() {

            @Override
            public void onResponse(ResponseObject<List<CustomerSellcardResp>> response) {
                if (ResponseObject.isOk(response)) {
                    if (Utils.isEmpty(response.getContent())) {
                        ToastUtil.showLongToast(R.string.customer_ordercenter_query_no_data_toast);
                        mOrderCenter_empty.setVisibility(View.VISIBLE);
                        orderList.setVisibility(View.GONE);
                        post(new EventClickOrdercenterListItem(windowToken, orderCategory, true));
                    } else {
                        ToastUtil.showLongToast(response.getMessage());
                        List<CustomerOrderBean> beans = sellCardResponseConvert(response.getContent());
                        resetData(orderCategory, beans);
                        mOrderCenter_empty.setVisibility(View.GONE);
                        orderList.setVisibility(View.VISIBLE);
                        onItemClick(orderList, null, 0, 0);
                    }

                } else {
                    ToastUtil.showLongToast(response.getMessage());
                    mOrderCenter_empty.setVisibility(View.VISIBLE);
                    orderList.setVisibility(View.GONE);
                    post(new EventClickOrdercenterListItem(windowToken, orderCategory, true));
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                mOrderCenter_empty.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }
        };

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.getEntityCardChangeOrders(queryEntityCardChangeOrdersReq, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

        private CustomerSellcardReq toQueryEntityCardChangeOrdersReq(String content, OrderCategory orderCategory) {
        CustomerSellcardReq customerSellCardReq = new CustomerSellcardReq();
        customerSellCardReq.setPageSize(PAGE_SIZE);
        if (!TextUtils.isEmpty(content)) {
            customerSellCardReq.setCardNo(content);
        }
        customerSellCardReq.setTradeStatus(getTradeStatus(orderCategory));

        return customerSellCardReq;
    }
}
