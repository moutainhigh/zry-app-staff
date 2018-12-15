package com.zhongmei.bty.entity.event.orderdishevent;


import java.util.List;

/**
 * 移除优惠券刷新
 */
public class EventShopcatDelCoupon {

    /**
     * 需要移除券的Id
     */
    public List<Long> mIds;

    public Long id;


    /**
     * @param ids 需要移除券的id
     */
    public EventShopcatDelCoupon(List<Long> ids) {
        this.mIds = ids;
    }
}
