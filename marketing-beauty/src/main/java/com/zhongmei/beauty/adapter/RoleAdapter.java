package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.beauty.view.RoleItemView;
import com.zhongmei.beauty.view.RoleItemView_;
import com.zhongmei.beauty.view.UserItemView_;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;
import com.zhongmei.yunfu.context.session.core.user.Role;


public class RoleAdapter extends RecyclerViewBaseAdapter<Role, RoleItemView> {

    private Context mContext;

    private Role mCheckedRole;

    private RoleItemView.OnRoleCheckedListener onRoleCheckListener;

    public RoleAdapter(Context context, RoleItemView.OnRoleCheckedListener onRoleCheckListener) {
        this.mContext = context;
        this.onRoleCheckListener = onRoleCheckListener;
    }

    public void setCheckedRole(Role role) {
        this.mCheckedRole = role;
    }

    @Override
    protected RoleItemView onCreateItemView(ViewGroup parent, int viewType) {
        RoleItemView view = RoleItemView_.build(mContext);
        view.setOnRoleCheckedListener(onRoleCheckListener);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<RoleItemView> holder, int position) {
        RoleItemView view = holder.getView();
        Role role = (Role) getItem(position);
        boolean isChecked = mCheckedRole != null && mCheckedRole.getId() == role.getId();
        view.refreshUI(role, isChecked);
    }

}
