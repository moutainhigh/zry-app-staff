package com.zhongmei.bty.dinner.table.view;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.IZone;


public interface OnControlListener {


    void onSwitchZone(IZone zone);


    boolean onSelect(DinnertableModel dinnertableModel, TableViewBase dinnertableView);


    void onTransfer(IDinnertableTrade orginal, IDinnertable dest);


    void onMerge(IDinnertableTrade orginal, IDinnertableTrade dest);


    void onDelete(IDinnertableTrade dinnertableTrade);


    void onShowControl(IDinnertable dinnertable);


    void onHideControl();



    void onTransferMoveDish(IDinnertableTradeMoveDish orginal, IDinnertable dest);


    void onMergeMoveDish(IDinnertableTradeMoveDish orginal, IDinnertableTrade dest);


}
