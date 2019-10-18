package com.zhongmei.bty.settings.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

public class NumberKeyboardLayout extends LinearLayout implements View.OnClickListener {

    private NumberClickListener listener;

    public interface NumberClickListener {
        void numberClicked(String number);
    }

    public void setListener(NumberClickListener listener) {
        this.listener = listener;
    }


    public NumberKeyboardLayout(Context context) {
        super(context);
    }

    public NumberKeyboardLayout(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public NumberKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.number_keyboard_layout, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);
        findViewById(R.id.zero).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
        ImageView dotView = (ImageView) findViewById(R.id.point);
        dotView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener == null)
            return;
        switch (v.getId()) {
            case R.id.one:
                listener.numberClicked("1");
                break;
            case R.id.two:
                listener.numberClicked("2");
                break;
            case R.id.three:
                listener.numberClicked("3");
                break;
            case R.id.four:
                listener.numberClicked("4");
                break;
            case R.id.five:
                listener.numberClicked("5");
                break;
            case R.id.six:
                listener.numberClicked("6");
                break;
            case R.id.seven:
                listener.numberClicked("7");
                break;
            case R.id.eight:
                listener.numberClicked("8");
                break;
            case R.id.nine:
                listener.numberClicked("9");
                break;
            case R.id.zero:
                listener.numberClicked("0");
                break;
            case R.id.delete:
                listener.numberClicked("d");
                break;
            case R.id.point:
                listener.numberClicked(".");
                break;
        }
    }
}
