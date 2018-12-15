package com.zhongmei.bty.dinner.table.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.yunfu.db.enums.TradeStatus;

/**
 * @Date 2016/6/12
 * @Description:移菜拖动view
 */
public class MovedishDragView extends DirectDragableView {
    private static final String TAG = MovedishDragView.class.getSimpleName();
    private IDinnertableTradeMoveDish mModel;
    private DinnertableStatus statusOfView;
    private boolean enableTransparent;

    public MovedishDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(IDinnertableTradeMoveDish model) {
        statusOfView = DinnertableStatus.EMPTY;
        mModel = model;
        if (mModel == null) {
            setVisibility(View.INVISIBLE);
        } else {
            int color;
            if (enableTransparent) {
                color = ViewUtils.COLOR_TRADE_EMPTY_TRANSPARENT;
            } else {
                color = ViewUtils.COLOR_TRADE_EMPTY;
            }
            // 未处理订单使用黑色
            if (mModel.getTradeStatus() != TradeStatus.UNPROCESSED) {
                statusOfView = mModel.getStatus();
                switch (statusOfView) {
                    case UNISSUED:
                        if (enableTransparent) {
                            color = ViewUtils.COLOR_TRADE_UNISSUED_TRANSPARENT;
                        } else {
                            color = ViewUtils.COLOR_TRADE_UNISSUED;
                        }
                        break;
                    case ISSUED:
                        if (enableTransparent) {
                            color = ViewUtils.COLOR_TRADE_ISSUED_TRANSPARENT;
                        } else {
                            color = ViewUtils.COLOR_TRADE_ISSUED;
                        }
                        break;
                    case SERVING:
                        if (enableTransparent) {
                            color = ViewUtils.COLOR_TRADE_SERVING_TRANSPARENT;
                        } else {
                            color = ViewUtils.COLOR_TRADE_SERVING;
                        }
                        break;
                    case EMPTY:
                        break;
                    default:
                        Log.e(TAG, "The status is wrong! " + statusOfView);
                }
            }
            TextView titleTv = (TextView) findViewById(R.id.title_tv);
            titleTv.setText(mModel.getTitle());
            titleTv.setTextColor(color);

            LinearLayout contentView = (LinearLayout) findViewById(R.id.content_ll);
            contentView.setBackgroundColor(color);
            TextView contentTv = (TextView) contentView.findViewById(R.id.content_tv);

            contentTv.setText(mModel.getContent());
            contentTv.setTextColor(Color.parseColor("#ffffff"));
            setVisibility(View.VISIBLE);
        }
    }

    DinnertableStatus getStatusOfView() {
        return statusOfView;
    }

    public IDinnertableTradeMoveDish getModel() {
        return mModel;
    }

    public void setEnableTransparent(boolean value) {
        enableTransparent = value;
    }

    @Override
    protected ClipData getClipData() {
        ClipData.Item item = new ClipData.Item("dragview");
        return new ClipData("dragview",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
    }


}
