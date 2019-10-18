package com.zhongmei.bty.basemodule.commonbusiness.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.commonbusiness.bean.QueueVoiceVo;
import com.zhongmei.bty.basemodule.commonbusiness.service.QueuePlayService;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.yunfu.db.enums.Sex;


public class QueuePlayServiceManager {

    private static QueuePlayService playService = null;

    public static void startService(Context context) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClass(context, QueuePlayService.class);
        context.bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    public static void unbind(Context context) {
        context.unbindService(conn);
    }

        private static ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
                        playService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
                        playService = ((QueuePlayService.AudioBinder) binder).getService();

        }
    };


    public static void play(QueueVoiceVo selectedVo, int selectIndex) {
        if (playService != null) {
            playService.play(selectedVo, selectIndex);
        }
    }


    public static void playCall(BaiduSyntheticSpeech speechCall, String num) {
        if (playService != null) {
            playService.playCall(speechCall, num);
        }
    }


    public static void playCall(BaiduSyntheticSpeech speechCall, String num, QueuePlayService.SpeechFinishListener listener) {
        if (playService != null) {
            playService.setSpeechFinishListener(listener);
            playService.playCall(speechCall, num);
        }
    }


    public static void playAudition(BaiduSyntheticSpeech speechCall) {
        if (playService != null) {
            playService.playAudition(speechCall);
        }
    }


    public static void playAudition(Integer type, String speechText, Sex speecherSex, Integer speed) {
        if (playService != null && !TextUtils.isEmpty(speechText)) {
            BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
            speech.setType(type == null ? 1 : type);
            speech.setContent(speechText);
            speech.setSex(speecherSex == null ? Sex.FEMALE : speecherSex);
            speech.setSpeed(speed == null ? 5 : speed);
            playService.playAudition(speech);
        }
    }

    public static void playAudition(String speechText) {
        playAudition(null, speechText, null, null);
    }


    public static void stop() {
        if (playService != null) {
            playService.stopPlay();
            playService.setStop(true);
        }
    }


    public static void stopAudition() {
        if (playService != null) {
            playService.stopPlay();
        }
    }

    public static boolean isPlay() {
        return playService.isPlay();
    }

    public static int getSelectIndex() {
                return playService.getSelectIndex();
    }

}
