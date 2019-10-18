package com.zhongmei.bty.basemodule.shopmanager.closing.message;

import com.zhongmei.bty.basemodule.shopmanager.closing.bean.ClosingAccountRecord;
import com.zhongmei.bty.basemodule.shopmanager.closing.bean.HandoverItem;
import com.zhongmei.bty.basemodule.shopmanager.closing.bean.UnHandoverItem;

import java.util.List;



public class ClosingHandOverResp {

        private List<HandoverItem> handoverItems;

        private List<UnHandoverItem> unHandoverItems;
        private ClosingAccountRecord lastClosing;
        private boolean haveUnClearAccount;

        private boolean havePaying;

        private boolean haveUnpaid;

    private boolean haveUnFinishPaid;

    public List<HandoverItem> getHandoverItems() {
        return handoverItems;
    }

    public void setHandoverItems(List<HandoverItem> handoverItems) {
        this.handoverItems = handoverItems;
    }

    public List<UnHandoverItem> getUnHandoverItems() {
        return unHandoverItems;
    }

    public void setUnHandoverItems(List<UnHandoverItem> unHandoverItems) {
        this.unHandoverItems = unHandoverItems;
    }

    public ClosingAccountRecord getLastClosing() {
        return lastClosing;
    }

    public void setLastClosing(ClosingAccountRecord lastClosing) {
        this.lastClosing = lastClosing;
    }

    public boolean isHaveUnClearAccount() {
        return haveUnClearAccount;
    }

    public void setHaveUnClearAccount(boolean haveUnClearAccount) {
        this.haveUnClearAccount = haveUnClearAccount;
    }

    public boolean isHavePaying() {
        return havePaying;
    }

    public void setHavePaying(boolean havePaying) {
        this.havePaying = havePaying;
    }

    public boolean isHaveUnpaid() {
        return haveUnpaid;
    }

    public void setHaveUnpaid(boolean haveUnpaid) {
        this.haveUnpaid = haveUnpaid;
    }

    public boolean isHaveUnFinishPaid() {
        return haveUnFinishPaid;
    }
}
