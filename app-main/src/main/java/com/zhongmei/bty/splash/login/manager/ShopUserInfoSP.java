/*
package com.zhongmei.bty.splash.login.manager;

import android.content.Context;
import android.content.SharedPreferences;

*/
/**
 * Created by demo on 2018/12/15
 * 用于保存商户和用户信息，供多进程间享用
 *//*


public class ShopUserInfoSP {
    private final String KEY_SHOPID = "key_shopid";
    private final String KEY_USERID = "key_userid";
    private final String KEY_BRANDID = "key_brandid";
    private final String KEY_USERNAME = "key_username";
    private final String SP_FILE_NAME = "sp_shopuser_info";
    private SharedPreferences shopUserInfoSP;

    public ShopUserInfoSP(Context context) {
        if (context != null)
            shopUserInfoSP = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_MULTI_PROCESS);
    }

    public void setShopId(String shopId) {
        if (shopUserInfoSP != null) {
            shopUserInfoSP.edit().putString(KEY_SHOPID, shopId).commit();
        }
    }

   public String getShopId() {
        if (shopUserInfoSP != null) {
            return shopUserInfoSP.getString(KEY_SHOPID, "");
        }
        return null;
    }

    public void setBrandId(String brandId) {
        if (shopUserInfoSP != null) {
            shopUserInfoSP.edit().putString(KEY_BRANDID, brandId).commit();
        }
    }

 public String getBrandId() {
        if (shopUserInfoSP != null) {
            return shopUserInfoSP.getString(KEY_BRANDID,"");
        }
        return null;
    }

    public void setUserId(Long userId) {
        if (shopUserInfoSP != null) {
            shopUserInfoSP.edit().putLong(KEY_USERID, userId).commit();
        }
    }

   public Long getUserId() {
        if (shopUserInfoSP != null) {
            return shopUserInfoSP.getLong(KEY_USERID, -1);
        }
        return null;
    }

    public void setUserName(String userName) {
        if (shopUserInfoSP != null) {
            shopUserInfoSP.edit().putString(KEY_USERNAME, userName).commit();
        }
    }

    public String getUserName() {
        if (shopUserInfoSP != null) {
            return shopUserInfoSP.getString(KEY_USERNAME, null);
        }
        return null;
    }
    public void clearAll(){
        if (shopUserInfoSP != null) {
            shopUserInfoSP.edit().clear().commit();
        }
    }
    public void clearUserInfo(){
       setUserId(-1L);
       setUserName("");
    }
}
*/
