package com.zhongmei.beauty.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "booking_trade_item_user")
public class BookingTradeItemUser extends IdEntityBase {

    public interface $ extends IdEntityBase.$ {

    }

    @DatabaseField(columnName = "user_id")
    Long userId;
    @DatabaseField(columnName = "user_name")
    String userName;
    @DatabaseField(columnName = "user_type")
    Integer userType;

    @DatabaseField(columnName = "role_id")
    private Long roleId;

    @DatabaseField(columnName = "role_name")
    private String roleName;

    @DatabaseField(columnName = "booking_id")
    Long bookingId;
    @DatabaseField(columnName = "booking_trade_item_id")
    Long bookingTradeItemId;
    @DatabaseField(columnName = "booking_trade_item_uuid")
    String bookingTradeItemUuid;
    @DatabaseField(columnName = "status_flag")
    Integer statusFlag;
    @DatabaseField(columnName = "is_assign")
    Integer isAssign;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingTradeItemId() {
        return bookingTradeItemId;
    }

    public void setBookingTradeItemId(Long bookingTradeItemId) {
        this.bookingTradeItemId = bookingTradeItemId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookingTradeItemUuid() {
        return bookingTradeItemUuid;
    }

    public void setBookingTradeItemUuid(String bookingTradeItemUuid) {
        this.bookingTradeItemUuid = bookingTradeItemUuid;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Integer getIsAssign() {
        return isAssign;
    }

    public void setIsAssign(Integer isAssign) {
        this.isAssign = isAssign;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return id;
    }
}
