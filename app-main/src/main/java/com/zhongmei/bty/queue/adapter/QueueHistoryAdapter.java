package com.zhongmei.bty.queue.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
import com.zhongmei.bty.queue.event.QueueShowChooseTableEvent;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.bty.settings.fragment.QueueSettingSwitchFragment;
import com.zhongmei.bty.settings.util.MobileUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class QueueHistoryAdapter extends BaseAdapter {
    private static final String TAG = QueueHistoryAdapter.class.getSimpleName();

    private List<NewQueueBeanVo> queueList = new ArrayList<>();

    public void setQueueList(List<NewQueueBeanVo> queueList) {
        this.queueList = queueList;
        notifyDataSetChanged();
    }

    private FragmentActivity mContext;

    public QueueHistoryAdapter(FragmentActivity context) {
        mContext = context;
    }

    private NewQueueBeanVo entraceQueue;

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return queueList.size();
    }

    @Override
    public Object getItem(int position) {
        return queueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView mQueueHistoryNumber;

        TextView mQueueHistorTime;

        TextView mQueueHistorPersonCount;

        TextView mQueueHistoryName;

        TextView mQueueHistoryPhone;

        ImageView mIvQueueStatus;

        ImageView mIvQueueGoin;

        ImageView mIvQueueRevocation;

        // 预点菜
        ImageView exMenu;

    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.queue_history_listview_item, null);

            holder = new ViewHolder();
            holder.mQueueHistoryNumber = (TextView) convertView.findViewById(R.id.id_queue_history_number);
            holder.mQueueHistorTime = (TextView) convertView.findViewById(R.id.id_queue_history_time_text);
            holder.mQueueHistorPersonCount = (TextView) convertView.findViewById(R.id.id_queue_history_personcount);
            holder.mQueueHistoryName = (TextView) convertView.findViewById(R.id.id_queue_history_name);
            holder.mQueueHistoryPhone = (TextView) convertView.findViewById(R.id.id_queue_history_phone);
            holder.mIvQueueStatus = (ImageView) convertView.findViewById(R.id.queue_history_status);
            holder.mIvQueueGoin = (ImageView) convertView.findViewById(R.id.lv_queue_goin);
            holder.mIvQueueRevocation = (ImageView) convertView.findViewById(R.id.lv_queue_revocation);
            holder.exMenu = (ImageView) convertView.findViewById(R.id.booking_ex_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NewQueueBeanVo item = queueList.get(position);
        final Queue queue = item.getQueue();
        holder.mQueueHistoryNumber.setText(item.getQueueNumber());
        String name = queue.getName();
        if (name == null || name.length() == 0) {
            name = mContext.getString(R.string.customer_no_name2);
        }
        holder.mQueueHistoryName.setText(name);
        holder.mQueueHistoryPhone.setText(MobileUtil.getMobileAndAreaCode(queue.getNationalTelCode(), queue.getMobile()));
        holder.mQueueHistorTime.setText(String.format(mContext.getString(R.string.queue_waiting_time), DateTimeUtils.formatDateTime(queue.getCreateDateTime(), "HH:mm")));
        holder.mQueueHistorPersonCount.setText(String.valueOf(queue.getRepastPersonCount()) + mContext.getResources().getString(R.string.person));

        if (item.isOrderDish()) {
            holder.exMenu.setVisibility(View.VISIBLE);
        } else {
            holder.exMenu.setVisibility(View.INVISIBLE);
        }

        if (queue.getQueueStatus() == QueueStatus.INVALID) {
            holder.mIvQueueStatus.setBackgroundResource(R.drawable.queue_already_pass);
            holder.mIvQueueGoin.setVisibility(View.VISIBLE);
            holder.mIvQueueRevocation.setVisibility(View.VISIBLE);
            holder.mIvQueueGoin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MobclickAgentEvent.onEvent(UserActionCode.PD020010);
                    try {
                        //判断是否使用开台入场
                        if (SpHelper.getDefault().getBoolean(QueueSettingSwitchFragment.QUEUE_IN_SWITCH, true)) {
                            entraceQueue = item;
                            EventBus.getDefault().post(new QueueShowChooseTableEvent(true, item));
                        } else {
                            updateQueueStatus(QueueReq.Type.IN, queue);//入场请求营销发券和实时更新微信排队状态;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }

                }
            });
        } else {
            holder.mIvQueueStatus.setBackgroundResource(R.drawable.queue_already_goin);
            holder.mIvQueueGoin.setVisibility(View.INVISIBLE);
            holder.mIvQueueRevocation.setVisibility(View.INVISIBLE);
        }

        holder.mIvQueueRevocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                QueueOperates operates = OperatesFactory.create(QueueOperates.class);
                CalmResponseListener<ResponseObject<Queue>> listener = new CalmResponseListener<ResponseObject<Queue>>() {
                    @Override
                    public void onError(NetError error) {
                        ToastUtil.showShortToast(error.getVolleyError().getMessage());
                    }

                    @Override
                    public void onSuccess(ResponseObject<Queue> data) {
                        if (data.getContent() != null) {
                            ToastUtil.showShortToast(R.string.operate_success);
                        } else {
                            ToastUtil.showShortToast(data.getMessage());
                        }
                    }
                };
                operates.queueRecoverInvalid(queue.getUuid(), queue.getModifyDateTime(), mContext, listener);
            }
        });

        return convertView;
    }

    /**
     * 更新队列状态
     */
    private void updateQueueStatus(@QueueReq.Type final int type, final Queue queue) {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        ResponseListener<QueueResp> listener = new ResponseListener<QueueResp>() {

            @Override
            public void onResponse(ResponseObject<QueueResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(R.string.booking_request_success);
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }

        };
        operates.updateQueue(type, queue, LoadingResponseListener.ensure(listener, mContext.getSupportFragmentManager()));
    }
}
