package com.zhongmei.bty.basemodule.customer.operates.impls;

import android.util.Log;

import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.bty.basemodule.customer.db.CoupDish;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.bty.basemodule.customer.operates.CouponDal;
import com.zhongmei.yunfu.context.util.Utils;

public class CouponDalImpl extends AbstractOpeartesImpl implements CouponDal {

    private static final String TAG = CouponDalImpl.class.getSimpleName();

    public CouponDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public Coupon findCouponById(Long couponId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<Coupon, Long> dao = helper.getDao(Coupon.class);
            return dao.queryForId(couponId);

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<CoupRule> findCoupRuleById(Long couponId) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CoupRule, Long> ruledao = helper.getDao(CoupRule.class);

            QueryBuilder<CoupRule, Long> qb = ruledao.queryBuilder();

            qb.where().eq(CoupRule.$.couponId, couponId);
            return qb.orderBy(CoupRule.$.id, true).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public CouponVo findCouponVoByCouponInfo(CustomerCouponResp couponInfo) throws Exception {
        if (couponInfo != null) {
            CouponVo vo = new CouponVo();
            vo.setCouponInfo(couponInfo);

            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                Dao<Coupon, Long> couponDao = helper.getDao(Coupon.class);
                vo.setCoupon(couponDao.queryForId(couponInfo.getId()));

//				Dao<CoupRule, Long> ruledao = helper.getDao(CoupRule.class);
//
//				QueryBuilder<CoupRule, Long> qb = ruledao.queryBuilder();
//
//				qb.where().eq(CoupRule.$.couponId, couponInfo.getId());
//
////				vo.setCoupRules(qb.orderBy(CoupRule.$.id, true).query());
            } finally {
                DBHelperManager.releaseHelper(helper);
            }
            return vo;

        } else {

            return null;
        }
    }

    @Override
    public List<CouponVo> findCouponVoListByCouponInfos(List<CustomerCouponResp> coupponInfoList) throws Exception {

        List<CouponVo> list = new ArrayList<CouponVo>();

        if (coupponInfoList != null && !coupponInfoList.isEmpty()) {
            DatabaseHelper helper = DBHelperManager.getHelper();
            try {
                Dao<Coupon, Long> couponDao = helper.getDao(Coupon.class);
                Map<Long, Coupon> couponMap = new HashMap<Long, Coupon>();
                List<Coupon> couponList = couponDao.queryForAll();
                if (Utils.isNotEmpty(couponList)) {
                    for (Coupon coupon : couponList) {
                        couponMap.put(coupon.getId(), coupon);
                    }
                }
                for (CustomerCouponResp info : coupponInfoList) {

                    CouponVo vo = new CouponVo();
                    vo.setCouponInfo(info);
                    vo.setCoupon(couponMap.get(info.getId()));
                    list.add(vo);
                }

            } finally {
                DBHelperManager.releaseHelper(helper);
            }
        }
        return list;
    }

    @Override
    public List<CoupDish> findCoupDishById(Long couponId) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CoupDish, Long> dao = helper.getDao(CoupDish.class);
            QueryBuilder<CoupDish, Long> qb = dao.queryBuilder();
            qb.where().eq(CoupDish.$.couponId, couponId).and().eq(CoupDish.$.isDelete, 0);
            return qb.orderBy(CoupDish.$.id, true).query();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    private List<CoupRule> getcoupRules(DatabaseHelper helper, Long CouponId) {
        try {
            Dao<CoupRule, Long> ruledao = helper.getDao(CoupRule.class);
            QueryBuilder<CoupRule, Long> qb = ruledao.queryBuilder();
            qb.where().eq(CoupRule.$.couponId, CouponId).and().eq(CoupRule.$.isDelete, 0);
            return qb.orderBy(CoupRule.$.id, true).query();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }
}
