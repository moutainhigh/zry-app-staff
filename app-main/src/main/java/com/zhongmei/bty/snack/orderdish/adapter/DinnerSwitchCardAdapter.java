package com.zhongmei.bty.snack.orderdish.adapter;

import java.util.List;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp.Card;
import com.zhongmei.yunfu.context.util.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DinnerSwitchCardAdapter extends BaseAdapter {

    private Context context;

    private List<Card> cards;

    public DinnerSwitchCardAdapter(Context context) {
        this.context = context;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public int getCount() {
        return Utils.isEmpty(cards) ? 0 : cards.size();
    }

    @Override
    public Card getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_switch_card, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvCardNum = (TextView) convertView.findViewById(R.id.tv_card_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Card card = cards.get(position);
        holder.tvName.setText(card.getCardKindName());
        if (TextUtils.isEmpty(card.getCardNum())) {
            holder.tvCardNum.setText("");
            holder.tvCardNum.setVisibility(View.GONE);
        } else {
            holder.tvCardNum.setText(card.getCardNum());
            holder.tvCardNum.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private final static class ViewHolder {
        TextView tvName;

        TextView tvCardNum;
    }

}
