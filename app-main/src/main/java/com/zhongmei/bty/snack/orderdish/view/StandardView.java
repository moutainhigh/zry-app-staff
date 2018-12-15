package com.zhongmei.bty.snack.orderdish.view;

import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.snack.orderdish.data.Position;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.cashier.orderdishmanager.DishPropertyManager;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;

/**
 * 规格
 */
public class StandardView extends LinearLayout implements OnClickListener {
    private static final String TAG = StandardView.class.getSimpleName();
    // 选择的规格
    private Set<DishProperty> mStandards;

    DishPropertyManager manager;

    private BaseShoppingCart mBaseShoppingCart;

    List<PropertyGroupVo<DishStandardVo>> mPropertyGroupVos;

    private final static int LINE_SIZE = 3;

    public StandardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public StandardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StandardView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
    }

    /**
     * @Title: setList
     * @Description: 设置规格
     * @Param @param propertyGroupVo
     * @Param @param manager 用于重新加载菜品
     * @Return void 返回类型
     */
    public void setList(List<PropertyGroupVo<DishStandardVo>> propertyGroupVos, DishPropertyManager manager,
                        BaseShoppingCart baseShoppingCart) {
        this.mPropertyGroupVos = propertyGroupVos;
        this.manager = manager;
        this.mBaseShoppingCart = baseShoppingCart;
        if (propertyGroupVos != null && propertyGroupVos.size() > 0) {
            inflateRecipe();
        }
    }

    /**
     * @Title: inflateRecipe
     * @Description: 动态加载view
     * @Return void 返回类型
     */
    private void inflateRecipe() {
        if (mStandards == null) {
            mStandards = new HashSet<DishProperty>();
        } else {
            mStandards.clear();
        }
        removeAllViews();
        if (mPropertyGroupVos != null && mPropertyGroupVos.size() > 0) {
            for (int typeIndex = 0; typeIndex < mPropertyGroupVos.size(); typeIndex++) {
                PropertyGroupVo<DishStandardVo> propertyGroupVo = mPropertyGroupVos.get(typeIndex);
                if (propertyGroupVo != null && propertyGroupVo.getPropertyList().size() > 0) {
                    addStandardTypeTitle(propertyGroupVo);

                    List<DishStandardVo> dishStandardVos = propertyGroupVo.getPropertyList();
                    int rowCount =
                            dishStandardVos.size() % LINE_SIZE == 0 ? dishStandardVos.size() / 3
                                    : dishStandardVos.size() / 3 + 1;
                    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                        View rootView =
                                LayoutInflater.from(getContext()).inflate(R.layout.cashier_order_dish_dlg_receipe_holer,
                                        null);

                        for (int lineIndex = 0; lineIndex < LINE_SIZE; lineIndex++) {
                            int propertyIndex = rowIndex * LINE_SIZE + lineIndex;
                            if (propertyIndex < dishStandardVos.size()) {
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

    /**
     * 设置规格的名称
     *
     * @param rootView
     * @param typeIndex       类别的索引
     * @param propertyIndex   属性在其类别下的索引
     * @param propertyGroupVo
     */
    private void setNameAndPrice(View rootView, int typeIndex, int propertyIndex,
                                 PropertyGroupVo<DishStandardVo> propertyGroupVo) {

        TextView tvName = (TextView) rootView.findViewById(R.id.text_name_0);
        switch (propertyIndex % LINE_SIZE) {
            case 0:
                tvName = (TextView) rootView.findViewById(R.id.text_name_0);
                break;
            case 1:
                tvName = (TextView) rootView.findViewById(R.id.text_name_1);
                break;
            case 2:
                tvName = (TextView) rootView.findViewById(R.id.text_name_2);
                break;
            default:
                Log.d(TAG, "No item clicked");
                break;
        }

        tvName.setText(propertyGroupVo.getProperty(propertyIndex).getName());
        View parentView = (View) (tvName.getParent());
        parentView.setTag(new Position(typeIndex, propertyIndex));
        if (manager.isSetmeal()) {
            parentView.setClickable(false);
        } else {
            parentView.setOnClickListener(this);
        }
        DishStandardVo standardVo = propertyGroupVo.getPropertyList().get(propertyIndex);
        //设置背景
        if (standardVo.getClearStatus() == ClearStatus.CLEAR) {
            parentView.setBackgroundResource(R.drawable.dish_standard_clear_bg);
            if (mBaseShoppingCart.getIsSalesReturn()) {
                //设置是否选中
                if (propertyGroupVo.getPropertyList().get(propertyIndex).isSelected()) {
                    parentView.setSelected(true);
                    mStandards.add(propertyGroupVo.getProperty(propertyIndex));
                } else {
                    parentView.setSelected(false);
                }
                //设置点击事件
                switch (standardVo.getState()) {
                    case DishStandardVo.ENABLE:
                        parentView.setActivated(false);
                        tvName.setEnabled(true);
                        break;
                    case DishStandardVo.DISABLE:
                        parentView.setActivated(true);
                        tvName.setEnabled(false);
                        break;
                    default:
                        Log.d(TAG, "No item clicked");
                        break;
                }
            } else {
                parentView.setSelected(false);
                parentView.setActivated(true);
            }
        } else {
            parentView.setBackgroundResource(R.drawable.dish_type_item_bg);
            //设置是否选中
            if (propertyGroupVo.getPropertyList().get(propertyIndex).isSelected()) {
                parentView.setSelected(true);
                mStandards.add(propertyGroupVo.getProperty(propertyIndex));
            } else {
                parentView.setSelected(false);
            }
            //设置点击事件
            switch (standardVo.getState()) {
                case DishStandardVo.ENABLE:
                    parentView.setActivated(false);
                    tvName.setEnabled(true);
                    break;
                case DishStandardVo.DISABLE:
                    parentView.setActivated(true);
                    tvName.setEnabled(false);
                    break;
            }
        }
    }

    /**
     * 添加规格的类别标题
     *
     * @param propertyGroup
     */
    private void addStandardTypeTitle(PropertyGroupVo<DishStandardVo> propertyGroup) {
        if (propertyGroup != null && propertyGroup.getPropertyList().size() > 0) {
            TextView tv = new TextView(getContext());
            tv.setText(propertyGroup.getPropertyType().getName());
            tv.setTextSize(24);
            tv.setTextColor(MainApplication.getInstance().getResources().getColor(R.color.text_pay_method));
            LayoutParams params = new LayoutParams(-2, -2);
            params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 20);
            params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 10);
            addView(tv, params);
        }
    }

    /**
     * 添加类别间的灰色线
     */
    private void addLine() {
        View line = new View(getContext());
        line.setBackgroundColor(MainApplication.getInstance().getResources().getColor(R.color.gray));
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(MainApplication.getInstance(), 1));
        lp.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 44);
        addView(line, lp);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        Position position = (Position) v.getTag();
        PropertyGroupVo<DishStandardVo> dishStandards = mPropertyGroupVos.get(position.getFirst());

        DishProperty vo = dishStandards.getProperty(position.getSecond());
        DishStandardVo dishStandardVo = dishStandards.getPropertyList().get(position.getSecond());
        if (dishStandardVo.getClearStatus() == ClearStatus.SALE || mBaseShoppingCart.getIsSalesReturn()) {
            switch (dishStandardVo.getState()) {
                case DishStandardVo.ENABLE:
                    if (mStandards.contains(vo)) {
                        mStandards.remove(vo);
                    } else {
                        mStandards.add(vo);
                    }
                    break;
                case DishStandardVo.DISABLE:
                    mStandards.clear();
                    mStandards.add(vo);
                    break;
                default:
                    Log.d(TAG, "No item clicked");
                    break;
            }

            manager.selectStandard(mStandards, mBaseShoppingCart.getIsSalesReturn());
        }
    }

}
