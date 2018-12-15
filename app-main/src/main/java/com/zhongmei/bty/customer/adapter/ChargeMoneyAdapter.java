package com.zhongmei.bty.customer.adapter;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.customer.vo.ChargeMoneyVo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 充值金额面板
 */
public class ChargeMoneyAdapter extends BaseAdapter {

    private List<ChargeMoneyVo> chargeMoneyList;

    private LayoutInflater mLayoutInflater;

    public ChargeMoneyAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.chargeMoneyList != null ? this.chargeMoneyList.size() : 0;
    }

    public Object getItem(int index) {
        return this.chargeMoneyList.get(index);
    }

    public long getItemId(int index) {
        return index;
    }

    public void setDataSource(List<ChargeMoneyVo> chargeMoneyList) {
        this.chargeMoneyList = chargeMoneyList;
        notifyDataSetChanged();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.charge_money_item, null);
            holder.chargeMoney = (TextView) convertView.findViewById(R.id.charge_item_money);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (chargeMoneyList.get(position).isAuto()) {
            holder.chargeMoney.setText(R.string.user_defined);
        } else {
            final BigDecimal money = chargeMoneyList.get(position).getFullMoney();
            if (money != null) {
                holder.chargeMoney.setText(money.toPlainString());
            } else {
                holder.chargeMoney.setText("");
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView chargeMoney;// 队列名称

    }

}
