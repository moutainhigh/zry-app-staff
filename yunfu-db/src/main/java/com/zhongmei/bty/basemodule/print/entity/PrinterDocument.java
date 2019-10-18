package com.zhongmei.bty.basemodule.print.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.bty.basemodule.print.enums.DocumentCategory;
import com.zhongmei.bty.basemodule.print.enums.IsPrint;
import com.zhongmei.bty.basemodule.print.enums.IsPrintPrice;
import com.zhongmei.bty.basemodule.print.enums.IsPrintSubmenu;
import com.zhongmei.bty.basemodule.print.enums.Scope;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.bty.basemodule.print.enums.CombinePolicy;
import com.zhongmei.bty.basemodule.print.enums.PrintPolicy;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;


@DatabaseTable(tableName = "printer_document")
public class PrinterDocument extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String deliveryType = "delivery_type";


        public static final String deviceId = "device_id";


        public static final String documentCategory = "document_category";


        public static final String isPrintSubmenu = "is_print_submenu";


        public static final String isPrintPrice = "is_print_price";


        public static final String name = "name";


        public static final String printCount = "print_count";


        public static final String printPolicy = "print_policy";


        public static final String combinePolicy = "combine_policy";


        public static final String scope = "scope";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String isShowTableArea = "isShowTableArea";


        public static final String isShowDishList = "isShowDishList";

        public static final String isShowMark = "isShowMark";

        public static final String isShowOrderTime = "isShowOrderTime";

        public static final String isShowShopPhone = "isShowShopPhone";

        public static final String isShowShopAddress = "isShowShopAddress";

        public static final String isShowPageNumber = "is_show_num";

        public static final String deviceIds = "deviceIds";

    }

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "delivery_type")
    private Integer deliveryType;

    @DatabaseField(columnName = "device_id")
    private Long deviceId;

    @DatabaseField(columnName = "document_category")
    private Integer documentCategory;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "is_print_submenu")
    private Integer isPrintSubmenu;

    @DatabaseField(columnName = "is_print_price")
    private Integer isPrintPrice;

    @DatabaseField(columnName = "print_count")
    private Integer printCount;

    @DatabaseField(columnName = "print_policy")
    private Integer printPolicy;

    @DatabaseField(columnName = "combine_policy")
    private Integer combinePolicy;

    @DatabaseField(columnName = "scope")
    private Integer scope;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "isShowTableArea")
    private Integer isShowTableArea;

    @DatabaseField(columnName = "isShowDishList")
    private Integer isShowDishList;
    @DatabaseField(columnName = "isShowMark")
    private Integer isShowMark;    @DatabaseField(columnName = "isShowOrderTime")
    private Integer isShowOrderTime;    @DatabaseField(columnName = "isShowShopPhone")
    private Integer isShowShopPhone;    @DatabaseField(columnName = "isShowShopAddress")
    private Integer isShowShopAddress;    @DatabaseField(columnName = "is_show_num")
    private Integer isShowNum;    @DatabaseField(columnName = "deviceIds")
    private String deviceIds;
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

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public StatusFlag getIsShowTableArea() {
        return ValueEnums.toEnum(StatusFlag.class, isShowTableArea);
    }

    public void setIsShowTableArea(StatusFlag isShowTableArea) {
        this.isShowTableArea = ValueEnums.toValue(isShowTableArea);
    }

    public StatusFlag getIsShowDishList() {
        return ValueEnums.toEnum(StatusFlag.class, isShowDishList);
    }

    public void setIsShowDishList(StatusFlag isShowDishList) {
        this.isShowDishList = ValueEnums.toValue(isShowDishList);
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public DocumentCategory getDocumentCategory() {
        return ValueEnums.toEnum(DocumentCategory.class, documentCategory);
    }

    public void setDocumentCategory(DocumentCategory documentCategory) {
        this.documentCategory = ValueEnums.toValue(documentCategory);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IsPrintSubmenu getIsPrintSubmenu() {
        return ValueEnums.toEnum(IsPrintSubmenu.class, isPrintSubmenu);
    }

    public void setIsPrintSubmenu(IsPrintSubmenu isPrintSubmenu) {
        this.isPrintSubmenu = ValueEnums.toValue(isPrintSubmenu);
    }

    public IsPrintPrice getIsPrintPrice() {
        return ValueEnums.toEnum(IsPrintPrice.class, isPrintPrice);
    }

    public void setIsPrintPrice(IsPrintPrice isPrintPrice) {
        this.isPrintPrice = ValueEnums.toValue(isPrintPrice);
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public PrintPolicy getPrintPolicy() {
        return ValueEnums.toEnum(PrintPolicy.class, printPolicy);
    }

    public void setPrintPolicy(PrintPolicy printPolicy) {
        this.printPolicy = ValueEnums.toValue(printPolicy);
    }

    public CombinePolicy getCombinePolicy() {
        return ValueEnums.toEnum(CombinePolicy.class, combinePolicy);
    }

    public void setCombinePolicy(CombinePolicy combinePolicy) {
        this.combinePolicy = ValueEnums.toValue(combinePolicy);
    }

    public Scope getScope() {
        return ValueEnums.toEnum(Scope.class, scope);
    }

    public void setScope(Scope scope) {
        this.scope = ValueEnums.toValue(scope);
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

    public IsPrint getIsShowMark() {
        return ValueEnums.toEnum(IsPrint.class, isShowMark);
    }

    public void setIsShowMark(IsPrint isShowMark) {
        this.isShowMark = ValueEnums.toValue(isShowMark);
    }

    public IsPrint getIsShowOrderTime() {
        return ValueEnums.toEnum(IsPrint.class, isShowOrderTime);
    }

    public void setIsShowOrderTime(IsPrint isShowOrderTime) {
        this.isShowOrderTime = ValueEnums.toValue(isShowOrderTime);
    }

    public IsPrint getIsShowShopPhone() {
        return ValueEnums.toEnum(IsPrint.class, isShowShopPhone);
    }

    public void setIsShowShopPhone(IsPrint isShowShopPhone) {
        this.isShowShopPhone = ValueEnums.toValue(isShowShopPhone);
    }

    public IsPrint getIsShowShopAddress() {
        return ValueEnums.toEnum(IsPrint.class, isShowShopAddress);
    }

    public void setIsShowShopAddress(IsPrint isShowShopAddress) {
        this.isShowShopAddress = ValueEnums.toValue(isShowShopAddress);
    }

    public IsPrint getIsShowPageNumber() {
        return ValueEnums.toEnum(IsPrint.class, isShowNum);
    }

    public void setIsShowPageNumber(IsPrint isShowPageNumber) {
        this.isShowNum = ValueEnums.toValue(isShowPageNumber);
    }

    public String getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }
}

