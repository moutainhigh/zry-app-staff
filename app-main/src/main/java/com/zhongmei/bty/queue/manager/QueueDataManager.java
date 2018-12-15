package com.zhongmei.bty.queue.manager;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.basemodule.commonbusiness.operates.PrepareTradeRelationDal;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.queue.vo.NewQueueAreaVo;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class QueueDataManager {

    public static final String TAG = QueueDataManager.class.getSimpleName();

    private static final Long ALL_AREA_ID = 0L;

    private PrepareTradeRelationDal tradeRelationDal;

    private QueueDal queueDal;

    private TradeDal tradeDal;

    private List<CommercialQueueLine> queueLineList;

    private List<Queue> queueList;

    private List<QueueExtra> queueExtraList;

    private List<TradeVo> tradeVoList;

    private Map<Long, PrepareTradeRelation> tradeRelationMap;

    private Map<Long, List<Queue>> queueLineIdQueueListMap;

    private Map<Long, QueueExtra> queueIdQueueExtraMap;

    private List<NewQueueAreaVo> queueAreaVoList;

    private long selectedAreaId = ALL_AREA_ID;

    private DataObserver mObserver = new DataObserver();

    private DataChangedListener mOnChangedListener;

    public QueueDataManager() {
        queueDal = OperatesFactory.create(QueueDal.class);
        tradeRelationDal = OperatesFactory.create(PrepareTradeRelationDal.class);
        tradeDal = OperatesFactory.create(TradeDal.class);
    }


    private void queueData() {
        Log.e(TAG, "refresh_data");
        try {
            queueLineList = queueDal.queryQueueLineList();
            queueList = queueDal.findAllDataByDate(new Date(), null, null);
            if (Utils.isNotEmpty(queueList)) {
                List<Long> queueIdList = new ArrayList<>();
                for (Queue q : queueList) {
                    queueIdList.add(q.getId());
                }
                queueExtraList = queueDal.getQueueExtraList(queueIdList);
            }
            tradeRelationMap = tradeRelationDal.findLineMap();
            if (tradeRelationMap != null && tradeRelationMap.size() > 0) {
                List<Long> tempTradeIdList = new ArrayList<>();
                List<Trade> tempTradeList = new ArrayList<>();
                for (PrepareTradeRelation tradeRelation : tradeRelationMap.values()) {
                    tempTradeIdList.add(tradeRelation.getTradeId());
                }
                tempTradeList = tradeDal.getTrade(tempTradeIdList, null);
                tradeVoList = tradeDal.getTradeVosByTrades(tempTradeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        queueLineIdQueueListMap = new HashMap<>();
        queueIdQueueExtraMap = new HashMap<>();
        if (Utils.isNotEmpty(queueList)) {
            for (Queue queue : queueList) {
                if (queue.getQueueStatus() == QueueStatus.CANCEL || queue.getQueueStatus() == QueueStatus.__UNKNOWN__)
                    continue;
                if (queueLineIdQueueListMap.containsKey(queue.getQueueLineId())) {
                    queueLineIdQueueListMap.get(queue.getQueueLineId()).add(queue);
                } else {
                    queueLineIdQueueListMap.put(queue.getQueueLineId(), new ArrayList<Queue>());
                    queueLineIdQueueListMap.get(queue.getQueueLineId()).add(queue);
                }
            }
        }
        if (Utils.isNotEmpty(queueExtraList)) {
            for (QueueExtra queueExtra : queueExtraList) {
                queueIdQueueExtraMap.put(queueExtra.queueID, queueExtra);
            }
        }
    }

    public List<NewQueueAreaVo> getQueueAreaVoList() {
        queueData();
        initData();
        if (Utils.isNotEmpty(queueAreaVoList)) {
            for (NewQueueAreaVo queueAreaVo : queueAreaVoList) {
                if (queueAreaVo.isSelected()) {
                    selectedAreaId = queueAreaVo.getQueueLine().getId();
                }
            }
        }
        queueAreaVoList = new ArrayList<>();
        if (Utils.isNotEmpty(queueLineList)) {
            for (CommercialQueueLine line : queueLineList) {
                queueAreaVoList.add(createQueueAreaVo(line));
            }
        }
        NewQueueAreaVo allQueueAreaVo = new NewQueueAreaVo();
        CommercialQueueLine queueLine = new CommercialQueueLine();
        queueLine.setId(ALL_AREA_ID);
        allQueueAreaVo.setQueueLine(queueLine);
        allQueueAreaVo.setSelected(queueLine.getId().longValue() == selectedAreaId);
        allQueueAreaVo.setQueueingBeanVoList(new ArrayList<NewQueueBeanVo>());
        allQueueAreaVo.setQueuedBeanVoList(new ArrayList<NewQueueBeanVo>());
        if (Utils.isNotEmpty(queueAreaVoList)) {
            for (NewQueueAreaVo queueAreaVo : queueAreaVoList) {
                if (Utils.isNotEmpty(queueAreaVo.getQueueingBeanVoList()))
                    allQueueAreaVo.getQueueingBeanVoList().addAll(queueAreaVo.getQueueingBeanVoList());
                if (Utils.isNotEmpty(queueAreaVo.getQueuedBeanVoList()))
                    allQueueAreaVo.getQueuedBeanVoList().addAll(queueAreaVo.getQueuedBeanVoList());
            }
        }
        queueAreaVoList.add(allQueueAreaVo);
        Collections.sort(queueAreaVoList, new Comparator<NewQueueAreaVo>() {
            @Override
            public int compare(NewQueueAreaVo o1, NewQueueAreaVo o2) {
                return o1.getQueueLine().getId().compareTo(o2.getQueueLine().getId());
            }
        });
        return queueAreaVoList;
    }

    private NewQueueAreaVo createQueueAreaVo(CommercialQueueLine queueLine) {
        NewQueueAreaVo queueAreaVo = new NewQueueAreaVo();
        queueAreaVo.setSelected(queueLine.getId().longValue() == selectedAreaId);
        queueAreaVo.setQueueingBeanVoList(new ArrayList<NewQueueBeanVo>());
        queueAreaVo.setQueuedBeanVoList(new ArrayList<NewQueueBeanVo>());
        if (queueLineIdQueueListMap.containsKey(queueLine.getId())) {
            NewQueueBeanVo queueBeanVo;
            for (Queue queue : queueLineIdQueueListMap.get(queueLine.getId())) {
                queueBeanVo = createQueueBeanVo(queueLine, queue);
                if (queue.getQueueStatus() == QueueStatus.QUEUEING) {
                    queueAreaVo.getQueueingBeanVoList().add(queueBeanVo);
                } else {
                    queueAreaVo.getQueuedBeanVoList().add(queueBeanVo);
                }
            }
        }
        queueAreaVo.setQueueLine(queueLine);
        Comparator<NewQueueBeanVo> comparator = new Comparator<NewQueueBeanVo>() {
            @Override
            public int compare(NewQueueBeanVo o1, NewQueueBeanVo o2) {
                return o1.getQueue().getId().compareTo(o2.getQueue().getId());
            }
        };
        Collections.sort(queueAreaVo.getQueueingBeanVoList(), comparator);
        Collections.sort(queueAreaVo.getQueueingBeanVoList(), comparator);
        return queueAreaVo;
    }

    private NewQueueBeanVo createQueueBeanVo(CommercialQueueLine queueLine, Queue queue) {
        NewQueueBeanVo queueBeanVo = new NewQueueBeanVo();
        queueBeanVo.setQueueLine(queueLine);
        queueBeanVo.setQueue(queue);
        queueBeanVo.setQueueExtra(queueIdQueueExtraMap.get(queue.getId()));
        queueBeanVo.setTradeRelation(tradeRelationMap.get(queue.getId()));
        if (queueBeanVo.getTradeRelation() != null) {
            for (TradeVo tradeVo : tradeVoList) {
                if (tradeVo.getTrade().getId().longValue() == queueBeanVo.getTradeRelation().getTradeId().longValue()) {
                    queueBeanVo.setTradeVo(tradeVo);
                }
            }
        }
        return queueBeanVo;
    }

    public void register(DataChangedListener listener) {
        this.mOnChangedListener = listener;
        ContentResolver resolver = MainApplication.getInstance().getContentResolver();
        Uri mUri = DBHelperManager.getUri(Queue.class);
        Uri relationUri = DBHelperManager.getUri(PrepareTradeRelation.class);
        Uri queueExtraUri = DBHelperManager.getUri(QueueExtra.class);
        resolver.registerContentObserver(mUri, true, mObserver);
        resolver.registerContentObserver(relationUri, true, mObserver);
        resolver.registerContentObserver(queueExtraUri, true, mObserver);
    }

    public void unregister() {
        ContentResolver resolver = MainApplication.getInstance().getContentResolver();
        resolver.unregisterContentObserver(mObserver);
        resolver.unregisterContentObserver(mObserver);
        resolver.unregisterContentObserver(mObserver);
        mOnChangedListener = null;
    }


    private class DataObserver extends ContentObserver {

        public DataObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            Log.e(TAG, uri.getPath());
            if (mOnChangedListener != null) {
                mOnChangedListener.onChanged();
            }
        }
    }

    public interface DataChangedListener {
        void onChanged();
    }
}
