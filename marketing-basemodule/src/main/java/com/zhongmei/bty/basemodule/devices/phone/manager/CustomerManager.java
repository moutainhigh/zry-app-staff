package com.zhongmei.bty.basemodule.devices.phone.manager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.zhongmei.bty.basemodule.devices.phone.bean.Customer;
import com.zhongmei.yunfu.context.base.BaseApplication;

import java.util.List;

/**
 * 与客户模块的交互
 *
 * @date 2014-10-23
 */
public class CustomerManager {
    private static final String TAG = CustomerManager.class.getSimpleName();

    private static CustomerManager mInstance;
    private Context mContext;

    private CustomerManager(Context context) {
        mContext = context;
    }

    /**
     * 不在这里传入Context, 而是提供单独的init方法, 主要为了避免每次传入Context
     *
     * @return
     */
    public static synchronized CustomerManager getDefault() {
        if (mInstance == null) {
            mInstance = new CustomerManager(BaseApplication.sInstance);
        }
        return mInstance;
    }

    @Deprecated
    public void init(Application context) {
        mContext = context;
    }

    /**
     * 是否安装了客户模块
     *
     * @return
     */
    public boolean hasCustomerModel() {
        return true;
    }

    /**
     * 创建客户
     */
    public void addCustomer(Context context, String phone) {
    	
    	/*ComponentName componentName = new ComponentName(  
                "com.zhongmei.bty",
                "com.zhongmei.bty.customer.CustomerAddActivity_"); */
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        //  intent.setComponent(componentName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction("com.zhongmei.bty.customer.add");
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, "", e);
        }
    }

    /**
     * 查看客户信息
     */
    public void viewCustomerInfo(Context context, String phone) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        Log.e("haoma", phone);
        intent.putExtras(bundle);
        //  intent.setComponent(componentName);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction("com.zhongmei.bty.customer.info");
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, "", e);
        }
    }


    /**
     * 根据手机号查找客户信息
     *
     * @param phone
     * @return
     */
    public Customer findCustomerByPhone(String phone) {
        /*if (System.currentTimeMillis() % 3 == 1) {
            return null;
        } 
        */


        return queryCustomer(phone);
    }

    public Customer findCustomerByPhone2(String phone) {
        /*if (System.currentTimeMillis() % 3 == 1) {
            return null;
        } 
        */


        return queryCustomer2(phone);
    }

    /**
     * 查找以phone开头的所有客户信息
     *
     * @param phone
     * @return
     */
    public List<Customer> listCustomerBeginWithPhone(String phone) {
        return null;
    }


    public Customer queryCustomer(String phone) {
        String name = null;
        String levelId = null;
        String sex = null;
        String serverId = null;
        String uuid = null;
        Cursor c = null;
        try {
            String sql = "name,mobile as phone,levelId,sex,serverId,uuid from customer where mobile='" + phone
                    + "' or mobile='0" + phone + "' ORDER BY name DESC limit 1" + " --";

            c = mContext.getContentResolver().query(Customer.CONTENT_URI, new String[]{sql}, null, null, null);
            while (c.moveToNext()) {
                name = c.getString(c.getColumnIndex("name"));
                levelId = c.getString(c.getColumnIndex("levelId"));
                sex = c.getString(c.getColumnIndex("sex"));
                serverId = c.getString(c.getColumnIndex("serverId"));
                uuid = c.getString(c.getColumnIndex("uuid"));
            }

            // OrderInfo orderInfo = parseData2Order(c);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return new Customer(null, phone, levelId);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return new Customer(name, phone, levelId, sex, serverId, uuid);

    }


    public Customer queryCustomer2(String phone) {
        String name = null;
        String levelId = null;
        String sex = null;
        String serverId = null;
        String uuid = null;
        Cursor c = null;
        try {
            String sql = "name,mobile as phone,levelId,sex,serverId,uuid from customer where mobile='" + phone
                    + "' ORDER BY name DESC limit 1" + " --";

            c = mContext.getContentResolver().query(Customer.CONTENT_URI, new String[]{sql}, null, null, null);
            while (c.moveToNext()) {
                name = c.getString(c.getColumnIndex("name"));
                levelId = c.getString(c.getColumnIndex("levelId"));
                sex = c.getString(c.getColumnIndex("sex"));
                serverId = c.getString(c.getColumnIndex("serverId"));
                uuid = c.getString(c.getColumnIndex("uuid"));
            }

            // OrderInfo orderInfo = parseData2Order(c);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return new Customer(null, phone, levelId);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return new Customer(name, phone, levelId, sex, serverId, uuid);

    }


}
