package com.zhongmei.bty.dinner.vo;

import com.zhongmei.yunfu.db.entity.CommercialArea;

import java.util.List;
import java.util.Map;

/**
 * 桌台批量操作的按区域划分的Vo
 * Created by demo on 2018/12/15
 */

public class DinnerConnectTablesAreaVo {

    /**
     * 所有桌台
     */
    public static final int TABLES_AREA_STATUS_ALL = 0;

    /**
     * 空闲桌台
     */
    public static final int TABLES_AREA_STATUS_EMPTY = 1;

    /**
     * 占用桌台
     */
    public static final int TABLES_AREA_STATUS_OCCUPY = 2;

    /**
     * 已联台
     */
    public static final int TABLES_CONNECT = 3;

    public CommercialArea area;

    public int status = TABLES_AREA_STATUS_ALL;

    public boolean isSelected = false;

    /**
     * 所有桌台的hashMap
     * key:桌台的座位数
     * value:对应座位数的所属桌台集合
     */
    public Map<Long, List<DinnerConnectTablesVo>> allTablesVoMap;

    /**
     * 空闲桌台的hashMap
     * key:桌台的座位数
     * value:对应座位数的所属桌台集合
     */
    public Map<Long, List<DinnerConnectTablesVo>> EmptyTablesVoMap;

    /**
     * 占用桌台的hashMap
     * key:桌台的座位数
     * value:对应座位数的所属桌台集合
     */
    public Map<Long, List<DinnerConnectTablesVo>> occupyTablesVoMap;


    /**
     * 联台桌台的hashMap
     * key:联台主单Id
     * value:对应关联该联台的桌台集合
     */
    public Map<Long, List<DinnerConnectTablesVo>> connectTablesVoMap;

}
