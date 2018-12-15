package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.data.operates.message.ResponseObjectExtra;
import com.zhongmei.bty.data.operates.message.content.DishSaleUpdateContract;
import com.zhongmei.bty.data.operates.message.content.InventoryInfoResp;
import com.zhongmei.bty.data.operates.message.content.InventorySetResp;
import com.zhongmei.bty.data.operates.message.content.InventorySyncReq;

/**
 * @Date： 2017/2/28
 * @Description:库存相关操作
 * @Version: 1.0
 */
public interface InventoryOperates extends IOperates {

    /**
     * 更新每日售卖量
     *
     * @param req
     * @param listener
     */
    void updateDishSaleCount(DishSaleUpdateContract.DishSaleUpdateReq req, ResponseListener<DishSaleUpdateContract.DishSaleUpdateResp> listener);

    /**
     * 获取库存开关设置项
     *
     * @param listener
     */
    void getInventorySet(ResponseListener<InventorySetResp> listener);

    /**
     * 获取库存数据
     *
     * @param time
     * @param listener
     */
    void getInventoryInfo(String time, ResponseListener<InventoryInfoResp> listener);

    /**
     * 同步商品库存家口
     *
     * @param inventorySyncReq 同步商品携带的网络请求数据
     * @param listener         回调接口
     */
    void postInventory(InventorySyncReq inventorySyncReq, ResponseListener<ResponseObjectExtra> listener);
}
