package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.basemodule.database.queue.QueueDetailImage;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

import com.zhongmei.bty.commonmodule.database.entity.CommercialQueueConfigFile;
import com.zhongmei.bty.data.db.common.queue.QueueImage;

/**
 * 文件列表
 */
public interface CommerQueueConfigFileDal extends IOperates {


    /**
     * 查询下载广播语音下载列表
     *
     * @param queue
     */
    List<CommercialQueueConfigFile> listSysFileBroadCast() throws Exception;

    /**
     * 查询无效下载广播语音下载列表
     *
     * @param queue
     */
    List<CommercialQueueConfigFile> listSysFileBroadCastInValid() throws Exception;

    /**
     * 叫号语音包下载地址
     *
     * @return
     * @throws Exception
     */
    QueueImage getQueueCallZip() throws Exception;

    /**
     * 验证文件是否有效
     *
     * @param fileName
     * @return
     */
    boolean validFile(String fileName) throws Exception;


    QueueImage getQueueImage() throws Exception;

    List<QueueDetailImage> getQueueDetailImageList(String queueImgId) throws Exception;

}
