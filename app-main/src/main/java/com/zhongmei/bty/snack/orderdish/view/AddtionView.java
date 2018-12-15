package com.zhongmei.bty.snack.orderdish.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

/**
 * 加料
 */
@EViewGroup(R.layout.cashier_order_dish_recipe)
public class AddtionView extends LinearLayout {
    private List<ViewHolder> viewHolders;

    private List<ExtraInfo> mList;
    //
    @ViewById(R.id.recipe_view)
    LinearLayout addtion_view;

    private OnAddtionCLicked mListener;

    public OnAddtionCLicked getListener() {
        return mListener;
    }

    public void setListener(OnAddtionCLicked mListener) {
        this.mListener = mListener;
    }

    private List<LinearLayout> rootViews;

    public AddtionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @AfterViews
    void init() {
        setOrientation(VERTICAL);
    }

    public void setList(List<OrderExtra> list) {
        mList = toExtraInfos(list);
        inflateAddtion();

    }


    public AddtionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddtionView(Context context) {
        this(context, null);
    }

    /**
     * @Title: inflateAddtion
     * @Description: TODO
     * @Param TODO
     * @Return void 返回类型
     */
    private void inflateAddtion() {
        addtion_view.removeAllViews();
        if (rootViews != null) {
            rootViews.clear();
            rootViews = null;
        }
        TextView tv = new TextView(getContext());
        tv.setText(R.string.dish_extra_title);
        tv.setTextSize(24);
        tv.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.text_pay_method));
        LayoutParams params = new LayoutParams(-2, -2);
        params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 20);
        params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 10);
        addtion_view.addView(tv, params);
        List<List<ExtraInfo>> holders = new ArrayList<List<ExtraInfo>>();
        for (int i = 0; i < mList.size(); i++) {
            if (i % 2 == 0) {
                List<ExtraInfo> list = new ArrayList<ExtraInfo>();
                list.add(mList.get(i));
                holders.add(list);
            } else {
                holders.get(holders.size() - 1).add(mList.get(i));
            }

        }
        for (int i = 0; i < holders.size(); i++) {
            View root_view = null;
            ViewHolder holder = null;
            for (int j = 0; j < holders.get(i).size(); j++) {
                holder = new ViewHolder();
                if (j == 0) {
                    root_view = LayoutInflater.from(getContext()).inflate(R.layout.add, null);
                    holder.text_name = (TextView) root_view.findViewById(R.id.name0);
                    holder.text_price = (TextView) root_view.findViewById(R.id.price0);
                    holder.ll = (LinearLayout) root_view.findViewById(R.id.count_ll0);
                    holder.count = (TextView) root_view.findViewById(R.id.count0);
                    holder.plus = (ImageView) root_view.findViewById(R.id.plus0);
                    holder.minus = (ImageView) root_view.findViewById(R.id.minus0);
                } else if (j == 1) {
                    holder.text_name = (TextView) root_view.findViewById(R.id.name1);
                    holder.text_price = (TextView) root_view.findViewById(R.id.price1);
                    holder.ll = (LinearLayout) root_view.findViewById(R.id.count_ll1);
                    holder.count = (TextView) root_view.findViewById(R.id.count1);
                    holder.plus = (ImageView) root_view.findViewById(R.id.plus1);
                    holder.minus = (ImageView) root_view.findViewById(R.id.minus1);
                }
                ExtraInfo extraInfo = holders.get(i).get(j);
                holder.text_name.setText(extraInfo.getName());
                final LinearLayout parentView = (LinearLayout) (holder.text_name.getParent());
                parentView.setId(i * 2 + j);
                if (extraInfo.getQty() != null && extraInfo.getQty().compareTo(BigDecimal.ZERO) == 1) {
                    holder.count.setText(MathDecimal.toTrimZeroString(extraInfo.getQty()));
                    holder.text_name.setMaxHeight(8);
                    holder.text_price.setText("+" + ShopInfoCfg.formatCurrencySymbol(
                            MathDecimal.toTrimZeroString(extraInfo.getAmount())));
                    parentView.setSelected(true);
                } else {
                    holder.text_price.setText("+" + ShopInfoCfg.formatCurrencySymbol(
                            MathDecimal.toTrimZeroString(extraInfo.getPrice())));
                    holder.ll.setVisibility(View.GONE);
                    parentView.setSelected(false);
                }

                ((View) holder.text_name.getParent()).setTag(i * 2 + j);
                parentView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mList.get(v.getId()).getQty() == null
                                || mList.get(v.getId()).getQty().compareTo(BigDecimal.ZERO) == 0) {
                            LinearLayout llLayout = (LinearLayout) ((LinearLayout) v).getChildAt(2);
                            v.setSelected(true);
                            llLayout.setVisibility(View.VISIBLE);
                            TextView tvCount = (TextView) llLayout.getChildAt(1);
                            tvCount.setText("1");
                            mList.get(v.getId()).setQty(BigDecimal.ONE);
                            mListener.onAddtionClicked(mList.get(v.getId()), BigDecimal.valueOf(1));
                        }
                        // holder.ll.setVisibility(View.VISIBLE);
                    }
                });
                holder.minus.setId(i * 2 + j);
                holder.minus.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int postion = v.getId();
                        BigDecimal singleQty = mList.get(postion).getQty();
                        if (singleQty.compareTo(BigDecimal.ZERO) > 0) {
                            int count = singleQty.intValue() - 1;
                            setExtraCount(parentView, postion, count);
                            //数量小于等于零时，点击minus按钮一般不会出现，属于异常状况，直接隐藏＋－按钮视图
                        } else {
                            hidenCount(postion);
                            parentView.setSelected(false);
                        }
                    }

                });
                holder.plus.setId(i * 2 + j);
                holder.plus.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int postion = v.getId();
                        int count = mList.get(postion).getQty().intValue() + 1;
                        setExtraCount(parentView, postion, count);
                    }

                });
                if (mList.size() % 2 != 0 && i == holders.size() - 1 && j == 0) {
                    try {
                        LinearLayout ll = (LinearLayout) parentView.getParent();
                        ll.getChildAt(1).setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        Log.e("xxxxx", e.toString());
                    }
                }
                if (rootViews == null)
                    rootViews = new ArrayList<LinearLayout>();
                rootViews.add(parentView);
                if (viewHolders == null) {
                    viewHolders = new ArrayList<ViewHolder>();
                }
                viewHolders.add(holder);

            }
            addtion_view.addView(root_view);
        }
//		Log.e("xxxxxx", addtion_view.getChildCount()+"");
        View line = new View(getContext());
        line.setBackgroundColor(MainApplication.getInstance().getResources().getColor(R.color.gray));
        LayoutParams lp =
                new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(MainApplication.getInstance(), 1));
        lp.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 44);
        addtion_view.addView(line, lp);
    }

    /**
     * 设置加料视图的数量和价格
     *
     * @param parentView 整个加料视图
     * @param position
     */
    private void setExtraCount(View parentView, int position, int count) {
        if (mListener != null && mListener.onAddtionClicked(mList.get(position), BigDecimal.valueOf(count))) {
            BigDecimal newQty = BigDecimal.valueOf(count);
            if (count == 0) {
                hidenCount(position);
                parentView.setSelected(false);
            } else {
                findTextView(position).setText(count + "");
                ((TextView) (rootViews.get(position)).getChildAt(1)).setText("+"
                        + ShopInfoCfg.formatCurrencySymbol(mList.get(position).getPrice().multiply(newQty)));
            }
            mList.get(position).setQty(newQty);
        }
    }

    private TextView findTextView(int postion) {
        LinearLayout llLayout = (LinearLayout) (rootViews.get(postion)).getChildAt(2);

        return (TextView) llLayout.getChildAt(1);
    }

    private void hidenCount(int postion) {
        rootViews.get(postion).setBackgroundResource(R.drawable.dish_type_item_bg);
        LinearLayout llLayout = (LinearLayout) (rootViews.get(postion)).getChildAt(2);
        llLayout.setVisibility(View.GONE);
    }

    class ViewHolder {
        TextView text_name;

        TextView text_price;

        LinearLayout ll;

        ImageView minus, plus;

        TextView count;
    }

    public interface OnAddtionCLicked {
        boolean onAddtionClicked(ExtraInfo extraInfo, BigDecimal qty);
    }

    public static class ExtraInfo {
        public final OrderExtra orderExtra;
        private BigDecimal qty;

        public ExtraInfo(OrderExtra orderExtra) {
            this.orderExtra = orderExtra;
            if (orderExtra != null && orderExtra.getSingleQty() != null) {
                qty = orderExtra.getSingleQty();
            } else {
                qty = BigDecimal.ZERO;
            }
        }

        public String getName() {
            return orderExtra.getSkuName();
        }

        public BigDecimal getQty() {
            return qty;
        }

        public void setQty(BigDecimal qty) {
            this.qty = qty;
        }

        public BigDecimal getPrice() {
            return orderExtra.getDishShop().getMarketPrice();
        }

        public BigDecimal getAmount() {
            return qty.multiply(getPrice());
        }
    }

    private static List<ExtraInfo> toExtraInfos(List<OrderExtra> list) {
        ArrayList<ExtraInfo> resultList = new ArrayList<ExtraInfo>();
        for (OrderExtra orderExtra : list) {
            resultList.add(new ExtraInfo(orderExtra));
        }
        return resultList;
    }
}
