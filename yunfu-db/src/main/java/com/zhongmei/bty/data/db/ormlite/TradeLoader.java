package com.zhongmei.bty.data.db.ormlite;

import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.net.Uri;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.yunfu.orm.DataBaseUtils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;

/**
 * OrmliteLoader使用示例，注意：仅仅是使用示例，勿直接用于项目中！！！
 */
public class TradeLoader extends OrmliteLoader<List<Trade>> {

    protected TradeLoader(Context context) {
        super(context, Uri.parse(DataBaseUtils.getUriHeader() + DBHelperManager.getTableName(Trade.class)));
    }

    @Override
    protected List<Trade> query(DatabaseHelper databaseHelper) throws SQLException {
        // 查出所有未处理的销货单
        Dao<Trade, String> dao = databaseHelper.getDao(Trade.class);
        return dao.queryBuilder()
                .where().eq(Trade.$.tradeType, TradeType.SELL)
                .and().eq(Trade.$.tradeStatus, TradeStatus.UNPROCESSED)
                .query();
    }

}
