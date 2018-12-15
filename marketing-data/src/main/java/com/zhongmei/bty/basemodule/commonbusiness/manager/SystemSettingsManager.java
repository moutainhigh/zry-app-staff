package com.zhongmei.bty.basemodule.commonbusiness.manager;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.shopmanager.closing.entity.ClosingAccountRecord;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.commonmodule.database.entity.LydMarketSetting;
import com.zhongmei.yunfu.db.enums.StatusFlag;

/**
 * 设置管理
 */
public class SystemSettingsManager {
    private static final String TAG = SystemSettingsManager.class.getSimpleName();
    //糯米自动接单设置标识
    public static final String NUOMI_AUTO_SETTING = "nuomiAutoOrder";

    /**
     * 根据key值查询是否自动接单
     *
     * @param key
     * @return
     */
    public static boolean isThridAutoAccept(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            CommercialCustomSettings typeSettings = helper.getDao(CommercialCustomSettings.class).queryBuilder().where()
                    .eq(CommercialCustomSettings.$.key, key)
                    .and().eq(CommercialCustomSettings.$.statusFlag, StatusFlag.VALID).queryForFirst();
            if (typeSettings != null) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return false;
    }

    /**
     * 查询最近一次关账时间
     *
     * @return
     */
    public static Long queryLastClosingAccountRecord() {
        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        try {
            ClosingAccountRecord record = systemSettingDal.findClosingAccountRecord();
            if (record != null) {
                return record.getEndTime();
            }
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }
        return 0L;
    }

    /**
     * @return 获取电子发票相关设置数据，包括电子发票开关、设置信息、税率信息
     */
    public static ElectronicInvoiceVo findElectronicInvoiceVo() {
        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        try {
            return systemSettingDal.findElectronicInvoiceVo();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return new ElectronicInvoiceVo();
    }


    /**
     * 获取代金券使用张数设置
     *
     * @return 用代金券的数量
     */
    public static int getCashCouponNum() {
        SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
        try {
            LydMarketSetting lydMarketSetting = systemSettingDal.findMarketSetting("cashCoupUseNum");
            if (lydMarketSetting != null) {
                int num = lydMarketSetting.getConfigValue();
                if (num > 0) {
                    return num;
                }
                return 1;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return 1;
    }

    /**
     * mind自动接单开关
     * @return
     */

    /*
    public static boolean mindAutoAccept() {
        DatabaseHelper helper = DBHelper.getHelper();
        try {
            CommercialCustomSettings typeSettings = helper.getDao(CommercialCustomSettings.class).queryBuilder().where()
                    .eq(CommercialCustomSettings.$.key, "shop.autoOrderSetting.switch")
                    .and().eq(CommercialCustomSettings.$.statusFlag, StatusFlag.VALID).queryForFirst();
            if (typeSettings == null) {
                return false;
            }
            if(typeSettings.getValue().equals("1"))
                return true;
            else
                return false;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            DBHelper.releaseHelper(helper);
        }
        return false;
    }
    */

}
