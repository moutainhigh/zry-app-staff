package com.zhongmei.yunfu.ui.view;

import com.zhongmei.yunfu.ui.R;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


@EFragment(resName = "commonmodule_loading_dialog_layout")
public class CalmLoadingDialogFragment extends BasicDialogFragment {

    public static final String TAG = CalmLoadingDialogFragment.class.getSimpleName();

    AnimationDrawable animationDrawable;

    protected boolean mCancelable = true;

    public static boolean isShowing;

    OnClickListener mCancelListener;

    @ViewById(resName = "loading_img")
    ImageView imageView;

    @ViewById(resName = "loading_text")
    TextView textView;

    private String loadingText;

    private int loadingTextId = -1;

    @AfterViews
    void initView() {
        if (!TextUtils.isEmpty(loadingText)) {
            textView.setText(loadingText);
        }
        if (loadingTextId != -1) {
            textView.setText(loadingTextId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        imageView.setImageResource(R.drawable.commonmodule_loading_animation);

        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.commonmodule_Loading_Transparent);
        setCancelable(false);
    }

    public void setLoadingText(String text) {
        if (textView != null) {
            textView.setText(text);
        }
        loadingText = text;
    }

    public void setLoadingText(int textResId) {
        if (textView != null) {
            textView.setText(textResId);
        }
        loadingTextId = textResId;
    }

    public static CalmLoadingDialogFragment show(FragmentManager manager) {
        CalmLoadingDialogFragment dialogFragment = CalmLoadingDialogFragment_.builder().build();
        isShowing = true;
        if (manager != null && !manager.isDestroyed()) {
            try {
                dialogFragment.show(manager, TAG);
            } catch (Exception e) {
                Log.w(TAG, "" + e);
            }
        }
        return dialogFragment;
    }


    public static CalmLoadingDialogFragment showByAllowingStateLoss(FragmentManager manager) {
        CalmLoadingDialogFragment dialogFragment = CalmLoadingDialogFragment_.builder().build();
        isShowing = true;
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(dialogFragment, TAG);
            ft.commitAllowingStateLoss();
        }
        return dialogFragment;
    }

    public static void hide(CalmLoadingDialogFragment dialogFragment) {
        try {
            dialogFragment.dismissAllowingStateLoss();
        } catch (Exception e) {
            Log.w(TAG, "" + e);
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        isShowing = false;
        try {
            if (animationDrawable != null && animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            Log.w(TAG, "" + e);
        }
    }
}
