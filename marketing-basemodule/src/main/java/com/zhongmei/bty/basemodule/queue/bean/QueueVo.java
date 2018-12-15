package com.zhongmei.bty.basemodule.queue.bean;

import android.view.View;

import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * @date:2015年9月1日下午5:32:55
 * @deprecated 请使用QueueBeanVo
 */
public class QueueVo {

    private Queue queue;

    // 要显示动画的view
    private transient View view;// modify 2016 06 01 防止序列化

    // 当前号 包括字母 b001
    private String num;

    // 预点菜单
    private TradeVo tradeVo;

    /**
     * 二维码URL地址
     */
    private String qrCodeUrl;

    public String getQrCodeUrl() {
        if (queue != null && queue.queueExtra != null) {
            return queue.queueExtra.url;
        }
        return null;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }

}
