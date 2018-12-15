package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.constants.OCConstant;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DishMakeStatus;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.util.DinnerOrderCenterDetailDataBuildTool;
import com.zhongmei.bty.dinner.util.DinnerOrderCenterDetailDataBuildTool.TradeDishDataItem;
import com.zhongmei.bty.dinner.util.GroupOrderCenterDataBuildTool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Date：2015年9月22日 上午18:28:04
 * @Description: 票据中心详细 商品信息adapter
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class OrderCenterDishAdapter extends BaseAdapter {

    /**
     * 默认item类型
     */
    private final static int DEFAULT_TYPE = -1;
    /**
     * 菜品
     */
    private final static int DISH = 0;
    /**
     * 备注／折扣
     */
    private final static int MEMO_OR_DISCOUNT = 1;
    /**
     * 营销活动
     */
    private final static int PLAN_ACTIVITY = 2;

    private Context mContext;

    private LayoutInflater mInflater;

    private List<TradeDishDataItem> mDataSet;

    private Drawable mChildIcon = null;

    private Drawable mAllOrderDiscount = null;

    private DinnerOrderCenterDetailDataBuildTool buildDataTool;

    private int mFromType = OCConstant.FromType.FROM_TYPE_SNACK;

    //是否是退货单,退货单优惠信息取反
    private boolean isRefund;

    public OrderCenterDishAdapter(Context context, Boolean isRefund) {
        this.mContext = context;
        buildDataTool = new DinnerOrderCenterDetailDataBuildTool();
        this.mInflater = LayoutInflater.from(mContext);
        this.mDataSet = new ArrayList<TradeDishDataItem>();
        this.mChildIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_child_icon);
        this.mAllOrderDiscount = context.getResources().getDrawable(R.drawable.cashier_order_dish_alldiscount_icon);
        this.isRefund = isRefund;
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
            if (tradeVo.getTrade().getBusinessType() == BusinessType.GROUP) {
                buildDataTool = new GroupOrderCenterDataBuildTool();
            }
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
                case DISH:
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_list_item_01, parent, false);
                    holder.vTopLine = convertView.findViewById(R.id.v_top_line);
                    holder.llDishView = (LinearLayout) convertView.findViewById(R.id.ll_dish_view);
                    holder.tvDishName = (TextView) convertView.findViewById(R.id.tv_dish_name);
                    holder.tvTechnician = (TextView) convertView.findViewById(R.id.tv_technician);
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
                    holder.enableWholePrivilege = (ImageView) convertView.findViewById(R.id.enable_whole_privilege);
                    holder.produceStatus = (TextView) convertView.findViewById(R.id.produce_status);
                    holder.ivPackIcon = (TextView) convertView.findViewById(R.id.pack_flag);
                    break;
                case MEMO_OR_DISCOUNT:
                    convertView = mInflater.inflate(R.layout.billcenter_trade_dish_list_item_03, parent, false);
                    holder.llDishPrivilege = (LinearLayout) convertView.findViewById(R.id.ll_dish_privilege);
                    holder.tvPrivilegeType = (TextView) convertView.findViewById(R.id.tv_privilege_type);
                    holder.tvPrivilegeAmount = (TextView) convertView.findViewById(R.id.tv_privilege_amount);
                    holder.tvDishMemo = (TextView) convertView.findViewById(R.id.tv_dish_memo);
                    break;
                case PLAN_ACTIVITY:
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
        int itemViewType = DEFAULT_TYPE;
        switch (dishDataItem.getType()) {
            case TradeDishDataItem.ITEM_TYPE_SINGLE:
            case TradeDishDataItem.ITEM_TYPE_COMBO:
            case TradeDishDataItem.ITEM_TYPE_COMBO_CHILD:
                itemViewType = DISH;
                break;
            case TradeDishDataItem.ITEM_TYPE_SINGLE_MEMO_AND_DISCOUNT:
            case TradeDishDataItem.ITEM_TYPE_COMBO_MEMO_AND_DISCOUNT:
            case TradeDishDataItem.ITEM_TYPE_COMBO_CHILD_MEMO:
                itemViewType = MEMO_OR_DISCOUNT;
                break;
            case TradeDishDataItem.ITEM_TYPE_PLAN_ACTIVITY:
                itemViewType = PLAN_ACTIVITY;
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
            case DISH:
                TradeItem tradeItem = tradeItemVo.getTradeItem();
                // 设置菜品名称、数量、价格、口味、做法格式
                if (type == TradeDishDataItem.ITEM_TYPE_COMBO_CHILD) {
                    holder.llDishView.setLayoutParams(getIsComboDiyWh(mContext));
                    holder.tvDishName.setTextSize(12);
                    holder.tvDishName.setPadding(DensityUtil.dip2px(MainApplication.getInstance(), 24), 0, 0, 0);
                    holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(mChildIcon, null, null, null);
                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    BigDecimal dishnum =
                            ShopcartItemUtils.computeSingleQty(tradeItem.getQuantity(),
                                    tradeDishDataItem.getParentTradeItemVo().getTradeItem(),
                                    null);
                    holder.tvDishNum.setText(MathDecimal.toTrimZeroString(dishnum.abs()));
                    // holder.tvDishNum.setText(MathDecimal.toTrimZeroString(tradeItem.getQuantity()));
                    if (tradeItem.getPrice().compareTo(BigDecimal.ZERO) != 0) {
                        holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().abs().doubleValue()));
                    } else {
                        holder.tvSingleDishPrice.setText("");
                    }
                    if (tradeItem.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                        holder.tvDishPrice.setText(Utils.formatPrice(tradeItem.getAmount().abs().doubleValue()));
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
                    holder.tvDishName.setTextSize(12);
                    if (tradeDishDataItem.isHasAllOrderDiscount()) {
                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        // 不显示整单打折的图标
                        // holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(mAllOrderDiscount,
                        // null, null, null);
                    } else {
                        holder.tvDishName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                    holder.tvDishName.setText(genDishName(tradeItemVo));
                    holder.tvDishNum.setText(MathDecimal.toTrimZeroString(tradeItem.getQuantity().abs()));
                    holder.tvSingleDishPrice.setText(Utils.formatPrice(tradeItem.getPrice().abs().doubleValue()));
                    holder.tvDishPrice.setText(Utils.formatPrice(tradeItem.getAmount().abs().doubleValue()));

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
                    if (tradeItemVo.getTradeItemExtra() != null
                            && tradeItemVo.getTradeItemExtra().getIsPack() == Bool.YES) {//打包标记
                        holder.ivPackIcon.setVisibility(View.VISIBLE);
                    } else {
                        holder.ivPackIcon.setVisibility(View.GONE);
                    }
                    Bool enableWholePrivilege = tradeItem.getEnableWholePrivilege();
                    if (enableWholePrivilege == Bool.NO) {
                        holder.enableWholePrivilege.setVisibility(View.VISIBLE);
                    } else {
                        holder.enableWholePrivilege.setVisibility(View.GONE);
                    }
                }

                //添加技师
                setTechnician(tradeItemVo, holder.tvTechnician);

                /**
                 * 口味做法
                 */
                List<TradeItemProperty> propertyList = buildDataTool.filterTradeItemProperty(tradeItemVo, PropertyKind.PROPERTY);
                if (Utils.isNotEmpty(propertyList)) {
                    holder.tvPropertyName.setText(getPropertyName(propertyList));
                    holder.tvPropertyQuantity.setVisibility(View.GONE);// 现在不需要显示口味做法的数量
                    holder.tvPropertyQuantity.setText(MathDecimal.toTrimZeroString(getPropertyQuantity(propertyList)));
                    if (isRefund) {
                        holder.tvPropertyAmount.setText(Utils.formatPrice(getPropertyAmount(propertyList).negate().doubleValue()));
                    } else {
                        holder.tvPropertyAmount.setText(Utils.formatPrice(getPropertyAmount(propertyList).doubleValue()));
                    }
                    holder.llDishProperty.setVisibility(View.VISIBLE);
                } else {
                    holder.llDishProperty.setVisibility(View.GONE);
                }

                // 加料
                setExtraView(tradeDishDataItem, holder);
                if (type == TradeDishDataItem.ITEM_TYPE_COMBO) {
                    holder.produceStatus.setVisibility(View.GONE);//套餐外壳不显示kds制作状态
                } else {
                    processProducesStatus(tradeItemVo, holder.produceStatus);
                }

                if (mFromType == OCConstant.FromType.FROM_TYPE_RETAIL) {
                    holder.produceStatus.setVisibility(View.GONE);
                }

                break;

            case MEMO_OR_DISCOUNT:
                // 设置折扣以及备注格式
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

                /**
                 * 菜品折扣
                 */
                TradePrivilege tradePrivilege = tradeItemVo.getTradeItemPrivilege();
                TradeReasonRel tradeReasonRel = tradeItemVo.getReasonLast();
//                if(tradeReasonRel == null){
//                    tradeReasonRel = tradeItemVo.getReasonLast();
//                }
                if (tradePrivilege != null) {
                    switch (tradePrivilege.getPrivilegeType()) {
                        case DISCOUNT:
                            String privilegeValue =
                                    MathDecimal.toTrimZeroString(tradePrivilege.getPrivilegeValue().divide(BigDecimal.TEN));
                            if (tradeReasonRel != null) {
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(privilegeValue
                                            + mContext.getResources().getString(R.string.discount1) + "    " + tradeReasonRel.getReasonContent() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(privilegeValue
                                            + mContext.getResources().getString(R.string.discount1) + "    " + tradeReasonRel.getReasonContent() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            } else {
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(privilegeValue
                                            + mContext.getResources().getString(R.string.discount1) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(privilegeValue
                                            + mContext.getResources().getString(R.string.discount1) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            }
//                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//                                    .doubleValue()));
                            holder.tvPrivilegeAmount.setVisibility(View.GONE);
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;

                        case FREE:
                        case GIVE:
                            if (tradeReasonRel != null) {
                                String reasonPre = mContext.getString(R.string.give_reason_label);
                                holder.tvPrivilegeType.setText(mContext.getString(R.string.give) + "    " + reasonPre + tradeReasonRel.getReasonContent() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                            } else {
                                holder.tvPrivilegeType.setText(mContext.getString(R.string.give) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                            }
//                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//                                    .doubleValue()));
                            holder.tvPrivilegeAmount.setVisibility(View.GONE);
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;

                        case REBATE:
                            if (tradeReasonRel != null) {
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.letThePrice) + "    " + tradeReasonRel.getReasonContent() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.letThePrice) + "    " + tradeReasonRel.getReasonContent() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            } else {
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.letThePrice) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.letThePrice) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            }
//                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//                                    .doubleValue()));
                            holder.tvPrivilegeAmount.setVisibility(View.GONE);
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;

                        case AUTO_DISCOUNT:
                            String autoPrivilegeValue =
                                    MathDecimal.toTrimZeroString(tradePrivilege.getPrivilegeValue().divide(BigDecimal.TEN));
                            if (isRefund) {
                                holder.tvPrivilegeType.setText(mContext.getString(R.string.auto_discount,
                                        autoPrivilegeValue) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                            } else {
                                holder.tvPrivilegeType.setText(mContext.getString(R.string.auto_discount,
                                        autoPrivilegeValue) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                            }
//                            holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount()
//                                    .doubleValue()));
                            holder.tvPrivilegeAmount.setVisibility(View.GONE);
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;
                        case MEMBER_PRICE:
                            // 会员价。会员让价
                            if (!TextUtils.isEmpty(tradePrivilege.getPrivilegeName())) {
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(tradePrivilege.getPrivilegeName() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(tradePrivilege.getPrivilegeName() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            } else {
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.member_price) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.member_price) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            }
//                          holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()));
                            holder.tvPrivilegeAmount.setVisibility(View.GONE);
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;
                        case PROBLEM:
                            if (tradeReasonRel != null) {
                                String reasonPre = mContext.getString(R.string.problem_dishes_reason);
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(reasonPre + "    " + tradeReasonRel.getReasonContent() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(reasonPre + "    " + tradeReasonRel.getReasonContent() + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            } else {
                                if (isRefund) {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.problem_dishes) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                                } else {
                                    holder.tvPrivilegeType.setText(mContext.getString(R.string.problem_dishes) + " (" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                                }
                            }
                            holder.tvPrivilegeAmount.setVisibility(View.GONE);
                            holder.llDishPrivilege.setVisibility(View.VISIBLE);
                            break;
                        default:
                            holder.llDishPrivilege.setVisibility(View.GONE);
                            break;
                    }
                } else {
                    //礼品券add 2016.10.21
                    if (tradeItemVo.getCouponPrivilegeVo() != null && tradeItemVo.getCouponPrivilegeVo().getTradePrivilege() != null) {
                        tradePrivilege = tradeItemVo.getCouponPrivilegeVo().getTradePrivilege();
                        if (isRefund) {
                            holder.tvPrivilegeType.setText(mContext.getString(R.string.coupon_gift) + "(" + tradePrivilege.getPrivilegeName() + ")" + "(" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().negate().doubleValue()) + ")");
                        } else {
                            holder.tvPrivilegeType.setText(mContext.getString(R.string.coupon_gift) + "(" + tradePrivilege.getPrivilegeName() + ")" + "(" + Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()) + ")");
                        }
//                        holder.tvPrivilegeAmount.setText(Utils.formatPrice(tradePrivilege.getPrivilegeAmount().doubleValue()));
                        holder.tvPrivilegeAmount.setVisibility(View.GONE);
                        holder.llDishPrivilege.setVisibility(View.VISIBLE);
                    } else {
                        holder.llDishPrivilege.setVisibility(View.GONE);
                    }
                }

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
            case PLAN_ACTIVITY:
                String ruleName = "";

                if (ruleName != null) {
                    ruleName = mContext.getString(R.string.activity_title) + tradeDishDataItem.getTradePlanActivity().getRuleName();//营销活动
                    //holder.tvPlanActivity.setText("【活动】"+ruleName);

                } else {
                    ruleName = mContext.getString(R.string.activity_title);
                    //holder.tvPlanActivity.setText("【活动】");
                }
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(ruleName);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.market_activity_red));
                stringBuilder.setSpan(colorSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                BigDecimal totalBigDecimal = tradeDishDataItem.getTradePlanActivity().getOfferValue();//总金额
                if (isRefund) {
                    totalBigDecimal = totalBigDecimal.negate();
                }
                stringBuilder.append("(" + Utils.formatPrice(totalBigDecimal.doubleValue()) + ")");
                holder.tvPlanActivity.setText(stringBuilder);
//              BigDecimal totalBigDecimal = tradeDishDataItem.getTradePlanActivity().getOfferValue();//总金额
//              holder.tvPlanPrice.setText(Utils.formatPrice(totalBigDecimal.doubleValue()));
                holder.tvPlanPrice.setVisibility(View.GONE);
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

    private void setTechnician(TradeItemVo itemVo, TextView textView) {
        List<TradeUser> userList = itemVo.getTradeItemUserList();
        if (Utils.isNotEmpty(userList)) {
            textView.setVisibility(View.VISIBLE);
            StringBuffer buffer = new StringBuffer("(");
            for (TradeUser tradeItemUser : userList) {
                buffer.append(tradeItemUser.getUserName());
                buffer.append(",");
            }

            String userNames = buffer.subSequence(0, buffer.length() - 1).toString() + ")";
            textView.setText(userNames);
        } else {
            textView.setVisibility(View.GONE);
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
            holder.tvExtraQuantity.setText(MathDecimal.toTrimZeroString(extraQuantity));
            holder.tvExtraQuantity.setVisibility(View.GONE);// 现在不需要显示加料的数量
            if (isRefund) {
                holder.tvExtraAmount.setText(Utils.formatPrice(getExtraAmount(extraList).negate().doubleValue()));
            } else {
                holder.tvExtraAmount.setText(Utils.formatPrice(getExtraAmount(extraList).doubleValue()));
            }

            holder.llDishExtra.setVisibility(View.VISIBLE);
        } else {
            holder.llDishExtra.setVisibility(View.GONE);
            holder.tvExtraQuantity.setVisibility(View.GONE);// 现在不需要显示加料的数量
        }
    }

    private void processProducesStatus(TradeItemVo tradeItemVo, TextView produceStatus) {
        TradeItemExtra tradeItemExtra = tradeItemVo.getTradeItemExtra();
        if (tradeItemExtra != null && ShopInfoCfg.getInstance().isExistKdsDevice()) {
            DishMakeStatus dishMakeStatus = tradeItemExtra.getDishMakeStatus();
            produceStatus.setVisibility(View.VISIBLE);
            switch (dishMakeStatus) {
                case WAITING:
//                    produceStatus.setBackgroundResource(R.drawable.produce_status_wait);
                    produceStatus.setBackgroundResource(R.drawable.lable_bg_orange_border_fdaf33_2px_radius);
                    produceStatus.setTextColor(Color.parseColor("#FDAF33"));
                    produceStatus.setText(R.string.dinner_waiting);
                    break;
                case MATCHING:
//                    produceStatus.setBackgroundResource(R.drawable.produce_status_preparation);
                    produceStatus.setBackgroundResource(R.drawable.lable_bg_orange_border_fdaf33_2px_radius);
                    produceStatus.setTextColor(Color.parseColor("#FDAF33"));
                    produceStatus.setText(R.string.dinner_jardiniering);
                    break;
                case MAKING:
//                    produceStatus.setBackgroundResource(R.drawable.produce_status_producing);
                    produceStatus.setBackgroundResource(R.drawable.lable_bg_orange_border_fdaf33_2px_radius);
                    produceStatus.setTextColor(Color.parseColor("#FDAF33"));
                    produceStatus.setText(R.string.dinner_cooking);
                    break;
                case FINISHED:
//                    produceStatus.setBackgroundResource(R.drawable.produce_status_completed);
                    produceStatus.setBackgroundResource(R.drawable.lable_bg_green_border_4dd5b7_2px_radius);
                    produceStatus.setTextColor(Color.parseColor("#4dd5b7"));
                    produceStatus.setText(R.string.dinner_finished);
                    break;
                default:
                    produceStatus.setVisibility(View.GONE);
                    break;
            }
        } else {
            produceStatus.setVisibility(View.GONE);
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


    //根据TradePlanActivity来排序
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

    public void setFromType(int fromType) {
        mFromType = fromType;
    }

    static class ViewHolder {
        View vTopLine;

        LinearLayout llDishView;

        TextView tvDishName;// 菜品名称

        TextView tvTechnician;//美业技师

        TextView tvDishNum;// 菜品数量

        TextView tvDishPrice;// 菜品价格

        TextView tvSingleDishPrice;// 菜品单价价格

        LinearLayout llDishProperty;// 菜品属性，包括做法、口味

        TextView tvPropertyName;

        TextView tvPropertyQuantity;

        TextView tvPropertyAmount;

        LinearLayout llDishExtra;// 加料

        TextView tvExtraName;

        TextView tvExtraQuantity;

        TextView tvExtraAmount;

        LinearLayout llDishPrivilege;// 菜品折扣

        TextView tvPrivilegeType;// 折扣类型

        TextView tvPrivilegeAmount;// 折扣数量

        TextView tvDishMemo;// 菜品备注

        TextView tvPlanActivity;//营销活动

        TextView tvPlanPrice;//营销活动总金额

        View v_line;//营销横线

        TextView produceStatus;//商品状态

        ImageView enableWholePrivilege;//整单折扣不参

        TextView ivPackIcon;//打包
    }


}
