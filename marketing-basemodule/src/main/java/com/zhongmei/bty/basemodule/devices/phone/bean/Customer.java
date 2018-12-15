package com.zhongmei.bty.basemodule.devices.phone.bean;

import android.net.Uri;

import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.bty.basemodule.devices.phone.bean.AdapterData;

public class Customer implements AdapterData {

    public static String SEX_MAIL = String.valueOf(Sex.MALE.value());

    public static String SEX_FEMAIL = String.valueOf(Sex.FEMALE.value());

    private String name;

    private String phone;

    private String levelId;

    private String sex;

    private String serverId;

    private String uuid;

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public static final String AUTHORITY = "com.zhongmei.calm.provider.Calm";

    public static final String CUSTOMER = "/customer";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CUSTOMER);

    public Customer(String name, String phone, String levelId) {
        super();
        this.name = name;
        this.phone = phone;
        this.levelId = levelId;
    }

    public Customer(String name, String phone, String levelId, String sex, String serverId, String uuid) {
        super();
        this.name = name;
        this.phone = phone;
        this.levelId = levelId;
        this.sex = sex;
        this.serverId = serverId;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }


    @Override
    public String toString() {

        return "serverId=" + serverId + ",name=" + name + ",phone=" + phone + ",sex=" + sex + ",levelId=" + levelId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
