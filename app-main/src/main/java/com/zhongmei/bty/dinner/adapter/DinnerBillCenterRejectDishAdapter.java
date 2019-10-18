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
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.util.DinnerOrderCenterDetailDataBuildTool;
import com.zhongmei.bty.dinner.util.DinnerOrderCenterDetailDataBuildTool.TradeDishDataItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class DinnerBillCenterRejectDishAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mInflater;

    private List<TradeDishDataItem> mDataSet;

    private Drawable mChildIcon = null;

    private Drawable mAllOrderDiscount = null;

    private DinnerOrderCenterDetailDataBuildTool buildDataTool;

    public DinnerBillCenterRejectDishAdapter(Context context) {
        buildDataTool = new DinnerOrderCenterDetailDataBuildTool();
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

            List<TradeDishDataItem> orderCenterDishDataItems = buildDataTool.buildDishDataList(tradeVo, true);
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
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_reject_list_item_01, parent, false);
                    holder.vTopLine = convertView.findViewById(R.id.v_top_line);
                    holder.llDishView = (LinearLayout) convertView.findViewById(R.id.ll_dish_view);
                    holder.tvdishReturn = (TextView) convertView.findViewById(R.id.tv_dish_return);
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
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_reject_list_item_03, parent, false);
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

                case 3:
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_reject_reason, parent, false);
                    holder.tv_reason = (TextView) convertView.findViewById(R.id.tv_reason);
                    break;
                case 4:
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_reject_name_time, parent, false);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
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
        int type = buildDataTool.getAdaterViewType(dishDataItem);

        return type;
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
                    holder.tvdishReturn.setVisibility(View.GONE);
                    holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(mChildIcon, null, null, null);
                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    BigDecimal dishnum = getComboChildDishNum(tradeDishDataItem, tradeItemVo);
                    holder.tvDishNum.setText(MathDecimal.toAbsTrimZeroString(dishnum));
                    if (tradeItem.getPrice().compareTo(BigDecimal.ZERO) != 0) {
                        holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().doubleValue()));
                    } else {
                        holder.tvSingleDishPrice.setText("");
                    }
                    if (tradeItem.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                        holder.tvDishPrice.setText(getReturnTotalPrice(tradeItemVo));
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

                    holder.tvdishReturn.setText(getReturnNum(tradeItemVo));

                    holder.tvdishReturn.setVisibility(View.VISIBLE);
                    if (tradeDishDataItem.isHasAllOrderDiscount()) {

                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    } else {
                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    holder.tvDishNum.setText(getDishNum(tradeItemVo));
                    holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().doubleValue()));
                    holder.tvDishPrice.setText(getReturnTotalPrice(tradeItemVo));

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


                setPropertyView(tradeItemVo, holder);

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
                if (tradePrivilege != null) {
                    switch (tradePrivilege.getPrivilegeType()) {
                        case DISCOUNT:
                            String privilegeValue =
                                    MathDecimal.toTrimZeroString(MathDecimal.div(tradePrivilege.getPrivilegeValue(), 10, 2));
                            holder.tvPrivilegeType.setText(privilegeValue
                                    + mContext.getResources().getString(R.string.discount1));
                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
                                    .doubleValue()));
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;

                        case FREE:
                            holder.tvPrivilegeType.setText(mContext.getString(R.string.give));
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
                                    MathDecimal.toTrimZeroString(MathDecimal.div(tradePrivilege.getPrivilegeValue(), 10, 2));
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
                    holder.llDishPrivilege.setVisibility(View.GONE);
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

            case 3:
                String reasoncontent = "";
                if (tradeItemVo.getRejectQtyReason() != null) {
                    reasoncontent = tradeItemVo.getRejectQtyReason().getReasonContent();

                }

                holder.tv_reason.setText(String.format(mContext.getString(R.string.reject_reason), reasoncontent));

                break;

            case 4:

                String name = "";
                long time = 0;
                name = tradeItemVo.getTradeItem().getUpdatorName();
                time = tradeItemVo.getTradeItem().getServerUpdateTime();
                if (time != 0) {
                    holder.tv_time.setText(String.format(mContext.getString(R.string.reject_time),
                            DateTimeUtils.formatDateTime(time)));
                }
                holder.tv_name.setText(String.format(mContext.getString(R.string.reject_operation), name));

                break;

            default:
                break;
        }
    }

    private BigDecimal getComboChildDishNum(TradeDishDataItem tradeDishDataItem, TradeItemVo tradeItemVo) {
        TradeItem tradeItem = tradeItemVo.getTradeItem();
        if (tradeItem.getInvalidType() == InvalidType.MODIFY_DISH) {
            return tradeItem.getQuantity().subtract(tradeItemVo.getModifyQuantity());
        }
        return ShopcartItemUtils.computeReturnSingleQty(tradeItem.getReturnQuantity(),
                tradeDishDataItem.getParentTradeItemVo().getTradeItem(),
                null);
    }

    private String getDishNum(TradeItemVo tradeItemVo) {
        TradeItem tradeItem = tradeItemVo.getTradeItem();
        if (tradeItem.getInvalidType() == InvalidType.MODIFY_DISH) {
            return MathDecimal.toAbsTrimZeroString(tradeItem.getQuantity().subtract(tradeItemVo.getModifyQuantity()));
        }
        return MathDecimal.toAbsTrimZeroString(tradeItem.getReturnQuantity());
    }

    private void setExtraView(TradeDishDataItem tradeDishDataItem, ViewHolder holder) {
        List<TradeItemVo> extraList = tradeDishDataItem.getExtraList();
        TradeItem tradeItem = tradeDishDataItem.getTradeItemVo().getTradeItem();
        if (Utils.isNotEmpty(extraList)) {
            String extraName = "";
            BigDecimal extraRetrunQuantity = BigDecimal.ZERO;
            if (tradeDishDataItem.getType() == TradeDishDataItem.ITEM_TYPE_COMBO_CHILD) {
                TradeItem parentTradeItem = tradeDishDataItem.getParentTradeItemVo().getTradeItem();
                if (parentTradeItem.getSaleType() == SaleType.UNWEIGHING
                        && tradeItem.getSaleType() == SaleType.UNWEIGHING) {
                    extraName = getExtraName(extraList, tradeItem.getReturnQuantity());
                    extraRetrunQuantity = getExtraQuantity(extraList, tradeItem.getReturnQuantity());
                } else if (parentTradeItem.getSaleType() == SaleType.UNWEIGHING
                        && tradeItem.getSaleType() == SaleType.WEIGHING) {
                    extraName = getExtraName(extraList, parentTradeItem.getReturnQuantity());
                    extraRetrunQuantity = getExtraQuantity(extraList, parentTradeItem.getReturnQuantity());
                } else if (parentTradeItem.getSaleType() == SaleType.WEIGHING
                        && tradeItem.getSaleType() == SaleType.UNWEIGHING) {
                    extraName = getExtraName(extraList, tradeItem.getReturnQuantity());
                    extraRetrunQuantity = getExtraQuantity(extraList, tradeItem.getReturnQuantity());
                } else {
                    extraName = getExtraName(extraList, BigDecimal.ONE);
                    extraRetrunQuantity = getExtraQuantity(extraList, BigDecimal.ONE);
                }
            } else {
                if (tradeItem.getSaleType() == SaleType.UNWEIGHING) {
                    extraName = getExtraName(extraList, tradeItem.getReturnQuantity());
                    extraRetrunQuantity = getExtraQuantity(extraList, tradeItem.getReturnQuantity());
                } else {
                    extraName = getExtraName(extraList, BigDecimal.ONE);
                    extraRetrunQuantity = getExtraQuantity(extraList, BigDecimal.ONE);
                }
            }
            holder.tvExtraName.setText(extraName);
                        holder.tvExtraQuantity.setText(MathDecimal.toAbsTrimZeroString(extraRetrunQuantity));
            holder.tvExtraQuantity.setVisibility(View.GONE);            holder.tvExtraAmount.setText(Utils.formatPrice(MathDecimal.toAbsTrimZeroString(getExtraAmount(extraList))));
            holder.llDishExtra.setVisibility(View.VISIBLE);
        } else {
            holder.llDishExtra.setVisibility(View.GONE);
            holder.tvExtraQuantity.setVisibility(View.GONE);        }
    }

    private void setPropertyView(TradeItemVo tradeItemVo, ViewHolder holder) {
        List<TradeItemProperty> propertyList = filterTradeItemProperty(tradeItemVo, PropertyKind.PROPERTY);
        if (Utils.isNotEmpty(propertyList)) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();

            holder.tvPropertyName.setText(getPropertyName(propertyList));

            String quantity = getPropertyQuantity(tradeItem, propertyList);
            holder.tvPropertyQuantity.setText(quantity);
            holder.tvPropertyQuantity.setVisibility(View.GONE);            String amount = getPropertyAmount(tradeItem, propertyList);
            holder.tvPropertyAmount.setText(amount);
            holder.llDishProperty.setVisibility(View.VISIBLE);
        } else {
            holder.llDishProperty.setVisibility(View.GONE);
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



    private String getExtraName(List<TradeItemVo> extraList, BigDecimal parentItemQuantity) {
        StringBuilder sb = new StringBuilder();
        if (Utils.isNotEmpty(extraList)) {

            int size = extraList.size();
            for (int i = 0; i < size; i++) {
                TradeItem tradeItem = extraList.get(i).getTradeItem();
                sb.append(tradeItem.getDishName())
                        .append("(x")
                        .append(MathDecimal.toAbsTrimZeroString(MathDecimal.div(tradeItem.getReturnQuantity(),
                                parentItemQuantity)))
                        .append(")");
                if (i < size - 1) {
                    sb.append("、");
                }
            }
        }

        return sb.toString();
    }

    private BigDecimal getExtraQuantity(List<TradeItemVo> extraList) {
        BigDecimal quantity = BigDecimal.ZERO;

        if (Utils.isNotEmpty(extraList)) {
            for (TradeItemVo tradeItemVo : extraList) {
                quantity = quantity.add(tradeItemVo.getTradeItem().getReturnQuantity());
            }
        }

        return quantity;
    }

    private BigDecimal getExtraQuantity(List<TradeItemVo> mextraList, BigDecimal parentItemReturnQuantity) {

        BigDecimal quantity = BigDecimal.ZERO;
        List<TradeItemVo> extraList = mextraList;
        if (Utils.isNotEmpty(extraList)) {
            for (TradeItemVo tradeItemVo : extraList) {
                                                quantity =
                        quantity.add(MathDecimal.div(tradeItemVo.getTradeItem().getReturnQuantity(),
                                parentItemReturnQuantity));

            }
        }

        return quantity;
    }

    private BigDecimal getExtraAmount(List<TradeItemVo> extraList) {
        BigDecimal amount = BigDecimal.ZERO;

        if (Utils.isNotEmpty(extraList)) {
            for (TradeItemVo tradeItemVo : extraList) {
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                BigDecimal singleAmount = tradeItem.getPrice().multiply(tradeItem.getReturnQuantity());
                amount = amount.add(singleAmount);
            }
        }

        return amount;
    }


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


    private String getReturnTotalPrice(TradeItemVo tradeItemVo) {
        TradeItem tradeItem = tradeItemVo.getTradeItem();
        if (tradeItem.getInvalidType() == InvalidType.MODIFY_DISH) {
            return Utils.formatPrice(tradeItem.getPrice().multiply(tradeItem.getQuantity().subtract(tradeItemVo.getModifyQuantity())).doubleValue());
        }
                return Utils.formatPrice(tradeItem.getPrice().doubleValue()
                * Math.abs(tradeItem.getReturnQuantity().doubleValue()));

    }

    private String getReturnNum(TradeItemVo tradeItemVo) {
        TradeItem tradeItem = tradeItemVo.getTradeItem();
        if (tradeItem.getInvalidType() == InvalidType.MODIFY_DISH) {
            return String.format(mContext.getString(R.string.reject_modify),
                    MathDecimal.toAbsTrimZeroString(tradeItem.getQuantity()),
                    MathDecimal.toAbsTrimZeroString(tradeItemVo.getModifyQuantity()));
        }

        String returnnum =
                String.format(mContext.getString(R.string.reject_quit),
                        MathDecimal.toAbsTrimZeroString(tradeItem.getQuantity()),
                        MathDecimal.toAbsTrimZeroString(tradeItem.getReturnQuantity()));
        return returnnum;
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


    private String getPropertyQuantity(TradeItem tradeItem, List<TradeItemProperty> tradeItemPropertyList) {
        BigDecimal quantity = BigDecimal.ZERO;

        if (Utils.isNotEmpty(tradeItemPropertyList)) {
            for (int i = 0; i < tradeItemPropertyList.size(); i++) {
                quantity = quantity.add(tradeItem.getReturnQuantity());
            }
        }

        return MathDecimal.toAbsTrimZeroString(quantity);
    }


    private String getPropertyAmount(TradeItem tradeItem, List<TradeItemProperty> tradeItemPropertyList) {
        BigDecimal amount = BigDecimal.ZERO;

        if (Utils.isNotEmpty(tradeItemPropertyList)) {
            for (TradeItemProperty tradeItemProperty : tradeItemPropertyList) {
                if (tradeItemProperty.getAmount() != null && tradeItemProperty.getPrice() != null) {
                    BigDecimal augend = tradeItemProperty.getPrice().multiply(tradeItem.getReturnQuantity());
                    amount = amount.add(augend);
                }
            }
        }

        return Utils.formatPrice(Math.abs(amount.doubleValue()));
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

        TextView tvDishName;
        TextView tvdishReturn;
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
        View v_line;
        TextView tv_reason;
        TextView tv_name;
        TextView tv_time;

    }


}
