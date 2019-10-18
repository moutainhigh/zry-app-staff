package com.zhongmei.bty.commonmodule.database.enums;

import android.content.res.Resources;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;


public enum PrintStatesEnum implements ValueEnum<Integer> {

        PRINT_NORMAL(0),
    PRINT_UNKNOWN_ERROR(-99),
        PRINT_GLOBAL_DATA_ERROR(-1),
    PRINT_GLOBAL_TEMPLET_ERROR(-2),
    PRINT_GLOBAL_NOGOODS_ERROR(-3),
    PRINT_GLOBAL_NOTICKET_CHOOSE_ERROR(-4),
    PRINT_GLOBAL_NODEVICE_CHOOSE_ERROR(-5),
    PRINT_GLOBAL_NODELIVERYTYPE_CHOOSE_ERROR(-6),
    PRINT_GLOBAL_PART_SUCC_PART_FAIL(-7),
    PRINT_GLOBAL_ALL_SUCC(-8),
    PRINT_GLOBAL_ALL_FAIL(-9),
        PRINT_PART_NO_DEVICE(-10),
    PRINT_PART_NO_AREA(-11),
    PRINT_PART_NO_GOODS_CHOOSE(-12),
    PRINT_PART_PACKAGE_ERROR(-13),
    PRINT_PART_SEND_SUCCESS(-14),
    PRINT_PART_SEND_FAILED(-15);

    private Integer code;     public static Resources mResources = BaseApplication.sInstance.getResources();

    PrintStatesEnum(Integer code) {
        helper = Helper.valueHelper(code);
        this.code = code;
    }

    public Integer code() {
        return this.code;
    }

    public String valueOf(int resId) {
        return mResources.getString(resId);
    }

    public String message() {
        switch (this) {
            case PRINT_NORMAL:
                return valueOf(R.string.commonmodule_print_callback_normal);
            case PRINT_UNKNOWN_ERROR:
                return valueOf(R.string.commonmodule_print_callback_unknown_error);
            case PRINT_GLOBAL_DATA_ERROR:
                return valueOf(R.string.commonmodule_print_callback_data_error);
            case PRINT_GLOBAL_TEMPLET_ERROR:
                return valueOf(R.string.commonmodule_print_callback_templet_error);
            case PRINT_GLOBAL_NOGOODS_ERROR:
                return valueOf(R.string.commonmodule_print_callback_no_valid_goods);
            case PRINT_GLOBAL_NOTICKET_CHOOSE_ERROR:
                return valueOf(R.string.commonmodule_print_callback_no_ticket_type);
            case PRINT_GLOBAL_NODEVICE_CHOOSE_ERROR:
                return valueOf(R.string.commonmodule_print_callback_no_choose_device);
            case PRINT_GLOBAL_NODELIVERYTYPE_CHOOSE_ERROR:
                return valueOf(R.string.commonmodule_print_callback_no_choose_delivery_type);
            case PRINT_GLOBAL_PART_SUCC_PART_FAIL:
                return valueOf(R.string.commonmodule_print_callback_part_success);
            case PRINT_GLOBAL_ALL_SUCC:
                return valueOf(R.string.commonmodule_print_callback_all_success);
            case PRINT_GLOBAL_ALL_FAIL:
                return valueOf(R.string.commonmodule_print_callback_all_failed);
            case PRINT_PART_NO_DEVICE:
                return valueOf(R.string.commonmodule_print_callback_config_error);
            case PRINT_PART_NO_AREA:
                return valueOf(R.string.commonmodule_print_callback_no_choose_area);
            case PRINT_PART_NO_GOODS_CHOOSE:
                return valueOf(R.string.commonmodule_print_callback_no_choose_goods);
            case PRINT_PART_PACKAGE_ERROR:
                return valueOf(R.string.commonmodule_print_callback_package_error);
            case PRINT_PART_SEND_SUCCESS:
                return valueOf(R.string.commonmodule_print_callback_send_success);
            case PRINT_PART_SEND_FAILED:
                return valueOf(R.string.commonmodule_print_callback_send_error);
        }
        return "";
    }

    private final Helper<Integer> helper;

    @Override
    public Integer value() {
        return helper.value();
    }

    @Override
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public boolean isUnknownEnum() {
        return helper.isUnknownEnum();
    }

    @Override
    public void setUnknownValue(Integer value) {
        helper.setUnknownValue(value);
    }

    @Override
    public String toString() {
        return "" + value() + message();
    }



}