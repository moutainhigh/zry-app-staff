package com.zhongmei.bty.dinner.table.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.AttributeSet;

import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;

public class UnionTradeGroupView extends DirectDragableView {

    private IDinnertableTrade mModel;

    public UnionTradeGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IDinnertableTrade getModel() {
        return mModel;
    }

    public void setModel(IDinnertableTrade mModel) {
        this.mModel = mModel;
    }

    @Override
    protected ClipData getClipData() {
        ClipData.Item item = new ClipData.Item("union_trade_group");
        return new ClipData("union_trade_group",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
    }
}
