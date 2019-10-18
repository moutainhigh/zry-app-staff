package com.zhongmei.bty.mobilepay.bean;

import android.content.Context;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.bty.basemodule.pay.enums.MobilePayMode;
import com.zhongmei.yunfu.db.enums.PayModeId;

import java.util.LinkedList;
import java.util.List;



public class MobliePayMenuTool {
    private Context mContext;
    public MobliePayMenuTool(Context context) {
        this.mContext = context;
    }

        public List<MobilePayMenuItem> getShowCodeMenus() {
        List<MobilePayMenuItem> menuList = new LinkedList<MobilePayMenuItem>();                if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE)) {
            List<MobilePaySetting> list = PaySettingCache.getMobilePaySettingHolder().getMobilePaySettingByMode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE);
            if (list.size() == 1) {
                MobilePaySetting mobilePaySetting = list.get(0);
                MobilePayMenuItem item = createMobilePayMenuItem(mobilePaySetting.getPayModeId());
                menuList.add(item);
            } else if (list.size() > 1) {
                if (PaySettingCache.isSupportOneCodePay()) {                    MobilePayMenuItem item = createMobilePayMenuItem(PayModeId.MOBILE_PAY);
                    for (MobilePaySetting mobilePaySetting : list) {
                        item.addChildMenu(createMobilePayMenuItem(mobilePaySetting.getPayModeId()));
                    }
                    item.menuName = this.mContext.getString(R.string.pay_cone_code_topay);
                    menuList.add(item);
                } else {                    for (MobilePaySetting mobilePaySetting : list) {
                        menuList.add(createMobilePayMenuItem(mobilePaySetting.getPayModeId()));
                    }
                }
            }
        }
                if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCANCODE)) {
            List<MobilePaySetting> list = PaySettingCache.getMobilePaySettingHolder().getMobilePaySettingByMode(MobilePayMode.PAY_MODE_SCANCODE);
            for (MobilePaySetting mobilePaySetting : list) {
                menuList.add(createMobilePayMenuItem(mobilePaySetting.getPayModeId()));
            }
        }

        return menuList;
    }

        public List<MobilePayMenuItem> getScanCodeMenus() {
        List<MobilePayMenuItem> menuList = new LinkedList<MobilePayMenuItem>();        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SHOWCODE)) {
            List<MobilePaySetting> list = PaySettingCache.getMobilePaySettingHolder().getMobilePaySettingByMode(MobilePayMode.PAY_MODE_SHOWCODE);
            for (MobilePaySetting mobilePaySetting : list) {
                menuList.add(createMobilePayMenuItem(mobilePaySetting.getPayModeId()));
            }
        }

        return menuList;
    }

    private MobilePayMenuItem createMobilePayMenuItem(PayModeId payModeId) {
        MobilePayMenuItem item = new MobilePayMenuItem();
        item.menuName = PaySettingCache.getPayModeNameByModeId(payModeId.value());
        item.payModeId = payModeId;
        item.payModeIcon = getMobilePayItemIcon(payModeId);
        return item;
    }

        private static int getMobilePayItemIcon(PayModeId payModeId) {
        switch (payModeId) {
            case WEIXIN_PAY:
                return R.drawable.mobile_pay_icon_weixin;

            case ALIPAY:
                return R.drawable.mobile_pay_icon_alipay;

            case ICBC_E_PAY:
                return R.drawable.mobile_pay_icon_epay;

            case UNIONPAY_CLOUD_PAY:
                return R.drawable.mobile_pay_icon_cloudpay;

            case DIANXIN_YIPAY:                return R.drawable.mobile_pay_icon_yipay;

            default:
                return 0;
        }
    }

        public static boolean isSetMobilePay() {
        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCANCODE)
                || PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SHOWCODE)
                || PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE)) {
            return true;
        }
        return false;
    }

        public static boolean isSetScanCode() {
        return PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SHOWCODE);
    }

        public static boolean isSetShowCode() {
        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCANCODE)
                || PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE)) {
            return true;
        }
        return false;
    }





}
