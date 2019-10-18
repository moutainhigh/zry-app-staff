package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTemplet;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTempletDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketTempletVo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private MemberPriceTemplet templet;

    private Map<Long, MemberPriceTempletDetail> memberDishMap;

    public MarketTempletVo(MemberPriceTemplet temp, List<MemberPriceTempletDetail> list) {
        this.templet = temp;
        if (list != null && !list.isEmpty()) {
            memberDishMap = new HashMap<Long, MemberPriceTempletDetail>();
            for (MemberPriceTempletDetail dish : list) {
                memberDishMap.put(dish.getBrandDishId(), dish);
            }
        }
    }

    public MemberPriceTemplet getTemplet() {
        return templet;
    }

        public boolean isContainsDish(Long brandDishId) {
        if (memberDishMap != null) {
            return memberDishMap.containsKey(brandDishId);
        } else {
            return false;
        }

    }

    public void setTemplet(MemberPriceTemplet templet) {
        this.templet = templet;
    }

    public List<MemberPriceTempletDetail> getMemberDishlist() {
        List<MemberPriceTempletDetail> memberDishlist = null;
        if (memberDishMap != null && !memberDishMap.isEmpty()) {
            memberDishlist = new ArrayList<MemberPriceTempletDetail>();
            memberDishlist.addAll(memberDishMap.values());
        }
        return memberDishlist;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((templet == null) ? 0 : templet.getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MarketTempletVo other = (MarketTempletVo) obj;
        if (templet == null) {
            if (other.templet != null)
                return false;
        } else if (!templet.equals(other.templet))
            return false;
        return true;
    }
}
