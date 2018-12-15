package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteDetail;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteNorms;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class DishMenuReq {
    private Long id;
    private Long brandIdenty;
    private Long shopIdenty;
    private String carteCode;
    private String name;
    private BigDecimal price;
    private int carteType;
    private int isPublic;
    private Long userId;
    private List<DishCarteNorms> norms;
    private List<DishCarteDetail> details;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCarteCode() {
        return carteCode;
    }

    public void setCarteCode(String carteCode) {
        this.carteCode = carteCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCarteType() {
        return carteType;
    }

    public void setCarteType(int carteType) {
        this.carteType = carteType;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public List<DishCarteNorms> getNorms() {
        return norms;
    }

    public void setNorms(List<DishCarteNorms> norms) {
        this.norms = norms;
    }

    public List<DishCarteDetail> getDetails() {
        return details;
    }

    public void setDetails(List<DishCarteDetail> details) {
        this.details = details;
    }
}
