package com.zhongmei.bty.basemodule.customer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.input.NumberClickListener;
import com.zhongmei.bty.basemodule.input.NumberKeyBoard;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;

/**
 *

 *
 */
public class PasswordDialog extends Dialog {

    private ImageButton back;

    private Button sure;

    private EditText et;

    private NumberKeyBoard keyboard;

    private PasswordCheckLisetner lisetner;

    private TextView memberName;

    private ImageView cleanImage;

    private Button bt_read_keybord;

    private boolean isAutoClean = false;

    private StringBuffer password = new StringBuffer();

    public void setLisetner(PasswordCheckLisetner lisetner) {
        this.lisetner = lisetner;
    }

    public PasswordDialog(Context context) {
        this(context, com.zhongmei.yunfu.commonmodule.R.style.custom_alert_dialog);
    }

    public PasswordDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customer_password_dlg_layout);
        init();
    }

    private void init() {
        // 隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        back = (ImageButton) findViewById(R.id.close);
        sure = (Button) findViewById(R.id.sure);
        et = (EditText) findViewById(R.id.password_et);
        keyboard = (NumberKeyBoard) findViewById(R.id.password_keyboard);
        memberName = (TextView) findViewById(R.id.password_member_name);
        cleanImage = (ImageView) findViewById(R.id.password_clean_iv);
        bt_read_keybord = (Button) findViewById(R.id.bt_read_keybord);
        TextView tvClean = (TextView) findViewById(R.id.clean);
        tvClean.setText("");
        bt_read_keybord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (lisetner != null) {
                    lisetner.showReadKeyBord();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                close();
            }

        });
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ClickManager.getInstance().isClicked()) {
                    if (lisetner != null) {
                        if (!TextUtils.isEmpty(password)) {
                            lisetner.checkPassWord(password.toString());
                        } else {
                            ToastUtil.showShortToast(R.string.customer_no_password);
                        }
                    }
                }
            }
        });
        keyboard.setListener(new NumberClickListener() {

            @Override
            public void numberClicked(String number) {
                if (password.length() < 6) {
                    password.append(number);
                    setEtText();
                }
            }

            @Override
            public void deleteClicked() {
                if (!TextUtils.isEmpty(password)) {
                    password.deleteCharAt(password.length() - 1);
                    setEtText();
                }
            }

            @Override
            public void clearClicked() {
                // password.setLength(0);
                // setEtText();
            }
        });
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    cleanImage.setVisibility(View.GONE);
                } else {
                    cleanImage.setVisibility(View.VISIBLE);
                }
                password.setLength(0);
                password.append(s);
                lisetner.showPassWord(s.toString());
            }
        });
        cleanImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                password.setLength(0);
                setEtText();
            }
        });
        et.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    if (ClickManager.getInstance().isClicked()) {
                        return true;
                    }
                    if (lisetner != null) {
                        password.setLength(0);
                        password.append(et.getText());
                        if (!TextUtils.isEmpty(password)) {
                            lisetner.checkPassWord(password.toString());
                        } else {
                            ToastUtil.showShortToast(R.string.customer_no_password);
                        }
                        return true;
                    }
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN && isNumber(keyCode) && password.length() < 6) {
                    password.append(event.getNumber());
                    setEtText();
                    return true;
                }

                return false;
            }
        });
        NumberKeyBoardUtils.setTouchListener(et);
        sure.setFocusable(false);
        et.requestFocus();
    }

    private boolean isNumber(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_NUMPAD_0 || keyCode == KeyEvent.KEYCODE_NUMPAD_1
                || keyCode == KeyEvent.KEYCODE_NUMPAD_2 || keyCode == KeyEvent.KEYCODE_NUMPAD_3
                || keyCode == KeyEvent.KEYCODE_NUMPAD_4 || keyCode == KeyEvent.KEYCODE_NUMPAD_5
                || keyCode == KeyEvent.KEYCODE_NUMPAD_6 || keyCode == KeyEvent.KEYCODE_NUMPAD_7
                || keyCode == KeyEvent.KEYCODE_NUMPAD_8 || keyCode == KeyEvent.KEYCODE_NUMPAD_9;
    }

    /**
     * 修改文本框显示值
     */
    private void setEtText() {
        et.setText(password);
        et.setSelection(password.length());
        et.requestFocus();
        if (lisetner != null && !isAutoClean) {
            lisetner.showPassWord(password.toString());
        } else {
            isAutoClean = false;
        }
    }

    public interface PasswordCheckLisetner {
        void checkPassWord(String password);

        void showPassWord(String password);

        void showReadKeyBord();
    }

    /**
     * 设置会员名称
     *
     * @param name
     */
    public void setMembeName(String name) {
        name = name == null ? getContext().getString(R.string.have_no_name) : name;
        memberName.setText(this.getContext().getString(R.string.customer_passwor_dialog_membername, name));
    }

    /**
     * 设置会员内容
     *
     * @param content
     */
    public void setMemberContent(String content) {
        memberName.setText(content);
    }

    /**
     * 关闭
     */
    public void close() {
        PasswordDialog.this.dismiss();
    }

    /**
     * 清空输入框
     */
    public void clean() {
        isAutoClean = true;
        password.setLength(0);
        setEtText();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }
}
