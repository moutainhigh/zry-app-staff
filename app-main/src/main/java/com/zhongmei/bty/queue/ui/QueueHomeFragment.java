package com.zhongmei.bty.queue.ui;

import android.os.Bundle;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * 排对中心
 */
@EFragment(R.layout.queue_base_frangment)
public class QueueHomeFragment extends QueueBaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void loadLayout() {
        QueueLeftFragment leftFragment = new QueueLeftFragment_();
        QueueRightFragment rightFragment = new QueueRightFragment_();
        replaceChildFragment(R.id.queue_left, leftFragment, QueueLeftFragment.TAG);
        replaceChildFragment(R.id.queue_right, rightFragment, QueueRightFragment.TAG);
    }
}
