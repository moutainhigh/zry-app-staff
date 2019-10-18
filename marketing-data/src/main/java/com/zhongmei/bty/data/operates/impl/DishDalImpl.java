package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.data.operates.DishDal;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.http.OpsRequest.SaveDatabaseResponseProcessor;
import com.zhongmei.bty.data.operates.message.content.DishClearStatusReq;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class DishDalImpl extends AbstractOpeartesImpl implements DishDal {

    public DishDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<UserDefineSku> listUserDefineSku(String skuUuid, long limit) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<TradeItem, String> dao = helper.getDao(TradeItem.class);
            QueryBuilder<TradeItem, String> qb = dao.queryBuilder();
            qb.selectColumns(TradeItem.$.skuName, TradeItem.$.price, TradeItem.$.serverCreateTime);
            qb.where().eq(TradeItem.$.skuUuid, skuUuid).and().eq(TradeItem.$.isChangePrice, Bool.YES);
            qb.groupBy(TradeItem.$.skuName).groupBy(TradeItem.$.price);
            qb.orderBy(TradeItem.$.serverCreateTime, false);
            qb.limit(limit);

            List<UserDefineSku> skuList = new ArrayList<UserDefineSku>();
            List<TradeItem> itemList = qb.query();
            for (TradeItem item : itemList) {
                UserDefineSku sku = new UserDefineSku();
                sku.setName(item.getDishName());
                sku.setPrice(item.getPrice());
                skuList.add(sku);
            }
            return skuList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public DishBrandType findDishBrandType(DishShop dishShop) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<DishBrandType, Long> dao = helper.getDao(DishBrandType.class);
            QueryBuilder<DishBrandType, Long> qb = dao.queryBuilder();
            qb.where().eq(DishBrandType.$.id, dishShop.getDishTypeId());
            DishBrandType dishBrandType = qb.queryForFirst();
            return dishBrandType;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void clearStatus(ClearStatus newValue, List<String> dishUuids, ResponseListener<Boolean> listener) {

    }


    private static class ClearStatusRespProcessor extends SaveDatabaseResponseProcessor<Boolean> {

        private final ClearStatus newValue;
        private final List<String> dishUuids;

        ClearStatusRespProcessor(ClearStatus newValue, List<String> dishUuids) {
            this.newValue = newValue;
            this.dishUuids = dishUuids;
        }

        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final Boolean resp) {
            if (resp) {
                return new Callable<Void>() {

                    @Override
                    public Void call() throws Exception {
                        Dao<DishShop, Long> dao = helper.getDao(DishShop.class);
                        for (String uuid : dishUuids) {
                            UpdateBuilder<DishShop, Long> up = dao.updateBuilder();
                            up.updateColumnValue(DishShop.$.clearStatus, newValue);
                            up.where().eq(DishShop.$.uuid, uuid);
                            up.update();
                        }
                        helper.getChangeSupportable().addChange(DishShop.class);
                        return null;
                    }
                };
            }
            return null;
        }

    }

    @Override
    public DishShop findDishShop(SaleType saleType) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<DishShop, Long> dao = helper.getDao(DishShop.class);
            QueryBuilder<DishShop, Long> qb = dao.queryBuilder();
            qb.where().eq(DishShop.$.enabledFlag, Bool.YES)
                    .and().ne(DishShop.$.type, DishType.CARD)
                    .and().eq(DishShop.$.saleType, saleType)
                    .and().eq(DishShop.$.isChangePrice, Bool.YES);
            return qb.orderBy(DishShop.$.serverUpdateTime, false)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
