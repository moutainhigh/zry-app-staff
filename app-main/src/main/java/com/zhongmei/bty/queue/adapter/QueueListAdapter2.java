package com.zhongmei.bty.queue.adapter;

import android.annotation.SuppressLint;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueNotifyType;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberReq;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberResp;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.queue.CallNoToSecondScreenHandler;
import com.zhongmei.bty.queue.event.QueueShowChooseTableEvent;
import com.zhongmei.bty.queue.manager.QueueOpManager;
import com.zhongmei.bty.queue.ui.view.ExMenuDetailDialog;
import com.zhongmei.bty.queue.ui.view.ExMenuDetailDialog_;
import com.zhongmei.bty.queue.ui.view.ModifyQueueDialogFragment;
import com.zhongmei.bty.queue.ui.view.ModifyQueueDialogFragment_;
import com.zhongmei.bty.queue.ui.view.QueueRemindPopupWindow;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.bty.settings.fragment.QueueSettingSwitchFragment;
import com.zhongmei.bty.settings.fragment.SpeechQueueSettingFragment;
import com.zhongmei.bty.settings.util.MobileUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class QueueListAdapter2 extends BaseAdapter {
    private static final String TAG = QueueListAdapter2.class.getSimpleName();

    private List<NewQueueBeanVo> queueList = new ArrayList<>();

    private ExMenuDetailDialog exMenuDialog;

    private CallNoToSecondScreenHandler mHander;

    private Map<Integer, BaiduSyntheticSpeech> speechMap;//叫号模板

    public void setSpeechMap(Map<Integer, BaiduSyntheticSpeech> speechMap) {
        this.speechMap = speechMap;
    }

    //当前提醒的Queue
    private Queue remindQueue;

    private QueueRemindPopupWindow remindPopupWindow;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setQueueList(List<NewQueueBeanVo> queueList) {
        this.queueList = queueList;

        //检查当前提醒的Queue是否还存在，若不存在dismiss remindPopup
        boolean isFound = false;
        if (remindQueue != null && queueList != null) {
            for (NewQueueBeanVo beanVo : queueList) {
                Queue queue = beanVo.getQueue();
                if (remindQueue != null
                        && remindQueue.getId() != null
                        && queue.getId() != null
                        && queue.getId().equals(remindQueue.getId())) {
                    isFound = true;
                    break;
                }
            }
        }
        if (!isFound) {
            if (remindPopupWindow != null && remindPopupWindow.isShowing()) {
                remindPopupWindow.dismiss();
            }
        }
        notifyDataSetChanged();
    }

    public void setHander(CallNoToSecondScreenHandler mHander) {
        this.mHander = mHander;
    }

    private FragmentActivity mContext;

    public QueueListAdapter2(FragmentActivity context) {
        mContext = context;
        speechMap = new HashMap<>();
    }

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


    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryViewHolder historyViewHolder = null;
        QueueingViewHolder queueingViewHolder = null;
        final NewQueueBeanVo item = queueList.get(position);
        final Queue queue = item.getQueue();
        switch (queue.getQueueStatus()) {
            case ADMISSION:
            case INVALID:
                if (convertView == null || convertView.getTag(R.id.id_queue_history_number) == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.queue_history_listview_item, null);
                    historyViewHolder = new HistoryViewHolder();
                    bindHistoryView(convertView, historyViewHolder);
                } else {
                    historyViewHolder = (HistoryViewHolder) convertView.getTag(R.id.id_queue_history_number);
                }
                initHistoryViewData(item, historyViewHolder);
                break;
            case QUEUEING:
                if (convertView == null || convertView.getTag(R.id.queue_list_number_tv) == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.queue_list_item, null);
                    queueingViewHolder = new QueueingViewHolder();
                    bindQueueingView(convertView, queueingViewHolder);
                } else {
                    queueingViewHolder = (QueueingViewHolder) convertView.getTag(R.id.queue_list_number_tv);
                }
                initQueueingViewData(item, queueingViewHolder, position);
                break;
        }
        return convertView;
    }

    void bindHistoryView(View convertView, HistoryViewHolder historyViewHolder) {
        historyViewHolder.mQueueHistoryNumber = (TextView) convertView.findViewById(R.id.id_queue_history_number);
        historyViewHolder.mQueueHistorTime = (TextView) convertView.findViewById(R.id.id_queue_history_time_text);
        historyViewHolder.mQueueHistorPersonCount = (TextView) convertView.findViewById(R.id.id_queue_history_personcount);
        historyViewHolder.mQueueHistoryName = (TextView) convertView.findViewById(R.id.id_queue_history_name);
        historyViewHolder.mQueueHistoryPhone = (TextView) convertView.findViewById(R.id.id_queue_history_phone);
        historyViewHolder.mIvQueueStatus = (ImageView) convertView.findViewById(R.id.queue_history_status);
        historyViewHolder.mIvQueueGoin = (ImageView) convertView.findViewById(R.id.lv_queue_goin);
        historyViewHolder.mIvQueueRevocation = (ImageView) convertView.findViewById(R.id.lv_queue_revocation);
        historyViewHolder.exMenu = (ImageView) convertView.findViewById(R.id.booking_ex_menu);
        historyViewHolder.mTvQueueDetail = (TextView) convertView.findViewById(R.id.queue_history_show_detail);
        historyViewHolder.mSelectedView = convertView.findViewById(R.id.history_selected_view);
        convertView.setTag(R.id.id_queue_history_number, historyViewHolder);
    }

    private void bindQueueingView(View convertView, QueueingViewHolder viewHolder) {
        viewHolder.tv_number = (TextView) convertView.findViewById(R.id.queue_list_number_tv);
        viewHolder.ll_number = (LinearLayout) convertView.findViewById(R.id.ll_number);
        viewHolder.tv_personCount = (TextView) convertView.findViewById(R.id.queue_list_personcount);
        viewHolder.tv_time = (TextView) convertView.findViewById(R.id.queue_list_time);
        viewHolder.tv_userName = (TextView) convertView.findViewById(R.id.queue_list_username);
        viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.queue_list_phonenumber);
        viewHolder.tv_pass = (TextView) convertView.findViewById(R.id.queue_list_pass);
        viewHolder.tv_in = (TextView) convertView.findViewById(R.id.queue_list_in);
        viewHolder.tv_remind = (TextView) convertView.findViewById(R.id.queue_list_remind);
        viewHolder.tv_modify = (TextView) convertView.findViewById(R.id.queue_list_modify);
        viewHolder.tv_remind_count = (TextView) convertView.findViewById(R.id.tv_remind_count);
        viewHolder.img_from = (ImageView) convertView.findViewById(R.id.queue_list_ordersource);
        viewHolder.remind_status = (ImageView) convertView.findViewById(R.id.queue_remind_status);
        viewHolder.ll_remind = (LinearLayout) convertView.findViewById(R.id.ll_remind);
        viewHolder.exMenu = (ImageView) convertView.findViewById(R.id.booking_ex_menu);
        viewHolder.mSelectedView = convertView.findViewById(R.id.selected_view);
        viewHolder.mTvQueueDetail = (TextView) convertView.findViewById(R.id.queue_show_detail);
        convertView.setTag(R.id.queue_list_number_tv, viewHolder);
    }

    private void initQueueingViewData(NewQueueBeanVo item, QueueingViewHolder viewHolder, int position) {
        bindClickListener(viewHolder, item);
        Queue queue = item.getQueue();
        viewHolder.tv_number.setText(String.format(mContext.getString(R.string.queue_number), item.getQueueNumber()));
        viewHolder.tv_personCount.setText(queue.getRepastPersonCount() + mContext.getResources().getString(R.string.person));
        viewHolder.tv_time.setText(QueueOpManager.getInstance().getQueueWaitTimeString(queue.getCreateDateTime(), mContext));
        viewHolder.tv_phone.setText(TextUtils.isEmpty(queue.getMobile()) ? "" : MobileUtil.getMobileAndAreaCode(queue.getNationalTelCode(), queue.getMobile()));
        viewHolder.tv_remind_count.setText(null != queue.queueExtra && queue.queueExtra.callCount != null ? queue.queueExtra.callCount + "" : 0 + "");
        viewHolder.tv_remind_count.setSelected(true);
        viewHolder.tv_userName.setText(QueueOpManager.getInstance().getQueueName(queue, mContext));
        if (TextUtils.isEmpty(queue.getMobile())) {
            viewHolder.remind_status.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.remind_status.setVisibility(View.VISIBLE);

            if (QueueNotifyType.NOTIFIED == queue.getNotifyType() // 已通知
                    || QueueNotifyType.NOTIFIEDIVR == queue.getNotifyType()// 自动语音发出/已发语音
                    || QueueNotifyType.NOTIFIEDSMS == queue.getNotifyType()// 已发短信
                    || QueueNotifyType.NOTIFIEDVOICE == queue.getNotifyType()) {// 已叫号
                viewHolder.remind_status.setBackgroundResource(R.drawable.queue_remind_already);
            } else {
                viewHolder.remind_status.setBackgroundResource(R.drawable.queue_remind);
            }
        }
        viewHolder.img_from.setBackgroundResource(QueueOpManager.getInstance().getDrawable(queue));

        if (item.isOrderDish()) {
            viewHolder.exMenu.setVisibility(View.VISIBLE);
        } else {
            viewHolder.exMenu.setVisibility(View.INVISIBLE);
        }
        if (item.isSelected()) {
            viewHolder.mSelectedView.setVisibility(View.VISIBLE);
            if (onItemClickListener != null) onItemClickListener.onClick(item);
        } else {
            viewHolder.mSelectedView.setVisibility(View.GONE);
        }
        if (remindQueue != null
                && remindQueue.getId().equals(queue.getId())
                && remindPopupWindow != null) {
            remindPopupWindow.showPopupWindow(viewHolder.ll_remind);
        }
    }

    private void bindClickListener(QueueingViewHolder viewHolder, NewQueueBeanVo item) {
        viewHolder.ll_number.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_pass.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_in.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_remind.setOnClickListener(newClick(viewHolder, item));
        viewHolder.ll_remind.setOnClickListener(newClick(viewHolder, item));
        viewHolder.exMenu.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_modify.setOnClickListener(newClick(viewHolder, item));
        viewHolder.mTvQueueDetail.setOnClickListener(newClick(viewHolder, item));
    }

    public View.OnClickListener newClick(final QueueingViewHolder viewHolder, final NewQueueBeanVo item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }
                Queue queue = item.getQueue();
                switch (v.getId()) {
                    case R.id.queue_list_pass:
                        MobclickAgentEvent.onEvent(UserActionCode.PD020009);
                        queue.setQueueStatus(QueueStatus.INVALID);
                        queue.setSkipDatetime(new Date().getTime());
                        doUpdateSecondScreen();
                        QueueOpManager.getInstance().updateQueueStatus(QueueReq.Type.PASS, queue, mContext, null);
                        break;
                    case R.id.queue_list_in:
                        MobclickAgentEvent.onEvent(UserActionCode.PD020008);
                        boolean isSetTemplate = SpHelper.getDefault().getBoolean(SpeechQueueSettingFragment.CALL_VOICE_SYNTHESIS, false);
                        int position = queueList.indexOf(item);
                        if (position == queueList.size() - 1) {
                            QueueOpManager.getInstance().queueCallNo(mContext, null, speechMap, item.getQueueNumber(), "");
                        } else {
                            NewQueueBeanVo nextQueueVo = queueList.get(position + 1);
                            if (nextQueueVo.getQueue().getQueueStatus() == QueueStatus.QUEUEING && nextQueueVo.getQueueLine().getId().longValue() == item.getQueueLine().getId().longValue()) {
                                QueueOpManager.getInstance().queueCallNo(mContext, null, speechMap, item.getQueueNumber(), nextQueueVo.getQueueNumber());
                                if (speechMap != null && speechMap.containsKey(2) && !TextUtils.isEmpty(speechMap.get(2).getContent()) && isSetTemplate)
                                    QueueOpManager.getInstance().queueUpdateRemindCount(mContext, nextQueueVo.getQueue());
                            } else {
                                QueueOpManager.getInstance().queueCallNo(mContext, null, speechMap, item.getQueueNumber(), "");
                            }
                        }
                        //判断是否使用开台入场
                        if (SpHelper.getDefault().getBoolean(QueueSettingSwitchFragment.QUEUE_IN_SWITCH, true)) {
                            EventBus.getDefault().post(new QueueShowChooseTableEvent(true, item));
                        } else {
                            //更新第二屏排队队列
                            doUpdateSecondScreen();
                            QueueOpManager.getInstance().updateQueueStatus(QueueReq.Type.IN, queue, mContext, null);
                        }
                        break;
                    case R.id.queue_list_remind:
                        MobclickAgentEvent.onEvent(UserActionCode.PD020007);
                        showOnlyCallNum(item, viewHolder.tv_remind);
                        break;
                    case R.id.ll_number:
                        // 显示小票重打提示框
                        showPrint(item, v);
                        break;
                    case R.id.ll_remind:
                        // 提醒
                        showRemind(item, v);
                        break;

                    case R.id.booking_ex_menu:
                        showExMenu(item);
                        break;
                    case R.id.queue_list_modify:
                        ModifyQueueDialogFragment modifyQueueDialogFragment = new ModifyQueueDialogFragment_();
                        modifyQueueDialogFragment.setQueue(item);
                        modifyQueueDialogFragment.show(mContext.getSupportFragmentManager(), ModifyQueueDialogFragment.class.getSimpleName());
                        break;
                    case R.id.queue_show_detail:
                        for (NewQueueBeanVo bean : queueList) {
                            bean.setSelected(false);
                        }
                        item.setSelected(true);
                        notifyDataSetChanged();
                        break;
                }
            }
        };
    }

    private void initHistoryViewData(final NewQueueBeanVo item, HistoryViewHolder historyViewHolder) {
        final Queue queue = item.getQueue();
        historyViewHolder.mQueueHistoryNumber.setText(String.format(mContext.getString(R.string.queue_number), item.getQueueNumber()));
        historyViewHolder.mQueueHistoryName.setText(QueueOpManager.getInstance().getQueueName(queue, mContext));
        historyViewHolder.mQueueHistoryPhone.setText(MobileUtil.getMobileAndAreaCode(queue.getNationalTelCode(), queue.getMobile()));
        historyViewHolder.mQueueHistorTime.setText(String.format(mContext.getString(R.string.queue_waiting_time), DateTimeUtils.formatDateTime(queue.getCreateDateTime(), "HH:mm")));
        historyViewHolder.mQueueHistorPersonCount.setText(String.valueOf(queue.getRepastPersonCount()) + mContext.getResources().getString(R.string.person));

        if (item.isOrderDish()) {
            historyViewHolder.exMenu.setVisibility(View.VISIBLE);
        } else {
            historyViewHolder.exMenu.setVisibility(View.INVISIBLE);
        }
        if (item.isSelected()) {
            historyViewHolder.mSelectedView.setVisibility(View.VISIBLE);
            if (onItemClickListener != null) onItemClickListener.onClick(item);
        } else {
            historyViewHolder.mSelectedView.setVisibility(View.GONE);
        }
        if (queue.getQueueStatus() == QueueStatus.INVALID) {
            historyViewHolder.mIvQueueStatus.setBackgroundResource(R.drawable.queue_already_pass);
            historyViewHolder.mIvQueueGoin.setVisibility(View.VISIBLE);
            historyViewHolder.mIvQueueRevocation.setVisibility(View.VISIBLE);
            historyViewHolder.mIvQueueGoin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MobclickAgentEvent.onEvent(UserActionCode.PD020010);
                    try {
                        //判断是否使用开台入场
                        if (SpHelper.getDefault().getBoolean(QueueSettingSwitchFragment.QUEUE_IN_SWITCH, true)) {
                            EventBus.getDefault().post(new QueueShowChooseTableEvent(true, item));
                        } else {
                            QueueOpManager.getInstance().updateQueueStatus(QueueReq.Type.IN, queue, mContext, null);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }

                }
            });
        } else {
            historyViewHolder.mIvQueueStatus.setBackgroundResource(R.drawable.queue_already_goin);
            historyViewHolder.mIvQueueGoin.setVisibility(View.INVISIBLE);
            historyViewHolder.mIvQueueRevocation.setVisibility(View.INVISIBLE);
        }

        historyViewHolder.mIvQueueRevocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                QueueOpManager.getInstance().queueRecoverInvalid(queue, mContext, null);
            }
        });

        historyViewHolder.mTvQueueDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (NewQueueBeanVo bean : queueList) {
                    bean.setSelected(false);
                }
                item.setSelected(true);
                notifyDataSetChanged();
            }
        });


    }

    /**
     * 更新第二屏的排队信息
     */
    private void doUpdateSecondScreen() {

        DisplayServiceManager.doUpdateQueue(mContext.getApplicationContext());
    }

    // 只处理叫号
    private void showOnlyCallNum(final NewQueueBeanVo queueVo, final View view) {
        QueueCallNumberReq req = new QueueCallNumberReq();
        req.queueID = queueVo.getQueue().getId();
        QueueOperates queueOperates = OperatesFactory.create(QueueOperates.class);
        CalmResponseListener<ResponseObject<QueueCallNumberResp>> listener = new CalmResponseListener<ResponseObject<QueueCallNumberResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showShortToast(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(ResponseObject<QueueCallNumberResp> data) {
                if (data.getContent() != null) {
                    QueueOpManager.getInstance().queueCallNo(mContext, view, speechMap, queueVo.getQueueNumber());
                    showInSecondScreen(queueVo.getQueueNumber());
                } else {
                    ToastUtil.showShortToast(data.getMessage());
                }
            }
        };
        queueOperates.queueCallNum(mContext, req, listener);
    }

    /**
     * 显示打印弹窗
     *
     * @param
     * @date:2015年8月31日
     */
    private void showPrint(NewQueueBeanVo queueVo, View anchor) {
        remindPopupWindow = new QueueRemindPopupWindow(mContext, QueueRemindPopupWindow.TYPE_PRINT, queueVo, ticketClickListener);
        remindPopupWindow.setAnchorWidth(DensityUtil.dip2px(mContext, 65));
        remindPopupWindow.showPopupWindow(anchor);
    }

    /**
     * 展示提醒弹窗
     *
     * @param queueVo
     * @param anchor
     */
    private void showRemind(NewQueueBeanVo queueVo, View anchor) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        if (TextUtils.isEmpty(queueVo.getQueue().getMobile())) {
            return;
        }
        remindPopupWindow = new QueueRemindPopupWindow(mContext, QueueRemindPopupWindow.TYPE_REMIND, queueVo, remindClickListener);
        remindPopupWindow.setAnchorWidth(DensityUtil.dip2px(mContext, 45));
        remindPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                remindQueue = null;
            }
        });
        remindQueue = queueVo.getQueue();
        remindPopupWindow.showPopupWindow(anchor);
    }

    /**
     * 微信预点菜弹窗
     *
     * @param item
     */
    private void showExMenu(NewQueueBeanVo item) {
        if (ClickManager.getInstance().isClicked()) return;

        if (null == exMenuDialog) {
            exMenuDialog = new ExMenuDetailDialog_();
        }
        exMenuDialog.setQueueVo(item);
        exMenuDialog.show(mContext.getSupportFragmentManager(), "QueueDetailMenuDailog");
    }

    private void showInSecondScreen(String num) {
        // 第二屏显示此号8秒
        //DisplayServiceManager.doShowQueueCallNo(mContext, num, DisplayOnCallNo.SHOW);
        Message msg = new Message();
        msg.what = CallNoToSecondScreenHandler.disappearNoWhat;
        msg.obj = num;

        /*
         * 多次点击的时候，之后最后一次发送消失的消息
         */
        if (mHander.hasMessages(CallNoToSecondScreenHandler.disappearNoWhat)) {
            mHander.removeMessages(CallNoToSecondScreenHandler.disappearNoWhat);
        }
        mHander.sendMessageDelayed(msg, 8000);
    }

    // 打印小票监听
    private QueueRemindPopupWindow.OnItemClickListener ticketClickListener = new QueueRemindPopupWindow.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position, NewQueueBeanVo queueVo) {
            switch (position) {
                case 0://小票重打
                    //  QueueOpManager.getInstance().doPrint(queueVo, new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
                    break;
                case 1://预点菜单
                    showExMenu(queueVo);
                    break;
            }
        }
    };

    // 提醒监听
    private QueueRemindPopupWindow.OnItemClickListener remindClickListener = new QueueRemindPopupWindow.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position, final NewQueueBeanVo queueVo) {
            switch (position) {
                case 1:// 电话
                    QueueOpManager.getInstance().callPhone(queueVo);
                    break;
                case 0:// 短信
                    QueueOpManager.getInstance().sendMessage(queueVo);
                    break;
                case 2:// 自助语音
                    QueueOpManager.getInstance().queuePhone(queueVo);
                    break;
            }
        }
    };

    class QueueingViewHolder {
        // 排队号
        TextView tv_number;

        LinearLayout ll_number;

        // 排队人数
        TextView tv_personCount;

        // 时间
        TextView tv_time;

        // 用户名
        TextView tv_userName;

        // 电话号码
        TextView tv_phone;

        // 订单来源
        ImageView img_from;

        // 过号
        TextView tv_pass;

        // 入场
        TextView tv_in;

        // 提醒
        TextView tv_remind;

        TextView tv_modify;

        TextView tv_remind_count;

        ImageView remind_status;

        LinearLayout ll_remind;

        // 预点菜
        ImageView exMenu;

        TextView mTvQueueDetail;

        View mSelectedView;
    }

    class HistoryViewHolder {
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

        TextView mTvQueueDetail;

        View mSelectedView;

    }

    public interface OnItemClickListener {
        void onClick(NewQueueBeanVo queueBeanVo);
    }


}
