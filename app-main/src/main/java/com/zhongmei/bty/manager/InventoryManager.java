package com.zhongmei.bty.manager;

import android.util.Log;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.data.operates.InventoryOperates;
import com.zhongmei.bty.data.operates.message.ResponseObjectExtra;
import com.zhongmei.bty.data.operates.message.content.InventoryInfoResp;
import com.zhongmei.bty.data.operates.message.content.InventorySetResp;
import com.zhongmei.bty.data.operates.message.content.InventorySyncReq;
import com.zhongmei.yunfu.init.sync.bean.ModulesBody;

import java.util.ArrayList;
import java.util.List;


public class InventoryManager {

        private static final String SALE_INENTORY = "saleInventory";

        private static final String AUTO_CLEAR_STATUS = "autoClearStatus";

        private boolean isRequesting = false;

    private static InventoryManager mInstance;

    public static InventoryManager getInstance() {
        synchronized (InventoryManager.class) {
            if (mInstance == null) {
                mInstance = new InventoryManager();
            }
        }

        return mInstance;
    }


    public void getInventorySet(ResponseListener<InventorySetResp> resp) {
        InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
        operates.getInventorySet(resp);
    }


    public void getInentoryInfo(String time, ResponseListener<InventoryInfoResp> resp) {
        InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
        operates.getInventoryInfo(time, resp);
    }



    public void getInventorySet() {
        InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
        operates.getInventorySet(setRespResponseListener);
    }


    public void getInventoryInfo() {
        if (!isRequesting) {
            isRequesting = true;
            InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
            String time = InventoryCacheUtil.getInstance().getUpdateTime();
            operates.getInventoryInfo(time, infoRespResponseListener);
        }
    }


    public void setChangeData(ModulesBody modulesBody) {
        List<String> list = modulesBody.getModules();
        if (!Utils.isEmpty(list)) {
            if (list.contains(AUTO_CLEAR_STATUS)) {
                getInventorySet();
            } else if (list.contains(SALE_INENTORY)) {
                if (InventoryCacheUtil.getInstance().getSaleSwitch()) {                    getInventoryInfo();
                }
            }
        }

    }


    public void changeInvetory(TradeVo tradeVo) {
        List<IShopcartItem> reduceList = ShoppingCart.getInstance().getReduceItems();
        List<IShopcartItem> addList = ShoppingCart.getInstance().getAddChangeItems();
        if (!Utils.isEmpty(reduceList)) {            InventorySyncReq req = new InventorySyncReq();
            req.setBrandId(MainApplication.getInstance().getBrandIdenty());
            req.setShopId(MainApplication.getInstance().getShopIdenty());
            req.setOrderNo(tradeVo.getTrade().getTradeNo());
            req.setTradeAmount(tradeVo.getTrade().getTradeAmount());
            req.setBizDate(DateTimeUtils.formatDateTime(tradeVo.getTrade().getBizDate()));
            req.setTradeTime(DateTimeUtils.formatDateTime(tradeVo.getTrade().getTradeTime()));
            req.setDeviceId(SystemUtils.getMacAddress());
            req.setTradeId(tradeVo.getTrade().getId());
            req.setTradeType(1);
            List<InventorySyncReq.Dish> disList = new ArrayList<InventorySyncReq.Dish>();
            for (IShopcartItem item : reduceList) {
                List<SetmealShopcartItem> setmealShopcartItems = (List<SetmealShopcartItem>) item.getSetmealItems();
                if (!Utils.isEmpty(item.getSetmealItems())) {
                    for (SetmealShopcartItem setmealShopcartItem : setmealShopcartItems) {
                        addDish(disList, setmealShopcartItem);
                    }
                } else {
                    addDish(disList, item);
                }

            }
            req.setDishes(disList);
            syncInventory(req, listener);
        }
        if (!Utils.isEmpty(addList)) {            InventorySyncReq req = new InventorySyncReq();
            req.setBrandId(MainApplication.getInstance().getBrandIdenty());
            req.setShopId(MainApplication.getInstance().getShopIdenty());
            req.setOrderNo(tradeVo.getTrade().getTradeNo());
            req.setTradeAmount(tradeVo.getTrade().getTradeAmount());
            req.setBizDate(DateTimeUtils.formatDateTime(tradeVo.getTrade().getBizDate()));
            req.setTradeTime(DateTimeUtils.formatDateTime(tradeVo.getTrade().getTradeTime()));
            req.setDeviceId(SystemUtils.getMacAddress());
            req.setTradeId(tradeVo.getTrade().getId());
            req.setTradeType(2);
            List<InventorySyncReq.Dish> disList = new ArrayList<InventorySyncReq.Dish>();
            for (IShopcartItem item : addList) {
                if (!Utils.isEmpty(item.getSetmealItems())) {
                    List<ReadonlySetmealShopcartItem> setmealItems = (List<ReadonlySetmealShopcartItem>) item.getSetmealItems();
                    for (ReadonlySetmealShopcartItem setmealItem : setmealItems) {
                        addDish(disList, setmealItem);
                    }

                } else {
                    addDish(disList, item);
                }
            }
            req.setDishes(disList);
            syncInventory(req, listener);
        }
    }


    private void addDish(List<InventorySyncReq.Dish> list, IShopcartItem item) {
        InventorySyncReq.Dish dish = new InventorySyncReq.Dish();
        dish.setAmount(item.getAmount());
        dish.setDishId(item.getDishShop().getBrandDishId());
        dish.setPrice(item.getPrice());
        dish.setQty(item.getTotalQty());
        dish.setUuid(item.getSkuUuid());
        list.add(dish);
    }


    private void addDish(List<InventorySyncReq.Dish> list, ISetmealShopcartItem item) {
        InventorySyncReq.Dish dish = new InventorySyncReq.Dish();
        dish.setAmount(item.getAmount());
        dish.setDishId(item.getDishShop().getBrandDishId());
        dish.setPrice(item.getPrice());
        dish.setQty(item.getTotalQty());
        dish.setUuid(item.getSkuUuid());
        list.add(dish);
    }

    private ResponseListener<ResponseObjectExtra> listener = new ResponseListener<ResponseObjectExtra>() {
        @Override
        public void onResponse(ResponseObject<ResponseObjectExtra> response) {
            if (ResponseObject.isOk(response)) {
                Log.e("inventory", response.getMessage());
            }
        }

        @Override
        public void onError(VolleyError error) {

        }
    };


    public void syncInventory(InventorySyncReq inventorySyncReq, ResponseListener<ResponseObjectExtra> listener) {
        InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
        operates.postInventory(inventorySyncReq, listener);
    }

    private ResponseListener<InventorySetResp> setRespResponseListener = new ResponseListener<InventorySetResp>() {
        @Override
        public void onResponse(ResponseObject<InventorySetResp> response) {
            if (ResponseObject.isOk(response)) {
                InventorySetResp content = response.getContent();
                if (content != null && content.isSuccess()) {
                                        if (content.getSaleNumOpen() != null) {
                        InventoryCacheUtil.getInstance().setSaleNumOpenSwitch(content.getSaleNumOpen().intValue());
                    }
                    InventoryCacheUtil.getInstance().setSaleSwitch(content.getShowSaleVal());
                    if (content.getShowSaleVal() == InventoryCacheUtil.SWITCH_OPEN) {                        getInventoryInfo();
                        InventoryCacheUtil.getInstance().setAutoClearSwitch(content.getAutoClearStatus());
                    }
                }
            }
        }

        @Override
        public void onError(VolleyError error) {

        }
    };

    private ResponseListener<InventoryInfoResp> infoRespResponseListener = new ResponseListener<InventoryInfoResp>() {
        @Override
        public void onResponse(ResponseObject<InventoryInfoResp> response) {
            isRequesting = false;
            if (ResponseObject.isOk(response)) {
                InventoryInfoResp content = response.getContent();
                if (content != null && content.isSuccess()) {
                    InventoryCacheUtil.getInstance().setInventoryData(content.getData());
                }
                InventoryCacheUtil.getInstance().setUpdateTime(content.getUpdateTime());
            }
        }

        @Override
        public void onError(VolleyError error) {
            isRequesting = false;
        }
    };
}
