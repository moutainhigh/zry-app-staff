package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishListenerImp;
import com.zhongmei.bty.snack.orderdish.adapter.TasteAdapter;
import com.zhongmei.bty.snack.orderdish.data.Position;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.util.MathDecimal;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@EViewGroup(R.layout.experience_layout)
public class TasteView extends LinearLayout implements View.OnClickListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

    private static final String TAG = TasteView.class.getSimpleName();

    @ViewById(R.id.experience_layout)
    LinearLayout experience_layout;

    @ViewById(R.id.exlv_taste)
    ExpandableListView mExlv_taste;

    @ViewById(R.id.btn_ok)
    Button btnOk;

    private TasteAdapter mTasteAdapter;

    private ArrayList<Position> mSelectedViews;

    private ArrayList<PropertyGroupVo<DishPropertyVo>> mPropertyGroupVos;

    private OrderDishListenerImp mListener;

    private boolean mIsUnFold = true;
    private boolean isBatchMode = false;

    public OrderDishListenerImp getListener() {
        return mListener;
    }

    public void setListener(OrderDishListenerImp mListener) {
        this.mListener = mListener;
    }

    private final static int LINE_SIZE = 1;

    public TasteView(Context context) {
        super(context);
    }

    public TasteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isUnFold() {
        return mIsUnFold;
    }

    public void setUnFold(boolean unFold) {
        mIsUnFold = unFold;
    }

    public TasteView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TasteView(Context context, boolean isBatchMode) {
        super(context);
        this.isBatchMode = isBatchMode;
    }

    @AfterViews
    public void initView() {
        setOrientation(VERTICAL);
        mExlv_taste.setGroupIndicator(null);
        mExlv_taste.setOnChildClickListener(this);

        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        if (isBatchMode) {
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(clickListener);
        } else {
            btnOk.setVisibility(View.GONE);
        }
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ok:
                    if (Utils.isEmpty(mSelectedViews)) {
                        ToastUtil.showShortToast(R.string.please_select_property);
                        return;
                    }
                    List<OrderProperty> orderProperties = new ArrayList<>();
                    for (Position position : mSelectedViews) {
                        OrderProperty orderProperty = getOrderProperty(position);
                        orderProperties.add(orderProperty);
                    }

                    if (mListener != null) {
                        mListener.onBatchAddProperty(orderProperties);
                                                clearSelected();
                    }
                    break;
                default:
                    Log.d(TAG, "No item clicked");
                    break;
            }
        }
    };

    public void setList(ArrayList<PropertyGroupVo<DishPropertyVo>> tasteGroupVo) {
        this.mPropertyGroupVos = tasteGroupVo;

        mSelectedViews = new ArrayList<Position>();
        if (mPropertyGroupVos != null && mPropertyGroupVos.size() > 0) {
            inflateRecipe();
        }

    }

    private void inflateRecipe() {
        mTasteAdapter = new TasteAdapter(getContext(), mPropertyGroupVos);
        mExlv_taste.setAdapter(mTasteAdapter);


        boolean isFold = SpHelper.getDefault().getBoolean(SpHelper.DINNER_FOLD_PRACTICE, false);
        if (mPropertyGroupVos != null && mPropertyGroupVos.size() > 0) {
            for (int typeIndex = 0; typeIndex < mPropertyGroupVos.size(); typeIndex++) {
                PropertyGroupVo<DishPropertyVo> propertyGroupVo = mPropertyGroupVos.get(typeIndex);

                if (mIsUnFold || !isFold) {
                    mExlv_taste.expandGroup(typeIndex);                }

                if (propertyGroupVo != null && propertyGroupVo.getPropertyList().size() > 0) {
                    List<DishPropertyVo> dishPropertyVos = propertyGroupVo.getPropertyList();
                    int rowCount = dishPropertyVos.size();
                    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                        DishPropertyVo dishPropertyVo = dishPropertyVos.get(rowIndex);
                        OrderProperty property =
                                new OrderProperty(propertyGroupVo.getPropertyType(), dishPropertyVo.getProperty());
                        if (dishPropertyVo.isSelected()) {
                            mListener.addExperience(property, false);
                            mSelectedViews.add(new Position(typeIndex, rowIndex));
                        }
                    }
                }
            }
        }
    }


    private void setNameAndPrice(View rootView, int typeIndex, int propertyIndex,
                                 PropertyGroupVo<DishPropertyVo> propertyGroupVo) {

        TextView tvName = (TextView) rootView.findViewById(R.id.text_name);
        TextView tvPrice = (TextView) rootView.findViewById(R.id.text_price);
        tvName.setText(propertyGroupVo.getProperty(propertyIndex).getName());
        tvPrice.setText(formatPrice(MathDecimal.trimZero(propertyGroupVo.getProperty(propertyIndex).getReprice())));
        View parentView = (View) (tvName.getParent());
        parentView.setTag(new Position(typeIndex, propertyIndex));
        parentView.setOnClickListener(this);
        parentView.setBackgroundResource(R.drawable.ic_dish_property_item_bg);
        if (propertyGroupVo.getPropertyList().get(propertyIndex).isSelected()) {
            OrderProperty property =
                    new OrderProperty(propertyGroupVo.getPropertyType(), propertyGroupVo.getProperty(propertyIndex));
            mListener.addExperience(property, false);
            parentView.setSelected(true);
        } else {
            parentView.setSelected(false);
        }
    }

    public static String formatPrice(BigDecimal value) {
        try {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return ShopInfoCfg.getInstance().getCurrencySymbol() + value;
            } else {
                return "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.negate(value);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return value + "";
        }
    }


    private void addPropertyTypeTitle(PropertyGroupVo<DishPropertyVo> dishProperties) {
        if (dishProperties != null && dishProperties.getPropertyList().size() > 0) {
            TextView tv = new TextView(getContext());
            tv.setText(dishProperties.getPropertyType().getName());
            tv.setTextSize(16);
            tv.setTextColor(getContext().getResources().getColor(R.color.dish_property_type_color));
            LayoutParams params = new LayoutParams(-2, -2);
            params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
            params.leftMargin = DensityUtil.dip2px(MainApplication.getInstance(), 26);
            params.rightMargin = DensityUtil.dip2px(MainApplication.getInstance(), 26);
            experience_layout.addView(tv, params);
        }
    }

    @Override
    public void onClick(View v) {
    }


    private OrderProperty getOrderProperty(Position pos) {
        if (pos.getFirst() >= 0 && pos.getFirst() < mPropertyGroupVos.size()) {
            PropertyGroupVo<DishPropertyVo> dishPropertyGroupVo = mPropertyGroupVos.get(pos.getFirst());
            List<DishPropertyVo> propertyVoList = dishPropertyGroupVo.getPropertyList();
            if (Utils.isNotEmpty(propertyVoList) && pos.getSecond() >= 0 && pos.getSecond() < propertyVoList.size()) {
                DishProperty property = dishPropertyGroupVo.getProperty(pos.getSecond());
                return new OrderProperty(dishPropertyGroupVo.getPropertyType(), property);
            }
        }

        return null;
    }


    private void setPropertySelectable(Position pos, boolean select) {
        if (pos.getFirst() >= 0 && pos.getFirst() < mPropertyGroupVos.size()) {
            PropertyGroupVo<DishPropertyVo> dishPropertyGroupVo = mPropertyGroupVos.get(pos.getFirst());
            DishPropertyVo dishPropertyVo = dishPropertyGroupVo.getPropertyList().get(pos.getSecond());
            DishProperty vo = dishPropertyGroupVo.getProperty(pos.getSecond());
            if (select) {
                mSelectedViews.add(pos);
                dishPropertyVo.setSelected(select);
                mListener.addExperience(new OrderProperty(dishPropertyGroupVo.getPropertyType(), vo), true);
            } else {
                mSelectedViews.remove(pos);
                dishPropertyVo.setSelected(select);
                mListener.deleteExperience(new OrderProperty(dishPropertyGroupVo.getPropertyType(), vo));
            }
            mTasteAdapter.notifyDataSetChanged();
        }
    }


    private void clearSelected() {
        if (Utils.isNotEmpty(mPropertyGroupVos)) {
            for (PropertyGroupVo<DishPropertyVo> propertyGroupVo : mPropertyGroupVos) {
                if (Utils.isNotEmpty(propertyGroupVo.getPropertyList())) {
                    for (DishPropertyVo propertyVo : propertyGroupVo.getPropertyList()) {
                        propertyVo.setSelected(false);
                    }
                }
            }
        }

        if (mSelectedViews != null) {
            mSelectedViews.clear();
        }

        mTasteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupIndex, int childIndex, long l) {
        Position vPos = new Position(groupIndex, childIndex);
        for (int i = 0; i < mSelectedViews.size(); i++) {
            Position position = mSelectedViews.get(i);
                        if (position.getFirst() == vPos.getFirst()) {
                if (position.getSecond() == vPos.getSecond()) {
                    setPropertySelectable(position, false);
                } else {
                    setPropertySelectable(vPos, true);
                    setPropertySelectable(position, false);
                }

                return true;
            }
        }

        setPropertySelectable(vPos, true);
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupIndex, long l) {
        return false;
    }
}
