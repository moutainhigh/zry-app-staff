package com.zhongmei.beauty.entity;

import com.zhongmei.yunfu.context.session.core.user.User;

/**
 * Created by demo on 2018/12/15
 */

public class UserVo {
    private User mUser;

    private boolean isOppoint = false; //是否指定（用户操作）默认非指定

    private boolean isFree = true; //是否空闲，默认空闲
    //是否选中
    private boolean isChecked = false;

    private int tradeUserType;// 标记当前身份

    private boolean isChild = false;//是否是子菜

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
