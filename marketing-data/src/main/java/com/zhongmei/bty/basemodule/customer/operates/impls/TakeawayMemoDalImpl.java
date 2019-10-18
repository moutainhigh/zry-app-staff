package com.zhongmei.bty.basemodule.customer.operates.impls;

import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.basemodule.customer.operates.TakeawayMemoDal;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.List;


public class TakeawayMemoDalImpl extends AbstractOpeartesImpl implements TakeawayMemoDal {

    public TakeawayMemoDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    @Override
    public List<TakeawayMemo> getDataList() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(TakeawayMemo.class)
                    .queryBuilder()
                    .where()
                    .eq(TakeawayMemo.$.status, TakeawayMemo.RECORD_STATUS_UNDELETE)
                    .and()
                    .isNotNull(TakeawayMemo.$.memoContent)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
