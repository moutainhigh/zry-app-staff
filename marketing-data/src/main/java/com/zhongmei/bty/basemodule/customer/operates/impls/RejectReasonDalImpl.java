package com.zhongmei.bty.basemodule.customer.operates.impls;

import com.zhongmei.bty.basemodule.customer.entity.RejectReason;
import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.basemodule.customer.operates.RejectReasonDal;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;


import java.util.List;

/**
 * Desc
 *
 * @created 2017/9/29
 */
public class RejectReasonDalImpl extends AbstractOpeartesImpl implements RejectReasonDal {

    public RejectReasonDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    @Override
    public List<RejectReason> getDataList(int type) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            return helper.getDao(RejectReason.class)
                    .queryBuilder()
                    .where()
                    .eq(RejectReason.$.status, TakeawayMemo.RECORD_STATUS_UNDELETE)
                    .and()
                    .eq(RejectReason.$.type, type)
                    .query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
