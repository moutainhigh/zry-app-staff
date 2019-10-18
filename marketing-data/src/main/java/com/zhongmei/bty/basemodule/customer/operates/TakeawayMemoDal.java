package com.zhongmei.bty.basemodule.customer.operates;

import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;


public interface TakeawayMemoDal extends IOperates {

    List<TakeawayMemo> getDataList() throws Exception;
}
