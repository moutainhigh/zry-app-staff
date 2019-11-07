package com.zhongmei.beauty;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongmei.bty.commonmodule.event.ActionPrinterServerChanged;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.manager.CashierPointManager;
import com.zhongmei.bty.settings.fragment.BasicSettingFragment_;
import com.zhongmei.bty.settings.fragment.BookSettingSwitchFragment_;
import com.zhongmei.bty.settings.fragment.DinnerAutoOrderFragment_;
import com.zhongmei.bty.settings.fragment.DinnerMenuSettingFragment;
import com.zhongmei.bty.settings.fragment.GroupPaySettingFragment_;
import com.zhongmei.bty.settings.fragment.HandoverTypeSettingFragment_;
import com.zhongmei.bty.settings.fragment.OrderDisplayDeviceSettingFragment_;
import com.zhongmei.bty.settings.fragment.PayModelOrderSettingFragment_;
import com.zhongmei.bty.settings.fragment.PrepaySettingFragment_;
import com.zhongmei.bty.settings.fragment.ScannerFragment_;
import com.zhongmei.bty.settings.fragment.ShopInfoFragment_;
import com.zhongmei.bty.settings.fragment.SupportFragment;
import com.zhongmei.bty.settings.fragment.SupportFragment_;
import com.zhongmei.bty.settings.fragment.TableNumberFragment_;
import com.zhongmei.bty.settings.fragment.TimingPrintFragment_;
import com.zhongmei.bty.settings.fragment.UpdateFragment_;
import com.zhongmei.bty.settings.fragment.WeiXinOrderControlFragment_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.data.VersionInfo;
import com.zhongmei.yunfu.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.beauty_setting_layout)
public class BeautySettingActivity extends BaseActivity {

    private static final String TAG = BeautySettingActivity.class.getSimpleName();

    @ViewById(R.id.setting_use_sence)
    TextView setting_useSence;

    @ViewById(R.id.setting_baseInfo)
    TextView setting_baseInfo;

    @ViewById(R.id.setting_tables)
    TextView setting_tables;

    private TextView textView;

    FragmentManager mFragmentManager;

    @AfterViews
    public void init() {
        mFragmentManager = getSupportFragmentManager();
    }

    @UiThread
    @Click(R.id.setting_use_sence)
    public void onUseSenceClick(){
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, setting_useSence);
            setCurrentStyle(setting_useSence);
            switchFragment(R.id.right_container1, new BeautySenceFragment_(), setting_useSence);
            textView = setting_useSence;
        }
    }

    @UiThread
    @Click(R.id.setting_baseInfo)
    public void onBaseInfoClick(){
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, setting_baseInfo);
            setCurrentStyle(setting_baseInfo);
            switchFragment(R.id.right_container1, new SystemInfoFragment(), setting_baseInfo);
            textView = setting_baseInfo;
        }
    }

    @UiThread
    @Click(R.id.setting_tables)
    public void onTablesClick(){
        if (!ClickManager.getInstance().isClicked()) {
            changeLastStyle(textView, setting_tables);
            setCurrentStyle(setting_tables);
            switchFragment(R.id.right_container1, new BeautySettingTableBindFragment_(), setting_tables);
            textView = setting_tables;
        }
    }

    private void changeLastStyle(TextView view1, TextView view2) {
        if (view1 != null && view1 != view2) {
            Resources res = getResources();
            view1.setBackgroundColor(res.getColor(R.color.settings_normalback));
            view1.setTextColor(res.getColor(R.color.settings_normalword));
        }
    }

    private void setCurrentStyle(TextView view) {
        if (textView != view) {
            Resources res = getResources();
            view.setBackgroundColor(res.getColor(R.color.settings_biueword));
            view.setTextColor(res.getColor(R.color.color_32ADF6));
        }
    }

    //点击切换fragemnt
    private void switchFragment(int containerId, Fragment fragment, TextView view) {
        if (textView != view) {
            try {
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(containerId, fragment);
                mFragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }

    //适用于给Fragment提供参数的切换
    private void switchFragment(int containerId, Fragment fragment, TextView view, Bundle bundle) {
        fragment.setArguments(bundle);
        switchFragment(containerId, fragment, view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void onEventMainThread(ActionPrinterServerChanged event) {

    }

    @Override
    protected void onDestroy() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.right_container1);
        if (fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
        super.onDestroy();
    }

}
