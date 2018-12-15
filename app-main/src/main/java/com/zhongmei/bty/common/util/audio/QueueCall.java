package com.zhongmei.bty.common.util.audio;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.queue.util.QueueFileUtil;

/**
 * 排队下载叫号
 */
public class QueueCall extends AudioBase {

    public static final String SIMPLE_PREFIX = "queue_prefix";

    public static final String SIMPLE_SUFFIX = "queue_suffix";

    private static List<String> audio;

    private static QueueCall instance;

    private QueueCall(Context context) {
        Resources res = context.getResources();
        audio = Arrays.asList(res.getStringArray(R.array.queue));
        for (String s : audio) {
            Log.e("queue", s);
        }
    }

    public static QueueCall getInstance(Context context) {
        synchronized (QueueCall.class) {
            if (instance == null) {
                instance = new QueueCall(context);
            }
        }

        return instance;
    }

    public String getAudioPiecePath(String fileName) throws Exception {
        switch (hasAudioPiece(fileName, audio)) {
            case MP3: {
                return getPath() + fileName + MP3_SUF;
            }
            case WAV: {
                return getPath() + fileName + WAV_SUF;
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
        return SIMPLE_PREFIX;
    }

    @Override
    public String getSimpleSuf() {
        // TODO Auto-generated method stub
        return SIMPLE_SUFFIX;
    }

    /**
     * 文件路径
     *
     * @return
     */
    private static String getPath() {
        return QueueFileUtil.getPath() + "call/audio/";
    }

}
