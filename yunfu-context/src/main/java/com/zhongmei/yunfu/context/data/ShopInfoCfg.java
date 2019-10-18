package com.zhongmei.yunfu.context.data;

import android.util.Log;

import com.google.gson.Gson;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.Constant;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

import java.text.DecimalFormat;


public class ShopInfoCfg {

    private static final String TAG = ShopInfoCfg.class.getSimpleName();
    public static final String SHOP_INFO_CFG = "calm3_shop_info_cfg";
    private static ShopInfoCfg instance = null;

    public enum UserState {
        UNKNOWN, BIND, INIT, LOGIN
    }

    private static SpHelper spHelper;
    private UserState userState = UserState.UNKNOWN;
        public IAuthUser authUser;
    public String city;
        private ICurrency currency;
    private VersionInfo appVersionInfo;
    private VersionInfo printVersionInfo;

    private IShopInfo shopInfo;

    public String deviceID;
    public String shopId;
    public String areaCode;
    public String commercialGroupId;
    public String commercialGroupName;
    public String tabletNumber;
    public String commercialName;
    public String commercialPhone;
    public String commercialAddress;
    public String commercialLogo;
    public String printerServerIp;
    public Long currencyId;
    public String bindDeviceType;
    public int mealType;
    public int usingDeviceType;
    public String currencyNo;    public String currencySymbol;        public int channelSource;
    public String taxIDNumber;    public String timeZone;
        public String firstLanguage;
    public String secondLanguage;

    private ShopInfoCfg(IShopInfo response) {
        setUserState(response != null ? UserState.BIND : UserState.UNKNOWN);
        this.shopInfo = response != null ? response : new ShopInfo();
        SwitchServerManager.init(shopInfo.getSyncUrl(), shopInfo.getBackupSyncUrl());
        this.deviceID = shopInfo.getDeviceID();
        this.shopId = shopInfo.getShopId();
        this.areaCode = shopInfo.getAreaCode();
        this.commercialGroupId = shopInfo.getCommercialGroupId();
        this.commercialGroupName = shopInfo.getCommercialGroupName();
        this.tabletNumber = shopInfo.getTabletNumber();
        this.commercialName = shopInfo.getCommercialName();
        this.commercialPhone = shopInfo.getCommercialPhone();
        this.commercialAddress = shopInfo.getCommercialAddress();
        this.commercialLogo = shopInfo.getCommercialLogo();
        this.printerServerIp = shopInfo.getPrinterServerIp();
        this.currencyId = shopInfo.getCurrencyId();
        this.bindDeviceType = shopInfo.getBindDeviceType();
        this.mealType = shopInfo.getMealType();
        this.usingDeviceType = shopInfo.getUsingDeviceType();
        this.currencyNo = shopInfo.getCurrencyNo();
        this.currencySymbol = shopInfo.getCurrencySymbol();
        this.channelSource = shopInfo.getChannelSource();
        this.timeZone = shopInfo.getTimeZone();
        this.taxIDNumber = shopInfo.getTaxIDNumber();
        this.firstLanguage = shopInfo.getFirstLanguage();
        this.secondLanguage = shopInfo.getSecondLanguage();
    }

    public static void init(IShopInfo response) {
        init(new Gson().toJson(response));
    }

    public static void init(String response) {
        getSpHelper().putString(SHOP_INFO_CFG, response);
        IShopInfo shopInfo = getShopInfoCfg(response);
        instance = new ShopInfoCfg(shopInfo);
    }

    public static void setSpHelper(SpHelper spHelper) {
        ShopInfoCfg.spHelper = spHelper;
    }

    public static SpHelper getSpHelper() {
        if (spHelper == null) {
            spHelper = SpHelper.getDefault();
        }
        return spHelper;
    }

    private static IShopInfo getShopInfoCfg(String json) {
        try {
            return new Gson().fromJson(json, ShopInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    synchronized public static ShopInfoCfg getInstance() {
        if (instance == null) {
            String cfg = getSpHelper().getString(SHOP_INFO_CFG);
            IShopInfo shopInfo = getShopInfoCfg(cfg);
            instance = new ShopInfoCfg(shopInfo);
        }
        return instance;
    }

    public void setUserState(UserState state) {
        this.userState = state;
    }

    public UserState getUserState() {
        return userState;
    }

    public boolean hasUserState(UserState... state) {
        if (state != null) {
            for (UserState it : state) {
                if (it == userState) {
                    return true;
                }
            }
        }
        return false;
    }

    public void login(IAuthUser authUser) {
        setUserState(UserState.LOGIN);
        this.authUser = authUser;
    }

    public void logout() {
        setUserState(userState != UserState.UNKNOWN ? userState.BIND : UserState.UNKNOWN);
            }

    public VersionInfo getAppVersionInfo() {
        if (appVersionInfo == null) {
            appVersionInfo = new VersionInfo();
        }
        return appVersionInfo;
    }

    public void setAppVersionInfo(VersionInfo appVersionInfo) {
        this.appVersionInfo = appVersionInfo;
    }

    public VersionInfo getPrintVersionInfo() {
        if (printVersionInfo == null) {
            printVersionInfo = new VersionInfo();
            printVersionInfo.setPackageName(Constant.PRINT_SERVICE_PACKAGE_NAME);
        }
        return printVersionInfo;
    }

    public void setPrintVersionInfo(VersionInfo printVersionInfo) {
        this.printVersionInfo = printVersionInfo;
        if (printVersionInfo != null) {
            printVersionInfo.setPackageName(Constant.PRINT_SERVICE_PACKAGE_NAME);
        }
    }

    public boolean isBind() {
        return userState == UserState.BIND || isInit();
    }

    public boolean isInit() {
        return userState == UserState.INIT || isLogin();
    }

    public boolean isLogin() {
        return userState == UserState.LOGIN;
    }


    public boolean hasDisplay(String[] arrayAppCode) {
        if (arrayAppCode != null) {
            for (String _code : arrayAppCode) {
                if (shopInfo.hasDisplay(_code)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Deprecated
    public String getServerKey() {
        return SwitchServerManager.getInstance().getServerKey();
    }

    public IShopInfo getShopInfo() {
        return shopInfo;
    }


    public boolean isDelicaciesMeal() {
        return shopInfo != null && shopInfo.isDelicaciesMeal();
    }

    public String getTabletNumberFormat() {
        return String.format("%06d", Utils.toLong(tabletNumber));
    }

    public boolean isExistKdsDevice() {
        return shopInfo != null && shopInfo.isExistKdsDevice();
    }

    public boolean isMainPos() {
        return shopInfo != null && shopInfo.isMainPos();
    }

    public Biz getBiz() {
        return Biz.valueOf(mealType);
    }

    public String getMonitorCode() {
        return shopInfo != null ? shopInfo.getMonitorCode() : "none";
    }

    public void setCurrency(ICurrency currency) {
        this.currency = currency;
    }

    public ICurrency getCurrency() {
        return currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol != null ? currencySymbol : ICurrency.DEFAULT_SYMBOL;
    }

    public static String formatCurrencySymbol(Object money) {
        return String.format("%s%s", getInstance().getCurrencySymbol(), money);
    }

    public ReceiptInfo getReceiptInfo(ReceiptInfo.Type type) {
        return shopInfo != null ? shopInfo.getReceiptInfo(type) : null;
    }

    public boolean isExpired() {
        return shopInfo != null && shopInfo.isExpired();
    }



        public interface ShopMonitorCode {

        final String MONITOR_CODE_HNXT = "hnxt";        final String MONITOR_CODE_FHXM = "fhxm";    }

    public boolean isMonitorCode(String code) {
        return shopInfo != null && shopInfo.isMonitorCode(code);
    }


    public static String formatCash(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            return ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(value);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return ShopInfoCfg.getInstance().getCurrencySymbol() + value + "";
    }
}
