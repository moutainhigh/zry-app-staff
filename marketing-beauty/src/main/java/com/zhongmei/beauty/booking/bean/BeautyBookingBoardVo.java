package com.zhongmei.beauty.booking.bean;

import com.zhongmei.beauty.entity.ReserverItemVo;
import com.zhongmei.beauty.entity.BookingTradeItemUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyBookingBoardVo {

    private List<BookingTradeItemUser> mTechnicians;//技师列表
    private Map<Long, ArrayList<ReserverItemVo>> mMapBookingItemVos;//根据技师id，存储对应的预定信息
    private List<BeautyBookingVo> mNoTechnicianBookingItemVos;//储存没有技师的预定单列表

    private List<BeautyBookingVo> mBookingItemVos;//储存没有技师的预定单列表

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
