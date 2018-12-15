package com.zhongmei.bty.dinner.shopcart.adapter;

import android.util.Log;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 封装多批打印数据, 根据数据的打印流水自动分组并按打印流水降序排列
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class ShopCartDataModel {

    private static final String TAG = ShopCartDataModel.class.getSimpleName();

    private Map<String, ShopCartDataVo> baseData;

    public ShopCartDataModel() {
        baseData = new TreeMap<String, ShopCartDataVo>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                try {
                    // 如果有空值，直接返回0
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

    /**
     * @Description: 往map 里面添加数据
     * @Param @param key 打印流水作键值
     * @Param @param dataItem 购物车菜品
     * @Return void 返回类型
     */
    public void addData(String key, IShopcartItem dataItem) {
        ShopCartDataVo dataMode = baseData.get(key);
        if (dataMode == null) {// 如果没有同流水号的分组，新建分组
            dataMode = new ShopCartDataVo(key);
            dataMode.addData(dataItem);
            baseData.put(key, dataMode);// 根据key做降序排列
        } else {// 如果有同流水号的分组，直接在尾部添加数据
            dataMode.addData(dataItem);
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

