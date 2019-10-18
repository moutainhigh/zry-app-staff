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
    }

    protected ZoneModel getModel() {
        return mModel;
    }

    protected void setModel(ZoneModel model) {
        mModel = model;
        dinnertableViewFinder.clear();

        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            if (view instanceof TableViewBase) {
                removeDinnertableView((TableViewBase) view);
            }
        }
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
                addView(dinnertableView);
                dinnertableViewFinder.put(dinnertableModel.getId(), dinnertableView);

            }
        }
    }

    public abstract OnDragListener getOnDragListener();

    public abstract OnClickListener getOnClickListener();

    protected abstract TableViewBase createDinnertableView(DinnertableModel dinnertableModel);


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



    public void refreshOpenTableStatus() {
        for (int i = dinnertableViewFinder.size() - 1; i >= 0; i--) {
            TableViewBase dinnertableView = dinnertableViewFinder.valueAt(i);
            dinnertableView.refreshOpenTableStatus();
        }

    }

}
