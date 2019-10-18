package com.zhongmei.bty.basemodule.erp.operates;

import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;

import java.util.List;

public interface ErpCommercialRelationDal extends IOperates {

    boolean findIsShukeInErp() throws Exception;

    ErpCommercialRelation findErpCommercialRelation() throws Exception;

    List<ErpCurrency> queryErpCurrenctList();


    ErpCurrency queryErpCurrenctByAreaCode(String areaCode);


    ErpCurrency getErpCurrency(Long currencyId);

}
