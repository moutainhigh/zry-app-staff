package com.zhongmei.bty.commonmodule.database.db.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhongmei.yunfu.orm.SQLiteDatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncUuidRecord;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.bty.commonmodule.database.entity.local.ContractOverdue;
import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;
import com.zhongmei.bty.commonmodule.database.entity.local.PayMenuOrder;
import com.zhongmei.bty.commonmodule.database.entity.local.PosSettlementLog;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.bty.commonmodule.database.entity.local.PrinterRecord;
import com.zhongmei.bty.commonmodule.database.entity.local.TimePrintInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * local.db 创建  v7.9添加
 * Created by demo on 2018/12/15
 */

public class LocalDatabaseHelper extends SQLiteDatabaseHelper {
    private static final String TAG = LocalDatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "local.db";
    /**
     * 初始版本号：10010
     * v7.14 -->TimePrintInfo添加trade_id字段
     * 10023-v7.15 -->CustomerArrivalShop到店离店推送
     * 10030-v8.4.2 -->数据库升级时移除AsyncHttpRecord，AsyncHttpRecord删除custom_json、kitchen_json、label_json、添加print_bean_json
     * 20180707-v8.14.0 -->BaiduSyntheticSpeech新增字段queueVoiceType
     */
    private static final int DATABASE_VERSION = 10040;

    public static final List<Class<? extends IEntity<?>>> TABLES;

    static {
        ArrayList<Class<? extends IEntity<?>>> tables = new ArrayList<Class<? extends IEntity<?>>>();

        //本地库
        //异步网络请求记录表(7.4添加)
        tables.add(AsyncHttpRecord.class);
        tables.add(AuthorizedLog.class);
        tables.add(BaiduSyntheticSpeech.class);
        tables.add(ContractOverdue.class);
        tables.add(TimePrintInfo.class);
//        //添加支付菜单顺序记录
        tables.add(PayMenuOrder.class);//20170119
//        // liandiPOS刷卡交易记录
        tables.add(PosTransLog.class);
        tables.add(PosSettlementLog.class);
        //7.12新增打印错误信息记录表
        tables.add(PrinterRecord.class);
        tables.add(CustomerArrivalShop.class);
        tables.add(AsyncUuidRecord.class);

        TABLES = Collections.unmodifiableList(tables);
    }


    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    /**
     * 创建SQLite数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        Log.i(TAG, "Creating database...");

        // 创建使用ORMLite的表
        try {
            for (int i = 0; i < TABLES.size(); i++) {
                TableUtils.createTableIfNotExists(connectionSource, TABLES.get(i));
            }
        } catch (SQLException ex) {
            Log.e(TAG, "Unable to create database", ex);
        }
    }

    /**
     * 更新SQLite数据库
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        Log.i(TAG, "Upgrading database from version " + oldVer + " to " + newVer);
        try {
            for (int i = TABLES.size() - 1; i >= 0; i--) {
                Class<? extends IEntity<?>> classType = TABLES.get(i);
                // 银联POS使用的表不直接删除
                if (classType == PosTransLog.class || classType == PosSettlementLog.class || classType == PayMenuOrder.class) {
                    alterTableStructure(oldVer, newVer, classType);
                } else {
                    TableUtils.dropTable(connectionSource, classType, true);
                }
            }
        } catch (SQLException ex) {
            Log.e(TAG, "Unable to drop table", ex);
        }
        onCreate(sqliteDatabase, connectionSource);
    }

    private void alterTableStructure(int oldVer, int newVer, Class<? extends IEntity<?>> classType) {

    }
}
