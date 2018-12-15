package com.zhongmei.yunfu.context.data;

public interface IShopInfo {

    String getMainMealName();

    String getMainMealCode();

    String getDeviceID();

    String getShopId();

    Long getShopIdLong();

    String getAreaCode();

    String getCommercialGroupId();

    String getCommercialGroupName();

    String getBrandId();

    Long getBrandIdLong();

    String getBrandName();

    String getTabletNumber();

    String getSyncUrl();

    String getBackupSyncUrl();

    String getCommercialName();

    String getCommercialPhone();

    String getCommercialAddress();

    String getCommercialLogo();

    String getPrinterServerIp();

    Long getCurrencyId();

    String getBindDeviceType();

    int getMealType();

    int getUsingDeviceType();

    boolean isDelicaciesMeal();

    String getTabletNumberFormat();

    boolean isExistKdsDevice();

    boolean isMainPos();

    Biz getBiz();

    String getMonitorCode();

    String getCurrencyNo();

    String getCurrencySymbol();

    /*public String getReceiptInfoURL(ReceiptInfo.Type type) {
        ReceiptInfo receiptInfo = getReceiptInfo(type);
        return receiptInfo != null ? receiptInfo.jumpUrl : null;
    }*/
    ReceiptInfo getReceiptInfo(ReceiptInfo.Type type);

    boolean isExpired();

    boolean hasDisplay(String appCode);

    boolean isMonitorCode(String code);

    public enum Biz {

        DINNER(1),
        SNACK(2);

        int value;

        Biz(int value) {
            this.value = value;
        }

        public static Biz valueOf(int value) {
            for (Biz biz : Biz.values()) {
                if (value == biz.value) {
                    return biz;
                }
            }
            return SNACK;
        }
    }

    //add v8.7
    public interface ShopMonitorCode {
        /* hnxt,表示海南信投,fhxm表示烽火*/
        final String MONITOR_CODE_HNXT = "hnxt";//海南信投
        final String MONITOR_CODE_FHXM = "fhxm";//烽火科技
    }

    // v8.12.0 口碑商户
    int getChannelSource();

    //商户唯一税号
    String getTaxIDNumber();

    //商户所在国家时区
    String getTimeZone();

    // v8.15.0 第一语言
    String getFirstLanguage();

    // v8.15.0 第二语言
    String getSecondLanguage();
}
