package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.beauty.view.UserItemView;
import com.zhongmei.beauty.view.UserItemView_;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

/**
 * Created by demo on 2018/12/15
 * 技师／顾问／销售员 适配器
 */
public class UserAdapter extends RecyclerViewBaseAdapter<UserVo, UserItemView> {

    private Context mContext;


    private UserItemView.OnUserItemCheckListener onUserItemCheckListener;
    private boolean isHasPonitView;

    public UserAdapter(Context context, boolean isHasPonitView) {
        this.mContext = context;
        this.isHasPonitView = isHasPonitView;
    }

    public void setOnUserItemCheckListener(UserItemView.OnUserItemCheckListener onUserItemCheckListener) {
        this.onUserItemCheckListener = onUserItemCheckListener;
    }

    @Override
    protected UserItemView onCreateItemView(ViewGroup parent, int viewType) {
        UserItemView view = UserItemView_.build(mContext, isHasPonitView);
        view.setOnUserCheckedListener(onUserItemCheckListener);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<UserItemView> holder, int position) {
        UserItemView view = holder.getView();
        UserVo userVo = (UserVo) getItem(position);
        view.refreshUI(userVo);
    }

}
