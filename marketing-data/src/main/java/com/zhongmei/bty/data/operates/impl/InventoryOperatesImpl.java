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


public class InventoryOperatesImpl extends AbstractOpeartesImpl implements InventoryOperates {

    public InventoryOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void updateDishSaleCount(DishSaleUpdateContract.DishSaleUpdateReq req,
                                    ResponseListener<DishSaleUpdateContract.DishSaleUpdateResp> listener) {

    }

    @Override
    public void getInventorySet(ResponseListener<InventorySetResp> listener) {


    }

    @Override
    public void getInventoryInfo(String time, ResponseListener<InventoryInfoResp> listener) {


    }

    @Override
    public void postInventory(InventorySyncReq inventorySyncReq, ResponseListener<ResponseObjectExtra> listener) {


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

                    if (resp.data == null) {                        if (resp.success) {
                            successMap.putAll(map);
                        }
                    } else {                        for (DishSaleUpdateContract.DishSaleUpdateResp.Item item : resp.data) {
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
