package com.zhongmei.bty.dinner.shopcart.adapter;

import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


public class ShopCartDataModel {

    private static final String TAG = ShopCartDataModel.class.getSimpleName();

    private Map<String, ShopCartDataVo> baseData;

    public ShopCartDataModel() {
        baseData = new TreeMap<String, ShopCartDataVo>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                try {
                                        if (o1 == null || o2 == null) {
                        return 0;
                    }
                    Long o2L = Long.parseLong(o2);
                    Long o1L = Long.parseLong(o1);

                    return o2L.compareTo(o1L);

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    return 0;
                }
            }
        });
    }


    public void addData(String key, IShopcartItem dataItem) {
        ShopCartDataVo dataMode = baseData.get(key);
        if (dataMode == null) {            dataMode = new ShopCartDataVo(key);
            dataMode.addData(dataItem);
            baseData.put(key, dataMode);        } else {            dataMode.addData(dataItem);
        }
    }

    public Map<String, ShopCartDataVo> getData() {
        return baseData;
    }

    public boolean isEmpty() {
        if (baseData == null) {
            return true;
        } else {
            return baseData.isEmpty();
        }
    }
}

