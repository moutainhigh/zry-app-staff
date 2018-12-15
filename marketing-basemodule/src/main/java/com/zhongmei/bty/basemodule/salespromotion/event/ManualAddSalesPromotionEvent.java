package com.zhongmei.bty.basemodule.salespromotion.event;

import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;

/**
 * 手动添加促销活动模式事件
 */
public class ManualAddSalesPromotionEvent {
    private boolean mEnterManualAddSalesPromotionMode;//true为进入手动添加促销活动模式，false为退出手动添加促销活动模式
    private SalesPromotionRuleVo mSalesPromotionRuleVo;//进入手动添加促销活动模式时不为null，退出手动添加促销活动模式为null

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
