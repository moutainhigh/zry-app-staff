package com.zhongmei.yunfu.util;

import android.content.Context;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.UserActionCode;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class MobclickAgentEvent {

        public static String popUpCashBox = "Pop_Up_Cashbox";

        public static String clearOrder = "Clear_Order";

        public static String openFunctionMenu = "Open_Function_menu";

        public static String orderRemark = "Order_Remark";

        public static String takeWay = "Take_Way";

        public static String orderDishcount = "Order_Dishcount";

        public static String clickMemberInOrderDish = "Click_Member_In_Order_Dish";

        public static String clickDeskInOrderDish = "Click_Desk_In_Order_Dish";

        public static String searchDishEvent = "Search_Dish_Event";

        public static String memberLoginError = "Member_Login_Error_Event";

        public static String memberLoginTimeout = "Member_Login_Timeout_Event";

        public static final String dinnerOrderDishSearch = "dinner_order_dish_search";

        public static final String dinnerOrderDishTypePageUp = "dinner_order_dish_type_page_up";

        public static final String dinnerOrderDishTypePageDown = "dinner_order_dish_type_page_down";

        public static final String dinnerOrderDishClearList = "dinner_order_dish_clear_list";

        public static final String dinnerOrderDishSaveBack = "dinner_order_dish_save_back";

        public static final String dinnerOrderDishSearchCode = "dinner_order_dish_search_code";

        public static final String dinnerOrderDishSearchFirstLetter = "dinner_order_dish_search_first_letter";

        public static final String dinnerOrderDishSearchPrice = "dinner_order_dish_search_price";

        public static final String dinnerOrderDishSearchKeyboardPackUp = "dinner_order_dish_search_keyboard_pack_up";

            public static final String dinnerSettleDiscountBatch = "dinner_settle_discount_batch";

        public static final String dinnerSettleDiscountDefine = "dinner_settle_discount_define";
        public static final String dinnerSettleSplit = "dinner_settle_split";
        public static final String dinnerSettleSplitSure = "dinner_settle_split_sure";

        public static final String dinnerSettleCleanDiscount = "dinner_settle_clean_discount";

        public static final String dinnerSettleFinishedDiscount = "dinner_settle_finished_discount";

        public static final String dinnerSettleDiscountClose = "dinner_settle_discount_close";

        public static final String dinnerSettleSave = "dinner_settle_save";

        public static final String dinnerSettlePrePrint = "dinner_settle_preprint";

        public static final String dinnerSettleToPay = "dinner_settle_to_pay";

        public static final String dinnerSettleBack = "dinner_settle_back";

        public static final String dinnerSettleSplitSelect = "dinner_settle_split_select";
        public static final String dinnerSettleSplitInvertSelect = "dinner_settle_split_invert_select";


        public static final String dinnerSettleMemberLogin = "dinner_settle_member_login";
        public static final String dinnerSettleMemberLoginCheck = "dinner_settle_member_login_check";
        public static final String dinnerSettleMemberLoginQuiet = "dinner_settle_member_login_quiet";
        public static final String dinnerSettleDiscount = "dinner_settle_discount";
        public static final String dinnerSettleExtrage = "dinner_settle_extrage";
        public static final String dinnerSettleMarket = "dinner_settle_market";
        public static final String dinnerSettleWeixinCode = "dinner_settle_weixincode";
        public static final String dinnerSettleCoupon = "dinner_settle_coupon";
        public static final String dinnerSettleIntegral = "dinner_settle_integral";


        public static final String dinnerTableAddCustomerNumber = "dinner_table_add_customer_number";

        public static final String dinnerTableMinusCustomerNumber = "dinner_table_minus_customer_number";

        public static final String dinnerTableClickCustomerNumberEdit = "dinner_table_click_customer_number_edit";

        public static final String dinnerTableClickGoodsInfo = "dinner_table_click_goods_info";

        public static final String dinnerTableClickTableInfo = "dinner_table_click_table_info";

        public static final String dinnerTableClickCloseButton = "dinner_table_click_close_button";

        public static final String dinnerTableClickOrderdishButton = "dinner_table_click_orderdish_button";

        public static final String dinnerTableClickSettleButton = "dinner_table_click_settle_button";

        public static final String dinnerTableClickPayButton = "dinner_table_click_pay_button";

        public static final String dinnerTableClickShowMoneyButton = "dinner_table_click_show_money_button";

        public static final String dinnerTableClickQuickOpentableButton = "dinner_table_click_quick_opentable_button";
        public static final String OnlinePayClickWeixinCheckButton = "online_pay_click_weixin_check_button";
        public static final String OnlinePayClickAlipayCheckButton = "online_pay_click_alipay_check_button";
        public static final String OnlinePayClickBaidubaoCheckButton = "online_pay_click_baidubao_check_button";
        public static final String OnlinePayClickZhusaoCheckLabel = "online_pay_click_zhusao_check_label";
        public static final String OnlinePayClickBeisaoCheckLabel = "online_pay_click_beisao_check_label";

        public static final String dinnerOrderDishOpenOperation = "dinner_order_dish_open_operation";
        public static final String dinnerOrderDishOpenNumberAndWaiter = "dinner_order_dish_open_number_and_waiter";
        public static final String dinnerOrderDishNumberPlus = "dinner_order_dish_number_plus";
        public static final String dinnerOrderDishNumberMinus = "dinner_order_dish_number_minus";
        public static final String dinnerOrderDishModifyWaiter = "dinner_order_dish_modify_waiter";

        public static final String dinnerDetailsTradesClick = "dinner_details_trades_click";
        public static final String dinnerTableDetailsOpenTradePre = "dinner_table_details_open_trade_pre";
        public static final String dinnerTableDetailsOpenTrade = "dinner_table_details_open_trade";
        public static final String dinnerDetailsCustomerAndWaiter = "dinner_details_customer_and_waiter";
        public static final String dinnerDetailsCustomerIncrease = "dinner_details_customer_increase";
        public static final String dinnerDetailsCustomerDecrease = "dinner_details_customer_decrease";
        public static final String dinnerDetailsWaiterModify = "dinner_details_waiter_modify";
        public static final String dinnerDetailsWaiterAndCustomerModifyCancel = "dinner_details_waiterandcustomer_modify_cancel";
        public static final String dinnerDetailsWaiterAndCustomerModifySure = "dinner_details_waiterandcustomer_modify_sure";

        public static final String dinnerAsyncHttpNotifyRetry = "dinner_async_http_notify_retry";
        public static final String dinnerAsyncHttpNotifyCancel = "dinner_async_http_notify_cancel";
        public static final String dinnerAsyncHttpDailoRetry = "dinner_async_http_dailo_retry";
        public static final String dinnerAsyncHttpDailoIgnore = "dinner_async_http_dailo_ignore";
        public static final String dinnerAsyncHttpTableRetry = "dinner_async_http_table_retry";

        public static final String HandoverCurrentFragmentHandover = "HandoverCurrentFragment_handover";
        public static final String HandoverCurrentFragmentOpenCashBox = "HandoverCurrentFragment_open_cash_box";
        public static final String HandoverCurrentFragmentCalculator = "HandoverCurrentFragment_calculator";
        public static final String ClosingActionFragmentDoClosing = "ClosingActionFragment_do_closing";
        public static final String ReportFormMainFragmentShowReportForm = "ReportFormMainFragment_showReportForm";
        public static final String ReportFormMainFragmentPrintReportForm = "ReportFormMainFragment_printReportForm";

        public static final String dinnerLikeOrRemark = "dinner_like_remark";

    public static void openActivityDurationTrack(boolean enabled) {
        MobclickAgent.openActivityDurationTrack(enabled);
    }


    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }


    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }


    public static void onResumePageStart(Context context, String tag) {
        MobclickAgent.onPageStart(tag);
        MobclickAgent.onResume(context);
    }


    public static void onPausePageEnd(Context context, String tag) {
        MobclickAgent.onPageEnd(tag);
        MobclickAgent.onPause(context);
    }

    public static void onPageStart(String tag) {
        MobclickAgent.onPageStart(tag);
    }

    public static void onPageEnd(String tag) {
        MobclickAgent.onPageEnd(tag);
    }

    public static void onEvent(UserActionCode eventCode) {
        onEvent(BaseApplication.sInstance, eventCode.name());
    }


    public static void onEvent(Context context, String eventId) {
            }

    public static void onEventValue(Context context, String tag, int duration) {
        String shopId = ShopInfoCfg.getInstance().shopId;
                if (duration > 500) {
            Map<String, String> map_value = new HashMap<>();
                                    map_value.put(tag, String.format("%s_%s", shopId, getTimeSecond(duration)));
            MobclickAgent.onEventValue(context, "app_start_duration", map_value, duration);
                    }
    }



    public static int getTimeSecond(int duration) {
                        float a = duration / 1000f;
        int b = (int) Math.floor(a);
        float c = a - b;
        if (c >= 0.8) {
            return b * 1000 + 1000;
        }
        if (c >= 0.3) {
            return b * 1000 + 500;
        }
        return b * 1000;
    }
}




