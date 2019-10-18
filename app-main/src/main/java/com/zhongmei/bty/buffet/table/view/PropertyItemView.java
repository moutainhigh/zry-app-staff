package com.zhongmei.bty.buffet.table.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;



@EViewGroup(R.layout.viewgroup_property_item)
public class PropertyItemView extends LinearLayout {

    @ViewById(R.id.tv_title)
    protected TextView tv_title;

    @ViewById(R.id.tv_property)
    protected TextView tv_property;
    @ViewById(R.id.iv_icon)
    protected ImageView iv_icon;
    @ViewById(R.id.view_line)
    protected View view_horizontal_line;

    private String titleText = "";
    private int devideVisiable = View.VISIBLE;
    private String propertyText = "";
    private int iconVisiable = View.VISIBLE;
    private int propertyVisiable = View.GONE;


    @AfterViews
    void init() {
        setTitleText(titleText);
        setPropertyText(propertyText);
        setDivideLineVisiable(devideVisiable);
        setIconVisiable(iconVisiable);
        setPropertyVisiable(propertyVisiable);
    }


    public PropertyItemView(Context context) {
        this(context, null);
    }

    public PropertyItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PropertyItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
    }

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.PropertyItemView);

        if (attr.getIndexCount() > 0) {
            titleText = attr.getString(R.styleable.PropertyItemView_titleText);
            devideVisiable = attr.getInt(R.styleable.PropertyItemView_devideLineVisiable, View.VISIBLE);
            propertyText = attr.getString(R.styleable.PropertyItemView_propertyText);
            propertyVisiable = attr.getInt(R.styleable.PropertyItemView_propertyVisiable, View.GONE);
            iconVisiable = attr.getInt(R.styleable.PropertyItemView_iconVisiable, View.VISIBLE);
        }
        attr.recycle();

    }

    public void setDivideLineVisiable(int visiable) {
        if (view_horizontal_line != null) {
            view_horizontal_line.setVisibility(visiable);
        }
    }

    public void setTitleText(String text) {
        if (tv_title != null && !TextUtils.isEmpty(text)) {
            tv_title.setText(text);
        }
    }

    public void setPropertyVisiable(int visiable) {
        if (tv_property != null) {
            tv_property.setVisibility(visiable);
        }
    }

    public void setIconVisiable(int visiable) {
        if (iv_icon != null) {
            iv_icon.setVisibility(visiable);
        }
    }

    public void setPropertyText(String text) {
        if (tv_property != null && !TextUtils.isEmpty(text)) {
            tv_property.setText(text);
        }
    }

    public String getPropertyText() {
        if (tv_property != null) {
            return tv_property.getText().toString();
        }
        return null;
    }


    public void setTitleText(int res) {
        String text = getResources().getString(res);
        setTitleText(text);
    }


    public void setPropertyText(int res) {
        String text = getResources().getString(res);
        setPropertyText(text);
    }
}
