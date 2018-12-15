package com.zhongmei.beauty.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.bty.commonmodule.util.DateUtil;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyBookingTimeShaft extends View {

    private Long startTime; //开店时间
    private Long stopTime; //关店时间
    private Long perTime = 30 * 60 * 1000L;//最小时间间隔 默认半小时
    private int timeWidth;

    private Paint mPaint;

    public BeautyBookingTimeShaft(Context context) {
        super(context);
        initData();
    }

    public BeautyBookingTimeShaft(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public BeautyBookingTimeShaft(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeautyBookingTimeShaft(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData();
    }

    private void initData() {
        timeWidth = getResources().getDimensionPixelSize(R.dimen.beauty_reserver_board_item_width);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.beauty_color_999999));
        mPaint.setTextSize(getResources().getDimension(R.dimen.text_12));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawTimeShaft(canvas);
        super.onDraw(canvas);
    }

    private void drawTimeShaft(Canvas canvas) {
        Long curTime = startTime;
        int offsetY = 0;
        int offsetX = 0;
        while (curTime < stopTime) {
            String timePoint = DateUtil.format(curTime, DateUtil.TIME_FORMAT);
            Rect rect = new Rect();
            mPaint.getTextBounds(timePoint, 0, timePoint.length(), rect);
            offsetY = (getHeight() - rect.height()) / 2 + rect.height();
            canvas.drawText(timePoint, offsetX, offsetY, mPaint);

            offsetX += timeWidth;
            curTime += perTime;
        }

        setLayoutParams(offsetX);
    }

    public void setBusinessTime(Long startTime, Long stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
        postInvalidate();
    }

    private void setLayoutParams(int width) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
