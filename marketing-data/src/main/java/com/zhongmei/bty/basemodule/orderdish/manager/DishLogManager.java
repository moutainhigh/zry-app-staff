package com.zhongmei.bty.basemodule.orderdish.manager;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.log.RLog;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;

import java.util.List;

/**
 * 负责DishLog的日志添加,查询
 * Created by demo on 2018/12/15
 */
public class DishLogManager {

    public static void queryDishFromDb() {

        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                Dao<DishShop, Long> dao;
                DatabaseHelper helper = DBHelperManager.getHelper();
                try {
                    dao = helper.getDao(DishShop.class);
                    QueryBuilder<DishShop, Long> qb = dao.queryBuilder();
                    qb.selectColumns(DishShop.$.id,
                            DishShop.$.statusFlag,
                            DishShop.$.name);
                    List<DishShop> dishShopList = qb.where().eq(DishShop.$.enabledFlag, Bool.YES)
                            .and().ne(DishShop.$.type, DishType.CARD).query();
                    RLog.i(RLog.DISH_KEY_TAG, "## 查询数据库的商品中类的个数为: " + dishShopList.size() + " position : DishLogManager -> queryDishFromDb()");
                    RLog.i(RLog.DISH_KEY_TAG, "## 查询数据库的商品中类结果为: " + dishShopList + " position : DishLogManager -> queryDishFromDb()");
                } catch (Exception e) {
                    RLog.e(RLog.DISH_KEY_TAG, "## 查询本地数据库的商品中类出现异常" + " position : DishLogManager -> queryDishFromDb()");
                    e.printStackTrace();
                }
            }
        });

    }

    public static void queryDishBrandTypeFromDb() {

        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                Dao<DishBrandType, Integer> dao;
                DatabaseHelper helper = DBHelperManager.getHelper();
                try {
                    dao = helper.getDao(DishBrandType.class);
                    List<DishBrandType> list = dao.queryBuilder().where().query();
                    RLog.i(RLog.DISH_KEY_TAG, "## 查询数据库的商品中类的个数为: " + list.size() + " position : DishLogManager -> queryDishBrandTypeFromDb()");
                    RLog.i(RLog.DISH_KEY_TAG, "## 查询数据库的商品中类结果为: " + list + " position : DishLogManager -> queryDishBrandTypeFromDb()");
                } catch (Exception e) {
                    RLog.e(RLog.DISH_KEY_TAG, "## 查询本地数据库的商品中类出现异常" + " position : DishLogManager -> queryDishBrandTypeFromDb()");
                    e.printStackTrace();
                } finally {
                    DBHelperManager.releaseHelper(helper);
                }
            }
        });

    }


}
