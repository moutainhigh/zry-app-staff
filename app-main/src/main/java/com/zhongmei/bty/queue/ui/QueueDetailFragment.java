package com.zhongmei.bty.queue.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueNotifyType;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.operates.message.content.QueuePredictWaitTimeResp;
import com.zhongmei.bty.queue.event.QueueListNoSelectedEvent;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.ui.view.ExMenuDetailDialog;
import com.zhongmei.bty.queue.ui.view.ExMenuDetailDialog_;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.bty.settings.util.MobileUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * 预订单详细信息
 */
@EFragment(R.layout.queueing_detail_layout)
public class QueueDetailFragment extends BasicFragment {

    private static final String TAG = QueueDetailFragment.class.getSimpleName();

    @ViewById(R.id.queue_empty_layout)
    protected RelativeLayout emptyLayout;

    @ViewById(R.id.queue_detail_layout)
    protected LinearLayout detailLayout;

    @ViewById(R.id.queue_detail_title1)
    protected TextView mTvTitle1;

    @ViewById(R.id.queue_detail_title2)
    protected TextView mTvTitle2;

    @ViewById(R.id.tv_queue_no)
    protected TextView mTvQueueNo;//排队号

    @ViewById(R.id.tv_queue_line)
    protected TextView mTvQueueLine;//排队队列

    @ViewById(R.id.tv_queue_customer)
    protected TextView mTvQueueCustomer;//顾客名称

    @ViewById(R.id.tv_queue_mobile)
    protected TextView mTvQueueMobile;//手机号

    @ViewById(R.id.tv_queue_person_count)
    protected TextView mTvQueuePersonCount;//排队人数

    @ViewById(R.id.tv_queue_order_dish)
    protected TextView mTvQueueDish;//预点菜单

    @ViewById(R.id.tv_queue_memo)
    protected TextView mTvQueueMemo;//预点菜单

    @ViewById(R.id.tv_queue_create_time)
    protected TextView mTvQueueCreateTime;//排队号

    @ViewById(R.id.tv_queue_remind)
    protected TextView mTvQueueRemind;//是否提醒

    @ViewById(R.id.tv_queue_status)
    protected TextView mTvQueueStatus;//排队状态

    @ViewById(R.id.tv_queue_status_time_label)
    protected TextView mTvQueueStatusTimeLabel;//状态对应时间

    @ViewById(R.id.tv_queue_status_time)
    protected TextView mTvQueueStatusTime;//状态对应时间

    private NewQueueBeanVo queueBeanVo;

    public void setQueueBeanVo(NewQueueBeanVo queueBeanVo) {
        this.queueBeanVo = queueBeanVo;
        if (detailLayout != null) {
            initView();
        }
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",
            Locale.getDefault());

    @AfterViews
    void initView() {
        if (queueBeanVo != null) {
            emptyLayout.setVisibility(View.GONE);
            detailLayout.setVisibility(View.VISIBLE);
            clear();
            refreshView();
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            detailLayout.setVisibility(View.GONE);
        }
    }

    void refreshView() {
        Queue queue = queueBeanVo.getQueue();
        mTvQueueNo.setText(String.format(getContext().getString(R.string.queue_number), queueBeanVo.getQueueNumber()));
        mTvQueueLine.setText(queueBeanVo.getQueueLine().getQueueName());
        mTvQueueCustomer.setText(QueueOpManager.getInstance().getQueueName(queue, getContext()));
        mTvQueueMobile.setText(TextUtils.isEmpty(queue.getMobile()) ? "" : MobileUtil.getMobileAndAreaCode(queue.getNationalTelCode(), queue.getMobile()));
        mTvQueuePersonCount.setText(queue.getRepastPersonCount() + getContext().getResources().getString(R.string.person));
        mTvQueueDish.setText(queueBeanVo.isOrderDish() ? getString(R.string.queue_null) : getString(R.string.queue_detail_queue_dish_detail_label));
        mTvQueueDish.setClickable(queueBeanVo.getTradeRelation() != null);
        mTvQueueMemo.setText(queue.getMemo());
        mTvQueueCreateTime.setText(format.format(new Date(queue.getCreateDateTime())));
        mTvQueueRemind.setText(getRemindStatus(queue));
        mTvQueueStatus.setText(getQueueStatus(queue));
        mTvQueueStatusTimeLabel.setText(getQueueStatusTimeLabel(queue));
        mTvQueueStatusTime.setText(getQueueStatusTime(queue));
        initTitle();
    }

    void initTitle() {
        if (queueBeanVo.getQueue().getQueueStatus() == QueueStatus.QUEUEING) {
            TaskContext.bindExecute(this, new SimpleAsyncTask<Integer>() {
                @Override
                public Integer doInBackground(Void... params) {
                    return QueueOpManager.getInstance().getWaiteCount(queueBeanVo.getQueue());
                }

                @Override
                public void onPostExecute(Integer integer) {
                    super.onPostExecute(integer);
                    mTvTitle1.setText(String.format(getString(R.string.queue_item_wait_table_count_hint), integer.intValue() + ""));
                }
            });

            ResponseListener<QueuePredictWaitTimeResp> listener = new ResponseListener<QueuePredictWaitTimeResp>() {
                @Override
                public void onResponse(ResponseObject<QueuePredictWaitTimeResp> response) {
                    QueuePredictWaitTimeResp resp = response.getContent();
                    if (resp != null) {

                        if (resp.getPossibilityWaitMins().longValue() > 0L) {
                            mTvTitle2.setText(String.format(getString(R.string.queue_predict_wait_time_hint), getQueuePredictWaitTime(resp.getPossibilityWaitMins())));
                        } else {
                            mTvTitle2.setText(R.string.queue_predict_time_at_once_in);
                        }
                    } else if (response.getStatusCode() == 1100) {
                        ToastUtil.showShortToast(getString(R.string.queue_predict_wait_not));
                        mTvTitle2.setText(String.format(getString(R.string.queue_predict_wait_time_hint), "-"));

                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                        mTvTitle2.setText(String.format(getString(R.string.queue_predict_wait_time_hint), "-"));

                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showShortToast(error.getMessage());

                }

            };
            QueueOpManager.getInstance().queuePredictWaitTime(queueBeanVo.getQueue(), getRootActivity(), LoadingResponseListener.ensure(listener, getFragmentManager()));

        } else {
            mTvTitle1.setText(String.format(getContext().getString(R.string.queue_number), queueBeanVo.getQueueNumber()));
            mTvTitle2.setText(getQueueStatus(queueBeanVo.getQueue()));
        }
    }

    @Click({R.id.tv_queue_order_dish})
    void onClick(View view) {
        if (ClickManager.getInstance().isClicked()) return;
        switch (view.getId()) {
            case R.id.tv_queue_order_dish:
                ExMenuDetailDialog exMenuDialog = new ExMenuDetailDialog_();
                exMenuDialog.setQueueVo(queueBeanVo);
                exMenuDialog.show(getActivity().getSupportFragmentManager(), "QueueDetailMenuDailog");
                break;
            default:
                break;
        }
    }

    void clear() {
        mTvQueueNo.setText("-");
        mTvQueueLine.setText("-");
        mTvQueueCustomer.setText("-");
        mTvQueueMobile.setText("-");
        mTvQueuePersonCount.setText("-");
        mTvQueueDish.setText("-");
        mTvQueueCreateTime.setText("-");
        mTvQueueRemind.setText("-");
        mTvQueueStatus.setText("-");
        mTvQueueStatusTime.setText("-");
    }

    private String getRemindStatus(Queue queue) {
        if (QueueNotifyType.NOTIFIED == queue.getNotifyType() // 已通知
                || QueueNotifyType.NOTIFIEDIVR == queue.getNotifyType()// 自动语音发出/已发语音
                || QueueNotifyType.NOTIFIEDSMS == queue.getNotifyType()// 已发短信
                || QueueNotifyType.NOTIFIEDVOICE == queue.getNotifyType()) {// 已叫号
            return getString(R.string.queue_detail_queue_reminded);
        } else {
            return getString(R.string.queue_detail_queue_not_remind);
        }
    }

    private String getQueueStatus(Queue queue) {
        String str = "-";
        switch (queue.getQueueStatus()) {
            case INVALID:
                str = getContext().getString(R.string.queue_ststus_passed);
                break;
            case ADMISSION:
                str = getContext().getString(R.string.queue_ststus_entranced);
                break;
            case QUEUEING:
                str = getContext().getString(R.string.queue_ststus_queuing);
                break;
            case CANCEL:
                str = getContext().getString(R.string.queue_ststus_canceled);
                break;
            default:
                break;
        }
        return str;
    }

    private String getQueueStatusTime(Queue queue) {
        String dataStr = "-";
        switch (queue.getQueueStatus()) {
            case INVALID:
                dataStr = format.format(new Date(queue.getSkipDatetime()));
                break;
            case ADMISSION:
                dataStr = format.format(new Date(queue.getInDateTime()));
                break;
            case QUEUEING:
                dataStr = QueueOpManager.getInstance().getQueueWaitTimeString(queue.getCreateDateTime(), getContext());
                break;
            case CANCEL:
                break;
            default:
                break;
        }
        return dataStr;
    }

    private String getQueueStatusTimeLabel(Queue queue) {
        String dataStr = "";
        switch (queue.getQueueStatus()) {
            case INVALID:
                dataStr = getString(R.string.queue_detail_queue_pass_time_label);
                break;
            case ADMISSION:
                dataStr = getString(R.string.queue_detail_queue_in_time_label);
                break;
            case QUEUEING:
                dataStr = getString(R.string.queue_detail_queue_waite_time_label);
                break;
            case CANCEL:
                dataStr = getString(R.string.queue_detail_queue_cancel_time_label);
                break;
            default:
                break;
        }
        return dataStr;
    }

    String getQueuePredictWaitTime(Long minute) {
        StringBuffer time = new StringBuffer("");
        if (minute / 60L > 0) {
            time.append(getContext().getString(R.string.queue_waitting_hour, minute / 60L));
        }
        time.append(getContext().getString(R.string.queue_waitting_min, minute % 60L));
        return time.toString();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queueBeanVo != null) {
            queueBeanVo.setSelected(false);
            EventBus.getDefault().post(new QueueListNoSelectedEvent());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
