package com.zhongmei.bty.dinner.orderdish;


import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.dinner.util.BatchTradeItemOperateUtil;
import com.zhongmei.bty.dinner.util.BatchTradeItemPropertyUtil;
import com.zhongmei.bty.dinner.util.BookingTradeItemPropertyUtil;
import com.zhongmei.bty.dinner.util.BuffetTradeItemOperateUtil;
import com.zhongmei.bty.dinner.util.BuffetTradeItemPropertyUtil;
import com.zhongmei.bty.dinner.util.CategoryDishOperateUtil;
import com.zhongmei.bty.dinner.util.GroupTradeItemPropertyUtil;
import com.zhongmei.bty.dinner.util.TradeItemOperateUtil;
import com.zhongmei.bty.dinner.util.TradeItemPropertyUtil;
import com.zhongmei.bty.dinner.util.WesternTradeItemPropertyUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 快餐点菜页中间栏位
 */
@EFragment(R.layout.dinner_dish_middle)
public class DinnerDishMiddleFragment extends MobclickAgentFragment {
    private static final String TAG = DinnerDishMiddleFragment.class.getSimpleName();
    public static final int DINNER_ORDER_MODE = 1;
    //团餐配菜
    public static final int GROUP_SLIDE_MODE = 2;
    //团餐点菜
    public static final int GROUP_ORDER_MODE = 3;
    //自助餐
    public static final int BUFFET_ORDER_MODE = 4;

    public static final int BOOKING_DISH_MODE = 5;
    //西餐
    public static final int WESTERN_DISH_MODE = 6;

    private IChangePageListener mListener;

    private ChangePageListener mChangePageListener;

    private TradeItemPropertyUtil itemPropertyUtil;
    private TradeItemOperateUtil itemOperateUtil;
    private BatchTradeItemOperateUtil batchTradeItemOperateUtil;
    private BatchTradeItemPropertyUtil batchTradeItemPropertyUtil;
    private CategoryDishOperateUtil categoryDishOperateUtil;
    private List<DishDataItem> mDishDataItems = new ArrayList<>();

    private boolean mIsCategory = false;
    //当前是有什么场景调用
    private int mCurrentMode = DINNER_ORDER_MODE;
    private boolean isComboEditMode = false;

    @Override
    public void onPause() {
        if (itemPropertyUtil != null) {
            itemPropertyUtil.dismissQuantityEditPopupWindow();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (itemPropertyUtil != null) {
            itemPropertyUtil.unregisterShoppingCartListener();
            itemPropertyUtil.stopAsyncTask();
        }
        if (batchTradeItemPropertyUtil != null) {
            batchTradeItemPropertyUtil.unregisterShoppingCartListener();
            batchTradeItemPropertyUtil.stopAsyncTask();
        }
        super.onDestroy();
    }

    public void setCurrentMode(int currentMode) {
        mCurrentMode = currentMode;
    }

    public void setComboEditMode(boolean isComboEditMode) {
        this.isComboEditMode = isComboEditMode;
    }

    /**
     * 接收购物车子项被选中事件
     *
     * @param item
     */
    public void doSelect(DishDataItem item) {
        mDishDataItems.clear();
        mDishDataItems.add(item);
    }

    public void doSelect(List<DishDataItem> items, boolean isCategory) {
        mDishDataItems.clear();
        mDishDataItems.addAll(items);
        mIsCategory = isCategory;
    }

    @AfterViews
    public void initData() {
        if (Utils.isNotEmpty(mDishDataItems)) {
            //单品操作
            if (mIsCategory) {
                categoryDishOperateUtil = new CategoryDishOperateUtil();
                categoryDishOperateUtil.setOperateButton(getActivity(), mDishDataItems, getView(), mChangePageListener, mListener);
            } else if (mDishDataItems.size() == 1) {
                itemPropertyUtil = getItemPropertyUtil(mCurrentMode);
                itemPropertyUtil.setCurrentMode(mCurrentMode);
                itemPropertyUtil.setPropertyButton(getActivity(), mDishDataItems.get(0), getView(), mListener, mChangePageListener);

                if (mCurrentMode == BUFFET_ORDER_MODE) {
                    itemOperateUtil = new BuffetTradeItemOperateUtil();
                    itemOperateUtil.setCurrentMode(mCurrentMode);
                } else {
                    itemOperateUtil = new TradeItemOperateUtil();
                    itemOperateUtil.setCurrentMode(mCurrentMode);
                }

                itemOperateUtil.setOperateButton(getActivity(), mDishDataItems.get(0), getView(), mChangePageListener, mListener, isComboEditMode);
                if (mCurrentMode == GROUP_SLIDE_MODE || mCurrentMode == BOOKING_DISH_MODE) {
                    itemOperateUtil.contorlSlideDishShow();
                }
                //批量操作
            } else if (mDishDataItems.size() > 1) {
                batchTradeItemPropertyUtil = new BatchTradeItemPropertyUtil();
                batchTradeItemPropertyUtil.setOperateButton(getActivity(), mDishDataItems, getView(), mChangePageListener, mListener);
                batchTradeItemOperateUtil = new BatchTradeItemOperateUtil();
                batchTradeItemOperateUtil.setOperateButton(getActivity(), mDishDataItems, getView(), mChangePageListener, mListener);
            }
        }
    }

    public static TradeItemPropertyUtil getItemPropertyUtil(int currentMode) {
        TradeItemPropertyUtil itemPropertyUtil;
        if (currentMode == GROUP_SLIDE_MODE || currentMode == GROUP_ORDER_MODE) {
            itemPropertyUtil = new GroupTradeItemPropertyUtil();
        } else if (currentMode == BUFFET_ORDER_MODE) {
            itemPropertyUtil = new BuffetTradeItemPropertyUtil();
        } else if (currentMode == BOOKING_DISH_MODE) {
            itemPropertyUtil = new BookingTradeItemPropertyUtil();
        } else if (currentMode == WESTERN_DISH_MODE) {
            itemPropertyUtil = new WesternTradeItemPropertyUtil();
        } else {
            itemPropertyUtil = new TradeItemPropertyUtil();
        }
        return itemPropertyUtil;
    }

    public interface IChangePageListener {
        void changePage(View contentView);

        /**
         * @param item 新增item才传，其余传null
         */
        void closePage(ShopcartItem item);

        /**
         * 编辑模式改变
         *
         * @param isEditMode true：正在添加套餐
         */
        void onEditModeChange(boolean isEditMode);
    }

    public void registerListener(IChangePageListener listener) {
        mListener = listener;
    }

    public void registerListener(ChangePageListener changePageListener) {
        this.mChangePageListener = changePageListener;
    }
}
