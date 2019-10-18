package com.zhongmei.bty.dinner.Listener;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;


public interface BrandTypeListener {

    void onBrandTypeChange(DishBrandType dishBrandType, String carteUuid, boolean single);

    void onSearchClick();
}
