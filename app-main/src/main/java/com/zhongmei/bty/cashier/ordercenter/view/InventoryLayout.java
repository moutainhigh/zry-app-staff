package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;


public class InventoryLayout extends LinearLayout implements RadioGroup.OnCheckedChangeListener {

    private Context mContext;

    private TextView title;

    private RadioGroup mRadioGroup;

    private boolean isReturnInventory = true;
    public InventoryLayout(Context context) {
        super(context);
    }

    public InventoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_center_operate_dialog_fragment_inventory, null);
        title = (TextView) view.findViewById(R.id.title);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.inventory_radiogroup);
        mRadioGroup.setOnCheckedChangeListener(this);
        addView(view);
    }

    public InventoryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(int titleID) {
        this.title.setText(titleID);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.return_inventory_btn) {
            isReturnInventory = true;
        } else {
            isReturnInventory = false;
        }
    }


    public boolean getInventoryCheckStatus() {
        return isReturnInventory;
    }
}
