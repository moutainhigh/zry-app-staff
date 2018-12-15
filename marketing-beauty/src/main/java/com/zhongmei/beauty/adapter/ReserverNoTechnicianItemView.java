package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.booking.interfaces.BeautyBookingTradeControlListener;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.db.enums.Sex;

/**
 * Created by demo on 2018/12/15
 */
public class ReserverNoTechnicianItemView extends RelativeLayout implements View.OnClickListener {

    private ImageView iv_header;
    private TextView tv_name;
    private TextView tv_phone;
    private Button btn_choiceTechnician;
    private TextView tv_time;

    private BeautyBookingVo mBookingVo;
    private BeautyBookingTradeControlListener mListener;


    public ReserverNoTechnicianItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.beauty_reserver_notechnician_item, this);
        initView();
    }

    private void initView() {
        iv_header = (ImageView) findViewById(R.id.iv_member_header);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        btn_choiceTechnician = (Button) findViewById(R.id.btn_choice_technician);
        tv_time = (TextView) findViewById(R.id.tv_reserver_time);

        btn_choiceTechnician.setOnClickListener(this);
    }

    public void refreshUI(BeautyBookingVo reserverTradeVo, BeautyBookingTradeControlListener listener) {
        this.mBookingVo = reserverTradeVo;
        this.mListener = listener;
        Booking bookingTrade = reserverTradeVo.getBooking();
        iv_header.setBackgroundResource(getCustomerHead(bookingTrade.getCommercialGender()));
        tv_name.setText(bookingTrade.getCommercialName() + getNamed(bookingTrade.getCommercialGender()));
        tv_phone.setText(bookingTrade.getCommercialPhone());

        Long startTime = reserverTradeVo.getBooking().getStartTime();
        tv_time.setText(getResources().getString(R.string.beauty_arrive_time) + ":" + DateTimeUtils.formatDate(startTime, DateTimeUtils.DATE_TIME_YYYY_MM_DD_HH_MM));

    }

    private int getCustomerHead(Sex sex) {
        int headRes = R.drawable.beauty_customer_default;
        if (sex == Sex.FEMALE) {
            headRes = R.drawable.beauty_customer_female;
        } else if (sex == Sex.MALE) {
            headRes = R.drawable.beauty_customer_male;
        }
        return headRes;
    }

    private String getNamed(Sex sex) {
        if (sex.value() == Sex.FEMALE.value()) {
            return getResources().getString(R.string.beauty_miss);
        } else {
            return getResources().getString(R.string.beauty_mr);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_choiceTechnician && mListener != null) {
            //选择技师
            mListener.editTrade(mBookingVo);
        }
    }
}
