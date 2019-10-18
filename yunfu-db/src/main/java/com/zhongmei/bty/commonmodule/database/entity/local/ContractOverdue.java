package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;


@DatabaseTable(tableName = "contract_overdue")
public class ContractOverdue extends EntityBase<Long> {

    public interface $ {


        public static final String id = "id";
        public static String shopId = "shopId";
        public static String serviceId = "serviceId";
        public static String serviceName = "serviceName";
        public static String expireDate = "expireDate";
        public static String remainDays = "remainDays";
        public static String salesName = "salesName";
        public static String salesPhone = "salesPhone";
        public static String time = "time";
    }


    @DatabaseField(columnName = "id", canBeNull = false, id = true)
    private Long id;


    @DatabaseField(columnName = "serviceName", canBeNull = false)
    private String serviceName;

    @DatabaseField(columnName = "serviceId")
    private Long serviceId;


    @DatabaseField(columnName = "expireDate", canBeNull = false)
    private long expireDate;

    @DatabaseField(columnName = "remainDays", canBeNull = false)
    private int remainDays;

    @DatabaseField(columnName = "salesName", canBeNull = false)
    private String salesName;

    @DatabaseField(columnName = "salesPhone")
    private String salesPhone;



    @DatabaseField(columnName = "time")
    private long time;


    @DatabaseField(columnName = "shopId", canBeNull = false)
    private String shopId;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public int getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(int remainDays) {
        this.remainDays = remainDays;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSalesPhone() {
        return salesPhone;
    }

    public void setSalesPhone(String salesPhone) {
        this.salesPhone = salesPhone;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public Long verValue() {
        return expireDate;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Long pkValue() {
        return id;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(id, serviceName, expireDate, remainDays, salesName, shopId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof ContractOverdue) {
            ContractOverdue c = (ContractOverdue) o;

            return serviceId == c.serviceId
                    && serviceName.equals(c.serviceName)
                    && expireDate == c.expireDate
                    && remainDays == c.remainDays;

        }
        return false;
    }
}
