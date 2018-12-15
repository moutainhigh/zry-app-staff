package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemBatch;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;
import com.zhongmei.bty.commonmodule.database.enums.AddItemBatchStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.orderdish.operates.AddItemDal;
import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class AddItemDalImpl extends AbstractOpeartesImpl implements AddItemDal {

    public AddItemDalImpl(ImplContext context) {
        super(context);
    }

    /**
     * 根据桌台ID查询加菜信息
     *
     * @param tableId
     * @return
     */
    @Override
    public List<AddItemVo> queryAdditemsByTableId(long tableId) {
        List<AddItemVo> listAddItemVo = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemBatch, Long> batchDao = helper.getDao(AddItemBatch.class);
            Dao<AddItemRecord, Long> recordDao = helper.getDao(AddItemRecord.class);

//            List<AddItemBatch> itemBatchs=batchDao.queryBuilder().where().eq(AddItemBatch.$.tableId,tableId).query();
            List<AddItemBatch> itemBatchs = batchDao.queryForAll();

            if (itemBatchs == null || itemBatchs.size() <= 0) {
                return null;
            }

            listAddItemVo = new ArrayList<AddItemVo>();

            for (AddItemBatch itemBatch : itemBatchs) {
                List<AddItemRecord> itemRecords = recordDao.queryBuilder().where().eq(AddItemRecord.$.batchId, itemBatch.getId()).query();


                if (itemRecords == null || itemRecords.size() <= 0) {
                    continue;
                }

                AddItemVo addItemVo = new AddItemVo();
                addItemVo.setmAddItemBatch(itemBatch);
                addItemVo.setmAddItemRecords(itemRecords);

                listAddItemVo.add(addItemVo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemVo;
    }

    /**
     * 根据TradeId查询微信加菜信息
     *
     * @param tradeId
     * @return
     */
    @Override
    public List<AddItemVo> queryAdditemsByTradeId(long tradeId) {
        List<AddItemVo> listAddItemVo = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemBatch, Long> batchDao = helper.getDao(AddItemBatch.class);
            Dao<AddItemRecord, Long> recordDao = helper.getDao(AddItemRecord.class);

            List<AddItemBatch> itemBatchs = batchDao.queryBuilder().where().eq(AddItemBatch.$.tradeId, tradeId).query();

            if (itemBatchs == null || itemBatchs.size() <= 0) {
                return null;
            }

            listAddItemVo = new ArrayList<AddItemVo>();

            for (AddItemBatch itemBatch : itemBatchs) {
                List<AddItemRecord> itemRecords = recordDao.queryBuilder().where().eq(AddItemRecord.$.batchId, itemBatch.getId()).query();

                if (itemRecords == null || itemRecords.size() <= 0) {
                    continue;
                }

                AddItemVo addItemVo = new AddItemVo();
                addItemVo.setmAddItemBatch(itemBatch);
                addItemVo.setmAddItemRecords(itemRecords);

                listAddItemVo.add(addItemVo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemVo;
    }

    @Override
    public List<AddItemBatch> queryItemBatchsByTableId(long tableId) {
        List<AddItemBatch> listAddItemBatch = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemBatch, Long> batchDao = helper.getDao(AddItemBatch.class);

            listAddItemBatch = batchDao.queryBuilder().where().eq(AddItemBatch.$.tableId, tableId).query();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemBatch;
    }

    @Override
    public List<AddItemBatch> queryAllItemBatchs() {
        List<AddItemBatch> listAddItemBatch = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemBatch, Long> batchDao = helper.getDao(AddItemBatch.class);

            Calendar cad = Calendar.getInstance();
            cad.setTimeInMillis(DateTimeUtils.getCurrentDayEnd());
            cad.add(Calendar.DAY_OF_MONTH, -3);
            Long timeStamp = cad.getTime().getTime();

            listAddItemBatch = batchDao.queryBuilder().orderBy(AddItemBatch.$.serverCreateTime, false).where().eq(AddItemBatch.$.statusFlag, StatusFlag.VALID).and()
                    .gt(AddItemBatch.$.serverCreateTime, timeStamp).and().eq(AddItemBatch.$.handleStatus, AddItemBatchStatus.HASDEALTRADE).query();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemBatch;
    }

    @Override
    public List<AddItemBatch> queryItemBatchsByTradeId(long tradeId) {
        List<AddItemBatch> listAddItemBatch = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemBatch, Long> batchDao = helper.getDao(AddItemBatch.class);

            listAddItemBatch = batchDao.queryBuilder().where().eq(AddItemBatch.$.tradeId, tradeId).query();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemBatch;
    }

    @Override
    public List<AddItemRecord> queryItemRecordsByBatchId(long batchId) {
        List<AddItemRecord> listAddItemRecord = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemRecord, Long> batchDao = helper.getDao(AddItemRecord.class);

            listAddItemRecord = batchDao.queryBuilder().orderBy(AddItemBatch.$.serverCreateTime, false).where().eq(AddItemRecord.$.batchId, batchId).query();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemRecord;
    }

    @Override
    public List<AddItemRecord> queryAllItemRecords() {
        List<AddItemRecord> listAddItemRecord = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemRecord, Long> batchDao = helper.getDao(AddItemRecord.class);

            listAddItemRecord = batchDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemRecord;
    }

    @Override
    public List<AddItemRecord> queryItemRecordssByTableId(long tableId) {
        List<AddItemRecord> listAddItemRecord = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemRecord, Long> batchDao = helper.getDao(AddItemRecord.class);

            listAddItemRecord = batchDao.queryBuilder().where().eq(AddItemRecord.$.tableId, tableId).query();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemRecord;
    }

    @Override
    public List<AddItemRecord> queryItemRecordsByTradeId(long tradeId) {
        List<AddItemRecord> listAddItemRecord = null;
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<AddItemRecord, Long> batchDao = helper.getDao(AddItemRecord.class);

            listAddItemRecord = batchDao.queryBuilder().where().eq(AddItemRecord.$.tradeId, tradeId).query();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listAddItemRecord;
    }
}
