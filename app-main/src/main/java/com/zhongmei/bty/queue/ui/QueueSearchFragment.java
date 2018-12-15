package com.zhongmei.bty.queue.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.zhongmei.yunfu.R;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.queue.CallNoToSecondScreenHandler;
import com.zhongmei.bty.queue.adapter.QueueListAdapter2;
import com.zhongmei.bty.queue.event.QueueListNoSelectedEvent;
import com.zhongmei.bty.queue.event.QueueShowDetailEvent;
import com.zhongmei.bty.queue.manager.QueueDataManager;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.vo.NewQueueAreaVo;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @date 2015年8月27日上午10:46:59
 */
@EFragment(R.layout.queue_search_layout)
public class QueueSearchFragment extends BasicFragment implements QueueDataManager.DataChangedListener {

    public static final String TAG = QueueSearchFragment.class.getSimpleName();
    public static String mac_regex = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";

    @ViewById(R.id.queue_list)
    ListView queueListView;

    @ViewById(R.id.et_search_content)
    EditText mEtSearch;

    @ViewById(R.id.bt_search)
    Button mTbSearch;

    private QueueDataManager queueDataManager;

    private NewQueueAreaVo queueAreaVo;

    private List<NewQueueBeanVo> queueBeanVoList;

    private NewQueueBeanVo queueBeanVo;

    private QueueListAdapter2 queueListAdapter;

    private MyHandler mHander = new MyHandler();

    private CallNoToSecondScreenHandler hander = new CallNoToSecondScreenHandler(getActivity());

    private static final int RefreshListViewDelayMillis = 60 * 1000;
    private Runnable taskListRefresh = new Runnable() {
        @Override
        public void run() {
            if (queueListView != null) {
                queueListAdapter.notifyDataSetChanged();
                queueListView.postDelayed(this, 60 * 1000);
            }
        }
    };

    @AfterViews
    void init() {
        registerEventBus();
        mEtSearch.setText("");
        queueDataManager = new QueueDataManager();
        queueListAdapter = new QueueListAdapter2(getActivity());
        queueListAdapter.setHander(hander);
        queueListView.postDelayed(taskListRefresh, RefreshListViewDelayMillis);
        queueListView.setAdapter(queueListAdapter);
        queueListAdapter.setOnItemClickListener(new QueueListAdapter2.OnItemClickListener() {
            @Override
            public void onClick(NewQueueBeanVo queueBeanVo) {
                QueueSearchFragment.this.queueBeanVo = queueBeanVo;
                EventBus.getDefault().post(new QueueShowDetailEvent(queueBeanVo));
            }
        });
        initData();
        queueDataManager.register(this);
    }

    void initData() {
        mTbSearch.setEnabled(false);
        AsyncTaskCompat.executeParallel(new SimpleAsyncTask<Void>() {

            @Override
            public Void doInBackground(Void... params) {
                List<NewQueueAreaVo> queueAreaVoList = queueDataManager.getQueueAreaVoList();
                queueAreaVo = queueAreaVoList.get(0);
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mTbSearch.setEnabled(true);
                String input = mEtSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(input)) {
                    showList(input);
                }
            }
        });
    }

    @Click(R.id.bt_search)
    void onClick() {
        if (ClickManager.getInstance().isClicked()) return;
        queueBeanVo = null;
        String input = mEtSearch.getText().toString().trim();
        showList(input);
    }

    void showList(String input) {
        queueBeanVoList = QueueOpManager.getInstance().searchQueueList(queueAreaVo, input);
        boolean haiSelected = false;
        if (queueBeanVo != null && Utils.isNotEmpty(queueBeanVoList)) {
            for (NewQueueBeanVo queueVo : queueBeanVoList) {
                if (queueVo.getQueue().getId().longValue() == queueBeanVo.getQueue().getId().longValue()) {
                    haiSelected = true;
                    queueVo.setSelected(true);
                }
            }
        }
        if (!haiSelected) {
            EventBus.getDefault().post(new QueueShowDetailEvent(null));
        }
        queueListAdapter.setQueueList(queueBeanVoList);
    }


    /**
     * 详情关闭，取消选择
     *
     * @param event
     */
    public void onEventMainThread(QueueListNoSelectedEvent event) {
        queueBeanVo = null;
        if (queueListView != null && queueListAdapter != null)
            queueListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChanged() {
        Log.i(TAG, "onChaged");
        // 刷新
        /*
         * 快速刷新的时候，只处理最后一次的
         */
        Message msg = new Message();
        msg.what = mHander.msgWhat;

        if (mHander.hasMessages(mHander.msgWhat)) {
            mHander.removeMessages(mHander.msgWhat);
        }
        mHander.sendMessageDelayed(msg, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventBus();
        queueDataManager.unregister();
        queueListView.removeCallbacks(taskListRefresh);
    }

    /**
     * 用于数据库变更刷新数据
     */
    private class MyHandler extends Handler {

        int msgWhat = 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.what == msgWhat) {
                if (isAdded()) {
                    initData();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus();
    }
}
