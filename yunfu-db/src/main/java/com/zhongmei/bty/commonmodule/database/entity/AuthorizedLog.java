package com.zhongmei.bty.commonmodule.database.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.bty.commonmodule.database.enums.AuthType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "authorized_log")
public class AuthorizedLog extends DataEntityBase {
    public interface $ extends DataEntityBase.$ {
        public static final String tradeId = "trade_id";
        public static final String tradeUuid = "trade_uuid";
        public static final String orderType = "order_type";
        public static final String authType = "auth_type";
        public static final String authTypeDesc = "auth_type_desc";
        public static final String authTime = "auth_time";
        public static final String authUserId = "auth_user_id";
        public static final String authUserName = "auth_user_name";
        public static final String authContent = "auth_content";
        public static final String versionUuid = "version_uuid ";
        public static final String operateId = "operate_id ";
        public static final String operateName = "operate_name ";

    }

    @DatabaseField(columnName = "trade_id")
    Long tradeId;

    /**
     * '订单号trade.uuid'
     */
    @DatabaseField(columnName = "trade_uuid")
    String tradeUuid;

    /**
     * 指关联订单日志的操作type
     */
    @DatabaseField(columnName = "order_type")
    String orderType;

    /**
     * 授权操作类型 code
     */
    @DatabaseField(columnName = "auth_type")
    Integer authType;

    /**
     * 授权操作文字描述
     */
    @DatabaseField(columnName = "auth_type_desc")
    String authTypeDesc;

    /**
     * 权限时间
     */
    @DatabaseField(columnName = "auth_time")

    Long authTime;
    /**
     * 授权人Id
     */
    @DatabaseField(columnName = "auth_user_id")
    Long authUserId;

    /**
     * 授权人名称
     */
    @DatabaseField(columnName = "auth_user_name")
    String authUserName;

    /**
     * 授权内容描述
     */
    @DatabaseField(columnName = "auth_content")
    String authContent;

    /**
     * 关联订单请求的唯一标识
     */
    @DatabaseField(columnName = "version_uuid")
    String versionUuid;

    /**
     * 操作人id，当前登录用户的id
     */
    @DatabaseField(columnName = "operate_id")
    Long operateId;

    /**
     * 操作人名，当前登录用户的名字
     */
    @DatabaseField(columnName = "operate_name")
    String operateName;


    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public OrderActionEnum getOrderType() {
        return ValueEnums.toEnum(OrderActionEnum.class, orderType);
    }

    public void setOrderType(OrderActionEnum orderType) {
        this.orderType = ValueEnums.toValue(orderType);
    }

    public AuthType getAuthType() {
        return ValueEnums.toEnum(AuthType.class, authType);
    }

    public void setAuthType(AuthType authType) {
        this.authType = ValueEnums.toValue(authType);
    }

    public Long getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Long authTime) {
        this.authTime = authTime;
    }

    public Long getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(Long authUserId) {
        this.authUserId = authUserId;
    }

    public String getAuthUserName() {
        return authUserName;
    }

    public void setAuthUserName(String authUserName) {
        this.authUserName = authUserName;
    }

    public String getAuthContent() {
        return authContent;
    }

    public void setAuthContent(String authContent) {
        this.authContent = authContent;
    }

    public String getAuthTypeDesc() {
        return authTypeDesc;
    }

    public void setAuthTypeDesc(String authTypeDesc) {
        this.authTypeDesc = authTypeDesc;
    }

    public String getVersionUuid() {
        return versionUuid;
    }

    public void setVersionUuid(String versionUuid) {
        this.versionUuid = versionUuid;
    }


    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }
}

