package com.zhongmei.bty.common.util.audio.combination;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.common.util.audio.AudioBase;
import com.zhongmei.bty.common.util.audio.Common;
import com.zhongmei.bty.common.util.audio.combination.AudioMediaUtil.OnMediaLister;
import com.zhongmei.yunfu.util.ToastUtil;

/**
 * 三段式组合类：用于组合声音播报片段的路径 eg：请 a123 号顾客到出餐口取餐
 *
 * @date:2015-11-25 18:16:44
 */
public class ThreeSectionCom extends BaseComboAudio {

    private boolean isPrepare;

    private Context context;

    private ConcurrentLinkedQueue<String> filePathQueue;

    private Common common;

    private AudioBase audio;

    private AudioMediaUtil audioMediaUtil;

    private static ThreeSectionCom instance;

    private ThreeSectionCom(Context context, AudioBase audio, Common common) {
        this.context = context;
        this.audio = audio;
        this.common = common;
        audioMediaUtil = new AudioMediaUtil(context);
        isPrepare = false;
    }

    public void setAudioType(AudioBase audio) {
        this.audio = audio;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public static ThreeSectionCom getInstance(Context context, AudioBase audio, Common common) {
        synchronized (ThreeSectionCom.class) {
            if (instance == null) {
                instance = new ThreeSectionCom(context, audio, common);
            }
        }

        instance.setAudioType(audio);
        instance.setCommon(common);

        return instance;
    }

    public static boolean isHasInstance() {
        return instance != null;
    }

    /**
     * @Title: prepare
     * @Param prefix 声音片段开头缩写
     * @Param num 号码
     * @Param suffix 声音片段尾缩写
     * @Return ConcurrentLinkedQueue<String> 返回类型
     * 组合后的声音片段URL顺序集合
     */
    public ConcurrentLinkedQueue<String> prepare(String prefix, String num, String suffix) {
        if (TextUtils.isEmpty(num)) {
            return null;
        }

        filePathQueue = new ConcurrentLinkedQueue<String>();

        try {
            add(prefix);
            addBody(num);
            add(suffix);
        } catch (Exception e) {
            ToastUtil.showShortToast(R.string.voice_not_exist);
            Log.e("", e.getMessage());
        }
        isPrepare = true;
        return filePathQueue;
    }

    /**
     * @Title: prepare
     * @Description: 组合固定开头结尾的声音片段
     * @Param @param s body
     * @Param @return TODO
     * @Return ConcurrentLinkedQueue<String> 返回类型
     * 组合后的声音片段URL顺序集合
     */
    public ConcurrentLinkedQueue<String> prepare(String s) {
        return prepare(audio.getSimplePre(), s, audio.getSimpleSuf());
    }

    public ConcurrentLinkedQueue<String> prepare(String num, OnMediaLister mediaListener) {
        audioMediaUtil.setMediaLister(mediaListener);
        return this.prepare(num);
    }

    public ConcurrentLinkedQueue<String> prepare(String abbrOfStart, String num, String abbrOfEnd,
                                                 OnMediaLister mediaListener) {
        audioMediaUtil.setMediaLister(mediaListener);
        return this.prepare(abbrOfStart, num, abbrOfEnd);
    }

    @Override
    public void add(String audioPiece) throws Exception {
        if (audioPiece == null) {
            return;
        }
        filePathQueue.offer(audio.getAudioPiecePath(audioPiece));
    }

    private void addBody(String num) throws Exception {
        // 主要body
        char[] nums = num.toCharArray();
        for (char c : nums) {
            filePathQueue.offer(common.getAudioPiecePath(Character.toUpperCase(c) + ""));
        }
    }

    /**
     * @param isDownLoad
     * @param isDownLoad 下载文件
     */
    public void play(boolean isDownLoad) {
        if (isPrepare == false) {
            Log.e("audio error!", "Audio ddoes't prepare!");
            return;
        }
        audioMediaUtil.play(filePathQueue, isDownLoad);
        isPrepare = false;
    }

    public void playSingleAudio(String filePath) {
        audioMediaUtil.play(filePath);
    }

    /**
     * 释放资源
     */
    public void realse() {
        if (audioMediaUtil != null) {
            audioMediaUtil.release();
            audioMediaUtil = null;
        }
        instance = null;
    }

}
