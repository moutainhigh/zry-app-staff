package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.booking.ui.DrawableCenterTextView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomChangePriceView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */


@EViewGroup(R.layout.dinner_custom_changeprice)
public class DinnerCustomChangePriceView extends CustomChangePriceView {

    @ViewById(R.id.tv_modify_price)
    DrawableCenterTextView modifyPrice;
    private FragmentActivity fragmentActivity;

    public DinnerCustomChangePriceView(Context context) {
        super(context);
    }

    public DinnerCustomChangePriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFramentActivity(FragmentActivity activity) {
        fragmentActivity = activity;
    }

    @Click({R.id.tv_modify_price})
    void onClick(View view) {
        VerifyHelper.verifyAlert(fragmentActivity, DinnerApplication.PERMISSION_DINNER_MODIFY_PRICE,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        modifyPrice.setVisibility(View.GONE);
                    }
                });
    }
}
