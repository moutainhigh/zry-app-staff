package com.zhongmei.bty.basemodule.slidemenu.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.slidemenu.listener.GroupSlideMenuListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(resName = "slidemenu_group_left_menu")
public class GroupSlideMenuView extends SlideMenuView {

    private final static int PAGE_GROUP_LIST = 1;
    private final static int PAGE_ORDER_CENTER = 2;

    private int pageTag = PAGE_GROUP_LIST;

    @ViewById(resName = "group_center")
    protected RadioButton btnGroupCenter;

    @ViewById(resName = "ordercenter")
    protected RadioButton btnOrderCenter;

    public GroupSlideMenuView(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundResource(R.drawable.slidemenu_cashier_slide_menu_bg);
    }

    @Override
    public void onModelClick(int pageNo) {

    }

    @AfterViews
    void init() {
        btnGroupCenter.setChecked(true);
    }

    @Click(resName = {"group_center", "ordercenter", "tv_back_home"})
    void click(View v) {
        GroupSlideMenuListener groupSlideMenuListener = null;
        if (slideMenuListener != null && slideMenuListener instanceof GroupSlideMenuListener) {
            groupSlideMenuListener = (GroupSlideMenuListener) slideMenuListener;
        }

        if (v.getId() == R.id.group_center) {
            if (pageTag != PAGE_GROUP_LIST) {
                if (groupSlideMenuListener != null) {
                    groupSlideMenuListener.switchToGroupList();
                }

                pageTag = PAGE_GROUP_LIST;
            }
        } else if (v.getId() == R.id.ordercenter) {
            if (pageTag != PAGE_ORDER_CENTER) {
                if (groupSlideMenuListener != null) {
                    groupSlideMenuListener.switchToOrderCenter();
                }

                pageTag = PAGE_ORDER_CENTER;
            }
        } else if (v.getId() == R.id.tv_back_home) {
            if (groupSlideMenuListener != null) {
                groupSlideMenuListener.backHome();
            }
        }

    }

}
