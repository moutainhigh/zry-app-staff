package com.zhongmei.yunfu.http.utils;

import android.net.Uri;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 */

public class RequestRetryUtil {

    //幂等最大重试次数
    public final static int MAX_SERVICE_RETRY_COUNT = 3;

    //支持幂等的接口url
    private static Set<String> retryUrlPaths = new HashSet<>();

    static {
        /*retryUrlPaths.add(Uri.parse(ServerKey.tradeModifyBuffet()).getPath());
        retryUrlPaths.add(Uri.parse(ServerKey.dinnerSetTableAndAccept()).getPath());
        retryUrlPaths.add(Uri.parse(ServerKey.newTradeModifyDinner()).getPath());
        retryUrlPaths.add(Uri.parse(ServerKey.getGroupModify()).getPath());
        retryUrlPaths.add(Uri.parse(ServerKey.modifyPrintStatus()).getPath());*/
    }


    /**
     * 接口是否可以执行业务幂等
     */
    public static boolean isUrlCanServiceRetry(String url) {
        String path = Uri.parse(url).getPath();
        return retryUrlPaths.contains(path);
    }

}
