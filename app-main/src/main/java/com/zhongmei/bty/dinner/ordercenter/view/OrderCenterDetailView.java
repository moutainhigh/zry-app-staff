package com.zhongmei.bty.dinner.ordercenter.view;

import com.zhongmei.yunfu.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class OrderCenterDetailView extends LinearLayout {

    private TextView titleTextView;

    private TextView timeTextView;

    private LinearLayout linearLayout;

    private int itemtextcolor = Color.parseColor("#333333");
    private int goodsitemcolor = Color.parseColor("#555555");
    private int goodsitemtitlecolor = Color.parseColor("#a1a8b0");
    private int little_text_size = 12;
    private int nomal_text_size = 14;
    private int big_text_size = 16;
    public OrderCenterDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        initView();
    }


    private void initView() {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, dip2px(getContext(), 16));
        relativeLayout.setLayoutParams(layoutParams);
        addView(relativeLayout);

        RelativeLayout.LayoutParams rllayoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        rllayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        titleTextView = new TextView(getContext());
        titleTextView.setTextColor(itemtextcolor);
        titleTextView.setTextSize(14);
        titleTextView.setLayoutParams(rllayoutParams);
        relativeLayout.addView(titleTextView);

        RelativeLayout.LayoutParams rllayoutParams1 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        rllayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        timeTextView = new TextView(getContext());
        timeTextView.setLayoutParams(rllayoutParams1);
        relativeLayout.addView(timeTextView);

        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.close_bill_lin_selector);
        linearLayout.setLayoutParams(layoutParams);
        addView(linearLayout);

    }


    public void setItemtextcolor(int itemtextcolor) {
        this.itemtextcolor = itemtextcolor;
    }


    public void setLittle_text_size(int little_text_size) {
        this.little_text_size = little_text_size;
    }


    public void setNomal_text_size(int nomal_text_size) {
        this.nomal_text_size = nomal_text_size;
    }


    public void setBig_text_size(int big_text_size) {
        this.big_text_size = big_text_size;
    }


    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setTime(String time) {
        timeTextView.setText(time);

    }

    public void setTime(SpannableStringBuilder time) {        timeTextView.setText(time);

    }


    public void addItemNormalView(String start, String middle, String end, boolean isaddlineView) {
        linearLayout.addView(getNormalitemview(start, middle, end));
        if (isaddlineView) {
            linearLayout.addView(getDividerView());
        }
    }


    public void addItemTotallView(String start, String middle, String end, boolean isaddlineView) {
        linearLayout.addView(getTotalitemview(start, middle, end));
        if (isaddlineView) {
            linearLayout.addView(getDividerView());
        }
    }


    private View getTotalitemview(String text0, String text1, String text2) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 56));
        layoutParams.setMargins(dip2px(getContext(), 24), 0, dip2px(getContext(), 24), 0);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(layoutParams);

        for (int i = 0; i < 3; i++) {
            RelativeLayout.LayoutParams layoutParams1 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(getContext());
                        textView.setTextSize(nomal_text_size);
            textView.setTextColor(itemtextcolor);
            switch (i) {
                case 0:
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                    if (text0 == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText(text0);
                    }

                    textView.setLayoutParams(layoutParams1);
                    relativeLayout.addView(textView);
                    break;
                case 1:

                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    if (text1 == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText(text1);
                    }

                    textView.setLayoutParams(layoutParams1);
                    relativeLayout.addView(textView);
                    break;
                case 2:
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setLayoutParams(layoutParams1);
                    TextView textView2 = new TextView(getContext());
                                        textView2.setTextSize(nomal_text_size);
                    textView2.setTextColor(itemtextcolor);
                                        textView.setTextSize(big_text_size);
                    if (text2 == null) {
                        textView2.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                    } else {
                        textView2.setText(getContext().getResources().getString(R.string.total_money));
                        textView.setText(text2);
                    }
                    layout.addView(textView2, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    layout.addView(textView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                    relativeLayout.addView(layout);
                    break;

                default:
                    break;
            }

        }

        return relativeLayout;

    }


    private View getNormalitemview(String text0, String text1, String text2) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 56));
        layoutParams.setMargins(dip2px(getContext(), 24), 0, dip2px(getContext(), 24), 0);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(layoutParams);

        for (int i = 0; i < 3; i++) {
            RelativeLayout.LayoutParams layoutParams1 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(getContext());
                        textView.setTextSize(nomal_text_size);
            textView.setTextColor(goodsitemcolor);
            switch (i) {
                case 0:
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                    if (text0 == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText(text0);
                    }

                    textView.setLayoutParams(layoutParams1);
                    relativeLayout.addView(textView);
                    break;
                case 1:

                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    if (text1 == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText(text1);
                    }

                    textView.setLayoutParams(layoutParams1);
                    relativeLayout.addView(textView);
                    break;
                case 2:
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                    layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    if (text2 == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setText(text2);
                    }
                    textView.setLayoutParams(layoutParams1);
                    relativeLayout.addView(textView);
                    break;

                default:
                    break;
            }

        }

        return relativeLayout;

    }


    public void addItemTitleGoodSView(String name, String count, String unitprice, String totalprice,
                                      boolean isaddlineView) {

        linearLayout.addView(getgoodsView(name,
                count,
                unitprice,
                totalprice,
                sp2px(getContext(), 14),
                goodsitemtitlecolor));
        if (isaddlineView) {
            linearLayout.addView(getNoMarginsDividerView());
        }

    }


    public void addItemGoodSView(String name, String count, String unitprice, String totalprice, boolean isaddlineView) {

        linearLayout.addView(getgoodsView(name, count, unitprice, totalprice, sp2px(getContext(), 16), goodsitemcolor));
        if (isaddlineView) {
            linearLayout.addView(getDividerView());
        }

    }

    private View getgoodsView(String name, String count, String unitprice, String totalprice, int textsize,
                              int textcolor) {

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 44));

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(dip2px(getContext(), 24), 0, dip2px(getContext(), 24), 0);

        linearLayout.setLayoutParams(layoutParams);

        for (int i = 0; i < 4; i++) {
            LayoutParams layoutParams1 = null;
            TextView textView = new TextView(getContext());
            textView.setTextSize(textsize);
            textView.setTextColor(textcolor);

            switch (i) {
                case 0:
                    if (name == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        layoutParams1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3);
                        layoutParams1.gravity = Gravity.CENTER;
                        textView.setSingleLine(false);
                        textView.setGravity(Gravity.LEFT);
                        textView.setText(name);
                    }

                    break;
                case 1:

                    if (count == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        layoutParams1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
                        layoutParams1.gravity = Gravity.CENTER;
                        textView.setGravity(Gravity.CENTER);
                        textView.setText(count);
                    }

                    break;
                case 2:
                    if (unitprice == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        layoutParams1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
                        layoutParams1.gravity = Gravity.CENTER;
                        textView.setGravity(Gravity.CENTER);
                        textView.setText(unitprice);
                    }

                    break;
                case 3:
                    if (totalprice == null) {
                        textView.setVisibility(View.GONE);
                    } else {
                        layoutParams1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
                        layoutParams1.gravity = Gravity.CENTER;
                        textView.setGravity(Gravity.RIGHT);
                        textView.setText(totalprice);
                    }

                    break;

                default:
                    break;
            }
            textView.setLayoutParams(layoutParams1);
            linearLayout.addView(textView);

        }
        return linearLayout;

    }


    private View getDividerView() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 1));
        layoutParams.setMargins(dip2px(getContext(), 24), 0, dip2px(getContext(), 24), 0);
        View view = new View(getContext());
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.parseColor("#dfe4e9"));
        return view;
    }


    private View getNoMarginsDividerView() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(), 1));

        View view = new View(getContext());
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.parseColor("#dfe4e9"));
        return view;
    }

    public void setAdapter() {

    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void removeAllItemView() {
        titleTextView.setText("");
        timeTextView.setText("");
        linearLayout.removeAllViews();
    }

}
