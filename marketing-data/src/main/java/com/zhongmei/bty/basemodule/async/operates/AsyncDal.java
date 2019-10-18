package com.zhongmei.bty.basemodule.async.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;

import java.sql.SQLException;
import java.util.List;


public interface AsyncDal extends IOperates {


    boolean update(String recUuid, Long tradeId, String json, String serialNumber) throws Exception;


    AsyncHttpRecord update(AsyncHttpRecord rec, AsyncHttpState state, String reason) throws Exception;


    AsyncHttpRecord retryCountPlus(AsyncHttpRecord rec) throws Exception;


    void deleteRecordByStatus(AsyncHttpState state) throws SQLException;


    List<AsyncHttpRecord> queryAllRecord() throws SQLException;



    List<AsyncHttpRecord> queryAllBesideExcuting() throws Exception;


    public List<AsyncHttpRecord> queryNotSuccess(long tradeId) throws SQLException;


    List<AsyncHttpRecord> query(String tradeUuid, Iterable<AsyncHttpType> types) throws SQLException;


    AsyncHttpRecord query(String redUuid) throws Exception;


    List<AsyncHttpRecord> queryFailRecord() throws SQLException;



    List<AsyncHttpRecord> queryFailOpenTable() throws SQLException;



    void deleteByUUID(String recUUID);


    void deleteByTradeUUID(String tradeUUID);

}