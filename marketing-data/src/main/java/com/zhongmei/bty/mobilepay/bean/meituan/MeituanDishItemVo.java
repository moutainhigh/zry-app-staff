package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class MeituanDishItemVo {
    //匹配到的菜品
    public List<MeituanDishItem> meituanItemVoList;
    //    抵扣金额
    BigDecimal debAmount;

    public List<MeituanDishItem> getMeituanItemVoList() {
        return meituanItemVoList;
    }

    public void setMeituanItemVoList(List<MeituanDishItem> meituanItemVoList) {
        this.meituanItemVoList = meituanItemVoList;
    }

    public BigDecimal getDebAmount() {
        return debAmount;
    }

    public void setDebAmount(BigDecimal debAmount) {
        this.debAmount = debAmount;
    }


}
