package com.zhongmei.bty.commonmodule.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.commonmodule.R;



public class NumberKeyBoard extends LinearLayout {
    public static final String TAG = NumberKeyBoard.class.getSimpleName();

    private TextView v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, vClean;

    private ImageView vPoint, vDelete;

    private EditText editText;

    private boolean isDefaultValue = false;
    private NumberClickListener listener;

    public void setListener(NumberClickListener listener) {
        this.listener = listener;
    }

    public NumberKeyBoard(Context context) {
        this(context, null);
    }

    public NumberKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.number_keyboard_layout, this, true);
        initView();
    }

    public void setShowPoint() {
        vClean.setVisibility(GONE);
        vPoint.setVisibility(VISIBLE);
    }

    public void setShowClean() {
        vClean.setVisibility(VISIBLE);
        vPoint.setVisibility(GONE);
    }

    public void setPointClickable(boolean clickable) {
        vPoint.setClickable(clickable);
    }

    public void setEditView(EditText editText) {
        this.editText = editText;
    }

    public void isNeedClearDefaultValue(boolean need) {
        isDefaultValue = true;
    }


    private void clearDefaultValue() {
        if (isDefaultValue) {
            editText.setText("");
            isDefaultValue = false;
        }
    }

    void initView() {
        v1 = (TextView) findViewById(R.id.one);
        v2 = (TextView) findViewById(R.id.two);
        v3 = (TextView) findViewById(R.id.three);
        v4 = (TextView) findViewById(R.id.four);
        v5 = (TextView) findViewById(R.id.five);
        v6 = (TextView) findViewById(R.id.six);
        v7 = (TextView) findViewById(R.id.seven);
        v8 = (TextView) findViewById(R.id.eight);
        v9 = (TextView) findViewById(R.id.nine);
        vClean = (TextView) findViewById(R.id.clean);
        vPoint = (ImageView) findViewById(R.id.point);
        v0 = (TextView) findViewById(R.id.zero);
        vDelete = (ImageView) findViewById(R.id.delete);

        v1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("1");
                } else {
                    if (listener != null)
                        listener.numberClicked("1");
                }
            }
        });
        v2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("2");
                } else {
                    if (listener != null)
                        listener.numberClicked("2");
                }
            }
        });
        v3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("3");
                } else {
                    if (listener != null)
                        listener.numberClicked("3");
                }
            }
        });
        v4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("4");
                } else {
                    if (listener != null)
                        listener.numberClicked("4");
                }
            }
        });
        v5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("5");
                } else {
                    if (listener != null)
                        listener.numberClicked("5");
                }
            }
        });
        v6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("6");
                } else {
                    if (listener != null)
                        listener.numberClicked("6");
                }
            }
        });
        v7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("7");
                } else {
                    if (listener != null)
                        listener.numberClicked("7");
                }
            }
        });
        v8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("8");
                } else {
                    if (listener != null)
                        listener.numberClicked("8");
                }
            }
        });
        v9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("9");
                } else {
                    if (listener != null)
                        listener.numberClicked("9");
                }
            }
        });
        vClean.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.setText("");
                } else {
                    if (listener != null)
                        listener.clearClicked();
                }

            }
        });
        vPoint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null) {
                    if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                        editText.setText("0.");
                    } else {
                        editText.append(".");
                    }
                } else {
                    if (listener != null)
                        listener.numberClicked(".");
                }

            }
        });
        v0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.append("0");
                } else {
                    if (listener != null)
                        listener.numberClicked("0");
                }
            }
        });
        vDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText != null) {
                    if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                        editText.setText(editText.getText().toString().subSequence(0, editText.length() - 1));
                        editText.setSelection(editText.length());
                    }
                } else {
                    if (listener != null)
                        listener.deleteClicked();
                }

            }
        });
    }

    public interface NumberClickListener {
        void numberClicked(String number);

        void clearClicked();

        void deleteClicked();
    }
}
