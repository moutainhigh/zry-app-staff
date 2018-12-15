package com.zhongmei.bty.basemodule.database.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.local.PrinterRecord;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.database.operates.PrinterRecordDal;
import com.zhongmei.bty.commonmodule.database.enums.PrintStatesEnum;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */

public class PrinterRecordImpl extends AbstractOpeartesImpl implements PrinterRecordDal {

    private static final String TAG = PrinterRecordImpl.class.getSimpleName();

    public PrinterRecordImpl(ImplContext context) {
        super(context);
    }

    @Override
    public boolean insertPrintRecords(final List<PrinterRecord> printRecords) {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            //更新后的数据插入数据库
            // modify 20170216 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, PrinterRecord.class, printRecords);
                    return null;
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LocalDBManager.releaseHelper();
        }
        return false;
    }

    @Override
    public List<PrinterRecord> queryAllPrintRecord() {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<PrinterRecord, String> dao = helper.getDao(PrinterRecord.class);
            return dao.queryBuilder().orderBy(PrinterRecord.$.clientUpdateTime, false).where()
                    .in(PrinterRecord.$.errorCode,
                            PrintStatesEnum.PRINT_PART_NO_DEVICE.code(), PrintStatesEnum.PRINT_PART_PACKAGE_ERROR.code(),
                            PrintStatesEnum.PRINT_GLOBAL_DATA_ERROR.code(), PrintStatesEnum.PRINT_GLOBAL_TEMPLET_ERROR.code(),
                            PrintStatesEnum.PRINT_GLOBAL_NOGOODS_ERROR.code()).query();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            LocalDBManager.releaseHelper();
        }
        return null;
    }

    @Override
    public boolean deletePrintRecord(String uuid) {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            final Dao<PrinterRecord, String> dao = helper.getDao(PrinterRecord.class);
            DeleteBuilder<PrinterRecord, String> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(PrinterRecord.$.uuid, uuid);
            int deleteCount = deleteBuilder.delete();
            if (deleteCount > 0) {
                helper.getChangeSupportable().addChange(PrinterRecord.class);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            LocalDBManager.releaseHelper();
        }
        return false;
    }
}
