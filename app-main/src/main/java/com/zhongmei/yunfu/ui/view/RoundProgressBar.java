package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;

/**
 * Created by dingzb on 2019/3/1.
 */

public class RoundProgressBar extends View {
    private long mProgress=0;//当前进度

    private long mMax=100;//最大进度

    private int mHeight;//当前view的高

    private int mWidth; //当前View的宽

    private int mRadium;//圆的半径

    private Paint mPaint=new Paint();

    public RoundProgressBar(Context context) {
        super(context);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(getResources().getColor(R.color.beauty_color_434343));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);

        float mXCenter=mHeight/2f;
        float mYCenter=mHeight/2;

        //画一个灰色的背景圆
        canvas.drawCircle(mXCenter,mYCenter,mRadium,mPaint);

        //根据比例画一个圆弧
        int precent=(int)(mProgress/(float)mMax*100);//百分比
        if (mProgress > 0 ) {
            mPaint.setColor(getResources().getColor(R.color.beauty_main_bg_color));

            RectF oval = new RectF();
            oval.left = (mXCenter - mRadium);
            oval.top = (mYCenter - mRadium);
            oval.right = mXCenter + mRadium;
            oval.bottom = mXCenter + mRadium;
            canvas.drawArc(oval, -90, precent/100f * 360, true, mPaint); // //字体
        }

        //画一个黑色的圆
        mPaint.setColor(getResources().getColor(R.color.beauty_color_434343));
        canvas.drawCircle(mXCenter,mYCenter,mRadium- DensityUtil.dip2px(getContext(),15),mPaint);

        //将百分比写到中间
        String precentStr=precent+"%";
        Rect mRect = new Rect();
        mPaint.getTextBounds(precentStr, 0, precentStr.length(), mRect);
        mPaint.setColor(getResources().getColor(R.color.beauty_text_white));
        mPaint.setTextSize(DensityUtil.sp2px(getContext(),18));
        canvas.drawText(precent+"%",mXCenter-(mRect.right-mRect.left)/2,mYCenter+(mRect.bottom-mRect.top)/2,mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取MyTextView当前实例的宽
        mHeight = this.getHeight();
        mWidth = this.getWidth();
        //计算半径
        mRadium=mHeight>mWidth?mWidth/2:mHeight/2;
    }

    /**
     * 设置当前进度
     */
    public void setProgress(long progress){
        this.mProgress=progress;
        this.postInvalidate();
    }

    /**
     * 设置总进度
     */
    public void setMax(long max){
        this.mMax=max;
    }
}
