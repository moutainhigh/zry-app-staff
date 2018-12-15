package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public interface CommercialDal extends IOperates {

    /**
     * 获取数据中商户编号数量
     *
     * @return
     */
    public List<Long> queryAllShopIdenty();
}
