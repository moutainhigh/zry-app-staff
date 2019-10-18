package com.zhongmei.yunfu.net;

import android.net.Uri;
import android.text.TextUtils;

import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.context.util.EncryptUtils;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;

import java.util.HashMap;
import java.util.Map;


public class HttpConstant {


    public static final String YF_API_MSG_ID = "yf-api-msgid";
        public static final String YF_API_DEVICE_ID = "yf-api-device-id";
        public static final String YF_API_SHOP_ID = "yf-api-shop-id";
        public static final String YF_API_BRAND_ID = "yf-api-brand-id";
        public static final String YF_API_TIMESTAMP = "yf-api-timestamp";
    public static final String YF_API_TOKEN = "yf-api-token";

        public static final String YF_API_SIGN = "yf-api-sign";

        public static final String KRY_COMPRESS_TYPE = "fy-compress-type";
        public static final String KRY_SYNC_LOCALE = "fy-sync-locale";
        public static final String ZONE_VERSION = "zone_version";

    public static final String TOKEN_SHOP_ID = "token_shop_id";


        public static final String DEFAULTLANGUAGE = "defaultLanguage";

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
