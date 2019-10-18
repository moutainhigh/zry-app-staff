package com.zhongmei.bty.dinner.vo;

import com.zhongmei.yunfu.db.entity.CommercialArea;

import java.util.List;
import java.util.Map;



public class DinnerConnectTablesAreaVo {


    public static final int TABLES_AREA_STATUS_ALL = 0;


    public static final int TABLES_AREA_STATUS_EMPTY = 1;


    public static final int TABLES_AREA_STATUS_OCCUPY = 2;


    public static final int TABLES_CONNECT = 3;

    public CommercialArea area;

    public int status = TABLES_AREA_STATUS_ALL;

    public boolean isSelected = false;


    public Map<Long, List<DinnerConnectTablesVo>> allTablesVoMap;


    public Map<Long, List<DinnerConnectTablesVo>> EmptyTablesVoMap;


    public Map<Long, List<DinnerConnectTablesVo>> occupyTablesVoMap;



    public Map<Long, List<DinnerConnectTablesVo>> connectTablesVoMap;

}
