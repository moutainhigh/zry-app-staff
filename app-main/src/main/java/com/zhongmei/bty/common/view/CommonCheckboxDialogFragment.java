package com.zhongmei.bty.common.view;

import android.content.Context;

import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.DialogInterface.OnKeyListener;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.zhongmei.yunfu.R;

@EFragment(R.layout.common_checkbox_dialog_layout)
public class CommonCheckboxDialogFragment extends CommonDialogFragment implements
        OnClickListener, OnKeyListener {

    @ViewById(R.id.print_kitchen_cb)
    CheckBox printKitchenCb;
    @ViewById(R.id.copy_dish_property_cb)
    CheckBox copyDishPropertyCb;

    @AfterViews
    protected void initView() {
        super.initView();
        super.mNegativeButton.setText("");
        printKitchenCb.setChecked(true);
        copyDishPropertyCb.setChecked(true);
        mPositiveButton.setTag(new CheckBox[]{printKitchenCb, copyDishPropertyCb});
    }

    public static class CommonDialogCheckboxFragmentBuilder extends CommonDialogFragmentBuilder {

        public CommonDialogCheckboxFragmentBuilder(Context context) {
            super(context);
        }

        @Override
        public CommonDialogFragment build() {
            CommonCheckboxDialogFragment fragment = new CommonCheckboxDialogFragment_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setpositiveListener(mpositiveLinstner);
            }
            return fragment;

        }
    }

}
