package com.zhongmei.bty.dinner.table.view;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.IZone;

/**
 * @version: 1.0
 * @date 2015年9月21日
 */
public interface OnControlListener {

    /**
     * 切换区域
     *
     * @param zone
     */
    void onSwitchZone(IZone zone);

    /**
     * 点击桌台时将回调此方法
     *
     * @param dinnertableModel
     * @return 返回true表示需要打开操控面板
     */
    boolean onSelect(DinnertableModel dinnertableModel, TableViewBase dinnertableView);

    /**
     * 触发换桌操作时将回调此方法
     *
     * @param orginal
     * @param dest
     */
    void onTransfer(IDinnertableTrade orginal, IDinnertable dest);

    /**
     * 触发合单操作时将回调此方法
     *
     * @param orginal
     * @param dest
     */
    void onMerge(IDinnertableTrade orginal, IDinnertableTrade dest);

    /**
     * 触发删单将回调此方法
     *
     * @param dinnertableTrade 被删除的单据
     */
    void onDelete(IDinnertableTrade dinnertableTrade);

    /**
     * 显示操控板时将回调此方法
     *
     * @param dinnertable
     */
    void onShowControl(IDinnertable dinnertable);

    /**
     * 隐藏操作板时将回调此方法
     */
    void onHideControl();


    /**
     * 触发换桌操作时将回调此方法
     *
     * @param orginal
     * @param dest
     */
    void onTransferMoveDish(IDinnertableTradeMoveDish orginal, IDinnertable dest);

    /**
     * 触发合单操作时将回调此方法
     *
     * @param orginal
     * @param dest
     */
    void onMergeMoveDish(IDinnertableTradeMoveDish orginal, IDinnertableTrade dest);


}
