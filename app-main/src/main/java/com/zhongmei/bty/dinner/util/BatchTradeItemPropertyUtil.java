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

/**
 * Created by demo on 2018/12/15
 */
public class BatchTradeItemPropertyUtil implements View.OnClickListener {

    private static final String TAG = BatchTradeItemPropertyUtil.class.getSimpleName();

    private FragmentActivity mActivity;

    private View mParentView;

    //操作栏位
    private LinearLayout vActionBar;
    //内容栏位
    private FrameLayout vContent;
    //菜品数量
    private CustomDishQuantityView vDishQuantity;
    //规格
    private Button btnStandard;
    //做法
    protected Button btnProperty;
    //单品备注
    private Button btnRemark;
    //加料
    protected Button btnExtra;
    //变价
    private Button btnChangePrice;
    //变价标示
    private ImageView btnChagepriceAnchor;
    //分割线
    private View vDivideLine;

    /**
     * 当前内容
     */
    private View mCurrentContentView;
    /**
     * 空态页
     */
    private CustomEmptyView mCustomEmptyView;
    /**
     * 做法页
     */
    private TasteView mTasteView;
    /**
     * 加料页
     */
    private ExtraView mExtraView;
    /**
     * 备注页
     */
    private DinnerRemarkView mRemarkView;

    private String mMemo;

    private ChangePageListener mChangePageListener;

    private DinnerDishMiddleFragment.IChangePageListener mListener;

    private List<ShopcartItem> shopcartItems = new ArrayList<>();

    private BatchDishPropertyManager mDishPropertyManager;
    private AsyncTask mAsyncTask;

    private ArrayList<PropertyGroupVo<DishPropertyVo>> tasteGroupVo = new ArrayList<>();
    /**
     * 加料列表
     */
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

        //批量模式下，不展示数量和规格
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
        //过滤未生效的item
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

        //刷新内容页
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

    /**
     * 刷新菜品属性内容
     *
     * @param batchDishPropertyVo
     */
    private void refreshView(BatchDishPropertyVo batchDishPropertyVo) {
        //做法
        setRecipe(batchDishPropertyVo);

        //加料
        setExtra(batchDishPropertyVo);
    }

    //设置做法
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

    //设置加料
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

    /**
     * 展示加料页
     */
    private void showExtraView() {
        ViewUtil.setButtonSelected(vActionBar, btnExtra);

        if (mExtraView == null) {
            mExtraView = ExtraView_.build(mActivity, true);
            mExtraView.setListener(new OrderDishListenerImp() {
//                @Override
//                public void onAddMaterial(ExtraView.ExtraInfo extraInfo, BigDecimal qty) {
//                    if(extraInfo != null && extraInfo.orderExtra != null){
//                        if(qty == null){
//                            qty = BigDecimal.ZERO;
//                        }
//                        extraInfo.orderExtra.setQty(qty, qty);
//                    }
//                }

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

    /**
     * 展示做法页
     */
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

                    Map<Long, OrderProperty> typePropertyMap = new HashMap<>();//存储propertypeid和OrderProperty
                    for (OrderProperty property : propertyList) {
                        typePropertyMap.put(property.getPropertyType().getId(), property);
                    }

                    for (ShopcartItem shopcartItem : shopcartItems) {
                        List<OrderProperty> itemProperties = shopcartItem.getProperties();
                        if (Utils.isNotEmpty(itemProperties)) {
                            //移出与新增口味做法同类型的口味做法
                            for (int i = itemProperties.size() - 1; i >= 0; i--) {
                                OrderProperty property = itemProperties.get(i);
                                if (typePropertyMap.containsKey(property.getPropertyType().getId())) {
                                    itemProperties.remove(i);
                                }
                            }
                        }

                        //新增新加的口味做法
                        if (itemProperties == null) {
                            itemProperties = new ArrayList<>();
                            shopcartItem.setProperties(itemProperties);
                        }
                        itemProperties.addAll(propertyList);

                        //更新购物车
                        mShoppingCart.updateDinnerDish(shopcartItem, false);
                    }
                }
            });
        }
        mTasteView.setList(tasteGroupVo);
        showCustomContentView(mTasteView);
    }

    /**
     * 展示备注页
     */
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

    /**
     * 刷新内容页
     *
     * @param customContentView
     */
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
