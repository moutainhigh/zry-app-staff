package com.zhongmei.bty.cashier.inventory.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryItem;
import com.zhongmei.yunfu.db.enums.SaleType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class ReturnInventoryLayout extends LinearLayout {

    private Context context;

    private LinearLayout returnInventoryLayout;

    private TextView returnCountTv;

    public ReturnInventoryLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_center_operate_dialog_fragment_return_inventory, this, true);
        returnInventoryLayout = (LinearLayout) view.findViewById(R.id.return_inventory_item);
        returnCountTv = (TextView) view.findViewById(R.id.count_num_tv);
    }

    public void refreshView(List<InventoryItem> inventoryItemList) {
        BigDecimal allCount = BigDecimal.ZERO;
        for (InventoryItem item : inventoryItemList) {
            if (item.getTradeItem().getSaleType() == SaleType.WEIGHING) {
                if (item.getReturnInventoryNum().compareTo(BigDecimal.ZERO) == 0) {
                    allCount = allCount.add(BigDecimal.ZERO);
                } else {
                    allCount = allCount.add(BigDecimal.ONE);
                }
            } else {
                allCount = allCount.add(item.getReturnInventoryNum());
            }
        }
        returnCountTv.setText(bigDecimalToString(allCount));
    }

    public BigDecimal getBigDecimal() {
        return new BigDecimal(returnCountTv.getText().toString().trim());
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
