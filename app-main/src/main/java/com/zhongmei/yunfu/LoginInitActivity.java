package com.zhongmei.yunfu;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhongmei.OSLog;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.sync.AbsInitCheck;
import com.zhongmei.yunfu.sync.CheckManager;
import com.zhongmei.yunfu.sync.InitCheck;
import com.zhongmei.yunfu.sync.InitCheckCallback;
import com.zhongmei.yunfu.ui.base.BaseActivity;
import com.zhongmei.yunfu.ui.base.ILoginController;
import com.zhongmei.yunfu.ui.view.AutoVerticalScrollTextView;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.yunfu.ui.view.CommonDialogFragmentExt;
import com.zhongmei.yunfu.ui.view.CommonDialogFragmentExt.CommonDialogFragmentExtBuilder;
import com.zhongmei.yunfu.ui.view.UpdateHintDialog;
import com.zhongmei.yunfu.util.DialogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@SuppressLint("ResourceAsColor")
@EActivity(R.layout.zm_login_layout_init)
public class LoginInitActivity extends BaseActivity implements ILoginController {

    @ViewById(R.id.login_progress)
    ProgressBar progressBar;

    @ViewById(R.id.login_network_tv)
    TextView networkTv;
    @ViewById(R.id.login_network_progress)
    ProgressBar networkProgress;
    @ViewById(R.id.login_network_state_tv)
    AutoVerticalScrollTextView networkStateTv;

    @ViewById(R.id.login_peripheral_tv)
    TextView peripheralTv;
    @ViewById(R.id.login_peripheral_progress)
    ProgressBar peripheralProgress;
    @ViewById(R.id.login_peripheral_state_tv)
    AutoVerticalScrollTextView peripheralStateTv;

    @ViewById(R.id.login_business_tv)
    TextView businessTv;
    @ViewById(R.id.login_business_progress)
    ProgressBar businessProgress;
    @ViewById(R.id.login_business_state_tv)
    AutoVerticalScrollTextView businessStateTv;

    @ViewById(R.id.login_system_data_tv)
    TextView systemDataTv;
    @ViewById(R.id.login_system_data_progress)
    ProgressBar systemDataProgress;
    @ViewById(R.id.login_system_data_state_tv)
    AutoVerticalScrollTextView systemDataStateTv;

    /*@ViewById(R.id.login_check_hint)
    protected TextView mCheckTitle;*/

    /*@ViewById(R.id.show_value)
    protected TextView mPassword;*/

    private AnimationDrawable mAnimDrawable;
    private PrintInstallReceiver mPrintInstallReceiver;

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoginInitActivity_.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //OneApmUtil.initOneApm(getApplicationContext());
        //AuthUserCache.unbind();
        //PushServiceManager.stopPushService();
        if (mPrintInstallReceiver == null) {
            mPrintInstallReceiver = new PrintInstallReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            filter.addDataScheme("package");
            registerReceiver(mPrintInstallReceiver, filter);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CheckManager.cancelCheck();
        if (mPrintInstallReceiver != null) {
            unregisterReceiver(mPrintInstallReceiver);
            mPrintInstallReceiver = null;
        }
    }

    @AfterViews
    protected void initView() {
        checkEnvironment();
    }

    private void checkEnvironment() {
        UserActionEvent.start(UserActionEvent.INIT_PROCESS);
        initCheckProgress();
        CheckManager.initCheck(newCheckCallback);
    }

    private InitCheckCallback newCheckCallback = new InitCheckCallback() {

        @Override
        public void onCheckProgress(InitCheck initCheck, int progress, String message) {
            displayShopId();
            updateCheckState(initCheck.getType(), progress, message);
        }

        @Override
        public void onCheckComplete(InitCheck initCheck, boolean success, String error, Throwable err) {
            if (!success) {
                autoSetConfig(initCheck, error, err);
                return;
            }
            //会有 sdcard下 Android/data/的目录不能及时创建的问题，所以初始化完成后调用图片查询
            //DisplayServiceManager.sendBootBroadCast(MainApplication.getInstance());

            initCommonManager(true, error);
            UserActionEvent.end(UserActionEvent.INIT_PROCESS);
            //checkUpdate();
            startHome();
        }
    };

    private void autoSetConfig(final InitCheck initCheck, String error, Throwable err) {
        //处理自助激活，自助配置及DB版本问题弹窗
        int errorCode = initCheck.getErrorCode(error);
        switch (errorCode) {
            case InitCheck.ERROR_CODE_DEVICE:
                DialogUtil.showErrorConfirmDialog(getSupportFragmentManager(), R.string.device_auth_file, R.string.quite,
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.sInstance.finishAllActivity(null);
                                //System.exit(0);
                            }
                        }, true, "get device auth fail");
                break;
            case InitCheck.ERROR_CODE_VERSION:
                ResetFragment.show(LoginInitActivity.this, ResetFragment.VERSION);
                break;
            case InitCheck.ERROR_CODE_AUTO_SET:
                /*DialogUtil.showErrorConfirmDialog(getSupportFragmentManager(), R.string.get_shop_fail,
                        R.string.selft_activation,
                        R.string.cancel,
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SelfActivationActivity_.intent(LoginInitActivity.this).start();
                                finish();
                            }
                        }, new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }, "selfActivationDialog");*/
                /*try {
                    AutoActivateDialog.show(this, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkEnvironment();
                        }
                    }, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                break;
            case InitCheck.CHECK_UPDATE_APP:
                showCheckUpdateAppDialog(initCheck);
                break;
            case InitCheck.CHECK_UPDATE_PRINT:
                showCheckUpdatePrintDialog(initCheck);
                break;
            case InitCheck.ERROR_VERSIONS_MATCHING:
//                showVersionsSwitcherDialog(initCheck,error);
                showVersionsErrorDialog(initCheck, error);
                break;
            case InitCheck.ERROR_VERSIONS_FAILED:
                showVersionsErrorDialog(initCheck, error);
                break;
            case InitCheck.ERROR_VERSIONS_NOT_CONFIG:
                showVersionsErrorDialog(initCheck, error);
                break;
            case InitCheck.ERROR_AUTH:
                DialogUtil.showErrorConfirmDialog(getSupportFragmentManager(), initCheck.getErrorMessage(error), R.string.quite,
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseApplication.sInstance.finishAllActivity(null);
                            }
                        }, true, "get device auth fail");
                break;
            case InitCheck.ERROR_NETWORK:
                /*ContractRenewalDialog.show(this, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkEnvironment();
                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });*/
                showInitErrorDialog(initCheck, initCheck.getErrorMessage(error),
                        getString(R.string.login_action_settings),
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            }
                        },
                        R.string.login_retry,
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkEnvironment();
                            }
                        });
                break;
            default:
                showInitErrorDialog(initCheck, error, OSLog.getStackTraceString(err), null, R.string.login_retry, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkEnvironment();
                    }
                });
                break;
        }
    }

    private void showCheckUpdateAppDialog(final InitCheck initCheck) {
        boolean isForce = ShopInfoCfg.getInstance().getAppVersionInfo().isForce();
        UpdateHintDialog.UpdateHintDialogBuilder builder = new UpdateHintDialog.UpdateHintDialogBuilder(MainApplication.getInstance())
                .title("系统版本升级"+ShopInfoCfg.getInstance().getAppVersionInfo().getVersionName())
                .message(ShopInfoCfg.getInstance().getAppVersionInfo().getVersionDes())
//                .title(getApplicationContext().getResources().getString(R.string.login_new_update))
                .positiveText(R.string.common_submit_update)
                .positiveLinstner(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        DownloadApkDialog.createDownload(LoginInitActivity.this,
                                ShopInfoCfg.getInstance().getAppVersionInfo(),
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkEnvironment();
                                    }
                                }).star();
                    }
                });
        if (!isForce) {
            builder = builder.negativeText(R.string.common_cancel_update)
                    .negativeLisnter(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpHelper.getDefault().putBoolean(SpHelper.APP_UPDATE_NOT_SECOND_REMIND, true);
                            CheckManager.setIgnore(InitCheck.CHECK_UPDATE_APP);
                            checkEnvironment();
                        }
                    });
        }

        UpdateHintDialog dialogFragment = builder.build();
        dialogFragment.show(getSupportFragmentManager(), "UpdateDialog");
    }

    private DownloadApkDialog mPrintDownloadApkDialog;

    private void showCheckUpdatePrintDialog(final InitCheck initCheck) {
        CommonDialogFragmentBuilder builder = new CommonDialogFragmentBuilder(MainApplication.getInstance())
                .title(getApplicationContext().getResources().getString(R.string.login_new_print_update))
                .iconType(CommonDialogFragment.ICON_HINT)
                .positiveText(R.string.common_submit)
                .positiveLinstner(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mPrintDownloadApkDialog = DownloadApkDialog.createDownload(LoginInitActivity.this,
                                ShopInfoCfg.getInstance().getPrintVersionInfo(),
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkEnvironment();
                                    }
                                })
                                .star();
                    }
                });
        if (!ShopInfoCfg.getInstance().getPrintVersionInfo().isForce()) {
            builder = builder.negativeText(R.string.common_cancel)
                    .negativeLisnter(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckManager.setIgnore(InitCheck.CHECK_UPDATE_PRINT);
                            checkEnvironment();
                        }
                    });
        }
        CommonDialogFragment dialogFragment = builder.build();
        dialogFragment.setCancelWithHomeKey(false);
        dialogFragment.show(getSupportFragmentManager(), "UpdateDialog");
    }

    private void showInitErrorDialog(InitCheck initCheck, String error, String detail, OnClickListener detailTitleListener, int positiveText, OnClickListener positiveLinstner) {
        String checkName = AbsInitCheck.getInitCheckName(initCheck.getType());
        CommonDialogFragmentExtBuilder builder = new CommonDialogFragmentExtBuilder(MainApplication.getInstance())
                .title(checkName)
                .message(String.format(getString(R.string.login_check_error_exit_message), error))
                .messageDetail(detail, detailTitleListener)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(/*R.string.login_retry*/ positiveText)
                .positiveListener(/*new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkEnvironment();
                    }
                }*/ positiveLinstner)
                .negativeText(R.string.login_quiet)
                .negativeLisnter(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseApplication.sInstance.finishAllActivity(null);
                        //System.exit(0);
                    }
                });

        CommonDialogFragmentExt mDialogFragment = builder.build();
        mDialogFragment.setCancelable(false);
        mDialogFragment.setCancelWithHomeKey(false);
        mDialogFragment.show(LoginInitActivity.this.getSupportFragmentManager(), "");
    }

    private void showVersionsSwitcherDialog(InitCheck initCheck, String error) {
        DialogUtil.showErrorConfirmDialog(getSupportFragmentManager(), initCheck.getErrorMessage(error), R.string.quite,
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BaseApplication.sInstance.finishAllActivity(null);
                        //System.exit(0);
                    }
                }, true, "get device auth fail");
    }

    private void showVersionsErrorDialog(InitCheck initCheck, String error) {
        DialogUtil.showErrorConfirmDialog(getSupportFragmentManager(), initCheck.getErrorMessage(error), R.string.quite,
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BaseApplication.sInstance.finishAllActivity(null);
                        //System.exit(0);
                    }
                }, true, "get device auth fail");
    }

    private void initCheckProgress() {
        for (InitCheck.CheckType type : InitCheck.CheckType.values()) {
            updateCheckState(type, 0, null);
        }
    }

    private void updateCheckState(InitCheck.CheckType checkType, int progress, String message) {
        switch (checkType) {
            case BaseInitCheck:
                progressBar.setProgress(25);
                setProgressDrawable(networkProgress, progress);
                networkTv.setTextColor(getProgressColor(progress));
                networkStateTv.setText(message);
                networkStateTv.setVisibility(isShow(progress) ? View.INVISIBLE : View.VISIBLE);
                break;
            case ConfigInitCheck:
                progressBar.setProgress(50);
                setProgressDrawable(peripheralProgress, progress);
                peripheralTv.setTextColor(getProgressColor(progress));
                peripheralStateTv.setText(message);
                peripheralStateTv.setVisibility(isShow(progress) ? View.INVISIBLE : View.VISIBLE);
                break;
            case SyncInitCheck:
                progressBar.setProgress(75);
                setProgressDrawable(businessProgress, progress);
                businessTv.setTextColor(getProgressColor(progress));
                businessStateTv.setText(message);
                businessStateTv.setVisibility(isShow(progress) ? View.INVISIBLE : View.VISIBLE);
                break;
            case CacheInitCheck:
                progressBar.setProgress(100);
                setProgressDrawable(systemDataProgress, progress);
                systemDataTv.setTextColor(getProgressColor(progress));
                systemDataStateTv.setText(message);
                systemDataStateTv.setVisibility(isShow(progress) ? View.INVISIBLE : View.VISIBLE);
                break;
        }
    }

    private void setProgressDrawable(ProgressBar progressBar, int progress) {
        int drawableResId;
        if (progress == 100) {
            drawableResId = R.drawable.login_check_execute_completed;
        } else if (progress > 0) {
            drawableResId = R.drawable.login_check_execute_loading;
        } else {
            drawableResId = R.drawable.login_check_execute_waiting;
        }

        if (progressBar.getTag() == null || Integer.parseInt(progressBar.getTag().toString()) != drawableResId) {
            Drawable drawable = getResources().getDrawable(drawableResId);
            drawable.setBounds(0, 0, progressBar.getWidth(), progressBar.getHeight());
            progressBar.setTag(drawableResId);
            progressBar.setIndeterminateDrawable(drawable);
            if (progress > 0) {
                if (progress == 100) {
                    progressBar.clearAnimation();
                } else {
                    final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                            0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(300);
                    animation.setRepeatCount(Animation.INFINITE);
                    progressBar.setAnimation(animation);
                    animation.startNow();
                }
            }
        }
    }

    private boolean isShow(int progress) {
        return progress == 100 || progress == 0;
    }

    private int getProgressColor(int progress) {
        if (progress == 100) {
            return getResources().getColor(R.color.color_EE5E1F);
        } else if (progress > 0) {
            return getResources().getColor(R.color.color_777777);
        }

        return getResources().getColor(R.color.color_bcbcbc);
    }

    private String getProgress(int progress) {
        if (progress == 100) {
            return getString(R.string.login_check_execute_completed);
        } else if (progress > 0) {
            return getString(R.string.login_check_execute_loading);
        }
        return getString(R.string.login_check_execute_waiting);
    }

    @UiThread
    protected void displayShopId() {
    }

    private void startHome() {
        ShopInfoCfg.getInstance().setUserState(ShopInfoCfg.UserState.INIT);
//        CalmHomeActivity.start(this);
        // v8.15.0 国际化业务修改
        LoginActivity.start(this);
        finish();
    }

    protected void initCommonManager(boolean isFreshCash, final String hint) {
        queryUser();
        displayShopId();
        /*if (hint != null) {
            mCheckTitle.setText(hint);
        }*/
        if (mAnimDrawable != null) {
            if (mAnimDrawable.isRunning()) mAnimDrawable.stop();
        }
    }

    @Background
    protected void queryUser() {
        // 分两次拦截exception，主要是为了前边出异常不会引起查询数据的继续执行
        try {
            SystemUtils.setAutoTime(this.getContentResolver());
            SystemUtils.setTime1224(this.getContentResolver());
        } catch (Exception e) {
            OSLog.error(e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    private class PrintInstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收安装广播
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                String packageName = intent.getDataString();
                if (("package:" + Constant.PRINT_SERVICE_PACKAGE_NAME).equals(packageName)) {
                    if (mPrintDownloadApkDialog != null) {
                        mPrintDownloadApkDialog.dismiss();
                        checkEnvironment();
                    }
                }
            }
        }
    }

}
