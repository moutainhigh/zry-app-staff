package com.zhongmei.bty.takeout.ordercenter.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon;

public class RefuseDialog extends Dialog {
    private ImageButton back;
    private Button sure;
    private String reason;
    private RadioGroup refuse_result;
    private RadioButton refuse_result5, refuse_result1, refuse_result2, refuse_result3, refuse_result4;
    private EditTextWithDeleteIcon et;
    private RefuseLisetner lisetner;

    public void setLisetner(RefuseLisetner lisetner) {
        this.lisetner = lisetner;
    }

    public RefuseDialog(Context context) {
        this(context, R.style.custom_alert_dialog);
    }

    public RefuseDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.takeout_refuse_dlg_layout);
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        back = (ImageButton) findViewById(R.id.close);
        sure = (Button) findViewById(R.id.sure);
        refuse_result5 = (RadioButton) findViewById(R.id.refuse_result5);
        refuse_result1 = (RadioButton) findViewById(R.id.refuse_result1);
        refuse_result2 = (RadioButton) findViewById(R.id.refuse_result2);
        refuse_result3 = (RadioButton) findViewById(R.id.refuse_result3);
        refuse_result4 = (RadioButton) findViewById(R.id.refuse_result4);
        refuse_result = (RadioGroup) findViewById(R.id.refuse_result);
        et = (EditTextWithDeleteIcon) findViewById(R.id.refuse_et);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                RefuseDialog.this.dismiss();
            }

        });
        refuse_result.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (refuse_result5.isChecked() && refuse_result.getCheckedRadioButtonId() != -1) {
                    refuse_result5.setChecked(false);
                    et.clearFocus();
                }
                sure.setEnabled(true);
            }
        });
        et.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    if (refuse_result.getCheckedRadioButtonId() != -1) {
                        refuse_result.check(-1);
                    }
                    refuse_result5.setChecked(true);
                    sure.setEnabled(true);
                }
            }
        });
        refuse_result5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et.requestFocus();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (lisetner != null) {

                    if (refuse_result.getCheckedRadioButtonId() != -1) {
                        switch (refuse_result.getCheckedRadioButtonId()) {
                            case R.id.refuse_result1:
                                reason = refuse_result1.getText().toString();
                                break;
                            case R.id.refuse_result2:
                                reason = refuse_result2.getText().toString();
                                break;
                            case R.id.refuse_result3:
                                reason = refuse_result3.getText().toString();
                                break;
                            case R.id.refuse_result4:
                                reason = refuse_result4.getText().toString();
                                break;
                        }
                    } else {
                        if (TextUtils.isEmpty(et.getText())) {
                            ToastUtil.showLongToast(R.string.please_input_reason);
                            return;
                        }
                        reason = et.getText().toString();
                    }
                    lisetner.refuse(reason);
                }
            }
        });
    }

    public interface RefuseLisetner {
        void refuse(String reason);
    }
}
