package com.zhongmei.bty.splash.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class DoubleUserDialog extends UserDialog {

    private List<AuthUser> currentUsers;

    private DoubleUserListener listener = null;

    public interface DoubleUserListener {
        void onUserSelect(List<AuthUser> authUsers);
    }

    public DoubleUserDialog(Context context, int titleResId, List<User> userList, List<AuthUser> currentUsers, DoubleUserListener listener) {
        super(context, userList, null, null);
        this.currentUsers = currentUsers;
        this.titleResId = titleResId;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onUserSelect(currentUsers);
                dismiss();
            }
        });
    }

    @Override
    protected List<UserGridItem> filledData(List<User> userList) {
        List<UserGridItem> mSortList = new ArrayList<UserGridItem>();

        for (int i = 0; i < userList.size(); i++) {
            UserGridItem gridItem = new UserGridItem();
            User authUser = userList.get(i);
            gridItem.setUser(authUser);            gridItem.setUserName(authUser.getDisplayName());
            gridItem.setUserId(authUser.getId());
                        String pinyin = characterParser.getSelling(authUser.getDisplayName());
            String sortString = pinyin.substring(0, 1).toUpperCase(Locale.getDefault());

                        if (sortString.matches("[A-Z]")) {
                gridItem.setSortLetters(sortString.toUpperCase(Locale.getDefault()));
            } else {
                gridItem.setSortLetters("#");
            }
            if (currentHasUser(authUser.getId()))
                gridItem.setSelected(true);

            mSortList.add(gridItem);
        }
        return mSortList;
    }

    @Override
    public void onItemClick(UserGridItem item) {
        if (item.isSelected()) {
            if (currentHasUser(item.getUserId())) {
                Iterator<AuthUser> iterator = currentUsers.iterator();
                while (iterator.hasNext()) {
                    User user = iterator.next();
                    if (user.getId().equals(item.getUserId()))
                        iterator.remove();
                }
            }
        } else {
            if (!currentHasUser(item.getUserId())) {
                AuthUser user = new AuthUser();
                user.setId(item.getUserId());
                user.setName(item.getUserName());
                currentUsers.add(user);
            }
        }
    }

    private boolean currentHasUser(Long id) {
        if (currentUsers == null)
            currentUsers = new ArrayList<>();
        for (User user : currentUsers) {
            if (user != null && user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
