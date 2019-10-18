package com.zhongmei.bty.dinner.table.view;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;


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
