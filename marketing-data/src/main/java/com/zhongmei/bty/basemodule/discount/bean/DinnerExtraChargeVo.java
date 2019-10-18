package com.zhongmei.bty.basemodule.discount.bean;

import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;


public class DinnerExtraChargeVo {
    ExtraCharge extrageCharge;

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


    public boolean isServiceCharge() {
        return extrageCharge.getCode().equalsIgnoreCase(ExtraManager.SERVICE_CONSUM);
    }
}
