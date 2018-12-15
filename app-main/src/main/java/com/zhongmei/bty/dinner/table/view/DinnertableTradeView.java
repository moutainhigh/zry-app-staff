package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;

/**
 * 桌台订单视图
 *
 * @version: 1.0
 * @date 2015年9月2日
 */
public class DinnertableTradeView extends TableTradeViewBase {

    private static final String TAG = DinnertableTradeView.class.getSimpleName();

    public DinnertableTradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setModel(IDinnertableTrade model) {
        super.setModel(model);

        TextView tvUnion = (TextView) findViewById(R.id.tv_union);


        if (tvUnion != null) {
            tvUnion.setVisibility(isUnionMainTrade(getModel().getTradeType()) ? View.VISIBLE : View.GONE);
        }

    }
}


