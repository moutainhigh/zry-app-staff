package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.List;

/**
 * 桌台单据信息
 *
 * @version: 1.0
 * @date 2015年9月7日
 */
public interface IDinnertableTrade {

    /**
     * 返回UUID，即trade_table.uuid
     *
     * @return
     */
    String getUuid();

    /**
     * 返回流水号
     *
     * @return
     */
    String getSn();

    /**
     * 返回单据状态
     *
     * @return
     */
    TradeStatus getTradeStatus();

    /**
     * 返回耗时，单位分钟
     *
     * @return
     */
    int getSpendTime();

    /**
     * 刷新耗时，即从开台到当前有多少分钟
     */
    void refreshSpendTime();

    /**
     * 返回就餐人数
     *
     * @return
     */
    int getNumberOfMeals();

    /**
     * 返回支付状态
     *
     * @return
     */
    TradePayStatus getTradePayStatus();

    /**
     * 返回出单状态
     *
     * @return
     */
    DinnertableStatus getStatus();

    /**
     * 返回ID，即trade_table.id，没有保存时为null
     *
     * @return
     */
    Long getId();

    /**
     * trade_table.server_update_time
     *
     * @return
     */
    Long getServerUpdateTime();

    /**
     * 返回订单UUID
     *
     * @return
     */
    String getTradeUuid();

    /**
     * 返回订单ID
     *
     * @return
     */
    Long getTradeId();

    /**
     * trade.server_update_time
     *
     * @return
     */
    Long getTradeServerUpdateTime();

    /**
     * trade.client_update_time
     *
     * @return
     */
    Long getTradeClientUpdateTime();

    /**
     * 返回桌台信息
     *
     * @return
     */
    IDinnertable getDinnertable();

    /**
     * 返回订单金额
     */
    String getTradeAmount();

    /**
     * 设置主单金额(联台主单订单价格变动)
     */
    void setTradeAmount(BigDecimal tradeAmount);

    /**
     * 返回上面内容类型x
     */
    UpContentType getUpContent();

    /**
     * @return
     */
    public BusinessType getBusinessType();

    enum UpContentType {
        SN,//订单号
        TRADE_AMOUNT//订单金额
    }

    /**
     * 获取预结单打印状态
     */
    YesOrNo getPreCashPrintStatus();

    /**
     * 返回订单对应的桌台座位号
     *
     * @return
     */
    List<TableSeat> getTableSeats();

    TradeType getTradeType();

    public String getTableName();

}
