package com.zhongmei.beauty.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;



public class XRadioGroup extends LinearLayout {
        private int mCheckedId = -1;
        private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
        private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public XRadioGroup(Context context) {
        super(context);
        init();
    }

    public XRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }


    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
                mPassThroughListener.mOnHierarchyChangeListener = listener;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

                if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    private void setViewState(View child) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        } else if (child instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) child;
            for (int i = 0; i < view.getChildCount(); i++) {
                setViewState(view.getChildAt(i));
            }
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        setViewState(child);
        super.addView(child, index, params);
    }


    public void check(int id) {
                if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }


    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }


    public void clearCheck() {
        check(-1);
    }


    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new XRadioGroup.LayoutParams(getContext(), attrs);
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof XRadioGroup.LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return XRadioGroup.class.getName();
    }


    public static class LayoutParams extends LinearLayout.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }


        public LayoutParams(int w, int h) {
            super(w, h);
        }


        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }


        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }


        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }


        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }


    public interface OnCheckedChangeListener {

        public void onCheckedChanged(XRadioGroup group, int checkedId);
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.e("CheckChangeListener","buttonViewId:"+buttonView.getId()+",isChecked:"+isChecked);
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }


    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;


        public void onChildViewAdded(View parent, View child) {
            setListener(child);

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }


        public void onChildViewRemoved(View parent, View child) {
            removeListener(child);

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }


    private void setListener(View child) {
        if (child instanceof RadioButton) {
            int id = child.getId();
                        if (id == View.NO_ID) {
                id = child.hashCode();
                child.setId(id);
            }
            ((RadioButton) child).setOnCheckedChangeListener(
                    mChildOnCheckedChangeListener);
        } else if (child instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) child;
            for (int i = 0; i < view.getChildCount(); i++) {
                setListener(view.getChildAt(i));
            }
        }
    }



    private void removeListener(View child) {
        if (child instanceof RadioButton) {
            ((RadioButton) child).setOnCheckedChangeListener(null);
        } else if (child instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) child;
            for (int i = 0; i < view.getChildCount(); i++) {
                removeListener(view.getChildAt(i));
            }
        }
    }
}
