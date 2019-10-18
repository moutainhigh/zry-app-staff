package com.zhongmei.beauty.utils;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;



public class BeautyDialogUtils {



    public static DialogFragment showDialog(FragmentManager fragmentManager, int iconType, int titleResId,
                                            int positiveResId, int negativeResId, View.OnClickListener positiveClickListener,
                                            View.OnClickListener negativeClickListener, String tag) {
        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df = cb.iconType(iconType)
                .title(titleResId)
                .negativeText(negativeResId)
                .positiveText(positiveResId)
                .positiveLinstner(positiveClickListener)
                .negativeLisnter(negativeClickListener)
                .negativeBtnRes(R.drawable.beauty_dialog_negative_bg_selector)
                .positiveBtnRes(R.drawable.beauty_dialog_postive_bg_selector)
                .build();
        df.show(fragmentManager, tag);
        return df;
    }

    public static DialogFragment showDialog(FragmentManager fragmentManager, int iconType, String title, View.OnClickListener positiveClickListener,
                                            View.OnClickListener negativeClickListener, String tag) {
        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(BaseApplication.sInstance);
        DialogFragment df = cb.iconType(iconType)
                .title(title)
                .positiveLinstner(positiveClickListener)
                .negativeText(R.string.beauty_cancel)
                .positiveText(R.string.beauty_sure)
                .negativeLisnter(negativeClickListener)
                .negativeBtnRes(R.drawable.beauty_dialog_negative_bg_selector)
                .positiveBtnRes(R.drawable.beauty_dialog_postive_bg_selector)
                .build();
        df.show(fragmentManager, tag);
        return df;
    }
}
