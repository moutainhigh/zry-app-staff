package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItem;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.math.BigDecimal;
import java.util.List;

/**
 * Kds划菜、取消划菜
 *
 * @created 2017/6/9
 */
public class KdsTradeDalImpl extends AbstractOpeartesImpl implements KdsTradeDal {

    public KdsTradeDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public long getTradeItem(Long id) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<KdsTradeItem, Long> tradeDao = helper.getDao(KdsTradeItem.class);
            KdsTradeItem tradeItem = tradeDao.queryForId(id);
            if (tradeItem != null) {
                return tradeItem.dishServerChangeTime;
            }
            return 0;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public BigDecimal getDishBatservingCount(long tradeItemId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<KdsTradeItemPart, Long> tradeDao = helper.getDao(KdsTradeItemPart.class);
            List<KdsTradeItemPart> tradeItemPartList = tradeDao.queryBuilder()
                    .where()
                    .eq(KdsTradeItemPart.$.tradeItemId, tradeItemId)
                    .query();
            BigDecimal qty = BigDecimal.ZERO;
            for (KdsTradeItemPart itemPart : tradeItemPartList) {
                qty.add(itemPart.quantity);
            }
            return qty;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<KdsTradeItemPart> getTradeItem(List<Long> tradeItemIds) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<KdsTradeItemPart, Long> tradeDao = helper.getDao(KdsTradeItemPart.class);
            List<KdsTradeItemPart> tradeItemPartList = tradeDao.queryBuilder()
                    .where()
                    .eq(KdsTradeItemPart.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .in(KdsTradeItemPart.$.tradeItemId, tradeItemIds)
                    .query();
            return tradeItemPartList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
