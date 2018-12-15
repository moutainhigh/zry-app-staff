package com.zhongmei.bty.common.util.audio;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.zhongmei.yunfu.R;

/**
 * @Date：2015年12月2日 上午9:29:47
 * @Description: 用于公共声音片段的播放
 * <p>
 * rights reserved.
 */
public class Common extends AudioBase {

    private Context context;

    public static final String TAG = "common";

    public static final String PATH = "audio/" + TAG + "/";

    private static List<String> audio;

    private static Common instance;

    protected Common(Context context) {
        this.context = context;
        Resources res = context.getResources();
        audio = Arrays.asList(res.getStringArray(R.array.common));
    }

    public static Common getInstance(Context context) {
        synchronized (Common.class) {
            if (instance == null) {
                instance = new Common(context);
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
        }
        throw new Exception("No such audio piece!");

    }

    @Override
    public String getFilePath() {
        return null;
    }

    @Override
    public String getSimplePre() {
        return null;
    }

    @Override
    public String getSimpleSuf() {
        return null;
    }

}
