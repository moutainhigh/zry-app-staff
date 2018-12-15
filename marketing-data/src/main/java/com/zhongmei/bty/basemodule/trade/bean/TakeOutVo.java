package com.zhongmei.bty.basemodule.trade.bean;

import java.util.List;


/**
 * 封装外卖数据对象
 */
public class TakeOutVo {

    /*
     * 未处理单据
     */
    private List<TradeVo> listUnProcess;

    /*
     * 待配送
     */
    private List<TradeVo> listDeliverWait;

    /*
     * 正在配送
     */
    private List<TradeVo> listDelivering;

    /*
     * 配送完成
     */
    private List<TradeVo> listRealDelivery;

    public List<TradeVo> getListUnProcess() {
        return listUnProcess;
    }

    public void setListUnProcess(List<TradeVo> listUnProcess) {
        this.listUnProcess = listUnProcess;
    }

    public List<TradeVo> getListDeliverWait() {
        return listDeliverWait;
    }

    public void setListDeliverWait(List<TradeVo> listDeliverWait) {
        this.listDeliverWait = listDeliverWait;
    }

    public List<TradeVo> getListDelivering() {
        return listDelivering;
    }

    public void setListDelivering(List<TradeVo> listDelivering) {
        this.listDelivering = listDelivering;
    }

    public List<TradeVo> getListRealDelivery() {
        return listRealDelivery;
    }

    public void setListRealDelivery(List<TradeVo> listRealDelivery) {
        this.listRealDelivery = listRealDelivery;
    }


}
