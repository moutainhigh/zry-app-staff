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


@DatabaseTable(tableName = "customer_arrival_shop")
public class CustomerArrivalShop extends EntityBase<Long> {


    public interface $ extends IdEntityBase.$ {




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
    public Integer customerSex;     @DatabaseField(columnName = "customer_phone")
    public String customerPhone;     @DatabaseField(columnName = "arrival_status")
    public Integer arrivalStatus;     @DatabaseField(columnName = "app_type")
    public Integer appType;     @DatabaseField(columnName = "table_id")
    public Long tableId;     @DatabaseField(columnName = "table_name")
    public String tableName;     @DatabaseField(columnName = "arrival_way")
    public int arrivalWay;
    @DatabaseField(columnName = "server_create_time")
    public Long serverCreateTime;
    @DatabaseField(columnName = "send_coupon_count")
    public int sendCouponCount;
    @DatabaseField(columnName = "uuid", canBeNull = false)    private String uuId;
    @DatabaseField(columnName = "operate_status", canBeNull = false)
    public Integer operateStatus = 1;
    @DatabaseField(columnName = "customer_level")
    public Integer level;
    @DatabaseField(columnName = "customer_level_name")
    public String levelName;
    @DatabaseField(columnName = "customer_remainValue")
    public BigDecimal remainValue;
    @DatabaseField(columnName = "customer_integral")
    public Integer integral;
    @DatabaseField(columnName = "customer_coupCount")
    public Integer coupCount;
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
