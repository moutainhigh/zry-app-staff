package com.zhongmei.beauty.booking.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 美业预定 enum
 * <p>
 * Created by demo on 2018/12/15
 */
public class BeautyBookingEnum {

    public static final int UNKNOWN = -999;//未知类型统一入口

    public static final String LAUNCHMODE_BOOKING_DIALOG = "booking_dialog_launchmode";
    //编辑预定数据
    public static final String DATA_BOOKING_DIALOG = "booking_dialog_data";

    @IntDef({BookingDialogLaunchMode.CREATE, BookingDialogLaunchMode.EDIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BookingDialogLaunchMode {
        int CREATE = 1; // 创建预定
        int EDIT = 2;//编辑预定
    }
}
