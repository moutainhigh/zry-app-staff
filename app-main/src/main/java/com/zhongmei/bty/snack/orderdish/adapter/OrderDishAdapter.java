package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.bty.basemodule.inventory.utils.InventoryCacheUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.commonmodule.util.DecimalFormatUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Date：2015年7月8日 下午5:10:12
 * @Description: 菜品适配器
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class OrderDishAdapter extends BaseAdapter {
    protected Context mContext;
    public static final int DEFAULT_DISH_CARD_BG = 0;
    public static final int DISH_CARD_BG1 = 1;
    //通过设置显示不同的菜品卡片背景
    private int cardBgType = DEFAULT_DISH_CARD_BG;
    private LayoutInflater mInflater;

    private List<DishVo> mDishList;
    protected String residue;
    private boolean isEditMode;//yutang add 添加编辑模式
    private boolean isHidClearNumber;//yutang add 是否显示估清数量(默认显示)
    private BigDecimal multiply = new BigDecimal(15);
    private BigDecimal divide = new BigDecimal(100);
    private int mColumns;
    private int mItemHeight = 0;
    private int currentSelected;

    protected InventoryCacheUtil mInventoryCacheUtil;

    public OrderDishAdapter(Context context, List<DishVo> dishList, int columns) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDishList = dishList;
        this.residue = mContext.getString(R.string.order_residue_item);
        this.mColumns = columns;
        this.mItemHeight = mContext.getResources().getDimensionPixelSize(R.dimen.order_dish_grid_width);
        mInventoryCacheUtil = InventoryCacheUtil.getInstance();
    }

    public int getCurrentSelected() {
        return currentSelected;
    }

    public void setCurrentSelected(int selected) {
        currentSelected = selected;
        notifyDataSetChanged();
    }

    public List<DishVo> getDishList() {
        return mDishList;
    }

    @Override
    public int getCount() {
        return mDishList != null ? mDishList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDishList != null && position < mDishList.size() ? mDishList.get(position) : null;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public void setHidClearNumber(boolean showClearNumber) {
        isHidClearNumber = showClearNumber;
    }

    public void setDishCardBg(int cardBgType) {
        this.cardBgType = cardBgType;
    }

    public void setItemHeight(int itemHeight) {
        mItemHeight = itemHeight;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ViewHolder updateView(int itemIndex, GridView gridView) {
        int visiblePosition = gridView.getFirstVisiblePosition();
        if (itemIndex - visiblePosition >= 0) {
            // 得到要更新的item的view
            View view = gridView.getChildAt(itemIndex - visiblePosition);
            if (view == null) {
                return null;
            }
            // 从view中取得holder
            ViewHolder holder = (ViewHolder) view.getTag();
            inflateHolder(view, holder);
            bindView(holder, itemIndex);

            if (mInventoryCacheUtil.getSaleSwitch()) {//实时库存开启
                DishVo dishVo = (DishVo) getItem(itemIndex);
                if (dishVo != null) {
                    setView(holder.tvResidue, dishVo);
                }
            }

            return holder;
        }


        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(getItemLayoutResId(), parent, false);
            inflateHolder(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindView(viewHolder, position);

        TextView tvResidue = (TextView) convertView.findViewById(R.id.tv_residue);
        if (mInventoryCacheUtil.getSaleNumOpenSwitch()) {
            DishVo dishVo = (DishVo) getItem(position);
            if (dishVo != null) {
                setView(tvResidue, dishVo);
            }
        }
//        if(mInventoryCacheUtil.getSaleSwitch()){//实时库存开启
//            DishVo dishVo = (DishVo) getItem(position);
//            if(dishVo!=null){
//                setView(tvResidue,dishVo);
//            }
//        }

        return convertView;
    }

    /**
     * 设置库存
     */
    private void setView(TextView tvResidue, DishVo dishVo) {
        if (dishVo != null) {
            //如果多规格菜品默认菜品被估清，那么从系列菜品中找到一个未被估清的菜品
            if (dishVo.isContainProperties() && dishVo.getDishShop().getClearStatus() == ClearStatus.CLEAR) {
                DishShop leastResidueFromOtherDishs = dishVo.getLeastResidueFromOtherDishs();
                if (leastResidueFromOtherDishs != null && !TextUtils.isEmpty(leastResidueFromOtherDishs.getUuid())) {
                    InventoryInfo inventoryInfo = mInventoryCacheUtil.getInventoryNumByDishUuid(leastResidueFromOtherDishs.getUuid());
                    if (inventoryInfo != null) {
                        dishVo.setInventoryNum(inventoryInfo.getInventoryQty());
                    } else {
                        dishVo.setInventoryNum(null);
                    }
                } else {
                    dishVo.setInventoryNum(null);
                }
            } else if (dishVo.isContainProperties()) {//多规格菜品
                if (dishVo.getDishShop() != null) {
                    InventoryInfo inventoryInfo = mInventoryCacheUtil.getInventoryNumByDishUuid(dishVo.getDishShop().getUuid());
                    if (inventoryInfo != null) {
                        dishVo.setInventoryNum(inventoryInfo.getInventoryQty());
                    } else {
                        dishVo.setInventoryNum(null);
                    }
                }
            }
            setInventView(dishVo, tvResidue);
        }
    }

    protected void setInventView(DishVo dishVo, TextView tvResidue) {
        setInventViewNew(dishVo, tvResidue);
//        boolean isShowInventory = mInventoryCacheUtil.getSaleNumOpenSwitch();
//        String residue;
//        if (dishVo.getDishShop() != null && dishVo.getDishShop().getSaleType() == SaleType.WEIGHING && dishVo.getInventoryNum() != null && isShowInventory) {
//            residue = mContext.getString(R.string.order_weighing_inventory_item);
//            residue=String.format(residue, DecimalFormatUtil.formateInventoryNumber(dishVo.getInventoryNum()));
//            tvResidue.setVisibility(View.VISIBLE);
//        } else if (dishVo.getInventoryNum() != null && isShowInventory) {
//            residue = mContext.getString(R.string.order_inventory_item);
//            residue=String.format(residue, DecimalFormatUtil.formateInventoryNumber(dishVo.getInventoryNum()));
//            tvResidue.setVisibility(View.VISIBLE);
//        } else if (dishVo.getDishShop() != null && dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
//            residue = mContext.getString(R.string.order_weighing_item);
//            tvResidue.setVisibility(View.VISIBLE);
//        } else {
//            residue="";
//            tvResidue.setVisibility(View.GONE);
//        }
//        tvResidue.setText(residue);
    }

    protected void setInventViewNew(DishVo dishVo, TextView tvResidue) {
        boolean isShowSaleCount = mInventoryCacheUtil.getSaleNumOpenSwitch();

        String residue;
        if (dishVo.getDishShop() != null && dishVo.getDishShop().getSaleType() == SaleType.WEIGHING && isShowSaleCount) {
            if (mInventoryCacheUtil.getSaleSwitch()) {
                residue = mContext.getString(R.string.order_weighing_inventory_item);
                residue = String.format(residue, DecimalFormatUtil.formateInventoryNumber(dishVo.getInventoryNum()));
            } else {
                residue = mContext.getString(R.string.order_weighing_sale_count_item);
                residue = String.format(residue, DecimalFormatUtil.formateInventoryNumber(dishVo.getDishShop().getResidueTotal()));
            }
            tvResidue.setVisibility(View.VISIBLE);
        } else if (isShowSaleCount) {
            if (mInventoryCacheUtil.getSaleSwitch()) {
                residue = mContext.getString(R.string.order_inventory_item);
                residue = String.format(residue, DecimalFormatUtil.formateInventoryNumber(dishVo.getInventoryNum()));
            } else {
                residue = mContext.getString(R.string.order_sale_count_item);
                residue = String.format(residue, DecimalFormatUtil.formateInventoryNumber(dishVo.getDishShop().getResidueTotal()));
            }
            tvResidue.setVisibility(View.VISIBLE);
        } else if (dishVo.getDishShop() != null && dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
            residue = mContext.getString(R.string.order_weighing_item);
            tvResidue.setVisibility(View.VISIBLE);
        } else {
            residue = "";
            tvResidue.setVisibility(View.GONE);
        }
        tvResidue.setText(residue);
    }


    public void setDishList(List<DishVo> list) {
        mDishList.clear();
        if (list != null) {
            mDishList.addAll(list);
        }
        notifyDataSetChanged();
    }

    private int getBackgroundResource(ViewHolder holder, int position) {

        int resId;
        Object item = getItem(position);
        if (item == null) {
            return getPlaceBg(holder);
        }

        DishVo dishVo = (DishVo) item;
        if (dishVo.isClear()) {
            resId = getClearBg(holder, dishVo);
        } else {
            switch (cardBgType) {
                case DISH_CARD_BG1:
                    holder.tvName.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                    resId = getResourceCardId1(dishVo, position);
                    break;
                case DEFAULT_DISH_CARD_BG:
                default:
                    resId = getResourceCardId(dishVo, position);
                    break;
            }
        }

        return resId;
    }

    protected int getClearBg(ViewHolder holder, DishVo dishVo) {
        int resId;
        if (dishVo.isCombo()) {
            resId = R.drawable.selector_dish_combo_clear;
        } else {
            resId = R.drawable.selector_dish_single_clear;
        }
        return resId;
    }

    protected int getPlaceBg(ViewHolder holder) {
        return R.drawable.orderdish_placeholder_bg;
    }

    /**
     * 菜品卡片resId
     *
     * @param dishVo
     * @param position
     * @return
     */
    protected int getResourceCardId(DishVo dishVo, int position) {
        int resId;
        int index = position % mColumns;
        if (dishVo.isCombo()) {// 套餐
            if (index == 0) {// 绿色
                resId = R.drawable.selector_dish_combo_green;
            } else if (index == 1) {// 蓝色
                resId = R.drawable.selector_dish_combo_blue;
            } else if (index == 2) {

                resId = R.drawable.selector_dish_combo_purple;
            } else if (index == 3) {// 紫色
                resId = R.drawable.selector_dish_combo_slateblue;
            } else {
                resId = R.drawable.selector_dish_combo_darkblue;
            }
        } else {// 默认
            if (index == 0) {// 绿色
                resId = R.drawable.selector_dish_single_green;
            } else if (index == 1) {// 蓝色
                resId = R.drawable.selector_dish_single_blue;
            } else if (index == 2) {

                resId = R.drawable.selector_dish_single_purple;
            } else if (index == 3) {// 紫色
                resId = R.drawable.selector_dish_single_slateblue;
            } else {
                resId = R.drawable.selector_dish_single_darkblue;
            }
        }
        return resId;
    }

    protected int getResourceCardId1(DishVo dishVo, int position) {
        int resId;
        int index = position % mColumns;
        if (dishVo.isCombo()) {// 套餐
            if (index == 0) {// 绿色
                resId = R.drawable.selector_dish_combo_green1;
            } else if (index == 1) {// 蓝色
                resId = R.drawable.selector_dish_combo_blue1;
            } else if (index == 2) {

                resId = R.drawable.selector_dish_combo_purple1;
            } else if (index == 3) {// 紫色
                resId = R.drawable.selector_dish_combo_slateblue1;
            } else {
                resId = R.drawable.selector_dish_combo_darkblue1;
            }
        } else {// 默认
            if (index == 0) {// 绿色
                resId = R.drawable.selector_dish_single_green1;
            } else if (index == 1) {// 蓝色
                resId = R.drawable.selector_dish_single_blue1;
            } else if (index == 2) {

                resId = R.drawable.selector_dish_single_purple1;
            } else if (index == 3) {// 紫色
                resId = R.drawable.selector_dish_single_slateblue1;
            } else {
                resId = R.drawable.selector_dish_single_darkblue1;
            }
        }
        return resId;
    }

    protected String getDishName(DishVo vo) {
        return vo.getName();
    }

    protected String getShortName(DishVo vo) {
        return vo.getShortName();
    }

    protected void bindView(ViewHolder holder, int position) {
        // 设置背景
        holder.vMainContent.setBackgroundResource(getBackgroundResource(holder, position));

        Object item = getItem(position);
        if (item == null) {
            holder.tvShortName.setVisibility(View.INVISIBLE);
            holder.tvName.setVisibility(View.INVISIBLE);
            holder.tvMarketPrice.setVisibility(View.INVISIBLE);
            holder.tvNumber.setVisibility(View.INVISIBLE);

            holder.ivProperty.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.GONE);
        } else {
            DishVo dishVo = (DishVo) getItem(position);

            DishShop dishShop = dishVo.getDishShop();

            // 短名称
            if (dishVo.isClear()) {
                holder.tvShortName.setTextColor(Color.parseColor("#ebeff2"));
                holder.tvShortName.setText(R.string.hadClear);
            } else {
                holder.tvShortName.setTextColor(Color.WHITE);
                holder.tvShortName.setText(getShortName(dishVo));
            }

            //名称
            String name = getDishName(dishVo);
            holder.tvName.setText(name);

            // 菜品价格
            if (dishVo.isCombo() && dishVo.getMinPrice().compareTo(dishVo.getMaxPrice()) != 0) {
                holder.tvMarketPrice.setVisibility(View.INVISIBLE);
            } else {
                holder.tvMarketPrice.setText(ShopInfoCfg.formatCurrencySymbol(dishVo.getPrice()));
                holder.tvMarketPrice.setVisibility(View.VISIBLE);
            }

            // 如果是编辑模式 yutang add 20160813
            if (this.isEditMode) {
                if (dishVo.isSelected()) {
                    holder.tvNumber.setBackgroundResource(R.drawable.dinner_dish_status_selected);
                } else {
                    holder.tvNumber.setBackgroundResource(R.drawable.dinner_dish_status_unselected);
                }
            } else {
                // 设置数量
                BigDecimal selectedQTY = getSelectedQty(dishVo);
                if (selectedQTY.compareTo(BigDecimal.ZERO) > 0) {
                    //大于或等于100，则显示99+
                    if (selectedQTY.compareTo(divide) >= 0) {
                        holder.tvNumber.setText("99+");
                    } else {
                        holder.tvNumber.setText(MathDecimal.toTrimZeroString(selectedQTY));
                    }

                    // 根据字数显示字体大小
                    int length = holder.tvNumber.getText().toString().length();
                    if (length <= 2) {
                        holder.tvNumber.setTextSize(18);
                    } else if (length == 3) {
                        holder.tvNumber.setTextSize(14);
                    } else {
                        holder.tvNumber.setTextSize(10);
                    }

                    holder.tvNumber.setVisibility(View.VISIBLE);
                } else {
                    holder.tvNumber.setVisibility(View.INVISIBLE);
                }
            }

            //隐藏数量 yutang add 20160816
            if (!this.isEditMode && isHidClearNumber) {
                holder.tvNumber.setVisibility(View.INVISIBLE);
            }

            setResidueView(dishVo, dishShop, holder);

            holder.ivProperty.setVisibility(dishVo.isContainProperties() ? View.VISIBLE : View.GONE);
            controlViewVisible(holder, dishVo);
        }
    }

    protected void setResidueView(DishVo dishVo, DishShop dishShop, ViewHolder holder) {
        //余量
        BigDecimal count = dishShop.getResidueTotal();
        BigDecimal total = dishShop.getSaleTotal();

        if (dishVo.isContainProperties() && dishVo.isClear()) {//全部沽清或者全部在售 显示默认

        } else if (dishShop.getClearStatus() == ClearStatus.CLEAR) {//默认的被沽清，余下按余量最小的显示
            //默认的被估清，余下的余量最小的那个菜
            DishShop leastResidueFromOtherDishs = dishVo.getLeastResidueFromOtherDishs();
            if (leastResidueFromOtherDishs != null) {
                count = leastResidueFromOtherDishs.getResidueTotal();
                total = leastResidueFromOtherDishs.getSaleTotal();
            }
        }

        if (count != null && count.compareTo(divide(total)) <= 0) {

            String unit = null;
            if (dishVo.getUnit() != null && dishVo.getUnit().getName() != null) {
                unit = dishVo.getUnit().getName();
            }

            if (count.compareTo(BigDecimal.ZERO) < 1) {
                count = BigDecimal.ZERO;
            }
        } else {
            if (dishVo.getDishShop() != null && dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
                holder.tvResidue.setVisibility(View.VISIBLE);
                holder.tvResidue.setText(R.string.order_weighing_item);
            } else {
                holder.tvResidue.setVisibility(View.GONE);
            }
        }

        boolean isShowInventory = mInventoryCacheUtil.getSaleNumOpenSwitch();
        if (isShowInventory)
            setInventView(dishVo, holder.tvResidue);

    }

    protected void controlViewVisible(ViewHolder holder, DishVo dishVo) {
        holder.tvName.setVisibility(View.VISIBLE);
    }

    protected BigDecimal divide(BigDecimal total) {
        return total.multiply(multiply).divide(divide, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 获取菜品当前已选数量
     */
    protected BigDecimal getSelectedQty(DishVo dishVo) {
        Map<String, BigDecimal> map = ShoppingCart.getInstance().getDishSelectQTY(ShoppingCart.getInstance().getShoppingCartVo());
        BigDecimal qty = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            if (dishVo.isSameSeries(entry.getKey())) {
                qty = qty.add(entry.getValue());
            }
        }
        return qty;
    }

    protected int getItemLayoutResId() {
        return R.layout.order_dish_grid_item;
    }


    protected void inflateHolder(View convertView, ViewHolder viewHolder) {
        //设置item高度
//        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
//        layoutParams.height = mItemHeight;
//        convertView.setLayoutParams(layoutParams);

        viewHolder.vMainContent = convertView.findViewById(R.id.v_main_content);
        viewHolder.tvShortName = (TextView) convertView.findViewById(R.id.tv_short_name);
        viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
        viewHolder.tvMarketPrice = (TextView) convertView.findViewById(R.id.tv_market_price);
        viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
        viewHolder.tvResidue = (TextView) convertView.findViewById(R.id.tv_residue);
        viewHolder.ivProperty = (ImageView) convertView.findViewById(R.id.iv_property);
//        viewHolder.topView=(View)convertView.findViewById(R.id.v_topview);
    }

    protected static class ViewHolder {
        public TextView tvNumber;

        public TextView tvShortName;

        public TextView tvName;

        public TextView tvPrice;

        public TextView tvMarketPrice;

        public View vMainContent;

        public ImageView ivProperty;

        public TextView tvResidue;

        public TextView tvWeight;

//        public View topView;
    }

}
