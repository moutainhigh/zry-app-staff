package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Date 2016/6/13
 * @Description:移菜拖动View绑定数据
 */
public class DinnertableTradeModelMoveDish implements IDinnertableTradeMoveDish {
    private DinnertableTradeVo sourceTradeVo;
    private List<IShopcartItem> items;
    private byte modeType = 0;//0：移菜  1：复制
    private int copyCount;//复制次数
    private int copyDishItemSize;


    public DinnertableTradeModelMoveDish(int dishSize) {
        this.copyCount = dishSize;
    }

    @Override
    public String getTitle() {
        return copyDishItemSize + BaseApplication.sInstance.getString(R.string.unit_item);
    }

    @Override
    public String getContent() {
        if (modeType == 0) {
            return BaseApplication.sInstance.getString(R.string.dinner_move_dishes);
        } else {
            return BaseApplication.sInstance.getString(R.string.has_copy_n_copies, copyCount);
        }

    }

    @Override
    public String getUuid() {
        return null;
    }

    @Override
    public String getSn() {
        return sourceTradeVo.getDinnertableTrade().getSn();
    }

    @Override
    public TradeStatus getTradeStatus() {
        return sourceTradeVo.getDinnertableTrade().getTradeStatus();
    }

    public void setCopyDishItemSize(int copyDishItemSize) {
        this.copyDishItemSize = copyDishItemSize;
    }

    @Override
    public int getSpendTime() {
        return 0;
    }

    @Override
    public void refreshSpendTime() {

    }

    @Override
    public int getNumberOfMeals() {
        return 0;
    }

    @Override
    public TradePayStatus getTradePayStatus() {
        return sourceTradeVo.getTradeVo().getTrade().getTradePayStatus();
    }

    @Override
    public DinnertableStatus getStatus() {
        return DinnertableStatus.UNISSUED;
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
        return sourceTradeVo.getDinnertableTrade().getTradeUuid();
    }

    @Override
    public Long getTradeId() {
        return sourceTradeVo.getDinnertableTrade().getTradeId();
    }

    @Override
    public Long getTradeServerUpdateTime() {
        return null;
    }

    @Override
    public Long getTradeClientUpdateTime() {
        return sourceTradeVo.getDinnertableTrade().getTradeClientUpdateTime();
    }

    @Override
    public IDinnertable getDinnertable() {
        return sourceTradeVo.getDinnertable();
    }

    public void setSourceTradeVo(DinnertableTradeVo sourceTradeVo) {
        this.sourceTradeVo = sourceTradeVo;
    }

    public DinnertableTradeVo getSourceTradeVo() {
        return sourceTradeVo;
    }

    public List<IShopcartItem> getItems() {
        return items;
    }

    public void setItems(List<IShopcartItem> items) {
        this.items = items;
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
        return sourceTradeVo.getDinnertableTrade().getBusinessType();
    }

    @Override
    public YesOrNo getPreCashPrintStatus() {
        return null;
    }

    @Override
    public List<TableSeat> getTableSeats() {
        return sourceTradeVo.getDinnertable().getTableSeats();
    }

    @Override
    public TradeType getTradeType() {
        return sourceTradeVo.getDinnertableTrade().getTradeType();
    }

    @Override
    public String getTableName() {
        return sourceTradeVo.getDinnertable().getName();
    }

    public void setModeType(byte modeType, int copyCount) {
        this.modeType = modeType;
        this.copyCount = copyCount;
    }

    public int getCopyCount() {
        return copyCount;
    }
}
