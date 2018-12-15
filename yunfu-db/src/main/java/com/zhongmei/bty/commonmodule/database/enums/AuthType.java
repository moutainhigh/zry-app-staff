package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;

/**
 * Created by demo on 2018/12/15
 */

public enum AuthType implements ValueEnum<Integer> {
    // 开台
    TYPE_START_DESK(1, R.string.commonmodule_dinner_opentable),//"开台"

    // 收银
    TYPE_CHECK_OUT(2, R.string.commonmodule_dinner_cashier),//"收银"

    // "接受/拒绝"
    TYPE_ACCEPT_REJECT(3, R.string.commonmodule_dinner_accept_or_refuse),
    // "作废"
    TYPE_CANCEL_ORDER(4, R.string.commonmodule_dinner_abolish),

    // "反结账"
    TYPE_ANIT_SETTLEMENT(5, R.string.commonmodule_dinner_order_center_repay),

    // "拆单"
    TYPE_SPLIT(6, R.string.commonmodule_dinner_devide_trade),
    //"退货"
    TYPE_RETURN(7, R.string.commonmodule_dinner_return_product),
    //转台
    TYPE_TRANSFER_DESK(8, R.string.commonmodule_dinner_change_table),
    //"合单"
    TYPE_MERGE_TRADE(9, R.string.commonmodule_dinner_mix_trade),
    //"复制/移菜"
    TYPE_COPY_MOVE_DISH(10, R.string.commonmodule_dinner_copy_or_move_dish),

    //"折扣上限"
    TYPE_PRIVILEGE_DISCOUNT(11, R.string.commonmodule_dinner_max_discount),
    //    "折让上限"
    TYPE_PRIVILEGE_REBETE(12, R.string.commonmodule_dinner_max_allowance),
    //    "免单"
    TYPE_PRIVILIGE_FREE(13, R.string.commonmodule_dinner_free_food),
    //    "赠送"
    TYPE_PRIVILIGE_PRESENT(14, R.string.commonmodule_dinner_present),
    //    "宴请"
    TYPE_PRIVILIGE_BANQUET(15, R.string.commonmodule_dinner_fete),
    //    "撤销银联pos刷卡"
    TYPE_NOTIFY_POS_RETRY(16, R.string.commonmodule_dinner_revocation_pos),
    //"挂账"
    TYPE_DINNER_CREDIT(17, R.string.commonmodule_dinner_on_account),
    //"正餐交接"
    TYPE_DINNER_HANDOVER(18, R.string.commonmodule_dinner_exchange),
    //"正餐历史交接"
    TYPE_DINNER_HANDOVER_LAST(19, R.string.commonmodule_dinner_hostory_exchange),
    //"关账"
    TYPE_CLOSING(20, R.string.commonmodule_dinner_closing),
    //"退菜"
    TYPE_RETURN_GOODS(21, R.string.commonmodule_dinner_return_dish),
    //"收支编辑"
    TYPE_PAYMENTS_EDIT(22, R.string.commonmodule_dinner_income_outgoing_edit),
    //"抹零"
    TYPE_MALING(23, R.string.commonmodule_dinner_clear_odd),
    //"快餐交接"
    TYPE_FAST_HANDOVER(24, R.string.commonmodule_dinner_quickserver_relay),
    //"快餐历史交接"
    TYPE_FAST_HANDOVER_LAST(25, R.string.commonmodule_dinner_quickserver_relay_hostory),
    //"优惠"
    TYPE_PRIVILEGE(26, R.string.commonmodule_dinner_privilege),
    //"快餐下单"
    TYPE_FASTFOOD_CREATE(27, R.string.commonmodule_dinner_quickserver_order),
    //"关账历史"
    TYPE_CLOSING_HISTORY(28, R.string.commonmodule_dinner_history_closebill),
    //"报表中心"
    TYPE_REPORT_FORM(29, R.string.commonmodule_dinner_form_center),
    //"清账"
    TYPE_CLEAR_BALANCE(30, R.string.commonmodule_dinner_clean_account),
    //"预结单打印设置"
    TYPE_PREPRINT_SETTING(31, R.string.commonmodule_dinner_prebill_print_setting),
    //修改服务员"
    TYPE_MODIFY_WAITER(32, R.string.commonmodule_dinner_update_waiter),
    //"开票"
    TYPE_INVOICE_QRCODE(33, R.string.commonmodule_dinner_invoice),
    //"冲红"
    TYPE_INVOICE_REVOKE(34, R.string.commonmodule_dinner_wash_invoice_record),
    //快餐重新校准
    TYPE_FAST_HANDOVER_CALIBRATE(35, R.string.commonmodule_snack_re_calibrate),
    //正餐重新校准
    TYPE_DINNER_HANDOVER_CALIBRATE(36, R.string.commonmodule_dinner_re_calibrate),

    //正餐称重商品修改
    TYPE_DINNER_EDIT_WEIGHT(37, R.string.commonmodule_quantity_weight_edit),

    //正餐顾客登录
    TYPE_DINNER_CUSTOMER_LOGIN(38, R.string.commonmodule_dinner_customer_login_by_number),

    //顾客会员积分修改
    TYPE_MEMBER_INTEGRAL_MODIFY(39, R.string.commonmodule_customer_member_Integral_modify),

    //正餐删菜
    TYPE_DINNER_DELETE_DISH(40, R.string.commonmodule_dinner_delete_dish),

    //正餐获取实时概况
    TYPE_DINNER_BUSINESS_CHARGE(41, R.string.commonmodule_dinner_business_charge),

    //正餐改价
    TYPE_DINNER_MODIFY_PRICE(42, R.string.commonmodule_dinner_modifyprice),
    /**
     * 未知的值
     *
     * @deprecated 为了避免转为enum出错而设置，不应直接使用
     */
    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;
    //类型描述
    private int typeDescResId;

    private AuthType(Integer value, int typeDescResId) {
        helper = Helper.valueHelper(value);
        this.typeDescResId = typeDescResId;
    }

    private AuthType() {
        helper = Helper.unknownHelper();
    }

    @Override
    public Integer value() {
        return helper.value();
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
    public boolean equalsValue(Integer value) {
        return helper.equalsValue(this, value);
    }

    @Override
    public String toString() {
        return "" + value();
    }

    public String getDesc() {
        if (typeDescResId > 0) {
            return BaseApplication.sInstance.getString(typeDescResId);
        } else {
            return BaseApplication.sInstance.getString(R.string.commonmodule_dialog_other);
        }
    }

}
