package com.zhongmei.beauty.booking.bean;

import com.zhongmei.beauty.entity.ReserverItemVo;
import com.zhongmei.beauty.entity.BookingTradeItemUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class BeautyBookingBoardVo {

    private List<BookingTradeItemUser> mTechnicians;    private Map<Long, ArrayList<ReserverItemVo>> mMapBookingItemVos;    private List<BeautyBookingVo> mNoTechnicianBookingItemVos;
    private List<BeautyBookingVo> mBookingItemVos;
    public List<BookingTradeItemUser> getmTechnicians() {
        return mTechnicians;
    }

    public void setmTechnicians(List<BookingTradeItemUser> mTechnicians) {
        this.mTechnicians = mTechnicians;
    }

    public Map<Long, ArrayList<ReserverItemVo>> getmMapBookingItemVos() {
        return mMapBookingItemVos;
    }

    public void setmMapBookingItemVos(Map<Long, ArrayList<ReserverItemVo>> mMapBookingItemVos) {
        this.mMapBookingItemVos = mMapBookingItemVos;
    }

    public List<BeautyBookingVo> getmNoTechnicianBookingItemVos() {
        return mNoTechnicianBookingItemVos;
    }

    public void setmNoTechnicianBookingItemVos(List<BeautyBookingVo> mNoTechnicianBookingItemVos) {
        this.mNoTechnicianBookingItemVos = mNoTechnicianBookingItemVos;
    }

    public List<BeautyBookingVo> getmBookingItemVos() {
        return mBookingItemVos;
    }

    public void setmBookingItemVos(List<BeautyBookingVo> mBookingItemVos) {
        this.mBookingItemVos = mBookingItemVos;
    }
}
