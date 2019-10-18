package com.zhongmei.bty.basemodule.commonbusiness.operates;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;
import java.util.Map;

public interface PrepareTradeRelationDal extends IOperates {
        public static final String TYPE_VALUE_BOOKING = "1";
        public static final String TYPE_VALUE_LINE = "2";


    List<PrepareTradeRelation> findList() throws Exception;

    List<PrepareTradeRelation> findBookingList() throws Exception;

    List<PrepareTradeRelation> findLineList() throws Exception;


    Map<Long, PrepareTradeRelation> findMap() throws Exception;

    Map<Long, PrepareTradeRelation> findBookingMap() throws Exception;

    Map<Long, PrepareTradeRelation> findLineMap() throws Exception;



    Map<Long, PrepareTradeRelation> findMapByTradeId(String type) throws Exception;
}
