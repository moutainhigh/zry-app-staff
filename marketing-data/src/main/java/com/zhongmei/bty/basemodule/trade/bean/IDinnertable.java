package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.bean.IZone;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;

import java.util.List;

/**
 * 餐台信息
 *
 * @version: 1.0
 * @date 2015年9月22日
 */
public interface IDinnertable {

    /**
     * 返回桌台ID
     *
     * @return
     */
    Long getId();

    /**
     * 返回桌台名称
     *
     * @return
     */
    String getName();

    /**
     * 返回桌台座位数
     *
     * @return
     */
    int getNumberOfSeats();

    /**
     * 返回桌台上的总就餐人数
     *
     * @return
     */
    int getNumberOfMeals();

    /**
     * 返回桌台上的单据总数
     *
     * @return
     */
    int getTradeCount();

    /**
     * 返回桌台状态
     *
     * @return
     */
    TableStatus getTableStatus();

    /**
     * 返回桌台的服务器更新时间
     *
     * @return
     */
    Long getServerUpdateTime();

    /**
     * 返回桌台所属区域
     *
     * @return
     */
    IZone getZone();

    /**
     * 返回桌台上未处理的单据总数
     *
     * @return
     */
    int getUnprocessTradeCount();

    /**
     * 返回该桌台是否只存在当前业务类型的单子
     *
     * @param businessType
     * @return
     */
    boolean isCurBusinessType(BusinessType businessType);

    /**
     * 获取坐台对应的座位号
     *
     * @return
     */
    List<TableSeat> getTableSeats();

}
