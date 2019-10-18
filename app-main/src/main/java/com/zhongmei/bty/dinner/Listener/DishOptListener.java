package com.zhongmei.bty.dinner.Listener;

import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;

import java.util.List;


public interface DishOptListener {

    void onSuccess(PrintOperationOpType type, List<DishDataItem> dataItems);

    void onFail(PrintOperationOpType type, List<DishDataItem> dataItems);
}
