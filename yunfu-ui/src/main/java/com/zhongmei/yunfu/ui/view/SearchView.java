package com.zhongmei.yunfu.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhongmei.yunfu.ui.R;



public class SearchView extends EditText implements
        View.OnFocusChangeListener, TextWatcher {

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    private float searchSize = 0;
    private float textSize = 0;
    private int textColor = 0xFF000000;
    private Drawable mDrawable;
    private Paint paint;

    private Drawable mClearDrawable;

    private boolean hasFoucs;
    private int gravity = 0;
    private float drawablePaddingLeft = 0;

    private String hint = "";
    private Button btnControl;
    private TextChangeCallback mCallback;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitResource(context, attrs);
        InitPaint();
    }

    private void InitResource(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.searchedit);
        float density = context.getResources().getDisplayMetrics().density;
        searchSize = mTypedArray.getDimension(R.styleable.searchedit_imagewidth, 18 * density + 0.5F);
        int defaultColor = getResources().getColor(R.color.search_hint_color);
        textColor = mTypedArray.getColor(R.styleable.searchedit_textEditColor, defaultColor);
        textSize = mTypedArray.getDimension(R.styleable.searchedit_textEditSize, 14 * density + 0.5F);
        hint = mTypedArray.getString(R.styleable.searchedit_textHint);
        gravity = mTypedArray.getInteger(R.styleable.searchedit_gravity, 0);
        drawablePaddingLeft = mTypedArray.getDimension(R.styleable.searchedit_drawablePaddingLeft, 0f);
        mTypedArray.recycle();
        init();
    }

    private void InitPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawSearchIcon(canvas);
    }

    private void DrawSearchIcon(Canvas canvas) {
        if (this.getText().toString().length() == 0) {
            float textWidth = paint.measureText(hint);
            float textHeight = getFontLeading(paint);

            float dx = 0;
            float dy = 0;
            if (gravity == LEFT) {
                dx = drawablePaddingLeft;
            } else {
                dx = (getWidth() - searchSize - textWidth - 8) / 2;
            }
            dy = (getHeight() - searchSize) / 2;
            canvas.save();
            canvas.translate(getScrollX() + dx, getScrollY() + dy);
            if (mDrawable != null) {
                mDrawable.draw(canvas);
            }
            canvas.drawText(hint, getScrollX() + searchSize + 8, getScrollY() + (getHeight() - (getHeight() - textHeight) / 2) - paint.getFontMetrics().bottom - dy, paint);
            canvas.restore();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawable == null) {
            try {
                mDrawable = getContext().getResources().getDrawable(R.drawable.group_search);
                mDrawable.setBounds(0, 0, (int) searchSize, (int) searchSize);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
            mDrawable = null;
        }
        super.onDetachedFromWindow();
    }

    public float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }


    private void init() {
                mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.delete);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        this.setPadding(0, 0, 30, 0);
                setClearIconVisible(false);
                setOnFocusChangeListener(this);
                addTextChangedListener(this);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }



    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
        if (btnControl != null) {
            if (visible) {
                btnControl.setEnabled(true);
                btnControl.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                btnControl.setEnabled(false);
                btnControl.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }


    public void setControlView(Button btnControl, TextChangeCallback callback) {
        this.btnControl = btnControl;
        mCallback = callback;
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void afterTextChanged(Editable s) {
        if (mCallback != null) {
            mCallback.afterTextChanged(s);
        }
    }

    public interface TextChangeCallback {
        public void afterTextChanged(Editable s);
    }
}
