package com.zhongmei.bty.common.util.audio;

import java.util.List;

/**
 * @Date：2015年12月2日 上午9:35:46
 * @Description: 声音片段的基础
 * 所有的分模块声音播放类应该先集成AudioBase，
 * 然后在string_audio.xml文件中配置array
 * eg:在构造器中调用Arrays.asList(res.getStringArray(R.array.cashier))方法得到array
 */
public abstract class AudioBase {

    public static final String MP3_SUF = ".mp3";

    public static final String WAV_SUF = ".wav";

    public static final int NOAUDIO = 0;

    public static final int MP3 = 1;

    public static final int WAV = 2;

    public abstract String getFilePath();

    public abstract String getAudioPiecePath(String fileName) throws Exception;

    protected int hasAudioPiece(String audioPiece, List<String> list) {
        //for(String s:list){Log.e("-----", s + "    " +audioPiece);}
        if (list.contains(audioPiece + MP3_SUF)) {
            return MP3;
        }
        if (list.contains(audioPiece + WAV_SUF)) {
            return WAV;
        }
        return NOAUDIO;
    }

    public abstract String getSimplePre();

    public abstract String getSimpleSuf();
}
