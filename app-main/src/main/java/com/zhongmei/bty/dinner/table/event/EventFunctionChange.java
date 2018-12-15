package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.dinner.table.bean.FunctionBean;

/**
 *
 */
public class EventFunctionChange {

    public FunctionBean funcationBean;
    public boolean isCheck;
    public int checkNum;
    public int totalNum;

    public EventFunctionChange() {
    }

    public EventFunctionChange(FunctionBean functionBean, boolean isCheck, int checkNum, int totalNum) {
        this.funcationBean = functionBean;
        this.isCheck = isCheck;
        this.checkNum = checkNum;
        this.totalNum = totalNum;
    }

}
