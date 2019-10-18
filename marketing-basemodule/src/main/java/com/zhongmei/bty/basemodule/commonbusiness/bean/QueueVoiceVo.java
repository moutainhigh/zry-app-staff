package com.zhongmei.bty.basemodule.commonbusiness.bean;

import java.io.Serializable;

import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;


public class QueueVoiceVo implements Serializable {


    private static final long serialVersionUID = 1L;
    private String name;

    private boolean isSelected;


    private int type;


    private BaiduSyntheticSpeech speech;


    private String path;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BaiduSyntheticSpeech getSpeech() {
        return speech;
    }

    public void setSpeech(BaiduSyntheticSpeech speech) {
        this.speech = speech;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
