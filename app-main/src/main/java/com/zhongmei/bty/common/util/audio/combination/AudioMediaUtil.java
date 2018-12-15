package com.zhongmei.bty.common.util.audio.combination;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;
import android.util.Log;

/**
 * @Date：2015年12月2日 上午9:30:40
 * @Description: 播放声音片段的类
 */
public class AudioMediaUtil {

    private static final String TAG = AudioMediaUtil.class.getSimpleName();

    private static AudioMediaUtil instance;

    private AssetManager assetManager;

    private Context mContext;

    private MediaPlayer mediaPlayer;

    private java.util.Queue<String> filePathQueue;

    /**
     * 是否播放下砸文件
     */
    private boolean isDownLoad = false;

    public AudioMediaUtil(Context context) {
        mContext = context;
        assetManager = mContext.getAssets();
        mediaPlayer = new MediaPlayer();
        filePathQueue = new ConcurrentLinkedQueue<String>();
        mediaPlayer.setOnCompletionListener(completeListener);
    }

    /**
     * 获得单例
     */
    public static AudioMediaUtil getInstance(Context context) {
        synchronized (AudioMediaUtil.class) {
            if (instance == null) {
                instance = new AudioMediaUtil(context);
            }
        }

        return instance;
    }

    OnCompletionListener completeListener = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            String filePath = filePathQueue.poll();
            if (!TextUtils.isEmpty(filePath)) {
                Log.e("audio path", filePath);
                play(filePath);
            } else {
                if (mediaListener != null)
                    mediaListener.onComplete();
                mediaPlayer.reset();
            }
        }
    };

    private OnMediaLister mediaListener;

    public void play(String filePath) {
        try {
            mediaPlayer.reset();
            if (isDownLoad) {
                FileInputStream localFileInputStream = new FileInputStream(filePath);
                mediaPlayer.setDataSource(localFileInputStream.getFD());
                localFileInputStream.close();
            } else {
                AssetFileDescriptor fileDescriptor = assetManager.openFd(filePath);
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
            }
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "", e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "", e);
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
    }

    public void play(ConcurrentLinkedQueue<String> queue, boolean isDownLoad) {
        this.isDownLoad = isDownLoad;
        if (queue == null || queue.size() == 0) {
            return;
        }
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            filePathQueue.clear();
        } catch (IllegalStateException e) {
            Log.e(TAG, "", e);
        }
        filePathQueue = queue;
        play(queue.poll());
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public void setMediaLister(OnMediaLister mediaListener) {
        this.mediaListener = mediaListener;
    }


    /**
     * @Date：2015年12月2日 上午9:31:20
     * @Description: 在播放完声音后调用该监听器
     */
    public interface OnMediaLister {
        public void onComplete();
    }
}
