package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.bty.snack.orderdish.data.Position;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.cashier.orderdishmanager.DishPropertyManager;
import com.zhongmei.bty.basemodule.shoppingcart.BaseShoppingCart;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.context.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜品规格控件
 */
@EViewGroup(R.layout.layout_custom_standard)
public class CustomStandardView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = CustomStandardView.class.getSimpleName();

    @ViewById(R.id.v_content)
    protected LinearLayout vContent;

    // 选择的规格
    private Set<DishProperty> mStandards;

    private DishPropertyManager manager;

    private BaseShoppingCart mBaseShoppingCart;

    private List<PropertyGroupVo<DishStandardVo>> mPropertyGroupVos;

    private PropertListener mPropertListener;

    public CustomStandardView(Context context) {
        super(context);
    }

    public CustomStandardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomStandardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    protected void initViews() {
        setOrientation(VERTICAL);
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
    }

    public void setListener(PropertListener propertListener) {
        mPropertListener = propertListener;
    }

    public void setData(List<PropertyGroupVo<DishStandardVo>> propertyGroupVos, DishPropertyManager manager,
                        BaseShoppingCart baseShoppingCart) {
        this.mPropertyGroupVos = propertyGroupVos;
        this.manager = manager;
        this.mBaseShoppingCart = baseShoppingCart;
        if (Utils.isNotEmpty(propertyGroupVos)) {
            inflateRecipe();
        }
    }

    private void inflateRecipe() {
        if (mStandards == null) {
            mStandards = new HashSet<DishProperty>();
        } else {
            mStandards.clear();
        }
        vContent.removeAllViews();
        if (Utils.isNotEmpty(mPropertyGroupVos)) {
            for (int typeIndex = 0; typeIndex < mPropertyGroupVos.size(); typeIndex++) {
                PropertyGroupVo<DishStandardVo> propertyGroupVo = mPropertyGroupVos.get(typeIndex);
                if (propertyGroupVo != null && Utils.isNotEmpty(propertyGroupVo.getPropertyList())) {
                    addTypeItem(propertyGroupVo);

                    List<DishStandardVo> dishStandardVos = propertyGroupVo.getPropertyList();
                    int rowCount = dishStandardVos.size();
                    for (int propertyIndex = 0; propertyIndex < rowCount; propertyIndex++) {
                        addItem(typeIndex, propertyIndex, propertyGroupVo);
                    }
                }
            }
        }

    }

    /**
     * 设置规格的名称
     *
     * @param textView
     * @param typeIndex       类别的索引
     * @param propertyIndex   属性在其类别下的索引
     * @param propertyGroupVo
     */
    private void setName(TextView textView, int typeIndex, int propertyIndex,
                         PropertyGroupVo<DishStandardVo> propertyGroupVo) {
        textView.setText(propertyGroupVo.getProperty(propertyIndex).getName());
        textView.setTag(new Position(typeIndex, propertyIndex));
        if (manager.isSetmeal()) {
            textView.setClickable(false);
        } else {
            textView.setOnClickListener(this);
        }
        DishStandardVo standardVo = propertyGroupVo.getPropertyList().get(propertyIndex);
        //设置背景
        if (standardVo.getClearStatus() == ClearStatus.CLEAR) {
            textView.setBackgroundResource(R.drawable.ic_dish_standard_clear_bg);
            if (mBaseShoppingCart.getIsSalesReturn()) {
                //设置是否选中
                if (propertyGroupVo.getPropertyList().get(propertyIndex).isSelected()) {
                    textView.setSelected(true);
                    mStandards.add(propertyGroupVo.getProperty(propertyIndex));
                } else {
                    textView.setSelected(false);
                }
                //设置点击事件
                switch (standardVo.getState()) {
                    case DishStandardVo.ENABLE:
                        textView.setActivated(false);
                        break;
                    case DishStandardVo.DISABLE:
                        textView.setActivated(true);
                        break;
                    default:
                        Log.d(TAG, "No state");
                        break;
                }
            } else {
                textView.setSelected(false);
                textView.setActivated(true);
            }
        } else {
            textView.setBackgroundResource(R.drawable.ic_dish_property_item_bg);
            //设置是否选中
            if (propertyGroupVo.getPropertyList().get(propertyIndex).isSelected()) {
                textView.setSelected(true);
                mStandards.add(propertyGroupVo.getProperty(propertyIndex));
            } else {
                textView.setSelected(false);
            }
            //设置点击事件
            switch (standardVo.getState()) {
                case DishStandardVo.ENABLE:
                    textView.setActivated(false);
                    break;
                case DishStandardVo.DISABLE:
                    textView.setActivated(true);
                    break;
                default:
                    Log.d(TAG, "No item clicked");
                    break;
            }
        }
    }

    /**
     * 添加规格的类别标题
     *
     * @param propertyGroup
     */
    private void addTypeItem(PropertyGroupVo<DishStandardVo> propertyGroup) {
        TextView tv = new TextView(getContext());
        tv.setText(propertyGroup.getPropertyType().getName());
        tv.setTextSize(16);
        //tv.setTextSize(DensityUtil.dip2px(16));
        tv.setTextColor(getContext().getResources().getColor(R.color.dish_property_type_color));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
        params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 12);
        vContent.addView(tv, params);
    }

    private void addItem(int typeIndex, int propertyIndex,
                         PropertyGroupVo<DishStandardVo> propertyGroupVo) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(16);
//        textView.setTextSize(DensityUtil.dip2px(16));
        textView.setTextColor(getContext().getResources().getColorStateList(R.color.dish_type_text_selector));
        textView.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(MainApplication.getInstance(), 48));
        params.bottomMargin = DensityUtil.dip2px(MainApplication.getInstance(), 14);
        setName(textView, typeIndex, propertyIndex, propertyGroupVo);
        vContent.addView(textView, params);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isEmpty(mPropertyGroupVos)) {
            return;
        }
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
                    Log.d(TAG, "No state");
                    break;
            }

            EventDishPropertiesNotice eventDishPropertiesNotice = manager.selectStandard(mStandards, mBaseShoppingCart.getIsSalesReturn());
            if (mPropertListener != null) {
                mPropertListener.propertyData(eventDishPropertiesNotice);
            }
        }
    }

    public interface PropertListener {
        public void propertyData(EventDishPropertiesNotice eventDishPropertiesNotice);
    }
}
