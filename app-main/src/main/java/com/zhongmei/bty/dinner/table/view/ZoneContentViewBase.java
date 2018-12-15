package com.zhongmei.bty.dinner.table.view;

import android.support.v4.util.LongSparseArray;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.AbsoluteLayout;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;

import java.util.HashMap;

/**
 * 楼层视图的基类
 *
 * @version: 1.0
 * @date 2015年9月6日
 */
@SuppressWarnings("deprecation")
abstract class ZoneContentViewBase extends AbsoluteLayout {

    protected final HVScrollView mScrollView;
    private final LongSparseArray<TableViewBase> dinnertableViewFinder;
    private LruCache<Long, TableViewBase> mTableViewCache;
    private ZoneModel mModel;
    private TableViewBase mSelectedDinnertableView;

    protected ZoneContentViewBase(HVScrollView scrollView) {
        super(scrollView.getContext());
        mScrollView = scrollView;
        dinnertableViewFinder = new LongSparseArray<TableViewBase>();
        mTableViewCache = new LruCache<>(100);
//		setBackgroundResource(R.drawable.dinnertable_zone_view_border);
    }

    protected ZoneModel getModel() {
        return mModel;
    }

    protected void setModel(ZoneModel model) {
//		UserActionEvent.start(UserActionEvent.DINNER_TABLE_ZONE_REFRESH);
        mModel = model;
        dinnertableViewFinder.clear();

        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            if (view instanceof TableViewBase) {
                removeDinnertableView((TableViewBase) view);
            }
        }
        // 创建桌台
        if (mModel.getDinnertableModels() != null) {
            for (DinnertableModel dinnertableModel : mModel.getDinnertableModels()) {
                TableViewBase dinnertableView = null;

                if (mTableViewCache != null) {
                    dinnertableView = mTableViewCache.get(dinnertableModel.getId());
                }

                if (dinnertableView == null) {
                    dinnertableView = createDinnertableView(dinnertableModel);
                    mTableViewCache.put(dinnertableModel.getId(), dinnertableView);
                } else {
                    dinnertableView.setModel(dinnertableModel);
                    dinnertableView.requestLayout();
                    dinnertableView.invalidate();
                    dinnertableView.measure(0, 0);
                    dinnertableView.setOnDragListener(getOnDragListener());
                    dinnertableView.setOnClickListener(getOnClickListener());
                }
//				DinnertableView dinnertableView=createDinnertableView(dinnertableModel);//原来的方式
                addView(dinnertableView);
                dinnertableViewFinder.put(dinnertableModel.getId(), dinnertableView);

            }
        }
//		UserActionEvent.end(UserActionEvent.DINNER_TABLE_ZONE_REFRESH);
    }

    public abstract OnDragListener getOnDragListener();

    public abstract OnClickListener getOnClickListener();

    protected abstract TableViewBase createDinnertableView(DinnertableModel dinnertableModel);

//	protected abstract DinnertableView createDinnertableView(DinnertableModel dinnertableModel) {
//		DinnertableView dinnertableView = ViewUtils.inflateDinnertableView(getContext());
//		int width = DensityUtil.dip2px(dinnertableModel.getWidth() + 8);
//		int height = DensityUtil.dip2px(dinnertableModel.getHeight() + 8);
//		int x = DensityUtil.dip2px(dinnertableModel.getX());
//		int y = DensityUtil.dip2px(dinnertableModel.getY());
//		LayoutParams layoutParams = new LayoutParams(width, height, x, y);
//		dinnertableView.setLayoutParams(layoutParams);
//		dinnertableView.setModel(dinnertableModel);
//		return dinnertableView;
//	}

    protected LayoutParams getTableViewLayoutParams(DinnertableModel dinnertableModel) {
        int width = DensityUtil.dip2px(MainApplication.getInstance(), dinnertableModel.getWidth() + 8);
        int height = DensityUtil.dip2px(MainApplication.getInstance(), dinnertableModel.getHeight() + 8);
        int x = DensityUtil.dip2px(MainApplication.getInstance(), dinnertableModel.getX());
        int y = DensityUtil.dip2px(MainApplication.getInstance(), dinnertableModel.getY());
        return new LayoutParams(width, height, x, y);
    }

    protected void removeDinnertableView(TableViewBase view) {
        removeView(view);
    }

    TableViewBase setSelectState(Long dinnertableId) {
        if (mSelectedDinnertableView != null) {
            mSelectedDinnertableView.unselect();
        }
        if (dinnertableId != null) {
            mSelectedDinnertableView = dinnertableViewFinder.get(dinnertableId);
            if (mSelectedDinnertableView != null) {
                mSelectedDinnertableView.select();
            }
        }

        return mSelectedDinnertableView;
    }

    boolean selectDinnertable(Long dinnertableId) {
        if (dinnertableId != null) {
            TableViewBase dinnertableView = dinnertableViewFinder.get(dinnertableId);
            if (dinnertableView != null) {
                return dinnertableView.performClick();
            }
        }
        return false;
    }

    void refreshDinnertable(Long dinnertableId) {
        if (dinnertableId != null) {
            TableViewBase dinnertableView = dinnertableViewFinder.get(dinnertableId);
            if (dinnertableView != null) {
                dinnertableView.refresh();
            }
        }
    }

    void refreshDinnertable() {
        for (int i = dinnertableViewFinder.size() - 1; i >= 0; i--) {
            TableViewBase dinnertableView = dinnertableViewFinder.valueAt(i);
            dinnertableView.refresh();
        }
    }

    void refreshSpendTime() {
        for (int i = dinnertableViewFinder.size() - 1; i >= 0; i--) {
            TableViewBase dinnertableView = dinnertableViewFinder.valueAt(i);
            dinnertableView.refreshSpendTime();
        }
    }

    /**
     * @Date 2016/8/12
     * @Description:刷新桌台预定状态
     * @Param
     * @Return
     */
    void refreshReserveStatus(HashMap<String, BookingTable> bookingMap) {
        for (int i = dinnertableViewFinder.size() - 1; i >= 0; i--) {
            TableViewBase dinnertableView = dinnertableViewFinder.valueAt(i);
            DinnertableModel mode = dinnertableView.getModel();
            if (bookingMap.get(mode.getUuid()) == null) {
                mode.setReserved(false);
            } else {
                mode.setReserved(true);
            }

            dinnertableView.refreshReserveStatus();
        }
    }


    /**
     * 刷新异步开台记录在桌台上的展示
     * 在DinnerTableStateCache中更新了异步记录，这边只是刷新一下。线程间通讯
     */
    public void refreshOpenTableStatus() {
        for (int i = dinnertableViewFinder.size() - 1; i >= 0; i--) {
            TableViewBase dinnertableView = dinnertableViewFinder.valueAt(i);
            dinnertableView.refreshOpenTableStatus();
        }

    }

}
