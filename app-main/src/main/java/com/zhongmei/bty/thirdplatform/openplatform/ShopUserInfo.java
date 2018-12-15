package com.zhongmei.bty.thirdplatform.openplatform;

import java.io.Serializable;

/**
 * Created by demo on 2018/12/15
 */

public class ShopUserInfo implements Serializable {
    public static final String TABLE_NAME = "shop_user";

    public static final String SHOPID = "shopId";
    public static final String SHOPNAME = "shopName";

    public static final String USERID = "userId";
    public static final String USERNAME = "userName";

    public static final String BRANDID = "brandId";
    public static final String BRANDNAME = "brandName";

    private String shopId;
    private String shopName;
    private String userId;
    private String userName;
    private String brandId;
    private String brandName;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "ShopUserInfo{" +
                "shopId='" + shopId + '\'' +
                ", userId='" + userId + '\'' +
                ", brandId='" + brandId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public static String creatTable() {
        return "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "(_id INTEGER PRIMARY KEY, " + SHOPID + " TEXT" +
                ", " + USERID + " TEXT, " + BRANDID + " TEXT, " + USERNAME + "  TEXT," + SHOPNAME + "  TEXT," + BRANDNAME + "  TEXT)";
    }

    public static String dropTable() {
        return "DROP TABLE " + TABLE_NAME;
    }
}
