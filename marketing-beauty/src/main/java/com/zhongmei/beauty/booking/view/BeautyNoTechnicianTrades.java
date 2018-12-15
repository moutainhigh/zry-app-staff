package com.zhongmei.beauty.booking.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.beauty.adapter.BeautyBookingNoTechnicianAdapter;
import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.booking.interfaces.BeautyBookingTradeControlListener;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyNoTechnicianTrades extends LinearLayout implements View.OnClickListener, BeautyBookingTradeControlListener {

    private RecyclerView lv_bookingTrades;
    private BeautyBookingNoTechnicianAdapter mAdapter;
    private TextView tv_tradesNumber;
    private TextView tv_close;

    private BeautyBookingTradeControlListener mListener;

    public BeautyNoTechnicianTrades(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.beauty_custom_notechnician, this);

        lv_bookingTrades = (RecyclerView) view.findViewById(R.id.lv_booking_trades);
        tv_close = (TextView) view.findViewById(R.id.tv_close);
        tv_tradesNumber = (TextView) view.findViewById(R.id.tv_trades_number);

        LinearLayoutManager manager = new RecyclerLinearLayoutManager(context);
        lv_bookingTrades.setLayoutManager(manager);
        tv_close.setOnClickListener(this);
    }

    private void initData(Context context) {
        mAdapter = new BeautyBookingNoTechnicianAdapter(context, this);
        lv_bookingTrades.setAdapter(mAdapter);
    }

    public void refreshData(List<BeautyBookingVo> mNoTechnicianBookingItemVos) {
        tv_tradesNumber.setText(mNoTechnicianBookingItemVos == null ? "0" : mNoTechnicianBookingItemVos.size() + "");
        mAdapter.setItems(mNoTechnicianBookingItemVos);
        mAdapter.notifyDataSetChanged();
    }

    public void setControlListener(BeautyBookingTradeControlListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_close) {
            setVisibility(View.GONE);
            Animation animator = AnimationUtils.loadAnimation(getContext(), R.anim.anim_right_exit_point);
            startAnimation(animator);
        }
    }

    @Override
    public void editTrade(BeautyBookingVo reserverTradeVo) {
        if (mListener != null) {
            mListener.editTrade(reserverTradeVo);
        }
    }
}
