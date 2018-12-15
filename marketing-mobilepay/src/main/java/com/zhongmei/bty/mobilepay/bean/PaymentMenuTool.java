package com.zhongmei.bty.mobilepay.bean;

import android.content.Context;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.utils.BusinessTypeUtils;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.bty.mobilepay.IPaymentMenuType;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class PaymentMenuTool implements IPaymentMenuType {
    private Context mContext;//上下文
    private int columns = 5;//列数
    private PayMethodItem mDefaultPayMethodItem;//默认支付方式
    private BusinessType mTradeBusinessType;//单据类别
    private boolean isbuildEmpty = true;//是否补空缺位
    private boolean isThirdCoustomer = false;//是否对接第3方储值支付
    private boolean isMemberPay = false;//是否储值支付
    private PayScene mPayScene;//支付场景
    private int defaultPaymentMenuType = -1;//默认支付菜单 add v8.4
    private boolean isSupportLag = true;//是否支持挂账 add v8.5
    private boolean isSupportOnline = true;//是否支持在线支付 add 8.9 for 异步版本
    private boolean isSuportMobilePay = true;//默认支持
    private boolean isSupportYiPay = false;//默认不支持电信翼支付

    private PaymentMenuTool() {
    }

    public PaymentMenuTool(Context context, BusinessType businessType) {
        this(context, PayScene.SCENE_CODE_SHOP, businessType);
    }

    public PaymentMenuTool(Context context, PayScene payScene, BusinessType businessType) {
        this.mContext = context;
        this.mPayScene = payScene;
        this.mTradeBusinessType = businessType;
    }

    public void setDefaultPaymentMenuType(int defaultPaymentMenuType) {
        this.defaultPaymentMenuType = defaultPaymentMenuType;
    }

    public void setSupportLag(boolean supportLag) {
        isSupportLag = supportLag;
    }

    public void setSupportOnline(boolean isSupportOnline) {
        this.isSupportOnline = isSupportOnline;
    }

    public void setSuportMobilePay(boolean suportMobilePay) {
        isSuportMobilePay = suportMobilePay;
    }

    public void setSupportYiPay(boolean supportYiPay) {
        isSupportYiPay = supportYiPay;
    }

    public void isBuildEmpty(boolean isbuildEmpty) {
        this.isbuildEmpty = isbuildEmpty;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public PayMethodItem getDefaultPayMethod() {
        return mDefaultPayMethodItem;
    }

    public void setThirdCoustomer(boolean isThirdCoustomer) {
        this.isThirdCoustomer = isThirdCoustomer;
    }

    public void setMemberPay(boolean memberPay) {
        isMemberPay = memberPay;
    }


    private void setPayMethodOrder(PayMethodItem payMethodItem, int payMenuType) {
        if (payMethodItem != null) {
            Integer order = PaySettingCache.getPayMenuOrderByMenuId(payMenuType);
            if (order == null) {
                payMethodItem.order = payMenuType;
            } else {
                payMethodItem.order = order;
            }
        }
    }

    private void setPayMethodEnable(PayMethodItem payMethodItem, boolean enable) {
        if (isThirdCoustomer) {
            payMethodItem.enabled = enable;
        }
    }

    public List<PayMethodItem> initMethodList() {
        List<PayMethodItem> menuList = new ArrayList<>();//防止空指针，默认不为空格
        //根据支付场景构建支付菜单
        switch (this.mPayScene) {
            case SCENE_CODE_SHOP://消费
            case SCENE_CODE_BUFFET_DEPOSIT://正餐自助交押金
            case SCENE_CODE_BAKERY_BOOKING_DEPOSIT:
                menuList = buildShopMenus(this.mTradeBusinessType);
                break;

            case SCENE_CODE_WRITEOFF:
                menuList = buildWriteoffMenus();//销账支付菜单
                break;

            case SCENE_CODE_BOOKING_DEPOSIT://交预订单
                if (PaySettingCache.isSupportOneCodePay()) {
                    menuList = buildOneCodePayMenus();//支持一码付
                } else {
                    menuList = buildCommonMenus();//不支持一码付
                }
                break;

            case SCENE_CODE_CHARGE://充值
            default:
                menuList = buildCommonMenus();
                break;
        }
        //先排序
        Collections.sort(menuList, getOrderComparator());
        //默认支付界面菜单
        if (this.mDefaultPayMethodItem == null && !menuList.isEmpty()) {
            this.mDefaultPayMethodItem = menuList.get(0);
        }
        //创建空菜单占位
        if (this.isbuildEmpty) {
            int mode = menuList.size() % this.columns;
            if (mode > 0) {
                menuList.addAll(buildEmptyMethod(this.columns - mode));
            }
        }
        return menuList;
    }

    //构建消费支付菜单
    private List<PayMethodItem> buildShopMenus(BusinessType businessType) {
        if (businessType != null) {
            switch (businessType) {
                case ENTITY_CARD_CHANGE: //当是换卡业务的时候，只支持现金支付
                    return buildChangeCardMenus();//换卡支付菜单

                case DINNER:
                case BUFFET:
                case GROUP:
                case SNACK:
                case TAKEAWAY:
                    if (isSuportMobilePay) {
                        if (PaySettingCache.isSupportOneCodePay()) {
                            return buildOneCodePayMenus();//支持一码付
                        } else {
                            return buildCommonMenus();//不支持一码付
                        }
                    } else {
                        return buildCommonMenus();//不支持一码付
                    }
                default:
                    return buildCommonMenus();//不支持一码付
            }

        } else {
            return buildCommonMenus();
        }
    }

    //构建通用支付菜单 add v8.11
    private List<PayMethodItem> buildCommonMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
        for (int i = 1; i <= PAY_MENU_MAX_SIZE; i++) {
            switch (i) {
                case PAY_MENU_TYPE_CASH://现金
                    /*if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.CASH.value()))*/
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_CASH);
                    item.payModelGroup = PayModelGroup.CASH;
                    item.methodResId = R.drawable.pay_method_type_cash_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_cash); //modify v8.11 mContext.getResources().getStringArray(R.array.trade_payment_mode)[2];
                    setPayMethodOrder(item, PAY_MENU_TYPE_CASH);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_CASH);//add v8.4
                    setPayMethodEnable(item, !isMemberPay);//add 20170411
                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_ALIPAY:// 支付宝
                    /*if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.ALIPAY.value()))*/
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_ALIPAY);
                    item.payModelGroup = PayModelGroup.ONLINE;
                    item.methodResId = R.drawable.pay_method_type_alipay_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_alipay);
                    ;//modify v8.11 mContext.getResources().getStringArray(R.array.trade_payment_mode)[5];
                    setPayMethodOrder(item, PAY_MENU_TYPE_ALIPAY);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_ALIPAY);//add v8.4
                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, !isMemberPay);//add 20170411
                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_WEIXIN://微信支付
                    /*if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.WEIXIN_PAY.value()))*/
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_WEIXIN);
                    item.payModelGroup = PayModelGroup.ONLINE;
                    item.methodResId = R.drawable.pay_method_type_weixin_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_weixin);
                    ;//modify v8.11  mContext.getResources().getStringArray(R.array.trade_payment_mode)[4];
                    setPayMethodOrder(item, PAY_MENU_TYPE_WEIXIN);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_WEIXIN);//add v8.4
                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, !isMemberPay);//add 20170411
                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_BAIDU:// 百度钱包
                    //烘焙押金不支持百度钱包
                    if (mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                        continue;
                    }
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.BAIFUBAO.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_BAIDU);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_type_baidy_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.BAIFUBAO.value());//modify v8.11 mContext.getResources().getStringArray(R.array.trade_payment_mode)[6];
                        setPayMethodOrder(item, PAY_MENU_TYPE_BAIDU);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_BAIDU);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);//add 20170411
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_UNION:// 银联
                    /*if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.BANK_CARD))*/
                {
                    //烘焙押金 ,不支持银行卡记账模式 add v8.15
                    if (!PaySettingCache.isUnionpay() && mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                        continue;
                    }
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_UNION);
                    item.payModelGroup = PayModelGroup.BANK_CARD;
                    item.methodResId = R.drawable.pay_method_type_union_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_union);
                    setPayMethodOrder(item, PAY_MENU_TYPE_UNION);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_UNION);//add v8.4
                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, !isMemberPay);//add 20170411
                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_MEITUANCOUPON:// 美团券
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_TUANGOU.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEITUANCOUPON);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_coupon_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.MEITUAN_TUANGOU.value());//modify v8.11 mContext.getResources().getStringArray(R.array.trade_payment_mode)[23];
                        setPayMethodOrder(item, PAY_MENU_TYPE_MEITUANCOUPON);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEITUANCOUPON);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);//add 20170411
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MEITUANCASHPAY://美团闪惠
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_FASTPAY.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEITUANCASHPAY);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_type_shanhui_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.MEITUAN_FASTPAY.value());//modify v8.11  mContext.getResources().getStringArray(R.array.trade_payment_mode)[22];
                        setPayMethodOrder(item, PAY_MENU_TYPE_MEITUANCASHPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEITUANCASHPAY);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);//add 20170411
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_BAINUOCOUPON:// 百度糯米券
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.BAINUO_TUANGOU.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_BAINUOCOUPON);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_bainuocoupon_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.BAINUO_TUANGOU.value());//modify v8.11   mContext.getResources().getStringArray(R.array.trade_payment_mode)[25];
                        setPayMethodOrder(item, PAY_MENU_TYPE_BAINUOCOUPON);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_BAINUOCOUPON);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);//add 20170411
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MEMBER://储值(正餐快餐外卖支持储值支付)
                    /*if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.VALUE_CARD) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT&& (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT))*/
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEMBER);
                    item.payModelGroup = PayModelGroup.VALUE_CARD;
                    item.methodResId = R.drawable.pay_method_type_vip_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_stored_val);
                    setPayMethodOrder(item, PAY_MENU_TYPE_MEMBER);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEMBER);//add v8.4
                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, isMemberPay);//add 20170411
                    menuList.add(item);
                    //modify 20170411 begin
                    //如果储值支付，默认选择储值
                    if (isThirdCoustomer && isMemberPay) {
                        mDefaultPayMethodItem = item;
                    }
                    //modify 20170411 end
                }
                break;

                case PAY_MENU_TYPE_OTHERS://其它
                    if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.OTHER)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_OTHERS);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_other_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[5];
                        setPayMethodOrder(item, PAY_MENU_TYPE_OTHERS);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_OTHERS);//add v8.4
                        setPayMethodEnable(item, !isMemberPay);//add 20170411
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_LAGPAY://挂账
                    //烘焙押金不支持挂账 add v8.15
                    if (mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                        continue;
                    }
                    if (isSupportLag) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_LAGPAY);
                        item.payModelGroup = PayModelGroup.LAGPAY;
                        item.methodResId = R.drawable.pay_method_type_lag_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[6];
                        setPayMethodOrder(item, PAY_MENU_TYPE_LAGPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_LAGPAY);//add v8.4
                        setPayMethodEnable(item, !isMemberPay);//add 20170411
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_JIN_CHENG://金诚支付
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_JIN_CHENG);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_jincheng_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.JIN_CHENG.value());//modify v8.11    mContext.getResources().getStringArray(R.array.trade_payment_mode)[26];
                        setPayMethodOrder(item, PAY_MENU_TYPE_JIN_CHENG);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_JIN_CHENG);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD://金诚储值卡支付
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG_VALUE_CARD.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_jincheng_value_card_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.JIN_CHENG_VALUE_CARD.value());//modify v8.11  mContext.getResources().getStringArray(R.array.trade_payment_mode)[27];
                        setPayMethodOrder(item, PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_FHXM://烽火手环支付
                    if (ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM) && PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.FENGHUO_WRISTBAND.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_FHXM);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_fenghuo_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.FENGHUO_WRISTBAND.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_FHXM);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_FHXM);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;
                case PAY_MENU_TYPE_KOUBEI://口碑券
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.KOUBEI_TUANGOU.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_KOUBEI);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_koubei_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.KOUBEI_TUANGOU.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_KOUBEI);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_KOUBEI);//add v8.4
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_UNIONPAY_CLOUD://银联云闪付 add v8.11
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.UNIONPAY_CLOUD_PAY.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_UNIONPAY_CLOUD);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_type_unionpay_cloud_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.UNIONPAY_CLOUD_PAY.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_UNIONPAY_CLOUD);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_UNIONPAY_CLOUD);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }

                    break;

                case PAY_MENU_TYPE_ICBC_EPAY://工商银行E支付 add v8.11
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.ICBC_E_PAY.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_ICBC_EPAY);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_icbc_epay_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.ICBC_E_PAY.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_ICBC_EPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_ICBC_EPAY);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }

                    break;
                case PAY_MENU_TYPE_DX_YIPAY: //翼支付 add v8.16
                    if (isSupportYiPay && PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.DIANXIN_YIPAY.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_DX_YIPAY);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_dx_yipay_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.DIANXIN_YIPAY.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_DX_YIPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_DX_YIPAY);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;
                default:
                    break;
            }
        }
        return menuList;
    }

    //构建会员销账支付菜单 add v8.11
    private List<PayMethodItem> buildWriteoffMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
        //添加现金销账
        if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.CASH.value())) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_CASH);
            item.payModelGroup = PayModelGroup.CASH;
            item.methodResId = R.drawable.pay_method_type_cash_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_CASH);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_CASH);//add v8.4
            setPayMethodEnable(item, !isMemberPay);//add 20170411
            menuList.add(item);
        }
        //添加虚拟会员支付菜单
        if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEMBER_CARD.value())) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEMBER);
            item.payModelGroup = PayModelGroup.VALUE_CARD;
            item.methodResId = R.drawable.pay_method_type_vip_selector;
            item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[2];
            setPayMethodOrder(item, PAY_MENU_TYPE_MEMBER);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEMBER);
            item.enabled = isSupportOnline;
            setPayMethodEnable(item, isMemberPay);
            menuList.add(item);
        }
        // 支付宝
        if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.ALIPAY.value())) {//add v8.13
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_ALIPAY);
            item.payModelGroup = PayModelGroup.ONLINE;
            item.methodResId = R.drawable.pay_method_type_alipay_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.ALIPAY.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_ALIPAY);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_ALIPAY);//add v8.4
            item.enabled = isSupportOnline;
            setPayMethodEnable(item, !isMemberPay);//add 20170411
            menuList.add(item);
        }
        //微信支付
        if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.WEIXIN_PAY.value())) {//add v8.13
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_WEIXIN);
            item.payModelGroup = PayModelGroup.ONLINE;
            item.methodResId = R.drawable.pay_method_type_weixin_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.WEIXIN_PAY.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_WEIXIN);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_WEIXIN);//add v8.4
            item.enabled = isSupportOnline;
            setPayMethodEnable(item, !isMemberPay);//add 20170411
            menuList.add(item);
        }

        //销账添加自订单支付菜单
        if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.OTHER) && !BusinessTypeUtils.isRetail()) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_OTHERS);
            item.payModelGroup = PayModelGroup.OTHER;
            item.methodResId = R.drawable.pay_method_type_other_selector;
            item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[5];
            setPayMethodOrder(item, PAY_MENU_TYPE_OTHERS);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_OTHERS);//add v8.4
            setPayMethodEnable(item, !isMemberPay);//add 20170411
            menuList.add(item);
        }
        return menuList;
    }

    //构建换卡支付菜单 add v8.12
    private List<PayMethodItem> buildChangeCardMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
        //换卡只支持现金
        if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.CASH.value())) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_CASH);
            item.payModelGroup = PayModelGroup.CASH;
            item.methodResId = R.drawable.pay_method_type_cash_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_CASH);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_CASH);//add v8.4
            setPayMethodEnable(item, !isMemberPay);//add 20170411
            menuList.add(item);
        }
        return menuList;
    }

    //构建一码支付主菜单（wallet 的支付统称移动支付） add v8.12 begin
    private List<PayMethodItem> buildOneCodePayMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
        for (int i = 1; i <= PAY_MENU_MAX_SIZE; i++) {

            switch (i) {
                case PAY_MENU_TYPE_CASH://现金
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.CASH.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_CASH);
                        item.payModelGroup = PayModelGroup.CASH;
                        item.methodResId = R.drawable.pay_method_type_cash_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_CASH);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_CASH);
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_UNION:// 银联
                    if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.BANK_CARD)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_UNION);
                        item.payModelGroup = PayModelGroup.BANK_CARD;
                        item.methodResId = R.drawable.pay_method_type_union_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[1];
                        setPayMethodOrder(item, PAY_MENU_TYPE_UNION);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_UNION);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MOBILE:// 移动支付，一码支付
                    if (MobliePayMenuTool.isSetMobilePay()) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MOBILE);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_type_mobilepay_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.MOBILE_PAY.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_MOBILE);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MOBILE);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;
                case PAY_MENU_TYPE_DX_YIPAY: //翼支付 add v8.16
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.DIANXIN_YIPAY.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_DX_YIPAY);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_dx_yipay_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.DIANXIN_YIPAY.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_DX_YIPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_DX_YIPAY);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MEITUANCOUPON:// 美团券
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_TUANGOU.value())) {
                        //交押金和交定金不支持
                        if (mPayScene == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BOOKING_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                            continue;
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEITUANCOUPON);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_coupon_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.MEITUAN_TUANGOU.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_MEITUANCOUPON);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEITUANCOUPON);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MEITUANCASHPAY://美团闪惠
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_FASTPAY.value())) {
                        //交押金和交定金不支持
                        if (mPayScene == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BOOKING_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                            continue;
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEITUANCASHPAY);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_type_shanhui_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.MEITUAN_FASTPAY.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_MEITUANCASHPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEITUANCASHPAY);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_BAINUOCOUPON:// 百度糯米券
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.BAINUO_TUANGOU.value())) {
                        //交押金和交定金不支持
                        if (mPayScene == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BOOKING_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                            continue;
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_BAINUOCOUPON);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_bainuocoupon_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.BAINUO_TUANGOU.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_BAINUOCOUPON);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_BAINUOCOUPON);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MEMBER://储值(正餐快餐外卖支持储值支付)
                    if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.VALUE_CARD)) {
                        if (ServerSettingCache.getInstance().isJinChBusiness() && this.mPayScene == PayScene.SCENE_CODE_CHARGE) {
                            //金诚充值不支持金诚App和储值
                            continue;
                        }
                        //交押金和交定金不支持
                        if (mPayScene == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                            continue;
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEMBER);
                        item.payModelGroup = PayModelGroup.VALUE_CARD;
                        item.methodResId = R.drawable.pay_method_type_vip_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[2];
                        setPayMethodOrder(item, PAY_MENU_TYPE_MEMBER);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEMBER);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, isMemberPay);
                        menuList.add(item);
                        if (isThirdCoustomer && isMemberPay) {
                            mDefaultPayMethodItem = item;
                        }
                        //modify 20170411 end
                    }
                    break;

                case PAY_MENU_TYPE_OTHERS://其它
                    if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.OTHER)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_OTHERS);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_other_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[5];
                        setPayMethodOrder(item, PAY_MENU_TYPE_OTHERS);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_OTHERS);
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_LAGPAY://挂账
                    if (isSupportLag) {
                        if (mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                            continue;
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_LAGPAY);
                        item.payModelGroup = PayModelGroup.LAGPAY;
                        item.methodResId = R.drawable.pay_method_type_lag_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[6];
                        setPayMethodOrder(item, PAY_MENU_TYPE_LAGPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_LAGPAY);
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_JIN_CHENG://金诚支付
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG.value())) {
                        if (ServerSettingCache.getInstance().isJinChBusiness()) {
                            if (this.mPayScene == PayScene.SCENE_CODE_CHARGE) { //金诚充值不支持金诚App和储值
                                continue;
                            }
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_JIN_CHENG);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_jincheng_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.JIN_CHENG.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_JIN_CHENG);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_JIN_CHENG);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD://金诚储值卡支付
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG_VALUE_CARD.value())) {
                        if (ServerSettingCache.getInstance().isJinChBusiness()) {
                            if (this.mTradeBusinessType == BusinessType.ENTITY_CARD_CHANGE) {
                                //金诚换卡不支持金诚储值卡
                                continue;
                            }
                            if (this.mPayScene == PayScene.SCENE_CODE_SHOP) {
                                //金诚消费不支持金诚充值卡
                                continue;
                            }
                        }

                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_jincheng_value_card_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.JIN_CHENG_VALUE_CARD.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_FHXM://烽火手环支付
                    if (ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM) && PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.FENGHUO_WRISTBAND.value())) {
                        //交押金和交定金不支持
                        if (mPayScene == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BOOKING_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                            continue;
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_FHXM);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_fenghuo_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.FENGHUO_WRISTBAND.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_FHXM);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_FHXM);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;
                case PAY_MENU_TYPE_KOUBEI://口碑券
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.KOUBEI_TUANGOU.value())) {
                        //交押金和交定金不支持
                        if (mPayScene == PayScene.SCENE_CODE_BUFFET_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BOOKING_DEPOSIT || mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                            continue;
                        }
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_KOUBEI);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_koubei_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.KOUBEI_TUANGOU.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_KOUBEI);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_KOUBEI);
                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;
                default:
                    break;
            }
        }
        return menuList;
    }
    //构建一码支付菜单（wallet 的支付统称移动支付） add v8.12 end

    //设置默认菜单
    private void setDefaultPaymentMenu(PayMethodItem item, int payMenuType) {
        if (this.defaultPaymentMenuType > 0 && payMenuType == this.defaultPaymentMenuType) {
            this.mDefaultPayMethodItem = item;
        }
    }

    //构建支付空菜单
    private List<PayMethodItem> buildEmptyMethod(int count) {
        List<PayMethodItem> menuList = new ArrayList<PayMethodItem>();
        for (int i = 1; i <= count; i++) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_EMPTY);
            item.payModelGroup = PayModelGroup.LAGPAY;
            item.methodResId = 0;
            item.methodName = " ";
            item.enabled = false;
            menuList.add(item);
        }

        return menuList;
    }

    private Comparator<PayMethodItem> getOrderComparator() {

        return new Comparator<PayMethodItem>() {
            @Override
            public int compare(PayMethodItem lhs, PayMethodItem rhs) {
                Integer lct = lhs.order;
                Integer rct = rhs.order;
                if (lct == null || rct == null) {
                    return 0;
                }
                if (lct > rct) {
                    return 1;
                }
                return -1;
            }
        };
    }
}
