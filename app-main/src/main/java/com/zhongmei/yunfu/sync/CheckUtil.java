package com.zhongmei.yunfu.sync;

import android.content.Context;
import android.content.res.Resources;

import com.zhongmei.yunfu.init.sync.AppUpdateCheck;
import com.zhongmei.yunfu.init.sync.Check;
import com.zhongmei.yunfu.init.sync.CheckListener;
import com.zhongmei.yunfu.init.sync.SyncCheck;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.OSLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class CheckUtil {

    private ArrayList<Check> mAllCheckList = new ArrayList<Check>();
    private Iterator<Check> mCurrentCheckIterator;
    private CheckCallback mAllCheckListener;
    private Check mCurrentCheck;

    public CheckUtil(Context context, CheckCallback allCheckListener) {
        //mAllCheckList.add(new AppUpdateCheck(context));
        //mAllCheckList.add(new PrintUpdateCheck(context));
        //mAllCheckList.add(new InventoryCheck(context));
        mAllCheckList.add(new SyncCheck(context));
        //mAllCheckList.add(new OfflineCheck(context));
        mAllCheckListener = allCheckListener;
        for (Check item : mAllCheckList) {
            item.setCheckListener(mCheckListener);
        }
    }

    public void cancelCheck() {
        OSLog.info("cancelCheck");
        mCurrentCheck = null;
        mAllCheckListener = null;
        mCheckListener = null;
    }

    public void start(Set<String> modules) {
        OSLog.info("start()");
        mCurrentCheckIterator = mAllCheckList.iterator();
        checkNext(modules);
    }

    private CheckListener mCheckListener = new CheckListener() {

        @Override
        public void update(Check check, String hint) {
            if (mCurrentCheck != null) {
                OSLog.info("update" + check.getmTitle() + "hint==="
                        + hint + ";mCurCheck"
                        + mCurrentCheck.getClass().getName());
                updateCheck(hint);
            }

        }

        @Override
        public void onSuccess(Check check, String hint) {
            if (mCurrentCheck != null) {
                OSLog.info("onQuerySuccess" + hint + ";mCurCheck ="
                        + mCurrentCheck.getClass().getName());
                updateCheck(hint);
                checkNext(null);
            }
        }

        @Override
        public void onError(Check check, String errorMsg, Throwable err) {
            OSLog.error("onError" + errorMsg);
            Resources res = MainApplication.getInstance().getResources();
            if (mCurrentCheck != null && mCurrentCheck.isCanContinue()) {
                OSLog.error(res.getString(R.string.can_continue_operation));
                updateCheck(errorMsg);
                checkNext(null);
            } else {
                OSLog.error(res.getString(R.string.cannot_continue_operation));
                if (null != mAllCheckListener) {
                    mAllCheckListener.onCheckOver(
                            CheckCallback.CHECK_RESULT_RESTART, errorMsg, err);
                }
            }

        }

    };

    private void updateCheck(String hint) {
        if (null != mAllCheckListener) {
            int progress = (mAllCheckList.indexOf(mCurrentCheck) * 100)
                    / mAllCheckList.size();
            mAllCheckListener.onCheckRunning(hint, progress);
        }
    }

    private void checkNext(Set<String> modules) {
        if (mAllCheckListener == null) {
            return;
        }

        if (mCurrentCheckIterator.hasNext()) {
            mCurrentCheck = mCurrentCheckIterator.next();
            OSLog.info("mCurrentCheck = "
                    + mCurrentCheck.getClass().getSimpleName() + " exec");
            mCurrentCheck.running(modules);
        } else {
            if (null != mAllCheckListener) {
                Resources res = MainApplication.getInstance().getResources();
                mAllCheckListener.onCheckOver(
                        CheckCallback.CHECK_RESULT_SUCCESS, res.getString(R.string.all_check_has_finished), null);
            }
        }
    }

}
