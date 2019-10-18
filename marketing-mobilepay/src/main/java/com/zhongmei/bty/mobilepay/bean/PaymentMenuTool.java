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



public class PaymentMenuTool implements IPaymentMenuType {
    private Context mContext;    private int columns = 5;    private PayMethodItem mDefaultPayMethodItem;    private BusinessType mTradeBusinessType;    private boolean isbuildEmpty = true;    private boolean isThirdCoustomer = false;    private boolean isMemberPay = false;    private PayScene mPayScene;    private int defaultPaymentMenuType = -1;    private boolean isSupportLag = true;    private boolean isSupportOnline = true;    private boolean isSuportMobilePay = true;    private boolean isSupportYiPay = false;
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
        List<PayMethodItem> menuList = new ArrayList<>();                switch (this.mPayScene) {
            case SCENE_CODE_SHOP:            case SCENE_CODE_BUFFET_DEPOSIT:            case SCENE_CODE_BAKERY_BOOKING_DEPOSIT:
                menuList = buildShopMenus(this.mTradeBusinessType);
                break;

            case SCENE_CODE_WRITEOFF:
                menuList = buildWriteoffMenus();                break;

            case SCENE_CODE_BOOKING_DEPOSIT:                if (PaySettingCache.isSupportOneCodePay()) {
                    menuList = buildOneCodePayMenus();                } else {
                    menuList = buildCommonMenus();                }
                break;

            case SCENE_CODE_CHARGE:            default:
                menuList = buildCommonMenus();
                break;
        }
                Collections.sort(menuList, getOrderComparator());
                if (this.mDefaultPayMethodItem == null && !menuList.isEmpty()) {
            this.mDefaultPayMethodItem = menuList.get(0);
        }
                if (this.isbuildEmpty) {
            int mode = menuList.size() % this.columns;
            if (mode > 0) {
                menuList.addAll(buildEmptyMethod(this.columns - mode));
            }
        }
        return menuList;
    }

        private List<PayMethodItem> buildShopMenus(BusinessType businessType) {
        if (businessType != null) {
            switch (businessType) {
                case ENTITY_CARD_CHANGE:                     return buildChangeCardMenus();
                case DINNER:
                case BUFFET:
                case GROUP:
                case SNACK:
                case TAKEAWAY:
                    if (isSuportMobilePay) {
                        if (PaySettingCache.isSupportOneCodePay()) {
                            return buildOneCodePayMenus();                        } else {
                            return buildCommonMenus();                        }
                    } else {
                        return buildCommonMenus();                    }
                default:
                    return buildCommonMenus();            }

        } else {
            return buildCommonMenus();
        }
    }

        private List<PayMethodItem> buildCommonMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
        for (int i = 1; i <= PAY_MENU_MAX_SIZE; i++) {
            switch (i) {
                case PAY_MENU_TYPE_CASH:
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_CASH);
                    item.payModelGroup = PayModelGroup.CASH;
                    item.methodResId = R.drawable.pay_method_type_cash_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_cash);                     setPayMethodOrder(item, PAY_MENU_TYPE_CASH);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_CASH);                    setPayMethodEnable(item, !isMemberPay);                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_ALIPAY:
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_ALIPAY);
                    item.payModelGroup = PayModelGroup.ONLINE;
                    item.methodResId = R.drawable.pay_method_type_alipay_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_alipay);
                    ;                    setPayMethodOrder(item, PAY_MENU_TYPE_ALIPAY);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_ALIPAY);                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, !isMemberPay);                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_WEIXIN:
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_WEIXIN);
                    item.payModelGroup = PayModelGroup.ONLINE;
                    item.methodResId = R.drawable.pay_method_type_weixin_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_weixin);
                    ;                    setPayMethodOrder(item, PAY_MENU_TYPE_WEIXIN);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_WEIXIN);                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, !isMemberPay);                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_BAIDU:                                        if (mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                        continue;
                    }
                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.BAIFUBAO.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_BAIDU);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_type_baidy_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.BAIFUBAO.value());                        setPayMethodOrder(item, PAY_MENU_TYPE_BAIDU);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_BAIDU);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_UNION:
                {
                                        if (!PaySettingCache.isUnionpay() && mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                        continue;
                    }
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_UNION);
                    item.payModelGroup = PayModelGroup.BANK_CARD;
                    item.methodResId = R.drawable.pay_method_type_union_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_union);
                    setPayMethodOrder(item, PAY_MENU_TYPE_UNION);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_UNION);                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, !isMemberPay);                    menuList.add(item);
                }
                break;

                case PAY_MENU_TYPE_MEITUANCOUPON:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_TUANGOU.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEITUANCOUPON);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_coupon_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.MEITUAN_TUANGOU.value());                        setPayMethodOrder(item, PAY_MENU_TYPE_MEITUANCOUPON);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEITUANCOUPON);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MEITUANCASHPAY:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_FASTPAY.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEITUANCASHPAY);
                        item.payModelGroup = PayModelGroup.ONLINE;
                        item.methodResId = R.drawable.pay_method_type_shanhui_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.MEITUAN_FASTPAY.value());                        setPayMethodOrder(item, PAY_MENU_TYPE_MEITUANCASHPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEITUANCASHPAY);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_BAINUOCOUPON:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.BAINUO_TUANGOU.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_BAINUOCOUPON);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_bainuocoupon_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.BAINUO_TUANGOU.value());                        setPayMethodOrder(item, PAY_MENU_TYPE_BAINUOCOUPON);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_BAINUOCOUPON);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_MEMBER:
                {
                    PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_MEMBER);
                    item.payModelGroup = PayModelGroup.VALUE_CARD;
                    item.methodResId = R.drawable.pay_method_type_vip_selector;
                    item.methodName = mContext.getString(R.string.pay_mode_stored_val);
                    setPayMethodOrder(item, PAY_MENU_TYPE_MEMBER);
                    setDefaultPaymentMenu(item, PAY_MENU_TYPE_MEMBER);                    item.enabled = isSupportOnline;
                    setPayMethodEnable(item, isMemberPay);                    menuList.add(item);
                                                            if (isThirdCoustomer && isMemberPay) {
                        mDefaultPayMethodItem = item;
                    }
                                    }
                break;

                case PAY_MENU_TYPE_OTHERS:                     {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_OTHERS);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_other_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[5];
                        setPayMethodOrder(item, PAY_MENU_TYPE_OTHERS);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_OTHERS);                        setPayMethodEnable(item, !isMemberPay);                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_LAGPAY:                                        if (mPayScene == PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT) {
                        continue;
                    }
                    if (isSupportLag) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_LAGPAY);
                        item.payModelGroup = PayModelGroup.LAGPAY;
                        item.methodResId = R.drawable.pay_method_type_lag_selector;
                        item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[6];
                        setPayMethodOrder(item, PAY_MENU_TYPE_LAGPAY);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_LAGPAY);                        setPayMethodEnable(item, !isMemberPay);                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_JIN_CHENG:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_JIN_CHENG);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_jincheng_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.JIN_CHENG.value());                        setPayMethodOrder(item, PAY_MENU_TYPE_JIN_CHENG);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_JIN_CHENG);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG_VALUE_CARD.value())) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_jincheng_value_card_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.JIN_CHENG_VALUE_CARD.value());                        setPayMethodOrder(item, PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_FHXM:                    if (ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM) && PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.FENGHUO_WRISTBAND.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_FHXM);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_fenghuo_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.FENGHUO_WRISTBAND.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_FHXM);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_FHXM);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;
                case PAY_MENU_TYPE_KOUBEI:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.KOUBEI_TUANGOU.value()) && (mPayScene != PayScene.SCENE_CODE_BUFFET_DEPOSIT) && mPayScene != PayScene.SCENE_CODE_BOOKING_DEPOSIT && (mPayScene != PayScene.SCENE_CODE_BAKERY_BOOKING_DEPOSIT)) {
                        PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_KOUBEI);
                        item.payModelGroup = PayModelGroup.OTHER;
                        item.methodResId = R.drawable.pay_method_type_koubei_selector;
                        item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.KOUBEI_TUANGOU.value());
                        setPayMethodOrder(item, PAY_MENU_TYPE_KOUBEI);
                        setDefaultPaymentMenu(item, PAY_MENU_TYPE_KOUBEI);                        item.enabled = isSupportOnline;
                        setPayMethodEnable(item, !isMemberPay);
                        menuList.add(item);
                    }
                    break;

                case PAY_MENU_TYPE_UNIONPAY_CLOUD:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.UNIONPAY_CLOUD_PAY.value())) {
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

                case PAY_MENU_TYPE_ICBC_EPAY:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.ICBC_E_PAY.value())) {
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
                case PAY_MENU_TYPE_DX_YIPAY:                     if (isSupportYiPay && PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.DIANXIN_YIPAY.value())) {
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

        private List<PayMethodItem> buildWriteoffMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
                if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.CASH.value())) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_CASH);
            item.payModelGroup = PayModelGroup.CASH;
            item.methodResId = R.drawable.pay_method_type_cash_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_CASH);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_CASH);            setPayMethodEnable(item, !isMemberPay);            menuList.add(item);
        }
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
                if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.ALIPAY.value())) {            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_ALIPAY);
            item.payModelGroup = PayModelGroup.ONLINE;
            item.methodResId = R.drawable.pay_method_type_alipay_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.ALIPAY.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_ALIPAY);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_ALIPAY);            item.enabled = isSupportOnline;
            setPayMethodEnable(item, !isMemberPay);            menuList.add(item);
        }
                if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.WEIXIN_PAY.value())) {            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_WEIXIN);
            item.payModelGroup = PayModelGroup.ONLINE;
            item.methodResId = R.drawable.pay_method_type_weixin_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.WEIXIN_PAY.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_WEIXIN);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_WEIXIN);            item.enabled = isSupportOnline;
            setPayMethodEnable(item, !isMemberPay);            menuList.add(item);
        }

                if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.OTHER) && !BusinessTypeUtils.isRetail()) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_OTHERS);
            item.payModelGroup = PayModelGroup.OTHER;
            item.methodResId = R.drawable.pay_method_type_other_selector;
            item.methodName = mContext.getResources().getStringArray(R.array.cash_type)[5];
            setPayMethodOrder(item, PAY_MENU_TYPE_OTHERS);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_OTHERS);            setPayMethodEnable(item, !isMemberPay);            menuList.add(item);
        }
        return menuList;
    }

        private List<PayMethodItem> buildChangeCardMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
                if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.CASH.value())) {
            PayMethodItem item = new PayMethodItem(PAY_MENU_TYPE_CASH);
            item.payModelGroup = PayModelGroup.CASH;
            item.methodResId = R.drawable.pay_method_type_cash_selector;
            item.methodName = PaySettingCache.getPayModeNameByModeId(PayModeId.CASH.value());
            setPayMethodOrder(item, PAY_MENU_TYPE_CASH);
            setDefaultPaymentMenu(item, PAY_MENU_TYPE_CASH);            setPayMethodEnable(item, !isMemberPay);            menuList.add(item);
        }
        return menuList;
    }

        private List<PayMethodItem> buildOneCodePayMenus() {
        LinkedList<PayMethodItem> menuList = new LinkedList<PayMethodItem>();
        for (int i = 1; i <= PAY_MENU_MAX_SIZE; i++) {

            switch (i) {
                case PAY_MENU_TYPE_CASH:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.CASH.value())) {
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

                case PAY_MENU_TYPE_UNION:                    if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.BANK_CARD)) {
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

                case PAY_MENU_TYPE_MOBILE:                    if (MobliePayMenuTool.isSetMobilePay()) {
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
                case PAY_MENU_TYPE_DX_YIPAY:                     if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.DIANXIN_YIPAY.value())) {
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

                case PAY_MENU_TYPE_MEITUANCOUPON:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_TUANGOU.value())) {
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

                case PAY_MENU_TYPE_MEITUANCASHPAY:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.MEITUAN_FASTPAY.value())) {
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

                case PAY_MENU_TYPE_BAINUOCOUPON:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.BAINUO_TUANGOU.value())) {
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

                case PAY_MENU_TYPE_MEMBER:                    if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.VALUE_CARD)) {
                        if (ServerSettingCache.getInstance().isJinChBusiness() && this.mPayScene == PayScene.SCENE_CODE_CHARGE) {
                                                        continue;
                        }
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
                                            }
                    break;

                case PAY_MENU_TYPE_OTHERS:                    if (PaySettingCache.isSetPayModeGroup(this.mPayScene.value(), PayModelGroup.OTHER)) {
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

                case PAY_MENU_TYPE_LAGPAY:                    if (isSupportLag) {
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

                case PAY_MENU_TYPE_JIN_CHENG:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG.value())) {
                        if (ServerSettingCache.getInstance().isJinChBusiness()) {
                            if (this.mPayScene == PayScene.SCENE_CODE_CHARGE) {                                 continue;
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

                case PAY_MENU_TYPE_JIN_CHENG_VALUE_CARD:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.JIN_CHENG_VALUE_CARD.value())) {
                        if (ServerSettingCache.getInstance().isJinChBusiness()) {
                            if (this.mTradeBusinessType == BusinessType.ENTITY_CARD_CHANGE) {
                                                                continue;
                            }
                            if (this.mPayScene == PayScene.SCENE_CODE_SHOP) {
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

                case PAY_MENU_TYPE_FHXM:                    if (ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM) && PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.FENGHUO_WRISTBAND.value())) {
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
                case PAY_MENU_TYPE_KOUBEI:                    if (PaySettingCache.isErpModeID(this.mPayScene.value(), PayModeId.KOUBEI_TUANGOU.value())) {
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

        private void setDefaultPaymentMenu(PayMethodItem item, int payMenuType) {
        if (this.defaultPaymentMenuType > 0 && payMenuType == this.defaultPaymentMenuType) {
            this.mDefaultPayMethodItem = item;
        }
    }

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
