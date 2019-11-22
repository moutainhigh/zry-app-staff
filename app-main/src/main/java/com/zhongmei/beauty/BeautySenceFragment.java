package com.zhongmei.beauty;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.commonmodule.database.entity.PrinterDevice;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_scene)
public class BeautySenceFragment extends Fragment {
    private static final String TAG = BeautySenceFragment.class.getSimpleName();

    @ViewById(R.id.cb_customer)
    public CheckBox cb_customer;

    @ViewById(R.id.cb_mixed)
    public CheckBox cb_mixed;

    @AfterViews
    public void init(){
        int curMode=SharedPreferenceUtil.getSpUtil().getInt(Constant.SP_USE_SENCE_MODE,1);
        cb_customer.setChecked(curMode==2);
        cb_mixed.setChecked(curMode==1);
    }



    @Click({R.id.rl_customer,R.id.rl_mixed})
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.rl_customer:
                if(!cb_customer.isChecked()){
                    SharedPreferenceUtil.getSpUtil().putInt(Constant.SP_USE_SENCE_MODE,2);
                    cb_customer.setChecked(true);
                    cb_mixed.setChecked(false);
                    ToastUtil.showShortToast("已切换至顾客模式！");
                }
                break;
            case R.id.rl_mixed:
                if(!cb_mixed.isChecked()){
                    SharedPreferenceUtil.getSpUtil().putInt(Constant.SP_USE_SENCE_MODE,1);
                    cb_customer.setChecked(false);
                    cb_mixed.setChecked(true);
                    ToastUtil.showShortToast("已切换至服务员模式！");
                }
                break;
        }
    }

}
