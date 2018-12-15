package com.zhongmei.bty.mobilepay.bean;

import android.content.Context;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.bty.basemodule.pay.enums.MobilePayMode;
import com.zhongmei.yunfu.db.enums.PayModeId;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 移动支付菜单工具
 */

public class MobliePayMenuTool {
    private Context mContext;//上下文

    public MobliePayMenuTool(Context context) {
        this.mContext = context;
    }

    //构建二维码支付方式菜单
    public List<MobilePayMenuItem> getShowCodeMenus() {
        List<MobilePayMenuItem> menuList = new LinkedList<MobilePayMenuItem>();//防止空指针，默认不为空格
        //构建支持一码付的二维码菜单（需要判断本地开关）
        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE)) {
            List<MobilePaySetting> list = PaySettingCache.getMobilePaySettingHolder().getMobilePaySettingByMode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE);
            if (list.size() == 1) {
                MobilePaySetting mobilePaySetting = list.get(0);
                MobilePayMenuItem item = createMobilePayMenuItem(mobilePaySetting.getPayModeId());
                menuList.add(item);
            } else if (list.size() > 1) {
                if (PaySettingCache.isSupportOneCodePay()) {//如果本地开启了一码付开关
                    MobilePayMenuItem item = createMobilePayMenuItem(PayModeId.MOBILE_PAY);
                    for (MobilePaySetting mobilePaySetting : list) {
                        item.addChildMenu(createMobilePayMenuItem(mobilePaySetting.getPayModeId()));
                    }
                    item.menuName = this.mContext.getString(R.string.pay_cone_code_topay);
                    menuList.add(item);
                } else {//如果不开启一码付就展开具体支付方式
                    for (MobilePaySetting mobilePaySetting : list) {
                        menuList.add(createMobilePayMenuItem(mobilePaySetting.getPayModeId()));
                    }
                }
            }
        }
        //构建不支持一码付的二维码菜单
        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCANCODE)) {
            List<MobilePaySetting> list = PaySettingCache.getMobilePaySettingHolder().getMobilePaySettingByMode(MobilePayMode.PAY_MODE_SCANCODE);
            for (MobilePaySetting mobilePaySetting : list) {
                menuList.add(createMobilePayMenuItem(mobilePaySetting.getPayModeId()));
            }
        }

        return menuList;
    }

    //构建被扫支付方式(扫码枪)菜单
    public List<MobilePayMenuItem> getScanCodeMenus() {
        List<MobilePayMenuItem> menuList = new LinkedList<MobilePayMenuItem>();//防止空指针，默认不为空格
        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SHOWCODE)) {
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

    //设置移动支付方式icon
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

            case DIANXIN_YIPAY://add v8.16 翼支付
                return R.drawable.mobile_pay_icon_yipay;

            default:
                return 0;
        }
    }

    //是否设置了移动支付
    public static boolean isSetMobilePay() {
        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCANCODE)
                || PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SHOWCODE)
                || PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE)) {
            return true;
        }
        return false;
    }

    //是否设置了主扫
    public static boolean isSetScanCode() {
        return PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SHOWCODE);
    }

    //是否设置了被扫
    public static boolean isSetShowCode() {
        if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCANCODE)
                || PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE)) {
            return true;
        }
        return false;
    }

    //生成移动支付副屏要的支付方式
    /*public static int getPayTypeByScanType(int payWay) {
        int payType = 0;

        switch (payWay) {
            case IPayConstParame.ONLIEN_SCAN_TYPE_ACTIVE://扫码枪 不需要判断是否开启一码付
                if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SHOWCODE)) {
                    List<MobilePaySetting> list = PaySettingCache.getMobilePaySettingHolder().getMobilePaySettingByMode(MobilePayMode.PAY_MODE_SHOWCODE);
                    for (MobilePaySetting mobilePaySetting : list) {
                        payType = payType | getPayTypeByMode(mobilePaySetting.getPayModeId());
                    }
                }
                break;

            case IPayConstParame.ONLIEN_SCAN_TYPE_UNACTIVE://二维码付款（需要判断一码付开关 ）
                if (PaySettingCache.getMobilePaySettingHolder().isContainsPayModeCode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE)) {
                    List<MobilePaySetting> list = PaySettingCache.getMobilePaySettingHolder().getMobilePaySettingByMode(MobilePayMode.PAY_MODE_SCAN_ONE_CODE);
                    for (MobilePaySetting mobilePaySetting : list) {
                        if (mobilePaySetting.isSupportOneCodePay()) {
                            payType = payType | getPayTypeByMode(mobilePaySetting.getPayModeId());
                        }
                    }
                }
                break;

            default:
                break;
        }

        return payType;
    }*/


    //生成支付副屏要的支付方式
    /*public static int getPayTypeByMode(PayModeId payModeId) {
        int payType = 0;
        switch (payModeId) {
            case WEIXIN_PAY:
                payType = PayMessage.PAY_TYPE_WEIXIN;
                break;
            case ALIPAY:
                payType = PayMessage.PAY_TYPE_ALIPAY;
                break;
            case BAIFUBAO:
                payType = PayMessage.PAY_TYPE_BAIFUBAO;
                break;
            case UNIONPAY_CLOUD_PAY:
                payType = PayMessage.PAY_TYPE_CLOUD_PAY;
                break;
            case ICBC_E_PAY:
                payType = PayMessage.PAY_TYPE_ICBC_E_PAY;
                break;
            default:
                break;
        }
        return payType;
    }*/
}
