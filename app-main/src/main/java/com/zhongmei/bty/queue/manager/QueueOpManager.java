package com.zhongmei.bty.queue.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.manager.QueuePlayServiceManager;
import com.zhongmei.bty.basemodule.commonbusiness.service.QueuePlayService;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueNotifyType;
import com.zhongmei.bty.basemodule.database.queue.QueueOrderSource;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.database.queue.SuccessOrFaild;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.bty.basemodule.queue.bean.QueuePrintVo;
import com.zhongmei.bty.common.util.audio.AudioBase;
import com.zhongmei.bty.common.util.audio.Common;
import com.zhongmei.bty.common.util.audio.QueueAudio;
import com.zhongmei.bty.common.util.audio.QueueCall;
import com.zhongmei.bty.common.util.audio.QueueCallCommon;
import com.zhongmei.bty.common.util.audio.combination.AudioMediaUtil;
import com.zhongmei.bty.common.util.audio.combination.ThreeSectionCom;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.operates.BaiduSyntheticSpeechDal;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberReq;
import com.zhongmei.bty.data.operates.message.content.QueueCallNumberResp;
import com.zhongmei.bty.data.operates.message.content.QueuePredictWaitTimeResp;
import com.zhongmei.bty.data.operates.message.content.QueueReq;
import com.zhongmei.bty.data.operates.message.content.QueueResp;
import com.zhongmei.bty.entity.enums.QueueProofType;
import com.zhongmei.bty.queue.util.QueueFileUtil;
import com.zhongmei.bty.queue.vo.NewQueueAreaVo;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.bty.settings.fragment.SpeechQueueSettingFragment;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.volley.Response;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by demo on 2018/12/15
 */

public class QueueOpManager {

    public static final String TAG = QueueOpManager.class.getSimpleName();

    private AnimationDrawable animDrawable = null;

    private View mAnimView = null;

    private static QueueOpManager manager = new QueueOpManager();

    public static QueueOpManager getInstance() {
        return manager;
    }

    /**
     * 根据排队输入人数选择相应的排队队列
     *
     * @param queueAreaVoList
     * @param personCount
     * @return
     */
    public List<NewQueueAreaVo> setAreaVoSelected(List<NewQueueAreaVo> queueAreaVoList, int personCount) {
        if (queueAreaVoList != null && queueAreaVoList.size() > 0) {
            if (queueAreaVoList.size() == 1) {
                queueAreaVoList.get(0).setSelected(true);
                return queueAreaVoList;
            }
            queueAreaVoList.get(0).setSelected(false);
            for (int i = 1; i < queueAreaVoList.size(); i++) {
                queueAreaVoList.get(i).setSelected(false);
                if (i == 1 && queueAreaVoList.get(i).getQueueLine().getMaxPersonCount() >= personCount) {
                    queueAreaVoList.get(i).setSelected(true);
                } else if (i == queueAreaVoList.size() - 1 && queueAreaVoList.get(i).getQueueLine().getMinPersonCount() <= personCount) {
                    queueAreaVoList.get(i).setSelected(true);
                } else if (queueAreaVoList.get(i).getQueueLine().getMinPersonCount() <= personCount && personCount <= queueAreaVoList.get(i).getQueueLine().getMaxPersonCount()) {
                    queueAreaVoList.get(i).setSelected(true);
                }
            }
        }
        return queueAreaVoList;
    }

    /**
     * 排队搜索
     *
     * @param queueAreaVo 当天全部排队
     * @return
     */
    public List<NewQueueBeanVo> searchQueueList(NewQueueAreaVo queueAreaVo, String searchStr) {
        if (TextUtils.isEmpty(searchStr)) return Collections.emptyList();
        Map<Long, List<NewQueueBeanVo>> queueLineIdQueueBeanVoListMap = new HashMap<>();
        List<NewQueueBeanVo> queueBeanVoList = new ArrayList<>();
        if (Utils.isNotEmpty(queueAreaVo.getQueueingBeanVoList())) {
            for (NewQueueBeanVo vo : queueAreaVo.getQueueingBeanVoList()) {
                vo.setSelected(false);
                if (vo.getQueueNumber().equals(searchStr) || vo.getQueue().getMobile().contains(searchStr) || vo.getQueue().getName().contains(searchStr)) {
//                    if (queueLineIdQueueBeanVoListMap.containsKey(vo.getQueueLine().getId())) {
//                        queueLineIdQueueBeanVoListMap.get(vo.getQueueLine().getId()).add(vo);
//                    } else {
//                        queueLineIdQueueBeanVoListMap.put(vo.getQueueLine().getId(), new ArrayList<NewQueueBeanVo>());
//                        queueLineIdQueueBeanVoListMap.get(vo.getQueueLine().getId()).add(vo);
//                    }
                    queueBeanVoList.add(vo);
                }
            }
        }
        if (Utils.isNotEmpty(queueAreaVo.getQueuedBeanVoList())) {
            for (NewQueueBeanVo vo : queueAreaVo.getQueuedBeanVoList()) {
                vo.setSelected(false);
                if (vo.getQueueNumber().equals(searchStr) || vo.getQueue().getMobile().contains(searchStr) || vo.getQueue().getName().contains(searchStr)) {
                   /* if (queueLineIdQueueBeanVoListMap.containsKey(vo.getQueueLine().getId())) {
                        queueLineIdQueueBeanVoListMap.get(vo.getQueueLine().getId()).add(vo);
                    } else {
                        queueLineIdQueueBeanVoListMap.put(vo.getQueueLine().getId(), new ArrayList<NewQueueBeanVo>());
                        queueLineIdQueueBeanVoListMap.get(vo.getQueueLine().getId()).add(vo);
                    }*/
                    queueBeanVoList.add(vo);
                }
            }
        }
        for (Long key : queueLineIdQueueBeanVoListMap.keySet()) {
            queueBeanVoList.addAll(queueLineIdQueueBeanVoListMap.get(key));
        }
        return queueBeanVoList;
    }


    /**
     * 排队详情小票打印方法
     *
     * @param queueVo
     * @throws Exception
     */
    public void doNewPrint(NewQueueBeanVo queueVo) throws Exception {
        // DinnerPrintHelper.getInstance().printQueueDetailTicket(queueVo.getQueue(), queueVo.getQueueNumber(), queueVo.getTradeVo(), new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
    }

    /**
     * 排队单打印方法
     * NewQueueBeanVo中必须有Queue和QueueExtra
     *
     * @param queueBeanVo
     */
    /*public void doPrint(final NewQueueBeanVo queueBeanVo, final PRTOnPrintListener listener) {
        if (ServerSettingCache.getInstance().isOpenPartnerShop(SourceId.MEITUAN_QUEUE.value(), 9) &&
                (queueBeanVo.getQueueExtra() == null || queueBeanVo.getQueueExtra().meidaUrl == null)) {
            QueueOperates op = OperatesFactory.create(QueueOperates.class);
            op.queryqtcode(queueBeanVo.getQueue().getId(), new ResponseListener<QueueExtra>() {
                @Override
                public void onResponse(ResponseObject<QueueExtra> response) {
                    if (response != null && response.getContent() != null) {
                        DinnerPrintHelper.getInstance().printQueueTicket(queueBeanVo.getQueue(), listener);
                    } else {
                        DinnerPrintHelper.getInstance().printQueueTicket(queueBeanVo.getQueue(), listener);
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    DinnerPrintHelper.getInstance().printQueueTicket(queueBeanVo.getQueue(), listener);
                }
            });
        } else {
            DinnerPrintHelper.getInstance().printQueueTicket(queueBeanVo.getQueue(), listener);
        }

    }*/
    public void queueCallNo(Context context, View animView, Map<Integer, BaiduSyntheticSpeech> speechMap, String... queueNumbers) {
        boolean isSetTemplate = SpHelper.getDefault().getBoolean(SpeechQueueSettingFragment.CALL_VOICE_SYNTHESIS, false);
        List<String> queueNumberList = Arrays.asList(queueNumbers);

        if (isSetTemplate) {
            //正常叫号
            if (queueNumberList.size() == 1) {
                callNoForTemplate(animView, queueNumberList.get(0), speechMap.get(0));
            } else if (queueNumberList.size() == 2) {
                intelligenceCallNo(speechMap.get(1), queueNumberList.get(0), speechMap.get(2), queueNumberList.get(1));
            }
        } else {
            callNoForDefault(context, animView, queueNumberList.get(0));
        }

    }


    /**
     * 排队叫号：设置模板叫号
     *
     * @param animView
     * @param queueNumber
     * @param speechCall
     */
    public void callNoForTemplate(final View animView, String queueNumber, BaiduSyntheticSpeech speechCall) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (animDrawable != null && animDrawable.isRunning()) {
                        animDrawable.stop();
                    }
                    if (animView != null) {
                        animView.setBackgroundResource(R.drawable.queue_list_item_remind_selector);
                    }
                }
            }
        };
        if (speechCall != null) {
            if (animDrawable != null && animDrawable.isRunning()) {
                animDrawable.stop();
                mAnimView.setBackgroundResource(R.drawable.queue_list_item_remind_selector);
            }
            String callContent = getCallContent(queueNumber, speechCall);
            /*TTSBean ttsBean = new TTSBean(callContent, speechCall.getSpeed(), speechCall.getSex() == Sex.FEMALE ? TTSVoice.FEMALE : TTSVoice.MALE);
            TVClientService.start(BaseApplication.getInstance(), TVType.TTS, ttsBean);*/
            boolean isOpen = SpHelper.getDefault().getBoolean(SpeechQueueSettingFragment.CALL_OPEN_VOICE, true);
            if (!isOpen) return;
            animView.setBackgroundResource(R.drawable.queue_remind_anim);
            mAnimView = animView;
            animDrawable = (AnimationDrawable) animView.getBackground();
            animDrawable.start();
            QueuePlayServiceManager.playCall(speechCall, queueNumber, new QueuePlayService.SpeechFinishListener() {
                @Override
                public void finish() {
                    handler.sendEmptyMessage(1);
                }
            });


        }
    }

    private String getCallContent(String queueNumber, BaiduSyntheticSpeech speechCall) {
        String content = speechCall.getContent();
        String format = BaseApplication.sInstance.getResources().getString(com.zhongmei.yunfu.basemodule.R.string.voice_number_toast);
        return content.replace(format, queueNumber);
    }


    /**
     * 排队叫号：默认叫号
     *
     * @param context
     * @param animView
     * @param queueNumber
     */
    public void callNoForDefault(Context context, final View animView, String queueNumber) {
        if (animView == null) return;
        try {
            if (animDrawable != null && animDrawable.isRunning()) {
                animDrawable.stop();
                mAnimView.setBackgroundResource(R.drawable.queue_list_item_remind_selector);
            }
            String content = context.getString(R.string.queue_system_call_temp, queueNumber);
            /*TTSBean ttsBean = new TTSBean(content, 5, TTSVoice.FEMALE);
            TVClientService.start(BaseApplication.getInstance(), TVType.TTS, ttsBean);*/
            boolean isOpen = SpHelper.getDefault().getBoolean(SpeechQueueSettingFragment.CALL_OPEN_VOICE, true);
            if (!isOpen) return;
            animView.setBackgroundResource(R.drawable.queue_remind_anim);
            mAnimView = animView;
            animDrawable = (AnimationDrawable) animView.getBackground();
            animDrawable.start();
            //Queue重名，直接使用包名+类名
            AudioBase base = QueueAudio.getInstance(context);
            Common common = Common.getInstance(context);
            boolean isDownLoad = checkDownLoadStation();
            if (isDownLoad) {
                base = QueueCall.getInstance(context);
                common = QueueCallCommon.getInstance(context);
            }
            ThreeSectionCom threeSectionCom = ThreeSectionCom.getInstance(context, base, common);
            threeSectionCom.prepare(queueNumber, new AudioMediaUtil.OnMediaLister() {
                @Override
                public void onComplete() {
                    if (animView != null) {
                        animView.setBackgroundResource(R.drawable.queue_list_item_remind_selector);
                    }
                }
            });
            threeSectionCom.play(isDownLoad);

        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public void intelligenceCallNo(BaiduSyntheticSpeech queueInVoiceTemp, String queueInNumber, BaiduSyntheticSpeech queueNextVoiceTemp, String queueNextNumber) {
        StringBuffer buffer = new StringBuffer("");
        if (!TextUtils.isEmpty(queueInVoiceTemp.getContent()) && !TextUtils.isEmpty(queueInNumber)) {
            buffer.append(getCallContent(queueInNumber, queueInVoiceTemp));
        }
        buffer.append(",");
        if (!TextUtils.isEmpty(queueNextVoiceTemp.getContent()) && !TextUtils.isEmpty(queueNextNumber)) {
            buffer.append(getCallContent(queueNextNumber, queueNextVoiceTemp));
        }
        BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        speech.setContent(buffer.toString());
        speech.setSpeed(queueInVoiceTemp.getSpeed());
        speech.setSex(queueInVoiceTemp.getSex());
        boolean isOpen = SpHelper.getDefault().getBoolean(SpeechQueueSettingFragment.CALL_OPEN_VOICE, true);
        if (isOpen) QueuePlayServiceManager.playAudition(speech);
        /*TTSBean ttsBean = new TTSBean(buffer.toString(), speech.getSpeed(), speech.getSex() == Sex.FEMALE ? TTSVoice.FEMALE : TTSVoice.MALE);
        TVClientService.start(BaseApplication.getInstance(), TVType.TTS, ttsBean);*/
    }

    /**
     * 获取排队等待时长
     *
     * @param createTime
     * @return
     */
    public String getQueueWaitTimeString(Long createTime, Context mContext) {
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
     * 名称
     *
     * @param queue
     * @return
     */
    public CharSequence getQueueName(Queue queue, Context mContext) {
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

    /**
     * 来源
     *
     * @param queue
     * @return
     */
    public int getDrawable(Queue queue) {
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
            case MeiDaQueue:
                drawable = R.drawable.source_meidaqueue_icon;
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
     * 发送短信
     *
     * @param
     */
    public void sendMessage(final NewQueueBeanVo queueVo) {
        QueueOperates opel = OperatesFactory.create(QueueOperates.class);
        Response.Listener<String> listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        ToastUtil.showShortToast(R.string.queue_send_message_successed);
                        queueVo.getQueue().setNotifyType(QueueNotifyType.NOTIFIEDSMS);
                        try {
                            updateQueue(queueVo.getQueue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtil.showShortToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    ToastUtil.showShortToast(R.string.queue_send_message_faild);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        opel.sendMessage(queueVo.getQueue(), listener, errorListener);
    }

    /**
     * 电话提醒
     *
     * @param queueVo
     */
    public void callPhone(NewQueueBeanVo queueVo) {
        try {
            /*if (CallingService.isCalling) {
                ToastUtil.showShortToast(R.string.tel_is_busy);
                return;
            }*/
            if (CalmPhoneManager.getDefault().isConnectSuccess()) {
                CalmPhoneManager.getDefault().getCalmPhone().dial(queueVo.getQueue().getMobile());
                Queue queue = queueVo.getQueue();
                queue.setNotifyType(QueueNotifyType.NOTIFIED);
                updateQueue(queue);
            } else {
                ToastUtil.showShortToast(R.string.check_phone);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }


    /**
     * 自助语音
     *
     * @param
     */
    public void queuePhone(final NewQueueBeanVo queueVo) {

        QueueOperates opel = OperatesFactory.create(QueueOperates.class);
        Response.Listener<String> listener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        Queue queue = queueVo.getQueue();
                        queue.setNotifyType(QueueNotifyType.NOTIFIEDIVR);
                        try {
                            updateQueue(queue);
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                        ToastUtil.showShortToast(R.string.queue_phone_success);
                    } else {
                        ToastUtil.showShortToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    ToastUtil.showShortToast(R.string.queue_queue_phone_faild);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        opel.queuePhone(queueVo.getQueue(), listener, errorListener);
    }


    /**
     * 格式化为A001的格式
     *
     * @param
     * @return
     * @date:2015年8月31日
     */
    public String formatNum(Queue queue, CommercialQueueLine currentQueueLine) {
        if (queue == null || currentQueueLine == null) {
            return "";
        }
        String queueChar = currentQueueLine.getQueueChar();
        if (queue.getQueueSource() != null && queue.getQueueSource() != QueueOrderSource.DaoDian && queue.getQueueSource() != QueueOrderSource.MeiDaQueue) {
            queueChar += "N";
        }
        String num = String.format(Locale.getDefault(), "%03d", queue.getQueueNumber());
        return queueChar + num;
    }


    /**
     * 通过人数查找队列
     * 耗时操作
     *
     * @param personCount
     * @return
     * @throws Exception
     */
    public CommercialQueueLine getQueueLineByPersonCount(int personCount) {
        QueueDal queueDal = OperatesFactory.create(QueueDal.class);
        List<CommercialQueueLine> queueLineList = null;
        try {
            queueLineList = queueDal.queryQueueLineList();
        } catch (Exception e) {
        }
        if (Utils.isNotEmpty(queueLineList)) {
            for (CommercialQueueLine queueLine : queueLineList) {
                if (queueLine.getMinPersonCount() <= personCount && personCount <= queueLine.getMaxPersonCount()) {
                    return queueLine;
                }
            }
            return queueLineList.get(queueLineList.size() - 1);
        }
        return null;
    }

    /**
     * 根据queueLineId查询CommercialQueueLine
     * 耗时
     *
     * @param queueLineId
     * @return
     * @throws Exception
     */
    public CommercialQueueLine getQueueLineByLineId(Long queueLineId) {
        QueueDal queueDal = OperatesFactory.create(QueueDal.class);
        try {
            return queueDal.findQueueLineByid(queueLineId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建排队，获取其排队号
     * 耗时
     *
     * @param queueLineId
     * @return
     * @throws Exception
     */
    public int getQueueNumber(Long queueLineId) {
        QueueDal queueDal = OperatesFactory.create(QueueDal.class);
        try {
            return queueDal.getQueueNumber(queueLineId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取当前排队还需等待数
     *
     * @param queue
     * @return
     */
    public int getWaiteCount(Queue queue) {
        QueueDal queueDal = OperatesFactory.create(QueueDal.class);
        try {
            return (int) queueDal.getCountByQueueUuid(queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 清零
     *
     * @param queueList
     */
    public void cleanQueueList(final List<Queue> queueList) {
        TaskContext.execute(new Runnable() {
            @Override
            public void run() {
                QueueDal dal = OperatesFactory.create(QueueDal.class);
                try {
                    dal.cleanQueueList(queueList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取当天排队
     *
     * @param queueLineId 为空则为全部区域
     * @param queueStatus 为空则为所有状态
     * @return
     */
    public List<Queue> findQueueList(Long queueLineId, QueueStatus queueStatus) {
        try {
            QueueDal dal = OperatesFactory.create(QueueDal.class);
            return dal.findAllDataByDate(new Date(), queueLineId, queueStatus);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 获取排队队列
     *
     * @return
     */
    public List<CommercialQueueLine> getQueueLineList() {
        try {
            QueueDal dal = OperatesFactory.create(QueueDal.class);
            return dal.queryQueueLineList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Map<Integer, BaiduSyntheticSpeech> getQueueVoiceMap() {
        BaiduSyntheticSpeechDal dal = OperatesFactory.create(BaiduSyntheticSpeechDal.class);
        try {
            Map<Integer, BaiduSyntheticSpeech> queueVoiceMap = dal.getQueueVoiceMap();
            if (queueVoiceMap == null) queueVoiceMap = new HashMap<>();
            List<BaiduSyntheticSpeech> saveQueueVoiceList = new ArrayList<>();
            BaiduSyntheticSpeech speech;
            String callTemplateStr = MainApplication.getInstance().getString(R.string.queue_voice_setting_call_template);
            String nextCallTemplateStr = MainApplication.getInstance().getString(R.string.queue_voice_setting_next_call_template);
            String numberTemplateStr = MainApplication.getInstance().getString(R.string.voice_number_toast);
            if (queueVoiceMap != null) {
                if (!queueVoiceMap.containsKey(0)) {
                    speech = createBaiduSyntheticSpeech(0, String.format(callTemplateStr, numberTemplateStr));
                    saveQueueVoiceList.add(speech);
                    queueVoiceMap.put(0, speech);
                }
                if (!queueVoiceMap.containsKey(1)) {
                    speech = createBaiduSyntheticSpeech(1, String.format(callTemplateStr, numberTemplateStr));
                    saveQueueVoiceList.add(speech);
                    queueVoiceMap.put(1, speech);
                }
                if (!queueVoiceMap.containsKey(2)) {
                    speech = createBaiduSyntheticSpeech(2, String.format(nextCallTemplateStr, numberTemplateStr));
                    saveQueueVoiceList.add(speech);
                    queueVoiceMap.put(2, speech);
                }
            }
            if (Utils.isNotEmpty(saveQueueVoiceList)) {
                dal.saveQueueList(saveQueueVoiceList);
            }
            return queueVoiceMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveBaiduSyntheticSpeechs(BaiduSyntheticSpeech... speeches) {
        final List<BaiduSyntheticSpeech> speechList = Arrays.asList(speeches);
        if (Utils.isNotEmpty(speechList)) {
            final BaiduSyntheticSpeechDal dal = OperatesFactory.create(BaiduSyntheticSpeechDal.class);
            TaskContext.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dal.saveQueueList(speechList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void saveBaiduSyntheticSpeechs(final List<BaiduSyntheticSpeech> speechList) {
        if (Utils.isNotEmpty(speechList)) {
            final BaiduSyntheticSpeechDal dal = OperatesFactory.create(BaiduSyntheticSpeechDal.class);
            TaskContext.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dal.saveQueueList(speechList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /**
     * 创建排队单
     *
     * @param personCount 人数
     * @param mobile      电话
     * @param name        名称 自助时为空
     * @param sex         性别 自助时为空
     * @throws Exception
     */
    public Queue createQueue(int personCount, String mobile, String name, Sex sex) {
        Long queueLineId = null;
        Queue queue = new Queue();
        queue.setRepastPersonCount(personCount);
        queue.setMobile(mobile);
        // 自助点餐没有队列 查找匹配的
        CommercialQueueLine queueLine = getQueueLineByPersonCount(personCount);
        if (queueLine != null) {
            queueLineId = queueLine.getId();
        } else {
            return null;
        }
        queue.setQueueLineId(queueLineId);
        if (TextUtils.isEmpty(name)) {
            queue.setName(MainApplication
                    .getInstance().getString(R.string.queue_anonymous));
        } else {
            queue.setName(name);
        }
        // 查找当前最大的序号
        queue.setQueueNumber(getQueueNumber(queueLineId));
        if (sex != null) {
            queue.setSex(sex);
        }
        Date now = new Date();
        queue.setCreateDateTime(now.getTime());
        queue.setModifyDateTime(now.getTime());
        queue.setIsOk(SuccessOrFaild.FAILD);
        queue.setIsZeroOped(YesOrNo.NO);
        queue.setQueueSource(QueueOrderSource.DaoDian);
        queue.setQueueStatus(QueueStatus.QUEUEING);
        queue.setNotifyType(QueueNotifyType.UNNOTIFIED);
        queue.setQueueProof(QueueProofType.PRINT.toString());
        queue.setCommercialID(Utils.toLong(ShopInfoCfg.getInstance().shopId));
        queue.setUuid(SystemUtils.genOnlyIdentifier());
        queue.setStatus(0);
        return queue;

    }

    /**
     * 创建队列
     *
     * @param queue
     */
    public void createQueueRequest(final FragmentActivity context, Queue queue, final SimpleResponseListener<Queue> listener) {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        operates.createAndUpdateQueue(0, queue, LoadingResponseListener.ensure(new SimpleResponseListener<Queue>() {
            @Override
            public void onSuccess(ResponseObject<Queue> response) {
                ToastUtil.showLongToast(R.string.booking_request_success);
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                ToastUtil.showLongToast(error.getMessage());
                if (listener != null) {
                    listener.onError(error);
                }
            }
        }, context.getSupportFragmentManager()));

    }

    /**
     * 撤销过号
     *
     * @param queue
     * @param fragmentActivity
     * @param listener
     */
    public void queueRecoverInvalid(Queue queue, FragmentActivity fragmentActivity, CalmResponseListener<ResponseObject<Queue>> listener) {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        if (listener == null) {
            listener = new CalmResponseListener<ResponseObject<Queue>>() {
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
        }
        operates.queueRecoverInvalid(queue.getUuid(), queue.getModifyDateTime(), fragmentActivity, listener);
    }

    /**
     * 更新排队状态
     *
     * @param type
     * @param queue
     * @param fragmentActivity
     * @param listener
     */
    public void updateQueueStatus(@QueueReq.Type final int type, final Queue queue, FragmentActivity fragmentActivity, ResponseListener<QueueResp> listener) {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        if (listener == null) {
            listener = new ResponseListener<QueueResp>() {

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
        }
        operates.updateQueue(type, queue, LoadingResponseListener.ensure(listener, fragmentActivity.getSupportFragmentManager()));
    }

    /**
     * 获取排队预计等待时长
     *
     * @param queue
     * @param fragmentActivity
     * @param listener
     */
    public void queuePredictWaitTime(Queue queue, FragmentActivity fragmentActivity, ResponseListener<QueuePredictWaitTimeResp> listener) {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        operates.predictWaitTime(fragmentActivity, queue.getUuid(), listener);
    }

    public void queueUpdateRemindCount(FragmentActivity mContext, Queue queue) {
        QueueCallNumberReq req = new QueueCallNumberReq();
        req.queueID = queue.getId();
        QueueOperates queueOperates = OperatesFactory.create(QueueOperates.class);
        CalmResponseListener<ResponseObject<QueueCallNumberResp>> listener = new CalmResponseListener<ResponseObject<QueueCallNumberResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showShortToast(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(ResponseObject<QueueCallNumberResp> data) {
                if (data.getContent() == null) {
                    ToastUtil.showShortToast(data.getMessage());
                }
            }
        };
        queueOperates.queueCallNum(mContext, req, listener);
    }

    public void clearAnim() {
        animDrawable = null;
        mAnimView = null;
    }

    /**
     * 判断是否使用下载叫号文件
     *
     * @return
     */
    private static boolean checkDownLoadStation() {
        String path = QueueFileUtil.getPath() + "call/audio/";
        File file0 = new File(path, "audio_0.mp3");
        File file1 = new File(path, "audio_1.mp3");
        File file2 = new File(path, "audio_2.mp3");
        File file3 = new File(path, "audio_3.mp3");
        File file4 = new File(path, "audio_4.mp3");
        File file5 = new File(path, "audio_5.mp3");
        File file6 = new File(path, "audio_6.mp3");
        File file7 = new File(path, "audio_7.mp3");
        File file8 = new File(path, "audio_8.mp3");
        File file9 = new File(path, "audio_9.mp3");
        File fileA = new File(path, "audio_A.mp3");
        File fileB = new File(path, "audio_B.mp3");
        File fileC = new File(path, "audio_C.mp3");
        File fileD = new File(path, "audio_D.mp3");
        File fileE = new File(path, "audio_E.mp3");
        File fileF = new File(path, "audio_F.mp3");
        File fileN = new File(path, "audio_N.mp3");
        File filePrefix = new File(path, "queue_prefix.mp3");
        File fileSuffix = new File(path, "queue_suffix.mp3");

        if (!file0.exists()) {
            return false;
        } else if (!file1.exists()) {
            return false;
        } else if (!file2.exists()) {
            return false;
        } else if (!file3.exists()) {
            return false;
        } else if (!file4.exists()) {
            return false;
        } else if (!file5.exists()) {
            return false;
        } else if (!file6.exists()) {
            return false;
        } else if (!file7.exists()) {
            return false;
        } else if (!file8.exists()) {
            return false;
        } else if (!file9.exists()) {
            return false;
        } else if (!fileN.exists()) {
            return false;
        } else if (!fileA.exists()) {
            return false;
        } else if (!fileB.exists()) {
            return false;
        } else if (!fileC.exists()) {
            return false;
        } else if (!fileD.exists()) {
            return false;
        } else if (!fileE.exists()) {
            return false;
        } else if (!fileF.exists()) {
            return false;
        } else if (!filePrefix.exists()) {
            return false;
        } else if (!fileSuffix.exists()) {
            return false;
        }
        return true;
    }


    /**
     * 转成打印对象
     *
     * @param queueBeanVo
     * @return
     */
    private QueuePrintVo convertPrintVo(NewQueueBeanVo queueBeanVo) {
        Queue queue = queueBeanVo.getQueue();
        QueuePrintVo printVo = new QueuePrintVo();
        printVo.setUuid(queue.getUuid());
        printVo.setCustomerName(queue.getName());
        printVo.setShopName(ShopInfoCfg.getInstance().commercialName);
        printVo.setRepastPersonCount(queue.getRepastPersonCount());
        printVo.setSex(queue.getSex());
        if (queue.queueExtra != null) {
            printVo.setQrCodeUrl(queue.queueExtra.url);
        }
        long waitCount = getWaiteCount(queue);
        printVo.setWaitingCount((int) waitCount);
        printVo.setQueueNo(queueBeanVo.getQueueNumber());
        printVo.setQueueLineName(queueBeanVo.getQueueLine().getQueueName());
        return printVo;
    }

    /**
     * 更新排队 入场 过号  提醒等
     *
     * @param queue
     * @throws Exception
     */
    private void updateQueue(final Queue queue) throws Exception {
        TaskContext.execute(new Runnable() {
            @Override
            public void run() {
                QueueDal dal = OperatesFactory.create(QueueDal.class);
                try {
                    dal.updateQueue(queue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private BaiduSyntheticSpeech createBaiduSyntheticSpeech(int queueVoiceType, String content) {
        BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        speech = new BaiduSyntheticSpeech();
        speech.setClientCreateTime(new Date().getTime());
        speech.setClientUpdateTime(new Date().getTime());
        speech.setUuid(SystemUtils.genOnlyIdentifier());
        speech.setStatusFlag(StatusFlag.VALID);
        speech.setType(1);
        speech.setQueueVoiceType(queueVoiceType);
        speech.setContent(content);
        speech.setSex(Sex.MALE);
        speech.setSpeed(5);
        return speech;
    }
}
