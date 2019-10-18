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

    final int itemMargins = 10;
    final int lineMargins = 10;
    private ViewGroup container = null;

    private String[] itemLists = null;

    private ItemClickListener itemClickListener;
    public CommentsView(Context context) {
        this(context, null, 0);
    }

    public CommentsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CommentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
            }


    public void initialView(Context context, int width) {
        if (itemLists == null || itemLists.length == 0) {
            return;
        }
                LinearLayout outsideLayout = new LinearLayout(context);
        addView(outsideLayout);
        outsideLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        outsideLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        outsideLayout.setOrientation(LinearLayout.VERTICAL);
        int padding = DensityUtil.dip2px(context, 10);
        outsideLayout.setPadding(padding, padding, padding, padding);
        int containerWidth =width - outsideLayout.getPaddingRight() - outsideLayout.getPaddingLeft();

        LayoutInflater inflater = LayoutInflater.from(context);
        Paint paint = new Paint();

                TextView textView = createTextView();

        final int itemPadding = textView.getCompoundPaddingLeft() + textView.getCompoundPaddingRight();
        LayoutParams tvParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(0, 0, itemMargins, 0);
        paint.setTextSize(textView.getTextSize());

                LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        outsideLayout.addView(layout);
                LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, lineMargins, 0, 0);

        int remainWidth = containerWidth;
                for (int i = 0; i < itemLists.length; ++i) {
            final String text = itemLists[i];
            final float itemWidth = paint.measureText(text) + itemPadding;
            if (remainWidth > itemWidth) {                addItemView(layout, tvParams, text);
            } else {                resetTextViewMarginsRight(layout);
                layout = new LinearLayout(context);
                layout.setLayoutParams(params);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                addItemView(layout, tvParams, text);                outsideLayout.addView(layout);
                remainWidth = containerWidth;
            }
            remainWidth = (int) (remainWidth - itemWidth + 0.5f) - itemMargins;
        }
        resetTextViewMarginsRight(layout);

    }


    private void resetTextViewMarginsRight(ViewGroup viewGroup) {
        final TextView tempTextView = (TextView) viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        if (tempTextView != null) {            tempTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }


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


    private TextView createTextView() {
        int padding = DensityUtil.dip2px(context, 10);
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

