package com.zhongmei.bty.basemodule.shopmanager.shoplogo;

import com.zhongmei.yunfu.resp.data.MindTransferResp;

/**
 * Created by demo on 2018/12/15
 */
public class ShopLogoResp extends MindTransferResp<ShopLogoResp.Data> {

    public class Data {

        private String logo;
        private String brandIdenty;
        private String shopIdenty;


        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }


        public String getBrandIdenty() {
            return brandIdenty;
        }

        public void setBrandIdenty(String brandIdenty) {
            this.brandIdenty = brandIdenty;
        }

        public String getShopIndenty() {
            return shopIdenty;
        }

        public void setShopIndenty(String shopIdenty) {
            this.shopIdenty = shopIdenty;
        }
    }
}
