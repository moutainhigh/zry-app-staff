package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.db.ICreator;


public class ModifyCashierPointReq {


    private static final long serialVersionUID = 1L;

    private Long printerDeviceId;
    private String printerDeviceIp;
    private Long id;
    private Long brandIdenty;
    private Long shopIdenty;
    private Long updatorId;
    private String updatorName;


    public Long getPrinterDeviceId() {
        return printerDeviceId;
    }

    public void setPrinterDeviceId(Long printerDeviceId) {
        this.printerDeviceId = printerDeviceId;
    }

    public String getPrinterDeviceIp() {
        return printerDeviceIp;
    }

    public void setPrinterDeviceIp(String printerDeviceIp) {
        this.printerDeviceIp = printerDeviceIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void validateCreateRequestData() {
        setBrandIdenty(BaseApplication.getInstance().getBrandIdenty());
        setShopIdenty(BaseApplication.getInstance().getShopIdenty());
        if (this instanceof ICreator) {
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                setUpdatorId(user.getId());
                setUpdatorName(user.getName());
            }
        }
    }
}

