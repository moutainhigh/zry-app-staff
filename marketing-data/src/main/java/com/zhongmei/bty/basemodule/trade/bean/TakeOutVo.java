package com.zhongmei.bty.basemodule.trade.bean;

import java.util.List;



public class TakeOutVo {


    private List<TradeVo> listUnProcess;


    private List<TradeVo> listDeliverWait;


    private List<TradeVo> listDelivering;


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
