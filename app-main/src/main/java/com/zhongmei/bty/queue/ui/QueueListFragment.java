package com.zhongmei.bty.queue.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.bty.queue.CallNoToSecondScreenHandler;
import com.zhongmei.bty.queue.QueueMainActivity;
import com.zhongmei.bty.queue.adapter.QueueLinePagerAdapter;
import com.zhongmei.bty.queue.adapter.QueueListAdapter2;
import com.zhongmei.bty.queue.event.QueueListNoSelectedEvent;
import com.zhongmei.bty.queue.event.QueueShowDetailEvent;
import com.zhongmei.bty.queue.event.SelectQueueLine;
import com.zhongmei.bty.queue.event.TableSelectEvent;
import com.zhongmei.bty.queue.event.UpdatePersonEvent;
import com.zhongmei.bty.queue.manager.QueueDataManager;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.vo.NewQueueAreaVo;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @date 2015年8月27日上午10:46:59
 */
@EFragment(R.layout.queue_list_layout)
public class QueueListFragment extends BasicFragment implements QueueDataManager.DataChangedListener {

    public static final String TAG = QueueListFragment.class.getSimpleName();

    public static String mac_regex = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";

    @ViewById(R.id.queue_list)
    ListView queueListView;

    @ViewById(R.id.queueline_layout)
    protected ViewPager vpQueueLine;

    @ViewById(R.id.queueline_pre)
    protected ImageView preImage;

    @ViewById(R.id.queueline_next)
    protected ImageView nextImage;

    private QueueDataManager queueDataManager;

    // 队列分类列表
    private List<NewQueueAreaVo> queueAreaVoList;

    private NewQueueAreaVo queueAreaVo;

    private NewQueueBeanVo queueBeanVo;

    private QueueListAdapter2 queueListAdapter;

    private QueueLinePagerAdapter pagerAdapter;

    private int currentPage = QueueMainActivity.PAGE_QUEUE_LIST;

    private int currentIndex = 0;

    private Map<Integer, BaiduSyntheticSpeech> speechCallMap;

    private CallNoToSecondScreenHandler hander = new CallNoToSecondScreenHandler(getActivity());

    private MyHandler mHander = new MyHandler();

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

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        if (pagerAdapter != null && queueListView != null)
            pagerAdapter.setType(currentPage);
    }

    @AfterViews
    void init() {
        registerEventBus();
        queueDataManager = new QueueDataManager();
        queueListAdapter = new QueueListAdapter2(getActivity());
        queueListAdapter.setHander(hander);
        queueListView.postDelayed(taskListRefresh, RefreshListViewDelayMillis);
        queueListView.setAdapter(queueListAdapter);
        queueListAdapter.setOnItemClickListener(new QueueListAdapter2.OnItemClickListener() {
            @Override
            public void onClick(NewQueueBeanVo queueBeanVo) {
                QueueListFragment.this.queueBeanVo = queueBeanVo;
                EventBus.getDefault().post(new QueueShowDetailEvent(queueBeanVo));
            }
        });
        pagerAdapter = new QueueLinePagerAdapter(getContext()) {
            public void selectQueueLine(NewQueueAreaVo queueAreaVo) {
                QueueListFragment.this.queueAreaVo = queueAreaVo;
                resetQueueList(currentPage);
                TableSelectEvent event = null;
                if (queueAreaVo.getQueueLine().getId() == 0) {
                    event = new TableSelectEvent(0, 0);
                } else if (queueAreaVoList.get(queueAreaVoList.size() - 1).getQueueLine().getId() == queueAreaVo.getQueueLine().getId()) {
                    event = new TableSelectEvent(queueAreaVo.getQueueLine().getMinPersonCount(), -1);
                } else {
                    event = new TableSelectEvent(queueAreaVo.getQueueLine().getMinPersonCount(), queueAreaVo.getQueueLine().getMaxPersonCount());
                }
                event.setName(queueAreaVo.getQueueLine().getQueueName());
                EventBus.getDefault().post(event);

            }
        };
        pagerAdapter.setType(currentPage);
        vpQueueLine.setAdapter(pagerAdapter);
        initData();
        queueDataManager.register(this);
    }

    void initData() {
        TaskContext.bindExecute(getActivity(), new SimpleAsyncTask<Void>() {

            @Override
            public Void doInBackground(Void... params) {
                queueAreaVoList = queueDataManager.getQueueAreaVoList();
                speechCallMap = QueueOpManager.getInstance().getQueueVoiceMap();
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pagerAdapter.setDataSet(queueAreaVoList);
                queueListAdapter.setSpeechMap(speechCallMap);
            }
        });
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
        /*TVClientService.setCheckListener(new TVListener() {
            @Override
            public void onConnect(List<String> successIP, List<String> errorIP) {
                if (errorIP.size() > 0) {
                    String errorMessage = MainApplication.getInstance().getString(R.string.queue_tv_client_service_error);
                    StringBuilder args = new StringBuilder();
                    for (Iterator<String> it = errorIP.iterator(); it.hasNext(); ) {
                        args.append(it.next());
                        if (it.hasNext()) {
                            args.append(",");
                        }
                    }
                    AppDialog.showHintDialog(String.format(errorMessage, args));
                }
            }
        });*/
    }


    /**
     * 输入人数
     *
     * @param selectEvent
     */
    public void onEventMainThread(final UpdatePersonEvent selectEvent) {
        QueueOpManager.getInstance().setAreaVoSelected(queueAreaVoList, selectEvent.personCount);
        pagerAdapter.notifyDataSetChanged();
        if (queueAreaVoList.contains(queueAreaVo)) {
            int num = queueAreaVoList.indexOf(queueAreaVo) + 1;
            currentIndex = num / 6;
            vpQueueLine.setCurrentItem(currentIndex);
        }
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

    /**
     * 切换列表
     * 点击气泡排队订单
     *
     * @param selectEvent
     */
    public void onEventMainThread(SelectQueueLine selectEvent) {
        QueueDal dal = OperatesFactory.create(QueueDal.class);
        try {
            Queue queue = dal.getQueueByUuid(selectEvent.uuid);
            QueueOpManager.getInstance().setAreaVoSelected(queueAreaVoList, queue.getRepastPersonCount());
            pagerAdapter.notifyDataSetChanged();
            if (queueAreaVoList.contains(queueAreaVo)) {
                int num = queueAreaVoList.indexOf(queueAreaVo) + 1;
                currentIndex = num / 6;
                vpQueueLine.setCurrentItem(currentIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 刷新界面
     *
     * @param currentPage
     */
    private void resetQueueList(final int currentPage) {
        setItemSelected();
        // 刷新列表
        if (currentPage == QueueMainActivity.PAGE_QUEUE_LIST) {
            if (queueAreaVo != null)
                queueListAdapter.setQueueList(queueAreaVo.getQueueingBeanVoList());
        } else if (currentPage == QueueMainActivity.PAGE_QUEUE_HISTORY_LIST) {
            if (queueAreaVo != null)
                queueListAdapter.setQueueList(queueAreaVo.getQueuedBeanVoList());
        }
    }

    private void setItemSelected() {
        if (currentPage == QueueMainActivity.PAGE_QUEUE_LIST) {
            if (queueBeanVo != null && Utils.isNotEmpty(queueAreaVo.getQueueingBeanVoList())) {
                for (NewQueueBeanVo queueVo : queueAreaVo.getQueueingBeanVoList()) {
                    if (queueVo.getQueue().getId().longValue() == queueBeanVo.getQueue().getId().longValue()) {
                        queueVo.setSelected(true);
                    }
                }
            }
        } else if (currentPage == QueueMainActivity.PAGE_QUEUE_HISTORY_LIST) {
            if (queueBeanVo != null && Utils.isNotEmpty(queueAreaVo.getQueuedBeanVoList())) {
                for (NewQueueBeanVo queueVo : queueAreaVo.getQueuedBeanVoList()) {
                    if (queueVo.getQueue().getId().longValue() == queueBeanVo.getQueue().getId().longValue()) {
                        queueVo.setSelected(true);
                    }
                }
            }
        }


    }


    /**
     * 翻页
     *
     * @param v
     */
    @Click({R.id.queueline_pre, R.id.queueline_next})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.queueline_pre:
                vpQueueLine.setCurrentItem(currentIndex - 1);
                break;
            case R.id.queueline_next:
                vpQueueLine.setCurrentItem(currentIndex + 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterEventBus();
        queueDataManager.unregister();
        queueListView.removeCallbacks(taskListRefresh);
        if (hander != null && hander.hasMessages(CallNoToSecondScreenHandler.disappearNoWhat)) {
            hander.removeMessages(CallNoToSecondScreenHandler.disappearNoWhat);
        }
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
}
