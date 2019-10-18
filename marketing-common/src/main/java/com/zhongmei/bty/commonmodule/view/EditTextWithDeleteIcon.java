package com.zhongmei.bty.commonmodule.view;

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

public class EditTextWithDeleteIcon extends EditText {
    private Drawable dRight;
    private Rect rBounds;
    private boolean flag;

    private OnClearListener onClearListener;

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

    public void setOnClearListener(OnClearListener onClearListener) {
        this.onClearListener = onClearListener;
    }

        private void initEditText() {
        setEditTextDrawable();
        addTextChangedListener(new TextWatcher() {             @Override
            public void afterTextChanged(Editable paramEditable) {
                EditTextWithDeleteIcon.this.setEditTextDrawable();
            }

            @Override
            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }

            @Override
            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }
        });
        this.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setEditTextDrawable();
            }
        });
    }

        public void setEditTextDrawable() {
        if (getText().toString().length() == 0 || !hasFocus()) {
            setCompoundDrawables(null, null, null, null);
            flag = false;
        } else {
            setCompoundDrawables(null, null, this.dRight, null);
            flag = true;
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
        if ((this.dRight != null) && (paramMotionEvent.getAction() == 1) && flag) {
            this.rBounds = this.dRight.getBounds();
            int i = (int) paramMotionEvent.getX();            if (i > getWidth() - this.rBounds.width() - getPaddingRight() - getPaddingLeft()) {
                setText("");
                if (onClearListener != null) {
                    onClearListener.AfterTextClear();
                }
                Log.e("--(int) paramMotionEvent.getX()----", "" + (int) paramMotionEvent.getX());
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

    public interface OnClearListener {
                public void AfterTextClear();
    }

}
