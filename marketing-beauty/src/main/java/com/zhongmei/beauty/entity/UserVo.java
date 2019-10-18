package com.zhongmei.beauty.entity;

import com.zhongmei.yunfu.context.session.core.user.User;



public class UserVo {
    private User mUser;

    private boolean isOppoint = false;
    private boolean isFree = true;         private boolean isChecked = false;

    private int tradeUserType;
    private boolean isChild = false;
    public UserVo(User user) {
        this.mUser = user;
    }


    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public boolean isOppoint() {
        return isOppoint;
    }

    public void setOppoint(boolean oppoint) {
        isOppoint = oppoint;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getTradeUserType() {
        return tradeUserType;
    }

    public void setTradeUserType(int tradeUserType) {
        this.tradeUserType = tradeUserType;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }
}
