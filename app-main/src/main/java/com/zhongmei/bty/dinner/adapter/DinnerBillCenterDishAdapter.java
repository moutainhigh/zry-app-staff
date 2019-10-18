package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.dinner.util.DinnerOrderCenterDetailDataBuildTool;
import com.zhongmei.bty.dinner.util.DinnerOrderCenterDetailDataBuildTool.TradeDishDataItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;

import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DinnerBillCenterDishAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mInflater;

    private List<TradeDishDataItem> mDataSet;

    private Drawable mChildIcon = null;

    private Drawable mAllOrderDiscount = null;

    private DinnerOrderCenterDetailDataBuildTool buildDataTool;

    public DinnerBillCenterDishAdapter(Context context) {
        this.mContext = context;
        buildDataTool = new DinnerOrderCenterDetailDataBuildTool();
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

            List<TradeDishDataItem> orderCenterDishDataItems = buildDataTool.buildDishDataList(tradeVo, false);
            if (orderCenterDishDataItems != null) {
                mDataSet.addAll(orderCenterDishDataItems);
            }


        }
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int itemViewType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (itemViewType) {
                case 0:
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_list_item_01, parent, false);
                    holder.vTopLine = convertView.findViewById(R.id.v_top_line);
                    holder.llDishView = (LinearLayout) convertView.findViewById(R.id.ll_dish_view);
                    holder.tvDishName = (TextView) convertView.findViewById(R.id.tv_dish_name);
                    holder.tvDishNum = (TextView) convertView.findViewById(R.id.tv_dish_num);
                    holder.tvDishPrice = (TextView) convertView.findViewById(R.id.tv_dish_price);
                    holder.tvSingleDishPrice = (TextView) convertView.findViewById(R.id.tv_singledish_price);
                    holder.llDishProperty = (LinearLayout) convertView.findViewById(R.id.ll_dish_property);
                    holder.tvPropertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
                    holder.tvPropertyQuantity = (TextView) convertView.findViewById(R.id.tv_property_quantity);
                    holder.tvPropertyAmount = (TextView) convertView.findViewById(R.id.tv_property_amount);
                    holder.llDishExtra = (LinearLayout) convertView.findViewById(R.id.ll_dish_extra);
                    holder.tvExtraName = (TextView) convertView.findViewById(R.id.tv_extra_name);
                    holder.tvExtraQuantity = (TextView) convertView.findViewById(R.id.tv_extra_quantity);
                    holder.tvExtraAmount = (TextView) convertView.findViewById(R.id.tv_extra_amount);
                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_list_item_03, parent, false);
                    holder.llDishPrivilege = (LinearLayout) convertView.findViewById(R.id.ll_dish_privilege);
                    holder.tvPrivilegeType = (TextView) convertView.findViewById(R.id.tv_privilege_type);
                    holder.tvPrivilegeAmount = (TextView) convertView.findViewById(R.id.tv_privilege_amount);
                    holder.tvDishMemo = (TextView) convertView.findViewById(R.id.tv_dish_memo);
                    break;
                case 2:
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_list_item_02, parent, false);
                    holder.tvPlanActivity = (TextView) convertView.findViewById(R.id.tv_plan_activity);
                    holder.tvPlanPrice = (TextView) convertView.findViewById(R.id.tv_plan_price);
                    holder.v_line = convertView.findViewById(R.id.v_line);
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
        return 5;
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
            case TradeDishDataItem.ITEM_TYPE_PLAN_ACTIVITY:
                itemViewType = 2;
                break;
            default:
                break;
        }
        return itemViewType;
    }

    private void bindView(ViewHolder holder, int position) {
        TradeDishDataItem tradeDishDataItem = (TradeDishDataItem) getItem(position);
        int itemViewType = getItemViewType(position);
        int type = tradeDishDataItem.getType();        TradeItemVo tradeItemVo = tradeDishDataItem.getTradeItemVo();
        switch (itemViewType) {
            case 0:
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                                if (type == TradeDishDataItem.ITEM_TYPE_COMBO_CHILD) {
                    holder.llDishView.setLayoutParams(getIsComboDiyWh(mContext));
                    holder.tvDishName.setTextSize(16);
                    holder.tvDishName.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 24), 0, 0, 0);
                    holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(mChildIcon, null, null, null);
                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    BigDecimal dishnum =
                            ShopcartItemUtils.computeSingleQty(tradeItem.getQuantity(),
                                    tradeDishDataItem.getParentTradeItemVo().getTradeItem(),
                                    null);
                    holder.tvDishNum.setText(MathDecimal.toTrimZeroString(dishnum));
                                        if (tradeItem.getPrice().compareTo(BigDecimal.ZERO) != 0) {
                        holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().doubleValue()));
                    } else {
                        holder.tvSingleDishPrice.setText("");
                    }
                    if (tradeItem.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                        holder.tvDishPrice.setText(Utils.formatPrice(tradeItem.getAmount().doubleValue()));
                    } else {
                        holder.tvDishPrice.setText("");
                    }
                    holder.vTopLine.setVisibility(View.GONE);

                    LinearLayout.LayoutParams diyWh =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                    diyWh.setMargins(0, DensityUtil.dip2px(MainApplication.getInstance(), 10), 0, DensityUtil.dip2px(MainApplication.getInstance(), 10));

                    holder.llDishProperty.setLayoutParams(diyWh);
                    holder.tvPropertyName.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 48), 0, 0, 0);
                    holder.llDishExtra.setLayoutParams(diyWh);
                    holder.tvExtraName.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 48), 0, 0, 0);
                } else {
                    holder.llDishView.setLayoutParams(getNoComboDiyWh(mContext));
                    holder.tvDishName.setPadding(0, 0, 0, 0);
                    holder.tvDishName.setTextSize(16);
                    if (tradeDishDataItem.isHasAllOrderDiscount()) {
                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                                                                            } else {
                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    holder.tvDishNum.setText(MathDecimal.toTrimZeroString(tradeItem.getQuantity()));
                    holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().doubleValue()));
                    holder.tvDishPrice.setText(Utils.formatPrice(tradeItem.getAmount().doubleValue()));

                    if (position != 0) {
                        holder.vTopLine.setVisibility(View.VISIBLE);
                    } else {
                        holder.vTopLine.setVisibility(View.GONE);
                    }
                    if (tradeDishDataItem.getTradePlanActivity() != null) {
                        holder.vTopLine.setVisibility(View.GONE);
                    }

                    LinearLayout.LayoutParams diyWh =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                    diyWh.setMargins(0, DensityUtil.dip2px(MainApplication.getInstance(), 10), 0, DensityUtil.dip2px(MainApplication.getInstance(), 10));

                    holder.llDishProperty.setLayoutParams(diyWh);
                    holder.tvPropertyName.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 24), 0, 0, 0);
                    holder.llDishExtra.setLayoutParams(diyWh);
                    holder.tvExtraName.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 24), 0, 0, 0);
                }


                List<TradeItemProperty> propertyList = buildDataTool.filterTradeItemProperty(tradeItemVo, PropertyKind.PROPERTY);
                if (Utils.isNotEmpty(propertyList)) {
                    holder.tvPropertyName.setText(getPropertyName(propertyList));
                    holder.tvPropertyQuantity.setVisibility(View.GONE);                    holder.tvPropertyQuantity.setText(MathDecimal.toTrimZeroString(getPropertyQuantity(propertyList)));
                    holder.tvPropertyAmount.setText(Utils.formatPrice(getPropertyAmount(propertyList).doubleValue()));
                    holder.llDishProperty.setVisibility(View.VISIBLE);
                } else {
                    holder.llDishProperty.setVisibility(View.GONE);
                }

                                setExtraView(tradeDishDataItem, holder);
                break;

            case 1:
                                if (type == TradeDishDataItem.ITEM_TYPE_COMBO_CHILD_MEMO) {
                    holder.llDishPrivilege.setLayoutParams(getIsComboDiyWh(mContext));
                    holder.tvPrivilegeType.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 48), 0, 0, 0);
                    holder.tvDishMemo.setLayoutParams(getIsComboDiyWh(mContext));
                    holder.tvDishMemo.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 48), 0, 0, 0);
                } else {
                    holder.llDishPrivilege.setLayoutParams(getNoComboDiyWh(mContext));
                    holder.tvPrivilegeType.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 24), 0, 0, 0);
                    holder.tvDishMemo.setLayoutParams(getNoComboDiyWh(mContext));
                    holder.tvDishMemo.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 24), 0, 0, 0);
                }


                TradePrivilege tradePrivilege = tradeItemVo.getTradeItemPrivilege();
                TradeReasonRel tradeReasonRel = tradeItemVo.getDiscountReason();
                if (tradePrivilege != null) {
                    switch (tradePrivilege.getPrivilegeType()) {
                        case DISCOUNT:
                            String privilegeValue =
                                    MathDecimal.toTrimZeroString(tradePrivilege.getPrivilegeValue().divide(BigDecimal.TEN));
                            holder.tvPrivilegeType.setText(privilegeValue
                                    + mContext.getResources().getString(R.string.discount1));
                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
                                    .doubleValue()));
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;

                        case FREE:
                            if (tradeReasonRel != null) {
                                String reasonPre = mContext.getString(R.string.give_reason_label);
                                holder.tvPrivilegeType.setText(mContext.getString(R.string.give) + "    " + reasonPre + tradeReasonRel.getReasonContent());
                            } else {
                                holder.tvPrivilegeType.setText(mContext.getString(R.string.give));
                            }
                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
                                    .doubleValue()));
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;

                        case REBATE:
                            holder.tvPrivilegeType.setText(mContext.getString(R.string.letThePrice));
                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
                                    .doubleValue()));
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;

                        case AUTO_DISCOUNT:
                            String autoPrivilegeValue =
                                    MathDecimal.toTrimZeroString(tradePrivilege.getPrivilegeValue().divide(BigDecimal.TEN));
                            holder.tvPrivilegeType.setText(mContext.getString(R.string.auto_discount,
                                    autoPrivilegeValue));
                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
                                    .doubleValue()));
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;
                        case MEMBER_PRICE:
                                                        holder.tvPrivilegeType.setText(mContext.getString(R.string.member_price));
                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
                                    .doubleValue()));
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;
                        default:
                            holder.llDishPrivilege.setVisibility(View.GONE);
                            break;
                    }
                } else {
                                        if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                        tradePrivilege = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
                        holder.tvPrivilegeType.setText(mContext.getString(R.string.coupon_gift) + "(" + tradePrivilege.getPrivilegeName() + ")");
                        holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
                                .doubleValue()));
                        holder.llDishPrivilege.setVisibility(View.VISIBLE);
                    } else {
                        holder.llDishPrivilege.setVisibility(View.GONE);
                    }
                }


                String memoarrays = buildTradeMemos(tradeItemVo);
                if (!TextUtils.isEmpty(memoarrays)) {
                    holder.tvDishMemo.setText(mContext.getString(R.string.order_center_detail_memo, memoarrays));
                    holder.tvDishMemo.setVisibility(View.VISIBLE);
                } else {
                    holder.tvDishMemo.setVisibility(View.GONE);
                }
                break;
            case 2:
                String ruleName = "";

                if (ruleName != null) {
                    ruleName = mContext.getString(R.string.activity_title) + tradeDishDataItem.getTradePlanActivity().getRuleName();
                } else {
                    ruleName = mContext.getString(R.string.activity_title);
                                    }
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(ruleName);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.market_activity_red));
                stringBuilder.setSpan(colorSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.tvPlanActivity.setText(stringBuilder);
                BigDecimal totalBigDecimal = tradeDishDataItem.getTradePlanActivity().getOfferValue();
                holder.tvPlanPrice.setText(Utils.formatPrice(totalBigDecimal.doubleValue()));
                if (position + 1 >= mDataSet.size()) {
                    holder.v_line.setVisibility(View.GONE);
                } else {
                    holder.v_line.setVisibility(View.VISIBLE);
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
                        holder.tvExtraQuantity.setText(MathDecimal.toTrimZeroString(extraQuantity));
            holder.tvExtraQuantity.setVisibility(View.GONE);            holder.tvExtraAmount.setText(Utils.formatPrice(getExtraAmount(extraList).doubleValue()));
            holder.llDishExtra.setVisibility(View.VISIBLE);
        } else {
            holder.llDishExtra.setVisibility(View.GONE);
            holder.tvExtraQuantity.setVisibility(View.GONE);        }
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


        private Map<TradePlanActivity, List<TradeItemVo>> sortMapByKey(Map<TradePlanActivity, List<TradeItemVo>> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<TradePlanActivity, List<TradeItemVo>> sortedMap = new TreeMap<TradePlanActivity, List<TradeItemVo>>(new Comparator<TradePlanActivity>() {
            @Override
            public int compare(TradePlanActivity t0, TradePlanActivity t1) {
                if (t0 == null || t1 == null) {
                    return -1;
                } else if (t0.getClientUpdateTime() <= t1.getClientUpdateTime()) {
                    return 1;
                } else {
                    return -1;
                }

            }
        });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }



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


    private String genDishName(TradeItemVo tradeItemVo) {
        StringBuilder sb = new StringBuilder(tradeItemVo.getTradeItem().getDishName());
        List<TradeItemProperty> tradeItemProperties = buildDataTool.filterTradeItemProperty(tradeItemVo, PropertyKind.STANDARD);
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


    private BigDecimal getPropertyQuantity(List<TradeItemProperty> tradeItemPropertyList) {
        BigDecimal quantity = BigDecimal.ZERO;

        if (Utils.isNotEmpty(tradeItemPropertyList)) {
            for (TradeItemProperty tradeItemProperty : tradeItemPropertyList) {
                quantity = quantity.add(tradeItemProperty.getQuantity());
            }
        }

        return quantity;
    }


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
            List<TradeItemProperty> memoList = buildDataTool.filterTradeItemProperty(tradeItemVo, PropertyKind.MEMO);

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

        TextView tvDishName;
        TextView tvDishNum;
        TextView tvDishPrice;
        TextView tvSingleDishPrice;
        LinearLayout llDishProperty;
        TextView tvPropertyName;

        TextView tvPropertyQuantity;

        TextView tvPropertyAmount;

        LinearLayout llDishExtra;
        TextView tvExtraName;

        TextView tvExtraQuantity;

        TextView tvExtraAmount;

        LinearLayout llDishPrivilege;
        TextView tvPrivilegeType;
        TextView tvPrivilegeAmount;
        TextView tvDishMemo;
        TextView tvPlanActivity;
        TextView tvPlanPrice;
        View v_line;    }


}
