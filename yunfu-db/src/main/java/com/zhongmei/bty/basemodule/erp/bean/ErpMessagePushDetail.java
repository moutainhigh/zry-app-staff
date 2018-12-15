package com.zhongmei.bty.basemodule.erp.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

//消息推送详情表
@DatabaseTable(tableName = "erp_message_push_detail")
public class ErpMessagePushDetail extends IdEntityBase {

    public interface $ extends IdEntityBase.$ {
        String status = "status";
        String title = "title";
        String thumbnail = "thumbnail";
        String detail = "detail";
        String serverCreateTime = "server_create_time";
        String serverUpdateTime = "server_update_time";
        String categoryId = "category_id";
        String effectiveDate = "effective_date";
        String expiryDate = "expiry_date";
        String msgLevel = "msg_level";
        String readedLocal = "readed_local";
    }

    public static final int CategoryNotice = 14;
    public static final int CategoryMessage = 8;

    @DatabaseField(columnName = "status")
    Integer status; //消息状态 1-有效 2-无效
    @DatabaseField(columnName = "title")
    String title;
    @DatabaseField(columnName = "thumbnail")
    String thumbnail; //标题图片
    @DatabaseField(columnName = "detail")
    String detail; //消息的详细信息 html格式
    @DatabaseField(columnName = "server_create_time")
    Long serverCreateTime;
    @DatabaseField(columnName = "server_update_time")
    Long serverUpdateTime;

    @DatabaseField(columnName = "category_id")
    Integer categoryId; //on pos 的消息通知category_id是8，公告category_id是14
    @DatabaseField(columnName = "msg_level")
    int msgLevel; //消息级别：1，普通；2，严重；3，特别严重’
    @DatabaseField(columnName = "effective_date")
    long effectiveDate; //生效时间
    @DatabaseField(columnName = "expiry_date")
    long expiryDate; //失效时间
    @DatabaseField(columnName = "readed_local")
    int readedLocal; //已读数

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(int msgLevel) {
        this.msgLevel = msgLevel;
    }

    public int getReadedLocal() {
        return readedLocal;
    }

    public void setReadedLocal(int readedLocal) {
        this.readedLocal = readedLocal;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, status);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }
}
