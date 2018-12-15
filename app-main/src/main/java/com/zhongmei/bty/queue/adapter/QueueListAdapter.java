package com.zhongmei.bty.queue.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberReq;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberResp;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
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
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
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
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * @date 2015年8月27日下午2:46:06
 */
public class QueueListAdapter extends BaseAdapter {
    private static final String TAG = QueueListAdapter.class.getSimpleName();

    private List<NewQueueBeanVo> mQueueList;

    private Map<Integer, BaiduSyntheticSpeech> speechMap;//叫号模板

    private FragmentActivity mContext;

    private ExMenuDetailDialog exMenuDialog;

    private CallNoToSecondScreenHandler mHander;

    //当前提醒的Queue
    private Queue remindQueue;

    private QueueRemindPopupWindow remindPopupWindow;

    public QueueListAdapter(FragmentActivity context) {
        mContext = context;
        mQueueList = new ArrayList<>();
        speechMap = new HashMap<>();
    }


    @Override
    public int getCount() {
        if (mQueueList != null) {
            return mQueueList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mQueueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.queue_list_item, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            initView(convertView, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewQueueBeanVo item = mQueueList.get(position);
        bindData(viewHolder, item, position);
        return convertView;
    }

    private void bindClickListener(ViewHolder viewHolder, NewQueueBeanVo item) {
        viewHolder.ll_number.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_pass.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_in.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_remind.setOnClickListener(newClick(viewHolder, item));
        viewHolder.ll_remind.setOnClickListener(newClick(viewHolder, item));
        viewHolder.exMenu.setOnClickListener(newClick(viewHolder, item));
        viewHolder.tv_modify.setOnClickListener(newClick(viewHolder, item));
        viewHolder.mIvShowMemo.setOnClickListener(newClick(viewHolder, item));
    }

    private void initView(View convertView, ViewHolder viewHolder) {
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
        viewHolder.mIvShowMemo = (ImageView) convertView.findViewById(R.id.queue_show_memo);
        viewHolder.mTvQueueMemo = (TextView) convertView.findViewById(R.id.queue_memo);
    }

    private void bindData(ViewHolder viewHolder, NewQueueBeanVo item, int position) {
        bindClickListener(viewHolder, item);
        Queue queue = item.getQueue();
        viewHolder.tv_number.setText(String.format(mContext.getString(R.string.queue_number), item.getQueueNumber()));
        viewHolder.tv_personCount.setText(queue.getRepastPersonCount() + mContext.getResources().getString(R.string.person));
        viewHolder.tv_time.setText(getCostTimeString(queue.getCreateDateTime()));
        viewHolder.tv_phone.setText(TextUtils.isEmpty(queue.getMobile()) ? "" : MobileUtil.getMobileAndAreaCode(queue.getNationalTelCode(), queue.getMobile()));
        viewHolder.tv_remind_count.setText(null != queue.queueExtra && queue.queueExtra.callCount != null ? queue.queueExtra.callCount + "" : 0 + "");
        viewHolder.tv_remind_count.setSelected(true);
        viewHolder.tv_userName.setText(getQueueName(queue));
        viewHolder.mTvQueueMemo.setText(String.format(mContext.getString(R.string.queue_list_item_memo), queue.getMemo() == null ? "" : queue.getMemo()));
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
        viewHolder.img_from.setBackgroundResource(getDrawable(queue));

        if (position == 0) {
            viewHolder.tv_number.setTextColor(mContext.getResources().getColor(R.color.bg_red));
        } else {
            viewHolder.tv_number.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        if (item.isOrderDish()) {
            viewHolder.exMenu.setVisibility(View.VISIBLE);
        } else {
            viewHolder.exMenu.setVisibility(View.INVISIBLE);
        }

        if (remindQueue != null
                && remindQueue.getId().equals(queue.getId())
                && remindPopupWindow != null) {
            remindPopupWindow.showPopupWindow(viewHolder.ll_remind);
        }
    }

    /**
     * deng'd
     *
     * @param createTime
     * @return
     */
    private String getCostTimeString(Long createTime) {
        StringBuilder text = new StringBuilder();
        text.append(mContext.getString(R.string.queue_waitting));
        long costTime = System.currentTimeMillis() - createTime;
        if (costTime > 0) {
            long hours = costTime / TimeUnit.HOURS.toMillis(1);
            if (hours > 0) {
                text.append(mContext.getString(R.string.queue_waitting_hour, hours));
            }

            long totalMinutes = (costTime % TimeUnit.HOURS.toMillis(1)) / TimeUnit.MINUTES.toMillis(1);
            if (totalMinutes >= 0) {
                text.append(mContext.getString(R.string.queue_waitting_min, totalMinutes));
            }
        } else {
            text.append(mContext.getString(R.string.queue_waitting_min, 0));
        }

        return text.toString();
    }

    /**
     * 来源
     *
     * @param queue
     * @return
     */
    private int getDrawable(Queue queue) {
        int drawable;
        switch (queue.getQueueSource()) {
            case Daodian:
            case DaoDian:
                drawable = R.color.transparent;
                break;
            case Alipay:
                drawable = R.drawable.source_alipay_icon;
                break;
            case Baidu:
            case BaiduDitu:
            case BaiduMap:
                drawable = R.drawable.source_baiduqianbao_icon;
                break;
            case BaiduWaiMai:
                drawable = R.drawable.source_baiduwaimai_icon;
                break;
            case DaiDingYuDing:
                drawable = R.drawable.source_dailiren_icon;
                break;
            case DaZhongDianPing:
                drawable = R.drawable.source_dazhongdianping_icon;
                break;
            case WeiXin:
                drawable = R.drawable.source_weixin_icon;
                break;
            case MeiTuan:
                drawable = R.drawable.source_meituan_icon;
                break;
            case DianHua:
            case DianHuaYuDing:
            case Enjoy:
            case Nuomi:
            case SelfHelp:
            case Shop:
            case ShouJiYuDing:
            case TaoDianDian:
            case XiaoMiShu:
            case ZhaoWei:
            case ZhiDaHao:
            case NONE:
            case App:
            default:
                drawable = R.color.transparent;
                break;
        }
        return drawable;
    }

    /**
     * 名称
     *
     * @param queue
     * @return
     */
    private CharSequence getQueueName(Queue queue) {
        Resources res = mContext.getResources();
        String name = queue.getName();
        if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(queue.getMobile())) {
            name = res.getString(R.string.phone_no_name);
        } else {
            if (Sex.FEMALE == queue.getSex()) {
                name = String.format(res.getString(R.string.customer_detail_sex_women), queue.getName());
            } else {
                name = String.format(res.getString(R.string.customer_detail_sex_man), queue.getName());
            }
        }
        return name;
    }

    class ViewHolder {
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

        ImageView mIvShowMemo;

        TextView mTvQueueMemo;
    }

    public View.OnClickListener newClick(final ViewHolder viewHolder, final NewQueueBeanVo item) {
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
                        updateQueueStatus(QueueReq.Type.PASS, queue);
                        break;
                    case R.id.queue_list_in:
                        MobclickAgentEvent.onEvent(UserActionCode.PD020008);
                        //判断是否使用开台入场
                        if (SpHelper.getDefault().getBoolean(QueueSettingSwitchFragment.QUEUE_IN_SWITCH, true)) {
                            EventBus.getDefault().post(new QueueShowChooseTableEvent(true, item));
                        } else {
                            //更新第二屏排队队列
                            doUpdateSecondScreen();
                            updateQueueStatus(QueueReq.Type.IN, queue);//入场请求营销发券和实时更新微信排队状态;
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
                    case R.id.queue_show_memo:
                        if (viewHolder.mTvQueueMemo.getVisibility() == View.GONE) {
                            viewHolder.mTvQueueMemo.setVisibility(View.VISIBLE);
                            viewHolder.mIvShowMemo.setImageResource(R.drawable.queue_item_show_memo_up);
                        } else {
                            viewHolder.mTvQueueMemo.setVisibility(View.GONE);
                            viewHolder.mIvShowMemo.setImageResource(R.drawable.queue_item_show_memo_down);
                        }
                }
            }
        };
    }

    /**
     * 展示提醒弹窗
     *
     * @param
     * @param
     * @date:2015年8月31日
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

                    if (SpHelper.getDefault().getBoolean(SpeechQueueSettingFragment.CALL_VOICE_SYNTHESIS, false)) {// 合成叫号
                        if (speechMap.get(0) == null)
                            ToastUtil.showShortToast(R.string.queue_speech_correct_format_toast);
                        else
                            QueueOpManager.getInstance().callNoForTemplate(view, queueVo.getQueueNumber(), speechMap.get(0));
                    } else {
                        QueueOpManager.getInstance().callNoForDefault(mContext, view, queueVo.getQueueNumber());
                    }
                    showInSecondScreen(queueVo.getQueueNumber());
                } else {
                    ToastUtil.showShortToast(data.getMessage());
                }
            }
        };
        queueOperates.queueCallNum(mContext, req, listener);
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

    public void resetData(List<NewQueueBeanVo> queueList) {
        mQueueList = queueList;

        //检查当前提醒的Queue是否还存在，若不存在dismiss remindPopup
        boolean isFound = false;
        if (remindQueue != null && queueList != null) {
            for (NewQueueBeanVo beanVo : mQueueList) {
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


    private void showExMenu(NewQueueBeanVo item) {
        if (ClickManager.getInstance().isClicked()) return;

        if (null == exMenuDialog) {
            exMenuDialog = new ExMenuDetailDialog_();
        }
        exMenuDialog.setQueueVo(item);
        exMenuDialog.show(mContext.getSupportFragmentManager(), "QueueDetailMenuDailog");
    }

    // 打印小票监听
    private QueueRemindPopupWindow.OnItemClickListener ticketClickListener = new QueueRemindPopupWindow.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position, NewQueueBeanVo queueVo) {
            switch (position) {
                case 0://小票重打
                    try {
                        //  QueueOpManager.getInstance().doPrint(queueVo, new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
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

    /**
     * 更新第二屏的排队信息
     */
    private void doUpdateSecondScreen() {

        DisplayServiceManager.doUpdateQueue(mContext.getApplicationContext());
    }

    public void setHanlder(CallNoToSecondScreenHandler hander) {
        mHander = hander;
    }


    /**
     * 更新队列状态
     */
    private void updateQueueStatus(final int type, final Queue queue) {
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
