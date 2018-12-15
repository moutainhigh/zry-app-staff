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

/**
 * 团餐预定表
 * <p>
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "booking_trade_group_info")
public class BookingGroupInfo extends BasicEntityBase implements Cloneable {

//    CREATE TABLE `booking_trade_group_info` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
//            `booking_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '预定id',
//            `booking_uuid` varchar(32) NOT NULL DEFAULT '' COMMENT '预定uuid',
//            `brand_identy` bigint(20) NOT NULL DEFAULT '0' COMMENT '品牌id',
//            `shop_identy` bigint(20) NOT NULL DEFAULT '0' COMMENT '门店id',
//            `status_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1有效的 2无效的',
//            `client_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '客户端创建时间',
//            `client_update_time` timestamp(3) NULL DEFAULT NULL COMMENT '客户端更新时间',
//            `server_create_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '服务器创建时间',
//            `server_update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '服务器更新时间',
//            `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者，创建记录的系统用户',
//            `creator_name` varchar(32) DEFAULT NULL COMMENT '创建者姓名',
//            `updator_id` bigint(20) DEFAULT NULL COMMENT '最后修改此记录的用户',
//            `updator_name` varchar(32) DEFAULT NULL COMMENT '最后修改者姓名',
//            `name` varchar(120) DEFAULT NULL COMMENT '团餐名',
//            `price` decimal(10,2) DEFAULT '0.00' COMMENT '餐标价格',
//            `table_num` smallint(3) DEFAULT '0' COMMENT '桌台数量',
//    PRIMARY KEY (`id`),
//    KEY `idx_booking_id` (`booking_id`) USING BTREE,
//    KEY `idx_brand_identy` (`brand_identy`) USING BTREE,
//    KEY `idx_shop_identy` (`shop_identy`) USING BTREE,
//    KEY `idx_server_update_time` (`server_update_time`) USING BTREE
//) ENGINE=InnoDB AUTO_INCREMENT=813 DEFAULT CHARSET=utf8 COMMENT='预定团餐信息';

    public interface $ extends BasicEntityBase.$ {
        /**
         * 预定id
         */
        String bookingId = "booking_id";
        /**
         * 预定uuid
         */
        String bookingUuid = "booking_uuid";
        /**
         * 门店id
         */
        String shopIdenty = "shop_identy";
        /**
         * 客户端创建时间
         */
        String clientCreateTime = "client_create_time";
        /**
         * 客户端更新时间
         */
        String clientUpdateTime = "client_update_time";
        /**
         * 创建者，创建记录的系统用户
         */
        String creatorId = "creator_id";
        /**
         * 创建者姓名
         */
        String creatorName = "creator_name";
        /**
         * 最后修改此记录的用户
         */
        String updatorId = "updator_id";
        /**
         * 最后修改者姓名
         */
        String updatorName = "updator_name";
        /**
         * 团餐名
         */
        String name = "name";
        /**
         * 餐标价格
         */
        String price = "price";
        /**
         * 桌台数量
         */
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

    /**
     * 门店Identy
     */
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
