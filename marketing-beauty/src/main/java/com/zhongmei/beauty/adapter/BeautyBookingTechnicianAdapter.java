package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;



public class BeautyBookingTechnicianAdapter extends RecyclerViewBaseAdapter<User, BeautyBookingTechnicianItemView> {

    private Context mContext;

    public BeautyBookingTechnicianAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    protected BeautyBookingTechnicianItemView onCreateItemView(ViewGroup parent, int viewType) {
        BeautyBookingTechnicianItemView view = new BeautyBookingTechnicianItemView(mContext);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<BeautyBookingTechnicianItemView> holder, int position) {
        BeautyBookingTechnicianItemView view = holder.getView();
        User user = (User) getItem(position);
        view.refreshUI(user);
    }

}
