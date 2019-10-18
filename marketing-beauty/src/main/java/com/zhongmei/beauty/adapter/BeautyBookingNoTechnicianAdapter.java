package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.booking.interfaces.BeautyBookingTradeControlListener;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;



public class BeautyBookingNoTechnicianAdapter extends RecyclerViewBaseAdapter<BeautyBookingVo, ReserverNoTechnicianItemView> {

    private Context mContext;
    private BeautyBookingTradeControlListener mListener;

    public BeautyBookingNoTechnicianAdapter(Context context, BeautyBookingTradeControlListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected ReserverNoTechnicianItemView onCreateItemView(ViewGroup parent, int viewType) {
        ReserverNoTechnicianItemView view = new ReserverNoTechnicianItemView(mContext);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ReserverNoTechnicianItemView> holder, int position) {
        ReserverNoTechnicianItemView view = holder.getView();
        BeautyBookingVo bookingTradeVo = (BeautyBookingVo) getItem(position);
        view.refreshUI(bookingTradeVo, mListener);
    }

}
