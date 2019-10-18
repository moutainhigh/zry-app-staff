package com.zhongmei.bty.basemodule.trade.bean;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.TradePrintStatus;
import com.zhongmei.yunfu.db.enums.TradeServingStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DinnertableTradeInfo {

    private static final String TAG = DinnertableTradeInfo.class.getSimpleName();

    private String tradeTableUuid;

    private Long tradeTableId;


    private String serialNumber;


    int numberOfMeals;


    int spendTime;


    String tableName;


    int tableSeatCount;


    int tableMealCount;


    String tableZoneName;

    TradeType tradeType;


    private TradeVo tradeVo;


    private List<IShopcartItem> items;

    private IDinnertableTrade iDinnertableTrade;

        private List<DinnertableTradeInfo> subTradeInfoList;

    private DinnertableTradeInfo() {
    }

    public DinnertableTradeInfo(TradeVo tradeVo) {
        setTradeVo(tradeVo);
    }

    public String getTradeTableUuid() {
        return tradeTableUuid;
    }

    public Long getTradeTableId() {
        return tradeTableId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getNumberOfMeals() {
        return numberOfMeals;
    }

    public int getSpendTime() {
        return spendTime;
    }

    public String getTableName() {
        return tableName;
    }

    public int getTableSeatCount() {
        return tableSeatCount;
    }

    public int getTableMealCount() {
        return tableMealCount;
    }

    public String getTableZoneName() {
        return tableZoneName;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public List<IShopcartItem> getItems() {
        return items;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;

        items = buildShopcartItem(tradeVo);
    }


    public static DinnertableTradeInfo create(TradeVo tradeVo, DinnerTableInfo dinnerTable) {
        DinnertableTradeInfo info = new DinnertableTradeInfo();
        if (Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            info.tradeTableUuid = tradeTable.getUuid();
            info.tradeTableId = tradeTable.getId();
            info.numberOfMeals = tradeTable.getTablePeopleCount();
            info.spendTime = (int) (System.currentTimeMillis() - tradeTable.getClientCreateTime()) / (60 * 1000);
            info.tableName = tradeTable.getTableName();
        }
        info.serialNumber = tradeVo.getTradeExtra().getSerialNumber();
        if (info.spendTime < 0) {
            info.spendTime = -1;
        }
        if (dinnerTable != null) {
            info.tableSeatCount = dinnerTable.getTableSeatCount();
            info.tableMealCount = dinnerTable.getTableMealCount();
            info.tableZoneName = dinnerTable.getTableZoneName();
        }
        info.tradeType = tradeVo.getTrade().getTradeType();
        info.setTradeVo(tradeVo);
        return info;
    }

    public static DinnertableTradeInfo create(IDinnertableTrade dinnertableTrade, TradeVo tradeVo) {
        DinnertableTradeInfo info = new DinnertableTradeInfo();
        info.tradeTableUuid = dinnertableTrade.getUuid();
        info.tradeTableId = dinnertableTrade.getId();
        info.serialNumber = dinnertableTrade.getSn();
        info.numberOfMeals = dinnertableTrade.getNumberOfMeals();
        info.spendTime = dinnertableTrade.getSpendTime();
        info.tableName = dinnertableTrade.getDinnertable().getName();
        info.tableSeatCount = dinnertableTrade.getDinnertable().getNumberOfSeats();
        info.tableMealCount = dinnertableTrade.getDinnertable().getNumberOfMeals();
        info.tableZoneName = dinnertableTrade.getDinnertable().getZone().getName();
        info.iDinnertableTrade = dinnertableTrade;
        info.tradeType = dinnertableTrade.getTradeType();
        info.setTradeVo(tradeVo);
        return info;
    }

    public static DinnertableTradeInfo createNoTableBuffet(TradeVo tradeVo) {
        DinnertableTradeInfo info = new DinnertableTradeInfo();
        TradeTableInfo tradeTableInfo = new TradeTableInfo(tradeVo.getTrade(), new TradeTable(), getTradeStatus(tradeVo.getTradeExtra()), null, null);
        info.serialNumber = tradeVo.getTrade().getSerialNumber();
        info.tradeType = tradeVo.getTrade().getTradeType();
        info.numberOfMeals = tradeVo.getTrade().getTradePeopleCount();
        info.iDinnertableTrade = new NoTableBuffetTradeModel(tradeTableInfo);
        info.setTradeVo(tradeVo);
        return info;
    }

    private static DinnertableStatus getTradeStatus(TradeExtra tradeExtra) {
        DinnertableStatus status = DinnertableStatus.UNISSUED;

        if(tradeExtra==null){
            return status;
        }

        if (tradeExtra.getHasServing() == TradeServingStatus.SERVED) {
            status = DinnertableStatus.SERVING;
        } else {
            if (tradeExtra.getIsPrinted() == TradePrintStatus.PRINTED) {
                status = DinnertableStatus.ISSUED;
            } else {
                status = DinnertableStatus.UNISSUED;
            }
        }
        return status;
    }



    public static List<IShopcartItem> buildShopcartItem(TradeVo tradeVo) {
        String shellUuid = "";
        if (tradeVo.getMealShellVo() != null) {
            shellUuid = tradeVo.getMealShellVo().getUuid();
        }
        ShopcartItemType shopcartItemType = ShopcartItemType.COMMON;
        if (tradeVo.isUnionMainTrade()) {
            shopcartItemType = ShopcartItemType.MAINBATCH;
        } else if (tradeVo.isUnionSubTrade()) {
            shopcartItemType = ShopcartItemType.SUB;
        }
        return buildShopcartItem(tradeVo.getTradeItemList(), shellUuid, tradeVo.getDeskCount(), shopcartItemType);
    }

    public static List<IShopcartItem> buildShopcartItem(List<TradeItemVo> tradeItemVoList, String shellUuid, BigDecimal deskCount) {
        return buildShopcartItem(tradeItemVoList, shellUuid, deskCount, ShopcartItemType.COMMON);
    }

    public static List<IShopcartItem> buildShopcartItem(List<TradeItemVo> tradeItemVoList, String shellUuid, BigDecimal deskCount, ShopcartItemType shopcartItemType) {
        if (Utils.isEmpty(tradeItemVoList)) {
            return new ArrayList<>(0);
        }

        Map<String, TradeItemVo> parentFinder = new LinkedHashMap<String, TradeItemVo>();
        List<TradeItemVo> setmealList = new ArrayList<TradeItemVo>();
        List<TradeItemVo> extraList = new ArrayList<TradeItemVo>();
        for (TradeItemVo tradeItemVo : tradeItemVoList) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (tradeItem.getType() == DishType.EXTRA) {
                extraList.add(tradeItemVo);
            } else if (TextUtils.isEmpty(tradeItem.getParentUuid()) || tradeItem.getParentUuid().equals(shellUuid)) {
                parentFinder.put(tradeItem.getUuid(), tradeItemVo);
            } else {
                setmealList.add(tradeItemVo);
            }
        }
        List<IShopcartItem> items = new ArrayList<IShopcartItem>();
        Set<TradeItem> allReturnsMap = new HashSet<TradeItem>();
        Map<String, ReadonlyShopcartItem> singleFinder = new HashMap<String, ReadonlyShopcartItem>();

        for (TradeItemVo tradeItemVo : parentFinder.values()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (tradeItem == null)
                continue;
                        if (tradeItem.getQuantity() != null && tradeItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                BigDecimal qty;
                TradeItemVo relateItemVo = parentFinder.get(tradeItem.getRelateTradeItemUuid());
                if (relateItemVo != null && relateItemVo.getTradeItem().getInvalidType() == InvalidType.RETURN_QTY) {
                    qty = relateItemVo.getTradeItem().getReturnQuantity().negate();
                } else {
                    qty = BigDecimal.ONE;
                }
                tradeItem.setQuantity(qty);
                allReturnsMap.add(tradeItem);
            }

            ReadonlyShopcartItem item = new ReadonlyShopcartItem(tradeItem, tradeItemVo.getRejectQtyReason());
            item.setTradeItemExtra(tradeItemVo.getTradeItemExtra());
            item.setCouponPrivilegeVo(tradeItemVo.getCouponPrivilegeVo());
            item.setDiscountReasonRel(tradeItemVo.getReasonLast());
            item.setPrivilege(tradeItemVo.getTradeItemPrivilege());
            item.setProperties(buildProperties(tradeItemVo));
            item.setExtraItems(new ArrayList<ReadonlyExtraShopcartItem>());
            item.setSetmealItems(new ArrayList<ReadonlySetmealShopcartItem>());
            item.setTradeItemOperations(tradeItemVo.getTradeItemOperations());
            item.setKdsScratchDishQty(tradeItemVo.getKdsScratchDishQty());             item.setTradeItemExtraDinner(tradeItemVo.getTradeItemExtraDinner());
            item.setTradeItemMainBatchRelList(tradeItemVo.getTradeItemMainBatchRelList());
            item.setTradeItemUserList(tradeItemVo.getTradeItemUserList());
            item.setCardServicePrivilegeVo(tradeItemVo.getCardServicePrivilegeVo());
            item.setAppletPrivilegeVo(tradeItemVo.getAppletPrivilegeVo());
            singleFinder.put(tradeItem.getUuid(), item);
            if (!TextUtils.isEmpty(item.getParentUuid()) && shellUuid.equals(item.getParentUuid())) {
                item.setIsGroupDish(true);
                item.setSigleDeskQuantity(ShopcartItemUtils.getDisplyQty(item, deskCount));
            } else {
                item.setIsGroupDish(false);
            }
            item.setShopcartItemType(shopcartItemType);
            items.add(item);
        }
                Map<String, ReadonlySetmealShopcartItem> setmealFinder = new HashMap<String, ReadonlySetmealShopcartItem>();
        for (TradeItemVo tradeItemVo : setmealList) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            String parentUuid = tradeItem.getParentUuid();
            ReadonlyShopcartItem parent = singleFinder.get(parentUuid);
            if (parent == null) {
                Log.e(TAG, "Not found the tradeItem's parent! tradeItem.uuid=" + tradeItem.getUuid());
            } else {
                ReadonlySetmealShopcartItem setmealItem = new ReadonlySetmealShopcartItem(tradeItem, parent);
                setmealItem.setTradeItemExtra(tradeItemVo.getTradeItemExtra());
                setmealItem.setPrivilege(tradeItemVo.getTradeItemPrivilege());
                setmealItem.setProperties(buildProperties(tradeItemVo));
                setmealItem.setExtraItems(new ArrayList<ReadonlyExtraShopcartItem>());
                setmealItem.setTradeItemOperations(tradeItemVo.getTradeItemOperations());
                setmealItem.setTradeItemExtraDinner(tradeItemVo.getTradeItemExtraDinner());
                setmealItem.setTradeItemMainBatchRelList(tradeItemVo.getTradeItemMainBatchRelList());
                setmealItem.setTradeItemUserList(tradeItemVo.getTradeItemUserList());
                if (parent.isGroupDish()) {
                    setmealItem.setIsGroupDish(true);
                    setmealItem.setSigleDeskQuantity(ShopcartItemUtils.getDisplyQty(setmealItem, deskCount));
                }
                setmealItem.setShopcartItemType(shopcartItemType);
                parent.getSetmealItems().add(setmealItem);
                setmealFinder.put(tradeItem.getUuid(), setmealItem);
            }
        }
                for (TradeItemVo tradeItemVo : extraList) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            String parentUuid = tradeItem.getParentUuid();
            ReadonlyShopcartItem parent = singleFinder.get(parentUuid);
            if (parent != null) {
                ReadonlyExtraShopcartItem extraItem = new ReadonlyExtraShopcartItem(tradeItem, parent);
                if (parent.isGroupDish()) {
                    extraItem.setIsGroupDish(true);
                    extraItem.setDeskCount(deskCount);
                }
                extraItem.setShopcartItemType(shopcartItemType);
                extraItem.setTradeItemMainBatchRelList(tradeItemVo.getTradeItemMainBatchRelList());
                parent.getExtraItems().add(extraItem);
            } else {
                ReadonlySetmealShopcartItem setmealParent = setmealFinder.get(parentUuid);
                if (setmealParent != null) {
                    ReadonlyExtraShopcartItem extraItem = new ReadonlyExtraShopcartItem(tradeItem, setmealParent);
                    if (setmealParent.isGroupDish()) {
                        extraItem.setIsGroupDish(true);
                        extraItem.setDeskCount(deskCount);
                    }
                    extraItem.setShopcartItemType(shopcartItemType);
                    extraItem.setTradeItemMainBatchRelList(tradeItemVo.getTradeItemMainBatchRelList());
                    setmealParent.getExtraItems().add(extraItem);
                } else {
                    Log.e(TAG, "Not found the tradeItem's parent! tradeItem.uuid=" + tradeItem.getUuid());
                }
            }
        }
                for (TradeItem item : allReturnsMap) {
            item.setQuantity(BigDecimal.ZERO);
        }
        return items;
    }

    private static List<ReadonlyOrderProperty> buildProperties(TradeItemVo tradeItemVo) {
        if (Utils.isEmpty(tradeItemVo.getTradeItemPropertyList())) {
            return Collections.emptyList();
        }
        List<ReadonlyOrderProperty> properties = new ArrayList<ReadonlyOrderProperty>();
        for (TradeItemProperty tip : tradeItemVo.getTradeItemPropertyList()) {
            properties.add(new ReadonlyOrderProperty(tip));
        }
        return properties;
    }

    public IDinnertableTrade getiDinnertableTrade() {
        return iDinnertableTrade;
    }

    public void setiDinnertableTrade(IDinnertableTrade iDinnertableTrade) {
        this.iDinnertableTrade = iDinnertableTrade;
    }

    public List<DinnertableTradeInfo> getSubTradeInfoList() {
        return subTradeInfoList;
    }

    public void setSubTradeInfoList(List<DinnertableTradeInfo> subTradeInfoList) {
        this.subTradeInfoList = subTradeInfoList;
    }

}
