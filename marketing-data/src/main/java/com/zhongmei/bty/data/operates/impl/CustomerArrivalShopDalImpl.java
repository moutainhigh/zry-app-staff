package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.shopmanager.utils.DateTimeUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.data.operates.CustomerArrivalShopDal;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.database.enums.ArriveStatus;
import com.zhongmei.bty.commonmodule.database.enums.ArriveWay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Desc
 *
 * @created 2017/7/27
 */
public class CustomerArrivalShopDalImpl extends AbstractOpeartesImpl implements CustomerArrivalShopDal {

    public CustomerArrivalShopDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    @Override
    public CustomerArrivalShop save(CustomerArrivalShop customerArrivalShop) {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<CustomerArrivalShop, Long> dao = helper.getDao(CustomerArrivalShop.class);
            dao.create(customerArrivalShop);
            return getByUuId(dao, customerArrivalShop.getUuId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LocalDBManager.releaseHelper();
        }
        return customerArrivalShop;//如果出现异常，返回原对象
    }

    @Override
    public CustomerArrivalShop getLastByCustomerId(Long customerId) {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            return helper.getDao(CustomerArrivalShop.class)
                    .queryBuilder().orderBy(CustomerArrivalShop.$.serverCreateTime, false)
                    .where()
                    .eq(CustomerArrivalShop.$.customerId, customerId)
                    .queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LocalDBManager.releaseHelper();
        }
        return null;
    }

    @Override
    public List<CustomerArrivalShop> find() throws Exception {
        return find(null, null, null);
    }

    @Override
    public List<CustomerArrivalShop> find(List<ArriveStatus> arriveStatusList, List<Long> tableIds,
                                          List<ArriveWay> arriveWayList) throws Exception {
        return findData(arriveStatusList, tableIds, arriveWayList, null);
    }

    @Override
    public boolean update(CustomerArrivalShop customer) throws Exception {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            helper.getDao(CustomerArrivalShop.class).update(customer);
        } finally {
            LocalDBManager.releaseHelper();
        }
        return true;
    }

    @Override
    public List<CustomerArrivalShop> findNotOperated() {
        List<CustomerArrivalShop> customerArrivalShopList = null;
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        try {
            customerArrivalShopList = findData(null, null, null, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerArrivalShopList;
    }

    @Override
    public void deleteAll() {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<CustomerArrivalShop, Long> dao = helper.getDao(CustomerArrivalShop.class);
            dao.deleteBuilder().delete();
        } catch (Exception e) {

        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    private CustomerArrivalShop getByUuId(Dao<CustomerArrivalShop, Long> dao, String uuid) {
        try {
            return dao.queryBuilder().orderBy(CustomerArrivalShop.$.serverCreateTime, false)
                    .where().eq(CustomerArrivalShop.$.uuid, uuid)
                    .queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CustomerArrivalShop> findData(List<ArriveStatus> arriveStatusList, List<Long> tableIds,
                                               List<ArriveWay> arriveWayList, List<Integer> OperateStatus) throws Exception {
        DatabaseHelper helper = LocalDBManager.getHelper();
        List<CustomerArrivalShop> customerArrivalShopList;
        try {
            Date date = DateTimeUtil.getOpenTime();
            Dao<CustomerArrivalShop, Long> dao = helper.getDao(CustomerArrivalShop.class);
            Where<CustomerArrivalShop, Long> where = dao.queryBuilder()
                    .orderBy(CustomerArrivalShop.$.serverCreateTime, false).where()
                    .ge(CustomerArrivalShop.$.serverCreateTime, date.getTime());

            //添加状态筛选
            if (Utils.isNotEmpty(arriveStatusList)) {
                where.and().in(CustomerArrivalShop.$.arrivalStatus, arriveStatusList);
            }

            //添加桌台筛选
            if (Utils.isNotEmpty(tableIds)) {
                where.and().in(CustomerArrivalShop.$.tableId, tableIds);
            }

            //添加到点方式筛选
            if (Utils.isNotEmpty(arriveWayList)) {
                where.and().in(CustomerArrivalShop.$.arrivalWay, arriveWayList);
            }
            //添加处理状态筛选
            if (Utils.isNotEmpty(OperateStatus)) {
                where.and().in(CustomerArrivalShop.$.operateStatus, OperateStatus);
            }
            customerArrivalShopList = where.query();
        } finally {
            LocalDBManager.releaseHelper();
        }

        return customerArrivalShopList;
    }
}
