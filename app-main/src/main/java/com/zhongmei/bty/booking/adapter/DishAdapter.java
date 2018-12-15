package com.zhongmei.bty.booking.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * @Date：2015年9月22日 上午18:28:04
 * @Description: 商品信息adapter
 * @Version: 1.0
 */
public class DishAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mInflater;

    private List<TradeDishDataItem> mDataSet;

    private Drawable mChildIcon = null;

    private Drawable mAllOrderDiscount = null;

    public DishAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDataSet = new ArrayList<TradeDishDataItem>();
        this.mChildIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_child_icon);
        this.mAllOrderDiscount = context.getResources().getDrawable(R.drawable.cashier_order_dish_alldiscount_icon);
    }

    @Override
    public int getCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSet == null ? null : mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDataSet(TradeVo tradeVo) {
        mDataSet.clear();
        if (tradeVo != null) {
            List<TradeItemVo> dataSet = getValidTradeItemList(tradeVo.getTradeItemList());
            boolean hasAllOrderDiscount = tradeVo.getTradePrivilege() != null;
            if (dataSet != null) {
                List<TradeDishDataItem> orderCenterDishDataItems = buildDishDataList(dataSet, hasAllOrderDiscount);
                mDataSet.addAll(orderCenterDishDataItems);
            }
        }
        notifyDataSetChanged();
    }

    private List<TradeItemVo> getValidTradeItemList(List<TradeItemVo> tradeItemList) {
        if (Utils.isNotEmpty(tradeItemList)) {
            List<TradeItemVo> validTradeItemList = new ArrayList<TradeItemVo>();
            for (TradeItemVo tradeItemVo : tradeItemList) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                if (tradeItem.getStatusFlag() == StatusFlag.VALID
                        && tradeItem.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
                    validTradeItemList.add(tradeItemVo);
                }
            }

            return validTradeItemList;
        }

        return Collections.emptyList();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int itemViewType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (itemViewType) {
                case 0:
                    convertView = mInflater.inflate(R.layout.booking_trade_dish_list_item_01, parent, false);
                    holder.vTopLine = convertView.findViewById(R.id.v_top_line);
                    holder.llDishView = (LinearLayout) convertView.findViewById(R.id.ll_dish_view);
                    holder.tvDishName = (TextView) convertView.findViewById(R.id.tv_dish_name);
                    holder.tvDishNum = (TextView) convertView.findViewById(R.id.tv_dish_num);
//					holder.tvDishPrice = (TextView)convertView.findViewById(R.id.tv_dish_price);
//					holder.tvSingleDishPrice = (TextView)convertView.findViewById(R.id.tv_singledish_price);
                    holder.llDishProperty = (LinearLayout) convertView.findViewById(R.id.ll_dish_property);
                    holder.tvPropertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
//					holder.tvPropertyQuantity = (TextView)convertView.findViewById(R.id.tv_property_quantity);
//					holder.tvPropertyAmount = (TextView)convertView.findViewById(R.id.tv_property_amount);
                    holder.llDishExtra = (LinearLayout) convertView.findViewById(R.id.ll_dish_extra);
                    holder.tvExtraName = (TextView) convertView.findViewById(R.id.tv_extra_name);
//					holder.tvExtraQuantity = (TextView)convertView.findViewById(R.id.tv_extra_quantity);
//					holder.tvExtraAmount = (TextView)convertView.findViewById(R.id.tv_extra_amount);
                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.booking_trade_dish_list_item_03, parent, false);
//					holder.llDishPrivilege = (LinearLayout)convertView.findViewById(R.id.ll_dish_privilege);
//					holder.tvPrivilegeType = (TextView)convertView.findViewById(R.id.tv_privilege_type);
//					holder.tvPrivilegeAmount = (TextView)convertView.findViewById(R.id.tv_privilege_amount);
                    holder.tvDishMemo = (TextView) convertView.findViewById(R.id.tv_dish_memo);
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bindView(holder, position);

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        TradeDishDataItem dishDataItem = (TradeDishDataItem) getItem(position);
        int itemViewType = -1;
        switch (dishDataItem.getType()) {
            case TradeDishDataItem.ITEM_TYPE_SINGLE:
            case TradeDishDataItem.ITEM_TYPE_COMBO:
            case TradeDishDataItem.ITEM_TYPE_COMBO_CHILD:
                itemViewType = 0;
                break;
            case TradeDishDataItem.ITEM_TYPE_SINGLE_MEMO_AND_DISCOUNT:
            case TradeDishDataItem.ITEM_TYPE_COMBO_MEMO_AND_DISCOUNT:
            case TradeDishDataItem.ITEM_TYPE_COMBO_CHILD_MEMO:
                itemViewType = 1;
                break;
            default:
                break;
        }
        return itemViewType;
    }

    private void bindView(ViewHolder holder, int position) {
        TradeDishDataItem tradeDishDataItem = (TradeDishDataItem) getItem(position);
        int itemViewType = getItemViewType(position);
        int type = tradeDishDataItem.getType();// 菜品类型
        TradeItemVo tradeItemVo = tradeDishDataItem.getTradeItemVo();
        switch (itemViewType) {
            case 0:
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                // 设置菜品名称、数量、价格、口味、做法格式
                if (type == TradeDishDataItem.ITEM_TYPE_COMBO_CHILD) {
                    holder.llDishView.setLayoutParams(getIsComboDiyWh(mContext));
                    holder.tvDishName.setTextSize(16);
                    holder.tvDishName.setPadding(DensityUtil.dip2px(mContext, 24), 0, 0, 0);
                    holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(mChildIcon, null, null, null);
                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    BigDecimal dishnum =
                            ShopcartItemUtils.computeSingleQty(tradeItem.getQuantity(),
                                    tradeDishDataItem.getParentTradeItemVo().getTradeItem(),
                                    null);
                    holder.tvDishNum.setText(MathDecimal.toTrimZeroString(dishnum));
                    // holder.tvDishNum.setText(MathDecimal.toTrimZeroString(tradeItem.getQuantity()));
//					if (tradeItem.getPrice().compareTo(BigDecimal.ZERO) != 0) {
//						holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().doubleValue()));
//					} else {
//						holder.tvSingleDishPrice.setText("");
//					}
//					if (tradeItem.getAmount().compareTo(BigDecimal.ZERO) != 0) {
//						holder.tvDishPrice.setText(Utils.formatPrice(tradeItem.getAmount().doubleValue()));
//					} else {
//						holder.tvDishPrice.setText("");
//					}
                    holder.vTopLine.setVisibility(View.GONE);

                    LinearLayout.LayoutParams diyWh =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                    diyWh.setMargins(0, DensityUtil.dip2px(mContext, 10), 0, DensityUtil.dip2px(mContext, 10));

                    holder.llDishProperty.setLayoutParams(diyWh);
                    holder.tvPropertyName.setPadding(DensityUtil.dip2px(mContext, 48), 0, 0, 0);
                    holder.llDishExtra.setLayoutParams(diyWh);
                    holder.tvExtraName.setPadding(DensityUtil.dip2px(mContext, 48), 0, 0, 0);
                } else {
                    holder.llDishView.setLayoutParams(getNoComboDiyWh(mContext));
                    holder.tvDishName.setPadding(0, 0, 0, 0);
                    holder.tvDishName.setTextSize(16);
                    if (tradeDishDataItem.isHasAllOrderDiscount()) {
                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        // 不显示整单打折的图标
                        // holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(mAllOrderDiscount,
                        // null, null, null);
                    } else {
                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    holder.tvDishNum.setText(MathDecimal.toTrimZeroString(tradeItem.getQuantity()));
//					holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().doubleValue()));
//					holder.tvDishPrice.setText(Utils.formatPrice(tradeItem.getAmount().doubleValue()));
//					
                    if (position != 0) {
                        holder.vTopLine.setVisibility(View.VISIBLE);
                    } else {
                        holder.vTopLine.setVisibility(View.GONE);
                    }

                    LinearLayout.LayoutParams diyWh =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                    diyWh.setMargins(0, DensityUtil.dip2px(mContext, 10), 0, DensityUtil.dip2px(mContext, 10));

                    holder.llDishProperty.setLayoutParams(diyWh);
                    holder.tvPropertyName.setPadding(DensityUtil.dip2px(mContext, 24), 0, 0, 0);
                    holder.llDishExtra.setLayoutParams(diyWh);
                    holder.tvExtraName.setPadding(DensityUtil.dip2px(mContext, 24), 0, 0, 0);
                }

                /**
                 * 口味做法
                 */
                List<TradeItemProperty> propertyList = filterTradeItemProperty(tradeItemVo, PropertyKind.PROPERTY);
                if (Utils.isNotEmpty(propertyList)) {
                    holder.tvPropertyName.setText(getPropertyName(propertyList));
//					holder.tvPropertyQuantity.setVisibility(View.GONE);// 现在不需要显示口味做法的数量
//					holder.tvPropertyQuantity.setText(MathDecimal.toTrimZeroString(getPropertyQuantity(propertyList)));
//					holder.tvPropertyAmount.setText(Utils.formatPrice(getPropertyAmount(propertyList).doubleValue()));
                    holder.llDishProperty.setVisibility(View.VISIBLE);
                } else {
                    holder.llDishProperty.setVisibility(View.GONE);
                }

                // 加料
                setExtraView(tradeDishDataItem, holder);
                break;

            case 1:
                // 设置折扣以及备注格式
                if (type == TradeDishDataItem.ITEM_TYPE_COMBO_CHILD_MEMO) {
//					holder.llDishPrivilege.setLayoutParams(getIsComboDiyWh(mContext));
//					holder.tvPrivilegeType.setPadding(DensityUtil.dip2px(48), 0, 0, 0);
                    holder.tvDishMemo.setLayoutParams(getIsComboDiyWh(mContext));
                    holder.tvDishMemo.setPadding(DensityUtil.dip2px(mContext, 48), 0, 0, 0);
                } else {
//					holder.llDishPrivilege.setLayoutParams(getNoComboDiyWh(mContext));
//					holder.tvPrivilegeType.setPadding(DensityUtil.dip2px(24), 0, 0, 0);
                    holder.tvDishMemo.setLayoutParams(getNoComboDiyWh(mContext));
                    holder.tvDishMemo.setPadding(DensityUtil.dip2px(mContext, 24), 0, 0, 0);
                }

//				/**
//				 * 菜品折扣
//				 */
//				TradePrivilege tradePrivilege = tradeItemVo.getTradeItemPrivilege();
//				if (tradePrivilege != null) {
//					switch (tradePrivilege.getPrivilegeType()) {
//						case DISCOUNT:
//							String privilegeValue =
//								MathDecimal.toTrimZeroString(MathDecimal.div(tradePrivilege.getPrivilegeValue(), 10, 2));
//							holder.tvPrivilegeType.setText(privilegeValue
//								+ mContext.getResources().getString(R.string.discount1));
//							holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//								.doubleValue()));
////							holder.llDishPrivilege.setVisibility(View.VISIBLE);
//							break;
//						
//						case FREE:
//							holder.tvPrivilegeType.setText(mContext.getString(R.string.dishFree));
//							holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//								.doubleValue()));
////							holder.llDishPrivilege.setVisibility(View.VISIBLE);
//							break;
//						
//						case REBATE:
//							holder.tvPrivilegeType.setText(mContext.getString(R.string.letThePrice));
//							holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//								.doubleValue()));
////							holder.llDishPrivilege.setVisibility(View.VISIBLE);
//							break;
//						
//						case AUTO_DISCOUNT:
//							String autoPrivilegeValue =
//								MathDecimal.toTrimZeroString(MathDecimal.div(tradePrivilege.getPrivilegeValue(), 10, 2));
//							holder.tvPrivilegeType.setText(mContext.getString(R.string.auto_discount,
//								autoPrivilegeValue));
//							holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//								.doubleValue()));
////							holder.llDishPrivilege.setVisibility(View.VISIBLE);
//							break;
//						
//						default:
////							holder.llDishPrivilege.setVisibility(View.GONE);
//							break;
//					}
//				} else {
////					holder.llDishPrivilege.setVisibility(View.GONE);
//				}

                /**
                 * 菜品备注
                 */
                String memoarrays = buildTradeMemos(tradeItemVo);
                if (!TextUtils.isEmpty(memoarrays)) {
                    holder.tvDishMemo.setText(mContext.getString(R.string.order_center_detail_memo, memoarrays));
                    holder.tvDishMemo.setVisibility(View.VISIBLE);
                } else {
                    holder.tvDishMemo.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }
    }

    private void setExtraView(TradeDishDataItem tradeDishDataItem, ViewHolder holder) {
        List<TradeItemVo> extraList = tradeDishDataItem.getExtraList();
        TradeItem tradeItem = tradeDishDataItem.getTradeItemVo().getTradeItem();
        if (Utils.isNotEmpty(extraList)) {
            String extraName = "";
            BigDecimal extraQuantity = BigDecimal.ZERO;
            if (tradeDishDataItem.getType() == TradeDishDataItem.ITEM_TYPE_COMBO_CHILD) {
                TradeItem parentTradeItem = tradeDishDataItem.getParentTradeItemVo().getTradeItem();
                if (parentTradeItem.getSaleType() == SaleType.UNWEIGHING
                        && tradeItem.getSaleType() == SaleType.UNWEIGHING) {
                    extraName = getExtraName(extraList, tradeItem.getQuantity());
                    extraQuantity = getExtraQuantity(extraList, tradeItem.getQuantity());
                } else if (parentTradeItem.getSaleType() == SaleType.UNWEIGHING
                        && tradeItem.getSaleType() == SaleType.WEIGHING) {
                    extraName = getExtraName(extraList, parentTradeItem.getQuantity());
                    extraQuantity = getExtraQuantity(extraList, parentTradeItem.getQuantity());
                } else if (parentTradeItem.getSaleType() == SaleType.WEIGHING
                        && tradeItem.getSaleType() == SaleType.UNWEIGHING) {
                    extraName = getExtraName(extraList, tradeItem.getQuantity());
                    extraQuantity = getExtraQuantity(extraList, tradeItem.getQuantity());
                } else {
                    extraName = getExtraName(extraList, BigDecimal.ONE);
                    extraQuantity = getExtraQuantity(extraList, BigDecimal.ONE);
                }
            } else {
                if (tradeItem.getSaleType() == SaleType.UNWEIGHING) {
                    extraName = getExtraName(extraList, tradeItem.getQuantity());
                    extraQuantity = getExtraQuantity(extraList, tradeItem.getQuantity());
                } else {
                    extraName = getExtraName(extraList, BigDecimal.ONE);
                    extraQuantity = getExtraQuantity(extraList, BigDecimal.ONE);
                }
            }
            holder.tvExtraName.setText(extraName);
            // holder.tvExtraQuantity.setText(MathDecimal.toTrimZeroString(getExtraQuantity(tradeDishDataItem)));
//			holder.tvExtraQuantity.setText(MathDecimal.toTrimZeroString(extraQuantity));
//			holder.tvExtraQuantity.setVisibility(View.GONE);// 现在不需要显示加料的数量
//			holder.tvExtraAmount.setText(Utils.formatPrice(getExtraAmount(extraList).doubleValue()));
            holder.llDishExtra.setVisibility(View.VISIBLE);
        } else {
            holder.llDishExtra.setVisibility(View.GONE);
//			holder.tvExtraQuantity.setVisibility(View.GONE);// 现在不需要显示加料的数量
        }
    }

    private LinearLayout.LayoutParams getIsComboDiyWh(Context context) {
        int topOrBottom = DensityUtil.dip2px(context, 10);
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(0, topOrBottom, 0, topOrBottom);
        return diyWh;
    }

    private LinearLayout.LayoutParams getNoComboDiyWh(Context context) {
        int top = DensityUtil.dip2px(context, 10);
        int bottom = DensityUtil.dip2px(context, 10);
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(0, top, 0, bottom);
        return diyWh;
    }

    public List<TradeDishDataItem> buildDishDataList(List<TradeItemVo> tradeItemVos, boolean hasAllOrderDiscount) {
        List<TradeItemVo> itemVos = new LinkedList<TradeItemVo>();
        // 先分出单品(含套餐外壳)、套餐明细、加料，存放套餐明细和加料的map的key为父条目的uuid
        Map<String, List<TradeItemVo>> setmealFinder = new HashMap<String, List<TradeItemVo>>();
        Map<String, List<TradeItemVo>> extraFinder = new HashMap<String, List<TradeItemVo>>();
        for (TradeItemVo itemVo : tradeItemVos) {
            TradeItem tradeItem = itemVo.getTradeItem();
            switch (tradeItem.getType()) {
                case SINGLE:
                    if (tradeItem.getParentUuid() != null) {
                        // 套餐明细
                        List<TradeItemVo> list = setmealFinder.get(tradeItem.getParentUuid());
                        if (list == null) {
                            list = new ArrayList<TradeItemVo>();
                            setmealFinder.put(tradeItem.getParentUuid(), list);
                        }
                        list.add(itemVo);
                    } else {
                        // 单品
                        itemVos.add(itemVo);
                    }
                    break;

                case COMBO: // 套餐外壳
                    itemVos.add(itemVo);
                    break;

                case EXTRA: // 加料
                    List<TradeItemVo> list = extraFinder.get(tradeItem.getParentUuid());
                    if (list == null) {
                        list = new ArrayList<TradeItemVo>();
                        extraFinder.put(tradeItem.getParentUuid(), list);
                    }
                    list.add(itemVo);
                    break;

                default:
                    break;
            }
        }

        // 遍历单品、套餐Map,构建新的TradeItemVo List
        List<TradeDishDataItem> resultList = new ArrayList<TradeDishDataItem>();
        Collections.reverse(itemVos);
        for (TradeItemVo tradeItemVo : itemVos) {
            if (tradeItemVo.getTradeItem().getType() == DishType.COMBO) {
                // 套餐名称
                resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO, tradeItemVo,
                        hasAllOrderDiscount));
                // 套餐明细列表
                List<TradeItemVo> comboSetmealList = setmealFinder.get(tradeItemVo.getTradeItem().getUuid());
                if (comboSetmealList != null && !comboSetmealList.isEmpty()) {
                    for (TradeItemVo setmealTradeItemVo : comboSetmealList) {
                        // 子菜名称、加料等
                        TradeDishDataItem tradeDishDataItem =
                                new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO_CHILD, setmealTradeItemVo,
                                        hasAllOrderDiscount);
                        tradeDishDataItem.setParentTradeItemVo(tradeItemVo);
                        List<TradeItemVo> childExtraList = extraFinder.get(setmealTradeItemVo.getTradeItem().getUuid());
                        tradeDishDataItem.setExtraList(childExtraList);
                        resultList.add(tradeDishDataItem);
                        // 子菜备注
                        String memo = setmealTradeItemVo.getTradeItem().getTradeMemo();
                        List<TradeItemProperty> memoList =
                                filterTradeItemProperty(setmealTradeItemVo, PropertyKind.MEMO);
                        if (!TextUtils.isEmpty(memo) || !memoList.isEmpty()) {
                            resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO_CHILD_MEMO,
                                    setmealTradeItemVo, hasAllOrderDiscount));
                        }
                    }
                }
                // 套餐备注、折扣
                String memo = tradeItemVo.getTradeItem().getTradeMemo();
                List<TradeItemProperty> memoList = filterTradeItemProperty(tradeItemVo, PropertyKind.MEMO);
                if (tradeItemVo.getTradeItemPrivilege() != null || !TextUtils.isEmpty(memo) || !memoList.isEmpty()) {
                    resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_COMBO_MEMO_AND_DISCOUNT,
                            tradeItemVo, hasAllOrderDiscount));// 套餐折扣
                }
            } else {
                // 单品名称、加料等
                TradeDishDataItem tradeDishDataItem =
                        new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_SINGLE, tradeItemVo, hasAllOrderDiscount);
                List<TradeItemVo> singleExtraList = extraFinder.get(tradeItemVo.getTradeItem().getUuid());
                tradeDishDataItem.setExtraList(singleExtraList);
                resultList.add(tradeDishDataItem);
                // 单品折扣、备注
                String memo = tradeItemVo.getTradeItem().getTradeMemo();
                List<TradeItemProperty> memoList = filterTradeItemProperty(tradeItemVo, PropertyKind.MEMO);
                if (tradeItemVo.getTradeItemPrivilege() != null || !TextUtils.isEmpty(memo) || !memoList.isEmpty()) {
                    resultList.add(new TradeDishDataItem(TradeDishDataItem.ITEM_TYPE_SINGLE_MEMO_AND_DISCOUNT,
                            tradeItemVo, hasAllOrderDiscount));
                }
            }
        }

        return resultList;
    }

    /**
     * 拼接加料文本
     */
    private String getExtraName(List<TradeItemVo> extraList, BigDecimal parentItemQuantity) {
        StringBuilder sb = new StringBuilder();
        if (Utils.isNotEmpty(extraList)) {

            int size = extraList.size();
            for (int i = 0; i < size; i++) {
                TradeItem tradeItem = extraList.get(i).getTradeItem();
                sb.append(tradeItem.getDishName())
                        .append("(x")
                        .append(MathDecimal.toTrimZeroString(MathDecimal.div(tradeItem.getQuantity(), parentItemQuantity)))
                        .append(")");
                if (i < size - 1) {
                    sb.append("、");
                }
            }
        }

        return sb.toString();
    }

    private BigDecimal getExtraQuantity(List<TradeItemVo> mextraList, BigDecimal parentItemQuantity) {

        BigDecimal quantity = BigDecimal.ZERO;
        List<TradeItemVo> extraList = mextraList;
        if (Utils.isNotEmpty(extraList)) {
            for (TradeItemVo tradeItemVo : extraList) {
                // quantity =
                // quantity.add(tradeItemVo.getTradeItem().getQuantity());
                quantity = quantity.add(MathDecimal.div(tradeItemVo.getTradeItem().getQuantity(), parentItemQuantity));

            }
        }

        return quantity;
    }

    private BigDecimal getExtraQuantity(TradeDishDataItem tradeDishDataItem) {

        BigDecimal quantity = BigDecimal.ZERO;
        List<TradeItemVo> extraList = tradeDishDataItem.getExtraList();
        if (Utils.isNotEmpty(extraList)) {
            for (TradeItemVo tradeItemVo : extraList) {
                quantity = quantity.add(tradeItemVo.getTradeItem().getQuantity());
            }
        }

        return quantity;
    }

    private BigDecimal getExtraAmount(List<TradeItemVo> extraList) {
        BigDecimal amount = BigDecimal.ZERO;

        if (Utils.isNotEmpty(extraList)) {
            for (TradeItemVo tradeItemVo : extraList) {
                amount = amount.add(tradeItemVo.getTradeItem().getAmount());
            }
        }

        return amount;
    }

    /**
     * @Title: filterTradeItemProperty
     * @Description: 根据输入类型，过滤交易类型
     * @Param @param tradeItemVo
     * @Param @param propertyKind
     * @Param @return TODO
     * @Return List<TradeItemProperty> 返回类型
     */
    public List<TradeItemProperty> filterTradeItemProperty(TradeItemVo tradeItemVo, PropertyKind propertyKind) {
        List<TradeItemProperty> tradeItemProperties = new ArrayList<TradeItemProperty>();

        List<TradeItemProperty> tradeItemPropertyList = tradeItemVo.getTradeItemPropertyList();
        if (tradeItemPropertyList != null) {
            for (TradeItemProperty tradeItemProperty : tradeItemPropertyList) {
                if (tradeItemProperty.getPropertyType() == propertyKind) {
                    tradeItemProperties.add(tradeItemProperty);
                }
            }
        }

        return tradeItemProperties;
    }

    private String genDishName(TradeItemVo tradeItemVo) {
        StringBuilder sb = new StringBuilder(tradeItemVo.getTradeItem().getDishName());
        List<TradeItemProperty> tradeItemProperties = filterTradeItemProperty(tradeItemVo, PropertyKind.STANDARD);
        int size = tradeItemProperties.size();
        for (int i = 0; i < size; i++) {
            TradeItemProperty tradeItemProperty = tradeItemProperties.get(i);
            if (i == 0) {
                sb.append("(");
            }

            sb.append(tradeItemProperty.getPropertyName());

            if (i == size - 1) {
                sb.append(")");
            } else {
                sb.append("、");
            }
        }

        return sb.toString();
    }

    /**
     * @Title: getPropertyName
     * @Description: 拼接属性名称
     * @Param @param tradeItemPropertyList
     * @Param @return TODO
     * @Return String 返回类型
     */
    private String getPropertyName(List<TradeItemProperty> tradeItemPropertyList) {
        StringBuilder sb = new StringBuilder();
        if (Utils.isNotEmpty(tradeItemPropertyList)) {
            int size = tradeItemPropertyList.size();
            for (int i = 0; i < size; i++) {
                TradeItemProperty tradeItemProperty = tradeItemPropertyList.get(i);
                sb.append(tradeItemProperty.getPropertyName());
                if (i != size - 1) {
                    sb.append("、");
                }
            }
        }

        return sb.toString();
    }

    /**
     * @Title: getPropertyQuantity
     * @Description: 返回属性数量
     * @Param @param tradeItemPropertyList
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    private BigDecimal getPropertyQuantity(List<TradeItemProperty> tradeItemPropertyList) {
        BigDecimal quantity = BigDecimal.ZERO;

        if (Utils.isNotEmpty(tradeItemPropertyList)) {
            for (TradeItemProperty tradeItemProperty : tradeItemPropertyList) {
                quantity = quantity.add(tradeItemProperty.getQuantity());
            }
        }

        return quantity;
    }

    /**
     * @Title: getPropertyAmount
     * @Description: 返回属性变价
     * @Param @param tradeItemPropertyList
     * @Param @return TODO
     * @Return BigDecimal 返回类型
     */
    private BigDecimal getPropertyAmount(List<TradeItemProperty> tradeItemPropertyList) {
        BigDecimal amount = BigDecimal.ZERO;

        if (Utils.isNotEmpty(tradeItemPropertyList)) {
            for (TradeItemProperty tradeItemProperty : tradeItemPropertyList) {
                if (tradeItemProperty.getAmount() != null) {
                    amount = amount.add(tradeItemProperty.getAmount());
                }
            }
        }

        return amount;
    }

    private String buildTradeMemos(TradeItemVo tradeItemVo) {

        if (tradeItemVo != null) {

            StringBuilder sb = new StringBuilder();
            String memo = tradeItemVo.getTradeItem().getTradeMemo();
            if (!TextUtils.isEmpty(memo)) {
                sb.append(memo);
            }
            List<TradeItemProperty> memoList = filterTradeItemProperty(tradeItemVo, PropertyKind.MEMO);

            int memoSize = memoList.size();
            for (int j = 0; j < memoSize; j++) {
                TradeItemProperty tradeItemProperty = memoList.get(j);
                if (!TextUtils.isEmpty(memo) && j == 0) {
                    sb.append(",");
                }
                sb.append(tradeItemProperty.getPropertyName());
                if (j < memoSize - 1) {
                    sb.append(",");
                }

            }
            return sb.toString();
        } else {
            return null;
        }

    }

    static class ViewHolder {
        View vTopLine;

        LinearLayout llDishView;

        TextView tvDishName;// 菜品名称

        TextView tvDishNum;// 菜品数量

//		TextView tvDishPrice;// 菜品价格
//		
//		TextView tvSingleDishPrice;// 菜品单价价格

        LinearLayout llDishProperty;// 菜品属性，包括做法、口味

        TextView tvPropertyName;

//		TextView tvPropertyQuantity;

//		TextView tvPropertyAmount;

        LinearLayout llDishExtra;// 加料

        TextView tvExtraName;

//		TextView tvExtraQuantity;

//		TextView tvExtraAmount;

//		LinearLayout llDishPrivilege;// 菜品折扣

//		TextView tvPrivilegeType;// 折扣类型

//		TextView tvPrivilegeAmount;// 折扣数量

        TextView tvDishMemo;// 菜品备注
    }

    /**
     * @Date：2015年8月10日 上午9:37:24
     * @Description: 订单中心详情交易项数据
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
    public class TradeDishDataItem {
        public static final int ITEM_TYPE_SINGLE = 1;// 单品

        public static final int ITEM_TYPE_SINGLE_MEMO_AND_DISCOUNT = 2;// 单品折扣

        public static final int ITEM_TYPE_COMBO = 3;// 套餐

        public static final int ITEM_TYPE_COMBO_CHILD = 4;// 套餐子菜

        public static final int ITEM_TYPE_COMBO_CHILD_MEMO = 5;// 套餐子菜备注

        public static final int ITEM_TYPE_COMBO_MEMO_AND_DISCOUNT = 6;// 套餐折扣

        private int type;

        private TradeItemVo parentTradeItemVo;

        private TradeItemVo tradeItemVo;

        private boolean hasAllOrderDiscount;

        private List<TradeItemVo> extraList;// 加料列表

        public TradeDishDataItem(int type, TradeItemVo tradeItemVo, boolean hasAllOrderDiscount) {
            this.type = type;
            this.tradeItemVo = tradeItemVo;
            this.hasAllOrderDiscount = hasAllOrderDiscount;
        }

        public int getType() {
            return type;
        }

        public TradeItemVo getTradeItemVo() {
            return tradeItemVo;
        }

        public boolean isHasAllOrderDiscount() {
            return hasAllOrderDiscount;
        }

        public void setHasAllOrderDiscount(boolean hasAllOrderDiscount) {
            this.hasAllOrderDiscount = hasAllOrderDiscount;
        }

        public List<TradeItemVo> getExtraList() {
            return extraList;
        }

        public void setExtraList(List<TradeItemVo> extraList) {
            this.extraList = extraList;
        }

        public TradeItemVo getParentTradeItemVo() {
            return parentTradeItemVo;
        }

        public void setParentTradeItemVo(TradeItemVo parentTradeItemVo) {
            this.parentTradeItemVo = parentTradeItemVo;
        }
    }

    /**
     * item禁止点击
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
