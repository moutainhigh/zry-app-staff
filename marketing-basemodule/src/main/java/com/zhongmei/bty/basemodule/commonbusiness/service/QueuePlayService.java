package com.zhongmei.bty.basemodule.commonbusiness.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.zhongmei.bty.basemodule.commonbusiness.bean.QueueVoiceVo;
import com.zhongmei.bty.basemodule.commonbusiness.event.SpeechCallStopEvent;
import com.zhongmei.bty.basemodule.commonbusiness.utils.SpeechSynthesizerUtil;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;

import java.io.FileInputStream;

import de.greenrobot.event.EventBus;

/**
 * 排队广播方法
 */

public class QueuePlayService extends Service {

    private static final String TAG = QueuePlayService.class.getSimpleName();

    private QueueVoiceVo selectedVo;

    private int selectIndex = -1;

    private MediaPlayer mediaPlayer;

    private boolean isPlay = false;

    private SpeechFinishListener speechFinishListener;

    public void setSpeechFinishListener(SpeechFinishListener speechFinishListener) {
        this.speechFinishListener = speechFinishListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new CompleteListener());
        // 百度语音
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                SpeechSynthesizerUtil.getInstance().init();
                SpeechSynthesizerUtil.getInstance().setOnListener(new PlayListener());
            }
        });
    }

    @Override
    public void onDestroy() {
        stopPlay();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AudioBinder();
    }

    /**
     * 是否叫号
     */
    private boolean isCallOrAuditon = false;

    /**
     * 叫号
     *
     * @param speechCall
     * @param num
     */
    public void playCall(BaiduSyntheticSpeech speechCall, String num) {
        // TODO Auto-generated method stub
        isCallOrAuditon = true;
        stopPlay();// 先停止
        SpeechSynthesizerUtil.getInstance().speakCall(speechCall, num);
    }

    /**
     * 试听
     *
     * @param speech
     */
    public void playAudition(BaiduSyntheticSpeech speech) {
        isCallOrAuditon = true;
        stopPlay();// 先停止
        SpeechSynthesizerUtil.getInstance().speak(speech);
    }

    /**
     * 播放音乐
     *
     * @param selectedVo
     * @param selectIndex
     */
    public void play(QueueVoiceVo selectedVo, int selectIndex) {
        this.selectedVo = selectedVo;
        this.selectIndex = selectIndex;
        stopPlay();// 先停止
        isPlay = true;
        if (selectedVo.getType() == 1) {// 合成列表
            SpeechSynthesizerUtil.getInstance().speak(selectedVo.getSpeech());
        } else {// 下载列表
            play(selectedVo.getPath());
        }
    }

    /**
     * 停止播放声音
     */
    public void stopPlay() {
        SpeechSynthesizerUtil.getInstance().stop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    /**
     * 播放器监听
     */
    private class CompleteListener implements OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (selectedVo != null) {
                play(selectedVo.getPath());
            }
        }
    }

    /**
     *

     *
     */
    private class PlayListener implements SpeechSynthesizerListener {

        @Override
        public void onError(String arg0, SpeechError arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSpeechFinish(String arg0) {
            if (isPlay) {
                if (selectedVo != null) {
                    if (selectedVo.getSpeech() != null) {
                        SpeechSynthesizerUtil.getInstance().speak(selectedVo.getSpeech());
                    } else if (selectedVo.getPath() != null) {
                        play(selectedVo.getPath());
                    }
                }
            }
            if (isCallOrAuditon) {
                if (speechFinishListener != null) {
                    speechFinishListener.finish();
                    speechFinishListener = null;
                }
                EventBus.getDefault().post(new SpeechCallStopEvent());
                isCallOrAuditon = false;
            }
        }

        @Override
        public void onSpeechProgressChanged(String arg0, int arg1) {

        }

        @Override
        public void onSpeechStart(String arg0) {
            Log.i(TAG, "start");

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
     * 播放本地下载文件
     *
     * @param filePath
     */
    private void play(String filePath) {
        try {
            mediaPlayer.reset();
            FileInputStream localFileInputStream = new FileInputStream(filePath);
            mediaPlayer.setDataSource(localFileInputStream.getFD());
            localFileInputStream.close();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    // 为了和Activity交互，我们需要定义一个Binder对象
    public class AudioBinder extends Binder {

        // 返回Service对象
        public QueuePlayService getService() {
            return QueuePlayService.this;
        }
    }

    /**
     * 播放状态
     *
     * @return
     */
    public boolean isPlay() {
        return isPlay;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setStop(boolean b) {
        isPlay = false;
    }

    public interface SpeechFinishListener {
        void finish();
    }

}