package com.zhongmei.bty.basemodule.trade.bean;

/**
 * 桌台单据信息
 *
 * @version: 1.0
 * @date 2015年9月7日
 */
public interface IDinnertableTradeMoveDish extends IDinnertableTrade {
    /**
     * 返回标题
     *
     * @return
     */
    abstract String getTitle();

    /**
     * 返回内容
     *
     * @return
     */
    abstract String getContent();

}
