package com.zhongmei.bty.settings.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.yunfu.context.util.helper.SpHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.setting_group_pay_set_fragment_layout)
public class GroupPaySettingFragment extends Fragment {

    @ViewById(R.id.btn_set_group_bay)
    Button allowGroupPayBT;

    private boolean isAllow = true;

    @AfterViews
    void init() {
        isAllow = SpHelper.getDefault().getBoolean(Constant.SUPPORT_GROUP_PAY, true);
        allowGroupPayBT.setSelected(isAllow);
    }

    @Click({R.id.btn_set_group_bay})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_set_group_bay:
                doSetting();
                break;
        }
    }

    private void doSetting() {
        isAllow = !isAllow;
        SpHelper.getDefault().putBoolean(Constant.SUPPORT_GROUP_PAY, isAllow);
        PaySettingCache.setSupportGroupPay(isAllow);
        allowGroupPayBT.setSelected(isAllow);
    }
}
