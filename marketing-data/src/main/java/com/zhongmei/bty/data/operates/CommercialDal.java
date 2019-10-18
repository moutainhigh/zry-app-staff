package com.zhongmei.bty.data.operates;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;


public interface CommercialDal extends IOperates {


    public List<Long> queryAllShopIdenty();
}
