package com.zhongmei.bty.basemodule.shopmanager.closing.message;

import com.zhongmei.bty.basemodule.shopmanager.closing.bean.ClosingAccountRecord;
import com.zhongmei.bty.basemodule.shopmanager.closing.bean.HandoverItem;
import com.zhongmei.bty.basemodule.shopmanager.closing.bean.UnHandoverItem;

import java.util.List;


/**
 * 关账交接记录查询response
 *
 * @date:2015年12月25日上午10:00:51
 */
public class ClosingHandOverResp {

    // 已交接记录
    private List<HandoverItem> handoverItems;

    // 未交接记录设备列表
    private List<UnHandoverItem> unHandoverItems;
    //最后一次关账记录
    private ClosingAccountRecord lastClosing;
    //是否有为清账订单
    private boolean haveUnClearAccount;

    //是否有支付中订单 true 有，false 没有
    private boolean havePaying;

    //是否有未付款订单，true 有，false 没有
    private boolean haveUnpaid;

    private boolean haveUnFinishPaid; //是否存在已支付未完结订单，true 有，false 没有


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
