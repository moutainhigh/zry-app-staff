package com.zhongmei.bty.splash.login.adapter;

import java.util.List;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.adapter.BaseListAdapter;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.splash.login.adapter.UserAdapter.ViewHolder;

public class UserAdapter extends BaseListAdapter<ViewHolder, User> {

    public UserAdapter(List<User> items) {
        super(R.layout.login_user_item_layout, items);
    }

    public static class ViewHolder {
        LinearLayout mUserNameBgLayout;
        ImageView mActiveUserIV;
        TextView mUserNameTV;
    }


    @Override
    public ViewHolder initViewHodler(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.mUserNameBgLayout = (LinearLayout) convertView
                .findViewById(R.id.login_useritem_name_layout);

        holder.mUserNameTV = (TextView) convertView
                .findViewById(R.id.login_useritem_name_tv);
        return holder;
    }

    @Override
    public void setViewHodler(ViewHolder viewHolder, int position) {
        final String userName = mItemList.get(position).getDisplayName();

        viewHolder.mUserNameTV.setText(userName);

    }

}