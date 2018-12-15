package com.zhongmei.beauty.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.beauty.utils.TradeUserUtil;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.common.view.NumberEditText;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerBanlanceAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyShopcartAdapter extends DinnerBanlanceAdapter {
    //当前选中的菜品
    private List<String> mSelectedUuids = new ArrayList<>();
    public List<Integer> mSelectPostions = new ArrayList<>();

    public BeautyShopcartAdapter(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        IMAGEMARGINRIGHT = 10;
        this.mDishUnSaveIcon = context.getResources().getDrawable(R.drawable.beauty_shopcart_pro_anchor);
        this.mDishUnPrintedIcon = context.getResources().getDrawable(R.drawable.beauty_shopcart_pro_anchor);
        this.mDishDiscountAllIcon = context.getResources().getDrawable(R.drawable.beauty_discount_selected);
        this.mDishNoDiscountIcon = context.getResources().getDrawable(R.drawable.beauty_discount_unselected);
        this.mCouponUnEnabledIcon = context.getResources().getDrawable(R.drawable.beauty_discount_unactive);
        this.mCouponEnabledIcon = context.getResources().getDrawable(R.drawable.beauty_discount_icon);
    }

    protected View loadDishLayout() {
        View convertView = LayoutInflater.from(context).inflate(R.layout.beauty_dish_shopcart_item_balance, null);
        return convertView;
    }

    protected void showDishLayout(ViewHolder holder, final DishDataItem item, int position) {
        super.showDishLayout(holder, item, position);
        showNumEditView(item, holder);
        setItemSelectedBg(holder, item);
    }

    protected void showDiscountDrawable(ViewHolder holder, DishDataItem item) {
        if (isBatchDiscountModle && (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)) {
            // 单菜或套餐可以批量打折
            // 批量赠送界面不处理不打折商品
            if (item.getBase() != null) {
                // 有营销活动或者后台设置了不能手动折扣，就不能批量折扣
                if (DinnerCashManager.hasMarketActivity(tradeItemPlanActivityMap, item.getBase())
                        || (!isBatchCoercionModel && item.getBase().getEnableWholePrivilege() == Bool.NO) || isCardService(item.getBase()) || isApplet(item.getBase())) {
                    Drawable checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_cannot_discount);
                    holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);
                } else {
                    itemSelect(item, holder);
                }
                // 菜品不能参与整单折扣
            } else {
                Drawable checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_cannot_discount);
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);
            }
        }
    }

    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        super.updateData(dataList, tradeVo, isShowInvalid);
    }

    /**
     * 构建整单用户
     *
     * @param tradeVo
     */
    protected void buildTradeUser(TradeVo tradeVo) {
        if (Utils.isEmpty(tradeVo.getTradeUsers())) {
            return;
        }
        for (TradeUser tradeUser : tradeVo.getTradeUsers()) {
            if (tradeUser.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
            DishDataItem item = new DishDataItem(ItemType.TRADE_USER);
            item.setTradeUser(tradeUser);
            item.setName(TradeUserUtil.getUserName(tradeUser));
            item.setNeedTopLine(true);
            data.add(item);
        }

    }

    /**
     * 数量编辑框显示
     *
     * @param item
     * @param holder
     */
    private void showNumEditView(DishDataItem item, ViewHolder holder) {
        final IShopcartItemBase shopcartItem = item.getBase();
        if (item.isCanEditNumber()) {
            holder.dish_num.setVisibility(View.GONE);
            holder.dish_edit_num.setBtnBg(R.drawable.beauty_decrease_selector, R.drawable.beauty_increase_selector);
            holder.dish_edit_num.setVisibility(View.VISIBLE);
            holder.dish_edit_num.setChangeListener(new NumberEditText.ChangeListener() {
                @Override
                public void onNumberChanged(BigDecimal number) {
                    EventBus.getDefault().post(new ActionClose());//关闭属性界面
                    if (shopcartItem instanceof ShopcartItem) {
                        ((ShopcartItem) shopcartItem).changeQty(number);
                    }
                    DinnerShoppingCart.getInstance().updateDinnerDish(shopcartItem, false);
                    // notifyDataSetChanged();
                }
            });
        } else {
            holder.dish_num.setVisibility(View.VISIBLE);
            holder.dish_edit_num.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新选中的条目
     */
    public void refreshSelectedItems() {
        if (Utils.isNotEmpty(mSelectedUuids)) {
            doSelectedItems(new ArrayList<>(mSelectedUuids));
        }
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

    //不需要的父类功能
    @Override
    protected void initAnchorLayout(ViewHolder holder, View convertView) {
    }

    @Override
    protected void setAnchor(ViewHolder holder, DishDataItem item) {
    }

    protected void setTopLineMargin(View view) {

    }

    public LinearLayout.LayoutParams getNoComboDiyLiWh(Context context) {
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(0, 0, 0, 0);
        return diyWh;
    }

    /**
     * 设置每项被选效果
     *
     * @param holder
     * @param item
     */
    private void setItemSelectedBg(ViewHolder holder, DishDataItem item) {
        if ((item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO
                || item.getType() == ItemType.CHILD || item.getType() == ItemType.WEST_CHILD) && item.isSelected()) {
            holder.dishView.setBackgroundResource(R.drawable.order_dish_item_shape);
        } else {
            holder.dishView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * 批量选择菜品
     *
     * @param selectedUuids
     * @return 返回选中的item
     */
    public List<DishDataItem> doSelectedItems(List<String> selectedUuids) {
        //先清空选中
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

    public DishDataItem doSelectedItem(String selectedUuid) {
        List<DishDataItem> dishDataItems = doSelectedItems(Utils.asList(selectedUuid));
        if (Utils.isNotEmpty(dishDataItems)) {
            return dishDataItems.get(0);
        } else {
            return null;
        }
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

    protected LinearLayout.LayoutParams getExtraDiyWh(Context context, boolean isChild) {
        int left = 0;
        if (isChild) {
            left = DensityUtil.dip2px(context, 30);
        } else {
            left = DensityUtil.dip2px(context, 20);
        }
        return getExtraDiyWh(left, 0, 0, 0);
    }
}
