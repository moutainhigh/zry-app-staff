package com.zhongmei.bty.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.zhongmei.yunfu.R;


public class LableTextEdit extends EditText {
    private String text;
    private Context context;

    public LableTextEdit(Context context) {
        super(context);
    }

    public LableTextEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("Recycle")
    public LableTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LableEditText);
        text = ta.getString(R.styleable.LableEditText_labletext);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(context.getResources().getDimension(R.dimen.text_18));
        paint.setAntiAlias(true);
        paint.setColor(context.getResources().getColor(R.color.customer_add_text_gray));
        canvas.drawText(text, 20, getHeight() / 2 + 8, paint);
        super.onDraw(canvas);
    }
}