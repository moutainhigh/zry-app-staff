package com.zhongmei.bty.mobilepay.event;

public class ExemptEventUpdate {
    private int eraseType;//抹零类别
    private boolean isExempt = false;//是否抹零操作，正餐改单也会发送次消息

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
