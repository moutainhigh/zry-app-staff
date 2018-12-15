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

/**
 * @Date： 2017/3/1
 * @Description:库存管理类
 * @Version: 1.0
 */
public class InventoryManager {

    //实时库存消息推送标识
    private static final String SALE_INENTORY = "saleInventory";

    //开关消息推送标识
    private static final String AUTO_CLEAR_STATUS = "autoClearStatus";

    //是否正在请求
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

    /**
     * 获取库存开关项外层可回调结果
     */
    public void getInventorySet(ResponseListener<InventorySetResp> resp) {
        InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
        operates.getInventorySet(resp);
    }

    /**
     * 获取库存数据外层可回调结果
     */
    public void getInentoryInfo(String time, ResponseListener<InventoryInfoResp> resp) {
        InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
        operates.getInventoryInfo(time, resp);
    }


    /**
     * 获取库存开关项调用者不需要回调结果
     */
    public void getInventorySet() {
        InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
        operates.getInventorySet(setRespResponseListener);
    }

    /**
     * 获取库存数据调用者不需要回调结果
     */
    public void getInventoryInfo() {
        if (!isRequesting) {
            isRequesting = true;
            InventoryOperates operates = OperatesFactory.create(InventoryOperates.class);
            String time = InventoryCacheUtil.getInstance().getUpdateTime();
            operates.getInventoryInfo(time, infoRespResponseListener);
        }
    }

    /**
     * 库存数据发生改变，重新请求数据
     */
    public void setChangeData(ModulesBody modulesBody) {
        List<String> list = modulesBody.getModules();
        if (!Utils.isEmpty(list)) {
            if (list.contains(AUTO_CLEAR_STATUS)) {
                getInventorySet();
            } else if (list.contains(SALE_INENTORY)) {
                if (InventoryCacheUtil.getInstance().getSaleSwitch()) {//实时库存开启
                    getInventoryInfo();
                }
            }
        }

    }

    /**
     * 扣减库存的数据组装与请求
     */
    public void changeInvetory(TradeVo tradeVo) {
        List<IShopcartItem> reduceList = ShoppingCart.getInstance().getReduceItems();
        List<IShopcartItem> addList = ShoppingCart.getInstance().getAddChangeItems();
        if (!Utils.isEmpty(reduceList)) {//扣减
            InventorySyncReq req = new InventorySyncReq();
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
        if (!Utils.isEmpty(addList)) {//退回
            InventorySyncReq req = new InventorySyncReq();
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

    /**
     * 将IShopcartItem转成Dish
     */
    private void addDish(List<InventorySyncReq.Dish> list, IShopcartItem item) {
        InventorySyncReq.Dish dish = new InventorySyncReq.Dish();
        dish.setAmount(item.getAmount());
        dish.setDishId(item.getDishShop().getBrandDishId());
        dish.setPrice(item.getPrice());
        dish.setQty(item.getTotalQty());
        dish.setUuid(item.getSkuUuid());
        list.add(dish);
    }

    /**
     * 将ISetmealShopcartItem转成Dish
     */
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

    /**
     * 同步库存
     */
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
                    //缓存开关
                    if (content.getSaleNumOpen() != null) {
                        InventoryCacheUtil.getInstance().setSaleNumOpenSwitch(content.getSaleNumOpen().intValue());
                    }
                    InventoryCacheUtil.getInstance().setSaleSwitch(content.getShowSaleVal());
                    if (content.getShowSaleVal() == InventoryCacheUtil.SWITCH_OPEN) {//实时库存开关打开
                        getInventoryInfo();
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
