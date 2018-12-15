package com.zhongmei.bty.splash.login;

import com.zhongmei.yunfu.context.session.core.user.User;

/**
 * @date:2016年6月7日上午9:24:54
 */
public class UserGridItem {
    //authuser的id
    private Long userId;
    //	用户名
    private String userName;
    //首字母
    private String sortLetters;
    //是否被选中
    private boolean isSelected;
    //首字母的位置
    private int section;
    private User user;//add v8.2

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
