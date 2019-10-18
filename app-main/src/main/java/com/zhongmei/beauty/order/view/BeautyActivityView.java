package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.beauty.order.view.BeautyProgramView;
import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.beauty.order.util.IChangeMiddlePageListener;
import com.zhongmei.yunfu.util.ToastUtil;



@EViewGroup(R.layout.beauty_activity_view)
public class BeautyActivityView extends LinearLayout {

    @ViewById(R.id.tv_program)
    TextView tv_program;

    @ViewById(R.id.tv_market_activity)
    TextView tv_market_activity;


    @ViewById(R.id.layout_content)
    LinearLayout layout_content;

    private Context mContext;
    private BeautyProgramView beautyProgramView;
    private BeautyMarketActivityView beautyMarketActivityView;
    private ChangePageListener mChangeListener;
    private IChangeMiddlePageListener mChangeMiddlePageListener;

    public BeautyActivityView(Context context, ChangePageListener changeListener, IChangeMiddlePageListener changeMiddlePageListener) {
        super(context);
        mContext = context;
        mChangeListener = changeListener;
        mChangeMiddlePageListener = changeMiddlePageListener;
    }

    @AfterViews
    void initView() {
        showProgramView();
    }


    @Click({R.id.tv_program, R.id.tv_market_activity})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_program:
                showProgramView();
                break;
            case R.id.tv_market_activity:
                showMarketActivityView();
                break;
        }
    }

    private void showProgramView() {
        if (CustomerManager.getInstance().getDinnerLoginCustomer() == null) {
            ToastUtil.showLongToast(R.string.beauty_customr_unlogin);
            return;
        }
        tv_program.setSelected(true);
        tv_market_activity.setSelected(false);
        layout_content.removeAllViews();
        beautyProgramView = new BeautyProgramView(mContext, mChangeListener, mChangeMiddlePageListener);
        layout_content.addView(beautyProgramView);
    }

    private void showMarketActivityView() {
        tv_program.setSelected(false);
        tv_market_activity.setSelected(true);
        layout_content.removeAllViews();
        beautyMarketActivityView = BeautyMarketActivityView_.build(mContext);
        layout_content.addView(beautyMarketActivityView);
    }


    public void cancelSelected() {
        if (beautyMarketActivityView != null)
            beautyMarketActivityView.cancelSelected();
    }

}
