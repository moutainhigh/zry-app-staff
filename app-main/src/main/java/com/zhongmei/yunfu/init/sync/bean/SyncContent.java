package com.zhongmei.yunfu.init.sync.bean;

import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.discount.entity.CustomerDishPrivilege;
import com.zhongmei.yunfu.db.entity.TaskRemind;
import com.zhongmei.yunfu.db.entity.crm.CustomerSaveRule;
import com.zhongmei.yunfu.db.entity.dish.DishDescribe;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishSetmealGroup;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRule;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.yunfu.db.entity.dish.DishTimeChargingRule;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.bty.sync.push.SysCmdResponse;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.entity.AuthPermission;
import com.zhongmei.yunfu.db.entity.AuthRole;
import com.zhongmei.yunfu.db.entity.AuthRolePermission;
import com.zhongmei.yunfu.db.entity.AuthUser;
import com.zhongmei.yunfu.db.entity.Brand;
import com.zhongmei.yunfu.db.entity.Commercial;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.init.sync.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SyncContent implements NoProGuard {

    private static final String TAG = SyncContent.class.getSimpleName();
    private static final String CONFIG_MODULE_CMD = "configModuleCMD";

            private SyncItem<AuthUser> authUser;
    private SyncItem<AuthPermission> authPermission;
    private SyncItem<AuthRole> authRole;
    private SyncItem<AuthRolePermission> authRolePermission;
    private SyncItem<Booking> booking;

    private SyncItem<CommercialArea> tableArea;
    private SyncItem<Tables> tables;

    private SyncItem<DishShop> dishShop;
    private SyncItem<DishBrandType> dishBrandType;
    private SyncItem<Trade> trade;
    private SyncItem<TradeTable> tradeTable;
    private SyncItem<TradePrivilege> tradePrivilege;
    private SyncItem<TradeItem> tradeItem;
    private SyncItem<TradeItemProperty> tradeItemProperty;
    private SyncItem<TradeUser> tradeUser;
    private SyncItem<TradeCustomer> tradeCustomer;
    private SyncItem<Payment> payment;
    private SyncItem<PaymentItem> paymentItem;
    private SyncItem<PaymentItemExtra> paymentItemExtra;
    private SyncItem<Coupon> coupon;
    private SyncItem<CustomerScoreRule> customerScoreRule;
    private SyncItem<DishProperty> dishProperty;
    private SyncItem<DishSetmealGroup> dishSetmealGroup;
    private SyncItem<DishSetmeal> dishSetMeal;
    private SyncItem<CommercialCustomSettings> commercialCustomSettings;
    private SyncItem<CustomerSaveRule> customerSaveRule;
    private SyncItem<CustomerDishPrivilege> customerDishPrivilege;
    private SyncItem<DishTimeChargingRule> dishTimeChargingRule;
    private SyncItem<DishDescribe> dishDescribe;






    public static void setSyncModuleConfig(SysCmdResponse config) {
        SysCmdResponse.SyncModuleConfig moduleConfig = (config != null && config.syncModule != null) ? config.syncModule : new SysCmdResponse.SyncModuleConfig();
        SpHelper.getDefault().putObject(SyncContent.CONFIG_MODULE_CMD, moduleConfig);
    }

    public static List<SysCmdResponse.SyncModuleConfig.ModuleBean> getModuleConfig() {
        SysCmdResponse.SyncModuleConfig moduleConfig = SpHelper.getDefault().getObject(CONFIG_MODULE_CMD, SysCmdResponse.SyncModuleConfig.class);
        if (moduleConfig == null) {
            moduleConfig = new SysCmdResponse.SyncModuleConfig();
        }
        return moduleConfig.getModuleConfig(getSyncModule(false));
    }

    public static Set<String> getSyncModuleAll() {
        return getSyncModule(true);
    }

    public static Set<String> getSyncModule(final boolean includeInitModule) {
        final HashSet<String> modules = new HashSet<>();
        callSyncContentFields(SyncContent.class, new SyncContentFieldCall() {
            @Override
            public void onCall(Field field, Class<?> genericClazz) {
                if (includeInitModule || !isInitOneModule(genericClazz)) {
                    String moduleName = getModuleName(field, genericClazz);
                    modules.add(moduleName);
                }
            }
        });
        return modules;
    }

    private static boolean isInitOneModule(Class<?> genericClazz) {
        if (genericClazz == ErpCurrency.class
                || genericClazz == ErpCurrency.class) {
            return true;
        }
        return false;
    }

    public interface SyncContentFieldCall {

        void onCall(Field field, Class<?> genericClazz);
    }

    static final Map<Field, Class> FIELD_GENERIC_CLASS_MAP = new HashMap<>();

    public static void callSyncContentFields(Class clazz, final SyncContentFieldCall call) {
        ReflectUtils.callFields(clazz, new ReflectUtils.FieldCall() {
            @Override
            public void onCall(Field field) {
                if (field.getType() == SyncItem.class) {
                    Class<?> genericClazz = FIELD_GENERIC_CLASS_MAP.get(field);
                    if (genericClazz == null) {
                        genericClazz = ReflectUtils.getClass(field.getGenericType());
                        FIELD_GENERIC_CLASS_MAP.put(field, genericClazz);
                    }

                    if (call != null) {
                        call.onCall(field, genericClazz);
                    }
                }
            }
        }, true);
    }

    public static String getModuleName(Field field, Class genericClazz) {

        return field.getName();
    }
}
