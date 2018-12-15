package com.zhongmei.bty.basemodule.devices.phone.entity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zhongmei.bty.basemodule.customer.bean.CustomerMobile;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobileVo;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.phone.utils.Lg;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.util.Date;


/**
 * 通话记录
 *
 * @date 2014-8-14
 */
public class CallHistorySession {

    private static CallHistory mInstance;

    private static CallHistory mRecordInstance = null;


    private CallHistorySession() {

        mRecordInstance = new CallHistory();
    }


    public synchronized static CallHistory openInCallSession() {

        CallHistory instance = getSession();
        instance.setStartTime(new Date());
        instance.setType(CallHistory.TYPE_INCALL);
        instance.setNumber(null);
        Log.e("CallHistorySession", "openInCallSession() <===> mInstance = " + instance);
        return instance;
    }


    public synchronized static CallHistory openOutCallSession() {

        CallHistory instance = getSession();
        instance.setStartTime(new Date());
        instance.setType(CallHistory.TYPE_OUTCALL);
        Log.e("CallHistorySession", "openOutCallSession() <===> mInstance = " + instance);
        return instance;
    }


    public synchronized static CallHistory getSession() {

//        Log.e("CallHistorySession","getSession() <===> mInstance = " + mInstance);
        if (mInstance == null) {

            mInstance = new CallHistory();
        }
        return mInstance;
    }


    public synchronized static void closeSession() {

        if (mInstance != null) {
            //重新创建句柄，为了快速释放minstance，以免影响新来电时getsession的值
            mRecordInstance = mInstance;
            Log.e("CallHistorySession", "closeSession()::mInstance = " + mInstance);
            Log.e("CallHistorySession", "closeSession()::mRecordInstance = " + mRecordInstance);
            mRecordInstance.setEndTime(new Date());
            String phoneNum = mRecordInstance.getNumber();
            setCallCustomerInfoByPhone(phoneNum);
//	        mInstance = null;//保存完毕清空
        } else {
            Lg.d("CallHistory#closeSession", "fail,  maybe there is no history");
        }
    }


    /**
     * 通过电话号码异步设置购物车顾客信息
     *
     * @param phoneNo
     */
    private static void setCallCustomerInfoByPhone(@NonNull final String phoneNo) {
        CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
        customerOperate.findCustomerByPhone(phoneNo, new ResponseListener<CustomerMobileVo>() {
            @Override
            public void onResponse(ResponseObject<CustomerMobileVo> response) {
                boolean hasCustomerInfo = ResponseObject.isOk(response) && CustomerMobileVo.isOk(response.getContent())
                        && response.getContent().result != null;
                if (hasCustomerInfo) {
                    CustomerResp mDataCustomer = toCustomerNew(response.getContent().result);
                    mRecordInstance.setContactsName(mDataCustomer.customerName);
                }
                mRecordInstance.save();
            }

            @Override
            public void onError(VolleyError error) {
                mRecordInstance.save();
            }
        });
    }

    /**
     * 转成customerNew
     */
    private static CustomerResp toCustomerNew(CustomerMobile customerMobile) {
        CustomerResp customerNew = new CustomerResp();
        customerNew.customerId = customerMobile.customerId;
        customerNew.customerName = customerMobile.customerName;
        customerNew.sex = customerMobile.sex;
        customerNew.levelId = customerMobile.levelId;
        customerNew.mobile = customerMobile.mobile;
        customerNew.synFlag = customerMobile.synFlag;
        return customerNew;
    }

}
