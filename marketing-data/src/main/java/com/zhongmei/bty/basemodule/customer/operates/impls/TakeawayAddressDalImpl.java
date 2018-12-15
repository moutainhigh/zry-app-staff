package com.zhongmei.bty.basemodule.customer.operates.impls;

import com.zhongmei.bty.basemodule.customer.entity.TakeawayAddress;
import com.zhongmei.bty.basemodule.customer.operates.TakeawayAddressDal;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;

/**
 * Desc
 *
 * @created 2017/9/29
 */
public class TakeawayAddressDalImpl extends AbstractOpeartesImpl implements TakeawayAddressDal {

    public TakeawayAddressDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    @Override
    public List<TakeawayAddress> getDataList() throws Exception {
        return null;
    }
}
