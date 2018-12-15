package com.zhongmei.bty.basemodule.commonbusiness.utils;

import android.os.Environment;
import android.util.Log;

import com.baidu.tts.answer.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.yunfu.db.enums.Sex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 百度语音
 */
public class SpeechSynthesizerUtil {
    private final String TAG = SpeechSynthesizerUtil.class.getSimpleName();

    private SpeechSynthesizer mSpeechSynthesizer;

    private static String mSampleDirPath;

    private static final String SAMPLE_DIR_NAME = "baiduTTS";

    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";

    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";

    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";

    private static SpeechSynthesizerUtil speechUtil;

    /**
     * 单例
     *
     * @return
     */
    public static SpeechSynthesizerUtil getInstance() {
        if (speechUtil == null) {
            speechUtil = new SpeechSynthesizerUtil();
        }
        return speechUtil;
    }

    /**
     * 初始化
     */
    public void init() {
        try {
            initialEnv();
            initBaiduYuYin();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
    }

    private void initBaiduYuYin() {
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(BaseApplication.sInstance);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(new Listener());
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE,
                mSampleDirPath + "/" + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId("6428171");
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey("dfpUrSKoKtDgUg2LMBhcYGnS", "NPCplw9Y6CMxLvST0ReNnoviGGeNOArZ");
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeechSynthesizer.SPEAKER_FEMALE);
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");//音量，取值范围[0, 9]，数值越大，音量越大
        mSpeechSynthesizer.loadModel(mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME, null);
        // 授权检测接口
        Log.i(TAG, "before auth");
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
        Log.i(TAG, "after auth");
        if (authInfo.isSuccess()) {

        } else {
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            Log.e(TAG, errorMsg);
        }
        Log.i(TAG, "before init");
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        Log.i(TAG, "after init");
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = BaseApplication.sInstance.getResources().getAssets().open("baiduTTS/" + source);
                fos = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *

     *
     */
    private class Listener implements SpeechSynthesizerListener {

        @Override
        public void onError(String arg0, SpeechError arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeechFinish(String arg0) {

        }

        @Override
        public void onSpeechProgressChanged(String arg0, int arg1) {
        }

        @Override
        public void onSpeechStart(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {

        }

        @Override
        public void onSynthesizeFinish(String arg0) {

        }

        @Override
        public void onSynthesizeStart(String arg0) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 叫号
     *
     * @param speech
     * @param num
     */
    public void speakCall(BaiduSyntheticSpeech speech, String num) {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
            if (speech.getSex() == Sex.MALE) {
                mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeechSynthesizer.SPEAKER_MALE);
                mSpeechSynthesizer.loadModel(mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME, null);
            } else {
                mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeechSynthesizer.SPEAKER_FEMALE);
                mSpeechSynthesizer.loadModel(mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME, null);
            }
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, speech.getSpeed().toString());
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");//音量，取值范围[0, 9]，数值越大，音量越大
            String content = speech.getContent();
            String format = BaseApplication.sInstance.getResources().getString(R.string.voice_number_toast);
            String newContent = content.replace(format, num);
            mSpeechSynthesizer.speak(newContent);
        }
    }

    /**
     * 播音
     *
     * @param speech
     */
    public void speak(BaiduSyntheticSpeech speech) {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
            if (speech.getSex() == Sex.MALE) {
                mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeechSynthesizer.SPEAKER_MALE);
                mSpeechSynthesizer.loadModel(mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME, null);
            } else {
                mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeechSynthesizer.SPEAKER_FEMALE);
                mSpeechSynthesizer.loadModel(mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME, null);
            }
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, speech.getSpeed().toString());
            mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");//音量，取值范围[0, 9]，数值越大，音量越大
            mSpeechSynthesizer.speak(speech.getContent());
        }
    }

    public void setOnListener(SpeechSynthesizerListener speechListener) {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.setSpeechSynthesizerListener(speechListener);
        }


    }

    public void stop() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stop();
        }
    }

}