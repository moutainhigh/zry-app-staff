package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;


public class PrintLayout extends LinearLayout
        implements OnCheckedChangeListener {
    private static final String TAG = PrintLayout.class.getName();
    private Context mContext;
    private TextView title;
    private CheckBox radioButton;

    public PrintLayout(Context context) {
        super(context);
    }

    public PrintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_center_operate_dialog_fragment_print, null);
        title = (TextView) view.findViewById(R.id.title);
        radioButton = (CheckBox) view.findViewById(R.id.print_button);
        addView(view);

        initRadioGroup();
    }

    public PrintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(int titleID) {
        this.title.setText(titleID);
    }

    private void initRadioGroup() {
        radioButton.setText(R.string.order_center_fragment_dialog_print_message);
        radioButton.setChecked(true);
        radioButton.setTextColor(mContext.getResources().getColor(
                R.color.reason_radio_color2));
        radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        radioButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "checked:" + isChecked);
        radioButton.setChecked(isChecked);
        if (isChecked) {
            radioButton.setTextColor(mContext.getResources().getColor(
                    R.color.reason_radio_color2));
        } else {
            radioButton.setTextColor(mContext.getResources().getColor(
                    R.color.reason_radio_color2));
        }
    }

    public boolean getPrintCheckedStatus() {
        return radioButton.isChecked();
    }
}
