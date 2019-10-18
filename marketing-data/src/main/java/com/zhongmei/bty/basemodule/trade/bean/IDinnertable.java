package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.trade.bean.IZone;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;

import java.util.List;


public interface IDinnertable {


    Long getId();


    String getName();


    int getNumberOfSeats();


    int getNumberOfMeals();


    int getTradeCount();


    TableStatus getTableStatus();


    Long getServerUpdateTime();


    IZone getZone();


    int getUnprocessTradeCount();


    boolean isCurBusinessType(BusinessType businessType);


    List<TableSeat> getTableSeats();

}
