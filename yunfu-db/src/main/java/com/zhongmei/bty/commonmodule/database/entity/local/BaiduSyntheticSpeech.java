package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;


@DatabaseTable(tableName = "baidu_synthetic_speech")
public class BaiduSyntheticSpeech extends LocalEntityBase {

    private static final long serialVersionUID = 1L;


    public interface $ extends LocalEntityBase.$ {


        public static final String context = "content";


        public static final String sex = "sex";


        public static final String speed = "speed";


        public static final String type = "type";


        public static final String name = "name";


        public static final String queueVoiceType = "queue_voice_type";

    }


    @DatabaseField(columnName = "name")
    private String name;


    @DatabaseField(columnName = "content")
    private String content;


    @DatabaseField(columnName = "sex")
    private Integer sex;


    @DatabaseField(columnName = "speed")
    private Integer speed;


    @DatabaseField(columnName = "type")
    private Integer type;


    @DatabaseField(columnName = "queue_voice_type")
    private Integer queueVoiceType;

    public static BaiduSyntheticSpeech create(String content, Sex sex) {
        BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        speech.setType(2);
        speech.setContent(content);
        speech.setSex(sex);
        speech.setSpeed(5);
        return speech;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Sex getSex() {
        return ValueEnums.toEnum(Sex.class, sex);
    }

    public void setSex(Sex sex) {
        this.sex = ValueEnums.toValue(sex);
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getQueueVoiceType() {
        return queueVoiceType;
    }

    public void setQueueVoiceType(Integer queueVoiceType) {
        this.queueVoiceType = queueVoiceType;
    }
}
