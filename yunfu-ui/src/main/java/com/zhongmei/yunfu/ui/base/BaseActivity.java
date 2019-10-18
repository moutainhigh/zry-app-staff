package com.zhongmei.yunfu.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.zhongmei.bty.router.RouteIntent;
import com.zhongmei.yunfu.context.UILoadingController;
import com.zhongmei.yunfu.ui.R;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.ui.base.entity.FragmentVo;

import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends FragmentActivity implements UILoadingController {

    private static final String TAG = BaseActivity.class.getSimpleName();

    public static final String INIT_PAGE = "init_page";

    private String ACTION_PHONE_DISCONNECT = "com.zhongmei.bty.phone.disconnect";

    private boolean consumeTouchEventWhenKeyBoardShow = true;

    private boolean mIsConsumeTochTransEvent = false;

    public List<String> fragmentTag = new ArrayList<String>();

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    @Override
    public Context getContext() {
        return this;
    }

    public void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentTag.add(tag);
    }

    public void addFragment(int containerViewId, Fragment fragment, String tag, Boolean isAnim) {
        if (getSupportFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
            } else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
            }
            fragmentTransaction.add(containerViewId, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentTag.add(tag);
        }

    }


    public void addFragment(FragmentVo mFragmentVo, Boolean isAnim) {
        if (getSupportFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
            } else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
            }
            fragmentTransaction.add(mFragmentVo.getContainerViewId(), mFragmentVo.getFragment(), mFragmentVo.getTag());
            fragmentTransaction.commitAllowingStateLoss();
            fragmentTag.add(mFragmentVo.getTag());

        }
    }


    public void showFragment(Fragment mFragment, Boolean isAnim) {
        if (getSupportFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
            } else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
            }
            fragmentTransaction.show(mFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentTag.add(tag);
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String tag, Boolean isAnim) {
        if (getSupportFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
            } else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
            }
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentTag.add(tag);
        }

    }


    protected void hideFragment(Fragment mFragment) {
        FragmentTransaction fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
        fragmentTransaction.hide(mFragment);
        fragmentTransaction.commit();
    }


    protected void replaceFragment(FragmentVo mFragmentVo, Boolean isAnim) {
        FragmentTransaction fragmentTransaction;
        if (isAnim) {
            fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
        } else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
        }
        fragmentTransaction.replace(mFragmentVo.getContainerViewId(), mFragmentVo.getFragment(), mFragmentVo.getTag());
        fragmentTag.add(mFragmentVo.getTag());
        fragmentTransaction.commitAllowingStateLoss();
    }


    protected void replaceFragment(List<FragmentVo> listFragment, Boolean isAnim) {
        if (listFragment != null && listFragment.size() > 0) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
            } else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
            }
            for (FragmentVo mFragmentVo : listFragment) {
                fragmentTransaction.replace(mFragmentVo.getContainerViewId(),
                        mFragmentVo.getFragment(),
                        mFragmentVo.getTag());
                fragmentTag.add(mFragmentVo.getTag());
            }
            fragmentTransaction.commitAllowingStateLoss();
        }

    }


    protected void removeFragment(Fragment mFragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(mFragment);
        fragmentTag.remove(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }


    protected void removeFragmentByTag(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            fragmentTag.remove(tag);
            ft.commitAllowingStateLoss();
        }
    }


    protected void removeFragments(List<FragmentVo> listFragment, Boolean isAnim) {
        if (listFragment != null && listFragment.size() > 0) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getSupportFragmentManager());
            } else {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
            }
            for (FragmentVo mFragmentVo : listFragment) {
                fragmentTransaction.remove(mFragmentVo.getFragment());
                fragmentTag.remove(mFragmentVo.getTag());
            }
            fragmentTransaction.commit();
        }

    }

    public FragmentTransaction getFragmentTransaction(FragmentManager mFragmentManager) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        return fragmentTransaction;
    }


    public void setConsumeTouchEventWhenKeyBoardShow(boolean consumeTouchEventWhenKeyBoardShow) {
        this.consumeTouchEventWhenKeyBoardShow = consumeTouchEventWhenKeyBoardShow;
    }



    public void setIsConsumeTochTransEvent(boolean isConsumeTochTransEvent) {
        this.mIsConsumeTochTransEvent = isConsumeTochTransEvent;
    }

    BroadcastReceiver phoneDisConnect = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_PHONE_DISCONNECT.equals(action)) {
                CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseActivity.this.getApplicationContext());
                cb.iconType(CommonDialogFragment.ICON_WARNING)
                        .title(R.string.commonmodule_phone_unconnect_please_check)
                        .positiveText(R.string.commonmodule_calm_logout_yes)
                        .positiveLinstner(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                                                Intent intent = new Intent("com.zhongmei.bty.phone.call.disconnect");
                                startActivity(intent);
                            }

                        })
                        .build()
                        .show(getSupportFragmentManager(), "phoneDisconnect");
            } else if (Constant.HAVE_NO_LOGINDATA.equals(action)) {
                new CommonDialogFragmentBuilder(BaseActivity.this.getApplicationContext()).title(getResources().getString(R.string.commonmodule_invalidLogin))
                        .iconType(R.drawable.commonmodule_dialog_icon_warning)
                        .negativeText(R.string.commonmodule_reLogin)
                        .negativeLisnter(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                RouteIntent.startLogin(BaseActivity.this);

                            }
                        })
                        .build()
                        .show(BaseActivity.this.getSupportFragmentManager(), TAG);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        Intent hideIntent = new Intent("com.android.action.hide_navigationbar");
        sendBroadcast(hideIntent);
        IntentFilter filter = new IntentFilter(ACTION_PHONE_DISCONNECT);
        filter.addAction(Constant.HAVE_NO_LOGINDATA);
        registerReceiver(phoneDisConnect, filter);

        registerEventBus();
    }


    private boolean keyboradIsShow() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsConsumeTochTransEvent && deelInputKeyboard()) {
            return super.dispatchTouchEvent(ev);
        }
        if (consumeTouchEventWhenKeyBoardShow && deelInputKeyboard()) {
            return true;
        }
                try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    protected boolean deelInputKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public String getFlag() {
        return this.getClass().getName();
    }


    protected void tryRemoveChildFragment(FragmentManager fm, String tag) {
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }


    protected void tryRemoveTargetFragment(FragmentManager fm, String tag) {
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(phoneDisConnect);

        if (!this.isFinishing()) {
            for (String tag : fragmentTag) {
                try {
                    tryRemoveTargetFragment(getSupportFragmentManager(), tag);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }
        fragmentTag.clear();
        unregisterEventBus();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void registerEventBus() {
        try {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        } catch (Exception e) {
        }
    }

    protected void unregisterEventBus() {
        try {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } catch (Exception e) {
        }
    }

    private CalmLoadingDialogFragment mDialogFragment = null;

    public void showLoadingProgressDialog() {
        showLoadingProgressDialog(null);
    }

    public void showLoadingProgressDialog(String text) {
        if (mDialogFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mDialogFragment = CalmLoadingDialogFragment.showByAllowingStateLoss(fragmentManager);
            mDialogFragment.setLoadingText(text);
            try {
                fragmentManager.executePendingTransactions();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissLoadingProgressDialog() {
        if (mDialogFragment != null) {
            CalmLoadingDialogFragment.hide(mDialogFragment);
            mDialogFragment = null;
        }
    }

    @Override
    public void showLoadingDialog() {
        showLoadingProgressDialog();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadingProgressDialog();
    }
}
