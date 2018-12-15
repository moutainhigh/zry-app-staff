package com.zhongmei.bty.cashier.inventory.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.SaleType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class InventoryDishItemAdapter extends BaseAdapter {

    private List<InventoryItem> inventoryItemList;

    private Context context;

    private LayoutInflater inflater;

    public InventoryDishItemAdapter(Context context, List<InventoryItem> inventoryItemList) {
        this.inventoryItemList = inventoryItemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return inventoryItemList == null ? 0 : inventoryItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return inventoryItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.return_inventory_list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.dishName = (TextView) view.findViewById(R.id.dish_name);
            holder.dishType = (TextView) view.findViewById(R.id.dish_type);
            holder.add = (ImageButton) view.findViewById(R.id.ib_increase);
            holder.subtract = (ImageButton) view.findViewById(R.id.ib_decrease);
            holder.dishNum = (TextView) view.findViewById(R.id.tv_dishQuantity);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        initData(holder, inventoryItemList.get(i));
        return view;
    }

    void initData(final ViewHolder holder, final InventoryItem inventoryItem) {
        final TradeItem tradeItem = inventoryItem.getTradeItem();
        holder.dishName.setText(tradeItem.getDishName());
        if (tradeItem.getSaleType() == SaleType.WEIGHING) {
            holder.dishType.setVisibility(View.VISIBLE);
            holder.dishType.setText(context.getString(R.string.weighin_flag_text));
        } else if (tradeItem.getType() == DishType.COMBO) {
            holder.dishType.setVisibility(View.VISIBLE);
            holder.dishType.setText(context.getString(R.string.print_combo));
        } else {
            holder.dishType.setVisibility(View.INVISIBLE);
        }
        if (tradeItem.getSaleType() == SaleType.UNWEIGHING) {
            holder.dishNum.setText(inventoryItem.getReturnInventoryNum() == null ? bigDecimalToString(tradeItem.getQuantity()) : bigDecimalToString(inventoryItem.getReturnInventoryNum()));
        } else {
            holder.dishNum.setText(inventoryItem.getReturnInventoryNum() == null ? bigDecimalToString(tradeItem.getQuantity()) : bigDecimalToString(inventoryItem.getReturnInventoryNum()));
        }
        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //减
                if (tradeItem.getSaleType() == SaleType.WEIGHING || inventoryItem.isGroupDish()) {
                    holder.dishNum.setText(0 + "");
                } else {
                    BigDecimal qty = new BigDecimal(holder.dishNum.getText().toString().trim()).subtract(inventoryItem.getStepNum());
                    //不能小于0
                    if (qty.compareTo(BigDecimal.ZERO) < 0) {
                        qty = BigDecimal.ZERO;
                    }
                    holder.dishNum.setText(bigDecimalToString(qty) + "");
                }
                judgeButton(holder, inventoryItem);
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //加
                if (tradeItem.getSaleType() == SaleType.WEIGHING || inventoryItem.isGroupDish()) {
                    holder.dishNum.setText(bigDecimalToString(tradeItem.getQuantity()) + "");
                } else {
                    BigDecimal qty = new BigDecimal(holder.dishNum.getText().toString().trim()).add(inventoryItem.getStepNum());
                    //不能大于最大值
                    if (qty.compareTo(inventoryItem.getMaxInventoryNum()) > 0) {
                        qty = inventoryItem.getMaxInventoryNum();
                    }
                    holder.dishNum.setText(bigDecimalToString(qty) + "");
                }
                judgeButton(holder, inventoryItem);
            }
        });
        judgeButton(holder, inventoryItem);

    }

    void judgeButton(ViewHolder holder, InventoryItem inventoryItem) {
        if (TextUtils.isEmpty(holder.dishNum.getText().toString().trim())) {
            return;
        }
        BigDecimal all = inventoryItem.getMaxInventoryNum();
        BigDecimal newNum = new BigDecimal(holder.dishNum.getText().toString().trim());
        inventoryItem.setTempReturnInventoryNum(newNum);
        if (all.compareTo(newNum) <= 0) {
            holder.add.setEnabled(false);
        } else {
            holder.add.setEnabled(true);
        }
        if (newNum.doubleValue() <= 0.00) {
            holder.subtract.setEnabled(false);
        } else {
            holder.subtract.setEnabled(true);
        }
    }

    class ViewHolder {
        TextView dishName;
        TextView dishType;
        ImageButton subtract;
        ImageButton add;
        TextView dishNum;
    }

    private String bigDecimalToString(BigDecimal bigDecimal) {
        String value = "";
        value = bigDecimal.doubleValue() + "";
        if (value.endsWith(".0") || value.endsWith(".00") || value.endsWith(".000")) {
            value = value.substring(0, value.indexOf("."));
        }
        return value;
    }
}
