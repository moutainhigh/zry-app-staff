package com.zhongmei.bty.data.operates;

import android.support.v4.app.Fragment;

import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.orderdish.bean.DishMenuVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.data.SupplyTransferResp;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.data.operates.message.content.DishMenuResp;
import com.zhongmei.bty.data.operates.message.content.DishMenuTransferResp;
import com.zhongmei.bty.data.operates.message.content.GroupTradeResp;

import java.util.List;



public interface GroupOperates extends IOperates {

    public void createDishMenu(DishMenuVo dishMenuVo, List<IShopcartItem> shopcartItemList, CalmResponseListener<ResponseObject<DishMenuTransferResp>> listener, Fragment fragment);



    public void deleteDishMenus(List<DishMenuVo> dishMenuVoList, CalmResponseListener<ResponseObject<SupplyTransferResp<DishMenuResp>>> listener, Fragment fragment);



    void openTable(TradeVo vo, CalmResponseListener<ResponseObject<GroupTradeResp>> listener);


    void modifyGroup(TradeVo tradeVo, CalmResponseListener<ResponseObject<TradeResp>> listener, boolean isShowLoadingDialog);

}