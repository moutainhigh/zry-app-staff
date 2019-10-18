package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.yunfu.context.session.core.user.User;


public class BeautyBookingTechnicianItemView extends RelativeLayout {

    private TextView tv_name;


    public BeautyBookingTechnicianItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.beauty_booking_board_technician_item, this);
        initView();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_technician_name);
    }

    public void refreshUI(User user) {
        tv_name.setText(user.getName());

    }


}
