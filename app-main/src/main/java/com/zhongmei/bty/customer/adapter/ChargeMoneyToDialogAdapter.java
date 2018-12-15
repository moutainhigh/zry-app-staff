package com.zhongmei.bty.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.customer.vo.ChargeMoneyVo;
import com.zhongmei.bty.basemodule.customer.enums.FullSend;
import com.zhongmei.bty.commonmodule.database.enums.SendType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值金额面板
 */
public class ChargeMoneyToDialogAdapter extends BaseAdapter {

    private List<ChargeMoneyVo> chargeMoneyList;

    private LayoutInflater mLayoutInflater;

    private Context mContext;

    private OnRuleClick mOnRuleClick;

    public interface OnRuleClick {
        void OnClickListener(ChargeMoneyVo vo);
    }

    public void setOnRuleClick(OnRuleClick click) {
        this.mOnRuleClick = click;
    }

    public ChargeMoneyToDialogAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.charge_money_item, null);
            holder.chargeMoney = (Button) convertView.findViewById(R.id.charge_item_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (chargeMoneyList.get(position).isAuto()) {
            holder.chargeMoney.setText(R.string.user_defined);
        } else {
            ChargeMoneyVo chargeMoney = chargeMoneyList.get(position);
            final BigDecimal fullMoney = chargeMoney.getFullMoney();
            final BigDecimal sendMoney = chargeMoney.getSendMoney();
            final BigDecimal sendRatio = chargeMoney.getSendRate();
            if (chargeMoney.getIsFullSend() == FullSend.YES) {
                if (chargeMoney.getSendType() == SendType.FIXED) {
                    // 固定值
                    if (fullMoney != null) {
                        if (sendMoney != null && sendMoney.compareTo(BigDecimal.ZERO) > 0) {
                            holder.chargeMoney.setText(mContext.getString(R.string.customer_charging_fullmoney_by_fixed, fullMoney.toPlainString(), sendMoney.toPlainString()));
                        } else {
                            holder.chargeMoney.setText(ShopInfoCfg.formatCurrencySymbol(fullMoney.toPlainString()));
                        }
                    } else {
                        holder.chargeMoney.setText("");
                    }
                } else {
                    // 百分比
                    if (fullMoney != null) {
                        if (sendRatio != null && sendRatio.compareTo(BigDecimal.ZERO) > 0) {
                            holder.chargeMoney.setText(mContext.getString(R.string.customer_charging_fullmoney_by_ratio, fullMoney.toPlainString(), sendRatio.toPlainString()));
                        } else {
                            holder.chargeMoney.setText(ShopInfoCfg.formatCurrencySymbol(fullMoney.toPlainString()));
                        }
                    } else {
                        holder.chargeMoney.setText("");
                    }
                }
            } else {
                if (fullMoney != null) {
                    holder.chargeMoney.setText(ShopInfoCfg.formatCurrencySymbol(fullMoney.toPlainString()));
                } else {
                    holder.chargeMoney.setText("");
                }
            }
            holder.chargeMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnRuleClick != null) {
                        mOnRuleClick.OnClickListener(chargeMoneyList.get(position));
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        Button chargeMoney;// 队列名称
    }

}
