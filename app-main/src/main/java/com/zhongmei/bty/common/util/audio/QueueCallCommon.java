package com.zhongmei.bty.common.util.audio;

import android.content.Context;

import com.zhongmei.bty.queue.util.QueueFileUtil;

/**
 * 使用下载叫号
 */
public class QueueCallCommon extends Common {


    private static QueueCallCommon instance;

    protected QueueCallCommon(Context context) {
        super(context);
    }

    public static QueueCallCommon getInstance(Context context) {
        synchronized (QueueCallCommon.class) {
            if (instance == null) {
                instance = new QueueCallCommon(context);
            }
        }

        return instance;
    }

    public String getAudioPiecePath(String fileName) throws Exception {
        return getPath() + fileName + MP3_SUF;

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

    /**
     * 文件前缀
     *
     * @return
     */
    private static String getPath() {
        return QueueFileUtil.getPath() + "call/audio/audio_";
    }
}
