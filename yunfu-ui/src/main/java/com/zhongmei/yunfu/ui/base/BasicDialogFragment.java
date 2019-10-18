package com.zhongmei.yunfu.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;



public class BasicDialogFragment extends DialogFragment {

    private List<String> fragmentTag = new ArrayList<String>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public <T extends View> T findViewById(int id) {
        if (getView() != null) {
            return (T) getView().findViewById(id);
        }
        return null;
    }


    @Override
    public void onDestroy() {
        if (!getActivity().isFinishing()) {
            for (String tag : fragmentTag) {
                tryRemoveTargetFragment(getFragmentManager(), tag);
                tryRemoveTargetFragment(getChildFragmentManager(), tag);
            }
        }
        fragmentTag.clear();
        dismissLoadingProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        unregisterEventBus();
        super.onDestroyView();
    }


    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void hideSoftInputFromWindow(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);     }

    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        try {
            if (!getActivity().isDestroyed() && getFragmentManager() != null) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(containerViewId, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentTag.add(tag);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    protected void replaceChildFragment(int containerViewId, Fragment fragment, String tag) {
        if (getChildFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentTag.add(tag);
        }
    }

    public void addFragment(int containerViewId, Fragment fragment, String tags) {
        if (getChildFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment, tags);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentTag.add(tags);
        }
    }

    public void showFragment(Fragment mFragment) {
        if (getChildFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.show(mFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    protected void hideFragment(Fragment mFragment) {
        if (getChildFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.hide(mFragment);
            fragmentTransaction.commit();
        }
    }

    private CalmLoadingDialogFragment mDialogFragment = null;

    public void showLoadingProgressDialog() {
        if (mDialogFragment == null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                mDialogFragment = CalmLoadingDialogFragment.showByAllowingStateLoss(fragmentManager);
                fragmentManager.executePendingTransactions();
            }
        }
    }

    public void dismissLoadingProgressDialog() {
        if (mDialogFragment != null) {
            CalmLoadingDialogFragment.hide(mDialogFragment);
            mDialogFragment = null;
        }
    }


    protected void tryRemoveTargetFragment(FragmentManager fm, String tag) {
        if (fm == null || fm.isDestroyed()) {
            return;
        }
        if (!getActivity().isFinishing()) {
            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }


    protected void removeFragment(Fragment mFragment, String tag) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.remove(mFragment);
        fragmentTag.remove(tag);
        fragmentTransaction.commit();
    }

}
