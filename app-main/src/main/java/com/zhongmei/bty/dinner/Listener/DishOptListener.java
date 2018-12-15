package com.zhongmei.bty.dinner.Listener;

import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;

import java.util.List;

/**
 * @Date： 2018/2/8
 * @Description:
 * @Version: 1.0
 */
public interface DishOptListener {

    void onSuccess(PrintOperationOpType type, List<DishDataItem> dataItems);

    void onFail(PrintOperationOpType type, List<DishDataItem> dataItems);
}
