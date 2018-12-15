package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.cashier.ordercenter.OrderCenterDetailFragment;
import com.zhongmei.bty.cashier.ordercenter.presenter.IOrderCenterDetailPresenter;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.bty.dinner.adapter.DinnerBillCenterRejectDishAdapter;
import com.zhongmei.bty.dinner.adapter.OrderCenterDishAdapter;
import com.zhongmei.bty.dinner.ordercenter.view.ColumnLayout;
import com.zhongmei.bty.dinner.ordercenter.view.OrderCenterDetailView;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.settings.view.XInnerListView;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date：2015-11-18 下午2:58:39
 * @Description: 显示正餐详细情况
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
@EViewGroup(R.layout.dinner_ordercenter_orderinfos)
public class OrderCenterInfoLayout extends LinearLayout {
    private static final String TAG = OrderCenterInfoLayout.class.getName();

    @ViewById(R.id.pay_info)
    View vPayInfo;

    @ViewById(R.id.privilege_info)
    View vPrivilegeInfo;

    @ViewById(R.id.ll_payinfo_content)
    LinearLayout llPayinfoContent;

    @ViewById(R.id.ll_privilegeinfo_content)
    LinearLayout llPrivilegeinfoContent;

    @ViewById(R.id.tv_payment_no)
    TextView tvPaymentNo;

    @ViewById(R.id.tv_payment_time)
    TextView tvPaymentTime;

    @ViewById(R.id.v_empty)
    View vEmpty;

    @ViewById(R.id.v_content)
    View vContent;

    @ViewById(R.id.tv_amount)
    TextView mTvAmount;

    @ViewById(R.id.tv_goods_number)
    TextView mTvNumber;

    @ViewById(R.id.tv_billinfo)
    TextView mTvBillInfo;

    @ViewById(R.id.tv_billtime)
    TextView mTvBillTime;

    @ViewById(R.id.tv_tablenumber)
    TextView mTvTableNumber;

    @ViewById(R.id.tv_billserialnumber)
    TextView mTvBillSerialNumber;

    @ViewById(R.id.tv_peoplecount)
    TextView mTvPeopleCount;

    @ViewById(R.id.tv_billtablememo)
    TextView mTvBillTableMemo;

    @ViewById(R.id.v_memo_group)
    LinearLayout vMemoGroup;

    @ViewById(R.id.tv_operationbillpeople)
    TextView mTvOperationBillPeople;

    @ViewById(R.id.tv_table_serverpoeple)
    TextView mTvTableServerPeople;

    @ViewById(R.id.bill_info)
    View vBillInfo;

    @ViewById(R.id.goods_info)
    View vGoodsInfo;

    @ViewById(R.id.v_people_group)
    LinearLayout mPeopleGroup;

    @ViewById(R.id.dinner_billcenter_detail_goodslistview)
    XInnerListView goodsListView;

    @ViewById(R.id.reject_goods_info)
    View vRejectGoodsInfo;// add 2015.12.21

    @ViewById(R.id.dinner_billcenter_detail_reject_goodslistview)
    XInnerListView rejectgoodsListView;// add 2015.12.21

    @ViewById(R.id.operating_info_view)
    OrderCenterDetailView operating_info_view1;

    @ViewById(R.id.additional_privilege_view)
    OrderCenterDetailView additional_privilege_view;

    @ViewById(R.id.order_center_tax_info)
    OrderCenterDetailView order_center_tax_info;

    @ViewById(R.id.cl_customer)
    ColumnLayout clCustomer;

    @ViewById(R.id.v_line_customer)
    View vLineCustomer;

    // add 2016.01.12 start
    @ViewById(R.id.tv_trade_source)
    TextView tvTradeSource;

    @ViewById(R.id.line_operation_people)
    View mLineOperationPeople;

    @ViewById(R.id.tv_origontrade_number)
    TextView tvOrigonTradeNumber;

    @ViewById(R.id.tv_privilege_no)
    TextView tvPrivilegeNo;

    private TradeDal tradeDal;

    // add 2016.01.12 end
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private TradePaymentVo mTradePaymentVo;

    private OrderCenterDishAdapter adapter;

    private DinnerBillCenterRejectDishAdapter rejectadapter;// add
    // 2015.12.21
    private IOrderCenterDetailPresenter mOrderCenterDetailPresenter;

    public OrderCenterInfoLayout(Context context) {
        super(context);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public OrderCenterInfoLayout(Context context, TradePaymentVo tradePaymentVo, IOrderCenterDetailPresenter presenter) {
        super(context);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mTradePaymentVo = tradePaymentVo;
        mOrderCenterDetailPresenter = presenter;
    }

    @AfterViews
    public void init() {
        tradeDal = OperatesFactory.create(TradeDal.class);
        adapter = new OrderCenterDishAdapter(mContext, false);
        // add 2015.12.21
        rejectadapter = new DinnerBillCenterRejectDishAdapter(mContext);
        goodsListView.setAdapter(adapter);
        // add 2015.12.21
        rejectgoodsListView.setAdapter(rejectadapter);
        // add 2015.12.21
        refreshOperateReasonInfo(mContext, mTradePaymentVo.getTradeVo());
        refreshPayInfo(mContext, mTradePaymentVo);
        refreshPrivilegeInfo(mContext, mTradePaymentVo);
        refreshBillInfo(mContext, mTradePaymentVo.getTradeVo());
        refreshGoodsInfo(mContext, mTradePaymentVo.getTradeVo());
        refreshRejectGoodsInfo(mTradePaymentVo.getTradeVo());
    }

    /**
     * @Title: refreshPayInfo
     * @Description: 刷新支付信息
     * @Return void 返回类型
     */
    private void refreshPayInfo(Context context, TradePaymentVo tradePaymentVo) {
        llPayinfoContent.removeAllViews();
        vPayInfo.setVisibility(View.GONE);
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        List<PaymentVo> paymentVoList = tradePaymentVo.getPaymentVoList();
        List<PaymentVo> paymentTempVoList = new ArrayList<PaymentVo>();
        if (Utils.isNotEmpty(paymentVoList)) {
            for (PaymentVo paymentVo : paymentVoList) {
                if (PaymentType.ADJUST != paymentVo.getPayment().getPaymentType()) {
                    paymentTempVoList.add(paymentVo);
                }
            }
        }
        List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
        if (Utils.isNotEmpty(paymentTempVoList)) {
            long lastPayTime = 0L;
            for (PaymentVo paymentVo : paymentTempVoList) {
                List<PaymentItem> items = paymentVo.getPaymentItemList();
                for (PaymentItem paymentItem : items) {
                    lastPayTime = paymentItem.getServerCreateTime() > lastPayTime ? paymentItem.getServerCreateTime() : lastPayTime;
                }
            }
            tvPaymentTime.setText(context.getString(R.string.dinner_order_center_payment_time, DateTimeUtils.formatDateTime(lastPayTime)));
            tvPaymentTime.setVisibility(View.VISIBLE);
            paymentVos.addAll(paymentTempVoList);
        } else {
            tvPaymentTime.setVisibility(View.GONE);
        }
        if (isNeedShowPayInfo(paymentVos)) {
            View view = mOrderCenterDetailPresenter.createOrderCenterPayInfoItem(LayoutInflater.from(context), context, tradeVo, paymentVos, false);
            llPayinfoContent.addView(view);
            vPayInfo.setVisibility(mOrderCenterDetailPresenter.showPayInfo(tradeVo, paymentTempVoList) ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isNeedShowPayInfo(List<PaymentVo> paymentVos) {
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL
                    || paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_REFUND) {
                List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                for (PaymentItem paymentItem : paymentItems) {
                    if (paymentItem.getPayStatus() == TradePayStatus.PAID) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @Title: refreshPrivilegeInfo
     * @Description: 刷新优惠信息
     * @Return void 返回类型
     */
    private void refreshPrivilegeInfo(Context context, TradePaymentVo tradePaymentVo) {
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        List<PaymentVo> paymentVoList = tradePaymentVo.getPaymentVoList();
        List<PaymentVo> paymentTempVoList = new ArrayList<PaymentVo>();
        if (Utils.isNotEmpty(paymentVoList)) {
            for (PaymentVo paymentVo : paymentVoList) {
                if (PaymentType.ADJUST != paymentVo.getPayment().getPaymentType()) {
                    paymentTempVoList.add(paymentVo);
                }
            }
        }
        llPrivilegeinfoContent.removeAllViews();
        if (mOrderCenterDetailPresenter.showPrivilegeInfo(tradeVo)) {
            List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
            if (Utils.isNotEmpty(paymentTempVoList)) {
                PaymentVo paymentVo = paymentTempVoList.get(0);
                paymentVos.add(paymentVo);
            } else {
                tvPrivilegeNo.setText(context.getString(R.string.dinner_order_center_privilegement_no));
            }
            View view = mOrderCenterDetailPresenter.createPrivilegeInfoItem(context, tradeVo, false);
            llPrivilegeinfoContent.addView(view);
            tvPrivilegeNo.setText(R.string.dinner_order_center_privilegement_no);
            vPrivilegeInfo.setVisibility(View.VISIBLE);
            tvPrivilegeNo.setVisibility(View.VISIBLE);
        } else {
            vPrivilegeInfo.setVisibility(View.GONE);
            tvPrivilegeNo.setVisibility(View.GONE);
        }
        initAdditional_privilege_view(tradeVo);
        initTaxInfo(tradeVo);
    }

    private void initAdditional_privilege_view(TradeVo tradeVo) {
        additional_privilege_view.removeAllItemView();
        List<TradePrivilege> tradePrivileges = mOrderCenterDetailPresenter.getOrderPrivilegeList(tradeVo, PrivilegeType.ADDITIONAL);
        if (tradePrivileges.isEmpty()) {
            additional_privilege_view.setVisibility(View.GONE);
            return;
        }
        additional_privilege_view.setVisibility(View.VISIBLE);
        additional_privilege_view.setTitle(getResources().getString(R.string.extra_charge));
        BigDecimal totalBigDecimal = new BigDecimal(0);
        for (int i = 0; i < tradePrivileges.size(); i++) {
            TradePrivilege tradePrivilege = tradePrivileges.get(i);
            ExtraCharge extraCharge = tradeVo.getExtraChargeMap().get(tradePrivilege.getPromoId());
            BigDecimal amount = tradePrivilege.getPrivilegeAmount();
            totalBigDecimal = totalBigDecimal.add(amount);
            if (null != extraCharge) {
                additional_privilege_view.addItemNormalView(extraCharge.getName(),
                        null,
                        Utils.formatPrice(amount.doubleValue()),
                        true);
            }
        }
        additional_privilege_view.addItemTotallView(null,
                null,
                Utils.formatPrice(totalBigDecimal.doubleValue()),
                false);
    }

    private void initTaxInfo(TradeVo tradeVo) {
        order_center_tax_info.removeAllItemView();
        List<TradeTax> tradeTaxList = tradeVo.getTradeTaxs();
        if (tradeTaxList == null || tradeTaxList.isEmpty()) {
            order_center_tax_info.setVisibility(View.GONE);
        } else {
            showTaxCode(tradeVo); //显示税号 add v9.0
            order_center_tax_info.setVisibility(View.VISIBLE);
            order_center_tax_info.setTitle(getResources().getString(R.string.tax));
            for (TradeTax tradeTax : tradeVo.getTradeTaxs()) {
                order_center_tax_info.addItemNormalView(tradeTax.getTaxTypeName(),
                        tradeTax.getTaxPlan() + "%",
                        Utils.formatPrice(tradeTax.getTaxAmount().doubleValue()),
                        false);
            }
        }
    }

    //显示税号 add v9.0
    private void showTaxCode(TradeVo tradeVo) {
        if (Utils.isEmpty(tradeVo.getTradeTaxs())) {
            order_center_tax_info.setTime("");
        } else {
            //如果税号已生成就展示
            if (tradeVo.getTradeInvoiceNo() != null && !TextUtils.isEmpty(tradeVo.getTradeInvoiceNo().getCode())) {
                order_center_tax_info.setTime(getResources().getString(R.string.tax_code) + ":" + tradeVo.getTradeInvoiceNo().getCode());
            } else {
                //已经支付或预结单没生产税号要提示
                if (tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAID || tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PREPAID) {
                    String alterText = getResources().getString(R.string.tax_code) + ":" + getResources().getString(R.string.tax_code_not_find);
                    SpannableStringBuilder builder =
                            new SpannableStringBuilder(alterText);
                    builder.setSpan(new ForegroundColorSpan(Color.RED),
                            0,
                            alterText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    order_center_tax_info.setTime(builder);
                } else {
                    order_center_tax_info.setTime("");
                }
            }
        }
    }

    /**
     * @Title: refreshBillInfo
     * @Description: 刷新单据信息
     * @Return void 返回类型
     */
    private void refreshBillInfo(Context context, TradeVo tradeVo) {
        if (tradeVo != null) {
            mTvBillTime.setText(context.getString(R.string.dinner_order_center_trade_time, DateTimeUtils.formatDateTime(tradeVo.getTrade().getServerCreateTime())));
            Trade trade = tradeVo.getTrade();
            // 开单人
            String creatorName = trade.getCreatorName();
            if (!TextUtils.isEmpty(creatorName)) {
                mTvOperationBillPeople.setText(context.getString(R.string.dinner_order_center_trade_creator,
                        creatorName));
                mTvOperationBillPeople.setVisibility(View.VISIBLE);
                mLineOperationPeople.setVisibility(View.VISIBLE);
            } else {
                mTvOperationBillPeople.setVisibility(View.INVISIBLE);
                mLineOperationPeople.setVisibility(View.INVISIBLE);
            }

            // 备注
            String tradeMemo = trade.getTradeMemo();
            if (!TextUtils.isEmpty(tradeMemo)) {
                mTvBillTableMemo.setText(context.getString(R.string.dinner_order_center_memo, tradeMemo));
                vMemoGroup.setVisibility(View.VISIBLE);
            } else {
                vMemoGroup.setVisibility(View.GONE);
            }

            // 流水号
            TradeExtra tradeExtra = tradeVo.getTradeExtra();
            if (tradeExtra != null && !TextUtils.isEmpty(tradeExtra.getSerialNumber())) {
                mTvBillSerialNumber.setText(context.getString(R.string.dinner_order_center_serial_no,
                        tradeExtra.getSerialNumber()));
                mTvBillSerialNumber.setVisibility(View.VISIBLE);
            } else {
                mTvBillSerialNumber.setVisibility(View.INVISIBLE);
            }

            List<TradeTable> tradeTableList = tradeVo.getTradeTableList();
            if (Utils.isNotEmpty(tradeTableList)) {
                TradeTable tradeTable = tradeTableList.get(0);

                // 桌台号
                TradeType tradeType = trade.getTradeType();
                String tableName = tradeTable.getTableName();
                if (!TextUtils.isEmpty(tableName)) {
                    mTvTableNumber.setText(context.getString(R.string.dinner_order_center_tables, tableName));
                    mTvTableNumber.setVisibility(View.VISIBLE);
                } else {
                    mTvTableNumber.setText(context.getString(R.string.dinner_order_center_tables,
                            context.getString(R.string.nothing)));
                    mTvTableNumber.setVisibility(View.VISIBLE);
                }
            } else {
                mTvTableNumber.setVisibility(View.GONE);
                mTvTableServerPeople.setVisibility(View.GONE);
                if (TextUtils.isEmpty(creatorName)) {
                    mPeopleGroup.setVisibility(View.GONE);
                }
            }
            // 客位数
            if (trade.getTradePeopleCount() != 0) {
                mTvPeopleCount.setVisibility(View.VISIBLE);
                mTvPeopleCount.setText(context.getString(R.string.dinner_order_center_people_count, String.valueOf(trade.getTradePeopleCount())));
            } else {
                mTvPeopleCount.setVisibility(View.GONE);
            }
            //顾客信息
            List<String> customerInfo = getCustomerInfo(context, tradeVo);
            if (Utils.isNotEmpty(customerInfo)) {
                clCustomer.setData(customerInfo);
                vLineCustomer.setVisibility(View.VISIBLE);
            } else {
                clCustomer.setVisibility(View.GONE);
                vLineCustomer.setVisibility(View.GONE);
            }
            // 添加订单来源 和订单原单号
            final String tradeSource = mOrderCenterDetailPresenter.getTradeSource(context, trade);
            if (!TextUtils.isEmpty(tradeSource)) {
                tvTradeSource.setText(context.getString(R.string.dinner_order_center_tradesource, tradeSource));
                tvTradeSource.setVisibility(View.VISIBLE);
                mLineOperationPeople.setVisibility(View.VISIBLE);
            } else {
                tvTradeSource.setVisibility(View.INVISIBLE);
                mLineOperationPeople.setVisibility(View.INVISIBLE);
            }

            final String origonTradeNumber = mOrderCenterDetailPresenter.getSplideOrigonTradeNumber(trade);
            if (!TextUtils.isEmpty(origonTradeNumber)) {
                tvOrigonTradeNumber.setText(context.getString(R.string.dinner_order_center_origontrade_number,
                        origonTradeNumber));
                tvOrigonTradeNumber.setVisibility(View.VISIBLE);
            } else {
                tvOrigonTradeNumber.setVisibility(View.INVISIBLE);
            }

            vBillInfo.setVisibility(View.VISIBLE);
        } else {
            vBillInfo.setVisibility(View.GONE);
        }
    }

    /**
     * @Title: refreshGoodsInfo
     * @Description: 刷新商品信息
     * @Return void 返回类型
     */
    private void refreshGoodsInfo(Context context, TradeVo tradeVo) {
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeItemList())) {
            List<TradeItemVo> tradeItemVos = tradeVo.getTradeItemList();
            adapter.setDataSet(tradeVo);
            // 合计
            BigDecimal totalAmount = OrderCenterDetailFragment.getGoodsAmount(tradeVo); //getGoodsAmount(tradeVo);
            mTvAmount.setText(Utils.formatPrice(totalAmount.doubleValue()));
            mTvNumber.setVisibility(View.VISIBLE);
            mTvNumber.setText(context.getString(R.string.dinner_order_center_goods_total_number, getAllDishCount(tradeItemVos, tradeVo.getTrade().getTradeType())));
            vGoodsInfo.setVisibility(View.VISIBLE);
        } else {
            vGoodsInfo.setVisibility(View.GONE);
        }
    }

    public String getAllDishCount(List<TradeItemVo> list, TradeType tradeType) {
        BigDecimal count = BigDecimal.ZERO;
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; i--) {
                TradeItem tradeItem = list.get(i).getTradeItem();
                if (tradeItem.getType() == DishType.SINGLE && tradeItem.getParentUuid() != null) {//套餐子菜
                    continue;
                }
                if (tradeItem.getType() == DishType.SINGLE || tradeItem.getType() == DishType.COMBO) {
                    if (tradeItem.getSaleType() == SaleType.WEIGHING) {
                        count = count.add(BigDecimal.ONE);
                    } else {
                        count = count.add(new BigDecimal(tradeItem.getQuantity() + "").abs());
                    }
                }
            }
        }
        return MathDecimal.trimZero(count).toString();
    }

    //商品金额
    private BigDecimal getGoodsAmount(TradeVo tradeVo) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal exemptAmount = BigDecimal.ZERO;
        BigDecimal actualAmount = tradeVo.getTrade().getSaleAmount();
        String discountAmount =
                Utils.transferDot2(tradeVo.getTrade().getPrivilegeAmount().add(exemptAmount.negate()).toString());
        if (!TextUtils.isEmpty(discountAmount)) {
            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
            // 附加费总和
            if (tradePrivileges != null) {
                for (TradePrivilege tradePrivilege : tradeVo.getTradePrivileges()) {
                    if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL) {
                        BigDecimal privilegeAmount = tradePrivilege.getPrivilegeAmount();
                        actualAmount = actualAmount.subtract(privilegeAmount);
                    }
                }
            }
            List<TradeTax> tradeTaxes = tradeVo.getTradeTaxs();
            if (tradeTaxes != null) {
                for (TradeTax tradeTax : tradeTaxes) {
                    actualAmount = actualAmount.subtract(tradeTax.getTaxAmount());
                }
            }
            totalAmount = actualAmount;
        }
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        if (tradeDeposit != null && tradeDeposit.getDepositPay() != null) {
            totalAmount = totalAmount.subtract(tradeDeposit.getDepositPay());
        }
        return totalAmount;
    }

    /**
     * @Title: refreshRejectGoodsInfo
     * @Description: 刷新废弃商品展示
     * @Return void 返回类型
     */
    private void refreshRejectGoodsInfo(TradeVo tradeVo) {
        List<TradeItemVo> tradeItemVoList = mOrderCenterDetailPresenter.getInvalidTradeItemList(tradeVo.getTradeItemList(), InvalidType.RETURN_QTY);
        if (tradeVo != null && Utils.isNotEmpty(tradeItemVoList)) {
            rejectadapter.setDataSet(tradeVo);
            vRejectGoodsInfo.setVisibility(View.VISIBLE);
        } else {
            vRejectGoodsInfo.setVisibility(View.GONE);
        }
    }


    private boolean isNotEmptyReason(TradeReasonRel value) {
        return value != null && !TextUtils.isEmpty(value.getReasonContent());
    }

    /**
     * @Title: refreshTradeStatusLogInfo
     * @Description: 刷新操作的信息
     * @Param @param tradeVo
     * @Return void 返回类型
     */
    private void refreshOperateReasonInfo(Context context, TradeVo tradeVo) {
        if (tradeVo != null) {
            operating_info_view1.removeAllItemView();
            operating_info_view1.setVisibility(View.GONE);
            TradeStatus tradeStatus = tradeVo.getTrade().getTradeStatus();
            OperateType operateType = null;
            int resId = 0;
            int titleResId = 0;
            int operatResId = 0;
            switch (tradeStatus) {
                case RETURNED:// 退货操作信息
                    TradeType tradeType = tradeVo.getTrade().getTradeType();
                    if (tradeType == TradeType.REFUND) {
                        operateType = OperateType.TRADE_RETURNED;
                    } else if (tradeType == TradeType.REFUND_FOR_REPEAT) {
                        operateType = OperateType.TRADE_REPEATED;
                    }
                    resId = R.string.order_center_detail_reason_return;
                    titleResId = R.string.order_center_detail_info_return;
                    operatResId = R.string.order_center_detail_operat_return;
                    break;
                case INVALID:// 作废操作信息
                    operateType = OperateType.TRADE_FASTFOOD_INVALID;
                    resId = R.string.order_center_detail_reason_invalid;
                    titleResId = R.string.order_center_detail_info_invalid;
                    operatResId = R.string.order_center_detail_operat_invalid;
                    break;
                case REFUSED:// 拒绝操作信息
                    operateType = OperateType.TRADE_FASTFOOD_REFUSE;
                    resId = R.string.order_center_detail_reason_refuse;
                    titleResId = R.string.order_center_detail_info_refuse;
                    operatResId = R.string.order_center_detail_operat_refuse;
                    break;
                case REPEATED:// 反结账操作信息
                    operateType = OperateType.TRADE_REPEATED;
                    resId = R.string.order_center_detail_reason_repeated;
                    titleResId = R.string.order_center_detail_info_repeated;
                    operatResId = R.string.order_center_detail_operat_repeated;
                    break;
                case CREDIT:// 挂账操作信息
                case WRITEOFF://销账状态
                    operateType = OperateType.TRADE_CREDIT;
                    resId = R.string.order_center_detail_reason_credit;
                    titleResId = R.string.order_center_detail_info_credit;
                    operatResId = R.string.order_center_detail_operat_credit;
                    break;
                case FINISH:
                    if (null != tradeVo.getTradeReasonRelList() && !tradeVo.getTradeReasonRelList().isEmpty()) {
                        operateType = OperateType.TRADE_CREDIT;
                        resId = R.string.order_center_detail_reason_credit;
                        titleResId = R.string.order_center_detail_info_credit;
                        operatResId = R.string.order_center_detail_operat_credit;
                    }
                    break;
                default:
                    if (null != tradeVo.getTradeReasonRelList() && !tradeVo.getTradeReasonRelList().isEmpty()) {
                        operateType = OperateType.TRADE_CREDIT;
                        resId = R.string.order_center_detail_reason_unknow;
                        titleResId = R.string.order_center_detail_info_unknow;
                        operatResId = R.string.order_center_detail_operat_unknow;
                    }
                    break;
            }
            if (operateType != null) {
                TradeReasonRel operateReason = tradeVo.getOperateReason(operateType);
                String date;
                if (operateType == OperateType.TRADE_RETURNED) {
                    date = context.getString(R.string.order_center_detail_return_time, DateTimeUtils.formatDateTime(tradeVo.getTrade().getServerUpdateTime()));// 获取订单的更新时间
                } else {
                    date = DateTimeUtils.formatDateTime(tradeVo.getTrade().getServerUpdateTime());// 获取订单的更新时间
                }
                String username = tradeVo.getTrade().getUpdatorName();// 获取用户名称

                operating_info_view1.setVisibility(View.VISIBLE);
                operating_info_view1.setTitle(context.getString(titleResId));
                operating_info_view1.setTime(date);
                if (!TextUtils.isEmpty(username)) {
                    operating_info_view1.addItemNormalView(context.getString(operatResId) + username, null, null, true);
                }
                String description = context.getString(resId, "");
                if (operateReason != null && !TextUtils.isEmpty(operateReason.getReasonContent())) {
                    description = context.getString(resId, operateReason.getReasonContent());// 获取原因
                }
                operating_info_view1.addItemNormalView(description, null, null, false);
            }
        }
    }

    private List<String> getCustomerInfo(Context context, TradeVo tradeVo) {
        List<String> customerInfo = new ArrayList<String>();
        // 会员信息 因为存在有 顾客 和会员 同时存在的情况这个时候应该展示会员的信息
        List<TradeCustomer> tradeCustomers = tradeVo.getTradeCustomerList();
        if (Utils.isNotEmpty(tradeCustomers)) {
            for (TradeCustomer tradeCustomer : tradeCustomers) {
                customerInfo.clear();
                if (tradeCustomer.getCustomerType() == CustomerType.BOOKING
                        || tradeCustomer.getCustomerType() == CustomerType.CUSTOMER) {
                    if (!TextUtils.isEmpty(tradeCustomer.getCustomerName())) {
                        customerInfo.add(context.getString(R.string.dinner_order_center_customer_name,
                                tradeCustomer.getCustomerName()));
                    }
                    if (!TextUtils.isEmpty(tradeCustomer.getCustomerPhone())) {
                        customerInfo.add(context.getString(R.string.dinner_order_center_customer_account,
                                tradeCustomer.getCustomerPhone()));
                    }
                    // 不用break
                } else if (tradeCustomer.getCustomerType() == CustomerType.CARD) {
//                    if (Utils.isEmpty(customerInfo)) {
//                        customerInfo.add("");
//                        customerInfo.add("");// 加两个占位
//                    }
                    customerInfo.add(context.getString(R.string.dinner_order_center_card_account,
                            tradeCustomer.getEntitycardNum()));
                    break;
                } else if (tradeCustomer.getCustomerType() == CustomerType.MEMBER) {
                    // 加一个填充位
                    if (!TextUtils.isEmpty(tradeCustomer.getCustomerName())) {
                        customerInfo.add(context.getString(R.string.dinner_order_center_member_name,
                                tradeCustomer.getCustomerName()));
                    }
                    if (!TextUtils.isEmpty(tradeCustomer.getCustomerPhone())) {
                        customerInfo.add(context.getString(R.string.dinner_order_center_customer_account,
                                tradeCustomer.getCustomerPhone()));
                    }
                    break;
                }
            }
        }
        return customerInfo;
    }
}
