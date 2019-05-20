package com.zhongmei.bty.basemodule.commonbusiness.utils;

import com.zhongmei.bty.basemodule.commonbusiness.constants.Configure;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;

/**
 * @Date：2014年11月8日 下午2:25:20
 * @Description:
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class ServerAddressUtil {

    private static ServerAddressUtil instance;

    public static ServerAddressUtil getInstance() {
        if (instance == null) {
            instance = new ServerAddressUtil();
        }
        return instance;
    }

    /**
     * 获取远程服务器地址
     */
    public static String getRemoteHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com";
            case 2: //测试环境
                return "http://demo.com";
            case 3: //灰度环境
                return "http://demo.com";
            case 4: //CI环境
                return "http://demo.com";
            case 5://新加坡
                return "http://demo.com";
            default:
                return Configure.REMOTE_SERVER_HOST;
        }
    }

    /**
     * 获取当前设备配置的门店信息
     *
     * @return
     */
    public String getErpApi() {
        return ShopInfoManager.REMOTE_SERVER_HOST + String.format("/pos/api/sync/shop/info/%s", SystemUtils.getMacAddress());
    }

    /**
     * 获取同步接口地址
     *
     * @return
     */
    public String getOwns() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/sync/data";
    }

    /**
     * 会员登录接口地址(新接口）
     * 使用透传接口获取数据
     *
     * @return
     */
    public String customerLogin() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/login";
    }

    /**
     * 顾客列表接口
     * 使用透传接口获取数据
     *
     * @return
     */
    public String queryCustomerList() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer";
    }

    /**
     * 顾客 根据ID获取数据
     * 使用透传接口获取数据
     *
     * @return
     */
    public String getCustomerDetailById() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/info";
    }

    /**
     * 获取优惠卷列表
     *
     * @return
     */
    public String memberCoupons() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/coups";
    }

    /**
     * 获取会员储值记录
     *
     * @return
     */
    public String memberValuecardHistory() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/balance";
    }

    /**
     * 创建会员
     *
     * @return
     */
    public String createMemberByMobile() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/save";
    }

    /**
     * 会员绑定会员卡
     * @return
     */
    public String customerBindCard() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/ecard/bind";
    }

    /**
     * 获取门店实体卡/匿名卡储值记录列表
     *
     * @return
     */
    public String getEntityCardStoreHistory() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/trade/card_time";
    }


    /**
     * 获取门店会员储值记录列表
     *
     * @return
     */
    public String getMemberStoreHistory() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/trade/member_store";
    }

    /**
     * 更新订单支付状态
     *
     * @Title: refreshStateUrl
     * @Return String 返回类型
     */
    public String refreshStateUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/pos/api/pay/syncTradeStatus";
    }

    /**
     * 查询支付结果 -- 调用翼支付接口
     *
     * @return
     */
    public String getPayStatusOfThird() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/syncTradeStatus";
    }

    //获得支付结果第三方支付时，sync调用第三方查询支付结果
    public String getPayStateUrl() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/syncTradeStatus";
    }

    /**
     * 查询支付结果 -- 不调用翼支付接口
     *
     * @return
     */
    public String getPayStatus() {
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/pay/queryTradeStatus";
    }

    /**
     * 针对paymentItem退款
     */
    public String getRefundPayment(Long paymentItemId) {
        return ShopInfoManager.REMOTE_SERVER_HOST
                + String.format("/pos/api/pay/refund/%d", paymentItemId);
    }

    /**
     * 新会员积分流水
     */
    public String newQueryIntegralDetail() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/integral";
    }

    /**
     * @Title: getUpdateCheckApi
     * @Description: 升级检查的接口
     * @Return String 返回类型
     */
    public String getUpdateCheckApi() {
        return getRemoteHost() + "/pos/check/update?shopId="
                + ShopInfoCfg.getInstance().shopId + "&sys=android&v="
                + SystemUtils.getVersionCode();
    }

    /**
     * openId扫描url
     */
    public String getOpenIdUrl() {
        return "/pos/pay/login/code?shopId="
                + ShopInfoCfg.getInstance().shopId + "&deviceId="
                + SystemUtils.getMacAddress() + "&appType="
                + SystemUtils.getAppType() + "&uuid=";
    }

}
