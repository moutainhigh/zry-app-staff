package com.zhongmei.bty.basemodule.devices.phone.event;

public class ActionSearchDevice {

    private boolean isSearch;

    public ActionSearchDevice(boolean isSearch) {
        super();
        this.isSearch = isSearch;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }

}
