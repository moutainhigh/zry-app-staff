package com.zhongmei.bty.basemodule.discount.operates;

import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;



public interface ExtraChargeDal extends IOperates {


    List<ExtraCharge> queryExtraChargeRules(boolean isQueryOrder) throws Exception;


    ExtraCharge queryExtraChargeChf() throws Exception;


    ExtraCharge queryExtraById(Long extraId) throws Exception;

}
