package com.zhongmei.bty.basemodule.commonbusiness.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.zhongmei.bty.basemodule.commonbusiness.bean.MediaPlayerFileVo;
import com.zhongmei.yunfu.context.base.BaseApplication;

import java.io.FileInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class MediaPlayerService implements MediaPlayer.OnCompletionListener {
    private final String TAG = MediaPlayerService.class.getSimpleName();
    private MediaPlayer mMediaplayer;
    private MediaPlayer.OnCompletionListener mMediaPlayCompleteListener;
    private static MediaPlayerService mMediaPlayerService;
    private ThreadPoolExecutor mThreadPool;

    public static MediaPlayerService getInstance() {
        if (mMediaPlayerService == null) {
            mMediaPlayerService = new MediaPlayerService();
        }
        return mMediaPlayerService;
    }

    public void setmMediaPlayCompleteListener(MediaPlayer.OnCompletionListener mMediaPlayCompleteListener) {
        this.mMediaPlayCompleteListener = mMediaPlayCompleteListener;
    }


    public MediaPlayer.OnCompletionListener getmMediaPlayCompleteListener() {
        return mMediaPlayCompleteListener;
    }

    private MediaPlayerService() {
        mMediaplayer = new MediaPlayer();
        mMediaplayer.setOnCompletionListener(this);
        mThreadPool = new ThreadPoolExecutor(1, 3, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void play(final MediaPlayerFileVo playFileVo) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                switch (playFileVo.getFileType()) {
                    case 1:
                        play(playFileVo.getFilePath());
                        break;
                    case 2:
                        play(playFileVo.getResId());
                        break;
                }
            }
        });

    }

    private void play(String filePath) {
        try {
            if (mMediaplayer != null) {
                mMediaplayer.reset();
            }
            FileInputStream localFileInputStream = new FileInputStream(filePath);
            mMediaplayer.setDataSource(localFileInputStream.getFD());
            localFileInputStream.close();
            mMediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaplayer.prepare();
            mMediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaplayer.start();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void play(int resId) {
        try {
            mMediaplayer.reset();
            mMediaplayer = MediaPlayer.create(BaseApplication.sInstance, resId);
            mMediaplayer.setOnCompletionListener(this);
            mMediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaplayer.start();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public boolean isPlaying() {
        if (mMediaplayer != null) {
            return mMediaplayer.isPlaying();
        }
        return false;
    }

    public void pause() {
        if (mMediaplayer == null) {
            Log.e(TAG, "Mediaplayer is null");
            return;
        }
        mMediaplayer.pause();
    }

    public void stop() {
        if (mMediaplayer == null) {
            Log.e(TAG, "Mediaplayer is null");
            return;
        }
        mMediaplayer.stop();
    }

    public void onDestroy() {
        if (mMediaplayer != null) {
            mMediaplayer.release();
        }

        if (mThreadPool != null) {
            mThreadPool.shutdown();
        }

        if (mMediaPlayerService != null) {
            mMediaPlayerService = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
                if (mMediaPlayCompleteListener != null) {
            mMediaPlayCompleteListener.onCompletion(mp);
        }
    }
}
