package com.zhongmei.bty.snack.orderdish.buinessview;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.basemodule.customer.operates.TakeawayMemoDal;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishInterfaceListener;
import com.zhongmei.bty.snack.orderdish.adapter.ExtraOrderAdapter;
import com.zhongmei.bty.snack.orderdish.data.SDConstant.MobClick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.cashier_remark_layout)
public class RemarkView extends LinearLayout {
    private static final String TAG = RemarkView.class.getSimpleName();

    @ViewById(R.id.tv_order_remark)
    protected TextView tvOrderRemark;

    @ViewById(R.id.tv_single_remark)
    protected TextView tvSingleRemark;

    @ViewById(R.id.et_remark)
    protected EditText etRemark;

    @ViewById(R.id.item_packaged)
    ToggleButton mItemPackaged;

    @ViewById(R.id.pack_layout)
    RelativeLayout mPackLayout;

    @ViewById(R.id.extra_order_view)
    RecyclerView mExtraOrderView;

    @Bean
    ExtraOrderAdapter mExtraOrderAdapter;

    private boolean isOrderRemark = true;
    private OrderDishInterfaceListener listener;

    protected ShopcartItemBase shopcartItem;


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



    public RemarkView(Context context, ShopcartItemBase shopcartItem, String orderRemark, boolean isOrderRemark) {
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

    public void judgePackLayoutVisible(ShopcartItemBase shopcartItem) {
        if (!ShoppingCart.getInstance().isHereOrTake()
                || shopcartItem == null || shopcartItem instanceof SetmealShopcartItem
                || ShoppingCart.getInstance().getIsSalesReturn() == Boolean.TRUE) {
            mPackLayout.setVisibility(View.GONE);
        } else {
            mPackLayout.setVisibility(View.VISIBLE);
            if (shopcartItem.getPack()) {
                mItemPackaged.setChecked(true);
            }
        }
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

        judgePackLayoutVisible(shopcartItem);

        if (isOrderRemark) {
            tvOrderRemark.setBackgroundResource(R.drawable.discount_left_selected);
            tvOrderRemark.setTextColor(getResources().getColor(R.color.text_white));
            tvSingleRemark.setBackgroundResource(R.drawable.discount_right_nomail);
            tvSingleRemark.setTextColor(getResources().getColor(R.color.text_blue));
                        etRemark.setText(orderRemark);
            etRemark.setSelection(etRemark.getText().length());
            iPanelItemSettings.setSnackRemarkType(REMARK_ORDER);
            loadTradeRemark();
        } else {
            tvOrderRemark.setBackgroundResource(R.drawable.discount_left_nomail);
            tvOrderRemark.setTextColor(getResources().getColor(R.color.text_blue));
            tvSingleRemark.setBackgroundResource(R.drawable.discount_right_selected);
            tvSingleRemark.setTextColor(getResources().getColor(R.color.text_white));
                        etRemark.setText(dishRemark);
            etRemark.setSelection(etRemark.getText().length());
            iPanelItemSettings.setSnackRemarkType(REMARK_DISH);
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
                OrderDish orderDish = shopcartItem.getOrderDish();
                DishShop dishShop = DishCache.getDishHolder().get(orderDish.getBrandDishId());
                if (dishShop == null) {
                    return Collections.emptyList();
                } else {
                    List<DishBrandProperty> dishPropertyList;
                    if (orderDish instanceof OrderSetmeal) {
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


    @Click({R.id.tv_order_remark, R.id.tv_single_remark, R.id.item_packaged})
    protected void click(View v) {
        switch (v.getId()) {
            case R.id.tv_order_remark:
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }
                isOrderRemark = true;
                tvOrderRemark.setBackgroundResource(R.drawable.discount_left_selected);
                tvOrderRemark.setTextColor(getResources().getColor(R.color.text_white));
                tvSingleRemark.setBackgroundResource(R.drawable.discount_right_nomail);
                tvSingleRemark.setTextColor(getResources().getColor(R.color.text_blue));

                                if (mCommonTradeRemarks != null) {
                    if (mExtraOrderAdapter != null) {
                        mExtraOrderAdapter.setItems(mCommonTradeRemarks);
                    }
                } else {
                    loadTradeRemark();
                }

                                etRemark.setText(orderRemark);
                etRemark.setSelection(etRemark.getText().length());
                                iPanelItemSettings.setSnackRemarkType(REMARK_ORDER);
                break;
            case R.id.tv_single_remark:
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }

                                MobclickAgentEvent.onEvent(getContext(), MobClick.SNACK_ORDER_GOODS_SINGLE_REMARK);

                if (shopcartItem == null) {
                    ToastUtil.showShortToast(R.string.please_select_food);
                    return;
                }

                isOrderRemark = false;
                tvOrderRemark.setBackgroundResource(R.drawable.discount_left_nomail);
                tvOrderRemark.setTextColor(getResources().getColor(R.color.text_blue));
                tvSingleRemark.setBackgroundResource(R.drawable.discount_right_selected);
                tvSingleRemark.setTextColor(getResources().getColor(R.color.text_white));

                                if (mCommonDishRemarks != null) {
                    if (mExtraOrderAdapter != null) {
                        mExtraOrderAdapter.setItems(mCommonDishRemarks);
                    }
                } else {
                    loadDishRemark();
                }

                                etRemark.setText(dishRemark);
                etRemark.setSelection(etRemark.getText().length());
                                iPanelItemSettings.setSnackRemarkType(REMARK_DISH);
                break;
            case R.id.item_packaged:
                                MobclickAgentEvent.onEvent(getContext(), MobClick.SNACK_ORDER_GOODS_SINGLE_BALE);
                if (shopcartItem == null) {
                    ToastUtil.showShortToast(R.string.please_select_food);
                    return;
                }
                if (mItemPackaged.isChecked()) {
                    shopcartItem.setPack(true);
                    if (!ShoppingCart.getInstance().haveExtraChargeChf()) {                        ExtraCharge extraChargeChf = ExtraManager.getExtraChargeChf();
                        if (extraChargeChf != null) {
                            List<ExtraCharge> temp = new ArrayList<ExtraCharge>();
                            temp.add(extraChargeChf);
                            ShoppingCart.getInstance().addExtraCharge(temp);
                        }

                    }
                    ShoppingCart.getInstance().updateFastFoodDish(shopcartItem, false);
                } else {
                    shopcartItem.setPack(false);
                    ShoppingCart.getInstance().updateFastFoodDish(shopcartItem, false);
                }
                break;
            default:
                Log.d(TAG, "No item clicked");
                break;
        }
    }
}
