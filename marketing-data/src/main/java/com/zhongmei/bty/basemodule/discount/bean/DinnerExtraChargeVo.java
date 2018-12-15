package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;

/**
 * 附加费传递对象
 *
 * @date:2016年3月10日下午3:21:59
 */
public class DinnerExtraChargeVo {
    ExtraCharge extrageCharge;

    // 是否被选中
    boolean isSelected = false;

    public ExtraCharge getExtrageCharge() {
        return extrageCharge;
    }

    public void setExtrageCharge(ExtraCharge extrageCharge) {
        this.extrageCharge = extrageCharge;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 是否是服务费
     *
     * @return
     */
    public boolean isServiceCharge() {
        return extrageCharge.getCode().equalsIgnoreCase(ExtraManager.SERVICE_CONSUM);
    }
}
