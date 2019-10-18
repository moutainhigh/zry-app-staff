package com.zhongmei.bty.basemodule.booking.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "booking_period")
public class BookingPeriod extends BasicEntityBase implements Cloneable {


    public interface $ extends BasicEntityBase.$ {

        String startTime = "start_time";

        String endTime = "end_time";

        String bookingId = "booking_id";

        String shopIdenty = "shop_identy";
    }

    @DatabaseField(columnName = "booking_id")
    private Long bookingId;

    @DatabaseField(columnName = "end_time")
    private Long endTime;

    @DatabaseField(columnName = "start_time")
    private Long startTime;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
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
        if (this instanceof IUpdator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    public BookingPeriod clone() {
        BookingPeriod groupInfo = null;
        try {
            groupInfo = (BookingPeriod) super.clone();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return groupInfo;
    }
}
