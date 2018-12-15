package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.beauty.entity.SchedulingVo;
import com.zhongmei.beauty.widgets.SchedulingView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.beauty_lv_item_scheduling)
public class SchedulingItemView extends RelativeLayout {

    @ViewById(R.id.sv_monday)
    protected SchedulingView sv_monday;

    private SchedulingVo mSchedulingVo;

    public SchedulingItemView(Context context) {
        super(context);
    }

    public SchedulingItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SchedulingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    public void init() {


    }


    public void refreshData(SchedulingVo schedulingVo) {
        this.mSchedulingVo = schedulingVo;
        setData();
    }


    private void setData() {
        sv_monday.setScheduling("上午班次");
        sv_monday.setTime("14:22-18:30");
    }
}
