package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;


public class DinnertableView extends TableViewBase {


    public DinnertableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(DinnertableModel model) {
        super.setModel(model);
    }

    protected TableTradeViewBase createTradeView(IDinnertableTrade tradeModel) {
        DinnertableTradeView tradeView = ViewUtils.inflateTradeView(getContext());
        tradeView.setEnabledDrag(false);
        tradeView.enablePreCashDisplay(true);
        tradeView.enableHttpRecord(false);
        tradeView.setModel(tradeModel);
        return tradeView;
    }

    @SuppressWarnings("deprecation")
    protected void refresh() {
        super.refresh();

                int businessIconRes = -1;

                if (mModel.isGroup()) {
            businessIconRes = R.drawable.dinner_table_group_icon;
        }

                if (mModel.isBuffet()) {
            businessIconRes = R.drawable.dinner_table_buffet_icon;
        }

        if (businessIconRes > 0) {
            reserveIv.setBackgroundResource(businessIconRes);
            reserveIv.setVisibility(View.VISIBLE);
        } else {
            reserveIv.setVisibility(View.GONE);
        }
    }

}
