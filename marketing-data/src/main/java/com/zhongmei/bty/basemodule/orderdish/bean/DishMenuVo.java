package com.zhongmei.bty.basemodule.orderdish.bean;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.orderdish.entity.DishCarte;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteDetail;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteNorms;
import com.zhongmei.yunfu.context.util.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class DishMenuVo implements Serializable {

    //团餐名
    private String name;
    //    菜品数量
    private String count;
    //餐标
    private BigDecimal price;
    //简码
    private String code;
    //是否被选中
    private boolean isSelected = false;

    //菜单名
    private String skuName;
    //菜单uuid
    private String skuUuid;
    //菜单id
    private Long skuId;
    //菜单模板
    private DishCarte dishCarte;
    //模板下的菜品
    private List<DishCarteDetail> carteDetailList;

    private List<DishCarteNorms> carteNormsList;

    /**
     * 是否是通用模版
     */
    private boolean isDefault;

    /**
     * 是否切换模版,模版是否发生改变
     */
    private boolean isChange;

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getSkuName() {
        if (!TextUtils.isEmpty(skuName)) {
            return skuName;
        }
        if (dishCarte == null) {
            return "";
        }
        return dishCarte.getName();
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public void setSkuUuid(String skuUuid) {
        this.skuUuid = skuUuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public String getCount() {
        if (Utils.isEmpty(carteDetailList)) {
            return "0";
        }
        BigDecimal count = BigDecimal.ZERO;
        for (DishCarteDetail detail : carteDetailList) {
            //子菜不统计数量
            if (detail.getMealId() == null || detail.getMealId() == 0) {
                count = count.add(detail.getNum());
            }
        }
        return count.toString();
    }

    public void setCount(String count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDisCartPrice() {
        if (dishCarte == null) {
            return BigDecimal.ZERO;
        }
        return dishCarte.getPrice();
    }

    public Long getSkuId() {
        if (dishCarte != null) {
            return dishCarte.getId();
        }
        return skuId;
    }

    public String getSkuUuid() {
        if (dishCarte != null) {
            return dishCarte.getUuid();
        }
        return skuUuid;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCode() {
        if (!TextUtils.isEmpty(code)) {
            return code;
        }
        if (dishCarte == null) {
            return "";
        }
        return dishCarte.getCarteCode() == null ? "" : dishCarte.getCarteCode();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public DishCarte getDishCarte() {
        return dishCarte;
    }

    public void setDishCarte(DishCarte dishCarte) {
        this.dishCarte = dishCarte;
    }

    public List<DishCarteDetail> getCarteDetailList() {
        return carteDetailList;
    }

    public void setCarteDetailList(List<DishCarteDetail> carteDetailList) {
        this.carteDetailList = carteDetailList;
    }

    public List<DishCarteNorms> getCarteNormsList() {
        return carteNormsList;
    }

    public void setCarteNormsList(List<DishCarteNorms> carteNormsList) {
        this.carteNormsList = carteNormsList;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }
}
