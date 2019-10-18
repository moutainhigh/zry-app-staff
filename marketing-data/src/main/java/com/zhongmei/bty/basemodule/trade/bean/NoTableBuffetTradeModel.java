package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;
import java.util.List;



public class NoTableBuffetTradeModel implements IDinnertableTrade {
    private TradeTableInfo tradeTableInfo = null;

    public NoTableBuffetTradeModel(TradeTableInfo tradeInfo) {
        this.tradeTableInfo = tradeInfo;

    }

    private int spendTime;

    @Override
    public String getUuid() {
        return tradeTableInfo.uuid;
    }

    @Override
    public String getSn() {
        return tradeTableInfo.serialNumber;
    }

    @Override
    public TradeStatus getTradeStatus() {
        return tradeTableInfo.tradeStatus;
    }

    @Override
    public int getSpendTime() {
        return spendTime;
    }

    @Override
    public void refreshSpendTime() {
        int time = (int) (System.currentTimeMillis() - tradeTableInfo.startTimeMillis) / (60 * 1000);
        if (time < 0) {
            time = -1;
        }
        spendTime = time;
    }

    @Override
    public int getNumberOfMeals() {
        return 0;
    }

    @Override
    public TradePayStatus getTradePayStatus() {
        return tradeTableInfo.tradePayStatus;
    }

    @Override
    public DinnertableStatus getStatus() {
        return tradeTableInfo.dishStatus;
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
    public IDinnertable getDinnertable() {
        return null;
    }

    @Override
    public String getTradeAmount() {
        if (tradeTableInfo.tradeSaleAmount == null) {
            return ShopInfoCfg.formatCurrencySymbol(String.valueOf(0));
        } else {
            return ShopInfoCfg.formatCurrencySymbol(
                    String.valueOf(tradeTableInfo.tradeSaleAmount.doubleValue()));

        }

    }

    @Override
    public void setTradeAmount(BigDecimal tradeAmount) {

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
    public YesOrNo getPreCashPrintStatus() {
        return YesOrNo.YES;
    }

    @Override
    public List<TableSeat> getTableSeats() {
        return null;
    }

    @Override
    public TradeType getTradeType() {
        return tradeTableInfo.tradeType;
    }

    @Override
    public String getTableName() {
        return null;
    }
}
