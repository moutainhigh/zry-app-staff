package com.zhongmei.bty.dinner.util;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.bty.snack.orderdish.buinessview.ExtraInfo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.bty.cashier.orderdishmanager.BatchDishPropertyManager;
import com.zhongmei.bty.cashier.orderdishmanager.entity.BatchDishPropertyVo;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertyVo;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.dinner.orderdish.DinnerDishMiddleFragment;
import com.zhongmei.bty.dinner.orderdish.view.DinnerRemarkView;
import com.zhongmei.bty.dinner.orderdish.view.DinnerRemarkView_;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishListenerImp;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomEmptyView_;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView_;
import com.zhongmei.bty.snack.orderdish.buinessview.TasteView;
import com.zhongmei.bty.snack.orderdish.buinessview.TasteView_;
import com.zhongmei.bty.snack.orderdish.view.CustomDishQuantityView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BatchTradeItemPropertyUtil implements View.OnClickListener {

    private static final String TAG = BatchTradeItemPropertyUtil.class.getSimpleName();

    private FragmentActivity mActivity;

    private View mParentView;

        private LinearLayout vActionBar;
        private FrameLayout vContent;
        private CustomDishQuantityView vDishQuantity;
        private Button btnStandard;
        protected Button btnProperty;
        private Button btnRemark;
        protected Button btnExtra;
        private Button btnChangePrice;
        private ImageView btnChagepriceAnchor;
        private View vDivideLine;


    private View mCurrentContentView;

    private CustomEmptyView mCustomEmptyView;

    private TasteView mTasteView;

    private ExtraView mExtraView;

    private DinnerRemarkView mRemarkView;

    private String mMemo;

    private ChangePageListener mChangePageListener;

    private DinnerDishMiddleFragment.IChangePageListener mListener;

    private List<ShopcartItem> shopcartItems = new ArrayList<>();

    private BatchDishPropertyManager mDishPropertyManager;
    private AsyncTask mAsyncTask;

    private ArrayList<PropertyGroupVo<DishPropertyVo>> tasteGroupVo = new ArrayList<>();

    private List<ExtraInfo> extraInfoList = new ArrayList<>();

    private DinnerShoppingCart mShoppingCart;

    public void setOperateButton(FragmentActivity activity, List<DishDataItem> items, View parentView, ChangePageListener changePageListener, DinnerDishMiddleFragment.IChangePageListener listener) {
        mActivity = activity;
        mParentView = parentView;
        mChangePageListener = changePageListener;
        mListener = listener;
        mShoppingCart = DinnerShoppingCart.getInstance();
        initView();
        setDishDataItems(items);
    }

    private void initView() {
        vActionBar = (LinearLayout) mParentView.findViewById(R.id.v_action_bar);
        vContent = (FrameLayout) mParentView.findViewById(R.id.v_content);
        vDishQuantity = (CustomDishQuantityView) mParentView.findViewById(R.id.v_dish_quantity);
        btnStandard = (Button) mParentView.findViewById(R.id.btn_standard);
        btnProperty = (Button) mParentView.findViewById(R.id.btn_property);
        btnRemark = (Button) mParentView.findViewById(R.id.btn_remark);
        btnExtra = (Button) mParentView.findViewById(R.id.btn_extra);
        btnChangePrice = (Button) mParentView.findViewById(R.id.btn_change_price);
        btnChagepriceAnchor = (ImageView) mParentView.findViewById(R.id.btn_chageprice_anchor);
        vDivideLine = mParentView.findViewById(R.id.v_divide_line);

                vDishQuantity.setVisibility(View.INVISIBLE);
        btnStandard.setVisibility(View.GONE);
        btnChangePrice.setVisibility(View.GONE);

        btnProperty.setOnClickListener(this);
        btnRemark.setOnClickListener(this);
        btnExtra.setOnClickListener(this);

        if (mShoppingCart.getOrder().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN)
            vDishQuantity.setTradeNmb(mShoppingCart.getSubTradeCount());
    }

    private void setDishDataItems(List<DishDataItem> items) {
                if (Utils.isNotEmpty(items)) {
            for (DishDataItem item : items) {
                if (item.getBase() instanceof ShopcartItem) {
                    shopcartItems.add((ShopcartItem) item.getBase());
                }
            }
        }
        initData();
    }

    protected void initData() {
        boolean hasUnsaveItem = Utils.isNotEmpty(shopcartItems);
        ViewUtil.setButtonEnabled(btnProperty, hasUnsaveItem);
        ViewUtil.setButtonEnabled(btnExtra, hasUnsaveItem);
        ViewUtil.setButtonEnabled(btnRemark, hasUnsaveItem);
        if (hasUnsaveItem) {
            mDishPropertyManager = new BatchDishPropertyManager();
            loadProperties();
        }

                showCustomContentView(null);
    }

    private void loadProperties() {
        mAsyncTask = new AsyncTask<Void, Void, BatchDishPropertyVo>() {

            @Override
            protected BatchDishPropertyVo doInBackground(Void... params) {
                return mDishPropertyManager.loadData(shopcartItems);
            }

            @Override
            protected void onPostExecute(BatchDishPropertyVo batchDishPropertyVo) {
                refreshView(batchDishPropertyVo);
            }
        }.execute();
    }


    private void refreshView(BatchDishPropertyVo batchDishPropertyVo) {
                setRecipe(batchDishPropertyVo);

                setExtra(batchDishPropertyVo);
    }

        private void setRecipe(BatchDishPropertyVo batchDishPropertyVo) {
        tasteGroupVo.clear();

        if (Utils.isNotEmpty(batchDishPropertyVo.propertyGroupVoList)) {
            int size = batchDishPropertyVo.propertyGroupVoList.size();
            for (int i = 0; i < size; i++) {
                DishPropertyType type =
                        batchDishPropertyVo.propertyGroupVoList.get(i).getPropertyType();
                if (type != null && type.getPropertyKind() == PropertyKind.PROPERTY) {
                    tasteGroupVo.add(batchDishPropertyVo.propertyGroupVoList.get(i));
                }
            }
        }
        btnProperty.setText(R.string.dish_property);
        ViewUtil.setButtonEnabled(btnProperty, Utils.isNotEmpty(tasteGroupVo));

        ViewUtil.setViewVisibility(btnProperty, View.VISIBLE);
    }

        private void setExtra(BatchDishPropertyVo batchDishPropertyVo) {
        extraInfoList.clear();

        if (batchDishPropertyVo.extraList != null) {
            for (OrderExtra orderExtra : batchDishPropertyVo.extraList) {
                ExtraInfo extraInfo = new ExtraInfo(orderExtra, false);
                extraInfo.setQty(BigDecimal.ZERO);
                extraInfoList.add(extraInfo);
            }
        }

        btnExtra.setText(R.string.dish_extra);
        ViewUtil.setButtonEnabled(btnExtra, Utils.isNotEmpty(extraInfoList));

        ViewUtil.setViewVisibility(btnExtra, View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_extra:
                if (!ClickManager.getInstance().isClicked(R.id.btn_extra)) {
                    showExtraView();
                }
                break;
            case R.id.btn_property:
                if (!ClickManager.getInstance().isClicked(R.id.btn_property)) {
                    showPropertyView();
                }
                break;
            case R.id.btn_remark:
                showRemarkView();
                break;
            default:
                break;
        }
    }


    private void showExtraView() {
        ViewUtil.setButtonSelected(vActionBar, btnExtra);

        if (mExtraView == null) {
            mExtraView = ExtraView_.build(mActivity, true);
            mExtraView.setListener(new OrderDishListenerImp() {

                @Override
                public void onBatchAddExtra(List<ExtraInfo> extraInfoList) {
                    if (Utils.isEmpty(shopcartItems) || Utils.isEmpty(extraInfoList)) {
                        return;
                    }

                    for (ShopcartItem shopcartItem : shopcartItems) {
                        for (ExtraInfo extraInfo : extraInfoList) {
                            try {
                                OrderExtra orderExtra = extraInfo.orderExtra.clone();
                                BigDecimal qty = shopcartItem.getExtraQty(orderExtra);
                                shopcartItem.setExtra(orderExtra, extraInfo.getQty().add(qty));
                                mShoppingCart.updateDinnerDish(shopcartItem, false);
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }
                }
            });
        }
        mExtraView.setListForBatchOperation(extraInfoList);
        showCustomContentView(mExtraView);
    }


    private void showPropertyView() {
        ViewUtil.setButtonSelected(vActionBar, btnProperty);

        if (mTasteView == null) {
            mTasteView = TasteView_.build(mActivity, true);
            mTasteView.setUnFold(false);
            mTasteView.setListener(new OrderDishListenerImp() {
                @Override
                public void onBatchAddProperty(List<OrderProperty> propertyList) {
                    if (Utils.isEmpty(shopcartItems) || Utils.isEmpty(propertyList)) {
                        return;
                    }

                    Map<Long, OrderProperty> typePropertyMap = new HashMap<>();                    for (OrderProperty property : propertyList) {
                        typePropertyMap.put(property.getPropertyType().getId(), property);
                    }

                    for (ShopcartItem shopcartItem : shopcartItems) {
                        List<OrderProperty> itemProperties = shopcartItem.getProperties();
                        if (Utils.isNotEmpty(itemProperties)) {
                                                        for (int i = itemProperties.size() - 1; i >= 0; i--) {
                                OrderProperty property = itemProperties.get(i);
                                if (typePropertyMap.containsKey(property.getPropertyType().getId())) {
                                    itemProperties.remove(i);
                                }
                            }
                        }

                                                if (itemProperties == null) {
                            itemProperties = new ArrayList<>();
                            shopcartItem.setProperties(itemProperties);
                        }
                        itemProperties.addAll(propertyList);

                                                mShoppingCart.updateDinnerDish(shopcartItem, false);
                    }
                }
            });
        }
        mTasteView.setList(tasteGroupVo);
        showCustomContentView(mTasteView);
    }


    private void showRemarkView() {
        if (ClickManager.getInstance().isClicked(R.id.btn_remark)) {
            return;
        }
        ViewUtil.setButtonSelected(vActionBar, btnRemark);

        mRemarkView = DinnerRemarkView_.build(mActivity, shopcartItems, mMemo);
        mRemarkView.setListener(new OrderDishListenerImp() {

            @Override
            public void onMemoChanged(String memo, boolean isOrderRemark) {
                if (!isOrderRemark) {
                    mMemo = memo;
                }
            }

            @Override
            public void onBatchMemoChange(String memo) {
                if (Utils.isEmpty(shopcartItems) || TextUtils.isEmpty(memo)) {
                    return;
                }

                for (ShopcartItem shopcartItem : shopcartItems) {
                    shopcartItem.setMemo(memo);
                    mShoppingCart.updateDinnerDish(shopcartItem, false);
                    mMemo = null;
                }
            }
        });
        showCustomContentView(mRemarkView);
    }


    private void showCustomContentView(View customContentView) {
        vContent.removeAllViews();
        if (customContentView == null) {
            if (mCustomEmptyView == null) {
                mCustomEmptyView = CustomEmptyView_.build(mActivity);
            }
            vContent.addView(mCustomEmptyView);
            setCurrentContentView(mCustomEmptyView);
        } else {
            vContent.addView(customContentView);
            setCurrentContentView(customContentView);
        }
    }

    public void setCurrentContentView(View currentContentView) {
        mCurrentContentView = currentContentView;
    }

    public void stopAsyncTask() {
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
        }
    }

    public void unregisterShoppingCartListener() {
        if (mShoppingCart != null) {
            mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.ORDER_DISH_MIDDLE);
        }
    }
}
