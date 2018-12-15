package com.zhongmei.bty.commonmodule.database.enums;

import android.content.res.Resources;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * 维护打印回调的code和对应的message
 */
public enum PrintStatesEnum implements ValueEnum<Integer> {

    // 通用code
    PRINT_NORMAL(0),
    PRINT_UNKNOWN_ERROR(-99),
    // 整体code
    PRINT_GLOBAL_DATA_ERROR(-1),
    PRINT_GLOBAL_TEMPLET_ERROR(-2),
    PRINT_GLOBAL_NOGOODS_ERROR(-3),
    PRINT_GLOBAL_NOTICKET_CHOOSE_ERROR(-4),
    PRINT_GLOBAL_NODEVICE_CHOOSE_ERROR(-5),
    PRINT_GLOBAL_NODELIVERYTYPE_CHOOSE_ERROR(-6),
    PRINT_GLOBAL_PART_SUCC_PART_FAIL(-7),
    PRINT_GLOBAL_ALL_SUCC(-8),
    PRINT_GLOBAL_ALL_FAIL(-9),
    // 局部code
    PRINT_PART_NO_DEVICE(-10),
    PRINT_PART_NO_AREA(-11),
    PRINT_PART_NO_GOODS_CHOOSE(-12),
    PRINT_PART_PACKAGE_ERROR(-13),
    PRINT_PART_SEND_SUCCESS(-14),
    PRINT_PART_SEND_FAILED(-15);

    private Integer code; //回调code
    public static Resources mResources = BaseApplication.sInstance.getResources();

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

    /**
     public static final int PRINT_NORMAL = 0; //全局或者局部正常的Code(默认情况)
     public static final int PRINT_UNKNOWN_ERROR = -99; // 未知的异常,难以控制的异常,局部和全局都可能会有该异常(例如catch中未知的错误)

     public static final int PRINT_GLOBAL_DATA_ERROR = -1; //打印数据异常
     public static final int PRINT_GLOBAL_TEMPLET_ERROR = -2; //获取自定义模板失败
     public static final int PRINT_GLOBAL_NOGOODS_ERROR = -3; //没有品项不能出单
     public static final int PRINT_GLOBAL_NOTICKET_CHOOSE_ERROR = -4; //没有选择票据类型的收银点或者出票口(针对全部收银点出票口都没选择打印的票据)
     public static final int PRINT_GLOBAL_NODEVICE_CHOOSE_ERROR = -5; //没有勾选设备的收银点(针对全部收银点都没有选择该设备)
     public static final int PRINT_GLOBAL_NODELIVERYTYPE_CHOOSE_ERROR = -6; //没有勾选用餐类型的(针对全部收银点或者标签都没有选择该用餐类型)
     public static final int PRINT_GLOBAL_PART_SUCC_PART_FAIL = -7; //部分成功部分失败
     public static final int PRINT_GLOBAL_ALL_SUCC = -8; //全部成功
     public static final int PRINT_GLOBAL_ALL_FAIL = -9; //全部失败

     public static final int PRINT_PART_NO_DEVICE = -10; //未配置打印机
     public static final int PRINT_PART_NO_AREA = -11; //未勾选该区域
     public static final int PRINT_PART_NO_GOODS_CHOOSE = -12; //未勾选商品
     public static final int PRINT_PART_PACKAGE_ERROR = -13;//组装自定义数据异常
     public static final int PRINT_PART_SEND_SUCCESS = -14;//发送打印服务成功
     public static final int PRINT_PART_SEND_FAILED= -15;//发送打印服务失败
     *
     */

}