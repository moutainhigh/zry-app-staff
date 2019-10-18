package com.zhongmei.yunfu;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.base.ActivityLifecycle;
import com.zhongmei.yunfu.ui.base.BaseActivity;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.context.util.FileUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.init.sync.SyncServiceUtil;

import java.io.File;


public class ResetFragment {

    private FragmentActivity mContext;
    private CommonDialogFragment mDialogFragment;

        public static final int OTHER = 1;

        public static final int LAUNCHER = 2;

        public static final int VERSION = 3;

    public ResetFragment(FragmentActivity context, int type) {
        mContext = context;
        CommonDialogFragment.CommonDialogFragmentBuilder builder = null;
        if (type == OTHER) {
            builder = new CommonDialogFragment.CommonDialogFragmentBuilder(context)
                    .title(mContext.getString(R.string.setting_reset_tips))
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .positiveLinstner(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reset();
                        }
                    });
        } else if (type == LAUNCHER) {
            builder = new CommonDialogFragment.CommonDialogFragmentBuilder(context)
                    .title(mContext.getString(R.string.lancher_reset_tips))
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(android.R.string.ok)
                    .positiveLinstner(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reset();
                        }
                    });
        } else if (type == VERSION) {
            builder = new CommonDialogFragment.CommonDialogFragmentBuilder(context);
            builder.iconType(CommonDialogFragment.ICON_HINT)
                    .title(mContext.getResources().getString(R.string.dataError))
                    .positiveText(R.string.calm_logout_yes)
                    .positiveLinstner(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reset();
                        }
                    });
        }

        mDialogFragment = builder.build();
        mDialogFragment.setCancelable(false);
        if (type == LAUNCHER) {
            mDialogFragment.setCancelWithHomeKey(false);
        }

    }

    public static ResetFragment show(FragmentActivity context, int type) {
        ResetFragment resetFragment = new ResetFragment(context, type);
        resetFragment.show();
        return resetFragment;
    }

    public void show() {
        mDialogFragment.show(mContext.getSupportFragmentManager(), "ResetFragment");
    }

    public void dismiss() {
        mDialogFragment.dismissAllowingStateLoss();
    }

    private void reset() {

        dismiss();
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingProgressDialog(mContext.getString(R.string.onekey_init_loading));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemUtils.exitPrintServer();
                SystemUtils.deleteRootDir();
                SystemUtils.deleteAllinpayDir();
                DatabaseHelper helper = DBHelperManager.getHelper();

                                File file = new File(FileUtil.getDBPath());
                SystemUtils.deleteAllFiles(file);

                SharedPreferenceUtil.getSpUtil().clear();
                DBHelperManager.releaseHelper(helper);
                SyncServiceUtil.setIsNeedInit(true);


                SyncServiceUtil.stopService(mContext);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                                                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).dismissLoadingProgressDialog();
                }
                startApp();
            }
        }).start();
    }

    private void startApp() {
        MainApplication.getInstance().finishAllActivity(new ActivityLifecycle.AppExitCallback() {
            @Override
            public void onAppExit() {
                MainApplication.getInstance().sendBroadcast(new Intent(BootReceiver.ACTION_APP_RESTART));
                System.exit(0);
            }
        });
    }
}
