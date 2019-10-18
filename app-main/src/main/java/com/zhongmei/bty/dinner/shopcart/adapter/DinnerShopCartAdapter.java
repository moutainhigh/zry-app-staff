package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem.DishCheckStatus;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.utils.DinnerUtils;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.common.view.NumberEditText;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.ServingStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.orderdish.DinnerDishTradeInfoFragment;
import com.zhongmei.bty.dinner.orderdish.view.SlideListView;
import com.zhongmei.bty.dinner.table.DinnertableFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class DinnerShopCartAdapter extends SuperShopCartAdapter {

    private final static String TAG = DinnerShopCartAdapter.class.getSimpleName();

    private Drawable mDishServingIcon;

    private ListView parentListView;
    private PrintOperationOpType opType;
    private boolean isInDesk = false;        private boolean isDishItemCanDelete = true;

        private boolean isShowWake = true;
        private boolean isExistKds = false;
        private List<String> mSelectedUuids = new ArrayList<>();
        public List<Integer> mSelectPostions = new ArrayList<>();


    public DinnerShopCartAdapter(Context context) {
        init(context);
    }


    @Override
    public boolean isEnabled(int position) {
                boolean isCanOperate = false;
        int type = getItemViewType(position);
        if (type == DISH_TYPE || type == TITLE_CATEGORY || type == TITLE_TYPE) {
            isCanOperate = true;
        }
        return super.isEnabled(position) && isCanOperate;
    }

    @Override
    protected void setDishLayoutValue(DishDataItem item, ViewHolder holder) {
        super.setDishLayoutValue(item, holder);
        IShopcartItemBase shopcartItem = item.getBase();
        BigDecimal qty = ShopcartItemUtils.getDisplyQty(shopcartItem, deskCount);
        holder.dish_edit_num.setValue(qty);
        BigDecimal subCount = DinnerShoppingCart.getInstance().getSubTradeCount();
        holder.dish_edit_num.setInCreaseUnitOffset(getIncreaseUnit(shopcartItem).multiply(subCount), genStepNum(shopcartItem).multiply(subCount));
    }


    protected void init(Context context) {
        super.init(context);
        this.mDishServingIcon = context.getResources().getDrawable(R.drawable.dinner_dish_dishready_status);

        isExistKds = ShopInfoCfg.getInstance().isExistKdsDevice();
    }

    protected View loadDishLayout() {
        View convertView = LayoutInflater.from(context).inflate(R.layout.dinner_dish_shopcart_item, null);
        return convertView;
    }

    protected View initDishLayout(ViewHolder holder) {
        View convertView = loadDishLayout();
        initDishCommon(holder, convertView);
        initSlideLayout(holder, convertView);
        initDishOtherLayout(holder, convertView);
        initTimeView(holder, convertView);
        return convertView;
    }



    protected void showDishLayout(ViewHolder holder, final DishDataItem item, int position) {
        final IShopcartItemBase shopcartItem = item.getBase();
        holder.mainLayout.setAlpha(1f);

        updateDishCheck(holder, item);
        hideOperateTags(holder);        if (holder.issueTimeTv != null) {
            holder.issueTimeTv.setVisibility(View.INVISIBLE);
        }
        holder.tvWeighFlag.setVisibility(View.GONE);
        holder.dishView.setVisibility(View.VISIBLE);
        if (item.isCanEditNumber() && !isDishCheckMode) {
            holder.dish_num.setVisibility(View.GONE);
            holder.dish_edit_num.setVisibility(View.VISIBLE);
            holder.dish_edit_num.setChangeListener(new NumberEditText.ChangeListener() {
                @Override
                public void onNumberChanged(BigDecimal number) {
                    doSelectedItem("no");                    EventBus.getDefault().post(new ActionClose());                    if (shopcartItem instanceof ShopcartItem) {
                        ((ShopcartItem) shopcartItem).changeQty(number);
                    }
                    DinnerShoppingCart.getInstance().updateDinnerDish(shopcartItem, false);
                                    }
            });
        } else {
            holder.dish_num.setVisibility(View.VISIBLE);
            holder.dish_edit_num.setVisibility(View.GONE);
        }
        holder.dish_desc.setVisibility(View.GONE);
        setDishLayoutValue(item, holder);
        showDishBatServing(holder, item);
        switch (item.getType()) {
            case SINGLE:                showSingleOrComboDish(holder, item, shopcartItem, position, true);
                break;
            case COMBO:            case WEST_CHILD:
                showSingleOrComboDish(holder, item, shopcartItem, position, false);
                break;
            case CHILD:                showChildDish(holder, item);
                break;
            default:
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;
        }
        showDishDesc(holder, item, shopcartItem, item.getType());
        updateReturnOrModifyDishItem(holder, item);        showStand(item, holder);
        setTopLine(holder.topLine, item, position);
        setItemSelectedBg(holder, item);
        setDeleteIcon(item, holder);        setGray(holder, item);    }

    private void showWestComboIcon(ViewHolder viewHolder, ItemType itemType) {
        if (!DinnerUtils.isWestStyle() || itemType != ItemType.COMBO || isSlideDish || isDishCheckMode) {
            return;
        }
        viewHolder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        Drawable westDrawable = context.getResources().getDrawable(R.drawable.dinner_dish_west_comob_icon);
        viewHolder.dish_name.setCompoundDrawablesWithIntrinsicBounds(westDrawable, null, null, null);
    }


    private void showDishDesc(ViewHolder holder, DishDataItem item, IShopcartItemBase shopcartItem, ItemType itemType) {
        if (holder.dish_desc == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.dish_desc.getLayoutParams();
        layoutParams.setMargins(mDishUnSaveIcon.getIntrinsicWidth() + 5, 0, 0, 0);
        String seatNumber = "";
        if (shopcartItem.getTradeItemExtraDinner() != null && shopcartItem.getTradeItemExtraDinner().isValid()) {
            seatNumber = shopcartItem.getTradeItemExtraDinner().getSeatNumber();
        }
        if (itemType == ItemType.WEST_CHILD) {
            if (TextUtils.isEmpty(seatNumber)) {
                holder.dish_desc.setText(item.getItem().getSkuName());
            } else {
                holder.dish_desc.setText(seatNumber + "/" + item.getItem().getSkuName());
            }
            holder.dish_desc.setVisibility(View.VISIBLE);
        } else {
            if (TextUtils.isEmpty(seatNumber)) {
                holder.dish_desc.setVisibility(View.GONE);
            } else {
                holder.dish_desc.setVisibility(View.VISIBLE);
                holder.dish_desc.setText(seatNumber);
            }
        }
    }


    private void showSingleOrComboDish(ViewHolder holder, DishDataItem item, IShopcartItemBase shopcartItem,
                                       int position, boolean isSingle) {
        holder.topLine.setVisibility(View.VISIBLE);
        int dp5 = DensityUtil.dip2px(context, 5);
        holder.dishView.setPadding(dp5, dp5, dp5, dp5);
        holder.dish_name.setTextAppearance(context, R.style.dinnerOrderTextStyle);
        if (shopcartItem != null) {
            if (isInDesk)
                showIssueTime(holder, item.getItem(), item);            showRemindDish(holder, shopcartItem);
            if ((item.getType() == ItemType.WEST_CHILD || item.getType() == ItemType.CHILD)) {
                showOperateTag(holder, item, true);
            } else {
                showOperateTag(holder, item, false);            }
            if (isSingle && isExistKds && !item.isDishServing()) {                showKdsMakeStatus(holder, shopcartItem);
            }

            if (isBatchDiscountModle || isDishCheckMode) {
            } else {
                holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                resetIcon(shopcartItem, holder);

                                if (isInDesk && shopcartItem.getIssueStatus() != IssueStatus.PAUSE
                        && shopcartItem.getStatusFlag() != StatusFlag.INVALID
                        && shopcartItem.getInvalidType() != InvalidType.SPLIT
                        && !isWakeUp(shopcartItem)) {
                    resetServingIcon(item, shopcartItem, holder);
                    if (shopcartItem.getShopcartItemType() == null || shopcartItem.getShopcartItemType() != ShopcartItemType.MAINSUB) {                         if (shopcartItem.getTotalQty() != null && shopcartItem.getTotalQty().doubleValue() != 0) {
                            addSlideItems(position);                        }
                    }
                }
            }
        } else {
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(0);
        }
        updatePrintState(shopcartItem, holder);
    }


    protected void showChildDish(ViewHolder holder, DishDataItem item) {
        IShopcartItemBase shopcartItem = item.getBase();
        int dp5 = DensityUtil.dip2px(context, 5);
        holder.dishView.setPadding(dp5, dp5, dp5, dp5);
        holder.dish_name.setTextAppearance(context, R.style.dinnerMemoStyle);
        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(this.mChildIcon, null, null, null);
        holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        holder.dish_printstate.setVisibility(View.GONE);

        if (shopcartItem != null) {
            if (shopcartItem.getActualAmount() != null) {
                holder.dish_price.setText("");
                holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedComboDishPrice));
            } else {
                holder.dish_price.setText("");
                holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
            }
            showOperateTag(holder, item, true);
            showRemindDish(holder, shopcartItem);
        }
                if (isExistKds && !item.isDishServing()) {             showKdsMakeStatus(holder, shopcartItem);
        }
    }


    private boolean isWakeUp(IShopcartItemBase shopcartItem) {
        if (shopcartItem instanceof ReadonlyShopcartItem) {
            List<TradeItemOperation> tradeItemOperations = ((ReadonlyShopcartItem) shopcartItem).getTradeItemOperations();
            if (tradeItemOperations != null) {
                if (hasOperationOpType(tradeItemOperations, PrintOperationOpType.WAKE_UP) ) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean hasOperationOpType(List<TradeItemOperation> tradeItemOperations, PrintOperationOpType operationOpType) {
        for (TradeItemOperation operation : tradeItemOperations) {
            if (operation.getOpType() == operationOpType
                    && operation.getStatusFlag() == StatusFlag.VALID) {
                return true;
            }
        }
        return false;
    }


    private void showDishBatServing(final ViewHolder holder, final DishDataItem item) {
        holder.tv_dish_bat_serving.setVisibility(View.GONE);
        final IShopcartItemBase shopcartItem = item.getBase();
        if (!(isBatchDiscountModle || isDishCheckMode)) {
                        if (isInDesk
                    && shopcartItem.getIssueStatus() != IssueStatus.PAUSE
                    && shopcartItem.getStatusFlag() != StatusFlag.INVALID
                    && shopcartItem.getInvalidType() != InvalidType.SPLIT) {
                if (shopcartItem.getTotalQty() != null && shopcartItem.getTotalQty().doubleValue() != 0) {
                    if (item.isDishServing()) {
                        holder.tv_dish_bat_serving.setVisibility(View.VISIBLE);
                        holder.tv_dish_bat_serving.setText(context.getString(R.string.dish_bat_serving_all));
                    } else {
                        if (shopcartItem instanceof ReadonlyShopcartItemBase) {
                            showDishBatServingPart(((ReadonlyShopcartItemBase) shopcartItem).getKdsScratchDishQty(), item, holder, shopcartItem);
                        }
                    }
                }
            }
        }
    }

    private void showDishBatServingPart(BigDecimal kdsScratchDishQty, DishDataItem item, ViewHolder holder, IShopcartItemBase shopcartItem) {
        if (kdsScratchDishQty == null || kdsScratchDishQty.compareTo(BigDecimal.ZERO) <= 0) {
            holder.tv_dish_bat_serving.setVisibility(View.GONE);
        } else {
            switch (item.getType()) {
                case SINGLE:                    if (shopcartItem.getSaleType() == SaleType.WEIGHING) {
                                                                    } else {
                        BigDecimal singleQty = shopcartItem.getSingleQty();
                        BigDecimal surplusQty = singleQty.subtract(kdsScratchDishQty);
                        if (surplusQty.compareTo(BigDecimal.ZERO) <= 0) {
                                                                                } else {
                            holder.tv_dish_bat_serving.setVisibility(View.VISIBLE);
                            holder.tv_dish_bat_serving.setText(String.format(context.getString(R.string.dish_bat_serving), MathDecimal.toTrimZeroString(kdsScratchDishQty), MathDecimal.toTrimZeroString(surplusQty)));
                        }
                    }
                    break;
                case COMBO:                                        BigDecimal singleQty = shopcartItem.getSingleQty();
                    BigDecimal surplusQty = singleQty.subtract(kdsScratchDishQty);
                    if (surplusQty.compareTo(BigDecimal.ZERO) <= 0) {
                                                                    }
                    break;
            }
        }
    }

    public void updateGroupSelectData(List<IShopcartItem> dataList, TradeVo tradeVo) {
        updateGroupSelectData(dataList, tradeVo, false);
        initialDishCheckStatus();
    }


    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        initCommonData(tradeVo);
        if (tradeVo.isUnionMainTrade()) {
            updateUnionDeskData(dataList, tradeVo);
        } else {
            updateSigleDeskData(dataList);
        }
        initialDishCheckStatus();        updateTrade(tradeVo, isShowInvalid);        initialRelateDishInfo();    }


    private void updateSigleDeskData(List<IShopcartItem> dataList) {
        ShopCartDataVo unSaveVo = new ShopCartDataVo(null);        ShopCartDataVo unPrintVo = new ShopCartDataVo(null);        ShopCartDataModel printModel = new ShopCartDataModel();
        if (dataList != null && dataList.size() > 0) {
                        for (int i = 0; i < dataList.size(); i++) {

                IShopcartItem shopCartItem = dataList.get(i);

                if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                        && (shopCartItem.getInvalidType() != InvalidType.SPLIT)
                        && (shopCartItem.getInvalidType() != InvalidType.RETURN_QTY)
                        && (shopCartItem.getInvalidType() != InvalidType.MODIFY_DISH)) {
                    continue;
                }

                                sumAllDishCount(shopCartItem);
                                if (shopCartItem.getId() == null) {

                    unSaveVo.addData(shopCartItem);
                } else {
                                        if (TextUtils.isEmpty(shopCartItem.getBatchNo())) {
                        unPrintVo.addData(shopCartItem);
                    } else {
                        printModel.addData(shopCartItem.getBatchNo(), shopCartItem);
                    }
                }
            }
        }
                if (!unSaveVo.isEmpty()) {
            DishDataItem item = new DishDataItem(ItemType.LABEL_UNSAVE);
            item.setName(context.getString(R.string.dinner_cart_unsave_label));
            data.add(item);
                        createItems(unSaveVo.getData(), data);


        }
                if (!unPrintVo.isEmpty()) {
            DishDataItem item = new DishDataItem(ItemType.LABEL_SAVE_UNPRINTED);
            item.setName(context.getString(R.string.dinner_cart_save_unprint_label));
            data.add(item);
            createItems(unPrintVo.getData(), data);
        }
                if (!printModel.isEmpty()) {
            Map<String, ShopCartDataVo> map = printModel.getData();
            for (String key : map.keySet()) {
                DishDataItem item = new DishDataItem(ItemType.LABEL_SAVE_PRINTED);
                item.setName(context.getString(R.string.dinner_cart_save_print_label) + "-" + key);
                data.add(item);
                createItems(map.get(key).getData(), data);
            }
        }
    }


    private void updateUnionDeskData(List<IShopcartItem> dataList, TradeVo tradeVo) {
        if (Utils.isEmpty(dataList)) {
            return;
        }

        List<IShopcartItem> mainList = new ArrayList<>();
        Map<Long, TradeTable> tradeTableMap = tradeVo.getSubTableMap();
                Map<Long, List<IShopcartItem>> tableItemMap = new LinkedHashMap<>();
        for (int i = dataList.size() - 1; i >= 0; i--) {
            IShopcartItem shopCartItem = dataList.get(i);
            if (shopCartItem.getStatusFlag() == StatusFlag.INVALID
                    && (shopCartItem.getInvalidType() != InvalidType.SPLIT)
                    && (shopCartItem.getInvalidType() != InvalidType.RETURN_QTY)
                    && (shopCartItem.getInvalidType() != InvalidType.MODIFY_DISH)) {
                continue;
            }
                        sumAllDishCount(shopCartItem);
            if (shopCartItem.getShopcartItemType() == ShopcartItemType.MAINBATCH || shopCartItem.getShopcartItemType() == ShopcartItemType.SUBBATCH) {
                mainList.add(shopCartItem);
            } else {
                                List singleDeskList = tableItemMap.get(shopCartItem.getTradeTableId());
                if (singleDeskList == null) {
                    singleDeskList = new ArrayList();
                    tableItemMap.put(shopCartItem.getTradeTableId(), singleDeskList);
                }
                singleDeskList.add(shopCartItem);
            }
        }

                if (!mainList.isEmpty()) {
            DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
            item.setName(context.getResources().getString(R.string.dinner_union_trade));
            data.add(item);
            createItems(mainList, data);
        }

        if (tableItemMap.size() <= 0) {
            return;
        }
        Iterator<Long> iterator = tableItemMap.keySet().iterator();
        while (iterator.hasNext()) {
            Long tradeTableId = iterator.next();
            List<IShopcartItem> shopcartItemList = tableItemMap.get(tradeTableId);
            if (Utils.isNotEmpty(shopcartItemList)) {
                DishDataItem item = new DishDataItem(ItemType.TITLE_ITEM);
                if (tradeTableMap != null) {
                    TradeTable tradeTable = tradeTableMap.get(tradeTableId);
                    if (tradeTable != null) {
                        item.setName(tradeTable.getTableName());
                        data.add(item);
                    }
                }
                createItems(shopcartItemList, data);
            }
        }

    }


    public DishDataItem getDishDataItem(ArrayList<DishDataItem> dishDataItems, Long tradeItemId) {
        if (dishDataItems != null) {
            for (DishDataItem item : dishDataItems) {
                IShopcartItemBase shopcartItem = item.getBase();
                if (shopcartItem != null && tradeItemId != null && tradeItemId.equals(shopcartItem.getId())) {
                    return item;
                }
            }
        }
        return null;
    }


    protected void updateTrade(TradeVo tradeVo, boolean isShowUnActive) {
                if (DinnerDishTradeInfoFragment.isMoveDishDragMode()) {
            return;
        }
                if (tradeVo != null && tradeVo.getTrade() != null) {
            buildTradeMemo(tradeVo);
        }
    }


    public void refreshSelectedItems() {
        if (Utils.isNotEmpty(mSelectedUuids)) {
            doSelectedItems(new ArrayList<>(mSelectedUuids));
        }
    }


    public DishDataItem doSelectedItem(String selectedUuid) {
        List<DishDataItem> dishDataItems = doSelectedItems(Utils.asList(selectedUuid));
        if (Utils.isNotEmpty(dishDataItems)) {
            return dishDataItems.get(0);
        } else {
            return null;
        }
    }

    public void doSelectedTitleItme(long id) {
                mSelectedUuids.clear();
        mSelectPostions.clear();
        List<DishDataItem> data = getAllData();
        if (Utils.isEmpty(data)) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            DishDataItem dishDataItem = data.get(i);
            if (dishDataItem.getType() == ItemType.TITLE_ITEM && id == dishDataItem.getDishTypeId()) {
                dishDataItem.setSelected(true);
            } else {
                dishDataItem.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }


    public List<DishDataItem> doSelectedItems(List<String> selectedUuids) {
                mSelectedUuids.clear();
        mSelectPostions.clear();

        List<DishDataItem> data = getAllData();
        if (Utils.isEmpty(data)) {
            return null;
        }
        List<DishDataItem> selectedItems = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            DishDataItem dishDataItem = data.get(i);
            if (dishDataItem.getBase() != null && selectedUuids.contains(dishDataItem.getBase().getUuid())
                    && (dishDataItem.getType() == ItemType.SINGLE || dishDataItem.getType() == ItemType.COMBO
                    || dishDataItem.getType() == ItemType.CHILD || dishDataItem.getType() == ItemType.WEST_CHILD)) {
                dishDataItem.setSelected(true);
                selectedItems.add(dishDataItem);
                mSelectPostions.add(i);
                mSelectedUuids.add(dishDataItem.getBase().getUuid());
            } else {
                dishDataItem.setSelected(false);
            }
        }
        notifyDataSetChanged();
        return selectedItems;
    }


    public void changeCateSelecteCount(DishDataItem item) {
        Long changedItemTypeId = getDishTypeId(item);
        if (changedItemTypeId == null) {
            return;
        }
                int typeSelectedCount = 0;
        DishDataItem categoryItem = null;
        boolean isHasUnChecked = false;
        for (DishDataItem dishDataItem : data) {
            if (dishDataItem.getType() == ItemType.TITLE_CATEGORY && dishDataItem.getDishTypeId() != null && (dishDataItem.getDishTypeId().compareTo(changedItemTypeId) == 0)) {
                categoryItem = dishDataItem;
            }
            if ((dishDataItem.getType() == ItemType.SINGLE || dishDataItem.getType() == ItemType.COMBO || dishDataItem.getType() == ItemType.WEST_CHILD) &&
                    dishDataItem.getItem() != null
                    && (getDishTypeId(dishDataItem) != null && getDishTypeId(dishDataItem).compareTo(changedItemTypeId) == 0)
                    ) {
                if (dishDataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
                    typeSelectedCount++;
                } else if (dishDataItem.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                    isHasUnChecked = true;
                }
            }
        }
        if (categoryItem != null) {
            categoryItem.setCount(typeSelectedCount);
            if (!isHasUnChecked) {
                categoryItem.setCheckStatus(DishCheckStatus.CHECKED);
            } else {
                categoryItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
            }
        }
    }


    public List<DishDataItem> getDishDataItemsByCate(DishDataItem item) {
        Long changedItemTypeId = item.getDishTypeId();
        List<DishDataItem> dishDataItemList = new ArrayList<>();
        if (changedItemTypeId == null) {
            return dishDataItemList;
        }
                DishDataItem categoryItem = null;
        boolean isHasUnChecked = false;
        for (DishDataItem dishDataItem : data) {
            if (dishDataItem.getType() == ItemType.TITLE_ITEM && (dishDataItem.getDishTypeId().compareTo(changedItemTypeId) == 0)) {
                categoryItem = dishDataItem;
            }
            if ((dishDataItem.getType() == ItemType.SINGLE || dishDataItem.getType() == ItemType.WEST_CHILD) &&
                    dishDataItem.getBase() != null
                    && (dishDataItem.getDishTypeId() != null && dishDataItem.getDishTypeId().compareTo(changedItemTypeId) == 0)
                    ) {
                dishDataItemList.add(dishDataItem);
            }
        }
        return dishDataItemList;
    }

    private Long getDishTypeId(DishDataItem item) {
        if (item.getItem() == null) {
            return null;
        }
        Long dishTypeId = null;
        if (item.getBase().getDishShop() != null) {
            dishTypeId = item.getItem().getDishShop().getDishTypeId();
        }
        return dishTypeId;
    }


    public void clearAllSelected() {
        mSelectedUuids.clear();
        List<DishDataItem> data = getAllData();
        if (data == null || data.size() == 0)
            return;
        for (DishDataItem dishDataItem : data) {
            dishDataItem.setSelected(false);
        }
        notifyDataSetChanged();
    }



    private void showRemindDish(ViewHolder holder, IShopcartItemBase shopcartItem) {
        if (isShowWake && (shopcartItem instanceof IShopcartItem || shopcartItem instanceof ReadonlySetmealShopcartItem)) {
            List<TradeItemOperation> tradeItemOperations = shopcartItem.getTradeItemOperations();
            int remindDishCount = 0;
            if (tradeItemOperations != null && !tradeItemOperations.isEmpty()) {
                for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                    if (tradeItemOperation.getOpType() == PrintOperationOpType.REMIND_DISH) {
                        remindDishCount++;
                    }
                }
            }
            if (remindDishCount > 0) {
                holder.tv_remind_dish.setVisibility(View.VISIBLE);
                if (remindDishCount == 1) {
                    holder.tv_remind_dish.setText(context.getResources().getString(R.string.remind_dish));
                } else {
                    holder.tv_remind_dish.setText(context.getResources().getString(R.string.remind_dish) + "X" + remindDishCount);
                }

                return;
            }
        }

        holder.tv_remind_dish.setVisibility(View.GONE);
    }


    private void showKdsMakeStatus(ViewHolder holder, IShopcartItemBase shopcartItem) {
        if (isShowWake && shopcartItem instanceof ReadonlyShopcartItemBase) {
            TradeItemExtra tradeItemExtra = ((ReadonlyShopcartItemBase) shopcartItem).getTradeItemExtra();
            if (tradeItemExtra != null && tradeItemExtra.getDishMakeStatus() != null) {
                switch (tradeItemExtra.getDishMakeStatus()) {
                    case WAITING:
                    case MATCHING:
                    case MAKING:
                        holder.tv_make_status.setBackgroundResource(R.drawable.kds_dish_unfinish_bg);
                        holder.tv_make_status.setTextColor(context.getResources().getColor(R.color.kds_dish_unfinish_color));
                        break;
                    case FINISHED:
                        holder.tv_make_status.setBackgroundResource(R.drawable.kds_dish_finish_bg);
                        holder.tv_make_status.setTextColor(context.getResources().getColor(R.color.kds_dish_finish_color));
                        break;
                    case CANCELED:
                        holder.tv_make_status.setBackgroundResource(R.drawable.kds_dish_cancel_bg);
                        holder.tv_make_status.setTextColor(context.getResources().getColor(R.color.text_hint_color));
                        break;
                }
                String desc = tradeItemExtra.getDishMakeStatus().desc();
                if (!TextUtils.isEmpty(desc)) {
                    holder.tv_make_status.setVisibility(View.VISIBLE);
                    holder.tv_make_status.setText(desc);

                    return;
                }
            }
        }

        holder.tv_make_status.setVisibility(View.GONE);
    }



    private void setItemSelectedBg(ViewHolder holder, DishDataItem item) {
        if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO
                || item.getType() == ItemType.CHILD || item.getType() == ItemType.WEST_CHILD) && item.isSelected()) {
            holder.dishView.setBackgroundResource(R.drawable.order_dish_item_shape);
        } else {
            holder.dishView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setParentView(ListView listView) {
        parentListView = listView;
    }

    private void addSlideItems(int position) {
        if (parentListView == null)
            return;
        if (parentListView instanceof SlideListView) {
            SlideListView slideListView = (SlideListView) parentListView;
            slideListView.addSlideItems(position);
        }
    }


    private void resetServingIcon(DishDataItem item, IShopcartItemBase shopcartItem, ViewHolder holder) {
        if (item.isDishServing()) {
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDishServingIcon, null, null, null);
            holder.slideStatusIv.setImageResource(R.drawable.dinner_orderdish_dish_not_ready_icon);
            holder.slideStatusTv.setText(R.string.dinner_table_info_dishservice_status1);
        } else {
            holder.slideStatusIv.setImageResource(R.drawable.dinner_orderdish_dish_ready_icon);
            holder.slideStatusTv.setText(R.string.dinner_table_info_dishservice_status0);
        }
    }


    public boolean refreshItem(int position) {
        if (data == null)
            return false;
        if (position <= data.size() - 1) {                        final DishDataItem item = data.get(position);
            if (item.getType() == ItemType.WEST_CHILD) {
                ToastUtil.showLongToast(context.getResources().getString(R.string.dinner_child_cannot_slide));
                return false;
            }
            if (item.getBase().getShopcartItemType() == ShopcartItemType.MAINSUB) {
                ToastUtil.showLongToast(context.getResources().getString(R.string.dinner_child_main_sub_cannot_slide));                 return false;
            }
            item.setDishServing(!item.isDishServing());
            notifyDataSetChanged();
        }
        return true;

    }


    protected void initialServingStatus() {
        if (data == null || data.size() == 0)
            return;
        for (DishDataItem item : data) {
            if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) {
                final IShopcartItemBase shopcartItem = item.getBase();
                item.setDishServing(shopcartItem.getServingStatus() == ServingStatus.SERVING ? true : false);
            }
        }
    }


    public void cancelServingStatusOperate() {
        initialServingStatus();
        notifyDataSetChanged();
    }


    protected void resetSlideItems() {
        if (parentListView == null)
            return;
        if (parentListView instanceof SlideListView) {
            SlideListView slideListView = (SlideListView) parentListView;
            slideListView.removeSlideItems();
        }
    }


    void initialRelateDishInfo() {
        if (data == null || data.size() == 0)
            return;
        HashMap<String, DishDataItem> itemFinder = new HashMap<String, DishDataItem>();
        for (DishDataItem item : data) {
            if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO) {
                IShopcartItemBase itemBase = item.getBase();
                if (itemBase != null) {
                    itemFinder.put(itemBase.getUuid(), item);
                }
            }

        }

        for (DishDataItem item : itemFinder.values()) {
            IShopcartItemBase itemBase = item.getBase();
            if (!TextUtils.isEmpty(itemBase.getRelateTradeItemUuid())) {
                DishDataItem relateItem = itemFinder.get(itemBase.getRelateTradeItemUuid());
                if (relateItem != null) {
                    relateItem.setCanEditNumber(false);
                    item.setRelateItem(relateItem);
                    item.setCanEditNumber(false);
                }
            }
        }
    }


    protected void updateReturnOrModifyDishItem(ViewHolder holder, final DishDataItem item) {
                if (item.isReturnDishItem() || item.isModifyDishItem()) {
            holder.dish_name.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            holder.dish_name.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        }
        DishDataItem relateItem = item.getRelateItem();
        if (relateItem != null && (relateItem.getType() == ItemType.SINGLE || relateItem.getType() == ItemType.COMBO)
                && relateItem.getBase() != null) {
            IShopcartItemBase relateShopcartItem = relateItem.getBase();
            if (relateShopcartItem.getInvalidType() == InvalidType.RETURN_QTY) {
                String returnQty = MathDecimal.toTrimZeroString(relateShopcartItem.getReturnQty().negate());                String originalQty = MathDecimal.toTrimZeroString(relateShopcartItem.getSingleQty());                holder.returnDishLL.setVisibility(View.VISIBLE);
                holder.returnDishQuantityTv.setText(context.getString(R.string.dinner_order_dish_return_dish_quantity,
                        originalQty, returnQty));
                if (relateShopcartItem.getReturnQtyReason() != null) {
                    holder.returnDishReasonTv.setText(context.getString(R.string.dinner_order_dish_return_dish_reason,
                            relateShopcartItem.getReturnQtyReason().getReasonContent()));
                } else {
                    holder.returnDishReasonTv.setText("");
                }
            } else if (relateShopcartItem.getInvalidType() == InvalidType.MODIFY_DISH) {
                holder.returnDishLL.setVisibility(View.VISIBLE);
                holder.returnDishQuantityTv.setText(R.string.modify_dish);
                holder.returnDishReasonTv.setText("");            } else {
                holder.returnDishLL.setVisibility(View.GONE);
            }
        } else {
            holder.returnDishLL.setVisibility(View.GONE);
        }


    }



    public void showDishCheckMode(boolean isCheckMode, PrintOperationOpType type) {
        isDishCheckMode = isCheckMode;
        opType = type;
        notifyDataSetChanged();
    }


    public void checkAllDish(boolean check) {
        checkAllDish(check, false);
    }


    public boolean isCheckedAll() {
        if (data == null || data.size() == 0)
            return false;

        boolean isCheckAll = false;
        for (DishDataItem item : data) {
            if (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO || item.getType() == ItemType.WEST_CHILD) {
                if (item.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                    isCheckAll = false;
                    break;
                } else if (item.getCheckStatus() == DishCheckStatus.CHECKED) {
                    isCheckAll = true;
                }
            }
        }
        return isCheckAll;
    }


    public void doSelectedByType(DishDataItem item) {
        int selectedCount = 0;
        if (item.getDishTypeId() == null || typeMap == null) {
            return;
        }
        List<IShopcartItem> iShopcartItemList = typeMap.get(item.getDishTypeId());
        if (iShopcartItemList == null) {
            iShopcartItemList = singleTypeMap.get(item.getDishTypeId());
        }
        if (iShopcartItemList == null) {
            return;
        }
        Map<String, IShopcartItem> typeShopcartMap = new HashMap<>();
        for (IShopcartItem iShopcartItem : iShopcartItemList) {
            typeShopcartMap.put(iShopcartItem.getUuid(), iShopcartItem);
        }
        for (DishDataItem dishDataItem : data) {
            if (dishDataItem.getItem() == null || !typeShopcartMap.containsKey(dishDataItem.getBase().getUuid()) || dishDataItem.getCheckStatus() == DishCheckStatus.INVALIATE_CHECK) {
                continue;
            }
            if (item.getCheckStatus() == DishCheckStatus.CHECKED) {
                dishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
                selectedCount++;
            } else {
                dishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
            }
        }
        item.setCount(selectedCount);
    }



    private byte getDishOperateTag(ViewHolder holder, final DishDataItem item, boolean isChild) {
        byte result = 0;
        List<TradeItemOperation> tradeItemOperations = item.getItem().getTradeItemOperations();
        if (isChild)
            tradeItemOperations = item.getBase().getTradeItemOperations();
        if (Utils.isNotEmpty(tradeItemOperations)) {
            for (TradeItemOperation temp : tradeItemOperations) {
                if (temp.getStatusFlag() != StatusFlag.VALID)
                    continue;
                if (temp.getOpType() == PrintOperationOpType.WAKE_UP) {
                    result = 1;
                    break;
                } else if (temp.getOpType() == PrintOperationOpType.RISE_DISH) {
                    result = 2;
                    break;
                } else if (temp.getOpType() == PrintOperationOpType.WAKE_UP_CANCEL) {
                    result = 3;
                    break;
                } else if (temp.getOpType() == PrintOperationOpType.RISE_DISH_CANCEL) {
                    result = 4;
                    break;
                }
            }
        }
        return result;

    }


    protected void showOperateTag(ViewHolder holder, final DishDataItem item, boolean isChild) {
        byte tag = getDishOperateTag(holder, item, isChild);
        if (tag == 0 || !isShowWake) {
            holder.dishPrepareTv.setVisibility(View.GONE);
            holder.dishMakeTv.setVisibility(View.GONE);
            holder.dishPrepareCancelTv.setVisibility(View.GONE);
            holder.dishMakeCancelTv.setVisibility(View.GONE);
        } else if (tag == 1) {
            holder.dishPrepareTv.setVisibility(View.VISIBLE);
            holder.dishMakeTv.setVisibility(View.GONE);
            holder.dishPrepareCancelTv.setVisibility(View.GONE);
            holder.dishMakeCancelTv.setVisibility(View.GONE);
        } else if (tag == 2) {
            holder.dishPrepareTv.setVisibility(View.GONE);
            holder.dishMakeTv.setVisibility(View.VISIBLE);
            holder.dishPrepareCancelTv.setVisibility(View.GONE);
            holder.dishMakeCancelTv.setVisibility(View.GONE);
        } else if (tag == 3) {
            holder.dishPrepareTv.setVisibility(View.GONE);
            holder.dishMakeTv.setVisibility(View.GONE);
            holder.dishPrepareCancelTv.setVisibility(View.VISIBLE);
            holder.dishMakeCancelTv.setVisibility(View.GONE);
        } else if (tag == 4) {
            holder.dishPrepareTv.setVisibility(View.GONE);
            holder.dishMakeTv.setVisibility(View.GONE);
            holder.dishPrepareCancelTv.setVisibility(View.GONE);
            holder.dishMakeCancelTv.setVisibility(View.VISIBLE);
        }

    }


    public void initialDishCheckStatus() {
        if (!isDishCheckMode) {
            return;
        }
                if (DinnerDishTradeInfoFragment.isMoveDishDragMode()) {
            return;
        }
        if (Utils.isEmpty(data)) {
            return;
        }
        Map<Long, DishDataItem> categoryMap = new HashMap<>();
        Map<Long, List<DishDataItem>> typeItemMap = new HashMap<>();
        for (DishDataItem dataItem : data) {
            if (dataItem.getType() == ItemType.TITLE_CATEGORY) {
                categoryMap.put(dataItem.getDishTypeId(), dataItem);
                continue;
            }
            if (dataItem.getItem() == null || dataItem.getItem().getDishShop() == null) {
                continue;
            }
            if (dataItem.getType() == ItemType.SINGLE || dataItem.getType() == ItemType.COMBO || dataItem.getType() == ItemType.WEST_CHILD) {
                dataItem.setCheckStatus(DinnerTradeItemManager.getInstance().getDishCheckStatus(dataItem, opType));
                Long typeId = dataItem.getItem().getDishShop().getDishTypeId();
                List<DishDataItem> itemList;
                if (typeItemMap.get(typeId) == null) {
                    itemList = new ArrayList<DishDataItem>();
                    typeItemMap.put(typeId, itemList);
                } else {
                    itemList = typeItemMap.get(typeId);
                }
                itemList.add(dataItem);
            }
        }

        for (Long key : typeItemMap.keySet()) {
            List<DishDataItem> itemList = typeItemMap.get(key);
            if (Utils.isEmpty(itemList)) {
                continue;
            }
            DishDataItem typeDishDataItem = categoryMap.get(key);
            if (typeDishDataItem == null) {
                continue;
            }
            int selectedCount = 0;
            boolean isHasNotCheck = false;
            for (DishDataItem item : itemList) {
                if (item.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                    isHasNotCheck = true;
                    typeDishDataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
                } else if (item.getCheckStatus() == DishCheckStatus.CHECKED) {
                    selectedCount++;
                }
            }
            typeDishDataItem.setCount(selectedCount);
            if (!isHasNotCheck && selectedCount > 0)
                typeDishDataItem.setCheckStatus(DishCheckStatus.CHECKED);
        }
    }

    public List<DishDataItem> getCheckItems() {
        List<DishDataItem> selectedItems = new ArrayList<DishDataItem>();
        if (data != null && !data.isEmpty()) {
            for (DishDataItem item : data) {
                if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO || item.getType() == ItemType.WEST_CHILD)
                        && item.getCheckStatus() == DishCheckStatus.CHECKED) {
                    selectedItems.add(item);
                }
            }
        }

        return selectedItems;
    }

    public PrintOperationOpType getOpType() {
        return opType;
    }

    public void setOpType(PrintOperationOpType opType) {
        this.opType = opType;
    }


    private void hideOperateTags(ViewHolder holder) {
        holder.dishPrepareTv.setVisibility(View.GONE);
        holder.dishMakeTv.setVisibility(View.GONE);
        holder.tv_remind_dish.setVisibility(View.GONE);
        holder.tv_make_status.setVisibility(View.GONE);
    }



    public List<IShopcartItem> initialMoveDishDragModeCheckStatus2() {
        if (!isDishCheckMode) {
            return null;
        }

        if (Utils.isEmpty(data)) {
            return null;
        }
        ArrayList<IShopcartItem> shopcartItems = new ArrayList<IShopcartItem>();
        for (DishDataItem dataItem : data) {
            if (dataItem.getType() == ItemType.SINGLE || dataItem.getType() == ItemType.COMBO) {
                if (dataItem.getCheckStatus() == DishCheckStatus.CHECKED) {
                    shopcartItems.add(dataItem.getItem());
                }

            }
        }
        return shopcartItems;
    }

    public void initialMoveDishCheckStatus() {
        if (!DinnerDishTradeInfoFragment.isMoveDishDragMode()) {
            return;
        }
        initDishCheckStatus();
    }


    public void initDishCheckStatus() {
        if (!isDishCheckMode) {
            return;
        }
        if (Utils.isEmpty(data)) {
            return;
        }

        for (DishDataItem dataItem : data) {
            if (dataItem.getType() == ItemType.SINGLE || dataItem.getType() == ItemType.COMBO || dataItem.getType() == ItemType.WEST_CHILD) {
                final IShopcartItem shopcartItem = dataItem.getItem();
                if (shopcartItem.getStatusFlag() == StatusFlag.VALID
                        && shopcartItem.getTotalQty().doubleValue() != 0) {
                    dataItem.setCheckStatus(DishCheckStatus.NOT_CHECK);
                } else {
                    dataItem.setCheckStatus(DishCheckStatus.INVALIATE_CHECK);
                }
            }
        }
    }

    private void checkAllDish(boolean check, boolean isMoveDish) {
        if (data == null || data.isEmpty()) {
            return;
        }
        Map<Long, DishDataItem> categoryMap = new HashMap<>();
        for (DishDataItem item : data) {
            if (item.getType() == ItemType.TITLE_CATEGORY) {
                categoryMap.put(item.getDishTypeId(), item);
                item.setCount(0);
                if (check) {
                    item.setCheckStatus(DishCheckStatus.CHECKED);
                } else {
                    item.setCheckStatus(DishCheckStatus.NOT_CHECK);
                }
                continue;
            }
            if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO || item.getType() == ItemType.WEST_CHILD) && item.getItem() != null) {
                Long dishTypeId = null;
                if (item.getItem().getDishShop() != null) {
                    dishTypeId = item.getItem().getDishShop().getDishTypeId();
                }
                if (check) {
                    if (item.getCheckStatus() == DishCheckStatus.NOT_CHECK) {
                        if (dishTypeId != null && categoryMap.get(dishTypeId) != null) {
                            DishDataItem categoryDataItem = categoryMap.get(dishTypeId);
                            categoryDataItem.setCount(categoryDataItem.getCount() + 1);
                        }
                        item.setCheckStatus(DishCheckStatus.CHECKED);
                        if (!isMoveDish && getOpType() != null) {
                        }
                    }
                } else {
                    if (item.getCheckStatus() == DishCheckStatus.CHECKED) {
                        item.setCheckStatus(DishCheckStatus.NOT_CHECK);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }


    public void checkAllDishForMoveDish(boolean check) {
        checkAllDish(check, true);
    }


    private void showIssueTime(ViewHolder holder, IShopcartItem shopcartItem, DishDataItem item) {
        if (SharedPreferenceUtil.getSpUtil().getBoolean(DinnertableFragment.SHOW_ISSUE_TIME_KEY, false)) {
            boolean isQuntityZero = shopcartItem.getSingleQty().compareTo(new BigDecimal(0)) == 0 ? true : false;

            if (shopcartItem.getId() != null
                    && !TextUtils.isEmpty(shopcartItem.getBatchNo())
                    && shopcartItem.getStatusFlag() == StatusFlag.VALID
                    && !item.isDishServing()
                    && !isQuntityZero) {
                String issueTime = getIssueTime(shopcartItem);
                if (issueTime == null) {
                    holder.issueTimeTv.setVisibility(View.INVISIBLE);
                } else {
                    holder.issueTimeTv.setVisibility(View.VISIBLE);
                    holder.issueTimeTv.setText(issueTime);
                }

            } else {
                holder.issueTimeTv.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.issueTimeTv.setVisibility(View.INVISIBLE);
        }


    }


    private String getIssueTime(IShopcartItem shopcartItem) {
        Log.i("zhubo", "获取出单时间");
        if (shopcartItem instanceof ReadonlyShopcartItem) {
            ReadonlyShopcartItem readonlyShopcartItem = (ReadonlyShopcartItem) shopcartItem;
            List<TradeItemOperation> operations = readonlyShopcartItem.getTradeItemOperations();
            long serverUpdateTime = 0;
            if (operations != null && operations.size() != 0) {
                for (TradeItemOperation temp : operations) {
                    if (temp.getOpType() == PrintOperationOpType.KITCHEN_PRINT && temp.getPrintStatus() == PrintStatus.FINISHED) {
                        if (temp.getServerUpdateTime() > serverUpdateTime) {
                            serverUpdateTime = temp.getServerUpdateTime();
                        }
                    }
                }
            }

            if (serverUpdateTime == 0) {
                return null;
            }

            final long issueTime = (System.currentTimeMillis() - serverUpdateTime) / 1000;
            String result = null;
            if (issueTime < 1 * 60 * 60) {
                result = issueTime / 60 + min;

            } else if (issueTime < 24 * 60 * 60) {
                result = issueTime / (60 * 60) + hour;
            } else {
                result = issueTime / (60 * 60 * 24) + day;
            }
            return result;
        }
        return null;
    }

    public void setInDesk(boolean inDesk) {
        isInDesk = inDesk;
    }

    public boolean isDishItemCanDelete() {
        return isDishItemCanDelete;
    }

    public void setDishItemCanDelete(boolean dishItemCanDelete) {
        isDishItemCanDelete = dishItemCanDelete;
    }


    public boolean isShowWake() {
        return isShowWake;
    }

    public void setShowWake(boolean showWake) {
        isShowWake = showWake;
    }

}
