package com.zhongmei.yunfu.ui.base;

import android.os.Bundle;

import com.zhongmei.yunfu.util.MobclickAgentEvent;


public class MobclickAgentFragment extends BasicFragment {

    boolean isCreated = false;
    long startTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgentEvent.onPageStart(getClass().getSimpleName());
        if (!isCreated) {
            isCreated = true;
            MobclickAgentEvent.onEventValue(getActivity(), getClass().getSimpleName(), (int) (System.currentTimeMillis() - startTime));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgentEvent.onPageEnd(getClass().getSimpleName());
    }
}
