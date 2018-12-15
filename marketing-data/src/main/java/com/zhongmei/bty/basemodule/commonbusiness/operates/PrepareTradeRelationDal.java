package com.zhongmei.bty.basemodule.commonbusiness.operates;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;
import java.util.Map;

public interface PrepareTradeRelationDal extends IOperates {
    //预定类型
    public static final String TYPE_VALUE_BOOKING = "1";
    //排队类型
    public static final String TYPE_VALUE_LINE = "2";

    /**
     * 所有匹配数据，返回list
     *
     * @return
     * @throws Exception
     */
    List<PrepareTradeRelation> findList() throws Exception;

    List<PrepareTradeRelation> findBookingList() throws Exception;

    List<PrepareTradeRelation> findLineList() throws Exception;

    /**
     * 查询所有数据，返回map集合
     * key 为related_id
     *
     * @return
     * @throws Exception
     */
    Map<Long, PrepareTradeRelation> findMap() throws Exception;

    Map<Long, PrepareTradeRelation> findBookingMap() throws Exception;

    Map<Long, PrepareTradeRelation> findLineMap() throws Exception;


    /**
     * 查询所有数据，返回map集合
     * key 为tradeid
     * type 为null时查询所有
     *
     * @return
     * @throws Exception
     */
    Map<Long, PrepareTradeRelation> findMapByTradeId(String type) throws Exception;
}
