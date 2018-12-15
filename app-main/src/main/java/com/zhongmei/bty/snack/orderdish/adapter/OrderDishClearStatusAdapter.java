package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.enums.ClearStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderDishClearStatusAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<DishAndStandards> mSelectedProperty;// 已选中的谷歌

    private List<DishAndStandards> mDataSet;

    private Resources mResources;

    public OrderDishClearStatusAdapter(Context context) {
        this.mContext = context;
        this.mResources = mContext.getResources();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mSelectedProperty = new ArrayList<DishAndStandards>();
        this.mDataSet = new ArrayList<DishAndStandards>();
    }

    public void setDataSet(List<DishAndStandards> dataSet) {
        mDataSet.clear();
        if (dataSet != null) {
            mDataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    public List<DishAndStandards> getDataSet() {
        return mDataSet;
    }

    public void setSelectedProperty(List<DishAndStandards> dataSet) {
        mSelectedProperty.clear();
        if (dataSet != null) {
            mSelectedProperty.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    public List<DishAndStandards> getSelectedProperty() {
        return mSelectedProperty;
    }

    public boolean isSelected(DishAndStandards dishAndStandards) {
        if (dishAndStandards != null) {
            return mSelectedProperty.contains(dishAndStandards);
        }

        return false;
    }

    public boolean isAllSelected(List<DishAndStandards> dishAndStandardsList) {
        if (Utils.isNotEmpty(dishAndStandardsList)) {
            for (DishAndStandards dishAndStandards : dishAndStandardsList) {
                if (!isSelected(dishAndStandards)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.orderdish_clear_status_item, parent, false);
            holder.tvPropertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        bindView(holder, position);

        return convertView;
    }

    private void bindView(ViewHolder holder, int position) {
        DishAndStandards dishAndStandards = (DishAndStandards) getItem(position);
        ClearStatus clearStatus = dishAndStandards.getDishShop().getClearStatus();
        if (clearStatus == ClearStatus.SALE) {// 在售
            /*holder.tvPropertyName.setBackgroundDrawable(mResources.getDrawable(R.drawable.orderdish_clear_status_item_bg_sale_selector));
			holder.tvPropertyName.setTextColor(mResources.getColorStateList(R.color.orderdish_clear_status_item_text_sale_selector));
			*/
            if (mSelectedProperty.contains(dishAndStandards)) {
                holder.tvPropertyName.setSelected(true);
                holder.tvPropertyName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
                holder.tvPropertyName.setBackgroundResource(R.drawable.bg_dinner_discount_pressed);
            } else {
                holder.tvPropertyName.setSelected(false);
                holder.tvPropertyName.setTextColor(mContext.getResources().getColor(R.color.orderdish_text_black));
                holder.tvPropertyName.setBackgroundResource(R.drawable.bg_dinner_discount_normal);
            }
        } else {// 已估清
            if (mSelectedProperty.contains(dishAndStandards)) {
                holder.tvPropertyName.setSelected(true);
                holder.tvPropertyName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
                holder.tvPropertyName.setBackgroundResource(R.drawable.bg_dinner_discount_pressed);
            } else {
                holder.tvPropertyName.setSelected(false);
                holder.tvPropertyName.setTextColor(mContext.getResources().getColor(R.color.orderdish_text_black));
                holder.tvPropertyName.setBackgroundResource(R.drawable.bg_dinner_discount_normal);
            }
			/*holder.tvPropertyName.setBackgroundDrawable(mResources.getDrawable(R.drawable.orderdish_clear_status_item_bg_clear_selector));
			holder.tvPropertyName.setTextColor(Color.WHITE);*/
        }
		
		/*if (mSelectedProperty.contains(dishAndStandards)) {
			holder.tvPropertyName.setSelected(true);
		} else {
			holder.tvPropertyName.setSelected(false);
		}*/

        holder.tvPropertyName.setText(genStandardString(dishAndStandards.getStandards()));
    }

    private String genStandardString(Set<DishProperty> standards) {
        StringBuilder sb = new StringBuilder();
        if (standards != null) {
            for (DishProperty dishProperty : standards) {
                sb.append(dishProperty.getName());
            }
        }

        return sb.toString();
    }

    static class ViewHolder {
        TextView tvPropertyName;
    }
}
