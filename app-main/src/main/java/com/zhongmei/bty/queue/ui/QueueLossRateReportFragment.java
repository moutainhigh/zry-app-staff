package com.zhongmei.bty.queue.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.commonmodule.adapter.AbstractSpinerAdapter;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarDialog;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarView;
import com.zhongmei.bty.queue.adapter.QueueLineChooseAdapter;
import com.zhongmei.bty.queue.adapter.QueueReportAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.queue_loss_rate_report_fragment)
public class QueueLossRateReportFragment extends QueueBaseFragment {

    private final static String TAG = QueueLossRateReportFragment.class.getSimpleName();

    @ViewById(R.id.lv_report)
    ListView lvReport;

    private QueueReportAdapter queueReportAdapter;

    private CalendarDialog calendarDialog;

    @ViewById(R.id.begin_date_tv)
    TextView beginDateTv;

    @ViewById(R.id.end_date_tv)
    TextView endDateTv;

    @ViewById(R.id.tv_total_desk_count)
    TextView tvTotalDeskCount;

    @ViewById(R.id.tv_total_queue_count)
    TextView tvTotalQueueCount;

    @ViewById(R.id.tv_total_meal_count)
    TextView tvTotalMealCount;

    @ViewById(R.id.tv_total_out_count)
    TextView tvTotalOutCount;

    @ViewById(R.id.tv_total_loss_rate)
    TextView tvTotalLossRate;

    @ViewById(R.id.tv_analysis_result)
    TextView tvAnalysisResult;

    @ViewById(R.id.tv_queue_line)
    TextView tvQueueLine;

    private Date beginDate;

    private Date endDate;

    private QueueLineChoosePopWindow queueLineChoosePopWindow;

    private int chooseQueueLinePos = 0;

    private List<QueueLineChooseAdapter.QueueLineChooseItem> mQueueLineChooseItems;

    private Long chooseQueueLineId = QueueLineChooseAdapter.ALL_QUEUE_LINE_ID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queueReportAdapter = new QueueReportAdapter(getActivity());
    }

    @AfterViews
    void init() {
        lvReport.setAdapter(queueReportAdapter);

        beginDate = new Date(DateTimeUtils.getDayStart(new Date()));//默认查询当天的数据
//        beginDateStr = DateTimeUtils.formatDate(beginDate, DateTimeUtils.DATE_TIME_FORMAT3);
        beginDateTv.setText(DateTimeUtils.formatDate(beginDate, DateTimeUtils.DATE_FORMAT));

        endDate = new Date(DateTimeUtils.getDayEnd(new Date()));
//        endDateStr = DateTimeUtils.formatDate(endDate, DateTimeUtils.DATE_TIME_FORMAT3);
        endDateTv.setText(DateTimeUtils.formatDate(endDate, DateTimeUtils.DATE_FORMAT));

        queryQueueResult();

        initPopWindow();
    }

    private void initPopWindow() {
        queueLineChoosePopWindow = new QueueLineChoosePopWindow(getActivity());
        queueLineChoosePopWindow.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                chooseQueueLinePos = pos;
                if (Utils.isNotEmpty(mQueueLineChooseItems) && mQueueLineChooseItems.size() > pos) {
                    QueueLineChooseAdapter.QueueLineChooseItem item = mQueueLineChooseItems.get(pos);
                    chooseQueueLineId = item.queueLineId;
                    queryQueueResult();
                }
            }
        });
        queueLineChoosePopWindow.setWidth(DensityUtil.dip2px(getContext(), 150));
    }

    /**
     * @Date 2016/7/1
     * @Description:展示日期弹框
     * @Param
     * @Return
     */
    private void showCalendarDialog(byte type) {
        calendarDialog = new CalendarDialog(getActivity(), new DateClick(type));
        Date defaultDate = null;
        if (type == 0) {
            defaultDate = beginDate;
        } else {
            defaultDate = endDate;
        }
        calendarDialog.setDefaultSelected(defaultDate);
        calendarDialog.showDialog();
    }

    private class DateClick implements CalendarView.OnItemClickListener {
        private byte type;

        public DateClick(byte type) {
            this.type = type;
        }

        @Override
        public void OnItemClick(Date downDate) {
            if (new Date(DateTimeUtils.getDayStart(downDate)).compareTo(new Date()) > 0) {
                ToastUtil.showShortToast(com.zhongmei.yunfu.basemodule.R.string.reportcenter_date_pick_tip3);
                return;
            }
//            calendarDialog.dismiss();

            Log.i("zhubo", DateTimeUtils.formatDate(downDate));
            if (type == 0) {
                if (downDate.compareTo(endDate) > 0) {
                    ToastUtil.showShortToast(com.zhongmei.yunfu.basemodule.R.string.reportcenter_date_pick_tip);
                } else {
                    beginDate = new Date(DateTimeUtils.getDayStart(downDate));
//                    beginDateStr = DateTimeUtils.formatDate(beginDate, DateTimeUtils.DATE_TIME_FORMAT3);
                    beginDateTv.setText(DateTimeUtils.formatDate(beginDate, DateTimeUtils.DATE_FORMAT));
                    calendarDialog.dismiss();
                }

            } else if (type == 1) {
                if (downDate.compareTo(beginDate) < 0) {
                    ToastUtil.showShortToast(com.zhongmei.yunfu.basemodule.R.string.reportcenter_date_pick_tip2);
                } else {
                    endDate = new Date(DateTimeUtils.getDayEnd(downDate));
//                    endDateStr = DateTimeUtils.formatDate(endDate, DateTimeUtils.DATE_TIME_FORMAT3);
                    endDateTv.setText(DateTimeUtils.formatDate(endDate, DateTimeUtils.DATE_FORMAT));
                    calendarDialog.dismiss();
                }
            }

        }
    }

    @Click({R.id.begin_date_tv, R.id.begin_btn, R.id.end_date_tv, R.id.end_btn, R.id.layout_query, R.id.tv_queue_line})
    void click(View v) {
        switch (v.getId()) {
            case R.id.begin_date_tv:
            case R.id.begin_btn:
                showCalendarDialog((byte) 0);
                break;
            case R.id.end_date_tv:
            case R.id.end_btn:
                showCalendarDialog((byte) 1);
                break;
            case R.id.layout_query:
                queryQueueResult();
                break;
            case R.id.tv_queue_line:
                queryQueueLine();
                break;
        }
    }

    private void queryQueueLine() {
        TaskContext.bindExecute(this, new SimpleAsyncTask<List<QueueLineChooseAdapter.QueueLineChooseItem>>() {

            @Override
            public List<QueueLineChooseAdapter.QueueLineChooseItem> doInBackground(Void... params) {
                try {
                    QueueDal queueDal = OperatesFactory.create(QueueDal.class);
                    List<CommercialQueueLine> queueLines = queueDal.queryQueueLineList();
                    if (Utils.isNotEmpty(queueLines)) {
                        List<QueueLineChooseAdapter.QueueLineChooseItem> items = new ArrayList<>();
                        //添加全部队列
                        QueueLineChooseAdapter.QueueLineChooseItem allItem = new QueueLineChooseAdapter.QueueLineChooseItem();
                        allItem.queueLineId = QueueLineChooseAdapter.ALL_QUEUE_LINE_ID;
                        allItem.queueLineName = getString(R.string.queue_all_queue_line);
                        items.add(allItem);

                        for (CommercialQueueLine queueLine : queueLines) {
                            QueueLineChooseAdapter.QueueLineChooseItem item = new QueueLineChooseAdapter.QueueLineChooseItem();
                            item.queueLineId = queueLine.getId();
                            item.queueLineName = queueLine.getQueueName();
                            items.add(item);
                        }

                        return items;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }


                return null;
            }

            @Override
            public void onPostExecute(List<QueueLineChooseAdapter.QueueLineChooseItem> queueLineChooseItems) {
                mQueueLineChooseItems = queueLineChooseItems;
                if (queueLineChoosePopWindow != null) {
                    queueLineChoosePopWindow.refreshData(queueLineChooseItems, chooseQueueLinePos);
                    queueLineChoosePopWindow.showAsDropDown(tvQueueLine, -10, 0);
                }
            }
        });
    }

    private void queryQueueResult() {
        TaskContext.bindExecute(this, new SimpleAsyncTask<List<QueueReportAdapter.QueueReportDataItem>>() {

            @Override
            public List<QueueReportAdapter.QueueReportDataItem> doInBackground(Void... params) {
                try {
                    QueueDal queueDal = OperatesFactory.create(QueueDal.class);
                    List<Queue> queues = queueDal.listQueueBetweenTime(chooseQueueLineId, beginDate, endDate);
                    if (Utils.isNotEmpty(queues)) {
                        Map<Long, List<Queue>> queueMap = new HashMap<>();
                        for (Queue queue : queues) {
                            List<Queue> qs = queueMap.get(queue.getQueueLineId());
                            if (qs == null) {
                                qs = new ArrayList<>();
                                queueMap.put(queue.getQueueLineId(), qs);
                            }
                            qs.add(queue);
                        }

                        List<QueueReportAdapter.QueueReportDataItem> items = new ArrayList<>();
                        TablesDal tablesDal = OperatesFactory.create(TablesDal.class);
                        for (Map.Entry<Long, List<Queue>> queueEntry : queueMap.entrySet()) {
                            QueueReportAdapter.QueueReportDataItem item = new QueueReportAdapter.QueueReportDataItem();

                            Long queueLineId = queueEntry.getKey();
                            List<Queue> qs = queueEntry.getValue();

                            CommercialQueueLine queueLine = DBHelperManager.queryById(CommercialQueueLine.class, queueLineId);
                            item.queueLineId = queueLineId;
                            item.queueName = queueLine.getQueueName();
                            item.deskCount = (int) tablesDal.countOfTablesByPeopleCount(queueLine.getMinPersonCount(), queueLine.getMaxPersonCount());

                            item.queueCount = qs.size();
                            item.mealCount = 0;
                            item.outCount = 0;
                            for (Queue q : qs) {
                                if (q.getQueueStatus() == QueueStatus.ADMISSION) {
                                    item.mealCount++;
                                } else if (q.getQueueStatus() == QueueStatus.INVALID || q.getQueueStatus() == QueueStatus.CANCEL) {
                                    item.outCount++;
                                }
                            }
                            item.lossRate = ((double) item.outCount) / item.queueCount;

                            items.add(item);
                        }

                        //按照队列id排序
                        if (Utils.isNotEmpty(items)) {
                            Collections.sort(items, new Comparator<QueueReportAdapter.QueueReportDataItem>() {
                                @Override
                                public int compare(QueueReportAdapter.QueueReportDataItem o1, QueueReportAdapter.QueueReportDataItem o2) {
                                    return o1.queueLineId.compareTo(o2.queueLineId);
                                }
                            });
                        }

                        return items;
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }

                return null;
            }

            @Override
            public void onPostExecute(List<QueueReportAdapter.QueueReportDataItem> queueReportDataItems) {
                queueReportAdapter.datas = queueReportDataItems;
                queueReportAdapter.notifyDataSetChanged();

                showTotalReport(queueReportDataItems);
                showAnalysisResult(queueReportDataItems);
            }
        });
    }

    private void showTotalReport(List<QueueReportAdapter.QueueReportDataItem> queueReportDataItems) {
        int deskCount = 0;
        int queueCount = 0;
        int mealCount = 0;
        int outCount = 0;
        double lossRate = 0;
        if (Utils.isNotEmpty(queueReportDataItems)) {
            for (QueueReportAdapter.QueueReportDataItem item : queueReportDataItems) {
                deskCount += item.deskCount;
                queueCount += item.queueCount;
                mealCount += item.mealCount;
                outCount += item.outCount;
            }
        }

        if (queueCount == 0) {
            lossRate = 0;
        } else {
            lossRate = ((double) outCount) / queueCount;
        }

        tvTotalDeskCount.setText(deskCount + "");
        tvTotalQueueCount.setText(queueCount + "");
        tvTotalMealCount.setText(mealCount + "");
        tvTotalOutCount.setText(outCount + "");
        tvTotalLossRate.setText(MathDecimal.round(lossRate * 100, 2) + "%");

    }

    private void showAnalysisResult(List<QueueReportAdapter.QueueReportDataItem> queueReportDataItems) {
        StringBuilder sb = new StringBuilder();
        if (Utils.isNotEmpty(queueReportDataItems)) {
            for (QueueReportAdapter.QueueReportDataItem item : queueReportDataItems) {
                if (item.queueCount > 10 && item.lossRate > 0.3) {
                    sb.append(item.queueName + "、");
                }
            }
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            tvAnalysisResult.setText(getString(R.string.queue_analysis_suggestion, sb.toString()));
        } else {
            tvAnalysisResult.setText(getString(R.string.queue_analysis_no_suggestion));
        }
    }

}
