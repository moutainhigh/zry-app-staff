package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.print.enums.CombinePolicy;
import com.zhongmei.bty.basemodule.print.enums.PrintPolicy;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * PrinterCashierTicket is a ORMLite bean type. Corresponds to the database table "cashier_ticket_document_type"
 */
@DatabaseTable(tableName = "printer_cashier_ticket_type")
public class PrinterCashierTicketType extends UuidEntityBase implements ICreator, IUpdator {
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "printer_cashier_ticket_type"
     */
    public interface $ extends UuidEntityBase.$ {
        public static final String statusFlag = "status_flag";
        public static final String brandIdenty = "brand_identy";
        public static final String id = "id";
        public static final String serverCreateTime = "server_create_time";
        public static final String serverUpdateTime = "server_update_time";
        public static final String shopIdenty = "shop_identy";
        public static final String deviceIdenty = "device_identy";


        public static final String creatorId = "creator_id";
        public static final String creatorName = "creator_name";
        public static final String updatorId = "updator_id";
        public static final String updatorName = "updator_name";
        public static final String cashierTicketId = "cashier_ticket_id";
        public static final String ticketTypeCode = "ticket_type_code";
        public static final String documentPrintCount = "document_print_count";
        public static final String isSingleDish = "is_single_dish";
        public static final String combinePolicy = "combine_policy";
        public static final String isPrintSubMenu = "is_print_submenu";
        public static final String isPrintAllSubdish = "is_print_all_subdish";
        public static final String comboShowWay = "combo_show_way";
        public static final String deliveryType = "deliver_type";
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

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "cashier_ticket_id")
    private Long cashierTicketId;

    @DatabaseField(columnName = "ticket_type_code")
    private Long ticketTypeCode;

    @DatabaseField(columnName = "document_print_count")
    private Integer documentPrintCount;

    @DatabaseField(columnName = "is_single_dish")
    private Integer isSingleDish;

    @DatabaseField(columnName = "combine_policy")
    private Integer combinePolicy;

    @DatabaseField(columnName = "is_print_submenu")
    private Integer isPrintSubMenu;

    @DatabaseField(columnName = "deliver_type")
    private Integer deliveryType;

    @DatabaseField(columnName = "combo_show_way")
    private Integer comboShowWay;

    //一菜一纸是否同一张票据打印打印所有子菜 1:在不同的票据上打印套餐子菜（默认）;2:在同一票据上打印套餐子菜
    @DatabaseField(columnName = "is_print_all_subdish")
    private Integer isPrintAllSubdish;


    public Integer getStatusFlag() {
        return statusFlag;
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

    public Integer getIsPrintAllSubdish() {
        return isPrintAllSubdish;
    }

    public void setIsPrintAllSubdish(Integer isPrintAllSubdish) {
        this.isPrintAllSubdish = isPrintAllSubdish;
    }

    public Integer getIsPrintSubMenu() {
        return isPrintSubMenu;
    }

    public void setIsPrintSubMenu(Integer isPrintSubMenu) {
        this.isPrintSubMenu = isPrintSubMenu;
    }

    public Integer getComboShowWay() {
        return comboShowWay;
    }

    public void setComboShowWay(Integer comboShowWay) {
        this.comboShowWay = comboShowWay;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

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

    public Long getCashierTicketId() {
        return cashierTicketId;
    }

    public void setCashierTicketId(Long cashierTicketId) {
        this.cashierTicketId = cashierTicketId;
    }

    public Long getTicketTypeCode() {
        return ticketTypeCode;
    }

    public void setTicketTypeCode(Long ticketTypeCode) {
        this.ticketTypeCode = ticketTypeCode;
    }

    public Integer getDocumentPrintCount() {
        return documentPrintCount;
    }

    public void setDocumentPrintCount(Integer documentPrintCount) {
        this.documentPrintCount = documentPrintCount;
    }

    public PrintPolicy getIsSingleDish() {
        return ValueEnums.toEnum(PrintPolicy.class, isSingleDish);
    }


    public void setIsSingleDish(Integer isSingleDish) {
        this.isSingleDish = isSingleDish;
    }

    public CombinePolicy getCombinePolicy() {
        return ValueEnums.toEnum(CombinePolicy.class, combinePolicy);

    }

    public void setCombinePolicy(Integer combinePolicy) {
        this.combinePolicy = combinePolicy;
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Integer getDeliverType() {
        return deliveryType;
    }

    public void setDeliverType(Integer deliverType) {
        this.deliveryType = deliverType;
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            ICreator creator = this;
            creator.setCreatorId(user.getId());
            creator.setCreatorName(user.getName());
        }
        validateUpdate();
    }

    public void validateUpdate() {
        setChanged(true);
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            IUpdator updator = this;
            updator.setUpdatorId(user.getId());
            updator.setUpdatorName(user.getName());
        }
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag, brandIdenty, shopIdenty);
    }
}
