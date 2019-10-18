package com.zhongmei.bty.cashier.ordercenter.presenter;

import android.support.v4.util.Pair;

import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchDeliveryFee;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterCondition;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterData;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.List;



public interface IOrderCenterListPresenter {

    boolean showBackButton();


    boolean showMenuButton();


    void openSideMenu();


    boolean showOrderRefreshButton();


    void openNotifyCenter();


    boolean showNotifyCenterTip();


    void destroy();


    void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo);


    void deliveryPayment(List<TradeVo> tradeVos);


    void bindDeliveryUser(List<TradeVo> tradeVos, User authUser);


    void loadData(int tab, FilterCondition condition, Trade trade);


    void search(int tab, int position, String keyword, FilterCondition condition, Trade trade);


    void countOrder();


    void addProcessTab(List<Pair<String, Integer>> processTab);


    void addSaleNoteTab(List<Pair<String, Integer>> childTabs);

        String calculateOrderAmount(List<TradeVo> tradeVoList);


    List<FilterData> getFilterData();


    void batchBindDeliveryUser();


    void showDeliveryPlatformChoose(List<TradeVo> tradeVos);

    void prepareDeliveryOrderDispatch(List<TradeVo> tradeVos, PartnerShopBiz partnerShopBiz);

        void deliveryOrderDispatch(List<TradeVo> tradeVos, PartnerShopBiz partnerShopBiz, List<BatchDeliveryFee> deliveryFees);


    boolean isShowKoubeiVerification();
}
