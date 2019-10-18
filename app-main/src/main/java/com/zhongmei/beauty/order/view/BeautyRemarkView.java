package com.zhongmei.beauty.order.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.basemodule.customer.operates.TakeawayMemoDal;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.beauty.order.event.ActionClearShopcart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishInterfaceListener;
import com.zhongmei.bty.snack.orderdish.adapter.ExtraOrderAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@EViewGroup(R.layout.beauty_remark_layout)
public class BeautyRemarkView extends LinearLayout {
    private static final String TAG = BeautyRemarkView.class.getSimpleName();

    @ViewById(R.id.tv_title)
    protected TextView tv_title;

    @ViewById(R.id.tv_order_remark)
    protected TextView tvOrderRemark;

    @ViewById(R.id.tv_single_remark)
    protected TextView tvSingleRemark;

    @ViewById(R.id.et_remark)
    protected EditText etRemark;

    @ViewById(R.id.extra_order_view)
    RecyclerView mExtraOrderView;

    @Bean
    ExtraOrderAdapter mExtraOrderAdapter;

    private boolean isOrderRemark = true;
    private OrderDishInterfaceListener listener;

    protected IShopcartItemBase shopcartItem;


    private List<String> mCommonTradeRemarks;

    private String orderRemark;


    private List<String> mCommonDishRemarks;

    private String dishRemark;

        private IPanelItemSettings iPanelItemSettings;

        public static final int REMARK_ORDER = 0;
        public static final int REMARK_DISH = 1;

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

            if (isOrderRemark) {
                orderRemark = remark;
            } else {
                dishRemark = remark;
            }
        }
    };


    public BeautyRemarkView(Context context, IShopcartItemBase shopcartItem, String orderRemark, boolean isOrderRemark) {
        super(context);
        this.shopcartItem = shopcartItem;
        if (shopcartItem != null) {
            this.dishRemark = shopcartItem.getMemo();
        }
        this.orderRemark = orderRemark;
        this.isOrderRemark = isOrderRemark;
    }

    public void setListener(OrderDishInterfaceListener listener) {
        this.listener = listener;
    }


    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
        if (isOrderRemark) {
            etRemark.removeTextChangedListener(mTextWatcher);
            etRemark.setText(orderRemark);
            etRemark.setSelection(etRemark.getText().length());
            etRemark.addTextChangedListener(mTextWatcher);
        }
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
        iPanelItemSettings = SettingManager.getSettings(IPanelItemSettings.class);
        mExtraOrderView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mExtraOrderView.setAdapter(mExtraOrderAdapter);
                if (shopcartItem != null) {
            isOrderRemark = false;
            tv_title.setText(R.string.orderSingleRemark);        }
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


        if (isOrderRemark) {
            tvOrderRemark.setSelected(true);
            tvSingleRemark.setSelected(false);
                        etRemark.setText(orderRemark);
            etRemark.setSelection(etRemark.getText().length());
            iPanelItemSettings.setSnackRemarkType(REMARK_ORDER);
            loadTradeRemark();
        } else {
            tvOrderRemark.setSelected(false);
            tvSingleRemark.setSelected(true);
                        etRemark.setText(dishRemark);
            etRemark.setSelection(etRemark.getText().length());
            loadDishRemark();
        }

        etRemark.addTextChangedListener(mTextWatcher);
    }


    private void loadTradeRemark() {
        AsyncTask<Void, Void, List<String>> asyncTask = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                List<String> commonTradeRemarks = new ArrayList<String>();
                try {
                    TakeawayMemoDal takeawayMemoDal = OperatesFactory.create(TakeawayMemoDal.class);
                    List<TakeawayMemo> orderMemos = takeawayMemoDal.getDataList();
                    if (Utils.isNotEmpty(orderMemos)) {
                        for (TakeawayMemo orderMemo : orderMemos) {
                            commonTradeRemarks.add(orderMemo.getMemoContent());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return commonTradeRemarks;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                mCommonTradeRemarks = strings;
                if (mExtraOrderAdapter != null) {
                    mExtraOrderAdapter.setItems(mCommonTradeRemarks);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }
    }


    private void loadDishRemark() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, List<String>> asyncTask = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                if (shopcartItem == null) {
                    return null;
                }
                DishShop dishShop = DishCache.getDishHolder().get(shopcartItem.getSkuId());
                if (dishShop == null) {
                    return Collections.emptyList();
                } else {
                    List<DishBrandProperty> dishPropertyList;
                                        if (shopcartItem instanceof SetmealShopcartItem || shopcartItem instanceof ReadonlySetmealShopcartItem) {
                        SetmealPropertyFilter filter = new SetmealPropertyFilter(dishShop);
                        dishPropertyList = DishCache.getDishPropertyHolder().filter(filter);
                    } else {
                        DishPropertyFilter filter = new DishPropertyFilter(dishShop);
                        dishPropertyList = DishCache.getDishPropertyHolder().filter(filter);
                    }

                    if (Utils.isEmpty(dishPropertyList)) {
                        return Collections.emptyList();
                    } else {
                        List<String> memoList = new ArrayList<String>();
                        for (DishBrandProperty dishBrandProperty : dishPropertyList) {
                            DishProperty property = DishCache.getPropertyHolder().get(dishBrandProperty.getPropertyId());
                            if (property != null && property.getPropertyKind() == PropertyKind.MEMO) {
                                memoList.add(property.getName());
                            }
                        }

                        return memoList;
                    }
                }
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                if (strings == null) {
                    ToastUtil.showShortToast(R.string.please_select_food);
                    return;
                }
                mCommonDishRemarks = strings;
                if (mExtraOrderAdapter != null) {
                    mExtraOrderAdapter.setItems(mCommonDishRemarks);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }
    }


    private static class SetmealPropertyFilter implements DishCache.DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        SetmealPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }


    private static class DishPropertyFilter implements DishCache.DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        DishPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }

    public void onEventMainThread(ActionClearShopcart event) {
        tvOrderRemark.setText("");
        tvSingleRemark.setText("");
    }

    @Click({R.id.tv_order_remark, R.id.tv_single_remark, R.id.item_packaged})
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.tv_order_remark:
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }
                isOrderRemark = true;
                tvOrderRemark.setSelected(true);
                tvSingleRemark.setSelected(false);
                                if (mCommonTradeRemarks != null) {
                    if (mExtraOrderAdapter != null) {
                        mExtraOrderAdapter.setItems(mCommonTradeRemarks);
                    }
                } else {
                    loadTradeRemark();
                }

                                etRemark.setText(orderRemark);
                etRemark.setSelection(etRemark.getText().length());
                break;
            case R.id.tv_single_remark:
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }

                if (shopcartItem == null) {
                    ToastUtil.showShortToast(R.string.please_select_food);
                    return;
                }

                isOrderRemark = false;
                tvOrderRemark.setSelected(false);
                tvSingleRemark.setSelected(true);

                                if (mCommonDishRemarks != null) {
                    if (mExtraOrderAdapter != null) {
                        mExtraOrderAdapter.setItems(mCommonDishRemarks);
                    }
                } else {
                    loadDishRemark();
                }

                                etRemark.setText(dishRemark);
                etRemark.setSelection(etRemark.getText().length());
                break;
            default:
                Log.d(TAG, "No item clicked");
                break;
        }
    }
}
