package com.zhongmei.yunfu.util;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;

public class DialogUtil {

    private final static String TAG = DialogUtil.class.getSimpleName();

    private static DialogUtil instance = new DialogUtil();

    private static CalmLoadingDialogFragment mLoadingDialogFragment = null;

    private DialogUtil() {
    }

    public static DialogUtil getInstance() {
        return instance;
    }


    public static boolean isLoadingDialogShowing() {
        if (mLoadingDialogFragment == null) {
            return false;
        }
                return CalmLoadingDialogFragment.isShowing;
            }


    public static void showLoadingDialog(FragmentManager fragmentManager, String loadingText) {
        if (fragmentManager != null) {
            try {
                                if (mLoadingDialogFragment != null && CalmLoadingDialogFragment.isShowing) {
                    mLoadingDialogFragment.dismissAllowingStateLoss();
                }
                                mLoadingDialogFragment = CalmLoadingDialogFragment.show(fragmentManager);
                fragmentManager.executePendingTransactions();
                if (!TextUtils.isEmpty(loadingText)) {
                    mLoadingDialogFragment.setLoadingText(loadingText);
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }


    public static void dismissLoadingDialog() {
        if (mLoadingDialogFragment != null) {
            try {
                CalmLoadingDialogFragment.hide(mLoadingDialogFragment);
                mLoadingDialogFragment = null;
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }


    public static DialogFragment showHintConfirmDialog(FragmentManager fragmentManager, int titleResId, int positiveResId,
                                                       int negativeResId, OnClickListener positiveClickListener, OnClickListener negativeClickListener, String tag) {
        return showConfirmDialog(fragmentManager,
                CommonDialogFragment.ICON_HINT,
                titleResId,
                positiveResId,
                negativeResId,
                positiveClickListener,
                negativeClickListener,
                tag);
    }


    public static DialogFragment showHintConfirmDialog(FragmentManager fragmentManager, String title, int positiveResId,
                                                       int negativeResId, OnClickListener positiveClickListener, OnClickListener negativeClickListener, String tag) {
        return showConfirmDialog(fragmentManager,
                CommonDialogFragment.ICON_HINT,
                title,
                positiveResId,
                negativeResId,
                positiveClickListener,
                negativeClickListener,
                tag);
    }


    public static DialogFragment showHintConfirmDialog(FragmentManager fragmentManager, String title, int resId,
                                                       OnClickListener clickListener, boolean isPositive, String tag) {
        return showConfirmDialog(fragmentManager,
                CommonDialogFragment.ICON_HINT,
                title,
                resId,
                clickListener,
                isPositive,
                tag);
    }


    public static DialogFragment showWarnConfirmDialog(FragmentManager fragmentManager, int titleResId, int positiveResId,
                                                       int negativeResId, OnClickListener positiveClickListener, OnClickListener negativeClickListener, String tag) {
        return showConfirmDialog(fragmentManager,
                CommonDialogFragment.ICON_WARNING,
                titleResId,
                positiveResId,
                negativeResId,
                positiveClickListener,
                negativeClickListener,
                tag);
    }


    public static DialogFragment showWarnConfirmDialog(FragmentManager fragmentManager, CharSequence title, int positiveResId,
                                                       int negativeResId, OnClickListener positiveClickListener, OnClickListener negativeClickListener, String tag) {
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df = cb.iconType(CommonDialogFragment.ICON_WARNING)
                .title(title)
                .negativeText(negativeResId)
                .positiveText(positiveResId)
                .positiveLinstner(positiveClickListener)
                .negativeLisnter(negativeClickListener)
                .build();
        df.show(fragmentManager, tag);
        return df;
    }


    public static DialogFragment showWarnConfirmDialog(FragmentManager fragmentManager, String title, int positiveResId,
                                                       int negativeResId, OnClickListener positiveClickListener, OnClickListener negativeClickListener, String tag) {
        return showConfirmDialog(fragmentManager,
                CommonDialogFragment.ICON_WARNING,
                title,
                positiveResId,
                negativeResId,
                positiveClickListener,
                negativeClickListener,
                tag);
    }


    public static DialogFragment showWarnConfirmDialog(FragmentManager fragmentManager, int titleResId, int resId,
                                                       OnClickListener clickListener, boolean isPositive, String tag) {
        return showConfirmDialog(fragmentManager, CommonDialogFragment.ICON_WARNING, titleResId, resId, clickListener,
                isPositive, tag);
    }


    public static DialogFragment showWarnConfirmDialog(FragmentManager fragmentManager, String title, int resId,
                                                       OnClickListener clickListener, boolean isPositive, String tag) {
        return showConfirmDialog(fragmentManager, CommonDialogFragment.ICON_WARNING, title, resId, clickListener,
                isPositive, tag);
    }


    public static DialogFragment showErrorConfirmDialog(FragmentManager fragmentManager, int titleResId, int positiveResId,
                                                        OnClickListener positiveClickListener, boolean isPositive, String tag) {
        return showConfirmDialog(fragmentManager, CommonDialogFragment.ICON_ERROR, titleResId, positiveResId, positiveClickListener,
                isPositive, tag);
    }

    public static DialogFragment showErrorConfirmDialog(FragmentManager fragmentManager, String title, int positiveResId,
                                                        OnClickListener positiveClickListener, boolean isPositive, String tag) {
        return showConfirmDialog(fragmentManager, CommonDialogFragment.ICON_ERROR, title, positiveResId, positiveClickListener,
                isPositive, tag);
    }

    public static DialogFragment showErrorConfirmDialog(FragmentManager fragmentManager, String title, String content, int positiveResId,
                                                        OnClickListener positiveClickListener, boolean isPositive, String tag) {
        return showConfirmDialog(fragmentManager, CommonDialogFragment.ICON_ERROR, title, content, positiveResId, positiveClickListener,
                isPositive, tag);
    }


    public static DialogFragment showErrorConfirmDialog(FragmentManager fragmentManager, int titleResId, int positiveResId,
                                                        int negativeResId, OnClickListener positiveClickListener, OnClickListener negativeClickListener, String tag) {
        return showConfirmDialog(fragmentManager,
                CommonDialogFragment.ICON_ERROR,
                titleResId,
                positiveResId,
                negativeResId,
                positiveClickListener,
                negativeClickListener,
                tag);
    }


    public static DialogFragment showErrorConfirmDialog(FragmentManager fragmentManager, String title, int positiveResId,
                                                        int negativeResId, OnClickListener positiveClickListener, OnClickListener negativeClickListener, String tag) {
        return showConfirmDialog(fragmentManager,
                CommonDialogFragment.ICON_ERROR,
                title,
                positiveResId,
                negativeResId,
                positiveClickListener,
                negativeClickListener,
                tag);
    }


    public static DialogFragment showConfirmDialog(FragmentManager fragmentManager, int iconType, String title,
                                                   int positiveResId, int negativeResId, OnClickListener positiveClickListener,
                                                   OnClickListener negativeClickListener, String tag) {
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df = cb.iconType(iconType)
                .title(title)
                .negativeText(negativeResId)
                .positiveText(positiveResId)
                .positiveLinstner(positiveClickListener)
                .negativeLisnter(negativeClickListener)
                .build();
        df.show(fragmentManager, tag);

        return df;
    }


    public static DialogFragment showConfirmDialog(FragmentManager fragmentManager, int iconType, int titleResId,
                                                   int positiveResId, int negativeResId, OnClickListener positiveClickListener,
                                                   OnClickListener negativeClickListener, String tag) {
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df = cb.iconType(iconType)
                .title(titleResId)
                .negativeText(negativeResId)
                .positiveText(positiveResId)
                .positiveLinstner(positiveClickListener)
                .negativeLisnter(negativeClickListener)
                .build();
        df.show(fragmentManager, tag);

        return df;
    }


    public static DialogFragment showConfirmDialog(FragmentManager fragmentManager, int iconType, int titleResId,
                                                   int resId, OnClickListener clickListener, boolean isPositive, String tag) {
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df;
        cb.iconType(iconType)
                .title(titleResId);
        if (isPositive) {
            cb.positiveText(resId).positiveLinstner(clickListener);
        } else {
            cb.negativeText(resId).negativeLisnter(clickListener);
        }
        df = cb.build();
        df.show(fragmentManager, tag);

        return df;
    }


    public static DialogFragment showConfirmDialog(FragmentManager fragmentManager, int iconType, String title,
                                                   int resId, OnClickListener clickListener, boolean isPositive, String tag) {
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df;
        cb.iconType(iconType)
                .title(title);
        if (isPositive) {
            cb.positiveText(resId).positiveLinstner(clickListener);
        } else {
            cb.negativeText(resId).negativeLisnter(clickListener);
        }
        df = cb.build();
        df.show(fragmentManager, tag);

        return df;
    }

    public static DialogFragment showConfirmDialog(FragmentManager fragmentManager, int iconType, String title, String content,
                                                   int resId, OnClickListener clickListener, boolean isPositive, String tag) {
        CommonDialogFragmentBuilder cb = new CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df;
        cb.iconType(iconType)
                .title(title).
                message(content);
        if (isPositive) {
            cb.positiveText(resId).positiveLinstner(clickListener);
        } else {
            cb.negativeText(resId).negativeLisnter(clickListener);
        }
        df = cb.build();
        df.show(fragmentManager, tag);

        return df;
    }

}
