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

        public interface ShopMonitorCode {

        final String MONITOR_CODE_HNXT = "hnxt";        final String MONITOR_CODE_FHXM = "fhxm";    }

        int getChannelSource();

        String getTaxIDNumber();

        String getTimeZone();

        String getFirstLanguage();

        String getSecondLanguage();
}
