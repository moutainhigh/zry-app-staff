package com.zhongmei.bty.basemodule.async.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public interface AsyncDal extends IOperates {

    /**
     * 更新异步记录的url和json
     *
     * @param recUuid
     * @param json
     * @return
     * @throws Exception
     */
    boolean update(String recUuid, Long tradeId, String json, String serialNumber) throws Exception;

    /**
     * 更新状态和原因
     *
     * @param rec
     * @param state
     * @param reason
     * @return
     * @throws Exception
     */
    AsyncHttpRecord update(AsyncHttpRecord rec, AsyncHttpState state, String reason) throws Exception;

    /**
     * 重试次数＋1
     *
     * @param rec
     * @throws Exception
     */
    AsyncHttpRecord retryCountPlus(AsyncHttpRecord rec) throws Exception;

    /**
     * 根据状态删除异步记录
     *
     * @param state
     * @throws SQLException
     */
    void deleteRecordByStatus(AsyncHttpState state) throws SQLException;

    /**
     * 查询所有异步记录
     *
     * @return
     * @throws SQLException
     */
    List<AsyncHttpRecord> queryAllRecord() throws SQLException;


    /**
     * 查询非正在执行的异步纪录
     *
     * @return
     * @throws SQLException
     */
    List<AsyncHttpRecord> queryAllBesideExcuting() throws Exception;

    /**
     * 查询订单非成功的记录
     *
     * @return
     * @throws SQLException
     */
    public List<AsyncHttpRecord> queryNotSuccess(long tradeId) throws SQLException;

    /**
     * 查询对应tradeUuid对应类型的异步操作记录
     *
     * @param tradeUuid
     * @param types
     * @return
     */
    List<AsyncHttpRecord> query(String tradeUuid, Iterable<AsyncHttpType> types) throws SQLException;

    /**
     * 查询对应UUID的异步操作纪录
     *
     * @param redUuid
     * @return
     * @throws SQLException
     */
    AsyncHttpRecord query(String redUuid) throws Exception;

    /**
     * 查询所有请求失败记录
     *
     * @return
     * @throws SQLException
     */
    List<AsyncHttpRecord> queryFailRecord() throws SQLException;


    /**
     * 查询开台未成功的
     *
     * @return
     * @throws SQLException
     */
    List<AsyncHttpRecord> queryFailOpenTable() throws SQLException;


    /**
     * 根据UUID删除异步网络请求记录
     *
     * @param recUUID
     */
    void deleteByUUID(String recUUID);

    /**
     * 根据TradeUUID删除异步网络请求记录
     *
     * @param tradeUUID
     */
    void deleteByTradeUUID(String tradeUUID);

}