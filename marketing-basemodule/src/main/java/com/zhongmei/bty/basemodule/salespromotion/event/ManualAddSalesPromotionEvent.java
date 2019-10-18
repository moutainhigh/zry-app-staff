package com.zhongmei.bty.basemodule.salespromotion.event;

import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;


public class ManualAddSalesPromotionEvent {
    private boolean mEnterManualAddSalesPromotionMode;    private SalesPromotionRuleVo mSalesPromotionRuleVo;
    public ManualAddSalesPromotionEvent(boolean enterManualAddSalesPromotionMode) {
        this.mEnterManualAddSalesPromotionMode = enterManualAddSalesPromotionMode;
    }

    public boolean isEnterManualAddSalesPromotionMode() {
        return mEnterManualAddSalesPromotionMode;
    }

    public void setEnterManualAddSalesPromotionMode(boolean enterManualAddSalesPromotionMode) {
        this.mEnterManualAddSalesPromotionMode = enterManualAddSalesPromotionMode;
    }

    public SalesPromotionRuleVo getSalesPromotionRuleVo() {
        return mSalesPromotionRuleVo;
    }

    public void setSalesPromotionRuleVo(SalesPromotionRuleVo salesPromotionRuleVo) {
        this.mSalesPromotionRuleVo = salesPromotionRuleVo;
    }
}
