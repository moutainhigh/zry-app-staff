package com.zhongmei.bty.basemodule.shopmanager.shoplogo;


import com.zhongmei.yunfu.ShopInfoManager;

/**
 * Created by demo on 2018/12/15
 */
public class ShopLogReq {
    private Long brandIdenty;
    private Long shopIdenty;

    public ShopLogReq() {
        this.shopIdenty = ShopInfoManager.getInstance().getShopInfo().getShopId();
        this.brandIdenty = ShopInfoManager.getInstance().getShopInfo().getBrandId();
    }


    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIndenty() {
        return shopIdenty;
    }

    public void setShopIndenty(Long shopIndenty) {
        this.shopIdenty = shopIndenty;
    }
}
