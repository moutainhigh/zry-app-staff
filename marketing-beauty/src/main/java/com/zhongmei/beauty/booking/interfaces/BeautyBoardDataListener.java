package com.zhongmei.beauty.booking.interfaces;

import com.zhongmei.beauty.booking.bean.BeautyBookingBoardVo;

/**
 * Created by demo on 2018/12/15
 */

public interface BeautyBoardDataListener {
    void onSuccess(BeautyBookingBoardVo boardVo);

    void onFail(String error);
}
