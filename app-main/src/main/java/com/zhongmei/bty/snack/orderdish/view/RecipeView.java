package com.zhongmei.bty.snack.orderdish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.snack.orderdish.PropertySelectListener;
import com.zhongmei.bty.snack.orderdish.data.Position;

import java.util.ArrayList;
import java.util.List;


public class RecipeView extends LinearLayout implements OnClickListener {
    private static final String TAG = RecipeView.class.getSimpleName();

    private ArrayList<View> mSelectedViews;

    private ArrayList<PropertyGroupVo<DishPropertyVo>> mPropertyGroupVos;

    private PropertySelectListener mListener;

    public PropertySelectListener getListener() {
        return mListener;
    }

    public void setListener(PropertySelectListener mListener) {
        this.mListener = mListener;
    }

    private final static int LINE_SIZE = 3;

    public RecipeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecipeView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
    }

    public void setList(ArrayList<PropertyGroupVo<DishPropertyVo>> tasteGroupVo) {
        this.mPropertyGroupVos = tasteGroupVo;
        mSelectedViews = new ArrayList<View>();
        if (mPropertyGroupVos != null && mPropertyGroupVos.size() > 0) {
            inflateRecipe();
        }

    }

    public void clearSelect() {
        if (mSelectedViews != null) {
            mSelectedViews.clear();
        }
    }

    private void inflateRecipe() {
        removeAllViews();
        if (mPropertyGroupVos != null && mPropertyGroupVos.size() > 0) {
            for (int typeIndex = 0; typeIndex < mPropertyGroupVos.size(); typeIndex++) {
                PropertyGroupVo<DishPropertyVo> propertyGroupVo = mPropertyGroupVos.get(typeIndex);
                if (propertyGroupVo != null && propertyGroupVo.getPropertyList().size() > 0) {
                    addPropertyTypeTitle(propertyGroupVo);

                    List<DishPropertyVo> dishPropertyVos = propertyGroupVo.getPropertyList();
                    int rowCount =
                            dishPropertyVos.size() % LINE_SIZE == 0 ? dishPropertyVos.size() / 3
                                    : dishPropertyVos.size() / 3 + 1;
                    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                        View rootView =
                                LayoutInflater.from(getContext()).inflate(R.layout.cashier_order_dish_dlg_receipe_holer,
                                        null);

                        for (int lineIndex = 0; lineIndex < LINE_SIZE; lineIndex++) {
                            int propertyIndex = rowIndex * LINE_SIZE + lineIndex;
                            if (propertyIndex < dishPropertyVos.size()) {
                                setNameAndPrice(rootView, typeIndex, propertyIndex, propertyGroupVo);
                            }
                        }
                        addView(rootView);
                    }

                    addLine();
                }
            }
        }
    }


    private void setNameAndPrice(View rootView, int typeIndex, int propertyIndex,
                                 PropertyGroupVo<DishPropertyVo> propertyGroupVo) {

        TextView tvName = (TextView) rootView.findViewById(R.id.text_name_0);
        TextView tvPrice = (TextView) rootView.findViewById(R.id.text_price_0);
        switch (propertyIndex % LINE_SIZE) {
            case 0:
                tvName = (TextView) rootView.findViewById(R.id.text_name_0);
                tvPrice = (TextView) rootView.findViewById(R.id.text_price_0);
                break;
            case 1:
                tvName = (TextView) rootView.findViewById(R.id.text_name_1);
                tvPrice = (TextView) rootView.findViewById(R.id.text_price_1);
                break;
            case 2:
                tvName = (TextView) rootView.findViewById(R.id.text_name_2);
                tvPrice = (TextView) rootView.findViewById(R.id.text_price_2);
                break;
            default:
                Log.d(TAG, "No item clicked");
                break;
        }

        tvName.setText(propertyGroupVo.getProperty(propertyIndex).getName());
        tvPrice.setText(ShopInfoCfg.formatCurrencySymbol(
                propertyGroupVo.getProperty(propertyIndex).getReprice()));
        View parentView = (View) (tvName.getParent());
        parentView.setTag(new Position(typeIndex, propertyIndex));
        parentView.setOnClickListener(this);
        parentView.setBackgroundResource(R.drawable.dish_type_item_bg);
        if (propertyGroupVo.getPropertyList().get(propertyIndex).isSelected()) {
            OrderProperty property =
                    new OrderProperty(propertyGroupVo.getPropertyType(), propertyGroupVo.getProperty(propertyIndex));
            mListener.addProperty(property, false);
            mSelectedViews.add(parentView);
            parentView.setSelected(true);
        } else {
            parentView.setSelected(false);
        }
    }


    private void addPropertyTypeTitle(PropertyGroupVo<DishPropertyVo> dishProperties) {
        if (dishProperties != null && dishProperties.getPropertyList().size() > 0) {
            TextView tv = new TextView(getContext());
            tv.setText(dishProperties.getPropertyType().getName());
            tv.setTextSize(24);
            tv.setTextColor(getContext().getResources().getColor(R.color.text_pay_method));
            LayoutParams params = new LayoutParams(-2, -2);
            params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 20);
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 10);
            addView(tv, params);
        }
    }


    private void addLine() {
        View line = new View(getContext());
        line.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(MainApplication.getInstance(), 1));
        lp.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 44);
        addView(line, lp);
    }

    @Override
    public void onClick(View v) {
        Position vPos = (Position) v.getTag();
        for (int i = 0; i < mSelectedViews.size(); i++) {
            Position position = (Position) mSelectedViews.get(i).getTag();
                        if (position.getFirst() == vPos.getFirst()) {
                if (position.getSecond() == vPos.getSecond()) {
                    setPropertySelectable(v, vPos, false);
                } else {
                    setPropertySelectable(v, vPos, true);
                    setPropertySelectable(mSelectedViews.get(i), position, false);
                }

                return;
            }
        }

        setPropertySelectable(v, vPos, true);
    }


    private void setPropertySelectable(View v, Position pos, boolean select) {
        PropertyGroupVo<DishPropertyVo> dishPropertyGroupVo = mPropertyGroupVos.get(pos.getFirst());
        DishProperty vo = dishPropertyGroupVo.getProperty(pos.getSecond());

        if (select) {
            if (mListener.addProperty(new OrderProperty(dishPropertyGroupVo.getPropertyType(), vo), true)) {
                mSelectedViews.add(v);
                v.setSelected(true);
            }
        } else {
            if (mListener.deleteProperty(new OrderProperty(dishPropertyGroupVo.getPropertyType(), vo))) {
                mSelectedViews.remove(v);
                v.setSelected(false);
            }
        }
    }

}
