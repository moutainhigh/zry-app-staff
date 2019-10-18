package com.zhongmei.bty.customer.event;



public class EventLoginView {
    public static final int TYPE_LOGIN_VIEW_START = 0;    public static final int TYPE_LOGIN_VIEW_END = 1;
    public EventLoginView(int type) {
        this.operateType = type;
    }

    public int getOperateType() {
        return operateType;
    }

    private int operateType;

}
