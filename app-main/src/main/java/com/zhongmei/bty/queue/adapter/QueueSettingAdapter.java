package com.zhongmei.bty.queue.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.ui.base.BaseActivity;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.ui.QueueSettingFragment;

import java.util.ArrayList;
import java.util.List;

public class QueueSettingAdapter extends BaseAdapter {
    private static final String TAG = QueueSettingAdapter.class.getSimpleName();

    private List<CommercialQueueLine> queueLineList;

    private List<Queue> queueList;

    private LayoutInflater mLayoutInflater;


    private Context context;

    public QueueSettingAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.queueLineList != null ? this.queueLineList.size() : 0;
    }

    public Object getItem(int index) {
        return this.queueLineList.get(index);
    }

    public long getItemId(int index) {
        return index;
    }

    public void setQueueLineList(List<CommercialQueueLine> queueLineList) {
        this.queueLineList = queueLineList;
    }

    public void setQueueList(List<Queue> queueList) {
        this.queueList = queueList;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.queue_setting_queueline, parent, false);
            holder.queueLineName = (TextView) convertView.findViewById(R.id.queueline_name_tx);
            holder.clean = (Button) convertView.findViewById(R.id.queueline_clean_tx);
            holder.queueingCount = (TextView) convertView.findViewById(R.id.queueing_count_tx);
            holder.queueingPersonCount = (TextView) convertView.findViewById(R.id.queueing_personcount_tx);
            holder.admissionCount = (TextView) convertView.findViewById(R.id.admission_count_tx);
            holder.admissionPersonCount = (TextView) convertView.findViewById(R.id.admission_personcount_tx);
            holder.invalidCount = (TextView) convertView.findViewById(R.id.invalid_count_tx);
            holder.invalidPersonCount = (TextView) convertView.findViewById(R.id.invalid_personcount_tx);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CommercialQueueLine queueLine = queueLineList.get(position);
        QueueLineDetai detai = getDetai(queueLine);
        holder.queueLineName.setText(detai.getQueueLineName());

        holder.queueingCount.setText(context.getString(R.string.queue_clean_table_count, detai.getQueueingCount()));
        holder.queueingPersonCount.setText(context.getString(R.string.queue_clean_person_count, detai.getQueueingPersonCount()));
        holder.admissionCount.setText(context.getString(R.string.queue_clean_table_count, detai.getAdmissionCount()));
        holder.admissionPersonCount.setText(context.getString(R.string.queue_clean_person_count, detai.getAdmissionPersonCount()));
        holder.invalidCount.setText(context.getString(R.string.queue_clean_table_count, detai.getInvalidCount()));
        holder.invalidPersonCount.setText(context.getString(R.string.queue_clean_person_count, detai.getInvalidPersonCount()));
        holder.clean.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                QueueSettingFragment.resetQueueType((BaseActivity) context, queueLine.getId(), new ResponseListener<QueueResp>() {
                    @Override
                    public void onResponse(ResponseObject<QueueResp> response) {
                        queueReset(queueLine);
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });

            }
        });
        return convertView;
    }

    private void queueReset(CommercialQueueLine queueLine) {
        if (queueList != null && queueList.size() > 0) {
            List<Queue> cleanList = new ArrayList<Queue>();
            for (Queue queue : queueList) {
                if (queueLine.getId().equals(queue.getQueueLineId())
                        && queue.getIsZeroOped().equalsValue(YesOrNo.NO.value())) {
                    cleanList.add(queue);
                }
            }

            try {
                if (cleanList.size() > 0) {
                    QueueOpManager.getInstance().cleanQueueList(cleanList);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e(TAG, "", e);
            }
        }
    }

    private QueueLineDetai getDetai(CommercialQueueLine queueLine) {
        QueueLineDetai detai = new QueueLineDetai();
        detai.setQueueLineName(queueLine.getQueueName());
        if (queueList != null && queueList.size() > 0) {
            for (Queue queue : queueList) {
                if (queueLine.getId().equals(queue.getQueueLineId())) {
                    if (queue.getQueueStatus().equalsValue(QueueStatus.QUEUEING.value())) {
                        detai.setQueueingCount(detai.getQueueingCount() + 1);
                        detai.setQueueingPersonCount(detai.getQueueingPersonCount() + queue.getRepastPersonCount());
                    } else if (queue.getQueueStatus().equalsValue(QueueStatus.ADMISSION.value())) {
                        detai.setAdmissionCount(detai.getAdmissionCount() + 1);
                        detai.setAdmissionPersonCount(detai.getAdmissionPersonCount() + queue.getRepastPersonCount());
                    } else if (queue.getQueueStatus().equalsValue(QueueStatus.INVALID.value())) {
                        detai.setInvalidCount(detai.getInvalidCount() + 1);
                        detai.setInvalidPersonCount(detai.getInvalidPersonCount() + queue.getRepastPersonCount());
                    }
                }

            }
        }

        return detai;
    }

    class ViewHolder {
        TextView queueLineName;// 队列名称

        Button clean;// 清零

        TextView queueingCount;// 正在排队桌数

        TextView queueingPersonCount;// 正在排队人数

        TextView admissionCount;// 入场桌数

        TextView admissionPersonCount;// 入场排队人数

        TextView invalidCount;// 过号排队桌数

        TextView invalidPersonCount;// 过号排队人数

    }

    class QueueLineDetai {
        String queueLineName;// 队列名称

        int queueingCount;// 正在排队桌数

        int queueingPersonCount;// 正在排队人数

        int admissionCount;// 入场桌数

        int admissionPersonCount;// 入场排队人数

        int invalidCount;// 过号排队桌数

        int invalidPersonCount;// 过号排队人数

        public String getQueueLineName() {
            return queueLineName;
        }

        public void setQueueLineName(String queueLineName) {
            this.queueLineName = queueLineName;
        }

        public int getQueueingCount() {
            return queueingCount;
        }

        public void setQueueingCount(int queueingCount) {
            this.queueingCount = queueingCount;
        }

        public int getQueueingPersonCount() {
            return queueingPersonCount;
        }

        public void setQueueingPersonCount(int queueingPersonCount) {
            this.queueingPersonCount = queueingPersonCount;
        }

        public int getAdmissionCount() {
            return admissionCount;
        }

        public void setAdmissionCount(int admissionCount) {
            this.admissionCount = admissionCount;
        }

        public int getAdmissionPersonCount() {
            return admissionPersonCount;
        }

        public void setAdmissionPersonCount(int admissionPersonCount) {
            this.admissionPersonCount = admissionPersonCount;
        }

        public int getInvalidCount() {
            return invalidCount;
        }

        public void setInvalidCount(int invalidCount) {
            this.invalidCount = invalidCount;
        }

        public int getInvalidPersonCount() {
            return invalidPersonCount;
        }

        public void setInvalidPersonCount(int invalidPersonCount) {
            this.invalidPersonCount = invalidPersonCount;
        }

    }

}
