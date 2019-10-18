package com.zhongmei.bty.basemodule.session.core.auth;



public interface Code {

    interface ZC {

        String AUTH_CODE_POS_LOGIN = "login:zc:pos";
    }

    interface KC {

        String AUTH_CODE_POS_LOGIN = "login:kc:pos";
    }

    interface LS {

        String AUTH_CODE_POS_LOGIN = "login:ls:pos";
    }


    String AUTH_CODE_POS_LOGIN = "login:pos:kc";

    interface Booking {

        String PERMISSION_BOOKING = "pos:yd";
    }

    interface Customer {

        String PERMISSION_CUSTOMER = "pos:gk";


        String PERMISSION_CUSTOMER_CREATE = "pos:gk:create";


        String PERMISSION_CUSTOMER_EDIT = "pos:gk:edit";


        String PERMISSION_CUSTOMER_UPDATE = "pos:gk:update";


        String PERMISSION_CUSTOMER_STORE = "pos:gk:store";


        String PERMISSION_CUSTOMER_STORE_CANCEL = "pos:gk:store:cancel";


        String PERMISSION_CUSTOMER_SELL_CARD = "pos:gk:sell:card";


        String PERMISSION_CUSTOMER_CARD_ENABLE = "pos:gk:card:enable";


        String PERMISSION_CUSTOMER_SELL_CARD_REFUND = "pos:gk:sell:card:refund";


        String PERMISSION_MEMBER_INTEGRAL_MODIFY = "pos:gk:integral_modify";
    }

    interface Dinner {

        String PERMISSION_DINNER = "pos:zc";


        String PERMISSION_DINNER_CREATE = "pos:zc:create";


        String PERMISSION_DINNER_TRANFER = "pos:zc:tranfer";


        String PERMISSION_DINNER_CASH = "pos:zc:cash";


        String PERMISSION_DINNER_MONEYBOX = "pos:zc:moneybox";


        String PERMISSION_DINNER_MALING = "pos:zc:maling";

        String PERMISSION_DINNER_AUTO_MALING = "pos:zc:automaling";

        String PERMISSION_DINNER_ACCEPT = "pos:zc:accept";


        String PERMISSION_DINNER_INVALID = "pos:zc:invalid";


        String PERMISSION_DINNER_REFUND = "pos:zc:refund";


        String PERMISSION_DINNER_HANDOVER = "pos:zc:handover";


        String PERMISSION_DINNER_HANDOVER_LAST = "pos:zc:handover:last";


        String PERMISSION_DINNER_PRIVILEDGE = "pos:zc:privilege";


        String PERMISSION_DINNER_PRIVILEDGE_DISCOUNT = "pos:zc:privilege:discount";


        String PERMISSION_DINNER_PRIVILEDGE_REBATE = "pos:zc:privilege:rebate";


        String PERMISSION_DINNER_PRIVILEDGE_FREE = "pos:zc:privilege:free";

        String PERMISSION_DINNER_PRIVILEDGE_PRESENT = "pos:zc:privilege:present";

        String PERMISSION_DINNER_PRIVILEDGE_BANQUET = "pos:zc:privilege:banquet";

        String PERMISSION_DINNER_REPAY = "pos:zc:repeat";


        String PERMISSION_DINNER_CLOSING = "pos:zc:closing";


        String PERMISSION_DINNER_CLOSING_HISTORY = "pos:zc:closing:history";


        String PERMISSION_DINNER_RETURN_GOODS = "pos:zc:return:goods";


        String PERMISSION_DINNER_NOTIFY_POS_RETRY = "pos:zc:notify:pos:retry";


        String PERMISSION_DINNER_SPLIT = "pos:zc:split";


        String PERMISSION_DINNER_CREDIT = "pos:zc:credit";


        String PERMISSION_PAYMENTS_EDIT = "pos:zc:paymentsedit";


        String PERMISSION_SELECT_WAITER = "pos:zx:selectwaiter";


        String PERMISSION_DINNER_MOVE_DISH = "pos:zc:movedish";


        String PERMISSION_DINNER_REPORT_FORM = "pos:zc:reportform";


        String PERMISSION_DINNER_TAKEOUT_CLEAR = "pos:zc:clear";

        String PERMISSION_DINNER_HANDOVER_CALIBRATE = "pos:zc:handover:calibrate";


        String PERMISSION_DINNER_PREREPRINT_SETTING = "pos:zc:preprint_setting";


        String PERMISSION_DINNER_QUANTITY = "pos:zc:weight_input";


        String PERMISSION_DINNER_CHANGEPRICE = "pos:zc:changeprice";


        String PERMISSION_DISCOUND = "discount";
        String PERMISSION_REBATE = "rebate";


        String PERMISSION_CUSTOMER_LOGIN = "pos:zc:customer_login";


        String PERMISSION__DINNER_DELETE_DISH = "pos:zc:delete_dish";


        String PERMISSION_DINNER_BATCH_DELETE_OR_RETURN = "pos:zc:batch_delete_or_return";


        String PERMISSION_DINNER_BUSINESS_CHARGE = "pos:zc:business_charge";


        String PERMISSION_DINNER_MODIFY_DISH = "pos:zc:modify_dish";


        String PERMISSION_DINNER_MODIFY_PRICE = "pos:zc:modify_price";
    }

    interface Snack {

        String PERMISSION_FASTFOOD = "pos:kc";

        String PERMISSION_FASTFOOD_CREATE = "pos:kc:create";

        String PERMISSION_FASTFOOD_CASH = "pos:kc:cash";

        String PERMISSION_FASTFOOD_MONEYBOX = "pos:kc:moneybox";

        String PERMISSION_FASTFOOD_AUTO_MALING = "pos:kc:automaling";


        String PERMISSION_FASTFOOD_MALING = "pos:kc:maling";

        String PERMISSION_FASTFOOD_ACCEPT = "pos:kc:accept";

        String PERMISSION_FASTFOOD_INVALID = "pos:kc:invalid";

        String PERMISSION_FASTFOOD_REFUND = "pos:kc:refund";

        String PERMISSION_FASTFOOD_REPRINT = "pos:kc:reprint";

        String PERMISSION_FASTFOOD_HANDOVER = "pos:kc:handover";

        String PERMISSION_FASTFOOD_HANDOVER_LAST = "pos:kc:handover:last";

        String PERMISSION_FASTFOOD_HANDOVER_CALIBRATE = "pos:kc:handover:calibrate";


        String PERMISSION_FASTFOOD_PRIVILEDGE = "pos:kc:privilege";

        String PERMISSION_FASTFOOD_PRIVILEDGE_DISCOUNT = "pos:kc:privilege:discount";

        String PERMISSION_FASTFOOD_PRIVILEDGE_REBATE = "pos:kc:privilege:rebate";

        String PERMISSION_FASTFOOD_PRIVILEDGE_FREE = "pos:kc:privilege:free";

        String PERMISSION_FASTFOOD_PRIVILEDGE_PRESENT = "pos:kc:privilege:present";

        String PERMISSION_FASTFOOD_CLOSING = "pos:kc:closing";


        String PERMISSION_FASTFOOD_CLOSING_HISTORY = "pos:kc:closing:history";

        String PERMISSION_FASTFOOD_NOTIFY_POS_RETRY = "pos:kc:notify:pos:retry";


        String PERMISSION_PAYMENTS_EDIT = "pos:kc:paymentsedit";

        String PERMISSION_DISCOUND = "discount";
        String PERMISSION_REBATE = "rebate";


        String PERMISSION_FASTFOOD_REPAY = "pos:kc:repeat";


        String PERMISSION_FASTFOOD_REPORT_FORM = "pos:kc:reportform";


        String PERMISSION_FASTFOOD_SC = "pos:kc:sc";
    }

    interface Phone {

        String PERMISSION_PHONE = "pos:dh";
    }

    interface Queue {

        String PERMISSION_QUEUE = "pos:pd";
    }

    interface Retail {

        String PERMISSION_FASTFOOD = "pos:kc";

        String PERMISSION_FASTFOOD_CREATE = "pos:kc:create";

        String PERMISSION_FASTFOOD_CASH = "pos:kc:cash";

        String PERMISSION_FASTFOOD_MONEYBOX = "pos:kc:moneybox";

        String PERMISSION_FASTFOOD_AUTO_MALING = "pos:kc:automaling";


        String PERMISSION_FASTFOOD_MALING = "pos:kc:maling";

        String PERMISSION_FASTFOOD_ACCEPT = "pos:kc:accept";

        String PERMISSION_FASTFOOD_INVALID = "pos:kc:invalid";

        String PERMISSION_FASTFOOD_REFUND = "pos:kc:refund";

        String PERMISSION_FASTFOOD_REPRINT = "pos:kc:reprint";

        String PERMISSION_FASTFOOD_HANDOVER = "pos:kc:handover";

        String PERMISSION_FASTFOOD_HANDOVER_LAST = "pos:kc:handover:last";

        String PERMISSION_FASTFOOD_HANDOVER_CALIBRATE = "pos:kc:handover:calibrate";


        String PERMISSION_FASTFOOD_PRIVILEDGE = "pos:kc:privilege";

        String PERMISSION_FASTFOOD_PRIVILEDGE_DISCOUNT = "pos:kc:privilege:discount";

        String PERMISSION_FASTFOOD_PRIVILEDGE_REBATE = "pos:kc:privilege:rebate";

        String PERMISSION_FASTFOOD_PRIVILEDGE_FREE = "pos:kc:privilege:free";

        String PERMISSION_FASTFOOD_PRIVILEDGE_PRESENT = "pos:kc:privilege:present";

        String PERMISSION_FASTFOOD_CLOSING = "pos:kc:closing";


        String PERMISSION_FASTFOOD_CLOSING_HISTORY = "pos:kc:closing:history";

        String PERMISSION_FASTFOOD_NOTIFY_POS_RETRY = "pos:kc:notify:pos:retry";


        String PERMISSION_PAYMENTS_EDIT = "pos:kc:paymentsedit";

        String PERMISSION_DISCOUND = "discount";
        String PERMISSION_REBATE = "rebate";


        String PERMISSION_FASTFOOD_REPAY = "pos:kc:repeat";


        String PERMISSION_FASTFOOD_REPORT_FORM = "pos:kc:reportform";


        String PERMISSION_FASTFOOD_SC = "pos:kc:sc";
    }

    interface Takeout {

        String PERMISSION_TAKEOUT = "pos:wm";


        String PERMISSION_TAKEOUT_ACCEPT = "pos:wm:accept";


        String PERMISSION_TAKEOUT_INVALID = "pos:wm:invalid";


        String PERMISSION_TAKEOUT_REFUND = "pos:wm:refund";


        String PERMISSION_TAKEOUT_CLEAR = "pos:wm:clear";
    }
}
