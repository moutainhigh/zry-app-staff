package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;
import java.util.Map;

import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;


public interface BaiduSyntheticSpeechDal extends IOperates {



    void saveQueue(BaiduSyntheticSpeech speech) throws Exception;

    void saveQueueList(List<BaiduSyntheticSpeech> speechList) throws Exception;


    void updateQueue(BaiduSyntheticSpeech speech) throws Exception;



    List<BaiduSyntheticSpeech> listSyntherticSpeech() throws Exception;


    BaiduSyntheticSpeech getCallSpeech() throws Exception;


    void delete(BaiduSyntheticSpeech speech) throws Exception;

    Map<Integer, BaiduSyntheticSpeech> getQueueVoiceMap() throws Exception;

    List<BaiduSyntheticSpeech> getQueueVoiceList() throws Exception;


}
