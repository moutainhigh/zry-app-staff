package com.zhongmei.bty.dinner.util;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment;
import com.zhongmei.bty.basemodule.auth.application.DinnerGroupApplication;



public class GroupTradeItemPropertyUtil extends TradeItemPropertyUtil {

    @Override
    public void setPropertyButton(FragmentActivity activity, DishDataItem item, View parentView, DinnerDishMiddleFragment.IChangePageListener listener, ChangePageListener changePageListener) {
        super.setPropertyButton(activity, item, parentView, listener, changePageListener);
    }

    @Override
    protected void initData() {
        super.initData();
        controlSlideDishShow();
    }


    private void controlSlideDishShow() {
        if (isSlideDish()) {
            ViewUtil.setButtonEnabled(btnExtra, false);
            ViewUtil.setButtonEnabled(btnProperty, false);
        }
    }

    @Override
    protected boolean isCanModify() {
        return false;
    }

    @Override
    protected String getModifyDishPermissionCode() {
        return DinnerGroupApplication.PERMISSION_GROUP_MODIFY_DISH;
    }

    @Override
    protected boolean isNeedInventory() {
        return mCurrentMode != DinnerDishMiddleFragment.GROUP_SLIDE_MODE;
    }
}
