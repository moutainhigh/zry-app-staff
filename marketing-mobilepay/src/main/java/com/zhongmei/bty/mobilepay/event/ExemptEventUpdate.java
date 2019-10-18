package com.zhongmei.bty.mobilepay.event;

public class ExemptEventUpdate {
    private int eraseType;    private boolean isExempt = false;
    public ExemptEventUpdate(int type) {
        eraseType = type;
    }

    public ExemptEventUpdate(int type, boolean isexempt) {
        this.eraseType = type;
        this.isExempt = isexempt;
    }

    public int getEraseType() {
        return eraseType;
    }

    public boolean isExempt() {
        return isExempt;
    }

}
