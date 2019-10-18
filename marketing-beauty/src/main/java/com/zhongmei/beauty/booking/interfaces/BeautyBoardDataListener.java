package com.zhongmei.beauty.booking.interfaces;

import com.zhongmei.beauty.booking.bean.BeautyBookingBoardVo;



public interface BeautyBoardDataListener {
    void onSuccess(BeautyBookingBoardVo boardVo);

    void onFail(String error);
}
