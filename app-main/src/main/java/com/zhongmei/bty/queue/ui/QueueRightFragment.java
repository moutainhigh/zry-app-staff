package com.zhongmei.bty.queue.ui;

import android.net.Uri;
import android.os.Bundle;

import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.queue.QueueMainActivity;
import com.zhongmei.bty.queue.event.IdleTableCountChangeEvent;
import com.zhongmei.bty.queue.event.QueueShowDetailEvent;
import com.zhongmei.bty.queue.event.SwictPageEvent;
import com.zhongmei.bty.queue.vo.QueueAreaVo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Author：LiuYang
 * Date：2016/6/27 17:26
 * E-mail：liuy0
 */
@EFragment(R.layout.queue_right_layout)
public class QueueRightFragment extends QueueBaseFragment {

    public static final String TAG = QueueRightFragment.class.getSimpleName();

    private int currentPage = QueueMainActivity.PAGE_CREATE_QUEUE;

    private QueueTakeNumberRightFragment createQueueFragment;
    private QueueTableSelectFragment idleTableFragment;
    private QueueDetailFragment queueDetailFragment;

    private TablesDal tablesDal;
    //private List<Tables> emptTabls;
    private TableDataChangeObserver observer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tablesDal = OperatesFactory.create(TablesDal.class);
        observer = new TableDataChangeObserver();
        DatabaseHelper.Registry.register(observer);
    }

    @AfterViews
    void init() {
        registerEventBus();
        createQueueFragment = new QueueTakeNumberRightFragment_();
        idleTableFragment = new QueueTableSelectFragment_();
        queueDetailFragment = new QueueDetailFragment_();
        changePage();
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        DatabaseHelper.Registry.unregister(observer);
        super.onDestroy();
    }

    private void changePage() {
        if (currentPage == QueueMainActivity.PAGE_CREATE_QUEUE) {
            if (createQueueFragment == null) {
                createQueueFragment = new QueueTakeNumberRightFragment_();
            }
            if (!createQueueFragment.isAdded()) {
                replaceChildFragment(R.id.right_root_layout, createQueueFragment, QueueTakeNumberRightFragment.class.getSimpleName());
            }
        } else if (currentPage == QueueMainActivity.PAGE_IDLE_TABLE) {
            if (idleTableFragment == null) {
                idleTableFragment = new QueueTableSelectFragment_();
            }
            if (!idleTableFragment.isAdded()) {
                replaceChildFragment(R.id.right_root_layout, idleTableFragment, QueueTableSelectFragment.class.getSimpleName());
            }
        } else if (currentPage == QueueMainActivity.PAGE_QUEUE_DETAIL) {
            if (queueDetailFragment == null) {
                queueDetailFragment = new QueueDetailFragment_();
            }
            if (!queueDetailFragment.isAdded()) {
                replaceChildFragment(R.id.right_root_layout, queueDetailFragment, QueueDetailFragment.class.getSimpleName());
            }
        }
    }

    public void onEventMainThread(SwictPageEvent selectEvent) {
        //只在创建队列页和空闲桌台页的时候记录当前page
        if (selectEvent.page == QueueMainActivity.PAGE_CREATE_QUEUE || selectEvent.page == QueueMainActivity.PAGE_IDLE_TABLE || selectEvent.page == QueueMainActivity.PAGE_QUEUE_DETAIL) {
            currentPage = selectEvent.page;
            changePage();
        }
    }

    /**
     * 点击queueListItem展示详情
     *
     * @param event
     */
    public void onEventMainThread(QueueShowDetailEvent event) {
        if (queueDetailFragment != null) {
            queueDetailFragment.setQueueBeanVo(event.getQueueBeanVo());
        }
    }

    private int getEmptTablCountByAreaID(List<Tables> tables, long area_id) {
        int count = 0;
        for (Tables table : tables) {
            if (table.getAreaId().equals(area_id)) {
                count++;
            }
        }
        return count;
    }


    /**
     * 桌台数据有变化
     */
    public class TableDataChangeObserver implements DatabaseHelper.DataChangeObserver {

        @Override
        public void onChange(Collection<Uri> uris) {
            if (uris.contains(DBHelperManager.getUri(Trade.class))) {
                try {
                    List<Tables> tables = tablesDal.listDinnerEmptyTablesByStatus(TableStatus.EMPTY);
                    List<Tables> tmp = new ArrayList<Tables>();
                    tmp.addAll(tables);
                    List<Tables> emptTabls = tablesDal.listDinnerEmptyTablesByStatus(TableStatus.EMPTY);
                    //求新增的空桌台
                    tmp.removeAll(emptTabls);
                    //当前的空桌台赋值为最新的数据
                    emptTabls = tables;
                    //通知显示空闲的桌台数
                    EventBus.getDefault().post(new IdleTableCountChangeEvent(emptTabls == null ? 0 : emptTabls.size()));

                    List<CommercialArea> areaList = tablesDal.listDinnerArea();
                    Map<Long, QueueAreaVo> areaVoMap = new HashMap<Long, QueueAreaVo>();
                    if (tables != null && tables.size() > 0) {
                        //有新增桌台，同区域进行合并
                        for (Tables table : tmp) {
                            if (areaVoMap.containsKey(table.getAreaId())) {
                                //合并同区域桌台
                                areaVoMap.get(table.getAreaId()).getTablesList().add(table);
                            } else {
                                QueueAreaVo vo = new QueueAreaVo();
                                for (CommercialArea area : areaList) {
                                    if (area.getId().equals(table.getAreaId())) {
                                        vo.setArea(area);
                                        vo.getTablesList().add(table);
                                        areaVoMap.put(area.getId(), vo);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    for (Map.Entry<Long, QueueAreaVo> entry : areaVoMap.entrySet()) {

                        if (entry.getValue().getTablesList() == null && entry.getValue().getTablesList().size() <= 0) {
                            continue;
                        }

//                for(CommercialArea area : areaList){
//                    if(area.getId() ==  entry.getKey()){
                        /*EventBus.getDefault().post(new ActionNotifySwitcher(EntranceType.QUEUE,
                                String.format(getString(R.string.queue_desk_idle),
                                        entry.getValue().getArea().getAreaName(),
                                        getEmptTablCountByAreaID(tables, entry.getValue().getArea().getId())), NotificationType.FREE_TABLES, true));*/
//                        break;
//                    }
//                }
                    }
                    //发送桌台状态更新消息
                    EventBus.getDefault().post(tables);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
