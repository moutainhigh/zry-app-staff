package com.zhongmei.beauty.utils;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;



public class BeautyServerAddressUtil {

    private static final String BEAUTY = "/beauty";


    public static String submit() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/addOrderData";
    }


    public static String modifyBeauty() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/modifyOrderData";
    }


    public static String deleteTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/deleteOrderData";
    }


    public static String refundTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/order/returnOrderData";
    }


    public static String repayTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/beauty/v1/trade/repay";
    }


    public static String pay() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/scanPay";
    }


    public static String cardServiceInfo() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/cardtime";
    }


    public static String getCustomerExpense() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/expense";
    }


    public static String getCustomerDoc() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customerArchives/archivesList";
    }


    public static String getTask() {
        return ShopInfoManager.getInstance().getServerKey() + "/staff/taskRemind/list";
    }


    public static String saveCustomerDoc() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customerArchives/addArchives";
    }


    public static String saveTask() {
        return ShopInfoManager.getInstance().getServerKey() + "/staff/taskRemind/save";
    }


    public static String saveEditTask() {
        return ShopInfoManager.getInstance().getServerKey() + "/staff/taskRemind/finish";//任务完成接口，将修改放到save中
    }


    public static String getCustomerDocDetail() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customerArchives/queryById";
    }


    public static String findHandOverHistoryInfoApi() {
        return SwitchServerManager.getInstance().getServerKey()
                + "/beauty/v1/cashHandOver/findHistory";
    }


    public static String getHandOverInfoApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/beauty/v2/cashHandOver/stats";
    }


    public static String createHandOverInfoApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/beauty/v1/cashHandOver/create";
    }



    public static String doCloseDetail() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/closeDetail";
    }


    public static String doCloseBill() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/close";
    }


    public static String queryCloseHistoryList() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/list";
    }


    public static String queryCloseHandoverhistory() {
        return ShopInfoCfg.getInstance().getServerKey() + "/beauty/v1/bill/handover/result";
    }



    public static String cardServiceHistory() {
        return "card_getCardServiceHistory";
    }


    public static String getActivityBuyRecord() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/wxTradeData/queryByCustomer";
    }


    public static String getActivityByCode() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/wxTradeData/queryByCode";
    }


    public static String getBookingList() {
        return ShopInfoManager.getInstance().getServerKey() + "/staff/booking/list";
    }


    public static String bookingSubmit() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/addBookingData";
    }


    public static String bookingCheckUser() {
        return SwitchServerManager.getInstance().getServerKey() + "/v1/booking/check-user";
    }


    public static String bookingCancel() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/delBookingData";
    }


    public static String bookingOpenTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/updateBookingToOrder";
    }


    public static String updateBookingSubmit() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/updateBookingData";
    }


    public static String acceptOrRefuseBooking() {
        return SwitchServerManager.getInstance().getServerKey() + "/pos/api/booking/updateWxBookingStatus";
    }

}
