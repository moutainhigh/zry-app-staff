package com.zhongmei.bty.basemodule.autoset.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.bty.basemodule.commonbusiness.enums.BusinessId;

/**
 * Created by demo on 2018/12/15
 */
public class AutoSetReq {

    private Integer modeId;

    private Integer businessId;

    private Long brandIdenty;

    private Long shopIdenty;

    private String printerDeviceIP;//打印机ip

    private String printerServiceIP;//打印服务ip

    private String deviceMac;//pos的mac地址

    public BusinessType getBusinessType() {
        return ValueEnums.toEnum(BusinessType.class, modeId);
    }

    public void setBusinessType(BusinessType businessType) {
        this.modeId = ValueEnums.toValue(businessType);
    }

    public BusinessId getBusinessId() {
        return ValueEnums.toEnum(BusinessId.class, businessId);
    }

    public void setBusinessId(BusinessId businessId) {
        this.businessId = ValueEnums.toValue(businessId);
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

    public String getPrinterDeviceIP() {
        return printerDeviceIP;
    }

    public void setPrinterDeviceIP(String printerDeviceIP) {
        this.printerDeviceIP = printerDeviceIP;
    }

    public String getPrinterServiceIP() {
        return printerServiceIP;
    }

    public void setPrinterServiceIP(String printerServiceIP) {
        this.printerServiceIP = printerServiceIP;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
}
