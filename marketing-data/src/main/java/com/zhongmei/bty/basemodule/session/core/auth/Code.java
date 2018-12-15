package com.zhongmei.bty.basemodule.session.core.auth;

/**
 * Created by demo on 2018/12/15
 */

public interface Code {

    interface ZC {
        /**
         * 登录权限吗
         */
        String AUTH_CODE_POS_LOGIN = "login:zc:pos";
    }

    interface KC {
        /**
         * 登录权限吗
         */
        String AUTH_CODE_POS_LOGIN = "login:kc:pos";
    }

    interface LS {
        /**
         * 登录权限嘛
         */
        String AUTH_CODE_POS_LOGIN = "login:ls:pos";
    }

    /**
     * POS快餐前端登录权限
     */
    String AUTH_CODE_POS_LOGIN = "login:pos:kc";

    interface Booking {
        /**
         * 预定权限
         */
        String PERMISSION_BOOKING = "pos:yd";
    }

    interface Customer {
        /**
         * 顾客权限
         */
        String PERMISSION_CUSTOMER = "pos:gk";

        /**
         * 顾客创建权限
         */
        String PERMISSION_CUSTOMER_CREATE = "pos:gk:create";

        /**
         * 顾客编辑权限
         */
        String PERMISSION_CUSTOMER_EDIT = "pos:gk:edit";

        /**
         * 顾客升级权限
         */
        String PERMISSION_CUSTOMER_UPDATE = "pos:gk:update";

        /**
         * 顾客储值权限
         */
        String PERMISSION_CUSTOMER_STORE = "pos:gk:store";

        /**
         * 储值记录撤销权限
         */
        String PERMISSION_CUSTOMER_STORE_CANCEL = "pos:gk:store:cancel";

        /**
         * 售卡权限
         */
        String PERMISSION_CUSTOMER_SELL_CARD = "pos:gk:sell:card";

        /**
         * 实体卡激活权限
         */
        String PERMISSION_CUSTOMER_CARD_ENABLE = "pos:gk:card:enable";

        /**
         * 售卡退货权限
         */
        String PERMISSION_CUSTOMER_SELL_CARD_REFUND = "pos:gk:sell:card:refund";

        /**
         * 顾客会员积分补录/扣除
         */
        String PERMISSION_MEMBER_INTEGRAL_MODIFY = "pos:gk:integral_modify";
    }

    interface Dinner {
        /**
         * 正餐权限
         */
        String PERMISSION_DINNER = "pos:zc";

        /**
         * 正餐开台权限
         */
        String PERMISSION_DINNER_CREATE = "pos:zc:create";

        /**
         * 正餐转台权限
         */
        String PERMISSION_DINNER_TRANFER = "pos:zc:tranfer";

        /**
         * 正餐收银权限
         */
        String PERMISSION_DINNER_CASH = "pos:zc:cash";

        /**
         * 正餐开钱箱权限
         */
        String PERMISSION_DINNER_MONEYBOX = "pos:zc:moneybox";

        /**
         * 正餐抹零权限
         */
        String PERMISSION_DINNER_MALING = "pos:zc:maling";
        /**
         * 正餐自定义抹零权限
         */
        String PERMISSION_DINNER_AUTO_MALING = "pos:zc:automaling";
        /**
         * 正餐接受拒绝权限
         */
        String PERMISSION_DINNER_ACCEPT = "pos:zc:accept";

        /**
         * 正餐作废权限
         */
        String PERMISSION_DINNER_INVALID = "pos:zc:invalid";

        /**
         * 正餐退货权限
         */
        String PERMISSION_DINNER_REFUND = "pos:zc:refund";

        /**
         * 正餐交接权限
         */
        String PERMISSION_DINNER_HANDOVER = "pos:zc:handover";

        /**
         * 正餐历史交接权限
         */
        String PERMISSION_DINNER_HANDOVER_LAST = "pos:zc:handover:last";

        /**
         * 正餐优惠权限
         */
        String PERMISSION_DINNER_PRIVILEDGE = "pos:zc:privilege";

        /**
         * 正餐打折优惠权限
         */
        String PERMISSION_DINNER_PRIVILEDGE_DISCOUNT = "pos:zc:privilege:discount";

        /**
         * 正餐折让权限
         */
        String PERMISSION_DINNER_PRIVILEDGE_REBATE = "pos:zc:privilege:rebate";

        /**
         * 正餐免单权限，针对整单
         */
        String PERMISSION_DINNER_PRIVILEDGE_FREE = "pos:zc:privilege:free";
        /**
         * 正餐赠送权限，针对批量
         */
        String PERMISSION_DINNER_PRIVILEDGE_PRESENT = "pos:zc:privilege:present";
        /**
         * 正餐宴请权限
         */
        String PERMISSION_DINNER_PRIVILEDGE_BANQUET = "pos:zc:privilege:banquet";
        /**
         * 正餐反结账权限
         */
        String PERMISSION_DINNER_REPAY = "pos:zc:repeat";

        /**
         * 正餐关账权限
         */
        String PERMISSION_DINNER_CLOSING = "pos:zc:closing";

        /**
         * 正餐关账历史权限
         */
        String PERMISSION_DINNER_CLOSING_HISTORY = "pos:zc:closing:history";

        /**
         * 正餐退菜权限
         */
        String PERMISSION_DINNER_RETURN_GOODS = "pos:zc:return:goods";

        /**
         * 正餐通知中心重试pos交易权限
         */
        String PERMISSION_DINNER_NOTIFY_POS_RETRY = "pos:zc:notify:pos:retry";

        /**
         * 正餐拆单权限
         */
        String PERMISSION_DINNER_SPLIT = "pos:zc:split";

        /**
         * 正餐挂账权限
         */
        String PERMISSION_DINNER_CREDIT = "pos:zc:credit";

        /**
         * 收支编辑权限
         */
        String PERMISSION_PAYMENTS_EDIT = "pos:zc:paymentsedit";

        /**
         * 选择服务员权限
         */
        String PERMISSION_SELECT_WAITER = "pos:zx:selectwaiter";

        /**
         * 移菜权限
         */
        String PERMISSION_DINNER_MOVE_DISH = "pos:zc:movedish";

        /**
         * 报表中心权限
         */
        String PERMISSION_DINNER_REPORT_FORM = "pos:zc:reportform";

        /**
         * 正餐清账权限
         */
        String PERMISSION_DINNER_TAKEOUT_CLEAR = "pos:zc:clear";
        /**
         * 正餐校准权限
         */
        String PERMISSION_DINNER_HANDOVER_CALIBRATE = "pos:zc:handover:calibrate";

        /**
         * 正餐打印预结单设置
         */
        String PERMISSION_DINNER_PREREPRINT_SETTING = "pos:zc:preprint_setting";

        /**
         * 称重商品编辑权限
         */
        String PERMISSION_DINNER_QUANTITY = "pos:zc:weight_input";

        /**
         * 正餐变价
         */
        String PERMISSION_DINNER_CHANGEPRICE = "pos:zc:changeprice";


        String PERMISSION_DISCOUND = "discount";
        String PERMISSION_REBATE = "rebate";

        /**
         * 正餐顾客登录权限（By Phone Number）
         */
        String PERMISSION_CUSTOMER_LOGIN = "pos:zc:customer_login";

        /**
         * 正餐删菜权限（对于正餐未传送后厨但是生效的菜品）
         */
        String PERMISSION__DINNER_DELETE_DISH = "pos:zc:delete_dish";

        /**
         * 正餐批量删除／作废菜品权限
         */
        String PERMISSION_DINNER_BATCH_DELETE_OR_RETURN = "pos:zc:batch_delete_or_return";

        /**
         * 正餐获取实时概况的权限
         */
        String PERMISSION_DINNER_BUSINESS_CHARGE = "pos:zc:business_charge";

        /**
         * 正餐修改菜品权限
         */
        String PERMISSION_DINNER_MODIFY_DISH = "pos:zc:modify_dish";

        /**
         * 正餐修改价格
         */
        String PERMISSION_DINNER_MODIFY_PRICE = "pos:zc:modify_price";
    }

    interface Snack {
        /**
         * 快餐权限
         */
        String PERMISSION_FASTFOOD = "pos:kc";
        /**
         * 快餐开台权限
         */
        String PERMISSION_FASTFOOD_CREATE = "pos:kc:create";
        /**
         * 快餐收银权限
         */
        String PERMISSION_FASTFOOD_CASH = "pos:kc:cash";
        /**
         * 快餐开钱箱权限
         */
        String PERMISSION_FASTFOOD_MONEYBOX = "pos:kc:moneybox";
        /**
         * 快餐自定义抹零权限
         */
        String PERMISSION_FASTFOOD_AUTO_MALING = "pos:kc:automaling";

        /**
         * 快餐抹零权限
         */
        String PERMISSION_FASTFOOD_MALING = "pos:kc:maling";
        /**
         * 快餐接受拒绝权限
         */
        String PERMISSION_FASTFOOD_ACCEPT = "pos:kc:accept";
        /**
         * 快餐作废权限
         */
        String PERMISSION_FASTFOOD_INVALID = "pos:kc:invalid";
        /**
         * 快餐退货权限
         */
        String PERMISSION_FASTFOOD_REFUND = "pos:kc:refund";
        /**
         * 快餐补打权限
         */
        String PERMISSION_FASTFOOD_REPRINT = "pos:kc:reprint";
        /**
         * 快餐交接权限
         */
        String PERMISSION_FASTFOOD_HANDOVER = "pos:kc:handover";
        /**
         * 快餐历史交接权限
         */
        String PERMISSION_FASTFOOD_HANDOVER_LAST = "pos:kc:handover:last";
        /**
         * 快餐校准权限
         */
        String PERMISSION_FASTFOOD_HANDOVER_CALIBRATE = "pos:kc:handover:calibrate";

        /**
         * 快餐优惠权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE = "pos:kc:privilege";
        /**
         * 快餐打折优惠权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_DISCOUNT = "pos:kc:privilege:discount";
        /**
         * 快餐折让权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_REBATE = "pos:kc:privilege:rebate";
        /**
         * 快餐免单权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_FREE = "pos:kc:privilege:free";
        /**
         * 快餐赠送权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_PRESENT = "pos:kc:privilege:present";
        /**
         * 快餐关账权限
         */
        String PERMISSION_FASTFOOD_CLOSING = "pos:kc:closing";

        /**
         * 快餐关账历史权限
         */
        String PERMISSION_FASTFOOD_CLOSING_HISTORY = "pos:kc:closing:history";
        /**
         * 快餐通知中心重试pos交易权限
         */
        String PERMISSION_FASTFOOD_NOTIFY_POS_RETRY = "pos:kc:notify:pos:retry";

        /**
         * 收支编辑权限
         */
        String PERMISSION_PAYMENTS_EDIT = "pos:kc:paymentsedit";

        String PERMISSION_DISCOUND = "discount";
        String PERMISSION_REBATE = "rebate";

        /**
         * 快餐反结账权限
         */
        String PERMISSION_FASTFOOD_REPAY = "pos:kc:repeat";

        /**
         * 报表中心权限
         */
        String PERMISSION_FASTFOOD_REPORT_FORM = "pos:kc:reportform";

        /**
         * 快餐送餐权限
         */
        String PERMISSION_FASTFOOD_SC = "pos:kc:sc";
    }

    interface Phone {
        /**
         * 电话权限
         */
        String PERMISSION_PHONE = "pos:dh";
    }

    interface Queue {
        /**
         * 排队权限
         */
        String PERMISSION_QUEUE = "pos:pd";
    }

    interface Retail {
        /**
         * 快餐权限
         */
        String PERMISSION_FASTFOOD = "pos:kc";
        /**
         * 快餐开台权限
         */
        String PERMISSION_FASTFOOD_CREATE = "pos:kc:create";
        /**
         * 快餐收银权限
         */
        String PERMISSION_FASTFOOD_CASH = "pos:kc:cash";
        /**
         * 快餐开钱箱权限
         */
        String PERMISSION_FASTFOOD_MONEYBOX = "pos:kc:moneybox";
        /**
         * 快餐自定义抹零权限
         */
        String PERMISSION_FASTFOOD_AUTO_MALING = "pos:kc:automaling";

        /**
         * 快餐抹零权限
         */
        String PERMISSION_FASTFOOD_MALING = "pos:kc:maling";
        /**
         * 快餐接受拒绝权限
         */
        String PERMISSION_FASTFOOD_ACCEPT = "pos:kc:accept";
        /**
         * 快餐作废权限
         */
        String PERMISSION_FASTFOOD_INVALID = "pos:kc:invalid";
        /**
         * 快餐退货权限
         */
        String PERMISSION_FASTFOOD_REFUND = "pos:kc:refund";
        /**
         * 快餐补打权限
         */
        String PERMISSION_FASTFOOD_REPRINT = "pos:kc:reprint";
        /**
         * 快餐交接权限
         */
        String PERMISSION_FASTFOOD_HANDOVER = "pos:kc:handover";
        /**
         * 快餐历史交接权限
         */
        String PERMISSION_FASTFOOD_HANDOVER_LAST = "pos:kc:handover:last";
        /**
         * 快餐校准权限
         */
        String PERMISSION_FASTFOOD_HANDOVER_CALIBRATE = "pos:kc:handover:calibrate";

        /**
         * 快餐优惠权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE = "pos:kc:privilege";
        /**
         * 快餐打折优惠权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_DISCOUNT = "pos:kc:privilege:discount";
        /**
         * 快餐折让权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_REBATE = "pos:kc:privilege:rebate";
        /**
         * 快餐免单权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_FREE = "pos:kc:privilege:free";
        /**
         * 快餐赠送权限
         */
        String PERMISSION_FASTFOOD_PRIVILEDGE_PRESENT = "pos:kc:privilege:present";
        /**
         * 快餐关账权限
         */
        String PERMISSION_FASTFOOD_CLOSING = "pos:kc:closing";

        /**
         * 快餐关账历史权限
         */
        String PERMISSION_FASTFOOD_CLOSING_HISTORY = "pos:kc:closing:history";
        /**
         * 快餐通知中心重试pos交易权限
         */
        String PERMISSION_FASTFOOD_NOTIFY_POS_RETRY = "pos:kc:notify:pos:retry";

        /**
         * 收支编辑权限
         */
        String PERMISSION_PAYMENTS_EDIT = "pos:kc:paymentsedit";

        String PERMISSION_DISCOUND = "discount";
        String PERMISSION_REBATE = "rebate";

        /**
         * 快餐反结账权限
         */
        String PERMISSION_FASTFOOD_REPAY = "pos:kc:repeat";

        /**
         * 报表中心权限
         */
        String PERMISSION_FASTFOOD_REPORT_FORM = "pos:kc:reportform";

        /**
         * 快餐送餐权限
         */
        String PERMISSION_FASTFOOD_SC = "pos:kc:sc";
    }

    interface Takeout {
        /**
         * 外卖权限
         */
        String PERMISSION_TAKEOUT = "pos:wm";

        /**
         * 外卖接受拒绝权限
         */
        String PERMISSION_TAKEOUT_ACCEPT = "pos:wm:accept";

        /**
         * 外卖作废权限
         */
        String PERMISSION_TAKEOUT_INVALID = "pos:wm:invalid";

        /**
         * 外卖退货权限
         */
        String PERMISSION_TAKEOUT_REFUND = "pos:wm:refund";

        /**
         * 外卖清账权限
         */
        String PERMISSION_TAKEOUT_CLEAR = "pos:wm:clear";
    }
}
