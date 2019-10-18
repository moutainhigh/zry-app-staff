package com.zhongmei.bty.basemodule.commonbusiness.manager;

import android.media.MediaPlayer;

import com.zhongmei.bty.basemodule.commonbusiness.bean.MediaPlayerFileVo;
import com.zhongmei.bty.basemodule.commonbusiness.service.MediaPlayerService;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MediaPlayerQueueManager implements MediaPlayer.OnCompletionListener {

    public static final int MEDIA_TYPE_WECHAT_ADDTRADE = 1;


    public static final int MEDIA_TYPE_BAIDU_RICE_NEWTRADE = 2;


    public static final int MEDIA_TYPE_PIZZAHUT_NEWTRADE = 3;


    public static final int MEDIA_TYPE_DIANPING_NEWTRADE = 4;


    public static final int MEDIA_TYPE_ELEME_NEWTRADE = 5;


    public static final int MEDIA_TYPE_WECHAT_NEWTRADE = 6;


    public static final int MEDIA_TYPE_SHUKE_NEWTRADE = 7;


    public static final int MEDIA_TYPE_MEITUAN_TAKEOUT_NEWTRADE = 8;


    public static final int MEDIA_TYPE_BAIDU_TAKEOUT_NEWTRADE = 9;


    public static final int MEDIA_TYPE_NEWBOOKING = 10;


    public static final int MEDIA_TYPE_TAKE_OUT_CANCEL = 11;


    public static final int MEDIA_TYPE_BROADCAST_NOTICE = 12;


    public static final int MEDIA_TYPE_CALL_WAITER = 13;


    public static final int MEDIA_TYPE_FAMILIAR_ADDTRADE = 14;


    public static final int MEDIA_TYPE_OTHER_CLIENT_PAY = 15;


    public static final int MEDIA_TYPE_NEW_AUTO_ACCEPT_TRADE = 16;


    public static final int MEDIA_TYPE_DELIVERY_CANCEL = 17;


    public static final int MEDIA_TYPE_CANCEL_TRADE = 18;


    public static final int MEDIA_TYPE_ELEME_DELIVERY_ORDER_CANCEL = 19;

    private static MediaPlayerQueueManager mMediaPlayerQueueManager;
    private Map<Integer, Object> mSoundTask;    private Integer mCurrentPlaying;
    public static MediaPlayerQueueManager getInstance() {
        if (mMediaPlayerQueueManager == null) {
            mMediaPlayerQueueManager = new MediaPlayerQueueManager();
        }

        if (MediaPlayerService.getInstance().getmMediaPlayCompleteListener() == null) {
            MediaPlayerService.getInstance().setmMediaPlayCompleteListener(mMediaPlayerQueueManager);
        }
        return mMediaPlayerQueueManager;
    }


    private MediaPlayerQueueManager() {
        mSoundTask = new HashMap<>();
    }


    public void play(int mediaType, int resId) {
        if (mSoundTask != null && !mSoundTask.containsKey(mediaType)) {
            mSoundTask.put(mediaType, new MediaPlayerFileVo(2, resId));
        }
        cyclePlay();
    }


    public void play(int mediaType, BaiduSyntheticSpeech speech) {
        if (mSoundTask != null && !mSoundTask.containsKey(mediaType)) {
            mSoundTask.put(mediaType, speech);
        }
        cyclePlay();
    }



    private void cyclePlay() {
        if (mSoundTask != null && mSoundTask.size() > 0) {
            Iterator iter = mSoundTask.entrySet().iterator();
            Map.Entry entry = (Map.Entry) iter.next();
            if (entry.getValue() instanceof MediaPlayerFileVo) {
                if (!MediaPlayerService.getInstance().isPlaying()) {
                    mCurrentPlaying = (Integer) entry.getKey();
                    MediaPlayerService.getInstance().play((MediaPlayerFileVo) entry.getValue());
                }
            } else if (entry.getValue() instanceof BaiduSyntheticSpeech) {
                if (!QueuePlayServiceManager.isPlay()) {
                    mCurrentPlaying = (Integer) entry.getKey();
                    QueuePlayServiceManager.playAudition((BaiduSyntheticSpeech) entry.getValue());
                }
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
                mSoundTask.remove(mCurrentPlaying);
                cyclePlay();
    }

    public void onDestory() {
        if (mSoundTask != null) {
            mSoundTask.clear();
        }

        MediaPlayerService.getInstance().onDestroy();
    }
}
