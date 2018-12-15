package com.zhongmei.bty.dinner.orderdish.Bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradePrintStatus;
import com.zhongmei.yunfu.db.enums.TradeServingStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class TableTradeInfo implements IDinnertableTrade {
    private int spendTime;

    private String serailNumber;

    private DinnertableStatus status;

    private List<TableSeat> tableSeats;

    private TradeType tradeType;

    public void setSpendTime(int spendTime) {
//        int minute = (int) (System.currentTimeMillis() - time) / (60 * 1000);
//        if (minute < 0) {
//            minute = -1;
//        }
//        spendTime = spendTime;
        this.spendTime = spendTime;
    }

    public String getSerailNumber() {
        return serailNumber;
    }

    public void setSerailNumber(String serailNumber) {
        this.serailNumber = serailNumber;
    }

    public void setStatus(TradeVo tradeVo) {
        if (tradeVo.getTradeExtra().getHasServing() == TradeServingStatus.SERVED) {
            this.status = DinnertableStatus.SERVING;
        } else {
            if (tradeVo.getTradeExtra().getIsPrinted() == TradePrintStatus.PRINTED) {
                this.status = DinnertableStatus.ISSUED;
            } else {
                this.status = DinnertableStatus.UNISSUED;
            }

        }
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public String getUuid() {
        return null;
    }

    @Override
    public String getSn() {
        return serailNumber;
    }

    @Override
    public TradeStatus getTradeStatus() {
        return null;
    }

    @Override
    public int getSpendTime() {
        return spendTime;
    }

    @Override
    public void refreshSpendTime() {

    }

    public void setTableSeats(List<TableSeat> tableSeats) {
        this.tableSeats = tableSeats;
    }

    @Override
    public int getNumberOfMeals() {
        return 0;
    }

    @Override
    public TradePayStatus getTradePayStatus() {
        return null;
    }

    @Override
    public DinnertableStatus getStatus() {
        return status;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Long getServerUpdateTime() {
        return null;
    }

    @Override
    public String getTradeUuid() {
        return null;
    }

    @Override
    public Long getTradeId() {
        return null;
    }

    @Override
    public Long getTradeServerUpdateTime() {
        return null;
    }

    @Override
    public Long getTradeClientUpdateTime() {
        return null;
    }

    @Override
    public IDinnertable getDinnertable() {
        return null;
    }

    @Override
    public String getTradeAmount() {
        return null;
    }

    @Override
    public void setTradeAmount(BigDecimal tradeAmount) {

    }

    @Override
    public UpContentType getUpContent() {
        return null;
    }

    @Override
    public BusinessType getBusinessType() {
        return null;
    }

    @Override
    public YesOrNo getPreCashPrintStatus() {
        return null;
    }

    @Override
    public List<TableSeat> getTableSeats() {
        return tableSeats;
    }

    @Override
    public TradeType getTradeType() {
        return tradeType;
    }

    @Override
    public String getTableName() {
        return null;
    }
}
