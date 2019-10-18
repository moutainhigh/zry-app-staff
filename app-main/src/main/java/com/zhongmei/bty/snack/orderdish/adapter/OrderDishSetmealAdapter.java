package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.basemodule.orderdish.bean.DishSetmealVo;
import com.zhongmei.yunfu.util.MathDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class OrderDishSetmealAdapter extends BaseAdapter {
    protected Context mContext;

    private LayoutInflater mInflater;

    private List<DishSetmealVo> mDishSetmealList;

    private int mNumColumns = 4;

    public OrderDishSetmealAdapter(Context context, int numColumns) {
        this.mContext = context;
        this.mNumColumns = numColumns;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDishSetmealList = new ArrayList<DishSetmealVo>();
    }

    @Override
    public int getCount() {
        return mDishSetmealList != null ? mDishSetmealList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDishSetmealList != null ? mDishSetmealList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(getItemLayoutResId(), parent, false);
            inflateViewHolder(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindView(viewHolder, position);

        return convertView;
    }

    public void setDishList(List<DishSetmealVo> list) {
        mDishSetmealList.clear();
        if (list != null && !list.isEmpty()) {
            mDishSetmealList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<DishSetmealVo> getDataList() {
        return mDishSetmealList;
    }

    public void setDish(DishSetmealVo dishSetmealVo) {
        if (dishSetmealVo != null) {
            for (int i = 0; i < mDishSetmealList.size(); i++) {
                DishSetmealVo setmealVo = mDishSetmealList.get(i);
                if (dishSetmealVo.getBrandDishId().equals(setmealVo.getBrandDishId())) {
                    mDishSetmealList.set(i, dishSetmealVo);
                    break;
                }
            }

            notifyDataSetChanged();
        }
    }

    protected int getBackgroundResource(int position) {
        int resId;

        DishSetmealVo dishSetmealVo = (DishSetmealVo) getItem(position);
        int index = position % mNumColumns;
        if (dishSetmealVo.isClear()) {
            resId = R.drawable.selector_dish_single_clear;
        } else {            if (index == 0) {                resId = R.drawable.selector_dish_single_green;
            } else if (index == 1) {                resId = R.drawable.selector_dish_single_blue;
            } else if (index == 2) {                resId = R.drawable.selector_dish_single_blue;
            } else {
                resId = R.drawable.selector_dish_single_slateblue;
            }
        }

        return resId;
    }

    protected void bindView(ViewHolder holder, int position) {
        DishSetmealVo dishSetmealVo = (DishSetmealVo) getItem(position);

        if (dishSetmealVo.isClear()) {
            holder.tvShortName.setTextColor(Color.parseColor("#ebeff2"));
            holder.tvShortName.setText(R.string.hadClear);
        } else {
            holder.tvShortName.setTextColor(Color.WHITE);
            holder.tvShortName.setText(dishSetmealVo.getShortName());
        }

        holder.tvName.setText(genName(dishSetmealVo));

                BigDecimal price = dishSetmealVo.getPrice();
        if (holder.tvPrice != null) {
            if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
                holder.tvPrice.setText("+" + ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.trimZero(price));
                holder.tvPrice.setVisibility(View.VISIBLE);
            } else {
                holder.tvPrice.setVisibility(View.GONE);
            }
        }

        if (dishSetmealVo.getDishShop() != null && dishSetmealVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
            holder.tvResidue.setVisibility(View.VISIBLE);
            holder.tvResidue.setText(R.string.order_weighing_item);
        } else {
            holder.tvResidue.setVisibility(View.GONE);
        }
                BigDecimal selectedQTY = dishSetmealVo.getSelectedQty();
        if (selectedQTY != null && selectedQTY.compareTo(BigDecimal.ZERO) > 0) {
            if (selectedQTY.compareTo(new BigDecimal("100")) >= 0) {
                holder.tvNumber.setText("99+");
            } else {
                holder.tvNumber.setText(MathDecimal.toTrimZeroString(selectedQTY));
            }

                        int length = holder.tvNumber.getText().toString().length();
            if (length <= 2) {
                holder.tvNumber.setTextSize(18);
            } else if (length == 3) {
                holder.tvNumber.setTextSize(14);
            } else {
                holder.tvNumber.setTextSize(10);
            }

            holder.tvNumber.setVisibility(View.VISIBLE);
        } else {
            holder.tvNumber.setVisibility(View.INVISIBLE);
        }

                holder.vMainContent.setBackgroundResource(getBackgroundResource(position));
        holder.tvMarketPrice.setVisibility(View.INVISIBLE);

        holder.ivProperty.setVisibility(dishSetmealVo.isContainProperties() ? View.VISIBLE : View.GONE);
    }


    private String genName(DishSetmealVo dishSetmealVo) {
        StringBuilder sb = new StringBuilder(dishSetmealVo.getName());
        Set<DishProperty> standards = dishSetmealVo.getStandards();
        if (standards != null && standards.size() > 0) {
            sb.append("(");
            for (DishProperty dishProperty : standards) {
                sb.append(dishProperty.getName()).append(",");
            }
            sb.replace(sb.length() - 1, sb.length(), ")");
        }

        return sb.toString();
    }

    protected int getItemLayoutResId() {
        return R.layout.order_dish_grid_item;
    }

    protected void inflateViewHolder(View convertView, ViewHolder viewHolder) {
        viewHolder.vMainContent = convertView.findViewById(R.id.v_main_content);
        viewHolder.tvShortName = (TextView) convertView.findViewById(R.id.tv_short_name);
        viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
        viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
        viewHolder.tvMarketPrice = (TextView) convertView.findViewById(R.id.tv_market_price);
        viewHolder.ivProperty = (ImageView) convertView.findViewById(R.id.iv_property);
        viewHolder.tvResidue = (TextView) convertView.findViewById(R.id.tv_residue);
    }

    public static class ViewHolder {
        public TextView tvNumber;

        public TextView tvShortName;

        public TextView tvName;

        public TextView tvPrice;

        public View vMainContent;

        public TextView tvMarketPrice;

        public View vBottom;

        public ImageView ivProperty;

        public TextView tvResidue;
    }

}
