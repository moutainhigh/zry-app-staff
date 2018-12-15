package com.zhongmei.bty.basemodule.commonbusiness.utils;

import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;

import com.zhongmei.bty.basemodule.commonbusiness.constants.Configure;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.ServerKey;
import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Date：2014年11月8日 下午2:25:20
 * @Description:
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class ServerAddressUtil extends ServerKey {
    private static final String TAG = ServerAddressUtil.class.getSimpleName();

    private static ServerAddressUtil instance;

    public String getOnStoreServerURL() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com/all";
            case 2: //测试环境
                return "http://demo.com/all";
            case 3: //灰度环境
                return "http://demo.com/all";
            case 4: //CI环境
                return "http://demo.com/all";
            default:
                return "http://demo.com/all";
        }
    }

    /*
     *获取远程服务器地址
     * */
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
     * PUSH服务器地址
     */
    public static String getPushHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1:
                return "demo.com";
            case 2:
                return "demo.com";
            case 3:
                return "demo.com";
            case 4:
                return "demo.com";
            case 5://新加坡
                return "demo.com";
            default:
                return Configure.PUSH_SERVER_HOST;
        }
    }

    /**
     * PUSH服务器端口
     */
    public static int getPushPort() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1:
                return 9001;
            case 2:
                return 9000;
            case 3:
                return 9000;
            case 4:
                return 9001;
            case 5://新加坡
                return 9001;
            default:
                return Configure.PUSH_SERVER_PORT;
        }
    }

    /**
     * 预结单熟客二维码的服务器地址
     */
    public static String getShuKeHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com";
            case 2: //测试环境
                return "http://demo.com";
            case 3: //灰度环境
                return "http://demo.com";
            case 4: //CI环境
                return "http://demo.com";
            default:
                return Configure.SHUKE_SERVER_HOST;
        }
    }


    /**
     * mind服务地址
     */
    public static String getMindHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com";
            case 2: //测试环境
                return "http://demo.com";
            case 3: //灰度环境
                return "http://demo.com";
            case 4: //CI环境
                return "http://demo.com";
            case 5: //新加坡
                return "http://demo.com";
            default:
                return Configure.MIND_SERVER_HOST;
        }
    }

    /**
     * 微信服务器地址
     */
    public static String getWeiXinHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com";
            case 2: //测试环境
                return "http://demo.com";
            case 3: //灰度环境
                return "http://demo.com";
//                return "http://demo.com";//7.7版本改成带点的地址
            case 4: //CI度环境
                return "http://demo.com";
            default:
                return Configure.WEIXIN_SERVER_HOST;
        }
    }

    /**
     * 公共业务组修改密码地址
     *
     * @return
     */
    public static String geAccountHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com";
            case 2: //测试环境
                return "http://demo.com";
            case 3: //灰度环境
                return "http://demo.com";
            case 4: //CI度环境
                return "http://demo.com";
            default:
                return Configure.ACCOUNT_SERVER_HOST;
        }
    }

    /**
     * 获取帮助中心地址
     *
     * @return
     */
    public static String getHelpURLHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1:
                return "http://demo.com";
            case 2: //测试环境
                return "http://demo.com";
            case 3: //灰度环境
                return "http://demo.com";
            case 4://CI
                return "http://demo.com";
            case 5://新加坡
                return "http://demo.com";
            default:
                return Configure.ERP_HELP_HOST;
        }
    }

    /**
     * 获取开发平台地址
     *
     * @return
     */
    public static String getOpenStoreURLHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
                return "http://demo.com/";
            case 2: //测试环境
                return "http://demo.com/";
            case 3: //灰度环境
                return "http://demo.com/";
            case 4: //CI环境
                return "http://demo.com/";
            default:
                return Configure.OPEN_STORE_HOST;
        }
    }

    /**
     * 获取Portal地址
     *
     * @return
     */
    public static String getPortalURLHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1: //开发环境
            case 2: //测试环境
            case 4: //CI环境
                return "http://demo.com/#/";
            case 3: //灰度环境
                return "http://demo.com/#/";
            default:
                return Configure.PORTAL_SERVER_HOST;
        }
    }

    /**
     * 判断域名是否开发平台网址
     *
     * @param url
     * @return
     */
    public static boolean hasOpenStoreURLHost(String url) {
        if (URLUtil.isNetworkUrl(url)) {
            Uri uri = Uri.parse(url);
            return "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost())
                    || "demo.com".equals(uri.getHost());
        }
        return false;
    }

    /*public static Map<String, String> tokenEncrypt(String shopId, String token) {
        return tokenEncrypt(shopId, token, System.currentTimeMillis());
    }

    *//**
     * api安全验证数据信息加密信息设置
     * SHA-256(kry-api-token=登录认证token&kry-api-shop-id=门店ID^512&kry-api-timestamp=客户端时间戳)
     *//*
    public static Map<String, String> tokenEncrypt(String shopId, String token, long currentTimeMillis) {
        Map<String, String> tempMap = new HashMap<>();
        if (!TextUtils.isEmpty(shopId) && !TextUtils.isEmpty(token)) {
            Long shopIdEncrypt = Long.parseLong(shopId) ^ 512;
            String curTime = currentTimeMillis + "";
            String encryptData = "kry-api-token=" + token + "&kry-api-shop-id=" + shopIdEncrypt + "&kry-api-timestamp=" + curTime;
            String securityEncrypt = EncryptUtils.encrypt(encryptData, "SHA-256");
            tempMap.put("token", securityEncrypt);
            tempMap.put("time", curTime);
        }

        return tempMap;
    }*/

    /**
     * <pre>
     * 参数说明:@return
     * 返回类型:@return ServerAddressUtil
     * 方法说明:单例模式
     * 修改历史:
     *    修改人员:zctsky 修改日期:May 20, 2014 11:27:29 AM
     *    修改目的:
     * </pre>
     */
    public static ServerAddressUtil getInstance() {
        if (instance == null) {
            instance = new ServerAddressUtil();
        }
        return instance;
    }

    /**
     * <pre>
     * 参数说明:@return
     * 返回类型:@return String
     * 方法说明:获取同步接口的地址
     * 修改历史:
     *    修改人员:zctsky 修改日期:May 20, 2014 10:54:04 AM
     *    修改目的:
     * </pre>
     */
    public String getSyncApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/SynController/sysInfoPost?debug=open";
    }

    /**
     * 获取当前设备配置的门店信息
     *
     * @return
     */
    public String getErpApi() {
        /*return getRemoteHost() + "/version/checkSyncWithPrinter?ov="
                + SystemUtils.getVersionCode() + "&mac=" + SystemUtils.getMacAddress()
                + "&p=" + SystemUtils.getSystemType() + "&app_type=5";*/
        //return ShopInfoManager.getInstance().getServerKey() + String.format("/pos/shop/info/%s", SystemUtils.getMacAddress());
        return ShopInfoManager.REMOTE_SERVER_HOST + String.format("/pos/api/sync/shop/info/%s", SystemUtils.getMacAddress());
    }

    /**
     * 获取指定门店的信息
     *
     * @return
     */
    public String getErpShopInfo() {
        return getRemoteHost() + "/os/api/queryCommercialBrandById?commercialId=";
    }

    /**
     * 获取短信验证码地址
     *
     * @return
     */
    public String createTokenByPhoneNumberUrl() {
        return getRemoteHost() + "/os/api/createMsgToken";
    }

    /**
     * 验证码检查地址
     *
     * @return
     */
    public String getCheckPhoneTokenUrl() {
        return getRemoteHost() + "/os/api/checkMsgToken";
    }

    /**
     * 获取同步接口地址
     *
     * @return
     */
    public String getOwns() {
        //return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/sync/owns";
        return ShopInfoManager.REMOTE_SERVER_HOST + "/pos/api/sync/data";
    }

    /**
     * @Title: getUpdateCheckApi
     * @Description: 升级检查的接口
     * @Return String 返回类型
     */
    public String getUpdateCheckApi() {
        return getRemoteHost() + "/version/checkVersion?cid="
                + ShopInfoCfg.getInstance().shopId + "&p=android&ov="
                + SystemUtils.getVersionCode();
    }

    public String getPrintUpdateCheckApi() {
        try {
            return getRemoteHost() + "/version/checkVersion?cid="
                    + ShopInfoCfg.getInstance().shopId + "&p=android&ov="
                    + SystemUtils.getVersionCode(Constant.PRINT_SERVICE_PACKAGE_NAME) + "&app_type=3";
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public String getUpdateMemberApi(JSONObject json) {

        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put("name", "zct");
            requestJson.put("sex", "1");
            requestJson.put("phone", "18612708182");
            requestJson.put("birthday", "1980-01-05");
            requestJson.put("isAcceptSubscription", "1");
            requestJson.put("commercialGroupId", "2192");
            requestJson.put("oldPhone", "15863952638");
            requestJson.put("customerSyncFlag", "535EF08C13CF46C7AC773D41C0107772");
            // 加入 操作者ID
            requestJson.put("userId", "c215184aac4048639ad1938c208e130a");
        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/memberController/upgradeMember?&content=" + requestJson.toString() + "&versionCode="
                + SystemUtils.getVersionCode() + "&versionName=4.0.5" + "&macAddress="
                + SystemUtils.getMacAddress() + "&shopID="
                + ShopInfoCfg.getInstance().shopId;
    }

    /**
     * 交接统计查询
     *
     * @return
     */
    public String getHandOverInfoApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/cashHandOver/stats";
    }

    /**
     * 交接创建
     *
     * @return
     */
    public String createHandOverInfoApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/cashHandOver/create";
    }

    /**
     * 交接历史查询
     *
     * @return
     */
    public String findHandOverHistoryInfoApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/cashHandOver/findHistory";
    }

    /*public String updateClearStatusApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/dish/clearStatus/update";
    }*/

    public String orderAndCash() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/orderall/orderAndCharge";
    }

    public String getWXPayStatus() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/orderall/orderStatus";
    }

    /**
     * http上传地址
     *
     * @return
     */
    public String getLogAddApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/readStreamController/readStream?debug=open";
    }

    /**
     * 七牛上传地址
     *
     * @return
     */
    public String getQiniuUploadApi() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/readStreamController/readStream?debug=open";
    }

    /**
     * 下单接口地址
     *
     * @return
     */
    public String tradeInsert() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/submit";
    }

    /**
     * 自助餐无桌下单接口地址
     *
     * @return
     */
    public String buffetNoTabletradeInsert() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/buffet/submitWithoutTable";
    }

    /**
     * 收银接口地址
     *
     * @return
     */
    public String tradePay() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/payment/submit";
    }

    /**
     * 调账接口地址
     *
     * @return
     */
    public String adjustPay() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/payment/adjust";
    }

    /**
     * 改单并收银地址
     *
     * @return
     */
    public String modifyAndPay() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/modify+cash";
    }

    /**
     * 对反结账产生的新单进行收银的地址
     *
     * @return
     */
    public String modifyAndPayOfRepeat() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/repay4cash";
    }

    /**
     * 下单并收银接口地址
     *
     * @return
     */
    public String tradeInsertAndPay() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/create+cash";
    }

    /**
     * 无单退货
     *
     * @return
     */
    public String salesReturnUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/return+refund";
    }

    /**
     * 获取微信支付URL接口地址
     *
     * @return
     */
    public String wechatPayUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/createScanCodePayUrl";
    }

    /**
     * 获取微信被扫支付接口地址
     *
     * @return
     */
    public String wechatPay() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/creditCard";
    }

    /**
     * 验证单据支付状态接口地址
     *
     * @return
     */
    public String verifyPay() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/getTradeIfPaid";
    }

    /**
     * 接受订单接口地址
     *
     * @return
     */
    public String tradeAccept() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/takeaway/trade/receive";
    }

    /**
     * 批量接受订单接口地址
     *
     * @return
     */
    public String tradeReceiveBatch() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/trade/receive/batch";
    }

    /**
     * 拒绝订单接口地址
     *
     * @return
     */
    public String tradeRefuse() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/refuse";
    }

    /**
     * 批量拒绝订单接口地址
     *
     * @return
     */
    public String tradeRefuseBatch() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/trade/refuse/batch";
    }

    /**
     * 接受正餐订单接口地址
     *
     * @return
     */
    public String dinnerAccept() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/dinnerReceive";
    }

    /**
     * 拒绝正餐订单接口地址
     *
     * @return
     */
    public String dinnerRefuse() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/dinnerRefuse";
    }

    /**
     * 作废交易接口地址
     *
     * @return
     */
    public String tradeRecision() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/cancel";
    }

    /**
     * 退货接口地址
     *
     * @return
     */
    public String tradeRefund() {
        // return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/return";
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v3/onos/refund/submit";
    }

    public String editCustomer() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/customer/update";
    }

    /**
     * 会员登录接口地址
     *
     * @return
     */
    public String memberLogin() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/member/login";
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
     * 单卡查询接口
     * 使用透传接口获取数据
     * <p>
     *
     * @return
     */
    public String getSingleSearchCardsUrlByTrans() {
        return "v2_card_selectByCardInstancePage";
    }

    /**
     * 即售即用实体卡绑定已有顾客接口
     * <p>
     *
     * @return
     */
    public String getBindCardInstance() {
        return "cardInstance_bindCardInstance";
    }

    /**
     * 实体卡绑定会员接口
     * <p>
     *
     * @return
     */
    public String getCardBindCardInstance() {
        return "v2_card_bindCardInstance";
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
     * 修改会员等级
     */
    public String memberModifyLevel() {
        return "v2_customerLevel_update";
    }

    /**
     * 验证会员密码
     * get
     */
    public String verifyMemberPassword() {
        return "customer_validationMemberPwd";
    }

    /**
     * 修改会员手机号
     */
    public String memberModifyMobile() {
        return "v2_member_updateMobile";
    }

    /**
     * 查询挂账销账记录
     */
    public String memberCreditList() {
        return "v2_credit_queryCustomerCreditList";
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
     * 获取会员实体卡列表
     *
     * @return
     */
    public String memberCards() {
        return "v2_card_getCustomerCards";
    }

    /**
     * 获取微信卡券详情
     */
    public String getCoupInstanceByWxCardNumber() {
        return "coupInstance_getCoupInstanceByWxCardNumber";
    }

    /**
     * 会员充值接口地址
     *
     * @return
     */
    public String memberRecharge() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/member/addMemberValueCard";
    }


    /**
     * 实体卡充值接口地址
     * /v1/entitycard/addMoney
     *
     * @return
     */
    public String cardRecharge() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/store";
    }

    /**
     * 会员验证码获取地址
     *
     * @return
     */
    public String memberCheckCode() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/member/getCheckCode";
    }

    /**
     * 会员验证码校验地址
     *
     * @return
     */
    public String memberValidateCheckCode() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/member/validateCheckCode";
    }

    /**
     * 会员修改密码地址
     *
     * @return
     */
    public String memberModifyPswd() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/member/modifyPassword";
    }

    /**
     * 获取会员储值记录
     *
     * @return
     */
    public String memberValuecardHistory() {
        return ShopInfoManager.getInstance().getServerKey()
                + "/pos/customer/balance";
    }

    /**
     * 修改商户打印服务器ip接口地址
     *
     * @return
     */
    public String modifyPrintServerIp() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/printer/modifyPrintServerIp";
    }

    /**
     * 更新票据样式接口地址
     *
     * @return
     */
    public String modifyPrinterStyleSetting() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/printer/updatePrinterStyleSetting";
    }

    /**
     * 创建更新打印机接口地址
     *
     * @return
     */
    public String storePrinter() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/printer/device/add_or_modify";
    }

    /**
     * 删除多个打印机接口地址
     *
     * @return
     */
    public String removePrinter() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/printer/device/delete";
    }

    /**
     * 批量清账接口地址
     *
     * @return
     */
    public String deliveredPayment() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/payment/delivered";
    }

    /**
     * 正餐批量清账接口地址
     *
     * @return
     */
    public String clearAccounts() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/clearAccounts";
    }

    /**
     * 拒绝预定
     *
     * @return
     */
    public String bookingRefuse() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/booking/refuse-booking";
    }

    /**
     * 接受预订接口
     *
     * @return
     */
    public String bookingAccept() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/booking/accept-booking";
    }

    /**
     * 创建或修改预订
     *
     * @return
     */
    public String creatOrUpdateBooking() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/booking/create+update";
    }

    public String bookingCancel() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/booking/cancel";
    }

    /*第二版预订：8.0.0版本-8.7.0
    public String createBooking() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/booking/submitBooking";
    }*/

    public String createBooking() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/booking/submitBooking";
    }

    /*第二版预订：8.0.0版本-8.7.0
    public String updateBooking() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/booking/updateBooking";
    }*/
    public String updateBooking() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/booking/updateBooking";
    }

    public String bookingArrivalShop() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/booking/arrivalShop";
    }

    /**
     * 发送短信
     *
     * @return
     */
    public String sendBookingMessage() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/booking/remind/sms";
    }

    /**
     * 预订统计
     *
     * @return
     */
    public String bookingRecorde() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/statistics/memberConsumeInfo";
    }

    /**
     * 预定查询接口: 预定台桌列表(时段)
     *
     * @return
     */
    public String bookingTableListByTime() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/doom/booking/table/list";
    }

    /**
     * 预订查询
     * b
     *
     * @return
     */
    public String bookingList() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/doom/booking/list";
    }

    /**
     * 搜索预订
     *
     * @return
     */
    public String bookingQuery() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/doom/booking/query";
    }

    /**
     * 预订搜索接口支持分页
     *
     * @return
     */
    public String bookingQueryV1() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/booking/query";
    }

    public String bookingQueryNum() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/booking/queryBookingNum";
    }

    /**
     * 预定详情
     */
    public String bookingDetail() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/doom/booking/details";
    }

    /**
     * 预订确认到店接口
     */
    public String bookingConfirm() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/booking/confirm";
    }

    /**
     * 正餐预定开台
     *
     * @return
     */
    public String toDinnerSubmit() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/booking/toDinnerSubmit";
    }

    public String toUnionTable() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/booking/to-union-table";
    }

    /**
     * 开始营业与歇业接口地址
     *
     * @return
     */
    public String openAndCloseBusiness() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/weixin/setting/startup";
    }

    /**
     * 通用门店设置
     *
     * @return
     */
    public String commercialSettings() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/commercial/settings";
    }

    public String tableNumberSetting() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/setTableNumberSetting";
    }

    /**
     * 估清或取消估清菜品接口地址
     *
     * @return
     */
    public String clearStatus() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/dishShop/clearStatus";
    }

    public String customerInfo() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/member/valuecardIntegralCoupons";
    }

    /**
     * 订单批量解绑优惠券接口
     *
     * @return
     */
    public String batchunbindCoupon() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/batchunbindCoupon";
    }

    public String unbindCoupon() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/unbindCoupon";
    }

    public String unbindIntegral() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/unbindIntegral";
    }

    public String sendCouponUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/coupon/getSendCouponUrl";
    }

    /**
     * 排队短信
     *
     * @return
     */
    public String queueMessage() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/queueController/queueSmsPost";
    }

    /**
     * 排队自助语音
     *
     * @return
     */
    public String queuePhone() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/queueController/queuePhonePost";
    }

    /**
     * 排队叫号
     *
     * @return
     */
    public String queueCallNumber() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/queue/enter/call_number";
    }

    public String queueRevocationCall() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/queue/recoverInvalid";
    }

    public String queueModify() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/queue/modify";
    }

    public String predictWaitTime() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/queue/predictWaitTime";

    }

    public String queryQTCode() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/queue/queryQTCode";

    }

    /**
     * 下单接口地址-正餐
     *
     * @return
     */
    public String tradeInsertDinner() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/dinnerSubmit";
    }

    /**
     * 下单接口地址-自助餐
     *
     * @return
     */
    public String tradeInsertBuffet() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/buffet/submit";
    }

//    /**
//     * 改单接口地址
//     *
//     * @return
//     */
//    public String tradeModifyDinner() {
//        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/modify";
//    }

    /**
     * 正餐接受微信加菜地址
     *
     * @return
     */
    public String acceptAddItem() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/wx_add_item/accept_weixin_additem";
    }


    /**
     * 正餐拒绝微信加菜地址
     *
     * @return
     */
    public String refuseAddItem() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/wx_add_item/refuse_weixin_additem";
    }


    /**
     * 自助激活接口
     *
     * @return
     */
    public String activation() {
        return getRemoteHost() + "/os/api/autoActivation";
    }

    /**
     * 正餐清台接口地址
     *
     * @return
     */
    public String clearDinnertable() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/tradeTable/clear";
    }

    /**
     * 正餐换桌接口地址
     *
     * @return
     */
    public String transferDinnertable() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/tradeTable/exchange";
    }

    /**
     * 正餐作废接口地址
     *
     * @return
     */
    public String recisionDinner() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/trade/dinnerDelete";
    }

    /**
     * 正餐退货接口地址
     *
     * @return
     */
    public String tradeRefundDinner() {
//		return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/trade/return";
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v3/onos/refund/submit";
    }

    /**
     * 正餐合单接口地址
     *
     * @return
     */
    public String mergeDinner() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/trade/unionTrade";
    }

    /**
     * 修改正餐菜品出单状态接口地址
     *
     * @return
     */
    public String changePrintStatus() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/item/issueStatus/change";
    }

    /**
     * @Title: weixinRefund
     * @Description: 微信退款
     * @Param @return
     * @Return String 返回类型
     */
    public String weixinRefundUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/weixin/refund";
    }

    /**
     * @Title: upgradeMemberUrl
     * @Description: 会员升级新接口地址
     * @Param @return
     * @Return String 返回类型
     */
    public String upgradeMemberUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/member/upgradeMember";
    }

    /**
     * @Title: tradeSplitUrl
     * @Description: 正餐拆单地址
     * @Return String 返回类型
     */
    public String tradeSplitUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/split";

    }

    /**
     * @Title: tradeSplitUrl
     * @Description: 正餐拆单及收银地址
     * @Param @return
     * @Return String 返回类型
     */
    public String tradeSplitPayUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/split+cash";
    }

    public String tradeRepayUrl() {
        //return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/repay";
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v3/onos/trade/repay";
    }

    /**
     * 语音通知url
     *
     * @return
     */
    public String callDishNotifyUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/callDish/notify";
    }

    /**
     * 交接清零设置
     *
     * @return
     */
    public String handoverCleanSet() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/cashHandOver/handoverCon";
    }

    /**
     * @return
     * @Date 2015年11月23日
     * @Description: 修改上菜状态
     * @Return String
     */
    public String modifyServiceDish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/batserving";
    }

    /**
     * 划菜/取消划菜
     *
     * @return
     */
    public String modifyServiceDishV2() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/trade/batserving";
    }

    /**
     * @Title: doCloseBill
     * @Description: 执行关账 url
     * @Param @return
     * @Return String 返回类型
     */
    public String doCloseBill() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/bill/close";
    }

    /**
     * 查询关账历史
     *
     * @Title: queryCloseHistoryList
     * @Description:
     * @Param @return TODO
     * @Return String 返回类型
     */
    public String queryCloseHistoryList() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/bill/list";
    }

    /**
     * @Return String 返回类型
     */
    public String doCloseDetail() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/bill/closeDetail";
    }

    /**
     * 查询关账交接记录
     *
     * @Title: queryCloseHandoverhistory
     * @Description: TODO
     * @Param @return TODO
     * @Return String 返回类型
     */
    public String queryCloseHandoverhistory() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/bill/handover/result";
    }

    /**
     * 获取会员积分记录新接口地址
     *
     * @return
     */
    public String memberIntegralHistory() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/member/memberIntegralHistory";
    }

    /**
     * 获取实体卡积分记录新接口地址
     *
     * @return
     */
    public String cardIntegralHistory() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/scorerecord";
    }

    /**
     * 查询单表数据
     *
     * @return
     */
    public String queryModuleItem() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/queryModuleItem";
    }

    /**
     * 修改银联退款状态
     *
     * @Title: changeUnionpayRefundStatus
     * @Return String 返回类型
     */
    public String changeRefundStatus() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/payment/changeRefundStatus";
    }

    /**
     * 修改银联退款状态:ps：7.6版本调整为新接口，未支持组合支付退款，该接口调用时不需要上行订单的支付状态，订单支付状态有服务端进判断
     *
     * @return
     */
    public String newChangeRefundStatus() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/onos/refund/unionpay/changeRefundStatus";
    }


    /**
     * 实体卡登录
     *
     * @Title: cardLogin
     * @Return String 返回类型
     */
    public String cardLogin() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/cardInstanceInfo";
    }

    /**
     * 创建会员
     *
     * @return
     */
    public String creatMemeberUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/preset/customer/createMember";
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
     * 通过预制顾客创建会员
     *
     * @return
     */
    public String createMemberByPresetCustomer() {
        return "v2_member_createMemberByPresetCustomer";
    }


    /**
     * 查询会员卡
     *
     * @return
     */
    public String getMemeberCardUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/getCardByMember";
    }

    /**
     * 修改打印记录表的打印状态
     *
     * @Title: updatePrinted
     * @Return String 返回类型
     */
    public String updatePrinted() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/on_mobile/trade/updateprinted";
    }

    /**
     * @Title: getRangeSearchCardsUrl
     * @Description: 根据卡范围查询卡接口地址
     */
    public String getRangeSearchCardsUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/getCardListByNum";
    }

    /**
     * @Title: getSingleSearchCardsUrl
     * @Description: 根据卡范围查询卡接口地址
     */
    public String getSingleSearchCardsUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/getByCardNoAndKind";
    }

    /**
     * @Description: 激活实体卡接口地址
     */
    public String getActiviteCardsUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/activeCardInstance";
    }

    /**
     * 查询旧会员
     *
     * @return
     */
    public String queueMemeberUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/preset/customer/queryInfoByMemberCard";
    }

    /**
     * 查询售卡记录
     *
     * @return
     */
    public String getCustomerSellCardOrders() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/queryAllByShop";
    }

    /**
     * 查询实体卡（会员卡、权益卡）换卡记录地址
     */
    public String getEntityCardChangeOrders() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/cardchange/queryAllByShop";
    }

    /**
     * 查询售卡详情
     *
     * @return
     */
    public String getCustomerSellCardDetailInfo() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/queryTradeItemCard";
    }

    /**
     * 查询实体卡（会员卡、权益卡）换卡详细信息
     */
    public String getEntityCardChangeDetailInfo() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/cardchange/queryTradeItemCard";
    }

    /**
     * 查询匿名实体卡售卡详情
     *
     * @return
     */
    public String getAnonymousEntityCardSellDetailInfo() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/tmpcard/queryTradeById";
    }

    /**
     * @Description: 售实体卡下单接口地址
     */
    public String getSellCardsInsertUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/sale";
    }

    /**
     * @Description: 售实体卡收银接口地址
     */
    public String getSellCardsPayUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/cardpayment";
    }

    /**
     * @Description: 售实体卡下单及收银接口地址
     */
    public String getSellCardsInsertAndPayUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/entitycard/cardsaleAndpayment";
    }

    /**
     * 查询实体卡储值记录
     *
     * @return
     */
    public String getCardStoreValueList() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/queryStoreRecords";
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
     * 查询实体卡储值记录详情
     *
     * @return
     */
    public String queryPaymentAndMember() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/queryPaymentAndMember";
    }

    /**
     * 根据paymentUuid查询订单相关信息
     *
     * @return
     */
    public String queryPayment() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/tmpcard/queryPaymentByUuid";
    }

    /**
     * 查询会员储值记录
     *
     * @return
     */
    public String getMemberStoreValueList() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/member/valuecardHistoryNoMobile";
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
     * @Description: 临时卡售卡下单地址
     */
    public String getInsertAnonymousCardsUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/tmpcard/sale";
    }

    /**
     * @Description: 临时卡售卡收银地址
     */
    public String getSellAnonymousCardsPayUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/tmpcard/sale4payment";
    }

    /**
     * @Description: 临时卡售卡下单并收银地址
     */
    public String getAnonymousCardsinsertAndPayUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/tmpcard/saleAndPayment";
    }

    /**
     * @Description: 临时卡售卡充值收银接口地址
     */
    public String getAnonymousCardSaleAndStoreUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/tmpcard/saleAndStore";
    }

    /**
     * @Description: 临时卡充值收银接口地址
     */
    public String getAnonymousCardStoreUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/tmpcard/store";
    }

    /**
     * @Description: 会员实体卡充值收银接口地址
     */
    public String getEntityCardStoreUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/store";
    }

    /**
     * @Description: 会员充值收银接口地址
     */
    public String getVirtualcardCardStoreUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/virtualcard/addValue";
    }

    /**
     * 饿了么退单确认
     *
     * @return
     */
    public String returnConfirm() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/return/confirm";
    }

    public String saleCardRecordInvaliate() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/saleInvalid";
    }

    /**
     * 匿名实体卡销售记录作废
     *
     * @return
     */
    public String anonymousEntityCardSaleRecordInvalid() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/tmpcard/invalid";
    }

    public String cardStoreValueRevoke() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/storerevoke";
    }

    public String memberStoreValueRevoke() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/virtualcard/revokeStore";
    }

    public String returnCardsUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/saleRefund";
    }

    /**
     * 催菜
     *
     * @Title: remindDish
     * @Return String 返回类型
     */
    public String remindDish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/batch/hurry";
    }

    /**
     * 起菜
     *
     * @Title: riseDish
     * @Return String 返回类型
     */
    public String riseDish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/batch/updish";
    }

    /**
     * 菜品操作
     */
    public String optDish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/trade/modifyItemOperation";
    }

    /**
     * 更新订单支付状态
     *
     * @Title: refreshStateUrl
     * @Return String 返回类型
     */
    public String refreshStateUrl() {
        //return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/ch5order/state";
        return ShopInfoCfg.getInstance().getServerKey() + "/pos/api/pay/syncTradeStatus";
    }

    /**
     * 更新订单支付状态
     *
     * @Title: refreshStateUrl
     * @Return String 返回类型
     */
    public String refreshReturnStateUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/ch5order/state4refund";
    }

    /**
     * 修改联台主单菜品打印状态
     *
     * @Title: modifyPrintStatus
     * @Return String 返回类型
     */
    public String modifyMainTradePrintStatus() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/fs/union/main-trade-modifyPrintStatus";
    }

    /**
     * 修改联台子单菜品打印状态
     *
     * @Title: modifyPrintStatus
     * @Return String 返回类型
     */
    public String modifySubTradePrintStatus() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/fs/union/sub-trade-modifyPrintStatus";
    }

    /**
     * 更新排队状态
     *
     * @Title: updateQueueStatus
     * @Return String 返回类型
     */

    public String changeQueue() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/queue/changeQueue";
    }

    /**
     * 大众点评完成交易
     *
     * @Title: tradeFinish
     * @Return String 返回类型
     */
    public String tradeFinish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/finish";
    }

    /**
     * 退押金地址
     *
     * @Title: tradeFinish
     * @Return String 返回类型
     */
    public String backDepositUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/deposit/refund";
    }

    /**
     * 自助退押金
     */
    public String buffetBackDepositUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v3/onos/refund/deposit/submit";
    }

    /**
     * 自助订单完成及退押金
     */
    public String buffetFinishAndDepositRefund() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/buffet/finish_and_deposit_refund";
    }

    /**
     * 创建排队和入场
     *
     * @Title: tradeFinish
     * @Return String 返回类型
     */
    public String createAndUpdateQueue() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v3/queue/submit";
    }

    /**
     * 合同到期提醒
     *
     * @return
     */
    public String HeTongDaoqiRequest() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/other/contract/getOverdueContracts";
    }

    /**
     * 移菜
     *
     * @return
     */
    public String moveDish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/item/move";
    }

    /**
     * 修改商户打印机健康状态
     *
     * @return
     */
    public String modifyPrintHealthStatus() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/printer/modifyPrintHealthStatus";
    }

    /**
     * 微信卡券详情
     *
     * @return
     */
    public String getWeixinCouponsDetailUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/weixin/queryWxCard";
    }

    /**
     * 美团团购券详情
     *
     * @return
     */
    public String getMeiTuanCouponsDetailUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/group_buying/ticketInfo";
    }

    /**
     * 查询匿名卡储值支付方式
     */
    public String getQueryTempCardStorePayModeUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/tmpcard/queryTempCardStorePayMode";
    }

    /**
     * 获取匿名卡最近一条交易信息
     *
     * @return
     */
    public String getAnoymousCardTradeUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/tmpcard/query/latelyInfo";
    }

    /**
     * 匿名卡退卡
     *
     * @return
     */
    public String returnAnoymousCardUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/tmpcard/refund";
    }


    /**
     * 挂账
     *
     * @return
     */
    public String getLagUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/credit/modifyAndUse";
    }

    /**
     * 点评查询单张团购券
     *
     * @return
     */
    public String ticketInfo() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/trade/group_buying/ticketInfo";
    }

    /**
     * 获取销售排行榜
     *
     * @return
     */
    public String getGoodsSaleRank() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/mind/dishSort";
    }

    /**
     * 获取销售排行榜打印信息
     *
     * @return
     */
    public String getGoodsSaleRankPrint() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/mind/getPrintData";
    }

    /**
     * 获取云打印地址
     *
     * @return
     */
    public String getClouldPrintUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/trade/cloud_print";
    }

    /**
     * 获取派送订单Url
     *
     * @return
     */
    public String getSendOrderUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/assistant/sendOrder";
    }

    /**
     * 获取取消派送订单Url
     *
     * @return
     */
    public String getCancelOrderUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/assistant/cancelOrder";
    }

    /**
     * 取消派送url（gateway）
     *
     * @return
     */
    public String getCancelDeliveryOrderUrl() {
        return "/api/core/delivery/v2/order/cancel";
    }

    public String mindUpdateSaveSetting() {
        return "mind/innerApi/client/updateSaveSetting";
    }

    /**
     * 通用透传地址
     *
     * @return
     */
    public String mindTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/mind/transfer";
    }

    /**
     * loyalty 通用透传接口
     *
     * @return
     */
    public String loyaltyTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/loyalty/transfer";
    }

    /**
     * mind自动配置（用来透传，不需要添加前缀）
     *
     * @return
     */
    public String mindAutoSet() {
        return "/mind/innerApi/commercial/initDemo";
    }

    /**
     * 后厨管理新增加接口
     *
     * @return
     */
    public String mindAddKitchenTicketOutlet() {
        return "/mind/innerApi/printerKitchen/add";
    }

    /**
     * 保存与更新标签设置
     *
     * @return
     */
    public String mindLablePrintSetting() {
        return "/mind/innerApi/printerKitchen/addOrUpdateLabel";
    }


    /**
     * 删除标签
     *
     * @return
     */
    public String mindRemoveLablePrintSetting() {
        return "/mind/innerApi/printerKitchen/deletePrinterLable";
    }

    /**
     * 收银点打印机信息修复接口
     *
     * @return
     */
    public String mindUpdateCashierPointDeviceId() {
        return "/mind/innerApi/printerKitchen/updatePrinterDeveiceId";
    }

    /**
     * 后厨管理修改出票口接口
     *
     * @return
     */
    public String mindUpdateKitchenTicketOutlet() {
        return "/mind/innerApi/printerKitchen/update";
    }

    /**
     * 后厨管理删除出票口接口
     *
     * @return
     */
    public String mindDeleteKitchenTicketOutlets() {
        return "/mind/innerApi/printerKitchen/delete";
    }


    /**
     * mind查询自动配置flag
     *
     * @return
     */
    public String mindQueryAutoSetFlag() {
        return "/mind/innerApi/commercial/initFlag";
    }

    /**
     * gateway通用透传地址
     *
     * @return
     */
    public String getGatewayTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/gateway/transfer";
    }

    /**
     * 电子发票获取开票二维码（用来透传，不需要添加前缀）
     *
     * @return
     */
    public String invoiceQrcode() {
        return "/api/invoice/qrcode";
    }

    /**
     * 烽火科技电子发票获取开票二维码（用来透传，不需要添加前缀）
     *
     * @return
     */
    public String invoiceFHQrcode() {
        return "/api/crm/v1/fenghuo/qrCode";
    }

    /**
     * 烽火科技电子发票获取开票二维码（用来透传，不需要添加前缀）
     *
     * @return
     */
    public String queryBalanceFH() {
        return "/api/crm/v1/fenghuo/braceletPay/queryBalance";
    }

    /**
     * 电子发票获取开票二维码（用来透传，不需要添加前缀）
     *
     * @return
     */
    public String addFee() {
        return "/api/core/delivery/order/tipping";
    }

    /**
     * 电子发票冲红（用来透传，不需要添加前缀）
     *
     * @return
     */
    public String invoiceRevoke() {
        return "/api/invoice/revoke";
    }

    /**
     * 查询配送费接口（用来透传，不需要添加前缀）
     */
    public String queryDeliveryFee() {
        return "/api/core/delivery/query/deliveryFee";
    }

    /**
     * 批量查询配送费接口（用来透传，不需要添加前缀）
     *
     * @return
     */
    public String batchQueryDeliveryFee() {
        return "/api/core/delivery/batchQuery/deliveryFee";
    }

    /**
     * 将订单下发到配送平台（用来透传，不需要添加前缀）
     */
    public String deliveryOrderDispatch() {
        return "/api/core/delivery/order/dispatch";
    }

    /**
     * 配送订单批量查询（用来透传，不需要添加前缀）
     */
    public String deliveryOrderList() {
        return "/api/core/delivery/order/list";
    }

    /**
     * 获取服务器时间
     *
     * @return
     */
    public String getServerTime() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/server/time/get";
    }

    /**
     * mind收支录入用来透传，不需要添加前缀）
     *
     * @return
     */
    public String getPaymentsInputUrl() {
        return "/mind/innerApi/accountSubject/save";
    }

    /**
     * mind收支详情用来透传，不需要添加前缀）
     *
     * @return
     */
    public String getPaymentsDetailUrl() {
        return "/mind/innerApi/accountSubject/secondSubjectQuery";
    }

    public String getAuthLogUploadUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/authority/batchUploadAuthLog";
    }

    /**
     * 新的实体卡登录地址
     *
     * @return
     */
    public String newCardLogin() {
        return "/b_kry/innerApi/cardInstance/cardLogin";
    }

    /**
     * 设置自定义模板类型地址
     *
     * @return
     */
    public String setPrintDocument() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/printer/setDocumentTemplate";
    }

    /**
     * 修改服务铃状态
     *
     * @return
     */
    public String updateCallWaiter() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/bellservice/callWaiter/update";
    }

    /**
     * 获取七牛token
     *
     * @return
     */
    public String getToken() {
        return "/mind/innerApi/qiniu/getToken";
    }

    public String getHelpUrl() {
        return getHelpURL();
//		return getMindHost()+"/mind/pos/help/listShow";
    }

    /**
     * 获取商户logoURL
     *
     * @return
     */
    public String getLogoURLRequest() {
        return "/mind/innerApi/commercial/info/getLogo";
    }

    /**
     * openId扫描url
     */
    public String getOpenIdUrl() {
        return getWeiXinHost() + "/user/loginPosScanCode?shopId="
                + ShopInfoCfg.getInstance().shopId + "&posDeviceID="
                + SystemUtils.getMacAddress() + "&appType="
                + SystemUtils.getAppType() + "&uuid=";
    }

    /**
     * POS设备认证
     *
     * @return
     */
    public String getDeviceAuth() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/security/device_auth";
    }

    /**
     * pos验证分区版本信息
     */
    public String checkVersionsAreaInfo() {
        return getRemoteHost() + "/api/cashier/validateVersionZone?";
    }

    public String checkAuth() {
        return getRemoteHost() + "/api/mobile/getBrandAuthVersion";
    }

    /**
     * 分步支付收银
     */
    public String newPayUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/onos/payment/submit";
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
     * 获取帮助中心地址
     *
     * @return
     */
    public static String getHelpURL() {
        return getHelpURLHost() + "/page/helpCenterOtherUsing/helpCenterList.html?id=1&name=%0A%20%20%20%20%20%20%20%20%0A%20%20%20%20%20%20%20%20%0A%20%20%20%20%20%20%20%20On%20Pos%0A%20%20%20%20";
    }

    /**
     * 获取系统公告点击量统计地址
     *
     * @return
     */
    public static String getMessagePushPosListUrl() {
        return getRemoteHost() + "/api/pos/qryCurrentPageviews?id=";
    }

    /**
     * supply 通用透传接口
     *
     * @return
     */
    public String supplyTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/trade/scm/transfer";
    }


    /**
     * supply库存开关接口
     */
    public String getSupplyInventorySet() {
        return "/merchandise/innerApi/dishshop/getAutoClearStatus";
    }

    /**
     * supply库存接口
     */
    public String getSupplySaleInventory() {
        return "/scm_kry/innerApi/inventory/getSaleInventory";
    }


    /**
     * 获取supply库存同步接口
     *
     * @return 返回同步接口
     */
    public String getSupplySyncInventory() {
        return "/scm_kry/innerApi/inventory/sync";
    }

    /**
     * 根据手机号查询顾客基本信息
     *
     * @return
     */
    public String getCustomerByType() {
        return "v2_member_getCustomerByType";
    }

    /**
     * 查询会员地址
     *
     * @return
     */
    public String getMemberAddressUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v2/member/queryMemberAddress";
    }

    /**
     * 溢收订单退款检查
     *
     * @return
     */
    public String getRefundCheck() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/onos/refund/overpay/check";
    }

    /**
     * 针对paymentItem退款
     */
    public String getRefundPayment(Long paymentItemId) {
        return ShopInfoManager.REMOTE_SERVER_HOST
                + String.format("/pos/api/pay/refund/%d", paymentItemId);
    }

    /**
     * 溢收订单退款
     *
     * @return
     */
    public String getRefundSubmit() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v3/onos/refund/overpay/submit";
    }

    /**
     * 改变取餐状态
     *
     * @return
     */
    public String getChangeTakeDishStatusUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/trade/takeaway/changeTakeDishStatus";
    }

    /**
     * 保存commercial_custom_settings表的mind接口地址
     */
    public String getSaveSettingURL() {
        return "/mind/innerApi/client/updateSaveSetting";
    }

    /**
     * 卡基本信息
     */
    public String getCardBaseInfoUrl() {
        return "v2_card_getCardBaseInfo";
    }

    /**
     * 通过实体卡卡号获取卡账户
     *
     * @return
     */
    public String getCardAccount() {
        return "v2_card_getCardAccount";
    }

    /**
     * 根据卡号精确查询卡信息
     */
    public String getCardInfoUrlByNum() {
        return "v2_card_getCardInfoByNum";
    }

    /**
     * 会员积分修改
     */
    public String integralModification() {
        return "v2_integral_compensateOrDeduction";
    }

    /**
     * 供应链地址
     *
     * @return
     */
    public String getCreateDishMenuUrl() {
        return "/merchandise/innerApi/carte/save";
    }

    /**
     * 删除菜单模板
     *
     * @return
     */
    public String deleteDishMenus() {
        return "/merchandise/innerApi/carte/delete";
    }

    /**
     * 团餐开台
     *
     * @return
     */
    public String groupOpenTable() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/trade/dinner/group/submit";
    }

    /**
     * 新会员积分流水
     */
    public String newQueryIntegralDetail() {
        return ShopInfoManager.getInstance().getServerKey() + "/pos/customer/integral";
    }

    /**
     * 人脸识别数据保存接口
     */
    public String saveCustomerFaceCode() {
        return "v2_member_saveCustomerFaceCode";
    }

    public String getForumListUrl() {
        return "/api/core/comment/list";
    }

    /**
     * 获得品牌直发券列表
     */
    public String customerDirectCouponList() {
        return "coupInstance_directCouponList" +
                "?brandId=" + BaseApplication.sInstance.getBrandIdenty();
    }

    /**
     * 获得品牌直发券列表
     */
    public String customerDirectCouponListV2() {
        return "coupInstance_getCanUseCouponListByBrandIdForFs" +
                "?brandId=" + BaseApplication.sInstance.getBrandIdenty();
    }

    /**
     * 发券
     */
    public String customerSendCoupons() {
        return "coupInstance_sendCoupons";
    }

    /**
     * 换卡下单
     *
     * @return
     */
    public String getChangeCardUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/cardchange/submit";
    }

    /**
     * 金诚换卡下单
     */
    public String getJCChangeCardUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/jincheng/cardchange";
    }

    /**
     * 金诚绑卡下单
     *
     * @return
     */
    public String getJCBindCardUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/entitycard/jincheng/cardsale";
    }

    /**
     * 金诚储值下单接口
     *
     * @return
     */
    public String getJCRechargeUrl() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/member/storeMemberValue";
    }

    /**
     * 团餐预定开台
     *
     * @return
     */
    public String bookingToGroupDinner() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/booking/toGroupDinnerSubmit";
    }

    private static String getQSApiServerHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1://开发环境
                return "http://demo.com";
            case 2://测试环境
                return "http://demo.com";
            case 3://灰度环境
                return "http://demo.com";
            case 4: //CI环境
                return "http://demo.com";
            case 5: //新加坡
                return "http://demo.com";
            default:
                return Configure.QS_API_SERVER_HOST;
        }
    }

    /**
     * 快餐服务地址
     */
    private static String getQSUpServerHost() {
        switch (AppBuildConfig.MY_BUILD_TYPE) {
            case 1://开发环境
                return "http://demo.com";
            case 2://测试环境
                return "http://demo.com";
            case 3://灰度环境
                return "http://demo.com";
            case 4: //CI环境
                return "http://demo.com";
            case 5: //新加坡
                return "http://demo.com";
            default:
                return Configure.QS_UP_SERVER_HOST;
        }
    }

    /**
     * 获取健康检查接口地址
     */
    public String getHealthUrl() {
        return getQSApiServerHost() + "/health";
    }

    /**
     * 获取校验并核销优惠券/积分/微信优惠券接口地址
     */
    public String getUsePrivilegeUrl() {
        return getQSApiServerHost() + "/v1/payment/usePrivilege";
    }

    /**
     * @return 返回快餐异步获取在线支付状态的url地址
     */
    public String getPayStatus4QSOffline() {
        return getQSApiServerHost() + "/v1/payment/item/result/get";
    }

    /**
     * 异步 获取未处理和错误的日志数量地址
     */
    public String getLogErrorQuantityApi() {
        return getQSApiServerHost() + "/v1/trade/report/shop-trade-quantity";
    }

    /**
     * 获取针对快餐异步机制的收银接口地址
     */
    public String getPay4QSUrl() {
        return getQSApiServerHost() + "/v1/payment/submit";
    }

    /**
     * 获取数据上传接口地址
     */
    public String getDataUploadUrl() {
        return getQSUpServerHost() + "/data/upload";
    }

    /**
     * 绑定配送员接口
     */
    public String getBindOrderUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/assistant/assign2Staff";
    }

    /**
     * 从ZK上获取对应的配置
     *
     * @return
     */
    public String getSysCmdConfig() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/v1/sys/cmd";
    }

    /**
     * 创建联台订单接口
     */
    public String getCreatUnionTradesUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/fs/union/union-table";
    }

    /**
     * 取消联台订单接口
     */
    public String getSplitUnionTradesUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/fs/union/separate-trade";
    }

    /**
     * 联台主单改单
     *
     * @return
     */
    public String getModifyUnionMainTrade() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/fs/union/main-trade-modify";
    }

    /**
     * 联台子单改单
     *
     * @return
     */
    public String getModifyUnionSubTrade() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/fs/union/sub-trade-modify";
    }

    /**
     * 联台合单和改接口
     */
    public String getUnionAndModifyUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/fs/union/union-and-modify";
    }


    /**
     * 联台菜品操作
     *
     * @return
     */
    public String getUnionTradeItemOperation() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/fs/union/modifyUnionItemOperation";
    }

    /**
     * //     * 三亚开关机
     * 通用开关机
     *
     * @return
     */
    public String getOnlineSwitch() {
//        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/sanya/onlineSwitch";
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v1/pos/onlineSwitch";
    }

    /**
     * 联台, 划菜
     *
     * @return
     */
    public String modifyServiceUnionMainDish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/fs/union/serving/maintrade/batchserving";
    }

    /**
     * 联台, 划菜(子单)
     *
     * @return
     */
    public String modifyServiceUnionSubDish() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/fs/union/serving/subtrade/batchserving";
    }

    /**
     * 修改订单备注
     *
     * @return
     */
    public String modifyTradeMemo() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v2/trade/modify-memo";
    }

    /**
     * 自助餐联台
     *
     * @return
     */
    public String buffetUnionTableUrl() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/fs/buffet/union/create";
    }

    /**
     * 取消自助联台订单接口
     */
    public String buffetSplitTradeUrl() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/fs/buffet/union/split-trade";
    }

    /**
     * 自助子单改单
     *
     * @return
     */
    public String buffetModifyUnionSubTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/fs/buffet/union/sub-trade-modify";
    }

    /**
     * 自助主单改单
     *
     * @return
     */
    public String buffetModifyUnionMainTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/fs/buffet/union/main-trade-modify";
    }

    /**
     * 自助餐连台主单添加餐标，添加/修改人数、押金、销售员
     *
     * @return
     */
    public String buffetCreateMenu() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/fs/buffet/union/create-menu";
    }

    /**
     * 自助主单完成合单接口
     *
     * @return
     */
    public String buffetUnionFinish() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/fs/buffet/union/finish";
    }

    /**
     * 会员销账接口
     *
     * @return
     */
    public String writeoffUrl() {
        //78表示零售
        if (BusinessTypeUtils.isRetail()) {
            return SwitchServerManager.getInstance().getServerKey() + "/retail/v1/trade/credit/clear_bill";
        } else {
            return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v1/trade/credit/clear_bill";
        }
    }

    /**
     * 会员销账轮训顾客主扫接口
     *
     * @return
     */
    public String writeoffZSResultUrl() {
        //78表示零售
        if (BusinessTypeUtils.isRetail()) {
            return SwitchServerManager.getInstance().getServerKey() + "/retail/v1/trade/status/query/gkzs_result";
        } else {
            return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/trade/status/query/gkzs_result";
        }

    }

    /**
     * 会员销账轮训顾客被扫接口
     *
     * @return
     */
    public String writeoffBSResultUrl() {
        if (BusinessTypeUtils.isRetail()) {
            return SwitchServerManager.getInstance().getServerKey() + "/retail/v1/trade/status/query/gkbs_result";
        } else {
            return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/trade/status/query/gkbs_result";
        }
    }

    /**
     * v8.11.0
     * 更新TradeCustomer
     */
    public String updateCustomer() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v2/trade/modifyCustomer";
    }

    /**
     * 查询税号接口
     *
     * @return
     */
    public String getTaxNoUrl() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v1/trade/queryInvoiceNo";

    }

    /**
     * 创建预付单
     */
    public String getPrePayTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v3/booking/prepay/createPrePayTrade";
    }

    /**
     * 作废预付单
     *
     * @return
     */
    public String deletePrePayTrade() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v3/booking/prepay/deletePrePayTrade";
    }

    /**
     * 口碑核销
     *
     * @return
     */
    public String verifyKoubei() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/takeaway/koubei/verify/fromPos";
    }

    /**
     * 预订预付金抵扣多付退款
     *
     * @return
     */
    public String prePayRefund() {
        return ShopInfoCfg.getInstance().getServerKey() + "/CalmRouter/v3/booking/prepay/prePayRefund";
    }

    /**
     * v8.13.0
     * 订金抵扣接口
     */
    public String getEarnestToPayUrl() {

        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/v3/booking/prepay/deduction";
    }

    /**
     * 联台订单作废
     *
     * @return
     */
    public String unionTradeRecision() {
        return SwitchServerManager.getInstance().getServerKey() + "/CalmRouter/fs/union/batch-delete";
    }

    /**
     * 获取每日售卖量更新接口
     *
     * @return
     */
    public String getDishSaleUpdateUrl() {
        return "/merchandise/innerApi/dishshop/updateSaleTotal";
    }

    /**
     * loyti获得会员消费次数接口
     *
     * @return
     */
    public String customTimes() {
        return "/loyt/crowd/innerapi/crowd/object";
    }


    /**
     * Talent 通用透传接口
     * /CalmRouter/talent/transfer 对应 http://demo.com/basic
     *
     * @return
     */
    public String talentTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/talent/transfer";
    }

    /**
     * Talent 通用透传接口
     * /CalmRouter/talent/common/transfer对应 http://demo.com
     *
     * @return
     */
    public String talentCommonTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/talent/common/transfer";
    }

    /**
     * 公共业务组 通用透传接口
     * /CalmRouter/ps/auth_transfer 对应 http://demo.com
     *
     * @return
     */
    public String psAuthTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/ps/auth_transfer";
    }

    /**
     * 公共业务组 通用透传接口
     * /CalmRouter/ps/account_transfer 对应 http://demo.com
     *
     * @return
     */
    public String psAccountTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/ps/account_transfer";
    }

    /**
     * Talent 通用透传接口
     * /CalmRouter/ps/resource_transfer对应 http://demo.com
     *
     * @return
     */
    public String psResourceTransfer() {
        return ShopInfoCfg.getInstance().getServerKey()
                + "/CalmRouter/ps/resource_transfer";
    }

    /**
     * 员工绑定人脸
     *
     * @return
     */
    public String talentBindFace() {
        return "/account/api/mobile/third/face/bind";
    }

    /**
     * 员工提交考勤
     *
     * @return
     */
    public String talentAddAttendanceUrl() {

        return "/api/attendance/add";
    }

    /**
     * 查询员工考勤
     *
     * @return
     */
    public String talentSearchAttendanceUrl() {

        return "/api/attendance/getPunchFace";
    }
}
