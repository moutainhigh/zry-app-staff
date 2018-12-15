package com.zhongmei.yunfu.context.data;

import android.util.Log;

import com.google.gson.Gson;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.Constant;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

import java.text.DecimalFormat;

/**
 * Created by demo on 2018/12/15
 * 门店配置信息
 */
public class ShopInfoCfg {

    private static final String TAG = ShopInfoCfg.class.getSimpleName();
    public static final String SHOP_INFO_CFG = "calm3_shop_info_cfg";
    private static ShopInfoCfg instance = null;

    public enum UserState {
        UNKNOWN, BIND, INIT, LOGIN
    }

    private static SpHelper spHelper;
    private UserState userState = UserState.UNKNOWN;
    //public ShopInfo shopInfo;
    public IAuthUser authUser;
    public String city;
    //private String currencySymbol; //国籍
    private ICurrency currency;
    private VersionInfo appVersionInfo;
    private VersionInfo printVersionInfo;
    //private Locale locale;

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
    public String currencyNo;// 货币编码
    public String currencySymbol;//货币符号
    // v8.12.0 口碑商户标示
    public int channelSource;
    public String taxIDNumber;//商户唯一税号
    public String timeZone;//商户所在国家时区

    // v8.15.0 第一语言，第二语言
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
        //instance = null;
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

    /**
     * 判断是否有显示权限，默认为有显示权限
     *
     * @return
     */
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

    /**
     * 改方法已过时，请调用 SwitchServerManager.getInstance().getServerKey()
     *
     * @return
     */
    @Deprecated
    public String getServerKey() {
        return SwitchServerManager.getInstance().getServerKey();
    }

    public IShopInfo getShopInfo() {
        return shopInfo;
    }

    /**
     * 是否为美食城业态 true-是，false-否；
     *
     * @return
     */
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

    /*public String getUIShopId(Context context) {
        return context.getString(R.string.commonmodule_shop_id) + shopId;
    }

    public String getUICommercialName(Context context) {
        return context.getString(R.string.commonmodule_shop_name) + commercialName;
    }

    public String getUICommercialGroupId(Context context) {
        return context.getString(R.string.commonmodule_shop_group_id) + commercialGroupId;
    }

    public String getUICommercialGroupName(Context context) {
        return context.getString(R.string.commonmodule_shop_group_name) + commercialGroupName;
    }

    public String getUIcommercialAddress(Context context) {
        return context.getString(R.string.commonmodule_shop_address) + commercialAddress;
    }

    public String getUICommercialPhone(Context context) {
        return context.getString(R.string.commonmodule_shop_phone) + commercialPhone;
    }

    public String getUItabletNumber(Context context) {
        return context.getString(R.string.commonmodule_settings_pos_id) + getTabletNumberFormat();
    }

    public String getUISyncUrl(Context context) {
        return context.getString(R.string.commonmodule_shop_server) + SwitchServerManager.getInstance().getServerKey();
    }

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
    }*/

    //add v8.7
    public interface ShopMonitorCode {
        /* hnxt,表示海南信投,fhxm表示烽火*/
        final String MONITOR_CODE_HNXT = "hnxt";//海南信投
        final String MONITOR_CODE_FHXM = "fhxm";//烽火科技
    }

    public boolean isMonitorCode(String code) {
        return shopInfo != null && shopInfo.isMonitorCode(code);
    }

    /**
     * "Y######.00"格式 将金额格式化添加货币符号
     */
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
