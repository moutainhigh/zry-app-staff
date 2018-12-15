package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

/**
 * 删除出票口设置的请求数据
 */
public class DeleteTicketOutletSettingReq implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<Long> ids;

    private Long brandIdenty;

    private Long shopIdenty;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
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
}

