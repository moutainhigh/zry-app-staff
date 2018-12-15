package com.zhongmei.bty.dinner.Listener;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;

/**
 * @Date： 17/6/7
 * @Description:
 * @Version: 1.0
 */
public interface BrandTypeListener {

    void onBrandTypeChange(DishBrandType dishBrandType, String carteUuid, boolean single);

    void onSearchClick();
}
