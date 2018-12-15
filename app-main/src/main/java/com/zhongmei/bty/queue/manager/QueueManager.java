package com.zhongmei.bty.queue.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.operates.PrepareTradeRelationDal;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.bty.basemodule.database.queue.QueueNotifyType;
import com.zhongmei.bty.basemodule.database.queue.QueueOrderSource;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.database.queue.SuccessOrFaild;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.bty.basemodule.queue.bean.QueuePrintVo;
import com.zhongmei.bty.common.util.audio.AudioBase;
import com.zhongmei.bty.common.util.audio.Common;
import com.zhongmei.bty.common.util.audio.QueueAudio;
import com.zhongmei.bty.common.util.audio.QueueCall;
import com.zhongmei.bty.common.util.audio.QueueCallCommon;
import com.zhongmei.bty.common.util.audio.combination.AudioMediaUtil.OnMediaLister;
import com.zhongmei.bty.common.util.audio.combination.ThreeSectionCom;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.operates.QueueOperates;
import com.zhongmei.bty.entity.enums.QueueProofType;
import com.zhongmei.bty.queue.util.QueueFileUtil;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.zhongmei.bty.queue.adapter.QueueListAdapter.ServerListener;

/**
 * 队列处理相关类
 *
 * @date 2015年8月27日下午2:57:16
 */
public class QueueManager {
    private static final String TAG = QueueManager.class.getSimpleName();

    private DataObserver mObserver = new DataObserver();

    private OnDataChangedListener mOnChangedListener;

    private static QueueManager queueManager = null;

    /**
     * 当天的所有单据
     */
    //private List<Queue> queueList;

    /**
     * 队列
     */
    //private List<CommercialQueueLine> queueLineList;

    /**
     * 当天正在排队的单据
     */
    //private List<Queue> queueingList;

    /**
     * 排队历史
     */
    //private List<Queue> queueHistoryList;

    //private static CommercialQueueLine currentQueueLine = null;

    private static AnimationDrawable animDrawable = null;

    private static View mAnimView = null;

    public static QueueManager getInstance() {
        if (queueManager == null) {
            queueManager = new QueueManager();
        }
        return queueManager;
    }

    /**
     * 初始化界面时注册监听
     */
    public void init() {
        Context context = MainApplication.getInstance();
        init(context);
    }

    public void init(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri mUri = DBHelperManager.getUri(Queue.class);
        Uri relationUri = DBHelperManager.getUri(PrepareTradeRelation.class);
        Uri queueExtraUri = DBHelperManager.getUri(QueueExtra.class);
        resolver.registerContentObserver(mUri, true, mObserver);
        resolver.registerContentObserver(relationUri, true, mObserver);
        resolver.registerContentObserver(queueExtraUri, true, mObserver);
    }

    public void close() {
        ContentResolver resolver = MainApplication.getInstance().getContentResolver();
        resolver.unregisterContentObserver(mObserver);
    }

    /**
     * 加载队列分类列表
     *
     * @return
     */
    public List<CommercialQueueLine> loadCommericQueueList() throws Exception {
        QueueDal dal = OperatesFactory.create(QueueDal.class);
        return dal.queryQueueLineList();
    }

    /**
     * 通过队列分类查找排队列表
     *
     * @param queueLine
     * @return
     */
    public List<Queue> queryQueueingListByLine(CommercialQueueLine queueLine) {
		/*List<Queue> queueingList = getQueueingList();
		if (queueLine != null && queueLine.getId() != null) {
			if (queueingList != null && queueingList.size() > 0) {
				Iterator<Queue> iterator = queueingList.iterator();
				while (iterator.hasNext()) {
					Queue queue = iterator.next();
					if (queue != null
							&& queue.getQueueLineId() != null
							&& queueLine.getId() != null
							&& (queue.getQueueLineId().longValue() != queueLine.getId().longValue())) {
						iterator.remove();
					}
				}
				return queueingList;
			}
		}
		return Collections.emptyList();*/

        Long queueLineId = null;
        if (queueLine != null) {
            queueLineId = queueLine.getId();
        }

        return findQueueList(queueLineId, QueueStatus.QUEUEING);
    }

    public static List<Queue> filterQueueingListByLine(CommercialQueueLine queueLine, List<Queue> queueingList) {
        List<Queue> result = new ArrayList<>();
        if (queueLine != null && queueLine.getId() != null) {
            if (queueingList != null && queueingList.size() > 0) {
                for (Queue queue : queueingList) {
                    if (queue != null && queue.getQueueLineId() != null && queue.getQueueLineId().equals(queueLine.getId())) {
                        result.add(queue);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 创建排队单
     *
     * @param personCount 人数
     * @param mobile      电话
     * @param name        名称 自助时为空
     * @param queueLineId 队列id 自助时为空
     * @param sex         性别 自助时为空
     * @throws Exception
     */
    public Queue createQueue(Context context, int personCount, String mobile, String name, Sex sex,
                             Long queueLineId) {
        try {
            List<CommercialQueueLine> queueLineList = loadCommericQueueList();
            if (queueLineList == null || queueLineList.size() == 0) {
//                ToastUtil.showShortToast(R.string.queue_line_empty);
                return null;
            }
            QueueDal dal = OperatesFactory.create(QueueDal.class);
            Queue queue = new Queue();
            queue.setRepastPersonCount(personCount);
            queue.setMobile(mobile);
            // 自助点餐没有队列 查找匹配的
            if (queueLineId == null) {
                CommercialQueueLine queueLine = dal.findQueueLineIdByPersonCount(personCount);
                if (queueLine != null) {
                    queueLineId = queueLine.getId();
                } else {// 没有匹配的取最后一个
                    queueLineId = queueLineList.get(queueLineList.size() - 1).getId();
                }
            }
            queue.setQueueLineId(queueLineId);
            if (TextUtils.isEmpty(name)) {
                queue.setName(context.getString(R.string.queue_anonymous));
            } else {
                queue.setName(name);
            }
            // 查找当前最大的序号
            queue.setQueueNumber(dal.getQueueNumber(queueLineId));
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
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 通过人数查找队列
     *
     * @param personCount
     * @return
     * @throws Exception
     */
    public CommercialQueueLine getQueueLineByPersonCount(int personCount) throws Exception {
        QueueDal dal = OperatesFactory.create(QueueDal.class);
        return dal.findQueueLineIdByPersonCount(personCount);
    }

    /**
     * 保存排队单
     *
     * @param queue
     * @throws Exception
     */
    public void saveQueue(Queue queue) throws Exception {
        QueueDal dal = OperatesFactory.create(QueueDal.class);
        dal.saveQueue(queue);
    }

    /**
     * 排队单打印方法
     *
     * @param queue
    public void doPrint(Queue queue, OnPrintListener listener) throws Exception{
    QueuePrintVo printVo = convertPrintVo(queue);
    PrintContentQueue.getInstance().printQueue(printVo, listener);


    }
     */
    /**
     * 排队单打印方法
     *
     * @param queue
     */
    /*public void doPrint(Queue queue, PRTOnPrintListener listener) throws Exception {
        QueuePrintVo printVo = convertPrintVo(queue);
        DinnerPrintHelper.getInstance().printQueueTicket(printVo, listener);
    }*/

    /**
     * 排队详情小票打印方法
     * @param queueVo
     * @throws Exception
     *//*
	public void doPrint(QueueVo queueVo) throws Exception {
		PrintContentQueue.getInstance().printQueueDetail(queueVo, new OnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
	}*/

    /**
     * 排队详情小票打印方法
     *
     * @param queueVo
     * @throws Exception
     */
    /*public void doNewPrint(QueueBeanVo queueVo) throws Exception {
        DinnerPrintHelper.getInstance().printQueueDetailTicket(queueVo.getQueue(), queueVo.getNum(), queueVo.getTradeVo(), new PRTOnSimplePrintListener(PrintTicketTypeEnum.QUEUE));
    }*/

    /**
     * 转成打印对象
     *
     * @param queue
     * @return
     */
    private QueuePrintVo convertPrintVo(Queue queue) {
        QueuePrintVo printVo = new QueuePrintVo();
        printVo.setUuid(queue.getUuid());
        printVo.setCustomerName(queue.getName());
        printVo.setShopName(ShopInfoCfg.getInstance().commercialName);
        printVo.setRepastPersonCount(queue.getRepastPersonCount());
        printVo.setSex(queue.getSex());
        if (queue.queueExtra != null) {
            printVo.setQrCodeUrl(queue.queueExtra.url);
        }
        QueueDal dal = OperatesFactory.create(QueueDal.class);
        try {
            long waitCount = dal.getCountByQueueUuid(queue);
            printVo.setWaitingCount((int) waitCount);
            CommercialQueueLine queueLine = dal.findQueueLineByid(queue.getQueueLineId());
            printVo.setQueueNo(formatNum(queue, queueLine));
            printVo.setQueueLineName(queueLine.getQueueName());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return printVo;
    }

    /**
     * 更新排队 入场 过号  提醒等
     *
     * @param queue
     * @throws Exception
     */
    public void updateQueue(final Queue queue) throws Exception {
        TaskContext.execute(new Runnable() {
            @Override
            public void run() {
                QueueDal dal = OperatesFactory.create(QueueDal.class);
                // 更新数据需要重新上传
                queue.setIsOk(SuccessOrFaild.FAILD);
//                queue.setModifyDateTime(new Date().getTime());
                try {
                    dal.updateQueue(queue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*  *//**
     * 发送短信
     *
     * @param
     *//*
    public void sendMessage(final QueueBeanVo queueVo, final ServerListener serverListener) {
        QueueOperates opel = OperatesFactory.create(QueueOperates.class);
        Listener<String> listener = new Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    // workaround for server
                    if (status == 0) {
                        ToastUtil.showShortToast(R.string.queue_send_message_successed);
                        serverListener.response(true, queueVo);
                    } else {
                        ToastUtil.showShortToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage(), e);
                    ToastUtil.showShortToast(R.string.queue_send_message_faild);
                }
            }
        };
        ErrorListener errorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        opel.sendMessage(queueVo.getQueue(), listener, errorListener);
    }

    */

    /**
     * 自助语音
     *
     * @param
     *//*
    public void queuePhone(final QueueBeanVo queueVo, final ServerListener serverListener) {

        QueueOperates opel = OperatesFactory.create(QueueOperates.class);
        Listener<String> listener = new Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    // workaround for server
                    if (status == 0) {
                        serverListener.response(true, queueVo);
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
        ErrorListener errorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        opel.queuePhone(queueVo.getQueue(), listener, errorListener);

    }*/
    public List<CommercialQueueLine> getQueueLineList() {
        try {
            return /*queueLineList*/ loadCommericQueueList();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }

    public List<Queue> getQueueList() {
        return findQueueList(null, null);
    }

    private List<Queue> findQueueList(Long queueLineId, QueueStatus queueStatus) {
        try {
            QueueDal dal = OperatesFactory.create(QueueDal.class);
            return dal.findAllDataByDate(new Date(), queueLineId, queueStatus);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Queue> getQueueingList() {
        //return queueingList;
		/*List<Queue> queueList = getQueueList();
		if (queueList != null && queueList.size() > 0) {
			Iterator<Queue> iterator = queueList.iterator();
			while (iterator.hasNext()) {
				Queue queue = iterator.next();
				if (queue != null
						&& !queue.getQueueStatus().equalsValue(QueueStatus.QUEUEING.value())) {
					iterator.remove();
				}
			}
			return queueList;
		}
		return Collections.EMPTY_LIST;*/

        return findQueueList(null, QueueStatus.QUEUEING);
    }

    public List<Queue> getQueueHistoryList() {
        //return queueHistoryList;
        List<Queue> queueHistoryList = new ArrayList<>();
        List<Queue> queueList = getQueueList();
        if (queueList != null && queueList.size() > 0) {
            for (Queue queue : queueList) {
                if (queue.getQueueStatus().equalsValue(QueueStatus.ADMISSION.value())
                        || queue.getQueueStatus().equalsValue(QueueStatus.INVALID.value())) {
                    queueHistoryList.add(queue);
                }
            }
        }
        return queueHistoryList;
    }

    /**
     * 监听数据变化时调用
     *
     * @param dataChangeListener
     */
    public void setDataChangeListener(OnDataChangedListener dataChangeListener) {
        this.mOnChangedListener = dataChangeListener;
    }

    /**
     * 清零
     *
     * @param queueList
     */
    public void cleanQueueList(List<Queue> queueList) throws Exception {
        QueueDal dal = OperatesFactory.create(QueueDal.class);
        dal.cleanQueueList(queueList);
    }

    /**
     * 验证手机号码
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            Pattern regex = Pattern.compile("^(([1][3,4,5,8,7][0-9]{9}$)|([0][4][0-9]{8}$)|([6][1][4][0-9]{8}$))");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 监听数据变化
     */
    private class DataObserver extends ContentObserver {
        public DataObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            TaskContext.execute(new SimpleAsyncTask<Void>() {

                @Override
                public Void doInBackground(Void... params) {
                    return null;
                }

                public void onPostExecute(Void vod) {
                    if (mOnChangedListener != null) {
                        mOnChangedListener.onChanged();
                    }

                }
            });
        }
    }

    public interface OnDataChangedListener {
        void onChanged();
    }

    /**
     * 格式化为A001的格式
     *
     * @param
     * @return
     * @date:2015年8月31日
     */
    public static String formatNum(Queue queue, CommercialQueueLine currentQueueLine) {
        if (queue == null || currentQueueLine == null) {
            return "";
        }
        String queueChar = currentQueueLine.getQueueChar();
        if (queue.getQueueSource() != null && queue.getQueueSource() != QueueOrderSource.DaoDian) {
            queueChar += "N";
        }
        String num = String.format(Locale.getDefault(), "%03d", queue.getQueueNumber());
        return queueChar + num;
    }

    /**
     * 只传分类id
     *
     * @param context
     * @param queue
     * @param
     * @return
     * @date:2015年8月31日
     */
    public static String formatNum(Context context, Queue queue) {
        QueueDal dal = OperatesFactory.create(QueueDal.class);
        CommercialQueueLine queuLine = null;
        try {
            queuLine = dal.findQueueLineByid(queue.getQueueLineId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "", e);
        }
        return formatNum(queue, queuLine);
    }

    /**
     * 叫号并提示
     *
     * @param num
     * @param animView
     * @param queue
     * @param queue
     * @param
     * @date:2015年9月2日
     */
    public static void playNum(Context context, final String num, final View animView, final Queue queue) {
        try {
            if (animDrawable != null && animDrawable.isRunning()) {
                animDrawable.stop();
                mAnimView.setBackgroundResource(R.drawable.queue_list_item_remind_selector);
            }
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
            threeSectionCom.prepare(num, new OnMediaLister() {
                @Override
                public void onComplete() {
                    if (animView != null) {
                        animView.setBackgroundResource(R.drawable.queue_list_item_remind_selector);
                    }

                    //updateRemindCount(queue);
                }
            });
            threeSectionCom.play(isDownLoad);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public static void updateRemindCount(Queue queue) {
        try {
            queue.setNotifyType(QueueNotifyType.NOTIFIEDVOICE);
            QueueDal dal = OperatesFactory.create(QueueDal.class);
            // 更新数据需要重新上传
            queue.setIsOk(SuccessOrFaild.FAILD);
//            queue.setModifyDateTime(new Date().getTime());
            queue.setRemindCount(queue.getRemindCount() == null ? 1 : queue.getRemindCount() + 1);
            queue.setRemindTime(System.currentTimeMillis());
            dal.updateQueue(queue);
            //TVClientService.start(MainApplication.getInstance());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "", e);
        }
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

    public static void doAnimation(AnimationDrawable animDrawable) {

        if (animDrawable.isRunning()) {
            animDrawable.stop();
        }
        animDrawable.start();
    }

    public static void clear() {
        animDrawable = null;
        mAnimView = null;
    }

    /**
     * 获取当前队列正在排队的数量
     *
     * @param line
     * @return
     */
    public int getQueueCount(CommercialQueueLine line) {
        int count = 0;
        List<Queue> queueingList = getQueueingList();
        if (queueingList != null && queueingList.size() > 0) {
            for (Queue queue : queueingList) {
                if (queue.getQueueLineId().equals(line.getId())) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 获取当前队列正在排队的数量
     *
     * @param line
     * @return
     */
    public int getQueueHistoryCount(CommercialQueueLine line) {
        int count = 0;
        List<Queue> queueHistoryList = getQueueHistoryList();
        if (queueHistoryList != null && queueHistoryList.size() > 0) {
            for (Queue queue : queueHistoryList) {
                if (queue.getQueueLineId().equals(line.getId())) {
                    count++;
                }
            }
        }
        return count;
    }

    public Map<Long, PrepareTradeRelation> findPrepareTradeRelation() throws Exception {
        PrepareTradeRelationDal dal = OperatesFactory.create(PrepareTradeRelationDal.class);
        return dal.findLineMap();
    }


    /**
     * 创建队列
     *
     * @param queue
     */
    public void createQueueRequest(final FragmentActivity context, Queue queue, final SimpleResponseListener<Queue> callback) {
        QueueOperates operates = OperatesFactory.create(QueueOperates.class);
        operates.createAndUpdateQueue(0, queue, LoadingResponseListener.ensure(new SimpleResponseListener<Queue>() {
            @Override
            public void onSuccess(ResponseObject<Queue> response) {
                ToastUtil.showLongToast(R.string.booking_request_success);
                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                ToastUtil.showLongToast(error.getMessage());
                if (callback != null) {
                    callback.onError(error);
                }
            }
        }, context.getSupportFragmentManager()));

    }

}
