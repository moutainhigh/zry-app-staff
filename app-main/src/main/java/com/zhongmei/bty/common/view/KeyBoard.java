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
        // TODO Auto-generated constructor stub
    }

    // @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                listener.numberClicked("1");
            }
        });
        v2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("2");
            }
        });
        v3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("3");
            }
        });
        v4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("4");
            }
        });
        v5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("5");
            }
        });
        v6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("6");
            }
        });
        v7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("7");
            }
        });
        v8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("8");
            }
        });
        v9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("9");
            }
        });
        vDot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.dotClicked();
            }
        });
        v0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.numberClicked("0");
            }
        });
        vDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
        // Drawable
        // d1=ta.getDrawable(R.styleable.keyborad_bg1);
        // Drawable
        // d2=ta.getDrawable(R.styleable.keyborad_bg2);
        // Drawable
        // d3=ta.getDrawable(R.styleable.keyborad_bg3);
        // Drawable
        // d4=ta.getDrawable(R.styleable.keyborad_bg4);
        // Drawable
        // d5=ta.getDrawable(R.styleable.keyborad_bg5);
        // Drawable
        // d6=ta.getDrawable(R.styleable.keyborad_bg6);
        // Drawable
        // d7=ta.getDrawable(R.styleable.keyborad_bg7);
        // Drawable
        // d8=ta.getDrawable(R.styleable.keyborad_bg8);
        // Drawable
        // d9=ta.getDrawable(R.styleable.keyborad_bg9);
        // Drawable
        // dDot=ta.getDrawable(R.styleable.keyborad_bgDot);
        // Drawable
        // d0=ta.getDrawable(R.styleable.keyborad_bg0);
        // Drawable
        // dDelete=ta.getDrawable(R.styleable.keyborad_bgDelete);
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
        // setBackground(d1,getChildAt(0));
        // setBackground(d2,getChildAt(1));
        // setBackground(d3,getChildAt(2));
        // setBackground(d4,getChildAt(3));
        // setBackground(d5,getChildAt(4));
        // setBackground(d6,getChildAt(5));
        // setBackground(d7,getChildAt(6));
        // setBackground(d8,getChildAt(7));
        // setBackground(d9,getChildAt(8));
        // setBackground(dDot,getChildAt(9));
        // setBackground(d0,getChildAt(10));
        // setBackground(dDelete,getChildAt(11));
        rl = DensityUtil.dip2px(getContext(), rl);
        tb = DensityUtil.dip2px(getContext(), tb);

    }

    // public void setBackground(Drawable d1,View v) {
    // if (d1!=null) {
    // v.setLayoutParams(new
    // LayoutParams(LayoutParams.WRAP_CONTENT, -2));
    // v.setBackground(d1);
    // }
    // }

    public KeyBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    private List<List<View>> mAllViews = new ArrayList<List<View>>();

    /**
     * 记录每一行的最大高度
     */
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
        // 存储每一行所有的childView
        // RelativeLayout
        // layout=(RelativeLayout)getChildAt(0);
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
        // // 遍历所有的孩子
        // for (int i = 0; i < cCount; i++) {
        // View child = getChildAt(i);
        // MarginLayoutParams lp =
        // (MarginLayoutParams)child.getLayoutParams();
        // int childWidth = child.getMeasuredWidth();
        // int childHeight = child.getMeasuredHeight();
        //
        // // 如果已经需要换行
        // if (childWidth + lp.leftMargin + lp.rightMargin +
        // lineWidth > width) {
        // // 记录这一行所有的View以及最大高度
        // mLineHeight.add(lineHeight);
        // //
        // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
        // mAllViews.add(lineViews);
        // lineWidth = 0;// 重置行宽
        // lineViews = new ArrayList<View>();
        // }
        // /**
        // * 如果不需要换行，则累加
        // */
        // lineWidth += childWidth + lp.leftMargin +
        // lp.rightMargin;
        // lineHeight = Math.max(lineHeight, childHeight +
        // lp.topMargin + lp.bottomMargin);
        // lineViews.add(child);
        // }
        // // 记录最后一行
        // mLineHeight.add(lineHeight);
        // mAllViews.add(lineViews);
        //
        // int left = 0;
        // int top = 0;
        // // 得到总行数
        // int lineNums = mAllViews.size();
        // for (int i = 0; i < lineNums; i++) {
        // // 每一行的所有的views
        // lineViews = mAllViews.get(i);
        // // 当前行的最大高度
        // lineHeight = mLineHeight.get(i);
        //
        // Log.e(TAG, "第" + i + "行 ：" + lineViews.size() +
        // " , " + lineViews);
        // Log.e(TAG, "第" + i + "行， ：" + lineHeight);
        //
        // // 遍历当前行所有的View
        // for (int j = 0; j < lineViews.size(); j++) {
        // View child = lineViews.get(j);
        //
        // if (child.getVisibility() == View.GONE) {
        // continue;
        // }
        // MarginLayoutParams lp =
        // (MarginLayoutParams)child.getLayoutParams();
        //
        // // 计算childView的left,top,right,bottom
        // int lc = left + lp.leftMargin;
        // int tc = top + lp.topMargin;
        // int rc = lc + child.getMeasuredWidth();
        // int bc = tc + child.getMeasuredHeight();
        //
        // Log.e(TAG, child + " , l = " + lc + " , t = " + t
        // + " , r =" + rc + " , b = " + bc);
        //
        // child.layout(lc, tc, rc, bc);
        //
        // left += child.getMeasuredWidth() + lp.rightMargin
        // + lp.leftMargin;
        // }
        // left = 0;
        // top += lineHeight;
        // }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0;
        int height = 0;
        // RelativeLayout cView =
        // (RelativeLayout)getChildAt(0);
        View clView = getChildAt(0);
        measureChild(clView, widthMeasureSpec, heightMeasureSpec);
        width = clView.getMeasuredWidth() * 3 + 2 * DensityUtil.dip2px(getContext(), rl);
        height = clView.getMeasuredHeight() * 4 + 3 * DensityUtil.dip2px(getContext(), tb);
        Log.e("width", width + "-----" + height);
        // for (int i = 0; i < cCount; i++)
        // {
        // View child = getChildAt(i);
        // // 测量每一个child的宽和高
        // measureChild(child, widthMeasureSpec,
        // heightMeasureSpec);
        // // 得到child的lp
        // MarginLayoutParams lp = (MarginLayoutParams)
        // child
        // .getLayoutParams();
        // // 当前子空间实际占据的宽度
        // int childWidth = child.getMeasuredWidth() +
        // lp.leftMargin
        // + lp.rightMargin;
        // // 当前子空间实际占据的高度
        // int childHeight = child.getMeasuredHeight() +
        // lp.topMargin
        // + lp.bottomMargin;
        // /**
        // * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height
        // 然后开启新行
        // */
        // if (lineWidth + childWidth > sizeWidth)
        // {
        // width = Math.max(lineWidth, childWidth);// 取最大的
        // lineWidth = childWidth; // 重新开启新行，开始记录
        // // 叠加当前高度，
        // height += lineHeight;
        // // 开启记录下一行的高度
        // lineHeight = childHeight;
        // } else
        // // 否则累加值lineWidth,lineHeight取最大高度
        // {
        // lineWidth += childWidth;
        // lineHeight = Math.max(lineHeight, childHeight);
        // }
        // // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
        // if (i == cCount - 1)
        // {
        // width = Math.max(width, lineWidth);
        // height += lineHeight;
        // }
        //
        // }
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
