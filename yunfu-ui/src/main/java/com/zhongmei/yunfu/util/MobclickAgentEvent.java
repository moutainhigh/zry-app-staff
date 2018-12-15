package com.zhongmei.yunfu.util;

import android.content.Context;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.UserActionCode;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class MobclickAgentEvent {

    // 点击弹钱箱次数
    public static String popUpCashBox = "Pop_Up_Cashbox";

    // 清除订单
    public static String clearOrder = "Clear_Order";

    // 点击功能菜单按钮
    public static String openFunctionMenu = "Open_Function_menu";

    // 整单备注
    public static String orderRemark = "Order_Remark";

    // 外带
    public static String takeWay = "Take_Way";

    // 整单打折
    public static String orderDishcount = "Order_Dishcount";

    // 点击点击界面中的会员
    public static String clickMemberInOrderDish = "Click_Member_In_Order_Dish";

    // 点击点菜界面中的桌号
    public static String clickDeskInOrderDish = "Click_Desk_In_Order_Dish";

    // 菜品搜索次数
    public static String searchDishEvent = "Search_Dish_Event";

    // 会员登录失败
    public static String memberLoginError = "Member_Login_Error_Event";

    // 会员登录超时
    public static String memberLoginTimeout = "Member_Login_Timeout_Event";

    // 正餐点菜界面搜索
    public static final String dinnerOrderDishSearch = "dinner_order_dish_search";

    // 正餐菜品种类上翻
    public static final String dinnerOrderDishTypePageUp = "dinner_order_dish_type_page_up";

    // 正餐菜品种类下翻
    public static final String dinnerOrderDishTypePageDown = "dinner_order_dish_type_page_down";

    // 正餐沽清列表按钮
    public static final String dinnerOrderDishClearList = "dinner_order_dish_clear_list";

    // 正餐点菜界面返回
    public static final String dinnerOrderDishSaveBack = "dinner_order_dish_save_back";

    // 正餐菜品搜索编码按钮
    public static final String dinnerOrderDishSearchCode = "dinner_order_dish_search_code";

    // 正餐菜品搜索首字母按钮
    public static final String dinnerOrderDishSearchFirstLetter = "dinner_order_dish_search_first_letter";

    // 正餐菜品搜索价格按钮
    public static final String dinnerOrderDishSearchPrice = "dinner_order_dish_search_price";

    // 正餐菜品搜索键盘收起按钮
    public static final String dinnerOrderDishSearchKeyboardPackUp = "dinner_order_dish_search_keyboard_pack_up";

    // 正餐结算界面
    // 批量折扣title点击
    public static final String dinnerSettleDiscountBatch = "dinner_settle_discount_batch";

    // 整单折扣title点击
    public static final String dinnerSettleDiscountDefine = "dinner_settle_discount_define";
    // 拆单支付
    public static final String dinnerSettleSplit = "dinner_settle_split";
    // 拆单支付的“确定”点击
    public static final String dinnerSettleSplitSure = "dinner_settle_split_sure";

    // 清空折扣
    public static final String dinnerSettleCleanDiscount = "dinner_settle_clean_discount";

    // 完成折扣
    public static final String dinnerSettleFinishedDiscount = "dinner_settle_finished_discount";

    // 折扣页关闭
    public static final String dinnerSettleDiscountClose = "dinner_settle_discount_close";

    // 保存结算信息
    public static final String dinnerSettleSave = "dinner_settle_save";

    // 打印预结单
    public static final String dinnerSettlePrePrint = "dinner_settle_preprint";

    // 去结账
    public static final String dinnerSettleToPay = "dinner_settle_to_pay";

    // 返回
    public static final String dinnerSettleBack = "dinner_settle_back";

    // 拆单全选入支付单
    public static final String dinnerSettleSplitSelect = "dinner_settle_split_select";
    // 拆单反选入支付单
    public static final String dinnerSettleSplitInvertSelect = "dinner_settle_split_invert_select";


    //正餐结算会员登录
    public static final String dinnerSettleMemberLogin = "dinner_settle_member_login";
    //正餐结算会员登录切换
    public static final String dinnerSettleMemberLoginCheck = "dinner_settle_member_login_check";
    //正餐结算会员登录退出
    public static final String dinnerSettleMemberLoginQuiet = "dinner_settle_member_login_quiet";
    //正餐结算折扣按钮
    public static final String dinnerSettleDiscount = "dinner_settle_discount";
    //    正餐结算附加费按钮
    public static final String dinnerSettleExtrage = "dinner_settle_extrage";
    //正餐结算营销活动按钮
    public static final String dinnerSettleMarket = "dinner_settle_market";
    //正餐结算微信卡劵按钮
    public static final String dinnerSettleWeixinCode = "dinner_settle_weixincode";
    //正餐结算积分按钮
    public static final String dinnerSettleCoupon = "dinner_settle_coupon";
    //    正餐结算积分按钮
    public static final String dinnerSettleIntegral = "dinner_settle_integral";


    //增加人数
    public static final String dinnerTableAddCustomerNumber = "dinner_table_add_customer_number";

    //减少人数
    public static final String dinnerTableMinusCustomerNumber = "dinner_table_minus_customer_number";

    //点击人数输入框
    public static final String dinnerTableClickCustomerNumberEdit = "dinner_table_click_customer_number_edit";

    //点击商品信息
    public static final String dinnerTableClickGoodsInfo = "dinner_table_click_goods_info";

    //点击桌台信息
    public static final String dinnerTableClickTableInfo = "dinner_table_click_table_info";

    //点击关闭按钮
    public static final String dinnerTableClickCloseButton = "dinner_table_click_close_button";

    //点菜按钮
    public static final String dinnerTableClickOrderdishButton = "dinner_table_click_orderdish_button";

    //结算按钮
    public static final String dinnerTableClickSettleButton = "dinner_table_click_settle_button";

    //结账按钮
    public static final String dinnerTableClickPayButton = "dinner_table_click_pay_button";

    //点击显示桌台按钮
    public static final String dinnerTableClickShowMoneyButton = "dinner_table_click_show_money_button";

    //点击快速开台按钮
    public static final String dinnerTableClickQuickOpentableButton = "dinner_table_click_quick_opentable_button";
    //收银微信按钮
    public static final String OnlinePayClickWeixinCheckButton = "online_pay_click_weixin_check_button";
    //收银支付宝按钮
    public static final String OnlinePayClickAlipayCheckButton = "online_pay_click_alipay_check_button";
    //收银百度钱包按钮
    public static final String OnlinePayClickBaidubaoCheckButton = "online_pay_click_baidubao_check_button";
    //收银二维码标签
    public static final String OnlinePayClickZhusaoCheckLabel = "online_pay_click_zhusao_check_label";
    //收银被扫（扫码枪）标签
    public static final String OnlinePayClickBeisaoCheckLabel = "online_pay_click_beisao_check_label";

    //正餐点菜界面展示操作按钮
    public static final String dinnerOrderDishOpenOperation = "dinner_order_dish_open_operation";
    //正餐点菜界面展示人数和服务员按钮
    public static final String dinnerOrderDishOpenNumberAndWaiter = "dinner_order_dish_open_number_and_waiter";
    //正餐点菜界面人数＋按钮
    public static final String dinnerOrderDishNumberPlus = "dinner_order_dish_number_plus";
    //正餐点菜界面人数－按钮
    public static final String dinnerOrderDishNumberMinus = "dinner_order_dish_number_minus";
    //正餐点菜界面更改服务员按钮
    public static final String dinnerOrderDishModifyWaiter = "dinner_order_dish_modify_waiter";

    //桌台详情页卡片点击
    public static final String dinnerDetailsTradesClick = "dinner_details_trades_click";
    //桌台详情页开台按钮第一次点击,提示开台
    public static final String dinnerTableDetailsOpenTradePre = "dinner_table_details_open_trade_pre";
    //桌台详情叶开台按钮二次点击,确认开台
    public static final String dinnerTableDetailsOpenTrade = "dinner_table_details_open_trade";
    //桌台详情界面展示人数和服务员按钮
    public static final String dinnerDetailsCustomerAndWaiter = "dinner_details_customer_and_waiter";
    //桌台详情页人数+
    public static final String dinnerDetailsCustomerIncrease = "dinner_details_customer_increase";
    //正餐桌台详情页人数—
    public static final String dinnerDetailsCustomerDecrease = "dinner_details_customer_decrease";
    //正餐桌台详情修改服务员
    public static final String dinnerDetailsWaiterModify = "dinner_details_waiter_modify";
    //正餐桌台详情服务员/就餐人数更改取消
    public static final String dinnerDetailsWaiterAndCustomerModifyCancel = "dinner_details_waiterandcustomer_modify_cancel";
    //正餐桌台详情服务员/就餐人数更改确定
    public static final String dinnerDetailsWaiterAndCustomerModifySure = "dinner_details_waiterandcustomer_modify_sure";

    //正餐异步操作通知栏重试
    public static final String dinnerAsyncHttpNotifyRetry = "dinner_async_http_notify_retry";
    //正餐异步操作通知栏取消
    public static final String dinnerAsyncHttpNotifyCancel = "dinner_async_http_notify_cancel";
    //正餐异步操作弹窗重试
    public static final String dinnerAsyncHttpDailoRetry = "dinner_async_http_dailo_retry";
    //正餐异步操作弹窗忽略
    public static final String dinnerAsyncHttpDailoIgnore = "dinner_async_http_dailo_ignore";
    //正餐异步操作桌台重试
    public static final String dinnerAsyncHttpTableRetry = "dinner_async_http_table_retry";

    //交接页面交接
    public static final String HandoverCurrentFragmentHandover = "HandoverCurrentFragment_handover";
    //交接页面开钱箱
    public static final String HandoverCurrentFragmentOpenCashBox = "HandoverCurrentFragment_open_cash_box";
    //交接页面计算器
    public static final String HandoverCurrentFragmentCalculator = "HandoverCurrentFragment_calculator";
    //关账页面执行关账
    public static final String ClosingActionFragmentDoClosing = "ClosingActionFragment_do_closing";
    //查看商品销售排行榜
    public static final String ReportFormMainFragmentShowReportForm = "ReportFormMainFragment_showReportForm";
    //打印商品销售排行榜
    public static final String ReportFormMainFragmentPrintReportForm = "ReportFormMainFragment_printReportForm";

    // 喜欢/备注
    public static final String dinnerLikeOrRemark = "dinner_like_remark";

    public static void openActivityDurationTrack(boolean enabled) {
        MobclickAgent.openActivityDurationTrack(enabled);
    }

    /**
     * 统计时长开始
     *
     * @param context
     */
    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    /**
     * 统计时长结束
     *
     * @param context
     */
    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    /**
     * 页面访问路径
     *
     * @param context
     * @param tag
     */
    public static void onResumePageStart(Context context, String tag) {
        MobclickAgent.onPageStart(tag);
        MobclickAgent.onResume(context);
    }

    /**
     * 页面访问路径
     *
     * @param context
     * @param tag
     */
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

    /**
     * 统计发生次数
     *
     * @param context
     * @param eventId
     */
    public static void onEvent(Context context, String eventId) {
        //MobclickAgent.onEvent(context, eventId);
    }

    public static void onEventValue(Context context, String tag, int duration) {
        String shopId = ShopInfoCfg.getInstance().shopId;
        //eventCat(shopId, tag, duration);
        if (duration > 500) {
            Map<String, String> map_value = new HashMap<>();
            //map_value.put("tag", tag);
            //map_value.put("shopId", MainApplication.getInstance().getShopInfo().get(ShopInfo.ID_KEY));
            map_value.put(tag, String.format("%s_%s", shopId, getTimeSecond(duration)));
            MobclickAgent.onEventValue(context, "app_start_duration", map_value, duration);
            //暂时关闭界面启动时间记录，后期自己开发接口上传
        }
    }

    /*private static void eventCat(String shopId, String tag, int duration) {
        Transaction transaction = Cat.newTransaction("App.Android",
                String.format("%s_%s", shopId, DateUtil.format(System.currentTimeMillis(), "yyyyMMdd")));
        try {
            Cat.logEvent(tag, String.valueOf(duration), Event.SUCCESS, String.format("%.2fs", duration / 1000f));
            transaction.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            transaction.setStatus(e);
        } finally {
            transaction.complete();
        }
    }*/

    public static int getTimeSecond(int duration) {
        //return String.format("%.1f", duration / 1000f);
        //return Math.round(duration / 1000f);
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




