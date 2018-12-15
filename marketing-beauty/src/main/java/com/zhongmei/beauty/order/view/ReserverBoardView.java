package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.beauty.booking.interfaces.BeautyBookingTradeControlListener;
import com.zhongmei.beauty.entity.ReserverItemVo;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;


/**
 * Created by demo on 2018/12/15
 */

public class ReserverBoardView extends AbsoluteLayout {
    private final String TAG = ReserverBoardView.class.getSimpleName();
    private List<ReserverItemVo> mReserverTradeVos;
    private BeautyBookingTradeControlListener mControlListener;

    public ReserverBoardView(Context context, HVScrollView scrollView, BeautyBookingTradeControlListener listener) {
        super(context);
        this.mControlListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画格子
        drawableItemLine(canvas);
        super.onDraw(canvas);
    }

    private void drawableItemLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.beauty_color_DEDEDE));
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        int width = getWidth();
        int height = getHeight();
        Log.e("ReserverView", "mHeight=>" + height + ";mWidth=>" + width);
        float itemHeight = getResources().getDimensionPixelSize(R.dimen.beauty_reserver_board_item_height);
        float itemWidth = getResources().getDimensionPixelSize(R.dimen.beauty_reserver_board_item_width);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        float curPaintHeight = 0f;
        int index = 0;
        while (curPaintHeight < height) {//画水平线
            curPaintHeight += itemHeight;
            index++;
            canvas.drawLine(0, curPaintHeight - index, width, curPaintHeight - index, paint);
        }

        float curPaintWidth = 0f;
        while (curPaintWidth < width) {//画垂直的线
            curPaintWidth += itemWidth;
            canvas.drawLine(curPaintWidth, 0, curPaintWidth, height, paint);
        }

    }


    public void refreshReserverTradeVos(List<ReserverItemVo> mReserverTradeVos) {
        removeAllViews();
        this.mReserverTradeVos = mReserverTradeVos;

        if (Utils.isEmpty(mReserverTradeVos)) {
            return;
        }

        for (ReserverItemVo reserverTradeVo : this.mReserverTradeVos) {
            ReserverTradeView reserverTradeView = createReserverTradeView(getContext(), reserverTradeVo);
            addView(reserverTradeView);
        }
    }


    /**
     * 构建预定看板View
     *
     * @param context
     * @param reserverItemVo
     * @return
     */
    private ReserverTradeView createReserverTradeView(Context context, ReserverItemVo reserverItemVo) {
        ReserverTradeView reserverTradeView = (ReserverTradeView) LinearLayout.inflate(context, R.layout.beauty_reserver_tradeview, null);
        reserverTradeView.setLayoutParams(getReserverTradeViewLayoutParams(context, reserverItemVo));
        reserverTradeView.setReserverTradeVo(reserverItemVo.getmReserverVo());
        reserverTradeView.setOnClickListener(mOnClickListener);
        return reserverTradeView;
    }


    /**
     * 获取预定看板view参数
     *
     * @param context
     * @param reserverItemVo
     * @return
     */
    private LayoutParams getReserverTradeViewLayoutParams(Context context, ReserverItemVo reserverItemVo) {
        int width = (int) (reserverItemVo.getWidth() - 1);
        int height = (int) (reserverItemVo.getHeight() - 1);
        int x = (int) (reserverItemVo.getOffsetX() + 1);
        int y = (int) reserverItemVo.getOffserY();
        return new LayoutParams(width, height, x, y);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view instanceof ReserverTradeView) {
                if (mControlListener != null) {
                    ReserverTradeView tradeView = (ReserverTradeView) view;
                    mControlListener.editTrade(tradeView.getmReserverTradeVo());
                }
            }
        }
    };

}
