package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.db.R;
import com.zhongmei.yunfu.context.base.BaseApplication;



public enum AuthType implements ValueEnum<Integer> {
        TYPE_START_DESK(1, R.string.commonmodule_dinner_opentable),
        TYPE_CHECK_OUT(2, R.string.commonmodule_dinner_cashier),
        TYPE_ACCEPT_REJECT(3, R.string.commonmodule_dinner_accept_or_refuse),
        TYPE_CANCEL_ORDER(4, R.string.commonmodule_dinner_abolish),

        TYPE_ANIT_SETTLEMENT(5, R.string.commonmodule_dinner_order_center_repay),

        TYPE_SPLIT(6, R.string.commonmodule_dinner_devide_trade),
        TYPE_RETURN(7, R.string.commonmodule_dinner_return_product),
        TYPE_TRANSFER_DESK(8, R.string.commonmodule_dinner_change_table),
        TYPE_MERGE_TRADE(9, R.string.commonmodule_dinner_mix_trade),
        TYPE_COPY_MOVE_DISH(10, R.string.commonmodule_dinner_copy_or_move_dish),

        TYPE_PRIVILEGE_DISCOUNT(11, R.string.commonmodule_dinner_max_discount),
        TYPE_PRIVILEGE_REBETE(12, R.string.commonmodule_dinner_max_allowance),
        TYPE_PRIVILIGE_FREE(13, R.string.commonmodule_dinner_free_food),
        TYPE_PRIVILIGE_PRESENT(14, R.string.commonmodule_dinner_present),
        TYPE_PRIVILIGE_BANQUET(15, R.string.commonmodule_dinner_fete),
        TYPE_NOTIFY_POS_RETRY(16, R.string.commonmodule_dinner_revocation_pos),
        TYPE_DINNER_CREDIT(17, R.string.commonmodule_dinner_on_account),
        TYPE_DINNER_HANDOVER(18, R.string.commonmodule_dinner_exchange),
        TYPE_DINNER_HANDOVER_LAST(19, R.string.commonmodule_dinner_hostory_exchange),
        TYPE_CLOSING(20, R.string.commonmodule_dinner_closing),
        TYPE_RETURN_GOODS(21, R.string.commonmodule_dinner_return_dish),
        TYPE_PAYMENTS_EDIT(22, R.string.commonmodule_dinner_income_outgoing_edit),
        TYPE_MALING(23, R.string.commonmodule_dinner_clear_odd),
        TYPE_FAST_HANDOVER(24, R.string.commonmodule_dinner_quickserver_relay),
        TYPE_FAST_HANDOVER_LAST(25, R.string.commonmodule_dinner_quickserver_relay_hostory),
        TYPE_PRIVILEGE(26, R.string.commonmodule_dinner_privilege),
        TYPE_FASTFOOD_CREATE(27, R.string.commonmodule_dinner_quickserver_order),
        TYPE_CLOSING_HISTORY(28, R.string.commonmodule_dinner_history_closebill),
        TYPE_REPORT_FORM(29, R.string.commonmodule_dinner_form_center),
        TYPE_CLEAR_BALANCE(30, R.string.commonmodule_dinner_clean_account),
        TYPE_PREPRINT_SETTING(31, R.string.commonmodule_dinner_prebill_print_setting),
        TYPE_MODIFY_WAITER(32, R.string.commonmodule_dinner_update_waiter),
        TYPE_INVOICE_QRCODE(33, R.string.commonmodule_dinner_invoice),
        TYPE_INVOICE_REVOKE(34, R.string.commonmodule_dinner_wash_invoice_record),
        TYPE_FAST_HANDOVER_CALIBRATE(35, R.string.commonmodule_snack_re_calibrate),
        TYPE_DINNER_HANDOVER_CALIBRATE(36, R.string.commonmodule_dinner_re_calibrate),

        TYPE_DINNER_EDIT_WEIGHT(37, R.string.commonmodule_quantity_weight_edit),

        TYPE_DINNER_CUSTOMER_LOGIN(38, R.string.commonmodule_dinner_customer_login_by_number),

        TYPE_MEMBER_INTEGRAL_MODIFY(39, R.string.commonmodule_customer_member_Integral_modify),

        TYPE_DINNER_DELETE_DISH(40, R.string.commonmodule_dinner_delete_dish),

        TYPE_DINNER_BUSINESS_CHARGE(41, R.string.commonmodule_dinner_business_charge),

        TYPE_DINNER_MODIFY_PRICE(42, R.string.commonmodule_dinner_modifyprice),

    @Deprecated
    __UNKNOWN__;

    private final Helper<Integer> helper;
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
