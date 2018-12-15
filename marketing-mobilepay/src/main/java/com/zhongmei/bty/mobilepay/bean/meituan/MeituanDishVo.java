package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class MeituanDishVo {
    public List<MeituanDishItemVo> matchDishItemVoList;
    public BigDecimal matchAmount = BigDecimal.ZERO;//  合计抵扣金额
    public int usedCount;//输入使用张数
    public int matchCount = 0;//匹配张数
    public boolean isMatchEnable = false;//是否满足使用条件(默认不满足 false)

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
