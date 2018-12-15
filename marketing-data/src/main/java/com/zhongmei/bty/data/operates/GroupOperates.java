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

/**
 * Created by demo on 2018/12/15
 */

public interface GroupOperates extends IOperates {
    /**
     * 创建团餐菜单模板
     *
     * @param dishMenuVo
     * @param shopcartItemList
     */
    public void createDishMenu(DishMenuVo dishMenuVo, List<IShopcartItem> shopcartItemList, CalmResponseListener<ResponseObject<DishMenuTransferResp>> listener, Fragment fragment);


    /**
     * 批量删除菜单模板
     *
     * @param dishMenuVoList
     * @param listener
     */
    public void deleteDishMenus(List<DishMenuVo> dishMenuVoList, CalmResponseListener<ResponseObject<SupplyTransferResp<DishMenuResp>>> listener, Fragment fragment);


    /**
     * 开台数据
     *
     * @param vo
     * @param listener
     */
    void openTable(TradeVo vo, CalmResponseListener<ResponseObject<GroupTradeResp>> listener);

    /**
     * 改单操作
     *
     * @param tradeVo
     * @param listener
     * @param isShowLoadingDialog 是否启用咖啡杯
     */
    void modifyGroup(TradeVo tradeVo, CalmResponseListener<ResponseObject<TradeResp>> listener, boolean isShowLoadingDialog);

}