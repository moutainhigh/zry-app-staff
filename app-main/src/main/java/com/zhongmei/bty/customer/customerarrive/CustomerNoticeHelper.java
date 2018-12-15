package com.zhongmei.bty.customer.customerarrive;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;
import com.zhongmei.bty.data.operates.CustomerArrivalShopDal;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.commonmodule.database.enums.ArriveWay;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.basemodule.commonbusiness.manager.QueuePlayServiceManager;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 顾客到店相关业务工具类
 */

public class CustomerNoticeHelper {
    public static void updateOperateStatus(final CustomerArrivalShop customer) {
        if (customer != null && customer.operateStatus == 1) {
            ThreadUtils.runOnWorkThread(new Runnable() {
                @Override
                public void run() {
                    CustomerArrivalShopDal dal = OperatesFactory.create(CustomerArrivalShopDal.class);
                    customer.operateStatus = 2;
                    try {
                        dal.update(customer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //是否开启顾客到店滚动提示
    public static boolean isOpenCustomerNotice() {
        return SpHelper.getDefault().getBoolean(SpHelper.CUSTOMER_ARRIVE_NOTICE, true);
    }

    //顾客到店语音叫号
    public static void callCustomerArrive(Context context, CustomerArrivalShop customer) {
        if (context == null || customer == null) {
            return;
        }
        String content = getCustomerName(context, customer) + getCustomerStatus(context, customer.arrivalStatus);
       /* BaiduSyntheticSpeech speech = new BaiduSyntheticSpeech();
        speech.setType(1);
        speech.setContent(content);
        speech.setSex(Sex.FEMALE);
        speech.setSpeed(5);*/// modify 20180130
        QueuePlayServiceManager.playAudition(content);
    }

    //拼接会员显示名称
    public static String getCustomerName(Context context, CustomerArrivalShop customer) {
        if (context == null || customer == null) {
            return " ";
        }
        String name = customer.customerName == null ? context.getString(R.string.customer_name_null) : customer.customerName;
        if (customer.customerSex != null) {
            switch (customer.customerSex) {
                case 0:
                    return name + context.getString(R.string.customer_sex_female);
                case 1:
                    return name + context.getString(R.string.customer_sex_male);
                default:
                    return name + context.getString(R.string.customer_sex_male);
            }
        }
        return name;
    }

    //拼接会员显示状态
    public static String getCustomerStatus(Context context, int arriveStatus) {
        if (context == null) {
            return " ";
        }
        switch (arriveStatus) {
            case 3:
            case 2:
                return context.getString(R.string.booking_arrived);
            case 4:
                return context.getString(R.string.booking_leave);
            case 1:
                return context.getString(R.string.booking_beyond);
            default:
                return context.getString(R.string.booking_arrived);
        }
    }

    public static void updateCustomerArrivalInfo(Context context, CustomerArrivalShop customerArrivalShop, CustomerInfoViewHolder viewHolder) {
        if (customerArrivalShop != null && viewHolder != null) {
            if (viewHolder.levelTV != null) {//等级
                StringBuilder sbLevel = new StringBuilder();
                if (!TextUtils.isEmpty(customerArrivalShop.levelName)) {
                    sbLevel.append(customerArrivalShop.levelName);
                }
                if (customerArrivalShop.level != null) {
                    sbLevel.append("/");
                    sbLevel.append(customerArrivalShop.level);
                    sbLevel.append(context.getResources().getString(R.string.level));
                }
                viewHolder.levelTV.setText(sbLevel.toString());
            }

            if (viewHolder.statusTV != null) {//到店状态
                viewHolder.statusTV.setText(getCustomerStatus(context, customerArrivalShop.arrivalStatus));
            }
            if (viewHolder.nameTV != null) {
                viewHolder.nameTV.setText(getCustomerName(context, customerArrivalShop));
            }

            if (viewHolder.phoneTV != null) {//手机号
                viewHolder.phoneTV.setText(TextUtils.isEmpty(customerArrivalShop.customerPhone) ? " " : customerArrivalShop.customerPhone);
            }
            if (viewHolder.arriveTypeTV != null) {//到店方式
                ArriveWay arriveWay = customerArrivalShop.getArrivalWay();
                if (arriveWay != null) {
                    viewHolder.arriveTypeTV.setText(arriveWay.getName() + "");
                } else {
                    viewHolder.arriveTypeTV.setText(" ");
                }
            }
            if (viewHolder.tableTV != null) {//桌台号
                viewHolder.tableTV.setText(TextUtils.isEmpty(customerArrivalShop.tableName) ? " " : customerArrivalShop.tableName);
            }
            if (viewHolder.balanceTV != null) {//会员余额
                viewHolder.balanceTV.setText(customerArrivalShop.remainValue == null ? " " : customerArrivalShop.remainValue.toString());
            }
            if (viewHolder.integralTV != null) {//会员积分
                viewHolder.integralTV.setText(customerArrivalShop.integral == null ? " " : customerArrivalShop.integral + "");
            }

            if (viewHolder.couponCountTV != null) {//发券数
                viewHolder.couponCountTV.setText(customerArrivalShop.sendCouponCount + context.getResources().getString(R.string.sheet));
            }
            //如果是人脸识别方式，要去服务器查询余额积分
            if (customerArrivalShop.getArrivalWay() == ArriveWay.FACE_RECOGNITION) {
                findCustomerInfoById(customerArrivalShop.customerId, viewHolder);
            }
        }
    }


    public static void asyncFindCustomer(Activity context, final AsyncFindCustomerCallback callback) {
        TaskContext.bindExecute(context, new SimpleAsyncTask<List<CustomerArrivalShop>>() {
            @Override
            public List<CustomerArrivalShop> doInBackground(Void... params) {
                CustomerArrivalShopDal dal = OperatesFactory.create(CustomerArrivalShopDal.class);
                return dal.findNotOperated();
            }

            @Override
            public void onPostExecute(List<CustomerArrivalShop> customerArrivalShops) {
                callback.onFindData(customerArrivalShops);
            }
        });
    }

    public static void asyncFindCustomer(Fragment context, final AsyncFindCustomerCallback callback) {
        TaskContext.bindExecute(context, new SimpleAsyncTask<List<CustomerArrivalShop>>() {
            @Override
            public List<CustomerArrivalShop> doInBackground(Void... params) {
                CustomerArrivalShopDal dal = OperatesFactory.create(CustomerArrivalShopDal.class);
                return dal.findNotOperated();
            }

            @Override
            public void onPostExecute(List<CustomerArrivalShop> customerArrivalShops) {
                callback.onFindData(customerArrivalShops);
            }
        });
    }

    private static void findCustomerInfoById(Long customerId, final CustomerInfoViewHolder viewHolder) {
        if (customerId == null || viewHolder == null) {
            return;
        }
        ResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp> listener = new ResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp>() {
            @Override
            public void onResponse(ResponseObject<com.zhongmei.bty.basemodule.customer.message.CustomerResp> response) {
                if (ResponseObject.isOk(response)) {
                    CustomerResp customer = response.getContent().result;
                    if (customer != null) {
                        try {
                            if (viewHolder.balanceTV != null) {//会员余额
                                viewHolder.balanceTV.setText(customer.remainValue == null ? " " : customer.remainValue.toString());
                            }
                            if (viewHolder.integralTV != null) {//会员积分
                                viewHolder.integralTV.setText(customer.integral == null ? " " : customer.integral + "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        ToastUtil.showShortToast(response.getContent().errorMessage);
                    }
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.getCustomerById(customerId, false, listener);
    }

    public interface AsyncFindCustomerCallback {
        void onFindData(List<CustomerArrivalShop> customerArrivalShops);
    }

    public static class CustomerInfoViewHolder {
        public TextView statusTV;//状态
        public TextView nameTV;//姓名
        public TextView levelTV;//等级
        public TextView phoneTV;//手机号
        public TextView arriveTypeTV;//到店方式
        public TextView tableTV;//桌台名称
        public TextView balanceTV;//会员余额
        public TextView integralTV;//会员积分
        public TextView tradeTimeTV;//消费次数
        public TextView tradeAmountTV;//消费金额
        public TextView couponCountTV;//券数
    }
}
