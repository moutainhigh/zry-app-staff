package com.zhongmei.bty.basemodule.inventory.utils;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date： 2017/2/28
 * @Description:缓存库存开关项、库存数量
 * @Version: 1.0
 */
public class InventoryCacheUtil {

    //关闭
    public static final int SWITCH_CLOSE = 1;

    //开启
    public static final int SWITCH_OPEN = 2;

    private static InventoryCacheUtil mInstance;

    //可售数展示开关：1、关闭 2、开启
    private int saleNumOpen;

    //1、商品每日售卖量 2、商品实际库存数
    private int mSaleSwitch;

    //自动估清开关：1、关闭 2、开启
    private int mAutoClearSwitch;

    //最新编辑时间
    private String mLastUpdateTime;

    //库存数据
    private Map<String, InventoryInfo> mData = new HashMap<String, InventoryInfo>();

    private Map<Integer, InventoryDataChangeListener> mArrayListener = new HashMap<>();

    /**
     * 获得单例
     */
    public static InventoryCacheUtil getInstance() {
        synchronized (InventoryCacheUtil.class) {
            if (mInstance == null) {
                mInstance = new InventoryCacheUtil();
            }
        }

        return mInstance;
    }

    /**
     * 获取是否展示库存信息的开关
     *
     * @return
     */
    public boolean getSaleNumOpenSwitch() {
        if (saleNumOpen == SWITCH_OPEN) {
            return true;
        }
        return false;
    }

    /**
     * 设置是否展示库存信息的开关
     *
     * @param saleNumOpen
     */
    public void setSaleNumOpenSwitch(int saleNumOpen) {
        this.saleNumOpen = saleNumOpen;
    }

    /**
     * 保存商品可售数开关项
     *
     * @param value
     */
    public void setSaleSwitch(int value) {
        mSaleSwitch = value;
        for (int key : mArrayListener.keySet()) {
            mArrayListener.get(key).dataChange(null);
        }
    }

    /**
     * 获取商品可售数开关
     *
     * @return true开启实时库存 false开启每日售卖量
     */
    public boolean getSaleSwitch() {
        if (mSaleSwitch == SWITCH_OPEN) {//每日售量未开启
            return true;
        }
        return false;
    }

    /**
     * 设置自动沽清开关项
     *
     * @param value
     */
    public void setAutoClearSwitch(int value) {
        mAutoClearSwitch = value;
    }

    /**
     * 获取自动沽清开关项
     *
     * @return true 开启 false 关闭
     */
    public boolean getAutoClearSwitch() {
        if (mAutoClearSwitch == SWITCH_OPEN) {
            return true;
        }
        return false;
    }

    /**
     * 设置更新时间
     *
     * @param time
     */
    public void setUpdateTime(String time) {
        mLastUpdateTime = time;
    }

    /**
     * 获取更新时间
     */
    public String getUpdateTime() {
        return mLastUpdateTime;
    }

    /**
     * 设置库存数据
     */
    public void setInventoryData(List<InventoryInfo> data) {
        if (data == null) {
            return;
        }
        if (mData == null) {
            mData = new HashMap<String, InventoryInfo>();
        }
        for (InventoryInfo info : data) {
            mData.put(info.getUuid(), info);
        }
        for (int key : mArrayListener.keySet()) {
            mArrayListener.get(key).dataChange(data);
        }
    }

    /**
     * 获取所有的库存数据
     */
    public Map<String, InventoryInfo> getAllInventoryData() {
        if (mData != null) {
            return mData;
        }
        return null;
    }

    /**
     * 根据菜品UUID返回库存对象
     */
    public InventoryInfo getInventoryNumByDishUuid(String uuid) {
        if (mData != null && !TextUtils.isEmpty(uuid)) {
            if (mData.containsKey(uuid)) {
                return mData.get(uuid);
            }
        }
        return null;
    }

    /**
     * 清除库存数据
     */
    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

    /**
     * 注册库存数据监听
     *
     * @param listenerTag
     * @param mInventoryDataChangeListener
     */
    public void registerListener(int listenerTag, InventoryDataChangeListener mInventoryDataChangeListener) {
        mArrayListener.put(listenerTag, mInventoryDataChangeListener);

    }

    /**
     * 根据监听tag反注册监听
     *
     * @param listenerTag
     */
    public void unRegisterListener(int listenerTag) {
        mArrayListener.remove(listenerTag);
    }

    /**
     * 库存发生改变listener
     */
    public interface InventoryDataChangeListener {
        void dataChange(List<InventoryInfo> data);
    }

}
