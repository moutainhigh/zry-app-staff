package com.zhongmei.bty.dinner.orderdish;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 菜品批次设置
 */
public class DinnerDishSequenceFragment extends BasicFragment implements View.OnClickListener {

    public interface DishSequenceCallback {
        void onDishSequence(List<DishSequenceBean> dishSequenceBeans);
    }

    ListView slideListView;
    DishSequenceAdapter dishSequenceAdapter;
    TextView dish_item_total;
    DinnerShoppingCart mShoppingCart;
    DishSequenceCallback dishSequenceCallback;

    /*public static DinnerDishSequenceFragment show(FragmentActivity fragmentActivity, DishSequenceCallback callback) {
        DinnerDishSequenceFragment fragment = new DinnerDishSequenceFragment();
        fragment.setDishSequenceCallback(callback);
        fragment.show(fragmentActivity.getSupportFragmentManager(), "DinnerDishSequenceFragment");
        return fragment;
    }*/

    public static DinnerDishSequenceFragment show(FragmentManager fragmentManager, @IdRes int containerViewId,
                                                  DinnerShoppingCart dinnerShoppingCart, DishSequenceCallback callback) {
        DinnerDishSequenceFragment fragment = (DinnerDishSequenceFragment) fragmentManager.findFragmentByTag("dinnerDishSequenceFragment");
        if (fragment == null) {
            fragment = new DinnerDishSequenceFragment();
            fragment.mShoppingCart = dinnerShoppingCart;
            fragment.setDishSequenceCallback(callback);
            fragmentManager.beginTransaction()
                    .add(containerViewId, fragment)
                    .addToBackStack("dinnerDishSequenceFragment")
                    .commit();
        }
        return fragment;
    }

    public void setDishSequenceCallback(DishSequenceCallback dishSequenceCallback) {
        this.dishSequenceCallback = dishSequenceCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dinner_dish_sequence_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        slideListView = findViewById(R.id.dish_sequence_lv);
        dish_item_total = findViewById(R.id.dish_item_total);
        findViewById(R.id.dish_sequence_btn_ok).setOnClickListener(this);
        findViewById(R.id.dish_sequence_btn_cancel).setOnClickListener(this);
        final ViewGroup dish_sequence_num_sv = findViewById(R.id.dish_sequence_num_sv);
        dish_sequence_num_sv.setVisibility(View.GONE);
        initItemSequenceNum(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish_item_total.setText("");
                List<DishSequenceBean> itemSelected = dishSequenceAdapter.getItemSelected();
                if (itemSelected != null) {
                    for (DishSequenceBean bean : itemSelected) {
                        bean.sort = v.getId();
                        bean.isChecked = false;
                    }

                    dishSequenceAdapter.notifySort();
                    dish_sequence_num_sv.setVisibility(View.GONE);
                }
            }
        });

        List<DishSequenceBean> dishSequenceBeans = getDishSequenceBean(mShoppingCart);
        dishSequenceAdapter = new DishSequenceAdapter(getContext(), dishSequenceBeans);
        slideListView.setAdapter(dishSequenceAdapter);
        dishSequenceAdapter.setItemCheckListener(new DishSequenceAdapter.ItemCheckListener() {
            @Override
            public void onItemCheck(View v, boolean isChecked) {
                List<DishSequenceBean> itemSelected = dishSequenceAdapter.getItemSelected();
                dish_item_total.setText(itemSelected.size() + "");
                if (itemSelected != null && itemSelected.size() > 0) {
                    dish_sequence_num_sv.setVisibility(View.VISIBLE);
                } else {
                    dish_sequence_num_sv.setVisibility(View.GONE);
                }
            }
        });
    }

    public void initItemSequenceNum(View.OnClickListener listener) {
        Resources res = getResources();
        int[] sequenceKey = res.getIntArray(R.array.dish_sequence_key);
        String[] sequenceValue = res.getStringArray(R.array.dish_sequence_value);
        final ViewGroup dish_sequence_num_ll = findViewById(R.id.dish_sequence_num_ll);
        dish_sequence_num_ll.removeAllViewsInLayout();
        for (int i = 0; i < sequenceKey.length; i++) {
            View child = LayoutInflater.from(getContext()).inflate(R.layout.dinner_dish_sequence_fragment_num_item, dish_sequence_num_ll, false);
            Button button = (Button) child.findViewById(R.id.dish_sequence_btn_sort);
            dish_sequence_num_ll.addView(child);
            button.setId(sequenceKey[i]);
            button.setText(sequenceValue[i]);
            button.setOnClickListener(listener);
        }
    }

    private List<DishSequenceBean> getDishSequenceBean(DinnerShoppingCart mShoppingCart) {
        List<DishSequenceBean> dishSequenceOrderBy = new LinkedList<>();
        List<IShopcartItem> shoppingCartDish = mShoppingCart.filterDishList(mShoppingCart.getShoppingCartDish());
        Collections.reverse(shoppingCartDish);
        shopingCartDishOrderByServingOrder(dishSequenceOrderBy, shoppingCartDish);
        return dishSequenceOrderBy;
    }

    private void shopingCartDishOrderByServingOrder(List<DishSequenceBean> shopcartItemOrderBy, List<? extends IShopcartItemBase> shoppingCartDish) {
        for (IShopcartItemBase item : shoppingCartDish) {
            if (item.getType() == DishType.COMBO) {
                if (item instanceof IShopcartItem) {
                    IShopcartItem shopcartItem = (IShopcartItem) item;
                    shopingCartDishOrderByServingOrder(shopcartItemOrderBy, shopcartItem.getSetmealItems());
                }
            } else {
                int sort = getServingOrder(item);
                shopcartItemOrderBy.add(new DishSequenceBean(DishSequenceBean.SequenceTypeChild, item, sort));
            }
        }
    }

    private int getServingOrder(IShopcartItemBase item) {
        TradeItemExtraDinner tradeItemExtraDinner = item.getTradeItemExtraDinner();
        boolean hasServingOrder = tradeItemExtraDinner != null
                && tradeItemExtraDinner.getServingOrder() != null
                && tradeItemExtraDinner.getServingOrder() > 0;
        return hasServingOrder ? item.getTradeItemExtraDinner().getServingOrder() : 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dish_sequence_btn_ok:
                //dismiss();
                getFragmentManager().popBackStackImmediate();
                if (dishSequenceCallback != null) {
                    dishSequenceCallback.onDishSequence(dishSequenceAdapter.getData());
                }
                break;
            case R.id.dish_sequence_btn_cancel:
                //dismiss();
                getFragmentManager().beginTransaction().remove(this).commitNow();
                break;
        }
    }

    static class DishSequenceBean {
        static final int SequenceTypeGroup = 1;
        static final int SequenceTypeChild = 0;
        int sequenceType; //0内容，1标题
        IShopcartItemBase shopcartItem;
        int sort;
        boolean isChecked;

        public DishSequenceBean(int sequenceType, IShopcartItemBase shopcartItem, int sort) {
            this.sequenceType = sequenceType;
            this.shopcartItem = shopcartItem;
            this.sort = sort;
        }

        public static String getServingOrderName(Context context, int servingOrder) {
            Resources res = context.getResources();
            int[] sequenceKey = res.getIntArray(R.array.dish_sequence_key);
            String[] sequenceValue = res.getStringArray(R.array.dish_sequence_value);
            int keyIndex = 0;
            for (int i = 0; i < sequenceKey.length; i++) {
                if (sequenceKey[i] == servingOrder) {
                    keyIndex = i;
                    break;
                }
            }

            return sequenceValue[keyIndex];
        }

        public String getSkuName() {
            String standard = getPropertyName(PropertyKind.STANDARD);
            if (!TextUtils.isEmpty(standard)) {
                return String.format("%s(%s)", shopcartItem.getSkuName(), standard);
            }
            return shopcartItem.getSkuName();
        }

        public String getUnitPrice() {
            if (shopcartItem instanceof ShopcartItem) {
                return Utils.formatPrice(((ShopcartItem) shopcartItem).getOrderDish().getDishShop().getMarketPrice().doubleValue());
            }
            if (shopcartItem instanceof ReadonlyShopcartItem) {
                return Utils.formatPrice((((ReadonlyShopcartItem) shopcartItem)).tradeItem.getAmount().doubleValue());
            }
            return null;
        }

        public String getPropertyName() {
            return getPropertyName(PropertyKind.PROPERTY);
        }

        public String getPropertyName(PropertyKind propertyKind) {
            if (shopcartItem instanceof ShopcartItemBase) {
                ShopcartItemBase item = (ShopcartItemBase) shopcartItem;
                List<OrderProperty> orderProperties = item.getProperties();
                if (orderProperties != null && orderProperties.size() > 0) {
                    //return orderProperties.get(0).getPropertyName();
                    StringBuffer buffer = new StringBuffer();
                    for (Iterator<OrderProperty> it = orderProperties.iterator(); it.hasNext(); ) {
                        OrderProperty dishShop = it.next();
                        if (dishShop.getProperty().getPropertyKind() == propertyKind) {
                            buffer.append(dishShop.getPropertyName());
                            if (it.hasNext()) {
                                buffer.append(", ");
                            }
                        }
                    }
                    return buffer.toString();
                }
            }
            if (shopcartItem instanceof ReadonlyShopcartItemBase) {
                ReadonlyShopcartItemBase readonlyShopcartItem = (ReadonlyShopcartItemBase) shopcartItem;
                List<ReadonlyOrderProperty> orderPropertyList = readonlyShopcartItem.getProperties();
                if (orderPropertyList != null && orderPropertyList.size() > 0) {
                    //return orderPropertyList.get(0).tradeItemProperty.getPropertyName();
                    StringBuffer buffer = new StringBuffer();
                    for (Iterator<ReadonlyOrderProperty> it = orderPropertyList.iterator(); it.hasNext(); ) {
                        ReadonlyOrderProperty orderProperty = it.next();
                        if (orderProperty.getPropertyKind() == propertyKind) {
                            TradeItemProperty dishShop = orderProperty.tradeItemProperty;
                            buffer.append(dishShop.getPropertyName());
                            if (it.hasNext()) {
                                buffer.append(", ");
                            }
                        }
                    }
                    return buffer.toString();
                }
            }
            return null;
        }

        public String getIngredientsName() {
            if (shopcartItem instanceof ShopcartItemBase) {
                ShopcartItemBase item = (ShopcartItemBase) shopcartItem;
                Collection<ExtraShopcartItem> extraShopcartItems = item.getExtraItems();
                if (extraShopcartItems != null && extraShopcartItems.size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (Iterator<ExtraShopcartItem> it = extraShopcartItems.iterator(); it.hasNext(); ) {
                        DishShop dishShop = it.next().getDishShop();
                        buffer.append(dishShop.getName() + " x " + MathDecimal.toTrimZeroString(dishShop.getDishQty()));
                        if (it.hasNext()) {
                            buffer.append(", ");
                        }
                    }
                    return buffer.toString();
                }
            }
            if (shopcartItem instanceof ReadonlyShopcartItemBase) {
                ReadonlyShopcartItemBase readonlyShopcartItem = (ReadonlyShopcartItemBase) shopcartItem;
                Collection<ReadonlyExtraShopcartItem> extraShopcartItems = readonlyShopcartItem.getExtraItems();
                if (extraShopcartItems != null && extraShopcartItems.size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    for (Iterator<ReadonlyExtraShopcartItem> it = extraShopcartItems.iterator(); it.hasNext(); ) {
                        TradeItem dishShop = it.next().tradeItem;
                        buffer.append(dishShop.getDishName() + " x " + MathDecimal.toTrimZeroString(dishShop.getQuantity()));
                        if (it.hasNext()) {
                            buffer.append(", ");
                        }
                    }
                    return buffer.toString();
                }
            }
            return null;
        }

        public String getMemoName() {
            if (shopcartItem instanceof ShopcartItemBase) {
                ShopcartItemBase item = (ShopcartItemBase) shopcartItem;
                return item.getMemo();
            }
            if (shopcartItem instanceof ReadonlyShopcartItemBase) {
                ReadonlyShopcartItemBase readonlyShopcartItem = (ReadonlyShopcartItemBase) shopcartItem;
                return readonlyShopcartItem.tradeItem.getTradeMemo();
            }
            return null;
        }

        public boolean isCheckEnabled() {
            return shopcartItem.getId() == null || TextUtils.isEmpty(shopcartItem.getBatchNo());
        }
    }

    static class DishSequenceAdapter extends ArrayAdapter<DishSequenceBean> {

        interface ItemCheckListener {
            void onItemCheck(View v, boolean isChecked);
        }

        List<DishSequenceBean> dishSequenceBeans;
        List<DishSequenceBean> dishSequenceGroups;
        ItemCheckListener itemCheckListener;

        public DishSequenceAdapter(@NonNull Context context, List<DishSequenceBean> dishSequenceBeans) {
            super(context, 0);
            this.dishSequenceBeans = dishSequenceBeans;
            notifySort();
        }

        private List<DishSequenceBean> getDishSequenceGroup(List<DishSequenceBean> dishSequenceOrderBy) {
            List<DishSequenceBean> copyDishSequenceOrderBy = new ArrayList<>(dishSequenceOrderBy);
            Collections.sort(copyDishSequenceOrderBy, new Comparator<DishSequenceBean>() {
                @Override
                public int compare(DishSequenceBean o1, DishSequenceBean o2) {
                    return o1.sort - o2.sort;
                }
            });

            List<DishSequenceBean> result = new LinkedList<>();
            List<DishSequenceBean> resultNoNum = new LinkedList<>();
            for (DishSequenceBean item : copyDishSequenceOrderBy) {
                int sort = item.sort;
                if (sort > 0) {
                    if (result.isEmpty() || result.get(result.size() - 1).sort != sort) {
                        result.add(new DishSequenceBean(DishSequenceBean.SequenceTypeGroup, null, sort));
                    }

                    result.add(item);
                } else {
                    resultNoNum.add(item);
                }
            }

            if (resultNoNum.size() > 0) {
                if (result.size() > 0) {
                    result.add(new DishSequenceBean(DishSequenceBean.SequenceTypeGroup, null, 0));
                }

                result.addAll(resultNoNum);
            }
            return result;
        }

        public void setItemCheckListener(ItemCheckListener itemCheckListener) {
            this.itemCheckListener = itemCheckListener;
        }

        public void notifySort() {
            clear();
            this.dishSequenceGroups = getDishSequenceGroup(dishSequenceBeans);
            addAll(dishSequenceGroups);
            notifyDataSetChanged();
        }

        public List<DishSequenceBean> getData() {
            return dishSequenceGroups;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).sequenceType;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DishSequenceBean item = getItem(position);
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case DishSequenceBean.SequenceTypeGroup:
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.dinner_dish_sequence_fragment_dish_item_title, parent, false);
                        break;
                    case DishSequenceBean.SequenceTypeChild:
                    default:
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.dinner_dish_sequence_fragment_dish_item, parent, false);
                        break;
                }
            }

            switch (type) {
                case DishSequenceBean.SequenceTypeGroup:
                    bindItemGroupView(position, convertView, item);
                    break;
                case DishSequenceBean.SequenceTypeChild:
                default:
                    bindItemChildView(position, convertView, item);
                    break;
            }

            return convertView;
        }

        @NonNull
        public void bindItemGroupView(int position, @Nullable View convertView, @NonNull DishSequenceBean item) {
            TextView dish_sequence_title_tv = (TextView) convertView.findViewById(R.id.dish_sequence_title_tv);
            if (item.sort <= 0) {
                dish_sequence_title_tv.setText(R.string.dish_sequence_value_none);
            } else {
                dish_sequence_title_tv.setText(DishSequenceBean.getServingOrderName(getContext(), item.sort));
            }
        }

        @NonNull
        public void bindItemChildView(int position, @Nullable View convertView, @NonNull final DishSequenceBean item) {
            final CheckBox dish_name_cb = (CheckBox) convertView.findViewById(R.id.dish_name_cb);
            TextView dish_sku_name_tv = (TextView) convertView.findViewById(R.id.dish_sku_name_tv);
            TextView dish_qty_tv = (TextView) convertView.findViewById(R.id.dish_qty_tv);
            TextView dish_price_tv = (TextView) convertView.findViewById(R.id.dish_price_tv);
            TextView dish_property_name_tv = (TextView) convertView.findViewById(R.id.dish_property_name_tv);
            TextView dish_ingredient_name_tv = (TextView) convertView.findViewById(R.id.dish_ingredient_name_tv);
            TextView dish_memo_name_tv = (TextView) convertView.findViewById(R.id.dish_memo_name_tv);

            dish_name_cb.setChecked(item.isChecked);
            dish_name_cb.setEnabled(item.isCheckEnabled());
            dish_sku_name_tv.setText(item.getSkuName());
            BigDecimal qty = item.shopcartItem.getTotalQty();
            dish_qty_tv.setText("×" + MathDecimal.toTrimZeroString(qty));
            dish_price_tv.setText(item.getUnitPrice());
            dish_property_name_tv.setText(getStringRes(R.string.dish_property, item.getPropertyName()));
            dish_property_name_tv.setVisibility(TextUtils.isEmpty(item.getPropertyName()) ? View.GONE : View.VISIBLE);
            dish_ingredient_name_tv.setText(getStringRes(R.string.dish_extra, item.getIngredientsName()));
            dish_ingredient_name_tv.setVisibility(TextUtils.isEmpty(item.getIngredientsName()) ? View.GONE : View.VISIBLE);
            dish_memo_name_tv.setText(getStringRes(R.string.remark_str, item.getMemoName()));
            dish_memo_name_tv.setVisibility(TextUtils.isEmpty(item.getMemoName()) ? View.GONE : View.VISIBLE);
            /*dish_name_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.isChecked = dish_name_cb.isChecked();
                    if (itemCheckListener != null) {
                        itemCheckListener.onItemCheck(v, dish_name_cb.isChecked());
                    }
                }
            });*/
            convertView.setEnabled(item.isCheckEnabled());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dish_name_cb.setChecked(!dish_name_cb.isChecked());
                    item.isChecked = dish_name_cb.isChecked();
                    if (itemCheckListener != null) {
                        itemCheckListener.onItemCheck(v, dish_name_cb.isChecked());
                    }
                }
            });
        }

        public String getStringRes(@StringRes int stringRes, String text) {
            return getContext().getString(stringRes) + " : " + text;
        }

        public List<DishSequenceBean> getItemSelected() {
            List<DishSequenceBean> result = new ArrayList<>();
            for (DishSequenceBean bean : dishSequenceGroups) {
                if (bean.isChecked) {
                    result.add(bean);
                }
            }
            return result;
        }
    }
}
