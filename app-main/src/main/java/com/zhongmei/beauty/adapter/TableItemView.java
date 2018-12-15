package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.view.OnControlListener;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.beauty_table)
public class TableItemView extends RelativeLayout implements View.OnClickListener {
    @ViewById(R.id.tv_table_name)
    protected TextView tv_name;

    @ViewById(R.id.iv_tip)
    protected ImageView iv_tip;

    @ViewById(R.id.tv_trade_no)
    protected TextView tv_tradeNo;

    private DinnertableModel mTableModel;

    private OnControlListener mOnControlListener;

    public TableItemView(Context context) {
        super(context);
    }

    public TableItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TableItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setmOnControlListener(OnControlListener mOnControlListener) {
        this.mOnControlListener = mOnControlListener;
    }

    public void refreshUI(DinnertableModel tableModel) {
        mTableModel = tableModel;
        tv_name.setText(tableModel.getName());
        if (tableModel.getTradeCount() > 0) {
            IDinnertableTrade tradeModel = tableModel.getDinnertableTrades().get(0);
            tv_tradeNo.setBackgroundColor((getResources().getColor(R.color.beauty_transparent)));
            tv_tradeNo.setText(tradeModel.getSn().toString());
            iv_tip.setVisibility(View.VISIBLE);
        } else {
            //没有订单，设置开台UI
            tv_tradeNo.setText("");
            tv_tradeNo.setBackground(getResources().getDrawable(R.drawable.beauty_icon_table_create_trade));
            iv_tip.setVisibility(View.GONE);
        }

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnControlListener != null) {
            mOnControlListener.onSelect(mTableModel, null);
        }
    }

}

