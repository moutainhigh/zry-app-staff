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

/**
 * @Date：2015年10月11日 上午9:39:10
 * @Description: 购物车数据对象
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class ShoppingCartVo {
    // 未生效的菜品
    protected List<ShopcartItem> listOrderDishshopVo = new ArrayList<ShopcartItem>();

    protected List<IShopcartItem> listIShopcatItem = new ArrayList<IShopcartItem>();

    // 临时保存套餐选菜信息
    protected ShopcartItem updateTempShopItem;

    // 用于保存当前正在操作的套餐
    protected ShopcartItem tempShopItem;

    // 临时保存删除的套餐
    // private SetmealShopcartItem tempSetmealShopcartItem;

    // 用于记录每个菜品点的数量
    protected Map<String, BigDecimal> selectDishQTYMap = new HashMap<String, BigDecimal>();

    // 用户在没有呼入电话时将起信息添加到tradeCustomer
    protected TakeOutInfo mTakeOutInfo;

    protected TradeVo mTradeVo = new TradeVo();

    protected List<Tables> mTables;

    protected Map<Integer, TradeCustomer> arrayTradeCustomer;

    // 菜品折扣
    protected TradePrivilege dishTradePrivilege;

    // 正餐结算界面中被选中的菜品
    public List<IShopcartItemBase> dinnerListShopcartItem = new ArrayList<IShopcartItemBase>();

    // 正餐桌台信息
    private DinnertableTradeInfo dinnertableTradeInfo;
//	protected IDinnertable mIDinnertable;
    // 正餐桌台
//	protected DinnertableTradeVo mDinnertableTradeVo;

    // 保存订单中修改的菜品数据
    protected Map<String, IShopcartItem> saveUpdateDish = new HashMap<String, IShopcartItem>();

    // 用于保存当前操作界面
    private int indexPage = 0;

    // 用于订单回执时保存登录会员
    private TradeCustomer mTradeMemer;

    private String showPropertyPageDishUUID = "";
    //是否是团餐或者自助餐组合点菜
    private boolean isGroupMode = false;

    //库存的一些数据
    private InventoryVo inventoryVo;

    //add v 8.1 订单销售员
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

//	public IDinnertable getmIDinnertable() {
//		return mIDinnertable;
//	}
//	
//	public void setmIDinnertable(IDinnertable mIDinnertable) {
//		this.mIDinnertable = mIDinnertable;
//	}
//	
//	public DinnertableTradeVo getmDinnertableTradeVo() {
//		return mDinnertableTradeVo;
//	}
//	
//	public void setmDinnertableTradeVo(DinnertableTradeVo mDinnertableTradeVo) {
//		this.mDinnertableTradeVo = mDinnertableTradeVo;
//	}

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
