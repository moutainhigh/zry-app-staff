package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.IdEntityBase;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.bty.commonmodule.database.enums.AppType;
import com.zhongmei.bty.commonmodule.database.enums.ArriveStatus;
import com.zhongmei.bty.commonmodule.database.enums.ArriveWay;

import java.math.BigDecimal;

/**
 * Desc
 *
 * @created 2017/7/27
 */
@DatabaseTable(tableName = "customer_arrival_shop")
public class CustomerArrivalShop extends EntityBase<Long> {

    /**
     * The columns of table "lyd_market_setting"
     */
    public interface $ extends IdEntityBase.$ {

        /**
         * status_flag
         */
        //String statusFlag = "status_flag";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";


        String customerId = "customer_id";
        String customerName = "customer_name";
        String customerSex = "customer_sex";
        String customerPhone = "customer_phone";
        String arrivalStatus = "arrival_status";
        String appType = "app_type";
        String tableId = "table_id";
        String tableName = "table_name";
        String arrivalWay = "arrival_way";
        String sendCouponCount = "send_coupon_count";
        String uuid = "uuid";
        String operateStatus = "operate_status";
    }

    @DatabaseField(columnName = "id", generatedId = true)
    private Long id;
    @DatabaseField(columnName = "customer_id")
    public Long customerId;
    @DatabaseField(columnName = "customer_name")
    public String customerName;
    @DatabaseField(columnName = "customer_sex")
    public Integer customerSex; //顾客性别:-1 未知 0女 1男
    @DatabaseField(columnName = "customer_phone")
    public String customerPhone; //顾客手机号
    @DatabaseField(columnName = "arrival_status")
    public Integer arrivalStatus; //到店状态(1.已到店; 2.已离店;3.到店周围;4.离店)
    @DatabaseField(columnName = "app_type")
    public Integer appType; //应用类型
    @DatabaseField(columnName = "table_id")
    public Long tableId; //桌台ID
    @DatabaseField(columnName = "table_name")
    public String tableName; //桌台名称
    @DatabaseField(columnName = "arrival_way")
    public int arrivalWay; //到店方式(1.微信;2.熟客、3.pos扫描)

    @DatabaseField(columnName = "server_create_time")
    public Long serverCreateTime;
    @DatabaseField(columnName = "send_coupon_count")
    public int sendCouponCount; //发券次数

    @DatabaseField(columnName = "uuid", canBeNull = false)//唯一标识
    private String uuId;
    @DatabaseField(columnName = "operate_status", canBeNull = false)
    public Integer operateStatus = 1; //处理状态(1.未处理; 2.已处理;)

    @DatabaseField(columnName = "customer_level")
    public Integer level;//会员等级

    @DatabaseField(columnName = "customer_level_name")
    public String levelName;//会员等级名称

    @DatabaseField(columnName = "customer_remainValue")
    public BigDecimal remainValue;//会员余额

    @DatabaseField(columnName = "customer_integral")
    public Integer integral;//会员积分

    @DatabaseField(columnName = "customer_coupCount")
    public Integer coupCount;//会员可用券数

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long verValue() {
        return serverCreateTime;
    }

    public Sex getCustomerSex() {
        return ValueEnums.toEnum(Sex.class, customerSex);
    }

    public ArriveStatus getArrivalStatus() {
        return ValueEnums.toEnum(ArriveStatus.class, arrivalStatus);
    }

    public AppType getAppType() {
        return ValueEnums.toEnum(AppType.class, appType);
    }

    public ArriveWay getArrivalWay() {
        return ValueEnums.toEnum(ArriveWay.class, arrivalWay);
    }
}
