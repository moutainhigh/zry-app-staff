package com.zhongmei.bty.basemodule.erp.operates;

import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;

import java.util.List;

public interface ErpCommercialRelationDal extends IOperates {
    /**
     * 查询erp中是否开启熟客功能
     *
     * @return
     * @throws Exception
     */
    boolean findIsShukeInErp() throws Exception;

    ErpCommercialRelation findErpCommercialRelation() throws Exception;

    List<ErpCurrency> queryErpCurrenctList();

    /**
     * 通过国籍id查询国籍
     *
     * @param areaCode 区域code
     * @return
     */
    ErpCurrency queryErpCurrenctByAreaCode(String areaCode);

    /**
     * 根据ID获取ErpCurrency
     *
     * @param currencyId
     * @return
     */
    ErpCurrency getErpCurrency(Long currencyId);

}
