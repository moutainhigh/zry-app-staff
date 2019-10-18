package com.zhongmei.bty.basemodule.shoppingcart.bean;

import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShoppingCartVo {
        protected List<ShopcartItem> listOrderDishshopVo = new ArrayList<ShopcartItem>();

    protected List<IShopcartItem> listIShopcatItem = new ArrayList<IShopcartItem>();

        protected ShopcartItem updateTempShopItem;

        protected ShopcartItem tempShopItem;


        protected Map<String, BigDecimal> selectDishQTYMap = new HashMap<String, BigDecimal>();

        protected TakeOutInfo mTakeOutInfo;

    protected TradeVo mTradeVo = new TradeVo();

    protected List<Tables> mTables;

    protected Map<Integer, TradeCustomer> arrayTradeCustomer;

        protected TradePrivilege dishTradePrivilege;

        public List<IShopcartItemBase> dinnerListShopcartItem = new ArrayList<IShopcartItemBase>();

        private DinnertableTradeInfo dinnertableTradeInfo;

        protected Map<String, IShopcartItem> saveUpdateDish = new HashMap<String, IShopcartItem>();

        private int indexPage = 0;

        private TradeCustomer mTradeMemer;

    private String showPropertyPageDishUUID = "";
        private boolean isGroupMode = false;

        private InventoryVo inventoryVo;

        private TradeUser tradeUser;

    private HashMap<String, InventoryItem> returnInventoryMap;

    public List<ShopcartItem> getListOrderDishshopVo() {
        return listOrderDishshopVo;
    }

    public void setListOrderDishshopVo(List<ShopcartItem> listOrderDishshopVo) {
        this.listOrderDishshopVo = listOrderDishshopVo;
    }

    public List<IShopcartItem> getListIShopcatItem() {
        return listIShopcatItem;
    }

    public void setListIShopcatItem(List<IShopcartItem> listIShopcatItem) {
        this.listIShopcatItem = listIShopcatItem;
    }

    public ShopcartItem getUpdateTempShopItem() {
        return updateTempShopItem;
    }

    public void setUpdateTempShopItem(ShopcartItem updateTempShopItem) {
        this.updateTempShopItem = updateTempShopItem;
    }

    public ShopcartItem getTempShopItem() {
        return tempShopItem;
    }

    public void setTempShopItem(ShopcartItem tempShopItem) {
        this.tempShopItem = tempShopItem;
    }

    public Map<String, BigDecimal> getSelectDishQTYMap() {
        return selectDishQTYMap;
    }

    public void setSelectDishQTYMap(Map<String, BigDecimal> selectDishQTYMap) {
        this.selectDishQTYMap = selectDishQTYMap;
    }

    public TakeOutInfo getmTakeOutInfo() {
        return mTakeOutInfo;
    }

    public void setmTakeOutInfo(TakeOutInfo mTakeOutInfo) {
        this.mTakeOutInfo = mTakeOutInfo;
    }

    public TradeVo getmTradeVo() {
        return mTradeVo;
    }

    public void setmTradeVo(TradeVo mTradeVo) {
        this.mTradeVo = mTradeVo;
    }

    public List<Tables> getmTables() {
        return mTables;
    }

    public void setmTables(List<Tables> mTables) {
        this.mTables = mTables;
    }

    public Map<Integer, TradeCustomer> getArrayTradeCustomer() {
        return arrayTradeCustomer;
    }

    public void setArrayTradeCustomer(Map<Integer, TradeCustomer> arrayTradeCustomer) {
        this.arrayTradeCustomer = arrayTradeCustomer;
    }

    public TradePrivilege getDishTradePrivilege() {
        return dishTradePrivilege;
    }

    public void setDishTradePrivilege(TradePrivilege dishTradePrivilege) {
        this.dishTradePrivilege = dishTradePrivilege;
    }

    public List<IShopcartItemBase> getDinnerListShopcartItem() {
        return dinnerListShopcartItem;
    }

    public void setDinnerListShopcartItem(List<IShopcartItemBase> dinnerListShopcartItem) {
        this.dinnerListShopcartItem = dinnerListShopcartItem;
    }

    public DinnertableTradeInfo getDinnertableTradeInfo() {
        return dinnertableTradeInfo;
    }

    public void setDinnertableTradeInfo(DinnertableTradeInfo dinnertableTradeInfo) {
        this.dinnertableTradeInfo = dinnertableTradeInfo;
    }


    public Map<String, IShopcartItem> getSaveUpdateDish() {
        return saveUpdateDish;
    }

    public void setSaveUpdateDish(Map<String, IShopcartItem> saveUpdateDish) {
        this.saveUpdateDish = saveUpdateDish;
    }

    public int getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(int indexPage) {
        this.indexPage = indexPage;
    }

    public String getShowPropertyPageDishUUID() {
        return showPropertyPageDishUUID;
    }

    public void setShowPropertyPageDishUUID(String showPropertyPageDishUUID) {
        this.showPropertyPageDishUUID = showPropertyPageDishUUID;
    }

    public TradeCustomer getmTradeMemer() {
        return mTradeMemer;
    }

    public void setmTradeMemer(TradeCustomer mTradeMemer) {
        this.mTradeMemer = mTradeMemer;
    }

    public boolean isGroupMode() {
        return isGroupMode;
    }

    public void setGroupMode(boolean groupMode) {
        isGroupMode = groupMode;
    }

    public InventoryVo getInventoryVo() {
        return inventoryVo;
    }

    public void setInventoryVo(InventoryVo inventoryVo) {
        this.inventoryVo = inventoryVo;
        returnInventoryMap = new HashMap<>();
        this.inventoryVo.setReturnInventoryItemMap(returnInventoryMap);
    }

    public void putReturnInventoryMap(List<InventoryItem> returnInventoryList) {
        if (this.returnInventoryMap == null) {
            this.returnInventoryMap = new HashMap<>();
        }
        if (Utils.isNotEmpty(returnInventoryList)) {
            for (InventoryItem item : returnInventoryList) {
                returnInventoryMap.put(item.getTradeItem().getUuid(), item);
            }
        }
        if (inventoryVo == null) {
            inventoryVo = new InventoryVo();
        }
        inventoryVo.setReturnInventoryItemMap(this.returnInventoryMap);
    }

    public TradeUser getTradeUser() {
        return tradeUser;
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }
}
