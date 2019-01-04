package com.zhongmei.bty;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.zhongmei.yunfu.ui.base.ILoginController;
import com.zhongmei.bty.basemodule.commonbusiness.dialog.PushInformatizationFragment;
import com.zhongmei.bty.basemodule.erp.bean.ErpMessagePushDetail;
import com.zhongmei.bty.common.view.ExitLoginRemindDialog;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragmentExt;

/**
 * Created by demo on 2018/12/15
 */

public class AppDialog {

    private BaseApplication application;

    public AppDialog(BaseApplication application) {
        this.application = application;
    }

    protected FragmentActivity getActivity() {
        final Activity activity = application.getActivityLifecycle().getCurrentActivity();
        if (activity != null && activity instanceof FragmentActivity) {
            return (FragmentActivity) activity;
        }
        return null;
    }

    public static void showExitLoginRemindDialog() {
        final FragmentActivity activity = BaseApplication.sInstance.getActivityLifecycle().getCurrentFragmentActivity();
        if (activity != null) {
            ExitLoginRemindDialog.show(activity);
        }
    }

    public static void showHintDialog(String message) {
        final FragmentActivity activity = BaseApplication.sInstance.getActivityLifecycle().getCurrentFragmentActivity();
        if (activity != null) {
            DialogUtil.showHintConfirmDialog(activity.getSupportFragmentManager(), message,
                    R.string.know, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }, true, "showHintDialog");
        }
    }

    /**
     * 默认提示信息
     *
     * @param message
     */
    /*public void showHintDialog(String message) {
        final Activity activity = MainApplication.getInstance().getActivityLifecycle().getCurrentActivity();
        if (activity != null && activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            DialogUtil.showHintConfirmDialog(fragmentActivity.getSupportFragmentManager(), message,
                    R.string.know, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }, true, "showHintDialog");
        }
    }*/

    /**
     * erp推送消息提示
     *
     * @param erpMessagePushDetail
     */
    public static void showErpInfoDialog(final ErpMessagePushDetail erpMessagePushDetail) {
        FragmentActivity activity = BaseApplication.sInstance.getActivityLifecycle().getCurrentFragmentActivity();
        if (activity != null) {
            PushInformatizationFragment.show(activity, erpMessagePushDetail.getTitle(), erpMessagePushDetail.getDetail(),
                    new PushInformatizationFragment.OnDialogListener() {
                        @Override
                        public void onClose() {

                        }

                        @Override
                        public void onKnow() {

                        }
                    });
        }
    }

    /**
     * 合同逾期提示
     */
    public static void showContractOverdueDialog() {
        FragmentActivity activity = BaseApplication.sInstance.getActivityLifecycle().getCurrentFragmentActivity();
        if (activity != null && !ILoginController.class.isAssignableFrom(activity.getClass())) {
            CommonDialogFragmentExt.CommonDialogFragmentExtBuilder builder = new CommonDialogFragmentExt.CommonDialogFragmentExtBuilder(BaseApplication.sInstance)
                    .title(activity.getString(R.string.erp_check_error_expired))
                    //.message(String.format(getString(R.string.login_check_error_exit_message), error))
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .positiveText(R.string.login_quiet)
                    .positiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseApplication.sInstance.finishAllActivity(null);
                            //System.exit(0);
                        }
                    });

            CommonDialogFragmentExt mDialogFragment = builder.build();
            mDialogFragment.setCancelable(false);
            mDialogFragment.setCancelWithHomeKey(false);
            mDialogFragment.show(activity.getSupportFragmentManager(), "");
        }
    }
}
