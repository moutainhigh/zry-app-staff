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


    private boolean isCallOrAuditon = false;


    public void playCall(BaiduSyntheticSpeech speechCall, String num) {
                isCallOrAuditon = true;
        stopPlay();        SpeechSynthesizerUtil.getInstance().speakCall(speechCall, num);
    }


    public void playAudition(BaiduSyntheticSpeech speech) {
        isCallOrAuditon = true;
        stopPlay();        SpeechSynthesizerUtil.getInstance().speak(speech);
    }


    public void play(QueueVoiceVo selectedVo, int selectIndex) {
        this.selectedVo = selectedVo;
        this.selectIndex = selectIndex;
        stopPlay();        isPlay = true;
        if (selectedVo.getType() == 1) {            SpeechSynthesizerUtil.getInstance().speak(selectedVo.getSpeech());
        } else {            play(selectedVo.getPath());
        }
    }


    public void stopPlay() {
        SpeechSynthesizerUtil.getInstance().stop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }


    private class CompleteListener implements OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (selectedVo != null) {
                play(selectedVo.getPath());
            }
        }
    }


    private class PlayListener implements SpeechSynthesizerListener {

        @Override
        public void onError(String arg0, SpeechError arg1) {

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

        }
    }


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

        public class AudioBinder extends Binder {

                public QueuePlayService getService() {
            return QueuePlayService.this;
        }
    }


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