package com.zhongmei.bty.queue.ui;

import android.os.Bundle;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.queue.QueueMainActivity;
import com.zhongmei.bty.queue.event.SwictPageEvent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;


@EFragment(R.layout.queue_left_layout)
public class QueueLeftFragment extends QueueBaseFragment {

    public static final String TAG = QueueLeftFragment.class.getSimpleName();

    private int currentPage = QueueMainActivity.PAGE_QUEUE_LIST;

    private QueueListFragment queueListFragment;
    private QueueSearchFragment queueSearchFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        registerEventBus();
        queueListFragment = new QueueListFragment_();
        queueSearchFragment = new QueueSearchFragment_();
        changePage();
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

    private void changePage() {
        if (currentPage == QueueMainActivity.PAGE_QUEUE_LIST) {
            if (queueListFragment == null) {
                queueListFragment = new QueueListFragment_();
            }
            queueListFragment.setCurrentPage(currentPage);
            if (!queueListFragment.isAdded()) {
                replaceChildFragment(R.id.left_root_layout, queueListFragment, QueueListFragment.class.getSimpleName());
            }
        } else if (currentPage == QueueMainActivity.PAGE_QUEUE_SEARCH) {
            queueSearchFragment = new QueueSearchFragment_();
            if (!queueSearchFragment.isAdded()) {
                replaceChildFragment(R.id.left_root_layout, queueSearchFragment, QueueSearchFragment.class.getSimpleName());
            }
        } else if (currentPage == QueueMainActivity.PAGE_QUEUE_HISTORY_LIST) {
            if (queueListFragment == null) {
                queueListFragment = new QueueListFragment_();
            }
            queueListFragment.setCurrentPage(currentPage);
            if (!queueListFragment.isAdded()) {
                replaceChildFragment(R.id.left_root_layout, queueListFragment, QueueListFragment.class.getSimpleName());
            }
        }
    }

    public void onEventMainThread(SwictPageEvent selectEvent) {
        //只在创建队列页和空闲桌台页的时候记录当前page
        if (selectEvent.page == QueueMainActivity.PAGE_QUEUE_LIST
                || selectEvent.page == QueueMainActivity.PAGE_QUEUE_SEARCH
                || selectEvent.page == QueueMainActivity.PAGE_QUEUE_HISTORY_LIST) {
            currentPage = selectEvent.page;
            changePage();
        }
    }
}
