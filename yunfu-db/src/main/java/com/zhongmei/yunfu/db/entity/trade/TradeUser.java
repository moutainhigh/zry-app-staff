package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.enums.TradeScenceType;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "trade_user")
public class TradeUser extends BasicEntityBase {
    private static final long serialVersionUID = 1L;

    public interface $ extends BasicEntityBase.$ {

        public static final String tradeId = "trade_id";
        public static final String tradeItemUuid = "trade_item_uuid";
        public static final String tradeItemId = "trade_item_id";
        public static final String roleId = "role_id";
        public static final String roleName = "role_name";

    }

    @DatabaseField(columnName = "trade_id", canBeNull = false)
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "user_id", canBeNull = false)
    private Long userId;

    @DatabaseField(columnName = "user_name")
    private String userName;

    @DatabaseField(columnName = "type")
    private Integer type;
    @DatabaseField(columnName = "role_id")
    private Long roleId;

    @DatabaseField(columnName = "role_name")
    private String roleName;

    @DatabaseField(columnName = "trade_item_id")
    private Long tradeItemId;

    @DatabaseField(columnName = "trade_item_uuid")
    private String tradeItemUuid;

    @DatabaseField(columnName = "is_assign")
    private boolean IsAssign;
        private String uuid;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setType(TradeScenceType type) {
        this.type = ValueEnums.toValue(type);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getTradeItemUuid() {
        return tradeItemUuid;
    }

    public void setTradeItemUuid(String tradeItemUuid) {
        this.tradeItemUuid = tradeItemUuid;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isAssign() {
        return IsAssign;
    }

    public void setIsAssign(boolean assign) {
        IsAssign = assign;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }
}
