package com.zhongmei.bty.settings.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.queue.QueueAutoTakeNumberActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 常用开关
 */
@EFragment(R.layout.settings_queue_switch)
public class QueueSettingSwitchFragment extends BasicFragment {
    private static final String TAG = QueueSettingSwitchFragment.class.getSimpleName();

    public static final String MOBILE_PRIVACY = "mobileprivacy";

    public static final String SCAN_SWITCH = "scanSwtich";

    public static final String QUEUE_IN_SWITCH = "queueInSwtich";

    @ViewById(R.id.scan_swtich)
    protected ToggleButton scanSwtich;

    @ViewById(R.id.mobile_privacy_swtich)
    protected ToggleButton mobilePrivacy;

    @ViewById(R.id.queue_come_in_open_switch)
    protected ToggleButton mQueueInSwitch;

    @AfterViews
    void init() {
        mobilePrivacy.setChecked(SpHelper.getDefault().getBoolean(MOBILE_PRIVACY, false));
        scanSwtich.setChecked(SpHelper.getDefault().getBoolean(SCAN_SWITCH, false));
        scanSwtich.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpHelper.getDefault().putBoolean(SCAN_SWITCH, isChecked);

            }
        });
        mobilePrivacy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpHelper.getDefault().putBoolean(MOBILE_PRIVACY, isChecked);

            }
        });

        mQueueInSwitch.setChecked(SpHelper.getDefault().getBoolean(QUEUE_IN_SWITCH, true));
        mQueueInSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpHelper.getDefault().putBoolean(QUEUE_IN_SWITCH, isChecked);

            }
        });
    }

    @Click(R.id.queue_self_swtich)
    void clic(View v) {
        switch (v.getId()) {
            case R.id.queue_self_swtich:
                /*
                 * 跳转到自动排队
                 */
                toAutoQueue();
                break;
            default:
                break;
        }
    }

    /*
     * 跳转到自动排队
     */
    private void toAutoQueue() {
        QueueAutoTakeNumberActivity_.intent(this).start();
        // AuthUserCache.onLogout();
        this.getActivity().finish();
    }

}
