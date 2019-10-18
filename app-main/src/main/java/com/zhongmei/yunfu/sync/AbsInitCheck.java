package com.zhongmei.yunfu.sync;


import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.regex.Pattern;


public abstract class AbsInitCheck implements InitCheck {

    private final static String TAG = AbsInitCheck.class.getSimpleName();

    private boolean isInit = false;
    private CheckType checkType;
    private boolean canceled = false;
    private InitCheckCallback callback;

    public AbsInitCheck(CheckType checkType) {
        this.checkType = checkType;
    }

    @Override
    public CheckType getType() {
        return checkType;
    }

    @Override
    public final void check(InitCheckCallback callback) {
        this.callback = callback;
        if (!isInit) {
            isInit = true;
            onInit();
        }
        canceled = false;
        handleCheck(callback);
    }

    protected abstract void handleCheck(InitCheckCallback callback);

    protected void updateCheckProgress(String... messages) {
        for (String msg : messages) {
            checkProgress(this, 1, msg);
            waitRandom();
        }
    }

    protected boolean isInit() {
        return isInit;
    }

    protected void onInit() {
    }

    @Override
    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    protected void waitRandom() {
        try {
            Thread.sleep(Math.max(1, (int) (Math.round(Math.random() * 3))) * 500);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static String getInitCheckName(CheckType type) {
        switch (type) {
            case BaseInitCheck:
                return MainApplication.getInstance().getString(R.string.login_check_device);
            case ConfigInitCheck:
                return MainApplication.getInstance().getString(R.string.login_check_config);
            case SyncInitCheck:
                return MainApplication.getInstance().getString(R.string.login_check_config);
            case CacheInitCheck:
                return MainApplication.getInstance().getString(R.string.login_check_cache);
            default:
                return null;
        }
    }

    public String getConfirmDialogError(int error) {
        return String.format("ErrorCode[%d]", error);
    }

    @Override
    public int getErrorCode(String error) {
        if (!TextUtils.isEmpty(error)) {
            String errorCodeStr = "";
            if (error.startsWith("ErrorCode[") && error.contains("]")) {
                errorCodeStr = error.substring(0, error.indexOf("]") + 1);
            }
            Pattern pattern = Pattern.compile("ErrorCode\\[\\d+\\]");
            if (pattern.matcher(errorCodeStr).matches()) {
                String errorCode = errorCodeStr.replaceAll("\\D*", "");
                return Utils.toInt(errorCode);
            }
        }

        return 0;
    }

    @Override
    public String getErrorMessage(String error) {
        String errorMessage = "";
        if (error.startsWith("ErrorCode[") && !error.endsWith("]")) {
            errorMessage = error.substring(error.indexOf("]") + 1, error.length());
            return errorMessage;
        }
        return error;
    }

    public void checkProgress(InitCheck initCheck, int progress, String message) {
        if (callback != null) {
            callback.onCheckProgress(initCheck, progress, message);
        }
    }

    public void checkComplete(InitCheck initCheck, boolean success, String error) {
        checkComplete(initCheck, success, error, null);
    }

    public void checkComplete(InitCheck initCheck, boolean success, String error, Throwable err) {
        if (callback != null) {
            callback.onCheckComplete(initCheck, success, error, err);
        }
    }
}
