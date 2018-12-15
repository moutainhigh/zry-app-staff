package com.zhongmei.bty.basemodule.booking.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.BasicEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 团餐预定表
 * <p>
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "booking_period")
public class BookingPeriod extends BasicEntityBase implements Cloneable {

//    CREATE TABLE `booking_period` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT,
//  `shop_identy` bigint(20) NOT NULL COMMENT '商户 ID',
//            `brand_identy` bigint(20) NOT NULL COMMENT '品牌 ID',
//            `status_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 有效 2 无效',
//            `server_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
//            `server_update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
//  `start_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '预定时间段开始时间\n',
//            `end_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '预定的时间段结束时间\n',
//            `booking_id` bigint(20) NOT NULL COMMENT '预定的 orderID\n',
//    PRIMARY KEY (`id`),
//    KEY `idx_shop_update` (`shop_identy`,`server_update_time`),
//    KEY `idx_booking_Id` (`booking_id`),
//    KEY `idx_start_time` (`start_time`),
//    KEY `idx_end_time` (`end_time`)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    public interface $ extends BasicEntityBase.$ {
        /**
         * 预定时间段开始时间
         */
        String startTime = "start_time";
        /**
         * 预定的时间段结束时间
         */
        String endTime = "end_time";
        /**
         * 预定的 orderID
         */
        String bookingId = "booking_id";
        /**
         * 商户 ID
         */
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
//        setClientCreateTime(System.currentTimeMillis());
//        setClientUpdateTime(System.currentTimeMillis());
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
//        setClientUpdateTime(System.currentTimeMillis());
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
