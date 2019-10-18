package com.zhongmei.bty.basemodule.booking.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;


@DatabaseTable(tableName = "booking_trade_group_info")
public class BookingGroupInfo extends BasicEntityBase implements Cloneable {


    public interface $ extends BasicEntityBase.$ {

        String bookingId = "booking_id";

        String bookingUuid = "booking_uuid";

        String shopIdenty = "shop_identy";

        String clientCreateTime = "client_create_time";

        String clientUpdateTime = "client_update_time";

        String creatorId = "creator_id";

        String creatorName = "creator_name";

        String updatorId = "updator_id";

        String updatorName = "updator_name";

        String name = "name";

        String price = "price";

        String tableNum = "table_num";
    }

    @DatabaseField(columnName = "booking_id")
    private Long bookingId;

    @DatabaseField(columnName = "booking_uuid")
    private String bookingUuid;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "client_create_time")
    private Long clientCreateTime;

    @DatabaseField(columnName = "client_update_time")
    private Long clientUpdateTime;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "price")
    private BigDecimal price;

    @DatabaseField(columnName = "table_num")
    private Integer tableNum;


    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingUuid() {
        return bookingUuid;
    }

    public void setBookingUuid(String bookingUuid) {
        this.bookingUuid = bookingUuid;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getClientCreateTime() {
        return clientCreateTime;
    }

    public void setClientCreateTime(Long clientCreateTime) {
        this.clientCreateTime = clientCreateTime;
    }

    public Long getClientUpdateTime() {
        return clientUpdateTime;
    }

    public void setClientUpdateTime(Long clientUpdateTime) {
        this.clientUpdateTime = clientUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTableNum() {
        return tableNum;
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum = tableNum;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setClientCreateTime(System.currentTimeMillis());
        setClientUpdateTime(System.currentTimeMillis());
        setChanged(true);
        if (this instanceof ICreator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
    }

    public void validateUpdate() {
        setChanged(true);
        setClientUpdateTime(System.currentTimeMillis());
        if (this instanceof IUpdator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    public BookingGroupInfo clone() {
        BookingGroupInfo groupInfo = null;
        try {
            groupInfo = (BookingGroupInfo) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return groupInfo;
    }
}
