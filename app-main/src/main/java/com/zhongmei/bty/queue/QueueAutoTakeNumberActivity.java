package com.zhongmei.bty.queue;

import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.queue.ui.QueueAutoTakeNumberRightFragment;
import com.zhongmei.bty.queue.ui.QueueAutoTakeNumberRightFragment_;
import com.zhongmei.yunfu.LoginActivity;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;

/**
 * 自动取号
 */
@EActivity(R.layout.queue_auto_takenumber)
public class QueueAutoTakeNumberActivity extends MainBaseActivity {

    private static final String TAG = QueueAutoTakeNumberActivity.class.getSimpleName();

    private static final String HIDE_NAVIBAR = "hide_navigation_bar";
    public static final String HIDE_STATUSBAR = "hide_statusbar";

    @ViewById(R.id.id_imageview_logo)
    ImageView mImageviewLogo;

    @ViewById(R.id.queue_self_view)
    LinearLayout queueBackGroundView;


    @AfterViews
    protected void initViews() {
        QueueAutoTakeNumberRightFragment rightFragment = new QueueAutoTakeNumberRightFragment_();
        replaceFragment(R.id.right_panel, rightFragment, QueueAutoTakeNumberRightFragment.class.getSimpleName());
    }

    @LongClick(R.id.id_imageview_logo)
    protected void logout() {
        try {
            Thread.sleep(2000);
            Session.unbind();
            Intent intent = new Intent(QueueAutoTakeNumberActivity.this, LoginActivity.class);
            intent.putExtra("activityName", "com.zhongmei.bty.cashier.MainActivity");
            startActivity(intent);

//			Bundle bundle=new Bundle();
//			bundle.putString("activityName", PathURI.URI_SNACK);
//			RouteIntent.startLogin(QueueAutoTakeNumberActivity.this,Intent.FLAG_ACTIVITY_NEW_TASK,bundle);

            DisplayServiceManager.doUpdateQueue(getApplicationContext(), true);
            this.finish();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //设置导航栏隐藏
        int navibar = Settings.System.getInt(getContentResolver(), HIDE_NAVIBAR, -1);
        int statusbar = Settings.System.getInt(getContentResolver(), HIDE_STATUSBAR, -1);
        if (navibar >= 0) {
            Settings.System.putInt(getContentResolver(), HIDE_NAVIBAR, 1);
        }
        if (statusbar >= 0) {
            Settings.System.putInt(getContentResolver(), HIDE_STATUSBAR, 1);
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        //设置导航栏显示
        int navibar = Settings.System.getInt(getContentResolver(), HIDE_NAVIBAR, -1);
        int statusbar = Settings.System.getInt(getContentResolver(), HIDE_STATUSBAR, -1);
        if (navibar >= 0) {
            Settings.System.putInt(getContentResolver(), HIDE_NAVIBAR, 0);
        }
        if (statusbar >= 0) {
            Settings.System.putInt(getContentResolver(), HIDE_STATUSBAR, 0);
        }
        super.onDestroy();
    }
}
