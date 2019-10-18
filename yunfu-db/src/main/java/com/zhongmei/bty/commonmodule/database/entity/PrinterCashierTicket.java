package com.zhongmei.bty.commonmodule.database.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;


@DatabaseTable(tableName = "printer_cashier_ticket")
public class PrinterCashierTicket extends UuidEntityBase implements ICreator, IUpdator {

    public interface $ extends UuidEntityBase.$ {
        public static final String statusFlag = "status_flag";
        public static final String brandIdenty = "brand_identy";
        public static final String id = "id";
        public static final String serverCreateTime = "server_create_time";
        public static final String serverUpdateTime = "server_update_time";
        public static final String shopIdenty = "shop_identy";
        public static final String deviceIdenty = "device_identy";
        public static final String updatorName = "updator_name";
        public static final String creatorName = "creator_name";
        public static final String creatorId = "creator_id";
        public static final String updatorId = "updator_id";
        public static final String ticketType = "ticket_type";
        public static final String name = "name";
        public static final String printerDeviceId = "printer_device_id";
        public static final String isGoods = "is_goods";
        public static final String isTable = "is_table";
        public static final String initFlag = "init_flag";
    }

    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;

    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    @DatabaseField(columnName = "id")
    private Long id;

    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "ticket_type")
    private Integer ticketType;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "printer_device_id")
    private Long printerDeviceId;

    @DatabaseField(columnName = "is_goods")
    private Integer isGoods;

    @DatabaseField(columnName = "is_table")
    private Integer isTable;

    @DatabaseField(columnName = "init_flag")
    private Integer initFlag;


    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Integer getInitFlag() {
        return initFlag;
    }

    public void setInitFlag(Integer initFlag) {
        this.initFlag = initFlag;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public Long getUpdatorId() {
        return updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    @Override
    public Long getCreatorId() {
        return creatorId;
    }

    @Override
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrinterDeviceId() {
        return printerDeviceId;
    }

    public void setPrinterDeviceId(Long printerDeviceId) {
        this.printerDeviceId = printerDeviceId;
    }

    public StatusFlag getIsGoods() {
        return ValueEnums.toEnum(StatusFlag.class, isGoods);
    }

    public void setIsGoods(Integer isGoods) {
        this.isGoods = isGoods;
    }

    public StatusFlag getIsTable() {
        return ValueEnums.toEnum(StatusFlag.class, isTable);
    }

    public void setIsTable(Integer isTable) {
        this.isTable = isTable;
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    @Override
    public void validateCreate() {
        super.validateCreate();
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(ShopInfoCfg.getInstance().getShopInfo().getBrandIdLong());
        setShopIdenty(ShopInfoCfg.getInstance().getShopInfo().getShopIdLong());
        setDeviceIdenty(ShopInfoCfg.getInstance().getShopInfo().getDeviceID());
    }

    @Override
    public void validateUpdate() {
        setChanged(true);
                        if (this instanceof ICreator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                ICreator creator = this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag, brandIdenty, shopIdenty);
    }
}
