package com.zhongmei.yunfu.context.util.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhongmei.yunfu.context.base.BaseApplication;

import java.util.Set;


public class SpHelper {

    private static SpHelper mInstance;

    private Context mContext;

    public static final String SP_NAME = "phone_sp";

    public static final String SP_LAST_CONNECT_TYPE = "last_connect_type";

    public static final String SP_PHONE_BOX_CONNECT_IP = "phone_box_connect_ip";
    public static final String SP_PHONE_BOX_CONNECT_MAC = "phone_box_connect_mac";

    public static final String SP_BLUETOOTH_CONNECT_NAME = "bluetooth_connect_name";
    public static final String SP_BLUETOOTH_CONNECT_MAC = "bluetooth_connect_mac";

    public static final String SP_UNREAD_NO_ANSWER = "unread_no_answer";

    public static final String SP_CALLLOG_CLEAR_PERIOD = "calllog_clear_period";
    public static final String SP_BLUETOOTH_SERVICE_SWITCH = "bluetooth_service_switch";
    public static final String SP_FIXEDPHONE_SERVICE_SWITCH = "fixedphone_service_switch";
    public static final String SP_IP_ADDRESS = "ip_address";
    public static final String SP_CUR_BUFFET_UUID = "cur_buffet_uuid";

        public static final String FACE_COLLECTED = "allow_face_collection";
        public static final String CUSTOMER_GOIN_REMIND = "customer_go_in_remind";
        public static final String WEIXIN_REMIND = "weixin_remind";
        public static final String FACE_REMIND = "face_remind";
        public static final String IBEACON_REMIND = "Ibeacon_remind";
        public static final String CUSTOMER_ARRIVE_DOT = "customer_arrive_dot";
        public static final String CUSTOMER_ARRIVE_NOTICE = "customer_arrive_notice";
        public static final String DINNER_ORDER_DISH_OPEN_OPERATION = "dinner_order_dish_open_operation";
        public static final String DINNER_FOLD_PRACTICE = "dinner_fold_practice";
        public static final String DINNER_CLEAR_SORT_SETTING = "dinner_clear_sort_setting";
        public static final String DINNER_DISH_LANGUAGE = "dinner_dish_language";
        public static final String SETTING_MONEY_BOX_IP = "setting_money_box_ip";

    public static final String APP_UPDATE_NOT_SECOND_REMIND = "app_update_not_second_remind";

    private SpHelper(Application context) {
        mContext = context;
    }

    public synchronized static SpHelper getDefault() {
        if (mInstance == null) {
            mInstance = new SpHelper(BaseApplication.sInstance);
        }
        return mInstance;
    }

    @Deprecated
    public void init(Application context) {
        mContext = context;
    }

    public int getUnreadNoAnswerCallHistoryCount() {
        return getInt(SP_UNREAD_NO_ANSWER);
    }

    public SpHelper saveUnreadNoAnswerCallHistoryCount(int count) {
        return putInt(SP_UNREAD_NO_ANSWER, count);
    }


    public String getBluetoothConnectMac() {
        return getString(SP_BLUETOOTH_CONNECT_MAC);
    }

    public SpHelper saveBluetoothConnectMac(String mac) {
        return putString(SP_BLUETOOTH_CONNECT_MAC, mac);
    }

    public String getBluetoothConnectName() {
        return getString(SP_BLUETOOTH_CONNECT_NAME);
    }

    public SpHelper saveBluetoothConnectName(String name) {
        return putString(SP_BLUETOOTH_CONNECT_NAME, name);
    }

    public String getPhoneBoxConnectMac() {
        return getString(SP_PHONE_BOX_CONNECT_MAC);
    }

    public SpHelper savePhoneBoxConnectMac(String mac) {
        return putString(SP_PHONE_BOX_CONNECT_MAC, mac);
    }

    public SpHelper saveIpAddress(String ip) {
        return putString(SP_IP_ADDRESS, ip);
    }

    public String getIpAddress() {
        return getString(SP_IP_ADDRESS);
    }

    public String getPhoneBoxConnectIp() {
        return getString(SP_PHONE_BOX_CONNECT_IP);
    }

    public SpHelper savePhoneBoxConnectIp(String ip) {
        return putString(SP_PHONE_BOX_CONNECT_IP, ip);
    }

    public String getLastConnectType() {
        return getString(SP_LAST_CONNECT_TYPE);
    }

    public SpHelper saveLastConnectType(String type) {
        return putString(SP_LAST_CONNECT_TYPE, type);
    }

    public SpHelper saveCalllogClearPeriod(int clearDatePeriod) {
        return putInt(SP_CALLLOG_CLEAR_PERIOD, clearDatePeriod);
    }

    public int getCalllogClearPeriod() {
        return getInt(SP_CALLLOG_CLEAR_PERIOD, 2);
    }


    public SpHelper putString(String key, String value) {
        getSharedPreferencesEditor().putString(key, value).commit();
        return this;
    }

    public SpHelper putInt(String key, int value) {
        getSharedPreferencesEditor().putInt(key, value).commit();
        return this;
    }

    public SpHelper putBoolean(String key, boolean value) {
        getSharedPreferencesEditor().putBoolean(key, value).commit();
        return this;
    }

    public SpHelper putLong(String key, long value) {
        getSharedPreferencesEditor().putLong(key, value).commit();
        return this;
    }

    public String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public long getLong(String key, long defValue) {
        return getSharedPreferences().getLong(key, defValue);
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private Editor getSharedPreferencesEditor() {
        return mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit();
    }


    public Set<String> getStringSet(String key, Set<String> values) {
        return getSharedPreferences().getStringSet(key, values);
    }


    public boolean remove(String key) {
        return getSharedPreferencesEditor().remove(key).commit();
    }


    public boolean putStringSet(String key, Set<String> values) {
        return getSharedPreferencesEditor().putStringSet(key, values)
                .commit();
    }

    public void putObject(String key, Object value) {
        getSharedPreferencesEditor().putString(key, new Gson().toJson(value))
                .commit();
    }

    public <T> T getObject(String key, Class<T> tClass) {
        try {
            return new Gson().fromJson(getSharedPreferences().getString(key, null), tClass);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}
