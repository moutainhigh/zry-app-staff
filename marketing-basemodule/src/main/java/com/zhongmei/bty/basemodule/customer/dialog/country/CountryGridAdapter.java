package com.zhongmei.bty.basemodule.customer.dialog.country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.util.ErpConstants;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

public class CountryGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private static final String Tag = "CountryGridAdapter";

    private List<CountryGridItem> list;

    private LayoutInflater mInflater;

    OnItemSelectedListener mListener;

    private Context mContext;

    public CountryGridAdapter(Context context, List<CountryGridItem> list,
                              OnItemSelectedListener listener) {
        this.list = list;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.customer_country_user_grid_item, parent, false);
            convertView.setTag(mViewHolder);
            mViewHolder.tvName = (TextView) convertView.findViewById(R.id.user_name);
            mViewHolder.imgAnchor = (ImageView) convertView.findViewById(R.id.user_item_anchor);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final CountryGridItem item = list.get(position);
        String countryName;
        if (!ErpConstants.isChinese()) {
            countryName = item.countryEn;
        } else {
            countryName = item.countryZh;
        }

        mViewHolder.tvName.setText(countryName);
        if (item.isSelected) {
            mViewHolder.tvName.setBackgroundResource(R.drawable.customer_country_item_pressed);
            mViewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            mViewHolder.imgAnchor.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.tvName.setBackgroundResource(R.drawable.customer_country_item_normal);
            mViewHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.orderdish_text_black));
            mViewHolder.imgAnchor.setVisibility(View.INVISIBLE);
        }
        mViewHolder.tvName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    item.isSelected = true;
                    if (ErpConstants.isChinese()) {
                        mListener.onSelected(item.id, item.countryZh, item.erpCurrency);//modify v8.3
                    } else {
                        mListener.onSelected(item.id, item.countryEn, item.erpCurrency);//modify v8.3
                    }
                }
            }
        });
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;
        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.customer_country_grid_header, parent, false);
            mHeaderHolder.mTextView = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }
        String letters = list.get(position).sortLetters;
        mHeaderHolder.mTextView.setText(letters);
        return convertView;
    }

    public static class ViewHolder {
        public TextView tvName;
        ImageView imgAnchor;
    }

    public static class HeaderViewHolder {
        public TextView mTextView;
    }

    /**
     * 根据gridView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).sortLetters.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public long getHeaderId(int position) {
        // TODO Auto-generated method stub
        return list.get(position).section;
    }


    public interface OnItemSelectedListener {
        /**
         * 选择事件
         *
         * @param countryId   国籍id
         * @param name        国籍名
         * @param erpCurrency 国籍对象
         */
        void onSelected(Long countryId, String name, ErpCurrency erpCurrency);
    }

}
