package com.zhongmei.bty.common.util.audio;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.zhongmei.yunfu.R;

/**
 * @Date：2015年12月2日 上午9:29:01
 * @Description: 用于排队功能模块的声音播放
 * <p>
 * rights reserved.
 */
public class QueueAudio extends AudioBase {

    public static final String SIMPLE_PREFIX = "queue_prefix";

    public static final String SIMPLE_SUFFIX = "queue_suffix";

    private Context context;

    public static final String TAG = "queue";

    public static final String PATH = "audio/" + TAG + "/";

    private static List<String> audio;

    private static QueueAudio instance;

    private QueueAudio(Context context) {
        this.context = context;
        Resources res = context.getResources();
        audio = Arrays.asList(res.getStringArray(R.array.queue));
        for (String s : audio) {
            Log.e("queue", s);
        }
    }

    public static QueueAudio getInstance(Context context) {
        synchronized (QueueAudio.class) {
            if (instance == null) {
                instance = new QueueAudio(context);
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
        return SIMPLE_PREFIX;
    }

    @Override
    public String getSimpleSuf() {
        // TODO Auto-generated method stub
        return SIMPLE_SUFFIX;
    }

}
