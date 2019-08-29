package com.zhongmei.beauty.utils;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

/**
 * 美业服务器地址类
 * Created by demo on 2018/12/15
 */

public class BeautyServerAddressUtil {

    private static final String BEAUTY = "/beauty";

    /**
     * 下单
     *
     * @return
     */
    public static String submit() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/addOrderData";
    }

    /**
     * 改单
     *
     * @return
     */
    public static String modifyBeauty() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/modifyOrderData";
    }

    /**
     * 订单作废
     *
     * @return
     */
    public static String deleteTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/deleteOrderData";
    }

    /**
     * 订单退货
     *
     * @return
     */
    public static String refundTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/returnOrderData";
    }

    /**
     * 订单反结账
     *
     * @return
     */
    public static String repayTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/beauty/v1/trade/repay";
    }

    /**
     * 收银
     *
     * @return
     */
    public static String pay() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/scanPay";
    }

    /**
     * 根据顾客id获取次卡服务信息
     *
     * @return
     */
    public static String cardServiceInfo() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/cardtime";
    }

    /**
     * 获取顾客消费记录
     *
     * @return
     */
    public static String getCustomerExpense() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/expense";
    }

    /**
     * 获取顾客档案
     *
     * @return
     */
    public static String getCustomerDoc() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customerArchives/archivesList";
    }

    /**
     * 获取顾客档案
     *
     * @return
     */
    public static String getTask() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/taskRemind/list";
    }

    /**
     * 获取顾客档案
     *
     * @return
     */
    public static String saveCustomerDoc() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customerArchives/addArchives";
    }

    /**
     * 保存新任务信息
     *
     * @return
     */
    public static String saveTask() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/taskRemind/addTaskRemind";
    }

    /**
     * 保存编辑任务信息
     *
     * @return
     */
    public static String saveEditTask() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/taskRemind/modfityTaskRemind";
    }

    /**
     * 获取顾客档案详情
     *
     * @return
     */
    public static String getCustomerDocDetail() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customerArchives/queryById";
    }

    /**
     * 交接历史查询
     *
     * @return
     */
    public static String findHandOverHistoryInfoApi() {
        return SwitchServerManager.getInstance().getServerKey()
                + "/beauty/v1/cashHandOver/findHistory";
    }

    /**
     * 交接统计查询
     *
     * @return
     */
    public static String getHandOverInfoApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/beauty/v2/cashHandOver/stats";
    }

    /**
     * 交接创建
     *
     * @return
     */
    public static String createHandOverInfoApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/beauty/v1/cashHandOver/create";
    }


    /**
     * 关账详情
     *
     * @Return String 返回类型
     */
    public static String doCloseDetail() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/closeDetail";
    }

    /**
     * @Description: 执行关账 url
     * @Param @return
     * @Return String 返回类型
     */
    public static String doCloseBill() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/close";
    }

    /**
     * 查询关账历史
     *
     * @Param @return TODO
     * @Return String 返回类型
     */
    public static String queryCloseHistoryList() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/list";
    }

    /**
     * 查询关账交接记录
     *
     * @Return String 返回类型
     */
    public static String queryCloseHandoverhistory() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/handover/result";
    }


    /**
     * 根据CardNo查询次卡消费历史记录
     *
     * @return
     */
    public static String cardServiceHistory() {
        return "card_getCardServiceHistory";
    }

    /**
     * 获取顾客活动购买记录
     *
     * @return
     */
    public static String getActivityBuyRecord() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/wxTradeData/queryByCustomer";
    }

    /**
     * 根据券码查询活动
     * @return
     */
    public static String getActivityByCode() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/wxTradeData/queryByCode";
    }

    /**
     * 获取预定列表
     *
     * @return
     */
    public static String getBookingList() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/getPageBooking";
    }

    /**
     * 创建预定
     *
     * @return
     */
    public static String bookingSubmit() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/addBookingData";
    }

    /**
     * 预定技师检查
     *
     * @return
     */
    public static String bookingCheckUser() {
        return SwitchServerManager.getInstance().getServerKey() + "/v1/booking/check-user";
    }

    /**
     * 预定取消
     *
     * @return
     */
    public static String bookingCancel() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/delBookingData";
    }

    /**
     * 预定开单
     *
     * @return
     */
    public static String bookingOpenTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/updateBookingToOrder";
    }

    /**
     * 编辑预定提交
     *
     * @return
     */
    public static String updateBookingSubmit() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/updateBookingData";
    }

    /**
     * 接受或拒绝
     *
     * @return
     */
    public static String acceptOrRefuseBooking() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/updateWxBookingStatus";
    }

}
