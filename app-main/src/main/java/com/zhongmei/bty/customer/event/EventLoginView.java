package com.zhongmei.bty.customer.event;

/**
 * Created by demo on 2018/12/15
 */

public class EventLoginView {
    public static final int TYPE_LOGIN_VIEW_START = 0;//開始登录
    public static final int TYPE_LOGIN_VIEW_END = 1;//退出登录

    public EventLoginView(int type) {
        this.operateType = type;
    }

    public int getOperateType() {
        return operateType;
    }

    private int operateType;

}
