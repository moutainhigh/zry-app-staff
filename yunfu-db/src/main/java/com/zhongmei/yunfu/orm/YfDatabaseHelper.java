package com.zhongmei.yunfu.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.context.Constant;
import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.yunfu.db.entity.AuthPermission;
import com.zhongmei.yunfu.db.entity.AuthRole;
import com.zhongmei.yunfu.db.entity.AuthRolePermission;
import com.zhongmei.yunfu.db.entity.AuthUser;
import com.zhongmei.yunfu.db.entity.Brand;
import com.zhongmei.yunfu.db.entity.Commercial;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;
import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.yunfu.db.entity.OpenTime;
import com.zhongmei.yunfu.db.entity.SyncMark;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingSetting;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerRightsConfig;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerThreshold;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRule;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRuleDetail;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.db.entity.crm.CustomerLevel;
import com.zhongmei.yunfu.db.entity.crm.CustomerSaveRule;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRule;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.yunfu.db.entity.discount.MarketDynamicCondition;
import com.zhongmei.yunfu.db.entity.discount.MarketPlan;
import com.zhongmei.yunfu.db.entity.discount.MarketPlanCommercialRel;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.db.entity.dish.DishCyclePeriod;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishSetmealGroup;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeScene;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class YfDatabaseHelper extends SQLiteDatabaseHelper {

    private static final String TAG = YfDatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "yun_fu.db";

    public static final List<Class<? extends IEntity<?>>> TABLES;

    static {
        ArrayList<Class<? extends IEntity<?>>> tables = new ArrayList<>();

        tables.add(SyncMark.class);
        tables.add(Brand.class);
        tables.add(Commercial.class);
        tables.add(AuthUser.class);
        tables.add(AuthPermission.class);
        tables.add(AuthRole.class);
        tables.add(AuthRolePermission.class);
        tables.add(Booking.class);
        tables.add(BookingTable.class);
        tables.add(BookingTradeItem.class);
        //tables.add(BookingTradeItemUser.class);
        tables.add(BookingSetting.class);
        //tables.add(CommercialCustomSettings.class);

        //待处理的表
        tables.add(Trade.class);
        tables.add(TradeExtra.class);
        tables.add(TradeItem.class);
        tables.add(TradeItemOperation.class);
//        tables.add(TradeItemUser.class);
        tables.add(TradeTable.class);
        tables.add(TradeReturnInfo.class);
        tables.add(MobilePaySetting.class);
        tables.add(Payment.class);
        tables.add(PaymentItem.class);
        tables.add(PaymentItemExtra.class);
        //tables.add(PrepareTradeRelation.class);
        tables.add(PaymentModeShop.class);
        tables.add(PaymentModeScene.class);
        tables.add(MarketPlan.class);
        tables.add(MarketDynamicCondition.class);
        tables.add(MarketActivityRule.class);
        tables.add(MarketPlanCommercialRel.class);
        tables.add(CustomerLevel.class);
        tables.add(CustomerGroupLevel.class);
        tables.add(CrmCustomerRightsConfig.class);
        tables.add(CrmLevelStoreRule.class);
        tables.add(CrmLevelStoreRuleDetail.class);
        tables.add(CrmCustomerThreshold.class);
        tables.add(ErpCommercialRelation.class);
        tables.add(DishBrandType.class);
        tables.add(DishShop.class);
        tables.add(DishPropertyType.class);
        tables.add(DishCyclePeriod.class);
        tables.add(DishBrandType.class);
        tables.add(OpenTime.class);
        //tables.add(Queue.class);

        //为了不报错加的表
        tables.add(TradeTable.class);
        //tables.add(AuthUserPermission.class);
        tables.add(Coupon.class);
        tables.add(CustomerScoreRule.class);
        tables.add(DishProperty.class);
        tables.add(DishSetmeal.class);
        tables.add(DishSetmealGroup.class);
        tables.add(CommercialCustomSettings.class);

        /*tables.add(Coupon.class);
        tables.add(CouponRuleDish.class);
        tables.add(Customer.class);
        tables.add(CustomerCardTime.class);
        tables.add(CustomerCoupon.class);
        tables.add(CustomerExtra.class);
        tables.add(CustomerLevelRule.class);
        tables.add(CustomerMarketingExpanded.class);
        tables.add(CustomerMarketingTogether.class);
        tables.add(CustomerScoreRule.class);
        tables.add(CustomerStored.class);
        tables.add(CutDownCustomer.class);
        tables.add(CutDownHistory.class);
        tables.add(CutDownMarketing.class);
        tables.add(ExpandedCommission.class);
        tables.add(FlashSalesMarketing.class);
        tables.add(MarketingExpanded.class);
        tables.add(MarketingPutOn.class);
        tables.add(MarketingShare.class);
        tables.add(MarketingTogether.class);
        tables.add(PushMessageCustomer.class);
        tables.add(PushPlanActivity.class);
        tables.add(PushPlanNewDish.class);*/
        //订单
        tables.add(CommercialArea.class); //桌台区域
        tables.add(Tables.class);
        //tables.add(TalentPlan.class);
        //tables.add(TalentRole.class);

        tables.add(TradeCustomer.class);
        tables.add(TradeUser.class);
        tables.add(TradePlanActivity.class);
        tables.add(TradeItemPlanActivity.class);
        tables.add(TradePrivilege.class);
        tables.add(TradePrivilegeExtra.class);
        tables.add(TradePromotion.class);
        tables.add(TradeItemProperty.class);
        tables.add(TradeReasonRel.class);
        tables.add(TradeItemExtra.class);
        tables.add(TradeCreditLog.class);
        tables.add(CoupRule.class);
        tables.add(CustomerSaveRule.class);

        BeautyDbHelperUtil.initTables(tables);
        TABLES = Collections.unmodifiableList(tables);
    }

    public YfDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, AppBuildConfig.VERSION_CODE);
    }

    /**
     * 创建SQLite数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        Log.i(TAG, "Creating database...");
        try {
            for (int i = 0; i < TABLES.size(); i++) {
                TableUtils.createTableIfNotExists(connectionSource, TABLES.get(i));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Log.e(TAG, "Unable to create database", ex);
        }
    }

    /**
     * 更新SQLite数据库
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        Constant.setIsNeedInit(true);
        Log.i(TAG, "Upgrading database from version " + oldVer + " to " + newVer);
        try {
            for (int i = TABLES.size() - 1; i >= 0; i--) {
                Class<? extends IEntity<?>> classType = TABLES.get(i);
                TableUtils.dropTable(connectionSource, classType, true);
            }
        } catch (SQLException ex) {
            Log.e(TAG, "Unable to drop table", ex);
        }
        onCreate(sqliteDatabase, connectionSource);
    }
}
