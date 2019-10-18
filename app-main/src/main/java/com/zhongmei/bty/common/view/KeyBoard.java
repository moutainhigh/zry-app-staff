package com.zhongmei.bty.common.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;

public class KeyBoard extends ViewGroup {
    private int rl, tb;

    private NumberClickListener listener;

    public void setListener(NumberClickListener listener) {
        this.listener = listener;
    }

    private String TAG = "333333";

    List<View> views;

    private KeyBoard(Context context) {
        this(context, null);
            }

        protected void onAttachedToWindow() {
                super.onAttachedToWindow();
        View v1 = findViewById(R.id.one);
        View v2 = findViewById(R.id.two);
        View v3 = findViewById(R.id.three);
        View v4 = findViewById(R.id.four);
        View v5 = findViewById(R.id.five);
        View v6 = findViewById(R.id.six);
        View v7 = findViewById(R.id.seven);
        View v8 = findViewById(R.id.eight);
        View v9 = findViewById(R.id.nine);
        View vDot = findViewById(R.id.dot);
        View v0 = findViewById(R.id.zero);
        View vDelete = findViewById(R.id.delete);

        v1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("1");
            }
        });
        v2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("2");
            }
        });
        v3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("3");
            }
        });
        v4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("4");
            }
        });
        v5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("5");
            }
        });
        v6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("6");
            }
        });
        v7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("7");
            }
        });
        v8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("8");
            }
        });
        v9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("9");
            }
        });
        vDot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.dotClicked();
            }
        });
        v0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.numberClicked("0");
            }
        });
        vDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                                listener.deleteClicked();
            }
        });
    }

    public KeyBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.keyborad);

        rl = ta.getInteger(R.styleable.keyborad_margingRL, 0);
        tb = ta.getInteger(R.styleable.keyborad_margingTB, 0);
        int layOut = ta.getResourceId(R.styleable.keyborad_source, 0);
                                                                                                                                                                                                        ta.recycle();
        this.removeAllViews();
        RelativeLayout lay = (RelativeLayout) LayoutInflater.from(getContext()).inflate(layOut, null);
        int c = lay.getChildCount();
        views = new ArrayList<View>();
        for (int i = 0; i < c; i++) {
            views.add(lay.getChildAt(i));
        }
        lay.removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            addView(views.get(i), i);
        }
                                                                                                        rl = DensityUtil.dip2px(getContext(), rl);
        tb = DensityUtil.dip2px(getContext(), tb);

    }


    public KeyBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
            }

    private List<List<View>> mAllViews = new ArrayList<List<View>>();


    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        int width = getWidth();
        int height = getHeight();
        int itemWidth = (width - 2 * rl) / 3;
        int itemHeight = (height - 3 * tb) / 4;
        int plus = +DensityUtil.dip2px(getContext(), 1);
                                int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            int left = 0;
            int top = 0;
            View cView = getChildAt(i);
            switch (i % 3) {
                case 0:
                    left = plus;
                    break;
                case 1:
                    left = plus + itemWidth + rl;
                    break;
                case 2:
                    left = plus + itemWidth * 2 + 2 * rl;
                    break;
                default:
                    break;
            }
            switch (i / 3) {
                case 0:
                    top = plus;
                    break;
                case 1:
                    top = plus + itemHeight + tb;
                    break;
                case 2:
                    top = plus + 2 * itemHeight + 2 * tb;
                    break;
                case 3:
                    top = plus + 3 * itemHeight + 3 * tb;
                    break;
                default:
                    break;
            }
            cView.layout(left, top, left + itemWidth, top + itemHeight);
            Log.e("xxxxxxxx", left + "-----" + top + "----" + (left + itemWidth) + "----" + (top + itemHeight));
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
                        View clView = getChildAt(0);
        measureChild(clView, widthMeasureSpec, heightMeasureSpec);
        width = clView.getMeasuredWidth() * 3 + 2 * DensityUtil.dip2px(getContext(), rl);
        height = clView.getMeasuredHeight() * 4 + 3 * DensityUtil.dip2px(getContext(), tb);
        Log.e("width", width + "-----" + height);
                                                                                                                                                                                                                                                                                                                                                                        int plus = +DensityUtil.dip2px(getContext(), 2);
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth + plus : width + plus,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight + plus : height + plus);
    }

    public interface NumberClickListener {
        void numberClicked(String number);


        void dotClicked();


        void deleteClicked();
    }
}
