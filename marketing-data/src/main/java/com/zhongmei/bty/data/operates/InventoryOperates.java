package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.data.operates.message.ResponseObjectExtra;
import com.zhongmei.bty.data.operates.message.content.DishSaleUpdateContract;
import com.zhongmei.bty.data.operates.message.content.InventoryInfoResp;
import com.zhongmei.bty.data.operates.message.content.InventorySetResp;
import com.zhongmei.bty.data.operates.message.content.InventorySyncReq;


public interface InventoryOperates extends IOperates {


    void updateDishSaleCount(DishSaleUpdateContract.DishSaleUpdateReq req, ResponseListener<DishSaleUpdateContract.DishSaleUpdateResp> listener);


    void getInventorySet(ResponseListener<InventorySetResp> listener);


    void getInventoryInfo(String time, ResponseListener<InventoryInfoResp> listener);


    void postInventory(InventorySyncReq inventorySyncReq, ResponseListener<ResponseObjectExtra> listener);
}
