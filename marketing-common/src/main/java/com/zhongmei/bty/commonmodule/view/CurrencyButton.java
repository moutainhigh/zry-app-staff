package com.zhongmei.bty.commonmodule.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;

import com.zhongmei.yunfu.util.ResourceUtils;


public class CurrencyButton extends Button implements ICurrencyView {

    private Context context;
    private CharSequence sourceText;
    private CharSequence sourceHint;

    public CurrencyButton(Context context) {
        super(context);
        init(context);
    }

    public CurrencyButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CurrencyButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CurrencyButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        sourceText = getText();
        sourceHint = getHint();
        setText(sourceText);
        setHintFormat(sourceHint);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String newText = ResourceUtils.formatString(text);
        super.setText(newText, type);
    }

    public void setHintFormat(int resId) {
        setHint(context.getText(resId));
    }

    public void setHintFormat(CharSequence hint) {
        String newHint = ResourceUtils.formatString(hint);
        setHint(newHint);
    }
}
