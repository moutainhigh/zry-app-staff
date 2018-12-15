package com.zhongmei.bty.common.util.audio;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.zhongmei.yunfu.R;

/**
 * @Date：2015年12月1日 下午5:52:30
 * @Description: 用于快餐功能声音片段播放
 * <p>
 * rights reserved.
 */
public class Cashier extends AudioBase {

    public static final String SIMPLE_PREFIX = "cashier_prefix";

    public static final String SIMPLE_SUFFIX = "cashier_suffix";

    private Context context;

    public static final String TAG = "cashier";

    public static final String PATH = "audio/" + TAG + "/";

    private static List<String> audio;

    private static Cashier instance;

    private Cashier(Context context) {
        this.context = context;
        Resources res = context.getResources();
        audio = Arrays.asList(res.getStringArray(R.array.cashier));
        for (String s : audio) {
            Log.e("cashier", s);
        }
    }

    public static Cashier getInstance(Context context) {
        synchronized (Cashier.class) {
            if (instance == null) {
                instance = new Cashier(context);
            }
        }

        return instance;
    }

    public String getAudioPiecePath(String fileName) throws Exception {
        switch (hasAudioPiece(fileName, audio)) {
            case MP3: {
                return PATH + fileName + MP3_SUF;
            }
            case WAV: {
                return PATH + fileName + WAV_SUF;
            }
            default: {
                throw new Exception("No such an audio piece!");
            }
        }
    }

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public String getSimplePre() {
        return SIMPLE_PREFIX;
    }

    @Override
    public String getSimpleSuf() {
        return SIMPLE_SUFFIX;
    }

}
