package com.zhongmei.bty.basemodule.inventory.utils;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InventoryCacheUtil {

        public static final int SWITCH_CLOSE = 1;

        public static final int SWITCH_OPEN = 2;

    private static InventoryCacheUtil mInstance;

        private int saleNumOpen;

        private int mSaleSwitch;

        private int mAutoClearSwitch;

        private String mLastUpdateTime;

        private Map<String, InventoryInfo> mData = new HashMap<String, InventoryInfo>();

    private Map<Integer, InventoryDataChangeListener> mArrayListener = new HashMap<>();


    public static InventoryCacheUtil getInstance() {
        synchronized (InventoryCacheUtil.class) {
            if (mInstance == null) {
                mInstance = new InventoryCacheUtil();
            }
        }

        return mInstance;
    }


    public boolean getSaleNumOpenSwitch() {
        if (saleNumOpen == SWITCH_OPEN) {
            return true;
        }
        return false;
    }


    public void setSaleNumOpenSwitch(int saleNumOpen) {
        this.saleNumOpen = saleNumOpen;
    }


    public void setSaleSwitch(int value) {
        mSaleSwitch = value;
        for (int key : mArrayListener.keySet()) {
            mArrayListener.get(key).dataChange(null);
        }
    }


    public boolean getSaleSwitch() {
        if (mSaleSwitch == SWITCH_OPEN) {            return true;
        }
        return false;
    }


    public void setAutoClearSwitch(int value) {
        mAutoClearSwitch = value;
    }


    public boolean getAutoClearSwitch() {
        if (mAutoClearSwitch == SWITCH_OPEN) {
            return true;
        }
        return false;
    }


    public void setUpdateTime(String time) {
        mLastUpdateTime = time;
    }


    public String getUpdateTime() {
        return mLastUpdateTime;
    }


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


    public Map<String, InventoryInfo> getAllInventoryData() {
        if (mData != null) {
            return mData;
        }
        return null;
    }


    public InventoryInfo getInventoryNumByDishUuid(String uuid) {
        if (mData != null && !TextUtils.isEmpty(uuid)) {
            if (mData.containsKey(uuid)) {
                return mData.get(uuid);
            }
        }
        return null;
    }


    public void clear() {
        if (mData != null) {
            mData.clear();
        }
    }


    public void registerListener(int listenerTag, InventoryDataChangeListener mInventoryDataChangeListener) {
        mArrayListener.put(listenerTag, mInventoryDataChangeListener);

    }


    public void unRegisterListener(int listenerTag) {
        mArrayListener.remove(listenerTag);
    }


    public interface InventoryDataChangeListener {
        void dataChange(List<InventoryInfo> data);
    }

}
