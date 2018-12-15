package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;

/**
 * 正餐桌台视图
 *
 * @version: 1.0
 * @date 2015年8月26日
 */
public class DinnertableView extends TableViewBase {


    public DinnertableView(Context context, AttributeSet attrs) {
        super(context, attrs);
//		mTradeViews = new ArrayList<DinnertableTradeView>();
    }

    public void setModel(DinnertableModel model) {
//		UserActionEvent.start(UserActionEvent.DINNER_TABLE_REFRESH);
        super.setModel(model);
//		UserActionEvent.end(UserActionEvent.DINNER_TABLE_REFRESH);
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

        //显示预定图标
        int businessIconRes = -1;

        //增加团餐图标
        if (mModel.isGroup()) {
            businessIconRes = R.drawable.dinner_table_group_icon;
        }

        //增加团餐图标
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
