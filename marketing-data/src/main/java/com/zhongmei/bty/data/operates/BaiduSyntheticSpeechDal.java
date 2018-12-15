package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;
import java.util.Map;

import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;

/**
 * 排队相关查询
 */
public interface BaiduSyntheticSpeechDal extends IOperates {


    /**
     * 保存语音
     *
     * @param queue
     */
    void saveQueue(BaiduSyntheticSpeech speech) throws Exception;

    void saveQueueList(List<BaiduSyntheticSpeech> speechList) throws Exception;

    /**
     * 更新语音
     *
     * @param queue
     * @throws Exception
     */
    void updateQueue(BaiduSyntheticSpeech speech) throws Exception;


    /**
     * 查询合成广播语音列表
     *
     * @param queue
     */
    List<BaiduSyntheticSpeech> listSyntherticSpeech() throws Exception;

    /**
     * 获取叫号语音
     *
     * @param queue
     */
    BaiduSyntheticSpeech getCallSpeech() throws Exception;

    /**
     * 删除
     *
     * @param speech
     */
    void delete(BaiduSyntheticSpeech speech) throws Exception;

    Map<Integer, BaiduSyntheticSpeech> getQueueVoiceMap() throws Exception;

    List<BaiduSyntheticSpeech> getQueueVoiceList() throws Exception;


}
