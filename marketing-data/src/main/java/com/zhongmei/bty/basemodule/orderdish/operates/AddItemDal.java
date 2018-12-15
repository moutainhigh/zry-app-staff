package com.zhongmei.bty.basemodule.orderdish.operates;

import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemBatch;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 微信加菜操作类
 */

public interface AddItemDal extends IOperates {
    /**
     * 根据tableID查询微信加菜信息
     *
     * @param tableId
     * @return
     */
    public List<AddItemVo> queryAdditemsByTableId(long tableId);


    /**
     * 根据TradeId查询微信加菜信息
     *
     * @param tradeId
     * @return
     */
    public List<AddItemVo> queryAdditemsByTradeId(long tradeId);


    /**
     * 根据tableid查询AdditemBatch
     *
     * @param tableId
     * @return
     */
    public List<AddItemBatch> queryItemBatchsByTableId(long tableId);

    /**
     * 查询所有的AdditemBatchs
     *
     * @return
     */
    public List<AddItemBatch> queryAllItemBatchs();


    /**
     * 根据tradeid查询AdditemBatch
     *
     * @param tradeId
     * @return
     */
    public List<AddItemBatch> queryItemBatchsByTradeId(long tradeId);

    /**
     * 根据batchId查询加菜记录
     *
     * @param batchId
     * @return
     */
    public List<AddItemRecord> queryItemRecordsByBatchId(long batchId);


    /**
     * 查询所有的加菜记录
     *
     * @return
     */
    public List<AddItemRecord> queryAllItemRecords();


    /**
     * 根据tableId查询AdditemRecords
     *
     * @param tableId
     * @return
     */
    public List<AddItemRecord> queryItemRecordssByTableId(long tableId);

    /**
     * 根据tradeId查询加菜记录
     *
     * @param tradeId
     * @return
     */
    public List<AddItemRecord> queryItemRecordsByTradeId(long tradeId);

}
