package com.zhongmei.bty.data.operates.message.content;

/**
 * @Date： 2017/2/28
 * @Description:库存请求对象
 * @Version: 1.0
 */
public class InventoryReq {

    private Long brandIdenty;

    private Long shopIdenty;

    private String queryDate;

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

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }
}
