package com.zhongmei.bty.queue.ui;

import android.os.Bundle;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.queue_reportcenter_fragment)
public class QueueReportCenterFragment extends QueueBaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaceFragment(R.id.content, new QueueLossRateReportFragment_(), QueueLossRateReportFragment.class.getSimpleName());
    }
}
