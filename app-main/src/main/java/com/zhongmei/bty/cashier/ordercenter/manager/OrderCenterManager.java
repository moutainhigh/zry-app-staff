package com.zhongmei.bty.cashier.ordercenter.manager;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.message.DepositRefundResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.common.view.CalmResponseToastFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class OrderCenterManager {
    public static final String TAG = OrderCenterManager.class.getSimpleName();

    private FragmentActivity mActivity;

    public OrderCenterManager(FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * 根据Trade UUID获取TradeVo
     *
     * @Param @param uuid
     * @Return TradeVo 返回类型
     */
    public TradeVo findTradeVoByUuid(String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            return null;
        }

        TradeDal dal = OperatesFactory.create(TradeDal.class);
        try {
            return dal.findTrade(uuid);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

        return null;
    }

    /**
     * 根据Trade获取TradePaymentVo
     *
     * @param tradeUuid
     * @return
     */
    public TradePaymentVo findTradePaymentVo(String tradeUuid) {
        TradeDal dal = OperatesFactory.create(TradeDal.class);
        try {
            return dal.findTradPayment(tradeUuid);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

        return null;
    }

    /**
     * @Title: loadPaymentVo
     * @Description: 根据Uuid获取支付信息
     * @Param @param tradeUuid
     * @Return void 返回类型
     */
    public List<PaymentVo> loadPaymentVo(String tradeUuid) {
        if (!TextUtils.isEmpty(tradeUuid)) {
            try {
                TradeDal dal = OperatesFactory.create(TradeDal.class);
                return dal.listPayment(tradeUuid);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }

        return Collections.emptyList();
    }

    /**
     * 呼叫用户
     */
    public void call(TradeCustomer tradeCustomer) {
        if (tradeCustomer == null || TextUtils.isEmpty(tradeCustomer.getCustomerPhone())) {
            ToastUtil.showLongToast(R.string.trade_center_call_no_phone);
            return;
        }
        //PhoneMainActivity.launchToPhone(mActivity, tradeCustomer.getCustomerPhone());
    }

    public String getPayModeName(PaymentItem paymentItem) {

        if (TextUtils.isEmpty(paymentItem.getPayModeName())) {
            //modify 20160707 yutang
            return PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId());
          /*  StringBuffer sb = new StringBuffer();

            String[] paymentModeName = mActivity.getResources().getStringArray(R.array.trade_payment_mode);
            switch ((int) (paymentItem.getPayModeId() + 0)) {
                case -1:
                    // -1:会员卡余额
                    sb.append(paymentModeName[0] + ",");
                    break;
                case -2:
                    // 优惠券
                    sb.append(paymentModeName[1] + ",");
                    break;
                case -3:
                    // 现金
                    sb.append(paymentModeName[2] + ",");
                    break;
                case -4:
                    // 银行卡
                    sb.append(paymentModeName[3] + ",");
                    break;
                case -5:
                    // 微信支付
                    sb.append(paymentModeName[4] + ",");
                    break;
                case -6:
                    // 支付宝
                    sb.append(paymentModeName[5] + ",");
                    break;
                case -7:
                    // 百度钱包
                    sb.append(paymentModeName[6] + ",");
                    break;
                case -8:
                    // 百度直达号
                    sb.append(paymentModeName[7] + ",");
                    break;
                case -9:
                    // 积分抵现
                    sb.append(paymentModeName[8] + ",");
                    break;
                case -10:
                    // 百度地图
                    sb.append(paymentModeName[9] + ",");
                    break;
                case -11:
                    // 银联POS刷卡
                    sb.append(paymentModeName[10] + ",");
                    break;
                case -12:
                    //百糯到店付
                    sb.append(paymentModeName[11] + ",");
                    break;
                case -13:
                    //百度外卖
                    sb.append(paymentModeName[12] + ",");
                    break;
                case -14:
                    //饿了么
                    sb.append(paymentModeName[13] + ",");
                    break;
                case -15:
                    //会员实体卡
                    sb.append(paymentModeName[14] + ",");
                    break;
                case -16:
                    //大众点评
                    sb.append(paymentModeName[15] + ",");
                    break;
                case -17:
                    //美团外卖
                    sb.append(paymentModeName[16] + ",");
                    break;
                case -18:
                    //点评团购劵
                    sb.append(paymentModeName[17] + ",");
                    break;
                case -19:
                    //点评闪惠
                    sb.append(paymentModeName[18] + ",");
                    break;
                case -20:
                    //匿名实体卡余额
                    sb.append(paymentModeName[19] + ",");
                    break;
                default:
                    // 其他
                    sb.append(paymentModeName[20] + ",");
                    break;
            }

            if (sb.toString().endsWith(",")) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();*/
        } else {
            return paymentItem.getPayModeName();
        }
    }

    public ReasonSource getReasonSource(TradeVo tradeVo) {
        SourceId sourceId = tradeVo.getTrade().getSource();
        return (sourceId == SourceId.BAIDU_TAKEOUT) ? ReasonSource.BAIDU_TAKEOUT : ReasonSource.ZHONGMEI;
    }

    /**
     * 获取该退票记录UUID
     *
     * @param tradeReturnInfo 退单信息
     * @return
     */
    public String getRequestUuid(TradeReturnInfo tradeReturnInfo) {
        if (tradeReturnInfo != null) {
            return tradeReturnInfo.getUuid();
        }

        return "";
    }

    /**
     * 判断这笔订单是否退单申请订单
     *
     * @param tradeReturnInfo
     * @param trade
     * @return
     */
    public boolean isReturnApply(TradeReturnInfo tradeReturnInfo, Trade trade) {
        if (tradeReturnInfo != null && trade != null) {
            return tradeReturnInfo.getTradeId().equals(trade.getId());
        }

        return false;
    }

    /**
     * 判断是否可以拒绝退票申请
     *
     * @return 为第一次申请可以拒绝退票请求，超过第一次则不行
     */
    public boolean canRefuseReturnApply(TradeReturnInfo tradeReturnInfo) {
        if (tradeReturnInfo != null) {
            Integer sequenceNo = tradeReturnInfo.getSequenceNo();
            return sequenceNo == null || sequenceNo == 1;
        }

        return false;
    }

    /**
     * 获取订单中的登录的会员（包括虚拟会员登录或者实体卡登录）
     *
     * @param tradeCustomerList
     * @return
     */
    public TradeCustomer getTradeMemberCustomer(List<TradeCustomer> tradeCustomerList) {
        TradeCustomer tradeMemberCustomer = null;
        if (Utils.isNotEmpty(tradeCustomerList)) {
            // 这个逻辑有点绕 ,首先 有 3个人 , 会员 :(1.
            // 订餐的人是会员 2.使用的会员) 订餐人不是会员
            // 当有会员的时候 ,使用的会员
            // 当没有会员的时候 , 订餐的会员
            // 由于多个customer ,会员的优先级高
            for (TradeCustomer tradeCustomer : tradeCustomerList) {
                if (tradeMemberCustomer != null) {
                    CustomerType customerType = tradeMemberCustomer.getCustomerType();
                    if (customerType == CustomerType.MEMBER || customerType == CustomerType.CARD) {

                    } else {
                        tradeMemberCustomer = tradeCustomer;
                    }
                } else {
                    tradeMemberCustomer = tradeCustomer;
                }
            }
        }

        return tradeMemberCustomer;
    }

    /**
     * 判断这笔已支付订单支付方式中是否包含现金
     *
     * @param paymentItems
     * @return
     */
    public boolean isPayInCash(List<PaymentItem> paymentItems) {
        if (Utils.isNotEmpty(paymentItems)) {
            for (PaymentItem paymentItem : paymentItems) {
                if (PayModeId.CASH.equalsValue(paymentItem.getPayModeId())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断这笔已支付订单支付方式中是否包含团购券
     *
     * @param paymentItems
     * @return
     */
    public boolean isPayInTuanGou(List<PaymentItem> paymentItems) {
        if (Utils.isNotEmpty(paymentItems)) {
            for (PaymentItem paymentItem : paymentItems) {
                if (PayModeId.MEITUAN_TUANGOU.equalsValue(paymentItem.getPayModeId())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取用餐时间(用订单相关的最后清台TradeTable的ServerUpdateTime减去订单创建时间)
     *
     * @param tradeVo
     * @return
     */
    public Long getMealTime(TradeVo tradeVo) {
        Long mealTime = null;
        long currentTimeMillis = System.currentTimeMillis();
        long orderCreateTime = tradeVo.getTrade().getServerCreateTime();//订单创建时间


        List<TradeTable> tradeTableList = tradeVo.getTradeTableList();
        if (Utils.isNotEmpty(tradeTableList)) {
            //如果订单相关桌台都已经清空，那么标识已就餐完成
            if (isAllTablesEmpty(tradeTableList)) {
                for (TradeTable tradeTable : tradeTableList) {
                    long deltaTime = tradeTable.getServerUpdateTime() - orderCreateTime;
                    if (mealTime == null || deltaTime > mealTime) {
                        mealTime = deltaTime;
                    }
                }
            } else {
                mealTime = currentTimeMillis - orderCreateTime;
            }
        }

        return mealTime;
    }

    /**
     * 获取第三方流水号
     *
     * @param tradeVo
     * @return
     */
    public String getThirdSerialNo(TradeVo tradeVo) {
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        return tradeExtra != null ? tradeExtra.getThirdSerialNo() : "";
    }

    /**
     * 判断订单相关联桌台是否都已空闲
     *
     * @param tradeTableList
     * @return
     */
    private boolean isAllTablesEmpty(List<TradeTable> tradeTableList) {
        for (TradeTable tradeTable : tradeTableList) {
            if (tradeTable.getSelfTableStatus() != TableStatus.EMPTY) {
                return false;
            }
        }

        return true;
    }

    /**
     * 订单退押金退押金
     *
     * @param tradeVo
     */
    public void depositRefund(TradeVo tradeVo) {
        ResponseListener<DepositRefundResp> listener = LoadingResponseListener.ensure(new ResponseListener<DepositRefundResp>() {
            @Override
            public void onResponse(ResponseObject<DepositRefundResp> response) {
                if (ResponseObject.isOk(response)) {
                    if (isPayInCash(response.getContent().getPaymentItems())) {
                        //PRTPrintContentQueue.getCommonPrintQueue().openMoneyBox(null);
                        //IPrintHelper.Holder.getInstance().openMoneyBox();
                    }
                    CalmResponseToastFragment.newInstance(CalmResponseToastFragment.SUCCESS, mActivity.getString(R.string.order_deposit_refund_success_message), null)
                            .show(mActivity.getSupportFragmentManager(), "depositRefund");
                } else {
                    CalmResponseToastFragment.newInstance(CalmResponseToastFragment.FAIL, mActivity.getString(R.string.order_deposit_refund_fail_message), null)
                            .show(mActivity.getSupportFragmentManager(), "depositRefund");
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        }, mActivity.getSupportFragmentManager());

        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        tradeOperates.depositRefund(tradeVo.getTrade().getId(), tradeVo.getTradeDeposit().getDepositPay(), PayModeId.CASH.value(), null, listener);
    }

    /**
     * 微信二次退款
     *
     * @param tradeVo
     */
    public void weixinRefund(TradeVo tradeVo) {
        tradeVo.getTrade().validateUpdate();
        ResponseListener<TradePaymentResp> listener =
                LoadingResponseListener.ensure(new WeiXinRefundResponse(), mActivity.getSupportFragmentManager());
        // 退款原因现在为null
        Reason reason = new Reason();

        TradeOperates httpImpl = OperatesFactory.create(TradeOperates.class);
        httpImpl.weixinRefund(tradeVo, reason, listener);
    }

    private class WeiXinRefundResponse implements ResponseListener<TradePaymentResp> {

        /**
         * @Title: onResponse
         */
        @Override
        public void onResponse(ResponseObject<TradePaymentResp> response) {
            ToastUtil.showLongToast(response.getMessage());
        }

        /**
         * @Title: onErrorResponse
         */
        @Override
        public void onError(VolleyError error) {
            ToastUtil.showLongToast(error.getMessage());
        }
    }

    /**
     * 计算所有商品的份数，称重商品只算一份,套餐外壳数量
     *
     * @param list
     */
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
            if (TradeType.REFUND == tradeType) {//退货将取反
                count = count.multiply(BigDecimal.valueOf(-1));
            }
        }
        return MathDecimal.trimZero(count).toString();
    }

    /**
     * 获取交易流水号
     *
     * @param tradeExtra
     * @return
     */
    public String getSerialNumber(TradeExtra tradeExtra) {
        return tradeExtra == null ? "" : tradeExtra.getSerialNumber();
    }

    /**
     * 获取交易发票信息
     *
     * @param tradeExtra
     * @return
     */
    public String getInvoiceTitle(TradeExtra tradeExtra) {
        return tradeExtra == null ? "" : tradeExtra.getInvoiceTitle();
    }

    /**
     * 获取交易号牌
     *
     * @param tradeExtra
     * @return
     */
    public String getNumberPlate(TradeExtra tradeExtra) {
        return tradeExtra == null ? "" : tradeExtra.getNumberPlate();
    }

    /**
     * 获取交易期望时间
     *
     * @param tradeExtra
     * @return
     */
    public Long getExpectTime(TradeExtra tradeExtra) {
        if (tradeExtra != null && tradeExtra.getExpectTime() != null) {
            return tradeExtra.getExpectTime();
        }

        return 0L;
    }

    /**
     * 获取配送地址
     *
     * @param tradeExtra
     * @return
     */
    public String getDeliveryAddress(TradeExtra tradeExtra) {
        return tradeExtra == null ? "" : tradeExtra.getDeliveryAddress();
    }

    /**
     * 获取押金项
     */
    public TradeDeposit getTradeDepositByUuid(String uuid) {
        if (!TextUtils.isEmpty(uuid)) {
            try {
                TradeDal dal = OperatesFactory.create(TradeDal.class);
                return dal.getTradeDepositByUuid(uuid);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }

    /**
     * 判断当前订单是否含有微信卡券
     *
     * @param tradeVo
     * @return
     */
    public boolean hasWeiXinPrivilege(TradeVo tradeVo) {
        if (tradeVo != null) {
            List<WeiXinCouponsVo> weiXinCouponsVos = tradeVo.getmWeiXinCouponsVo();
            return Utils.isNotEmpty(weiXinCouponsVos);
        }

        return false;
    }
}
