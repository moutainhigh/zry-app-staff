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

/**
 * 订单中心订单列表Presenter接口
 */

public interface IOrderCenterListPresenter {
    /**
     * 是否展示返回按钮
     *
     * @return true为展示；false为不展示
     */
    boolean showBackButton();

    /**
     * 是否展示菜单按钮
     *
     * @return true为展示；false为不展示
     */
    boolean showMenuButton();

    /**
     * 呼出侧边菜单栏
     */
    void openSideMenu();

    /**
     * 是否展示订单刷新按钮
     *
     * @return true为展示；false为不展示
     */
    boolean showOrderRefreshButton();

    /**
     * 呼出通知中心
     */
    void openNotifyCenter();

    /**
     * 是否展示通知中心提示
     *
     * @return true为展示；false为不展示
     */
    boolean showNotifyCenterTip();

    /**
     * presenter销毁，主要用于反注册掉一些东西
     */
    void destroy();

    /**
     * 语音叫号
     *
     * @param type      1=微信叫号,2=IVR叫号
     * @param tradeUuid 订单UUID
     * @param mobile    客户手机号
     * @param serialNo  订单流水号
     * @param tradeNo   订单号
     */
    void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo);

    /**
     * 批量清账
     *
     * @param tradeVos 需要清账的单据列表
     */
    void deliveryPayment(List<TradeVo> tradeVos);

    /**
     * 批量绑定配送员
     *
     * @param tradeVos 订单列表
     * @param authUser 配送员
     */
    void bindDeliveryUser(List<TradeVo> tradeVos, User authUser);

    /**
     * 加载数据
     */
    void loadData(int tab, FilterCondition condition, Trade trade);

    /**
     * 搜索
     *
     * @param tab      当前一级栏位
     * @param position 选中的item的位置
     * @param keyword  搜索关键字
     */
    void search(int tab, int position, String keyword, FilterCondition condition, Trade trade);

    /**
     * 查找订单总数
     */
    void countOrder();

    /**
     * 添加待处理tab
     *
     * @param processTab
     */
    void addProcessTab(List<Pair<String, Integer>> processTab);

    /**
     * 添加销货单tab
     *
     * @param childTabs
     */
    void addSaleNoteTab(List<Pair<String, Integer>> childTabs);

    //清账模式计算所选订单全部金额
    String calculateOrderAmount(List<TradeVo> tradeVoList);

    /**
     * 获取筛选的数据
     *
     * @return
     */
    List<FilterData> getFilterData();

    /**
     * 批量绑定配送员
     */
    void batchBindDeliveryUser();

    /**
     * 查询商家开通的配送平台
     *
     * @param tradeVos
     * @return
     */
    void showDeliveryPlatformChoose(List<TradeVo> tradeVos);

    void prepareDeliveryOrderDispatch(List<TradeVo> tradeVos, PartnerShopBiz partnerShopBiz);

    //批量下发配送
    void deliveryOrderDispatch(List<TradeVo> tradeVos, PartnerShopBiz partnerShopBiz, List<BatchDeliveryFee> deliveryFees);

    /**
     * v8.12.0
     * 显示口碑核销
     */
    boolean isShowKoubeiVerification();
}
