package com.zhongmei.yunfu.http.utils;

import android.net.Uri;

import java.util.HashSet;
import java.util.Set;



public class RequestRetryUtil {

        public final static int MAX_SERVICE_RETRY_COUNT = 3;

        private static Set<String> retryUrlPaths = new HashSet<>();

    static {

    }



    public static boolean isUrlCanServiceRetry(String url) {
        String path = Uri.parse(url).getPath();
        return retryUrlPaths.contains(path);
    }

}
