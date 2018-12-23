package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年9月20日
 */
public class DinnertableTradeModel implements IDinnertableTrade {

    private final TradeTableInfo tradeTableInfo;
    private final IDinnertable dinnertable;
    /**
     * 耗时，根据startTime计算
     */
    private int spendTime;

    public DinnertableTradeModel(TradeTableInfo tradeTableInfo, IDinnertable dinnertable) {
        this.tradeTableInfo = tradeTableInfo;
        this.dinnertable = dinnertable;
    }

    @Override
    public String getUuid() {
        return tradeTableInfo.uuid;
    }

    @Override
    public String getSn() {
        return tradeTableInfo.serialNumber;
    }

    @Override
    public int getNumberOfMeals() {
        return tradeTableInfo.numberOfMeals;
    }

    @Override
    public TradePayStatus getTradePayStatus() {
        return tradeTableInfo.tradePayStatus;
    }

    @Override
    public Long getId() {
        return tradeTableInfo.id;
    }

    @Override
    public Long getServerUpdateTime() {
        return tradeTableInfo.serverUpdateTime;
    }

    @Override
    public String getTradeUuid() {
        return tradeTableInfo.tradeUuid;
    }

    @Override
    public Long getTradeId() {
        return tradeTableInfo.tradeId;
    }

    @Override
    public Long getTradeServerUpdateTime() {
        return tradeTableInfo.tradeServerUpdateTime;
    }

    @Override
    public Long getTradeClientUpdateTime() {
        return tradeTableInfo.tradeClientUpdateTime;
    }

    @Override
    public TradeStatus getTradeStatus() {
        return tradeTableInfo.tradeStatus;
    }

    @Override
    public DinnertableStatus getStatus() {
        return tradeTableInfo.dishStatus;
    }

    @Override
    public IDinnertable getDinnertable() {
        return dinnertable;
    }

    @Override
    public int getSpendTime() {
        return spendTime;
    }

    @Override
    public void refreshSpendTime() {
        long startTimeMillis = tradeTableInfo.startTimeMillis != null ? tradeTableInfo.startTimeMillis : 0;
        int time = (int) (System.currentTimeMillis() - startTimeMillis) / (60 * 1000);
        if (time < 0) {
            time = -1;
        }
        spendTime = time;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DinnertableTradeModel other = (DinnertableTradeModel) obj;
        if (getUuid() == null) {
            if (other.getUuid() != null)
                return false;
        } else if (!getUuid().equals(other.getUuid()))
            return false;
        return true;
    }

    @Override
    public UpContentType getUpContent() {
        UpContentType type = null;
        boolean isShowTradeMoney = SharedPreferenceUtil.getSpUtil().getBoolean(DinnerTableConstant.SHOW_TRADE_MONEY_KEY, true);
        if (isShowTradeMoney) {
            type = UpContentType.TRADE_AMOUNT;
        } else {
            type = UpContentType.SN;
        }
        return type;
    }

    @Override
    public BusinessType getBusinessType() {
        return tradeTableInfo.businessType;
    }

    @Override
    public String getTradeAmount() {
//		Log.i("zhubo","tradeTableInfo"+tradeTableInfo);
        if (tradeTableInfo.tradeSaleAmount == null) {
            return ShopInfoCfg.formatCurrencySymbol(String.valueOf(0));
        } else {
            return ShopInfoCfg.formatCurrencySymbol(
                    String.valueOf(tradeTableInfo.tradeSaleAmount.doubleValue()));

        }

    }


    @Override
    public void setTradeAmount(BigDecimal tradeAmount) {
        tradeTableInfo.tradeSaleAmount = tradeAmount;
    }

    public TradeTableInfo getTradeTableInfo() {
        return tradeTableInfo;
    }

    @Override
    public YesOrNo getPreCashPrintStatus() {
        return tradeTableInfo.printPreCash;
    }

    @Override
    public List<TableSeat> getTableSeats() {
        return dinnertable.getTableSeats();
    }

    @Override
    public TradeType getTradeType() {
        return tradeTableInfo.tradeType;
    }

    @Override
    public String getTableName() {
        return dinnertable.getName();
    }

    /**
     * @Date 2016/10/13
     * @Description:获取http请求数据
     * @Param
     * @Return
     */
    public AsyncHttpRecord getAsyncHttpRecord() {
        if (tradeTableInfo.httpRecord != null && tradeTableInfo.httpRecord.size() > 0) {
            return tradeTableInfo.httpRecord.get(0);
        }
        return null;
    }

    /**
     * @Date 2016/10/20
     * @Description:获取订单对应加菜数据
     * @Param
     * @Return
     */
    public List<AddItemVo> getAddItemVos() {
        return tradeTableInfo.addItemVoList;
    }
}
