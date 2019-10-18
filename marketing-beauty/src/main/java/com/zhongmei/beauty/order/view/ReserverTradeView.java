package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.yunfu.db.enums.Sex;



public class ReserverTradeView extends LinearLayout {
    private BeautyBookingVo mReserverTradeVo;

    public ReserverTradeView(Context context) {
        super(context);
    }

    public ReserverTradeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setReserverTradeVo(BeautyBookingVo tradeVo) {
        this.mReserverTradeVo = tradeVo;
        refreshUI(tradeVo);

    }

    public BeautyBookingVo getmReserverTradeVo() {
        return mReserverTradeVo;
    }

    private void refreshUI(BeautyBookingVo tradeVo) {
        TextView tv_customerName = (TextView) findViewById(R.id.tv_customer_name);
        tv_customerName.setText(tradeVo.getBooking().getCommercialName() + getNamed(tradeVo.getBooking().getCommercialGender()));
    }


    private String getNamed(Sex sex) {
        if (sex.value() == Sex.FEMALE.value()) {
            return getResources().getString(R.string.beauty_miss);
        } else {
            return getResources().getString(R.string.beauty_mr);
        }
    }


}
