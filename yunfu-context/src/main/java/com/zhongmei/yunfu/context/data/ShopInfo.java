package com.zhongmei.yunfu.context.data;

import android.text.TextUtils;

import com.zhongmei.yunfu.context.util.Utils;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 门店配置信息
 */
public class ShopInfo /*extends ResponseBoss*/ implements IShopInfo {

    public String deviceID;
    private Integer isMainPos;
    public String shopId;
    public String secretKey;
    public String autobind;
    public String areaCode;
    public String commercialGroupId;
    public String commercialGroupName;
    public String tabletNumber;
    public String syncUrl;
    public String backupSyncUrl;
    public String commercialName;
    public String commercialPhone;
    public String commercialAddress;
    public String commercialLogo;
    public String supportFastFood;
    public String printerServerIp;
    public String permission;
    public String payType;
    public String areaId;
    public String firstLanguage; //系统第一语言
    public String secondLanguage; //系统第二语言
    public String existKdsDevice;
    public Long currencyId; //国籍
    private String currencyNo;//货币编码
    private String currencySymbol;//货币符号
    public String bindDeviceType;
    public String mealTypeCode;
    public String mealTypeName;
    public String deviceType;
    public String functionNumber;
    public String firstName;
    public int mealType;
    //public long isUnionpay;
    //public long bankChannelId;
    //public double bankRates;
    public int isDelicaciesMeal; //是否为美食城业态 1-是，2-否；
    public int usingDeviceType; //使用设备类型1-消费端，2-收银端。
    public String monitorCode; //一种为none,表示无开关配置信息，也是默认；一种为hnxt,表示海南信投,fhxm表示烽火
    //public String shopInfos;
    public List<ReceiptInfo> receiptInfos;
    public String telAreaCode;
    public boolean expired;
    public Integer face;
    // v8.12.0 口碑一体机商户标示：11
    public int channelSource;
    public String taxIDNumber;//商户所在国家时区
    public String timeZone;//商户唯一税号


    public String mainMealName;

    public String mainMealCode;


    @Override
    public String getMainMealName() {
        return mainMealName;
    }

    @Override
    public String getMainMealCode() {
        if (mainMealCode == null) {
            mainMealCode = "";
        }
        return mainMealCode;
    }

    @Override
    public String getDeviceID() {
        return deviceID;
    }

    @Override
    public String getShopId() {
        return shopId;
    }

    @Override
    public Long getShopIdLong() {
        return Utils.toLong(shopId);
    }

    @Override
    public String getAreaCode() {
        return areaCode;
    }

    @Override
    public String getBrandId() {
        return commercialGroupId;
    }

    @Override
    public Long getBrandIdLong() {
        return Utils.toLong(commercialGroupId);
    }

    @Override
    public String getBrandName() {
        return commercialGroupName;
    }

    @Override
    public String getCommercialGroupId() {
        return commercialGroupId;
    }

    @Override
    public String getCommercialGroupName() {
        return commercialGroupName;
    }

    @Override
    public String getTabletNumber() {
        return tabletNumber;
    }

    @Override
    public String getSyncUrl() {
        return syncUrl;
    }

    @Override
    public String getBackupSyncUrl() {
        return backupSyncUrl;
    }

    @Override
    public String getCommercialName() {
        return commercialName;
    }

    @Override
    public String getCommercialPhone() {
        return commercialPhone;
    }

    @Override
    public String getCommercialAddress() {
        return commercialAddress;
    }

    @Override
    public String getCommercialLogo() {
        return commercialLogo;
    }

    @Override
    public String getPrinterServerIp() {
        return printerServerIp;
    }

    @Override
    public Long getCurrencyId() {
        return currencyId;
    }

    @Override
    public String getBindDeviceType() {
        return bindDeviceType;
    }

    @Override
    public int getMealType() {
        return mealType;
    }

    @Override
    public int getUsingDeviceType() {
        return usingDeviceType;
    }

    /**
     * 是否为美食城业态 true-是，false-否；
     *
     * @return
     */
    @Override
    public boolean isDelicaciesMeal() {
        return isDelicaciesMeal == 1;
    }

    @Override
    public String getTabletNumberFormat() {
        return String.format("%06d", Utils.toLong(tabletNumber));
    }

    @Override
    public boolean isExistKdsDevice() {
        //return existKdsDevice != 2;
        return !"2".equals(existKdsDevice);
    }

    @Override
    public boolean isMainPos() {
        return isMainPos != null && isMainPos == 0;
    }

    @Override
    public Biz getBiz() {
        return Biz.valueOf(mealType);
    }

    @Override
    public String getMonitorCode() {
        if (monitorCode != null) {
            return monitorCode;
        }
        return "none";
    }

    @Override
    public String getCurrencyNo() {
        return currencyNo;
    }

    @Override
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * 根据类型获取打印饿了么配置信息
     *
     * @param type
     * @return
     */
    /*public String getReceiptInfoURL(ReceiptInfo.Type type) {
        ReceiptInfo receiptInfo = getReceiptInfo(type);
        return receiptInfo != null ? receiptInfo.jumpUrl : null;
    }*/
    @Override
    public ReceiptInfo getReceiptInfo(ReceiptInfo.Type type) {
        if (receiptInfos != null) {
            for (ReceiptInfo info : receiptInfos) {
                if (info.receiptType == type.value) {
                    return info;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isExpired() {
        return expired;
    }

    @Override
    public boolean hasDisplay(String appCode) {
        String mealTypeCode = functionNumber;
        if (!TextUtils.isEmpty(mealTypeCode)) {
            String[] arrayMeal = mealTypeCode.split(",");
            for (String meal : arrayMeal) {
                if (meal.equals(appCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isMonitorCode(String code) {
        /*if (ShopMonitorCode.MONITOR_CODE_FHXM.equals(code))
            return true;*/

        if (this.monitorCode != null && code != null) {
            return this.monitorCode.equals(code);
        }
        return false;
    }

    @Override
    public int getChannelSource() {
        return channelSource;
    }

    @Override
    public String getTaxIDNumber() {
        return taxIDNumber;
    }

    @Override
    public String getTimeZone() {
        return timeZone;
    }

    @Override
    public String getFirstLanguage() {
        return firstLanguage;
    }

    @Override
    public String getSecondLanguage() {
        return secondLanguage;
    }
}
