package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishInterfaceListener;
import com.zhongmei.bty.snack.orderdish.adapter.ExtraOrderAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 菜品备注页
 */
@EViewGroup(R.layout.dinner_remark_layout)
public class DinnerRemarkView extends LinearLayout {

    @ViewById(R.id.et_remark)
    protected EditText etRemark;

    @ViewById(R.id.extra_order_view)
    RecyclerView mExtraOrderView;

    @ViewById(R.id.btn_ok)
    Button btnOk;

    @Bean
    ExtraOrderAdapter mExtraOrderAdapter;

    private boolean isOrderRemark = false;//默认是整单备注

    private boolean isBatchMode = false;//是否是批量操作模式

    private OrderDishInterfaceListener listener;

    protected ShopcartItemBase shopcartItem;

    protected List<ShopcartItem> shopcartItems;//批量操作模式下使用

    /**
     * 常用整单备注
     */
    private List<String> mCommonTradeRemarks;

    private String orderRemark;

    /**
     * 常用单品备注
     */
    private List<String> mCommonDishRemarks;

    private String dishRemark;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String remark = s.toString().trim();
            if (listener != null) {
                listener.onMemoChanged(remark, isOrderRemark);
            }
            dishRemark = remark;
        }
    };

    public DinnerRemarkView(Context context, ShopcartItemBase shopcartItem, String orderRemark, boolean isOrderRemark) {
        super(context);
        this.shopcartItem = shopcartItem;
        if (shopcartItem != null) {
            this.dishRemark = shopcartItem.getMemo();
        }
        this.orderRemark = orderRemark;
        this.isOrderRemark = isOrderRemark;
        this.isBatchMode = false;
    }

    public DinnerRemarkView(Context context, List shopcartItems, String batchDishRemark) {
        super(context);
        this.shopcartItems = shopcartItems;
        this.dishRemark = batchDishRemark;
        this.orderRemark = null;
        this.isOrderRemark = false;
        this.isBatchMode = true;
    }

    public void setListener(OrderDishInterfaceListener listener) {
        this.listener = listener;
    }


    public void setDishRemark(String dishRemark) {
        this.dishRemark = dishRemark;
        if (!isOrderRemark) {
            etRemark.removeTextChangedListener(mTextWatcher);
            etRemark.setText(dishRemark);
            etRemark.setSelection(etRemark.getText().length());
            etRemark.addTextChangedListener(mTextWatcher);
        }
    }

    @AfterViews
    protected void initView() {
        mExtraOrderView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mExtraOrderView.setAdapter(mExtraOrderAdapter);
        mExtraOrderAdapter.setOnItemClickListener(new ExtraOrderAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String item = (String) mExtraOrderAdapter.getItem(position);
                StringBuilder stringBuilder = new StringBuilder();
                if (isOrderRemark) {
                    if (!TextUtils.isEmpty(orderRemark)) {
                        stringBuilder.append(orderRemark);
                        stringBuilder.append(",");
                    }
                } else {
                    if (!TextUtils.isEmpty(dishRemark)) {
                        stringBuilder.append(dishRemark);
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append(item);
                etRemark.setText(stringBuilder.toString());
                etRemark.setSelection(etRemark.getText().length());
            }
        });

        //设置已有备注
        etRemark.setText(dishRemark);
        etRemark.addTextChangedListener(mTextWatcher);
        if (!isBatchMode) {
            etRemark.setSelection(etRemark.getText().length());
            btnOk.setVisibility(View.GONE);
        } else {
            etRemark.setText(dishRemark);
            btnOk.setVisibility(View.VISIBLE);
            btnOk.setOnClickListener(batchOkClickListener);
        }

        loadDishRemark();
    }

    private OnClickListener batchOkClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ok:
                    String memo = etRemark.getText().toString().trim();
                    if (TextUtils.isEmpty(memo)) {
                        ToastUtil.showShortToast(R.string.please_input_or_select_memo);
                        return;
                    }

                    if (listener != null) {
                        listener.onBatchMemoChange(memo);
                        //回调后清空输入
                        dishRemark = null;
                        etRemark.setText("");
                    }
                    break;
            }
        }
    };

    /**
     * 加载单品常用备注
     */
    private void loadDishRemark() {
        AsyncTask<Void, Void, List<String>> asyncTask = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                if (isBatchMode) {
                    return loadBatchDishRemark();
                } else {
                    return loadSingleDishRemark();
                }
            }

            private List<String> loadBatchDishRemark() {
                if (Utils.isEmpty(shopcartItems)) {
                    return null;
                }

                List<String> memoResultList = setPublicProperty();//备注公共库

                for (ShopcartItem shopcartItem : shopcartItems) {
                    OrderDish orderDish = shopcartItem.getOrderDish();
                    DishShop dishShop = DishCache.getDishHolder().get(orderDish.getBrandDishId());
                    if (dishShop == null) {
                        continue;
                    } else {
                        List<DishBrandProperty> dishPropertyList = getDishMemoList(orderDish, dishShop);
                        if (Utils.isNotEmpty(dishPropertyList)) {
                            List<String> memoList = filterMemo(dishPropertyList);
                            if (Utils.isNotEmpty(memoList)) {
                                memoResultList.retainAll(memoList);
                            }
                        }
                    }
                }

                return memoResultList;
            }

            private List<String> loadSingleDishRemark() {
                if (shopcartItem == null) {
                    return null;
                }
                OrderDish orderDish = shopcartItem.getOrderDish();
                DishShop dishShop = DishCache.getDishHolder().get(orderDish.getBrandDishId());
                if (dishShop == null) {
                    return Collections.emptyList();
                } else {
                    List<DishBrandProperty> dishPropertyList = getDishMemoList(orderDish, dishShop);
                    if (Utils.isEmpty(dishPropertyList)) {
                        return setPublicProperty();
                    }

                    List<String> memoList = filterMemo(dishPropertyList);
                    //如果未设置属性，拿公共库的
                    if (memoList.isEmpty()) {
                        memoList = setPublicProperty();
                    }
                    return memoList;
                }
            }

            /**
             * 从属性中过滤备注
             * @param dishPropertyList
             * @return
             */
            private List<String> filterMemo(List<DishBrandProperty> dishPropertyList) {
                if (Utils.isEmpty(dishPropertyList)) {
                    return null;
                }

                List<String> memoList = new ArrayList<String>();
                for (DishBrandProperty dishBrandProperty : dishPropertyList) {
                    DishProperty property = DishCache.getPropertyHolder().get(dishBrandProperty.getPropertyId());
                    if (property != null && property.getPropertyKind() == PropertyKind.MEMO) {
                        memoList.add(property.getName());
                    }
                }

                return memoList;
            }

            /**
             * 获取制定菜品的常用备注
             * @param orderDish
             * @param dishShop
             */
            private List<DishBrandProperty> getDishMemoList(OrderDish orderDish, DishShop dishShop) {
                if (orderDish instanceof OrderSetmeal) {
                    SetmealPropertyFilter filter = new SetmealPropertyFilter(dishShop);
                    return DishCache.getDishPropertyHolder().filter(filter);
                } else {
                    DishPropertyFilter filter = new DishPropertyFilter(dishShop);
                    return DishCache.getDishPropertyHolder().filter(filter);
                }
            }

            /**
             * 获取常用备注公共库
             */
            private List<String> setPublicProperty() {
                List<String> memoList = new ArrayList<>();
                List<DishProperty> dishPropertieList = DishCache.getPropertyHolder().filter(null);
                if (!Utils.isEmpty(dishPropertieList)) {
                    for (DishProperty property : dishPropertieList) {
                        if (property.getPropertyKind() == PropertyKind.MEMO) {
                            memoList.add(property.getName());
                        }
                    }
                }

                return memoList;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                mCommonDishRemarks = strings;
                if (mExtraOrderAdapter != null && mCommonDishRemarks != null) {
                    mExtraOrderAdapter.setItems(mCommonDishRemarks);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            asyncTask.execute();
        }
    }

    /**
     * 套餐明细的属性数据过滤器
     *
     * @version: 1.0
     * @date 2015年7月20日
     */
    private static class SetmealPropertyFilter implements DishCache.DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        SetmealPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId())
                    && entity.getPropertyKind() == PropertyKind.MEMO;
        }

    }

    /**
     * 单品或套餐的属性数据过滤器
     *
     * @version: 1.0
     * @date 2015年7月15日
     */
    private static class DishPropertyFilter implements DishCache.DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        DishPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId())
                    && entity.getPropertyKind() == PropertyKind.MEMO;
        }

    }

}
