package com.zhongmei.bty.common.view;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.erp.util.ErpConstants;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.util.ResourceUtils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.settings.view.NumberKeyboardLayout;


public class CustomDiscountDialog extends BasicDialogFragment implements NumberKeyboardLayout.NumberClickListener,
        View.OnClickListener {

    public static final int TYPE_DISCOUNT = 1;
    public static final int TYPE_REBATE = 2;
    private static final String DOT = ".";

    private int mType = TYPE_DISCOUNT;
    private float mAmount = 0;
    private EditText mEditValue;
    private TextView mPrivilege;
    private TextView mTvDiscountUnite;
    private NumberKeyboardLayout mKeyBoard;
    private String mTmpValue = "";

    private CustomDiscountListener listener = null;
    private boolean isDinner;
    private boolean allowFree;
        private String discountPermission;
        private String rebatePermission;

    public interface CustomDiscountListener {
        void onCustomDiscount(float discount);
    }

    public void setListener(CustomDiscountListener listener) {
        this.listener = listener;
    }


    public static CustomDiscountDialog newInstance(String discountPermission, String rebatePermission, int type, float amount) {
        return newInstance(discountPermission, rebatePermission, type, amount, false);
    }


    public static CustomDiscountDialog newInstance(String discountPermission, String rebatePermission, int type, float amount, boolean allowFree) {
        CustomDiscountDialog f = new CustomDiscountDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putFloat("amount", amount);
        bundle.putBoolean("allowFree", allowFree);
        bundle.putString("discountCode", discountPermission);
        bundle.putString("rebateCode", rebatePermission);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type", TYPE_DISCOUNT);
        mAmount = getArguments().getFloat("amount", 0);
        allowFree = getArguments().getBoolean("allowFree");
        discountPermission = getArguments().getString("discountCode");
        rebatePermission = getArguments().getString("rebateCode");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_custom_discount_layout, container);

        mEditValue = (EditText) view.findViewById(R.id.tv_value);
        mPrivilege = (TextView) view.findViewById(R.id.tv_privilege);
        mTvDiscountUnite = (TextView) view.findViewById(R.id.tv_discount_unite);
        mKeyBoard = (NumberKeyboardLayout) view.findViewById(R.id.keyboard);
        mKeyBoard.setListener(this);
        view.findViewById(R.id.btn_ok).setOnClickListener(this);
        NumberKeyBoardUtils.setTouchListener(mEditValue);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String permissionCode;
        if (mType == TYPE_REBATE) {
            permissionCode = rebatePermission;
            ((TextView) view.findViewById(R.id.tv_title)).setText(R.string.userdefine_rebate);
            mEditValue.setHint(R.string.input_userdefine_rebate);
            mTvDiscountUnite.setVisibility(View.GONE);
        } else {
            permissionCode = discountPermission;
            mTvDiscountUnite.setVisibility(View.VISIBLE);
        }

        if (!VerifyHelper.verify(permissionCode)) {
            mPrivilege.setVisibility(View.VISIBLE);
            mPrivilege.setText(R.string.user_has_no_permission);
        } else {
            String value;
            if (mType == TYPE_DISCOUNT) {
                value = Session.getAuthUser().getAuth().getAuthDetail(discountPermission, DinnerApplication.PERMISSION_DISCOUND);
                if (!TextUtils.isEmpty(value)) {
                    mPrivilege.setVisibility(View.VISIBLE);
                    if (!ErpConstants.isSimplifiedChinese()) {
                        mPrivilege.setText(String.format(getString(R.string.now_discount_limit_format), (int) (10 - Float.valueOf(value) * 100) + ""));
                    } else {
                        mPrivilege.setText(String.format(getString(R.string.now_discount_limit_format), value));
                    }

                }
            } else {
                value = Session.getAuthUser().getAuth().getAuthDetail(rebatePermission, DinnerApplication.PERMISSION_REBATE);
                if (!TextUtils.isEmpty(value)) {
                    mPrivilege.setVisibility(View.VISIBLE);
                    mPrivilege.setText(String.format(ResourceUtils.getString(R.string.now_rebate_limit_format), Float.parseFloat(value)));
                }
            }
        }
        return view;
    }

    @Override
    public void numberClicked(String number) {
        if (number.equals(DOT)) {
            if (TextUtils.isEmpty(mTmpValue) || mTmpValue.contains(DOT)) {
                return;
            }
            if (mType == TYPE_DISCOUNT && !ErpConstants.isSimplifiedChinese()) {
                return;
            }
            mTmpValue += number;
        } else if (number.equals("d")) {
            if (mTmpValue.length() <= 1)
                mTmpValue = "";
            else
                mTmpValue = mTmpValue.substring(0, mTmpValue.length() - 1);
            mEditValue.setText(mTmpValue);
            Selection.setSelection(mEditValue.getText(), mEditValue.getText().length());
            return;
        } else {
            mTmpValue += number;
        }

        if (TextUtils.isEmpty(mTmpValue)) {
            return;
        }
        if (mType == TYPE_DISCOUNT) {
            if (mTmpValue.contains(DOT) && mTmpValue.length() > mTmpValue.indexOf(DOT) + 2) {
                ToastUtil.showLongToast(getResources().getString(R.string.inputADecimal));
                mTmpValue = mEditValue.getText().toString().trim();
                return;
            }
        } else {
            if (mTmpValue.contains(DOT) && mTmpValue.length() > mTmpValue.indexOf(DOT) + 3) {
                ToastUtil.showLongToast(getResources().getString(R.string.inputTwoADecimal));
                mTmpValue = mEditValue.getText().toString().trim();
                return;
            }
        }

        mEditValue.setText(mTmpValue);
        Selection.setSelection(mEditValue.getText(), mEditValue.getText().length());
    }

    @Override
    public void onClick(View v) {
        CharSequence tmp = mEditValue.getText();
        if (tmp == null || TextUtils.isEmpty(tmp)) {
            mEditValue.setText("");
            return;
        }

        float value;
        try {
            value = Float.parseFloat(tmp.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ToastUtil.showLongToast(getString(R.string.toast_input_type_error));
            return;
        }
        if (mType == TYPE_DISCOUNT) {
            if (!ErpConstants.isSimplifiedChinese()) {
                if (value <= 0 || value >= 100) {
                    ToastUtil.showLongToast(getResources().getString(R.string.en_discount_error));
                    mTmpValue = "";
                    mEditValue.setText("");
                    return;
                }
                value = (100f - value) / 10f;
            } else {
                if (value <= 0 || value >= 10) {
                    ToastUtil.showLongToast(getResources().getString(R.string.discountError));
                    mTmpValue = "";
                    mEditValue.setText("");
                    return;
                }
            }

        } else {
            if (allowFree) {
                if (value > mAmount) {
                    ToastUtil.showLongToast(getResources().getString(R.string.privilegeError));
                    mTmpValue = "";
                    mEditValue.setText("");
                    return;
                }
            } else {
                if (value >= mAmount) {
                    ToastUtil.showLongToast(getResources().getString(R.string.privilegeError));
                    mTmpValue = "";
                    mEditValue.setText("");
                    return;
                }
            }
        }
        if (listener != null)            listener.onCustomDiscount(value);
        dismiss();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogWidthAndHeight(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setDialogWidthAndHeight(View view) {
        Window window = getDialog().getWindow();
        view.measure(0, 0);
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int desiredWidth = metrics.widthPixels;
        int desiredHeight = metrics.heightPixels;
        window.setLayout(desiredWidth, desiredHeight);
        window.getAttributes().y = 0;
    }
}
