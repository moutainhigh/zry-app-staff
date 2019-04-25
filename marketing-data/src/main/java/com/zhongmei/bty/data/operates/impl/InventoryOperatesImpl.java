package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.data.operates.InventoryOperates;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.data.operates.message.ResponseObjectExtra;
import com.zhongmei.bty.data.operates.message.content.DishSaleUpdateContract;
import com.zhongmei.bty.data.operates.message.content.InventoryInfoResp;
import com.zhongmei.bty.data.operates.message.content.InventoryReq;
import com.zhongmei.bty.data.operates.message.content.InventorySetResp;
import com.zhongmei.bty.data.operates.message.content.InventorySyncReq;
import com.zhongmei.yunfu.resp.data.TransferReq;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Date： 2017/2/28
 * @Description:库存请求接口
 * @Version: 1.0
 */
public class InventoryOperatesImpl extends AbstractOpeartesImpl implements InventoryOperates {

    public InventoryOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void updateDishSaleCount(DishSaleUpdateContract.DishSaleUpdateReq req,
                                    ResponseListener<DishSaleUpdateContract.DishSaleUpdateResp> listener) {
        /*String supplyUrl = ServerAddressUtil.getInstance().getDishSaleUpdateUrl();
        TransferReq<DishSaleUpdateContract.DishSaleUpdateReq> transferReq = new TransferReq<>(supplyUrl, req);

        String url = ServerAddressUtil.getInstance().supplyTransfer();
        OpsRequest.Executor<TransferReq, DishSaleUpdateContract.DishSaleUpdateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseClass(DishSaleUpdateContract.DishSaleUpdateResp.class)
                .responseProcessor(new UpdateDishSaleCountProcessor(req))
                .execute(listener, supplyUrl);*/
    }

    @Override
    public void getInventorySet(ResponseListener<InventorySetResp> listener) {
        /*InventoryReq req = new InventoryReq();
        req.setBrandIdenty(BaseApplication.getInstance().getBrandIdenty());
        req.setShopIdenty(BaseApplication.getInstance().getShopIdenty());
        String supplyUrl = ServerAddressUtil.getInstance().getSupplyInventorySet();
        String url = ServerAddressUtil.getInstance().supplyTransfer();
        TransferReq<InventoryReq> transferReq = new TransferReq<>(supplyUrl, req);

        OpsRequest.Executor<TransferReq, InventorySetResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .addRouter("InventorySwitch")
                .responseClass(InventorySetResp.class)
                .execute(listener, supplyUrl);*/

    }

    @Override
    public void getInventoryInfo(String time, ResponseListener<InventoryInfoResp> listener) {

        /*InventoryReq req = new InventoryReq();
        req.setBrandIdenty(BaseApplication.getInstance().getBrandIdenty());
        req.setShopIdenty(BaseApplication.getInstance().getShopIdenty());
        req.setQueryDate(time);
        String supplyUrl = ServerAddressUtil.getInstance().getSupplySaleInventory();
        String url = ServerAddressUtil.getInstance().supplyTransfer();
        TransferReq<InventoryReq> transferReq = new TransferReq<>(supplyUrl, req);

        OpsRequest.Executor<TransferReq, InventoryInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseClass(InventoryInfoResp.class)
                .execute(listener, supplyUrl);*/
    }

    @Override
    public void postInventory(InventorySyncReq inventorySyncReq, ResponseListener<ResponseObjectExtra> listener) {

        /*String url = ServerAddressUtil.getInstance().supplyTransfer();
        String supplyUrl = ServerAddressUtil.getInstance().getSupplySyncInventory();
        TransferReq<InventorySyncReq> transferReq = new TransferReq<>(supplyUrl, inventorySyncReq);

        OpsRequest.Executor<TransferReq, ResponseObjectExtra> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq)
                .responseClass(ResponseObjectExtra.class)
                .execute(listener, url);*/
    }

    private static class UpdateDishSaleCountProcessor extends OpsRequest.SaveDatabaseResponseProcessor<DishSaleUpdateContract.DishSaleUpdateResp> {

        private Map<String, BigDecimal> map = new HashMap<>();

        private UpdateDishSaleCountProcessor(DishSaleUpdateContract.DishSaleUpdateReq req) {
            if (req != null && req.dishes2Update != null) {
                for (DishSaleUpdateContract.DishSaleUpdateReq.Item item : req.dishes2Update) {
                    map.put(item.uuid, item.saleTotal);
                }
            }
        }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final DishSaleUpdateContract.DishSaleUpdateResp resp) throws Exception {
            return new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Map<String, BigDecimal> successMap = new HashMap<>();

//                        有supply那边各种理由不修改接口，导致，当只有一个菜品的时候，直接返回成功或者失败
//                        ，当有多个菜品的时候，返回一个链表。真TM的郁闷
                    if (resp.data == null) {//单个菜品
                        if (resp.success) {
                            successMap.putAll(map);
                        }
                    } else {//多个菜品
                        for (DishSaleUpdateContract.DishSaleUpdateResp.Item item : resp.data) {
                            if (item.success) {
                                successMap.put(item.uuid, map.get(item.uuid));
                            }
                        }
                    }

                    Dao<DishShop, Long> dao = helper.getDao(DishShop.class);
                    UpdateBuilder<DishShop, Long> builder = dao.updateBuilder();

                    for (Map.Entry<String, BigDecimal> entry : successMap.entrySet()) {
                        builder.updateColumnValue(DishShop.$.residueTotal, entry.getValue());
                        builder.where().eq(DishShop.$.uuid, entry.getKey());
                        builder.update();

                        builder.reset();
                    }

                    helper.getChangeSupportable().addChange(DishShop.class);
                    return null;
                }
            };
        }
    }
}
