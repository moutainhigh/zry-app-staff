package com.zhongmei.bty.queue.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Environment;
import android.util.Log;

/**
 * 工具类
 */
public class QueueFileUtil {
    private static final String TAG = QueueFileUtil.class.getSimpleName();

    public static String getPath() {
        String rootpath = Environment.getExternalStorageDirectory().getPath();
        if (!rootpath.endsWith("/")) {
            rootpath += "/";
        }
        return rootpath + "calm/voice/";
    }

    /**
     * url地址转化 处理中文问题
     */
    public static String convertUtf(String path) {
        try {
            path = URLEncoder.encode(path, "utf-8");
            path = path.replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
            return path;
        } catch (UnsupportedEncodingException e) {
            Log.e("", TAG, e);
        }
        return path;
    }
}
