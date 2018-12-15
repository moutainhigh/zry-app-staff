package com.zhongmei.bty.settings.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.R;

/**
 * 语音合成与下载设置
 */
@EFragment(R.layout.speech_synthesis_setting)
public class SpeechSynthesisSettingFragment extends BasicFragment {
    private static final String TAG = SpeechSynthesisSettingFragment.class.getSimpleName();

    // 广播
    public static final int BROADCAST_VOICE = 1;

    // 叫号
    public static final int CALL_VOICE = 2;

    /**
     * 广播语音
     */
    @ViewById(R.id.broadcast_voice)
    protected TextView tvBroadcastVoice;

    /**
     * 叫号语音
     */
    @ViewById(R.id.call_voice)
    protected TextView tvCallvoice;

    private int pageIndex = BROADCAST_VOICE;

    private SpeechBroadcastSettingFragment broadcastSettingFragment;

    private SpeechQueueSettingFragment queueSettingFragment;


    @AfterViews
    void init() {
        broadcastSettingFragment = new SpeechBroadcastSettingFragment_();
        queueSettingFragment = new SpeechQueueSettingFragment_();
        switchPage();
    }


    @Click({R.id.call_voice, R.id.broadcast_voice})
    void initListener(View v) {
        switch (v.getId()) {
            case R.id.broadcast_voice:
                pageIndex = BROADCAST_VOICE;
                switchPage();
                break;
            case R.id.call_voice:
                pageIndex = CALL_VOICE;
                switchPage();
                break;
            default:
                break;
        }
    }


    /**
     * 更换按钮颜色背景
     */
    private void switchPage() {
        if (pageIndex == BROADCAST_VOICE) {
            tvBroadcastVoice.setBackgroundResource(R.color.write);
            tvBroadcastVoice.setTextColor(getResources().getColor(R.color.text_blue));
            tvCallvoice.setBackgroundResource(R.color.group_title_bg);
            tvCallvoice.setTextColor(getResources().getColor(R.color.text_white));
        } else {
            tvBroadcastVoice.setBackgroundResource(R.color.group_title_bg);
            tvBroadcastVoice.setTextColor(getResources().getColor(R.color.text_white));
            tvCallvoice.setBackgroundResource(R.color.write);
            tvCallvoice.setTextColor(getResources().getColor(R.color.text_blue));
        }
        switchFragment();
    }

    private void switchFragment() {
        if (pageIndex == BROADCAST_VOICE) {
            if (broadcastSettingFragment == null) {
                broadcastSettingFragment = new SpeechBroadcastSettingFragment_();
            }
            if (!broadcastSettingFragment.isAdded()) {
                replaceChildFragment(R.id.fl_content, broadcastSettingFragment, SpeechBroadcastSettingFragment.class.getSimpleName());
            }
        } else {
            if (queueSettingFragment == null) {
                queueSettingFragment = new SpeechQueueSettingFragment_();
            }
            if (!queueSettingFragment.isAdded()) {
                replaceChildFragment(R.id.fl_content, queueSettingFragment, SpeechQueueSettingFragment.class.getSimpleName());
            }
        }
    }
}
