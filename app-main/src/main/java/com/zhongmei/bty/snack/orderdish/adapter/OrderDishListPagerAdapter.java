package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.zhongmei.bty.basemodule.orderdish.bean.DishPageInfo;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.util.SettingManager;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class OrderDishListPagerAdapter extends PagerAdapter {
    protected Context mContext;

    private LayoutInflater mLayoutInflater;

    private List<DishPageInfo> mDataSet;

    private long mLastItemClickTime = 0L;

    private long INTERVALTIME = 0L;

    private boolean isEditMode;
    private boolean isHidClearNumber;
    private int mGridHeight = 0;
    protected int dishCardType;

    public OrderDishListPagerAdapter(Context context, List<DishPageInfo> dataSet) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mDataSet = dataSet;
    }

    @Override
    public int getCount() {
//        return (mDataSet.size() + getPageSize() - 1) / getPageSize();
        return mDataSet.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GridView gridView = (GridView) mLayoutInflater.inflate(R.layout.order_dish_list_page, container, false);
        gridView.setId(position);
        gridView.setNumColumns(getNumColumns());
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    return true;
                }

                return false;
            }
        });
//        final List<DishVo> subDataSet = getSubDataSet(position);
        final List<DishVo> subDataSet = getItem(position).listDishVos;
        gridView.setAdapter(getAdapter(subDataSet));
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DishVo item = subDataSet.get(arg2);
                if (item == null) {
                    return;
                }

                if (item.isCombo()) {

                    if (!isItemClicked()) {
                        doItemTouch(item);
                        INTERVALTIME = 1000L;
                    }
                } else {

                    if (!isItemClicked()) {
                        doItemTouch(item);
                        INTERVALTIME = 0L;
                    }
                }

            }
        });
        gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DishVo item = subDataSet.get(arg2);
                if (item != null) {
                    doItemLongClick(item);
                }
                return true;
            }
        });
        container.addView(gridView);

        return gridView;
    }


    public void updateItemView(GridView gridView, String dishUuid) {
        if (gridView != null) {
            OrderDishAdapter mOrderDishAdapter = (OrderDishAdapter) gridView.getAdapter();
            List<DishVo> dishVos = mOrderDishAdapter.getDishList();
            if (Utils.isNotEmpty(dishVos)) {
                int size = dishVos.size();
                for (int i = 0; i < size; i++) {
                    DishVo dishVo = dishVos.get(i);
                    if (dishVo != null && dishVo.isSameSeries(dishUuid)) {
                        mOrderDishAdapter.updateView(i, gridView);
                        break;
                    }
                }
            }
        }
    }


    public void updateItemView(GridView gridView, List<String> dishUuids) {
        for (String dishUuid : dishUuids) {
            updateItemView(gridView, dishUuid);
        }
    }


    public void updateItemView(GridView gridView, Map<String, DishShop> dishShopMap) {
        if (gridView != null) {
            OrderDishAdapter orderDishAdapter = (OrderDishAdapter) gridView.getAdapter();
            if (orderDishAdapter != null && orderDishAdapter.getDishList() != null) {
                int size = orderDishAdapter.getDishList().size();
                for (int i = 0; i < size; i++) {
                    boolean isChange = false;
                    DishVo dishVo = orderDishAdapter.getDishList().get(i);
                    if (dishVo != null && dishShopMap != null && dishShopMap.containsKey(dishVo.getSkuUuid())) {
                        dishVo.getDishShop().setResidueTotal(dishShopMap.get(dishVo.getSkuUuid()).getResidueTotal());
                        dishVo.getDishShop().setClearStatus(dishShopMap.get(dishVo.getSkuUuid()).getClearStatus());
                        isChange = true;
                    }
                    if (dishVo != null && dishShopMap != null && dishVo.getOtherDishs() != null && dishVo.getOtherDishs().size() > 0) {
                        Map<String, DishShop> voDishMap = dishVo.getOtherDishs();
                        for (String key : voDishMap.keySet()) {
                            if (dishShopMap.containsKey(key)) {
                                isChange = true;
                                voDishMap.get(key).setResidueTotal(dishShopMap.get(dishVo.getSkuUuid()).getResidueTotal());
                                voDishMap.get(key).setClearStatus(dishShopMap.get(dishVo.getSkuUuid()).getClearStatus());
                            }
                        }
                    }
                    if (isChange) orderDishAdapter.updateView(i, gridView);
                }
            }
        }
    }


    public void updateItemInventory(GridView gridView, Map<String, InventoryInfo> inventoryInfoMap) {
        if (gridView != null) {
            OrderDishAdapter orderDishAdapter = (OrderDishAdapter) gridView.getAdapter();
            int size = orderDishAdapter.getDishList().size();
            for (int i = 0; i < size; i++) {
                DishVo dishVo = orderDishAdapter.getDishList().get(i);
                if (dishVo != null && inventoryInfoMap != null && inventoryInfoMap.containsKey(dishVo.getSkuUuid())) {
                    dishVo.setInventoryNum(inventoryInfoMap.get(dishVo.getSkuUuid()).getInventoryQty());
                    orderDishAdapter.updateView(i, gridView);
                }
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public DishPageInfo getItem(int position){
        if(mDataSet!=null && mDataSet.size()>position){
            return mDataSet.get(position);
        }
        return null;
    }


    public void setDataSet(List<DishPageInfo> dataSet) {
        mDataSet.clear();
        if (dataSet != null) {
            mDataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
//        DishManager.clearDishToEnd(mDataSet);
        super.notifyDataSetChanged();
    }


//    private List<DishVo> getSubDataSet(int position) {
//        int start = position * getPageSize();
//        int end = Math.min((position + 1) * getPageSize(), mDataSet.size());
//        List<DishVo> list = new ArrayList<DishVo>(mDataSet.subList(start, end));
//
//        int size = list.size();
//        if (size < getPageSize()) {
//            for (int i = size; i < getPageSize(); i++) {
//                list.add(null);
//            }
//        }
//
//        return list;
//    }

    private boolean isItemClicked() {

        long current = System.currentTimeMillis();
        long timeD = current - mLastItemClickTime;
        if (INTERVALTIME != 0 && 0 < timeD && timeD <= INTERVALTIME) {
            return true;
        }
        mLastItemClickTime = current;
        return false;
    }

    protected ListAdapter getAdapter(List<DishVo> subDataSet) {

        OrderDishAdapter orderDishAdapter = new OrderDishAdapter(mContext, subDataSet, getNumColumns());
        orderDishAdapter.setEditMode(this.isEditMode);
        orderDishAdapter.setDishCardBg(dishCardType);
        orderDishAdapter.setHidClearNumber(this.isHidClearNumber);
        if (getGridHeight() > 0 && getNumRows() > 0) {
            orderDishAdapter.setItemHeight(getGridHeight() / getNumRows());
        }
        return orderDishAdapter;
    }

    public void setEditMode(boolean isEdit) {
        this.isEditMode = isEdit;
    }

    protected boolean isEditMode() {
        return isEditMode;
    }

    public void setDishCardType(int dishCardType) {
        this.dishCardType = dishCardType;
    }

    protected boolean isHidClearNumber() {
        return isHidClearNumber;
    }

    public void setHidClearNumber(boolean hidClearNumber) {
        isHidClearNumber = hidClearNumber;
    }

    public void setGridHeight(int gridHeight) {
        mGridHeight = gridHeight;
    }

    public int getGridHeight() {
        return mGridHeight;
    }


    protected int getNumColumns() {
        return SettingManager.getSettings(IPanelItemSettings.class).getOrderPageColumns();
    }

    protected int getNumRows() {
        return 5;
    }


    public int getPageSize() {
        return getNumColumns() * getNumRows();
    }


    public abstract void doItemTouch(DishVo dishVo);

    public abstract void doItemLongClick(DishVo dishVo);

}
