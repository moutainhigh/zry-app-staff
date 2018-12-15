package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarte;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteDetail;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteNorms;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class DishMenuResp {
    public Long id;
    public Long brandIdenty;
    public Long shopIdenty;
    public String uuid;
    public String carteCode;
    public String name;
    public BigDecimal price;
    public Integer carteType;
    public Integer isPublic;
    public Integer source;
    public Integer isDisable;
    public Integer statusFlag;
    public Long createId;
    public Long updateId;
    public Long userId;
    public Long serverCreateTime;
    public Long serverUpdateTime;
    public List<DishCarteNorms> norms;
    public List<DishCarteDetail> details;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Integer getCarteType() {
        return carteType;
    }

    public void setCarteType(Integer carteType) {
        this.carteType = carteType;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public Integer getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Integer statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public DishCarte getDishCarte() {
        DishCarte dishCarte = new DishCarte();
        dishCarte.setId(getId());
        dishCarte.setSource(getSource());
        dishCarte.setStatusFlag(ValueEnums.toEnum(StatusFlag.class, statusFlag));
        dishCarte.setShopIdenty(getShopIdenty());
        dishCarte.setCarteType(getCarteType());
        dishCarte.setIsDisable(getIsDisable());
        dishCarte.setCarteCode(getCarteCode());
        dishCarte.setIsPublic(getIsPublic());
        dishCarte.setCreateId(getCreateId());
        dishCarte.setUpdateId(getUpdateId());
        dishCarte.setName(getName());
        dishCarte.setPrice(getPrice());
        dishCarte.setUuid(getUuid());
        dishCarte.setBrandIdenty(getBrandIdenty());
        dishCarte.setServerCreateTime(getServerCreateTime());
        dishCarte.setServerUpdateTime(getServerUpdateTime());
        return dishCarte;
    }
}
