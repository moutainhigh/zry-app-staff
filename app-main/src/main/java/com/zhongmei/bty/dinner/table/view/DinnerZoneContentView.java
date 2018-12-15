package com.zhongmei.bty.dinner.table.view;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;

/**
 * 桌台区域内容视图，不带滚动功能
 *
 * @version: 1.0
 * @date 2015年9月6日
 */
@SuppressWarnings("deprecation")
public class DinnerZoneContentView extends ZoneContentView {

    private static final String TAG = DinnerZoneContentView.class.getSimpleName();


    DinnerZoneContentView(HVScrollView scrollView, Callback callback) {
        super(scrollView, callback);
    }

    @Override
    protected TableViewBase createDinnertableView(DinnertableModel dinnertableModel) {
        DinnertableView dinnertableView = ViewUtils.inflateDinnertableView(getContext());
        LayoutParams layoutParams = getTableViewLayoutParams(dinnertableModel);
        dinnertableView.setLayoutParams(layoutParams);
        dinnertableView.setModel(dinnertableModel);
        dinnertableView.setOnClickListener(getOnClickListener());
        dinnertableView.setOnDragListener(getOnDragListener());
        return dinnertableView;
    }
}
