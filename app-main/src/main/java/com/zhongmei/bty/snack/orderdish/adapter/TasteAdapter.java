package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.snack.orderdish.data.Position;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by demo on 2018/12/15
 */

public class TasteAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<PropertyGroupVo<DishPropertyVo>> mTasteProperty;
    private LayoutInflater mInflater;

    public TasteAdapter(Context context, ArrayList<PropertyGroupVo<DishPropertyVo>> tasteProperty) {
        this.mContext = context;
        this.mTasteProperty = tasteProperty;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        if (Utils.isNotEmpty(mTasteProperty)) {
            return mTasteProperty.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupIndex) {
        PropertyGroupVo<DishPropertyVo> groupProperty = mTasteProperty.get(groupIndex);
        if (Utils.isNotEmpty(groupProperty.getPropertyList())) {
            return groupProperty.getPropertyList().size();
        }
        return 0;
    }

    @Override
    public PropertyGroupVo<DishPropertyVo> getGroup(int groupIndex) {
        if (mTasteProperty != null && groupIndex < mTasteProperty.size()) {
            return mTasteProperty.get(groupIndex);
        }
        return null;
    }

    @Override
    public DishPropertyVo getChild(int groupIndex, int childIndex) {
        PropertyGroupVo<DishPropertyVo> groupProperty = mTasteProperty.get(groupIndex);
        if (groupProperty.getPropertyList() != null && childIndex < groupProperty.getPropertyList().size()) {
            return groupProperty.getPropertyList().get(childIndex);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupIndex) {
        return groupIndex;
    }

    @Override
    public long getChildId(int groupIndex, int childIndex) {
        return childIndex;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupIndex, boolean isExpand, View groupView, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (groupView == null) {
            groupView = mInflater.inflate(R.layout.experience_item_view_title, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_groupName = (TextView) groupView.findViewById(R.id.cb_group_name);
            groupView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) groupView.getTag();
        }

        PropertyGroupVo<DishPropertyVo> groupProperty = getGroup(groupIndex);
        groupViewHolder.tv_groupName.setText(groupProperty.getPropertyType().getName());
        groupViewHolder.tv_groupName.setSelected(isExpand);

        return groupView;
    }

    @Override
    public View getChildView(int groupIndex, int childIndex, boolean b, View childView, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (childView == null) {
            childView = mInflater.inflate(R.layout.experience_item_view, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvPrice = (TextView) childView.findViewById(R.id.text_price);
            childViewHolder.tvName = (TextView) childView.findViewById(R.id.text_name);
            childViewHolder.parentView = childView.findViewById(R.id.ll_parent);

            childView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) childView.getTag();
        }

        DishPropertyVo childProperty = getChild(groupIndex, childIndex);

        childViewHolder.tvName.setText(childProperty.getProperty().getName());
        childViewHolder.tvPrice.setText(formatPrice(MathDecimal.trimZero(childProperty.getProperty().getReprice())));
        childViewHolder.parentView.setTag(new Position(groupIndex, childIndex));
        childViewHolder.parentView.setSelected(childProperty.isSelected());

        return childView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private TextView getGroupView(Context context) {
        CheckBox cb = new CheckBox(context);
        cb.setTextSize(16);
        cb.setTextColor(context.getResources().getColor(R.color.dish_property_type_color));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
        params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
        params.leftMargin = DensityUtil.dip2px(MainApplication.getInstance(), 26);
        params.rightMargin = DensityUtil.dip2px(MainApplication.getInstance(), 26);
        cb.setLayoutParams(params);
        return cb;
    }

    private String formatPrice(BigDecimal value) {
        try {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return ShopInfoCfg.getInstance().getCurrencySymbol() + value;
            } else {
                return "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.negate(value);
            }
        } catch (Exception e) {
            return value + "";
        }
    }

    class GroupViewHolder {
        public TextView tv_groupName;
    }

    class ChildViewHolder {
        public View parentView;
        public TextView tvName;
        public TextView tvPrice;
    }

}
