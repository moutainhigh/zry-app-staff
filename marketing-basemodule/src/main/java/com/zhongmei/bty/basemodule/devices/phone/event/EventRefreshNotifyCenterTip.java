package com.zhongmei.bty.basemodule.devices.phone.event;


/**
 * 通知中心的红点是否显示
 */
public class EventRefreshNotifyCenterTip {

    /*value =  View.INVISIBLE, View.VISIBLE, View.GONE*/
//	public int isShow;

    private int unprocessCount;

    private boolean isSystemState;

//	public EventRefreshNotifyCenterTip(int isShow) {
//		super();
//		this.isShow = isShow;
//	}

    public EventRefreshNotifyCenterTip(int unprocessCount, boolean isSystemState) {

        super();
        this.unprocessCount = unprocessCount;
        this.isSystemState = isSystemState;
    }

//	public int getIsShow() {
//		return isShow;
//	}
//
//	public void setIsShow(int isShow) {
//		this.isShow = isShow;
//	}


    public int getUnprocessCount() {
        return unprocessCount;
    }


    public boolean isSystemState() {
        return isSystemState;
    }

}
