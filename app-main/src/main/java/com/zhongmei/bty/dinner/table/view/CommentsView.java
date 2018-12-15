package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.util.DensityUtil;

public class CommentsView extends LinearLayout {
    private Context context;

    final int itemMargins = 10; // 标签之间的间距 px

    final int lineMargins = 10; // 标签的行间距 px

    private ViewGroup container = null;

    private String[] itemLists = null;

    private ItemClickListener itemClickListener;// 点击监听

    public CommentsView(Context context) {
        this(context, null, 0);
    }

    public CommentsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CommentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        //initialView(context);
    }

    /**
     * @param context
     * @Date 2015-9-11
     * @Description: 初始化控件内容
     * @Return void
     */
    public void initialView(Context context, int width) {
        if (itemLists == null || itemLists.length == 0) {
            return;
        }
        // 初始化最外面的LinearLayout
        LinearLayout outsideLayout = new LinearLayout(context);
        addView(outsideLayout);
        outsideLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        outsideLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        outsideLayout.setOrientation(LinearLayout.VERTICAL);
        int padding = DensityUtil.dip2px(context, 10);
        outsideLayout.setPadding(padding, padding, padding, padding);
        int containerWidth =/*getMeasuredWidth()*/width - outsideLayout.getPaddingRight() - outsideLayout.getPaddingLeft();

        LayoutInflater inflater = LayoutInflater.from(context);
        Paint paint = new Paint();

        // 初始化每一项textview
        TextView textView = createTextView();

        final int itemPadding = textView.getCompoundPaddingLeft() + textView.getCompoundPaddingRight();
        LayoutParams tvParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(0, 0, itemMargins, 0);
        paint.setTextSize(textView.getTextSize());

        // 第一行layout
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        outsideLayout.addView(layout);
        // 每一行的布局
        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, lineMargins, 0, 0);

        int remainWidth = containerWidth;// 一行剩余宽度

        // 循环添加item
        for (int i = 0; i < itemLists.length; ++i) {
            final String text = itemLists[i];
            final float itemWidth = paint.measureText(text) + itemPadding;
            if (remainWidth > itemWidth) {// 当前行可以添加item
                addItemView(layout, tvParams, text);
            } else {// 换行添加item
                resetTextViewMarginsRight(layout);
                layout = new LinearLayout(context);
                layout.setLayoutParams(params);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                addItemView(layout, tvParams, text);// 将前面textview加入新的一行
                outsideLayout.addView(layout);
                remainWidth = containerWidth;
            }
            remainWidth = (int) (remainWidth - itemWidth + 0.5f) - itemMargins;
        }
        resetTextViewMarginsRight(layout);

    }

    /**
     * @param viewGroup
     * @Date 2015-9-11
     * @Description: 将每行最后一个textview的MarginsRight去掉
     * @Return void
     */
    private void resetTextViewMarginsRight(ViewGroup viewGroup) {
        final TextView tempTextView = (TextView) viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        if (tempTextView != null) {//防止空指针
            tempTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * @param inflater
     * @param viewGroup
     * @param params
     * @param text
     * @Date 2015-9-11
     * @Description: Linearlayout添加item
     * @Return void
     */
    private void addItemView(ViewGroup viewGroup, LayoutParams params, final String text) {
        TextView itemView = createTextView();
        itemView.setText(text);
        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                itemClickListener.onClick(text);

            }
        });
        viewGroup.addView(itemView, params);
    }

    /**
     * @return
     * @Date 2015-9-11
     * @Description: 创建textview
     * @Return TextView
     */
    private TextView createTextView() {
        int padding = DensityUtil.dip2px(context, 10);
        // 初始化每一项textview
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textView.setBackgroundColor(Color.parseColor("#FF8000"));
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextSize(DensityUtil.sp2px(context, 16));
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(1);
        return textView;

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setAdatper(String[] itemLists) {
        this.itemLists = itemLists;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(String content);
    }

}

