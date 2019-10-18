package com.zhongmei.bty.basemodule.trade.enums;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.context.base.BaseApplication;



public enum OperateType {

    MOVE_DISH(BaseApplication.sInstance.getResources().getString(R.string.dinner_movedish)),
    COPY_DISH(BaseApplication.sInstance.getResources().getString(R.string.dinner_copydish)),
    TRANSFER_TABLE(BaseApplication.sInstance.getResources().getString(R.string.dinner_change_table)),
    MARGE_TRADE(BaseApplication.sInstance.getResources().getString(R.string.dinner_mix_trade));

    private String typeName;

    private OperateType(String typeName) {
        this.typeName = typeName;
    }

    public String desc() {
        return typeName;
    }

}
