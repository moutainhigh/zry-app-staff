package com.zhongmei.beauty.booking.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class BeautyBookingEnum {

    public static final int UNKNOWN = -999;
    public static final String LAUNCHMODE_BOOKING_DIALOG = "booking_dialog_launchmode";
        public static final String DATA_BOOKING_DIALOG = "booking_dialog_data";

    @IntDef({BookingDialogLaunchMode.CREATE, BookingDialogLaunchMode.EDIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BookingDialogLaunchMode {
        int CREATE = 1;         int EDIT = 2;    }
}
