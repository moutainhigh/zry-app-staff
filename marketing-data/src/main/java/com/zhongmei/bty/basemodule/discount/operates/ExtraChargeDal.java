package com.zhongmei.bty.basemodule.discount.operates;

import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface ExtraChargeDal extends IOperates {

    /**
     * 获取所有有效的附加费规则
     *
     * @param isQueryOrder 是否查询要加入订单的数据
     * @throws Exception
     * @Title: queryExtraChargeRules
     * @Return List<ExtraCharge> 返回类型
     */
    List<ExtraCharge> queryExtraChargeRules(boolean isQueryOrder) throws Exception;

    /**
     * @Title: queryExtraChargeChf
     * @Description: 查询餐盒费
     * @Param @return
     * @Return ExtraCharge 返回类型
     */
    ExtraCharge queryExtraChargeChf() throws Exception;

    /**
     * @Title: queryExtraById
     * @Description: 通过附加费id查询附加费
     * @Param @return
     * @Return ExtraCharge 返回类型
     */
    ExtraCharge queryExtraById(Long extraId) throws Exception;

}
