package com.zhongmei.yunfu;

import com.google.gson.Gson;
import com.zhongmei.yunfu.context.data.ICurrency;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.context.util.manager.SwitchServerManager;

import java.util.regex.Pattern;


public class ShopInfoManager {

    public enum UserState {
        UNKNOWN, BIND, INIT, LOGIN
    }

    private static final String TAG = ShopInfoManager.class.getSimpleName();
    public static final String MARKETING_SERVER_HOST = "http://mk.zhongmeiyunfu.com/marketing";
//    public static final String MARKETING_SERVER_HOST ="http://10.180.2.177:8080/marketing";
    public static final String REMOTE_SERVER_HOST = "http://b.zhongmeiyunfu.com/MeiYe";
    //    public static final String REMOTE_SERVER_HOST = "http://192.168.14.100:8090/MeiYe";
    public static final String SHOP_INFO_CFG = "zm_shop_info_cfg";
    private static ShopInfoManager instance = null;
    private static SpHelper spHelper;
    private UserState userState = UserState.UNKNOWN;
    public ShopInfo shopInfo;
    public IAuthUser authUser;

    private ShopInfoManager(ShopInfo response) {
        setUserState(response != null ? UserState.BIND : UserState.UNKNOWN);
        this.shopInfo = response != null ? response : new ShopInfo();
        SwitchServerManager.init(shopInfo.getSyncUrl(), shopInfo.getSyncUrl());
    }

    public static void init(ShopInfo response) {
        init(new Gson().toJson(response));
    }

    public static void init(String response) {
        getSpHelper().putString(SHOP_INFO_CFG, response);
        ShopInfo shopInfo = getShopInfoCfg(response);
        instance = new ShopInfoManager(shopInfo);
    }

    public static void setSpHelper(SpHelper spHelper) {
        ShopInfoManager.spHelper = spHelper;
    }

    public static SpHelper getSpHelper() {
        if (spHelper == null) {
            spHelper = SpHelper.getDefault();
        }
        return spHelper;
    }

    private static ShopInfo getShopInfoCfg(String json) {
        try {
            return new Gson().fromJson(json, ShopInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    synchronized public static ShopInfoManager getInstance() {
        if (instance == null) {
            String cfg = getSpHelper().getString(SHOP_INFO_CFG);
            ShopInfo shopInfo = getShopInfoCfg(cfg);
            instance = new ShopInfoManager(shopInfo);
        }
        return instance;
    }

    public void login(IAuthUser authUser) {
        setUserState(UserState.LOGIN);
        this.authUser = authUser;
    }

    public void logout() {
        setUserState(userState != UserState.UNKNOWN ? userState.BIND : UserState.UNKNOWN);
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

    public boolean isBind() {
        return userState == UserState.BIND || isInit();
    }

    public boolean isInit() {
        return userState == UserState.INIT || isLogin();
    }

    public boolean isLogin() {
        return userState == UserState.LOGIN;
    }


    public String getServerKey() {
        return MARKETING_SERVER_HOST;
    }

    public ShopInfo getShopInfo() {
        return shopInfo != null ? shopInfo : new ShopInfo();
    }



    public ICurrency getCurrency() {
        return new ICurrency() {
            @Override
            public Long getId() {
                return null;
            }

            @Override
            public String getAreaCode() {
                return null;
            }

            @Override
            public String getPhoneRegulation() {
                return "1[0-9]{10}";
            }

            @Override
            public boolean checkPhone(String phone) {
                return Pattern.matches(getPhoneRegulation(), phone);
            }

            @Override
            public String getCountryZh() {
                return null;
            }

            @Override
            public String getCountryEn() {
                return null;
            }

            @Override
            public String getCurrencySymbol() {
                return "￥";
            }
        };
    }
}