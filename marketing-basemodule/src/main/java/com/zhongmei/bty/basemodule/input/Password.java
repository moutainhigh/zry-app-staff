package com.zhongmei.bty.basemodule.input;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.basemodule.R;

/**
 * 自定义密码输入
 */
public class Password extends LinearLayout {
    public static final String TAG = Password.class.getSimpleName();

    private StringBuffer password;

    private static final int MAX_INPUT_NUMBER = 6;
    private Context context;
    private PassVerify verifyListener;

    public Password(Context context) {
        this(context, null);
    }

    public Password(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        password = new StringBuffer();
    }

    // @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        for (int i = 0; i < MAX_INPUT_NUMBER; i++) {
            ImageView view = new ImageView(context);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.leftMargin = 20;
            view.setBackgroundResource(R.drawable.input_password_dot_normal);
            addView(view, params);
        }
    }

    public void setText(String number) {
        if (password.length() == MAX_INPUT_NUMBER) {
            // 如果已经达到最大数量，则不做任何操作
        } else {
            // 未达到最大值，则继续添加
            password = password.append(number);
            getChildAt(password.length() - 1).setBackgroundResource(R.drawable.input_password_dot_input);
            if (password.length() == MAX_INPUT_NUMBER) {
                if (null != verifyListener) {
                    verifyListener.verifyPass();
                }
            }
        }
    }

    public void deleteText(boolean clear) {
        if (password.length() == 0) {

        } else {
            getChildAt(password.length() - 1).setBackgroundResource(R.drawable.input_password_dot_normal);
            password = password.delete(password.length() - 1, password.length());
            if (clear) {
                deleteText(true);
            }
        }
    }

    public String getInputPassword() {
        return password.toString();
    }

    public void setListener(PassVerify verifyListener) {
        this.verifyListener = verifyListener;
    }

    public interface PassVerify {
        public void verifyPass();
    }
}
