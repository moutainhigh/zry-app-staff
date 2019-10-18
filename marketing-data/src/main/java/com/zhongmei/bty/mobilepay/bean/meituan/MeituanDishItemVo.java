package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;
import java.util.List;



public class MeituanDishItemVo {
        public List<MeituanDishItem> meituanItemVoList;
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
