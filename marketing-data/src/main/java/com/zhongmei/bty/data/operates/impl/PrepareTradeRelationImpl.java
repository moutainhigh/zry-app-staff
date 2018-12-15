package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.basemodule.commonbusiness.operates.PrepareTradeRelationDal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PrepareTradeRelationImpl extends AbstractOpeartesImpl implements
        PrepareTradeRelationDal {


    public PrepareTradeRelationImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<PrepareTradeRelation> findList() throws Exception {

        return findList(null);
    }

    @Override
    public List<PrepareTradeRelation> findBookingList() throws Exception {

        return findList(TYPE_VALUE_BOOKING);
    }

    @Override
    public List<PrepareTradeRelation> findLineList() throws Exception {
        return findList(TYPE_VALUE_LINE);
    }

    @Override
    public Map<Long, PrepareTradeRelation> findBookingMap() throws Exception {
        return findMap(TYPE_VALUE_BOOKING);
    }

    @Override
    public Map<Long, PrepareTradeRelation> findLineMap() throws Exception {
        return findMap(TYPE_VALUE_LINE);
    }

    @Override
    public Map<Long, PrepareTradeRelation> findMap() throws Exception {

        return findMap(null);
    }

    private List<PrepareTradeRelation> findList(String type) throws Exception {

        List<PrepareTradeRelation> list = new ArrayList<PrepareTradeRelation>();

        final DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<PrepareTradeRelation, String> dao = helper
                    .getDao(PrepareTradeRelation.class);
            QueryBuilder<PrepareTradeRelation, String> qb = dao.queryBuilder();

            if (null == type) {
                list = qb.query();
            } else {
                list = qb.where().eq(PrepareTradeRelation.$.type, type).query();
            }

        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return list;
    }

    /**
     * 用relatedId作为key的结果
     *
     * @param type
     * @return
     * @throws Exception
     */
    private Map<Long, PrepareTradeRelation> findMap(String type)
            throws Exception {
        Map<Long, PrepareTradeRelation> map = new TreeMap<Long, PrepareTradeRelation>();

        List<PrepareTradeRelation> list = findList(type);

        if (null != list && !list.isEmpty()) {
            for (PrepareTradeRelation ptr : list) {
                if (null != ptr && null != ptr.getRelatedId()) {
                    map.put(ptr.getRelatedId(), ptr);
                }
            }
        }
        return map;
    }

    /**
     * 用tradeid作为key的结果
     *
     * @param type(排队或者预定) 为null时查询所有数据
     * @return
     * @throws Exception
     */
    public Map<Long, PrepareTradeRelation> findMapByTradeId(String type)
            throws Exception {
        Map<Long, PrepareTradeRelation> map = new TreeMap<Long, PrepareTradeRelation>();

        List<PrepareTradeRelation> list = findList(type);

        if (null != list && !list.isEmpty()) {
            for (PrepareTradeRelation ptr : list) {
                if (null != ptr && null != ptr.getTradeId()) {
                    map.put(ptr.getTradeId(), ptr);
                }
            }
        }
        return map;
    }

}
