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

        public static boolean isOpenCustomerNotice() {
        return SpHelper.getDefault().getBoolean(SpHelper.CUSTOMER_ARRIVE_NOTICE, true);
    }

        public static void callCustomerArrive(Context context, CustomerArrivalShop customer) {
        if (context == null || customer == null) {
            return;
        }
        String content = getCustomerName(context, customer) + getCustomerStatus(context, customer.arrivalStatus);
               QueuePlayServiceManager.playAudition(content);
    }

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
            if (viewHolder.levelTV != null) {                StringBuilder sbLevel = new StringBuilder();
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

            if (viewHolder.statusTV != null) {                viewHolder.statusTV.setText(getCustomerStatus(context, customerArrivalShop.arrivalStatus));
            }
            if (viewHolder.nameTV != null) {
                viewHolder.nameTV.setText(getCustomerName(context, customerArrivalShop));
            }

            if (viewHolder.phoneTV != null) {                viewHolder.phoneTV.setText(TextUtils.isEmpty(customerArrivalShop.customerPhone) ? " " : customerArrivalShop.customerPhone);
            }
            if (viewHolder.arriveTypeTV != null) {                ArriveWay arriveWay = customerArrivalShop.getArrivalWay();
                if (arriveWay != null) {
                    viewHolder.arriveTypeTV.setText(arriveWay.getName() + "");
                } else {
                    viewHolder.arriveTypeTV.setText(" ");
                }
            }
            if (viewHolder.tableTV != null) {                viewHolder.tableTV.setText(TextUtils.isEmpty(customerArrivalShop.tableName) ? " " : customerArrivalShop.tableName);
            }
            if (viewHolder.balanceTV != null) {                viewHolder.balanceTV.setText(customerArrivalShop.remainValue == null ? " " : customerArrivalShop.remainValue.toString());
            }
            if (viewHolder.integralTV != null) {                viewHolder.integralTV.setText(customerArrivalShop.integral == null ? " " : customerArrivalShop.integral + "");
            }

            if (viewHolder.couponCountTV != null) {                viewHolder.couponCountTV.setText(customerArrivalShop.sendCouponCount + context.getResources().getString(R.string.sheet));
            }
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
                            if (viewHolder.balanceTV != null) {                                viewHolder.balanceTV.setText(customer.remainValue == null ? " " : customer.remainValue.toString());
                            }
                            if (viewHolder.integralTV != null) {                                viewHolder.integralTV.setText(customer.integral == null ? " " : customer.integral + "");
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
        public TextView statusTV;        public TextView nameTV;        public TextView levelTV;        public TextView phoneTV;        public TextView arriveTypeTV;        public TextView tableTV;        public TextView balanceTV;        public TextView integralTV;        public TextView tradeTimeTV;        public TextView tradeAmountTV;        public TextView couponCountTV;    }
}
