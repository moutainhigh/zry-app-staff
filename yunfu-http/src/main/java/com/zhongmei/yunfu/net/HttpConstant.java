package com.zhongmei.yunfu.net;

import android.net.Uri;
import android.text.TextUtils;

import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.context.util.EncryptUtils;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date：2014年11月4日 上午11:43:44
 * @Description: 公用的静态变量声明
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class HttpConstant {

    /**
     * 响应后台日志埋点的请求ID
     */
    public static final String YF_API_MSG_ID = "yf-api-msgid";
    //设备ID
    public static final String YF_API_DEVICE_ID = "yf-api-device-id";
    //门店id
    public static final String YF_API_SHOP_ID = "yf-api-shop-id";
    //品牌编号
    public static final String YF_API_BRAND_ID = "yf-api-brand-id";
    //客户端本地时间戳
    public static final String YF_API_TIMESTAMP = "yf-api-timestamp";
    public static final String YF_API_TOKEN = "yf-api-token";

    //数据签名
    public static final String YF_API_SIGN = "yf-api-sign";

    //并且判断其值是否为gzip
    public static final String KRY_COMPRESS_TYPE = "kry-compress-type";
    //本次请求的语言环境
    public static final String KRY_SYNC_LOCALE = "kry-sync-locale";
    //分区标示
    public static final String ZONE_VERSION = "zone_version";

    public static final String TOKEN_SHOP_ID = "token_shop_id";


    //系统默认语言
    public static final String DEFAULTLANGUAGE = "defaultLanguage";

    //api安全验证码token
    public static final String TOKENENCRYPT = "tokenEncrypt";


    public static String getYfApiToken() {
        return SharedPreferenceUtil.getSpUtil().getString(HttpConstant.TOKENENCRYPT, "");
    }

    public static Map<String, String> tokenEncrypt(String shopId) {
        return tokenEncrypt(shopId, getYfApiToken(), System.currentTimeMillis());
    }

    public static Map<String, String> tokenEncrypt(String shopId, String token) {
        return tokenEncrypt(shopId, token, System.currentTimeMillis());
    }

    /**
     * api安全验证数据信息加密信息设置
     * SHA-256(kry-api-token=登录认证token&kry-api-shop-id=门店ID^512&kry-api-timestamp=客户端时间戳)
     */
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
    }

    //压缩起始最小值
    public static final int KRY_COMPRESS_MIN = 1200;

    public static boolean isAllowCompress(String url, byte[] body) {
        if (AppBuildConfig.ALLOW_COMPRESS
                && body != null && body.length >= HttpConstant.KRY_COMPRESS_MIN) {
            if (url != null) {
                Uri uri = Uri.parse(url);
                if (uri.getPath().startsWith("/CalmRouter") && !excludesUri(uri)) {
                    return false;
                }
            }
        }
        return false;
    }

    private static final String[] EXCLUDES_REGEX = {
            "/CalmRouter/v[\\d]+/print/.*",
            "/CalmRouter/v[\\d]+/printer/.*"
    };

    private static boolean excludesUri(Uri uri) {
        for (String regex : EXCLUDES_REGEX) {
            if (uri.getPath().matches(regex)) {
                return true;
            }
        }
        return false;
    }
}
