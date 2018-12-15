package com.zhongmei.bty.basemodule.commonbusiness.manager;

import android.media.MediaPlayer;

import com.zhongmei.bty.basemodule.commonbusiness.bean.MediaPlayerFileVo;
import com.zhongmei.bty.basemodule.commonbusiness.service.MediaPlayerService;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 * 声音播放队列管理
 */
public class MediaPlayerQueueManager implements MediaPlayer.OnCompletionListener {
    /**
     * 微信加菜单
     */
    public static final int MEDIA_TYPE_WECHAT_ADDTRADE = 1;

    /**
     * 百度糯米新订单
     */
    public static final int MEDIA_TYPE_BAIDU_RICE_NEWTRADE = 2;

    /**
     * 必胜客新订单
     */
    public static final int MEDIA_TYPE_PIZZAHUT_NEWTRADE = 3;

    /**
     * 大众点评新订单
     */
    public static final int MEDIA_TYPE_DIANPING_NEWTRADE = 4;

    /**
     * 饿了么新订单
     */
    public static final int MEDIA_TYPE_ELEME_NEWTRADE = 5;

    /**
     * 微信新订单
     */
    public static final int MEDIA_TYPE_WECHAT_NEWTRADE = 6;

    /**
     * 熟客新订单
     */
    public static final int MEDIA_TYPE_SHUKE_NEWTRADE = 7;

    /**
     * 美团外卖新订单
     */
    public static final int MEDIA_TYPE_MEITUAN_TAKEOUT_NEWTRADE = 8;

    /**
     * 百度外卖新订单
     */
    public static final int MEDIA_TYPE_BAIDU_TAKEOUT_NEWTRADE = 9;

    /**
     * 未处理预订
     */
    public static final int MEDIA_TYPE_NEWBOOKING = 10;

    /**
     * 外卖退单申请
     */
    public static final int MEDIA_TYPE_TAKE_OUT_CANCEL = 11;

    /**
     * 广播叫号
     */
    public static final int MEDIA_TYPE_BROADCAST_NOTICE = 12;

    /**
     * 呼叫服务铃
     */
    public static final int MEDIA_TYPE_CALL_WAITER = 13;

    /**
     * 熟客加菜单
     */
    public static final int MEDIA_TYPE_FAMILIAR_ADDTRADE = 14;

    /**
     * 其他端支付
     */
    public static final int MEDIA_TYPE_OTHER_CLIENT_PAY = 15;

    /**
     * 自动接单
     */
    public static final int MEDIA_TYPE_NEW_AUTO_ACCEPT_TRADE = 16;

    /**
     * 配送取消
     */
    public static final int MEDIA_TYPE_DELIVERY_CANCEL = 17;

    /**
     * 订单取消
     */
    public static final int MEDIA_TYPE_CANCEL_TRADE = 18;

    /**
     * 饿了么外卖配送订单被取消
     */
    public static final int MEDIA_TYPE_ELEME_DELIVERY_ORDER_CANCEL = 19;

    private static MediaPlayerQueueManager mMediaPlayerQueueManager;
    private Map<Integer, Object> mSoundTask;//声音任务
    private Integer mCurrentPlaying;//当前正在播放的声音

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

    /**
     * 通用播放入口
     *
     * @param mediaType
     * @param resId
     */
    public void play(int mediaType, int resId) {
        if (mSoundTask != null && !mSoundTask.containsKey(mediaType)) {
            mSoundTask.put(mediaType, new MediaPlayerFileVo(2, resId));
        }
        cyclePlay();
    }

    /**
     * 合成语音播放
     */
    public void play(int mediaType, BaiduSyntheticSpeech speech) {
        if (mSoundTask != null && !mSoundTask.containsKey(mediaType)) {
            mSoundTask.put(mediaType, speech);
        }
        cyclePlay();
    }


    /**
     * 轮训播放
     */
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
        //移除当前播放完成的
        mSoundTask.remove(mCurrentPlaying);
        //播放下一个语音信息
        cyclePlay();
    }

    public void onDestory() {
        if (mSoundTask != null) {
            mSoundTask.clear();
        }

        MediaPlayerService.getInstance().onDestroy();
    }
}
