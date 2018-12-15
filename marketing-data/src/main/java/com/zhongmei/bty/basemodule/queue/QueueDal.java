package com.zhongmei.bty.basemodule.queue;

import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.basemodule.print.entity.PrintContent;
import com.zhongmei.bty.commonmodule.database.entity.QrCodeInfo;
import com.zhongmei.bty.basemodule.database.queue.QueueDetailImage;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 排队相关查询
 */
public interface QueueDal extends IOperates {

    /**
     * 当天所有排队单
     *
     * @param date
     * @return
     * @throws Exception
     */
    List<Queue> findAllDataByDate(Date date, Long queueLineId, QueueStatus queueStatus) throws Exception;

    /**
     * 通过人数获取队列
     *
     * @param personCount
     * @return
     */
    CommercialQueueLine findQueueLineIdByPersonCount(int personCount) throws Exception;

    /**
     * 获取排队序号
     *
     * @param queueLineId
     * @return
     */
    int getQueueNumber(long queueLineId) throws Exception;

    /**
     * 保存数据
     *
     * @param queue
     */
    void saveQueue(Queue queue) throws Exception;

    /**
     * 更新排队
     *
     * @param queue
     * @throws Exception
     */
    void updateQueue(Queue queue) throws Exception;

    /**
     * 通过id查找队列
     *
     * @param queueLineId
     * @return
     * @throws Exception
     */
    CommercialQueueLine findQueueLineByid(Long queueLineId) throws Exception;

    /**
     * 等待人数
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    long getCountByQueueUuid(Queue queue) throws Exception;

    /**
     * 查询所有的队列分类
     *
     * @return
     * @throws Exception
     */
    List<CommercialQueueLine> queryQueueLineList() throws Exception;

    /**
     * 获取当前叫号的信息
     *
     * @param queueLindIds
     * @return
     * @throws Exception
     */
    List<Queue> queueListRemind(Long... queueLindIds) throws Exception;

    /**
     * 队列清零
     *
     * @param queueList
     * @throws Exception
     */
    void cleanQueueList(List<Queue> queueList) throws Exception;

    /**
     * 查询未处理订单
     *
     * @return
     * @throws Exception
     */
    List<Queue> listUnProcess() throws Exception;

    /**
     * 查询未处理订单数量
     *
     * @return
     * @throws Exception
     */
    long getCountUnProcess() throws Exception;

    /**
     * 查找排队
     *
     * @param uuid
     * @return
     */
    Queue getQueueByUuid(String uuid) throws Exception;

    /**
     * 获取二维码信息
     *
     * @return
     * @throws Exception
     */
    QrCodeInfo getQrCodeInfo() throws Exception;

    /**
     * 获取排队自定义内容
     *
     * @return
     * @throws Exception
     */
    List<PrintContent> getPrintContent(String commercialId) throws Exception;

    /**
     * 获取QueueExtraList
     */
    List<QueueExtra> getQueueExtraList(List<Long> queueIdList) throws Exception;

    QueueExtra getQueueExtra(Long queueId) throws Exception;

    /**
     * 查询某个时间段内的排队数据
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws SQLException
     */
    List<Queue> listQueueBetweenTime(Long queueLineId, Date startDate, Date endDate) throws SQLException;
}
