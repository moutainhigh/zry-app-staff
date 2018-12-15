package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.Sex;

/**
 * 合成语音记录
 */
@DatabaseTable(tableName = "baidu_synthetic_speech")
public class BaiduSyntheticSpeech extends LocalEntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "sync_mark"
     */
    public interface $ extends LocalEntityBase.$ {

        /**
         * content
         */
        public static final String context = "content";

        /**
         * sex
         */
        public static final String sex = "sex";

        /**
         * speed
         */
        public static final String speed = "speed";

        /**
         * type
         */
        public static final String type = "type";

        /**
         * name
         */
        public static final String name = "name";

        /**
         * 排队叫号模板类型
         */
        public static final String queueVoiceType = "queue_voice_type";

    }

    /**
     * 名称
     */
    @DatabaseField(columnName = "name")
    private String name;

    /**
     * 播放内容
     */
    @DatabaseField(columnName = "content")
    private String content;

    /**
     * 播放性别
     */
    @DatabaseField(columnName = "sex")
    private Integer sex;

    /**
     * 1最慢， 3较慢 ，5正常， 7较快， 9 最快
     */
    @DatabaseField(columnName = "speed")
    private Integer speed;

    /**
     * 1，叫号语音，2，广播语音
     */
    @DatabaseField(columnName = "type")
    private Integer type;

    /**
     * 排队叫号模板类型：
     * 0：普通叫号模板
     * 1：智能叫号，入场模板
     * 2：智能叫号，下一个排队叫号模板
     */
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
