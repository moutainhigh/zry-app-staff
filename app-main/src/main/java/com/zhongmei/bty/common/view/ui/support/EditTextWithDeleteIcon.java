package com.zhongmei.bty.common.view.ui.support;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.zhongmei.yunfu.context.util.ArgsUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.math.BigDecimal;


public class EditTextWithDeleteIcon extends EditText {

    private static final String TAG = EditTextWithDeleteIcon.class.getSimpleName();

    private Drawable dRight;
    private Rect rBounds;
    private boolean flag;

    private com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon.OnClearListener onClearListener;

    public EditTextWithDeleteIcon(Context paramContext) {
        super(paramContext);
        initEditText();
    }

    public EditTextWithDeleteIcon(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initEditText();
    }

    public EditTextWithDeleteIcon(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        initEditText();
    }

    public void setOnClearListener(com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon.OnClearListener onClearListener) {
        this.onClearListener = onClearListener;
    }

        private void initEditText() {
        setEditTextDrawable();
        addTextChangedListener(new TextWatcher() {             @Override
            public void afterTextChanged(Editable paramEditable) {
            }

            @Override
            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }

            @Override
            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
                EditTextWithDeleteIcon.this.setEditTextDrawable();
            }
        });
        this.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setEditTextDrawable();
                if (!hasFocus) {
                    String text = getText().toString();
                    if (text != null) {
                        text = text.replace(ShopInfoCfg.getInstance().getCurrencySymbol(), "0");
                        text.replaceAll(" ", "");
                    }
                    try {
                        double result = Double.parseDouble(text);
                        setText(ShopInfoCfg.formatCurrencySymbol(
                                ArgsUtils.returnIfEmpty(MathDecimal.toDecimalFormatString(BigDecimal.valueOf(result)), "0.00")));
                    } catch (NumberFormatException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                } else {
                }
            }
        });
    }

    public void setEditTextDrawable() {
        if (dRight != null) {
            return;
        }
        Drawable[] compoundDrawables = getCompoundDrawables();
        if (compoundDrawables != null) {
            dRight = compoundDrawables[2];
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dRight = null;
        this.rBounds = null;

    }


    @Override
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if ((this.dRight != null) && (paramMotionEvent.getAction() == 1) && isFocused()) {
            this.rBounds = this.dRight.getBounds();
            int i = (int) paramMotionEvent.getX();            if (i > getWidth() - this.rBounds.width()) {
                setText(ShopInfoCfg.getInstance().getCurrencySymbol());
                if (onClearListener != null) {
                    onClearListener.AfterTextClear();
                }
                Log.e("--i----", "" + i);
                Log.e("--igetWidth---", "" + this.getWidth());
                Log.e("--width----", "" + rBounds.width());
                Log.e("--height-----", "" + rBounds.height());
                paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(paramMotionEvent);
    }


    @Override
    public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
        if (paramDrawable3 != null)
            this.dRight = paramDrawable3;
        super.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
    }

}
