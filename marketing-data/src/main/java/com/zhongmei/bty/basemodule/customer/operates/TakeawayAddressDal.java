package com.zhongmei.bty.basemodule.customer.operates;

import com.zhongmei.bty.basemodule.customer.entity.TakeawayAddress;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;


public interface TakeawayAddressDal extends IOperates {

    List<TakeawayAddress> getDataList() throws Exception;
}
