package com.zhongmei.bty.dinner.action;

/**
 * @date:2016年3月31日下午5:13:27
 */
public class ActionDinnerBatchFree {

    private boolean isBatchCoercionModel; //是否强制开启折扣，不受后台折扣限制

    public ActionDinnerBatchFree(boolean isBatchCoercionModel) {
        this.isBatchCoercionModel = isBatchCoercionModel;
    }

    public boolean isBatchCoercionModel() {
        return isBatchCoercionModel;
    }
}
