package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class MeituanDishVo {
    public List<MeituanDishItemVo> matchDishItemVoList;
    public BigDecimal matchAmount = BigDecimal.ZERO;    public int usedCount;    public int matchCount = 0;    public boolean isMatchEnable = false;
    public MeituanDishVo(int usedCount) {
        this.usedCount = usedCount;
    }

    public List<ICouponDishRelate> getCouponDishRelates() {

        if (matchDishItemVoList != null && matchDishItemVoList.size() > 0) {
            List<ICouponDishRelate> ls = new ArrayList<ICouponDishRelate>();
            for (MeituanDishItemVo meituanDishItemVo : matchDishItemVoList) {
                ls.addAll(meituanDishItemVo.getMeituanItemVoList());
            }
            return ls;
        }
        return null;
    }
}
