package com.zhongmei.bty.customer.adapter;

import java.math.BigDecimal;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.database.enums.SendType;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo;
import com.zhongmei.bty.basemodule.customer.bean.RechargeRuleVo.RechargeRuleDetailVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class RechargeRuleDialogFragmentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private Context mContext;

    private RechargeRuleVo ruleVo;

    public RechargeRuleDialogFragmentAdapter(Context _mContext, RechargeRuleVo ruleVo) {
        this.mContext = _mContext;
        this.ruleVo = ruleVo;
        mInflater = LayoutInflater.from(mContext);
    }

    class ViewHold {
        TextView rechargeMoneyTx;

        TextView givenMoneyTx;
    }

    @Override
    public int getCount() {
        if (ruleVo != null) {
            return ruleVo.getRuleDetailList().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (ruleVo.getRuleDetailList() != null) {
            return ruleVo.getRuleDetailList().get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold vh;
        if (convertView == null) {
            vh = new ViewHold();
            convertView = mInflater.inflate(R.layout.recharge_dialog_list_item, parent, false);
            vh.rechargeMoneyTx = (TextView) convertView.findViewById(R.id.recharge_dialog_listview_item_tx);
            vh.givenMoneyTx = (TextView) convertView.findViewById(R.id.recharge_dialog_listview_given_item_tx);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold) convertView.getTag();
        }

        if (getItem(position) != null) {
            RechargeRuleDetailVo vo = (RechargeRuleDetailVo) getItem(position);
            BigDecimal fullValue = vo.getFullValue();
            BigDecimal sendValue = vo.getSendValue();
            BigDecimal rate = vo.getSendRate();
            if (fullValue != null) {
                String tempFullValue =
                        String.format(mContext.getResources().getString(R.string.full_money), ShopInfoCfg.formatCurrencySymbol(fullValue.toString()));
                vh.rechargeMoneyTx.setText(tempFullValue);
            }
            vh.givenMoneyTx.setText("-");
            if (ruleVo != null) {
                SendType sendType = ruleVo.getSendType();
                if (sendType != null) {
                    switch (sendType) {
                        case FIXED:
                            String sendStr = mContext.getResources().getString(R.string.send_given_money);
                            if (sendValue != null) {
                                String tempFullValue = String.format("%s%s", sendStr, ShopInfoCfg.formatCurrencySymbol(sendValue.toString()));
                                vh.givenMoneyTx.setText(tempFullValue);
                            }
                            break;

                        case RATIO:
                            if (rate != null) {
                                String tempFullValue = String.format("%s%s", rate.toString(), "%");
                                vh.givenMoneyTx.setText(tempFullValue);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return convertView;
    }

}
