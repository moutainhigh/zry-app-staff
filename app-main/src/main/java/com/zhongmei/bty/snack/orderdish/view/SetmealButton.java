package com.zhongmei.bty.snack.orderdish.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

/**
 * @Date：2015年7月9日 上午9:34:53
 * @Description: 套餐界面按钮
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class SetmealButton extends FrameLayout {
    private Context mContext;

    private RelativeLayout mainView;

    private TextView tvTitle;

    private TextView tvDescription;

    private ImageView ivTag;

    private String mTitle;

    private String mDescription;

    private Drawable mBackground;

    public SetmealButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SetmealButton);
        mTitle = a.getString(R.styleable.SetmealButton_title);
        mDescription = a.getString(R.styleable.SetmealButton_description);
        mBackground = a.getDrawable(R.styleable.SetmealButton_background);
        a.recycle();

        init(context);
    }

    public SetmealButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.setmeal_button_layout, null);

        mainView = (RelativeLayout) view.findViewById(R.id.main);
        mainView.setBackgroundDrawable(mBackground);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        setTitleText(mTitle);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        setDescriptionText(mDescription);
        ivTag = (ImageView) view.findViewById(R.id.iv_tag);

        addView(view);
        setClickable(true);
    }

    public RelativeLayout getMainView() {
        return mainView;
    }

    public void setMainBackground(Drawable background) {
        if (mainView != null) {
            mainView.setBackgroundDrawable(background);
        }
    }

    public void setTitleText(CharSequence title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void setTitleColor(int color) {
        if (tvTitle != null) {
            tvTitle.setTextColor(color);
        }
    }

    public void setDescriptionText(CharSequence description) {
        if (tvDescription != null) {
            tvDescription.setText(description);
        }
    }

    public void setDescriptionColor(int color) {
        if (tvDescription != null) {
            tvDescription.setTextColor(color);
        }
    }

    public void setTagVisibility(int visibility) {
        if (ivTag != null) {
            ivTag.setVisibility(visibility);
        }
    }
}
