package com.zhongmei.bty.basemodule.session.support;

import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.application.FastFoodApplication;
import com.zhongmei.bty.basemodule.auth.permission.dialog.VerifyPermissionsDialogFragment;
import com.zhongmei.bty.basemodule.auth.permission.dialog.VerifyPermissionsDialogFragment.CloseListener;
import com.zhongmei.bty.basemodule.auth.permission.dialog.VerifyPermissionsDialogFragment_;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.baseservice.util.peony.land.Task;
import com.zhongmei.bty.commonmodule.database.enums.AuthType;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.peony.ArgsUtils;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyHelper {

    private static final String TAG = VerifyHelper.class.getSimpleName();

    public static abstract class Callback {
        public void onPositive(User user, String code, Auth.Filter filter) {
        }

        public void onNegative(String code, Auth.Filter filter) {
        }
    }

    public static void verifyAlert(FragmentActivity activity, String authCode, Callback callback) {
        verify(authCode, new AlertCallback(activity, callback));
    }

    public static void verifyAlert(FragmentActivity activity, String authCode, Auth.Filter filter, Callback callback) {
        verify(authCode, filter, new AlertCallback(activity, callback));
    }

    public static void verify(String authCode, Callback callback) {
        verify(authCode, null, callback);
    }

    public static void verify(String authCode, Auth.Filter filter, Callback callback) {
        CallbackProxy proxy = new CallbackProxy(callback);
        if (verify(authCode, filter)) {
            proxy.onPositive(Session.getAuthUser(), authCode, filter);
        } else {
            proxy.onNegative(authCode, filter);
        }
    }

    public static boolean verify(String authCode) {
        return verifyInner(authCode, null);
    }

    public static boolean verify(String authCode, Auth.Filter filter) {
        return verifyInner(authCode, filter);
    }

    private static boolean verifyInner(String authCode, Auth.Filter filter) {
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null && authCode != null) {
            Auth auth = authUser.getAuth();
            if (auth != null) {
                return auth.hasAuth(authCode, filter);
            }
        }
        return false;
    }

	/*public static void getAlert(FragmentActivity activity, String authCode, Callback callback) {
		get(authCode, new AlertCallback(activity, callback));
	}

	public static void getAlert(FragmentActivity activity, String authCode, Auth.Filter filter, Callback callback) {
		get(authCode, filter, new AlertCallback(activity, callback));
	}*/

    public static void get(String authCode, Callback callback) {
        get(authCode, null, callback);
    }

    public static void get(String authCode, Auth.Filter filter, Callback callback) {
        CallbackProxy proxy = new CallbackProxy(callback);
        proxy.onNegative(authCode, filter);
    }


    private static class AlertCallback extends Callback {

        final Callback callback;
        final FragmentActivity activity;

        AlertCallback(FragmentActivity activity, Callback callback) {
            ArgsUtils.notNull(activity, "FragmentActivity is null");
            this.activity = activity;
            this.callback = callback;
        }

        @Override
        public void onPositive(User user, String code, Auth.Filter filter) {
            super.onPositive(user, code, filter);
            if (callback != null) {
                callback.onPositive(user, code, filter);
            }
        }

        @Override
        public void onNegative(String code, Auth.Filter filter) {
            super.onNegative(code, filter);
            Session.getFunc(UserFunc.class).getUsers(code, filter, new InnerCallback(activity, code, filter, callback));
        }

        private static class InnerCallback implements Task.Callback<List<User>> {

            final FragmentActivity activity;
            final String code;
            final Auth.Filter filter;
            final Callback callback;

            InnerCallback(FragmentActivity activity, String code, Auth.Filter filter, Callback callback) {
                this.activity = activity;
                this.code = code;
                this.filter = filter;
                this.callback = callback;
            }

            @Override
            public void callback(List<User> users) {
                if (ArgsUtils.isEmpty(users)) {
                    ToastUtil.showShortToast(R.string.no_user_has_permission);
                    callback.onNegative(code, filter);
                } else {
                    VerifyPermissionsDialogFragment alertDialog = new VerifyPermissionsDialogFragment_();
                    alertDialog.setSpinnerData(users);
                    alertDialog.setTag(code);
                    alertDialog.setCloseListener(new InnerCloseListener(filter, callback));
                    alertDialog.setListener(new InnerPermissionVerify(filter, callback));
                    alertDialog.show(activity.getSupportFragmentManager(), code);
                }
            }
        }

        private static class InnerPermissionVerify implements VerifyPermissionsDialogFragment.PermissionVerify2 {

            final Auth.Filter filter;
            final Callback callback;

            InnerPermissionVerify(Auth.Filter filter, Callback callback) {
                this.filter = filter;
                this.callback = callback;
            }

            @Override
            public void verify(String permission, boolean success) {/*empty*/}

            @Override
            public void verify(User user, String permission, boolean success) {
                if (callback != null) {
                    if (success) {
                        CallbackProxy.makeAuthLog(user, permission);
                        callback.onPositive(user, permission, filter);
                    } else {
                        callback.onNegative(permission, filter);
                    }
                }
            }
        }

        private static class InnerCloseListener implements CloseListener {

            final Auth.Filter filter;
            final Callback callback;

            InnerCloseListener(Auth.Filter filter, Callback callback) {
                this.filter = filter;
                this.callback = callback;
            }

            @Override
            public void close(String permission) {
                if (callback != null) {
                    callback.onNegative(permission, filter);
                }
            }
        }
    }

    private static class CallbackProxy extends Callback {

        private static Map<String, AuthType> map = new HashMap<>();

        static {
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_CASH, AuthType.TYPE_CHECK_OUT);
            map.put(DinnerApplication.PERMISSION_DINNER_ACCEPT, AuthType.TYPE_ACCEPT_REJECT);
            map.put(DinnerApplication.PERMISSION_DINNER_INVALID, AuthType.TYPE_CANCEL_ORDER);
            map.put(DinnerApplication.PERMISSION_DINNER_CREATE, AuthType.TYPE_START_DESK);
            map.put(DinnerApplication.PERMISSION_DINNER_MERGE, AuthType.TYPE_MERGE_TRADE);
            map.put(DinnerApplication.PERMISSION_DINNER_REPORT_FORM, AuthType.TYPE_REPORT_FORM);
            map.put(DinnerApplication.PERMISSION_DINNER_TRANFER, AuthType.TYPE_TRANSFER_DESK);
            map.put(DinnerApplication.PERMISSION_SELECT_WAITER, AuthType.TYPE_MODIFY_WAITER);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_HANDOVER_CALIBRATE, AuthType.TYPE_FAST_HANDOVER_CALIBRATE);
            map.put(DinnerApplication.PERMISSION_DINNER_HANDOVER_CALIBRATE, AuthType.TYPE_DINNER_HANDOVER_CALIBRATE);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_HANDOVER, AuthType.TYPE_FAST_HANDOVER);
            map.put(DinnerApplication.PERMISSION_DINNER_HANDOVER, AuthType.TYPE_DINNER_HANDOVER);
            map.put(DinnerApplication.PERMISSION_DINNER_HANDOVER_LAST, AuthType.TYPE_DINNER_HANDOVER_LAST);
            map.put(DinnerApplication.PERMISSION_DINNER_REPAY, AuthType.TYPE_ANIT_SETTLEMENT);
            map.put(DinnerApplication.PERMISSION_DINNER_REFUND, AuthType.TYPE_RETURN);
            map.put(DinnerApplication.PERMISSION_DINNER_CASH, AuthType.TYPE_CHECK_OUT);
            map.put(DinnerApplication.PERMISSION_DINNER_TAKEOUT_CLEAR, AuthType.TYPE_CLEAR_BALANCE);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_REFUND, AuthType.TYPE_RETURN);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_ACCEPT, AuthType.TYPE_ACCEPT_REJECT);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_REPAY, AuthType.TYPE_ANIT_SETTLEMENT);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_INVALID, AuthType.TYPE_CANCEL_ORDER);
            map.put(CustomerApplication.PERMISSION_MEMBER_INTEGRAL_MODIFY, AuthType.TYPE_MEMBER_INTEGRAL_MODIFY);
            map.put(BeautyApplication.PERMISSION_CUSTOMER_LOGIN, AuthType.TYPE_DINNER_CUSTOMER_LOGIN);
            map.put(DinnerApplication.PERMISSION_DINNER_PRIVILEDGE_BANQUET, AuthType.TYPE_PRIVILIGE_BANQUET);
            map.put(DinnerApplication.PERMISSION_DINNER_PRIVILEDGE_FREE, AuthType.TYPE_PRIVILIGE_FREE);
            map.put(DinnerApplication.PERMISSION_DINNER_PRIVILEDGE_PRESENT, AuthType.TYPE_PRIVILIGE_PRESENT);
            map.put(DinnerApplication.PERMISSION_DINNER_SPLIT, AuthType.TYPE_SPLIT);
            map.put(DinnerApplication.PERMISSION_DINNER_PRIVILEDGE, AuthType.TYPE_PRIVILEGE);
            map.put(DinnerApplication.PERMISSION_DINNER_CLOSING, AuthType.TYPE_CLOSING);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_CLOSING, AuthType.TYPE_CLOSING);
            map.put(DinnerApplication.PERMISSION_DINNER_CLOSING_HISTORY, AuthType.TYPE_CLOSING_HISTORY);
            map.put(DinnerApplication.PERMISSION_DINNER_MODIFY_PRICE, AuthType.TYPE_DINNER_MODIFY_PRICE);
            map.put(DinnerApplication.PERMISSION_DINNER_QUANTITY, AuthType.TYPE_DINNER_EDIT_WEIGHT);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_PRIVILEDGE_FREE, AuthType.TYPE_PRIVILIGE_FREE);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_PRIVILEDGE_PRESENT, AuthType.TYPE_PRIVILIGE_PRESENT);
            map.put(FastFoodApplication.PERMISSION_PAYMENTS_EDIT, AuthType.TYPE_PAYMENTS_EDIT);
            map.put(DinnerApplication.PERMISSION_PAYMENTS_EDIT, AuthType.TYPE_PAYMENTS_EDIT);
            map.put(DinnerApplication.PERMISSION_DINNER_BUSINESS_CHARGE, AuthType.TYPE_DINNER_BUSINESS_CHARGE);
            map.put(DinnerApplication.PERMISSION_DINNER_RETURN_GOODS, AuthType.TYPE_RETURN_GOODS);
            map.put(DinnerApplication.PERMISSION_DINNER_CREDIT, AuthType.TYPE_DINNER_CREDIT);
            map.put(DinnerApplication.PERMISSION_DINNER_MALING, AuthType.TYPE_MALING);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_MALING, AuthType.TYPE_MALING);
            map.put(DinnerApplication.PERMISSION_DINNER_AUTO_MALING, AuthType.TYPE_MALING);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_AUTO_MALING, AuthType.TYPE_MALING);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_REPORT_FORM, AuthType.TYPE_REPORT_FORM);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_CREATE, AuthType.TYPE_FASTFOOD_CREATE);
            map.put(DinnerApplication.PERMISSION_DINNER_PREREPRINT_SETTING, AuthType.TYPE_PREPRINT_SETTING);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_HANDOVER_LAST, AuthType.TYPE_FAST_HANDOVER_LAST);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_CLOSING_HISTORY, AuthType.TYPE_CLOSING_HISTORY);
            map.put(DinnerApplication.PERMISSION_DINNER_PRIVILEDGE_DISCOUNT, AuthType.TYPE_PRIVILEGE_DISCOUNT);
            map.put(DinnerApplication.PERMISSION_DINNER_PRIVILEDGE_REBATE, AuthType.TYPE_PRIVILEGE_REBETE);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_PRIVILEDGE_DISCOUNT, AuthType.TYPE_PRIVILEGE_DISCOUNT);
            map.put(FastFoodApplication.PERMISSION_FASTFOOD_PRIVILEDGE_REBATE, AuthType.TYPE_PRIVILEGE_REBETE);

            //美业权限
            map.put(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE, AuthType.TYPE_PRIVILEGE);
            map.put(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_BANQUET, AuthType.TYPE_PRIVILIGE_BANQUET);
            map.put(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_FREE, AuthType.TYPE_PRIVILIGE_FREE);
            map.put(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_PRESENT, AuthType.TYPE_PRIVILIGE_PRESENT);
            map.put(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT, AuthType.TYPE_PRIVILEGE_DISCOUNT);
            map.put(BeautyApplication.PERMISSION_BEAUTY_PRIVILEDGE_REBATE, AuthType.TYPE_PRIVILEGE_REBETE);
            map.put(BeautyApplication.PERMISSION_BEAUTY_MALING, AuthType.TYPE_MALING);
            map.put(BeautyApplication.PERMISSION_BEAUTY_AUTO_MALING, AuthType.TYPE_MALING);
            map.put(BeautyApplication.PERMISSION_BEAUTY_CASH, AuthType.TYPE_CHECK_OUT);

            //员工管理

        }


        final Callback callback;

        CallbackProxy(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onPositive(User user, String code, Auth.Filter filter) {
            super.onPositive(user, code, filter);
            makeAuthLog(user, code);
            if (callback != null) {
                callback.onPositive(user, code, filter);
            }
        }

        @Override
        public void onNegative(String code, Auth.Filter filter) {
            super.onNegative(code, filter);
            if (callback != null) {
                callback.onNegative(code, filter);
            }
        }

        private static void makeAuthLog(User user, String code) {
            AuthType authType = map.get(code);
            if (authType != null) {
                String desc = authType.getDesc();
                AuthLogManager.getInstance().putAuthLog(authType, desc, user);
            }
        }
    }

    //	------extends-------
    public static Auth.Filter createMax(String name, BigDecimal value) {
        return new InnerFilter(InnerFilter.FLAG_MAX, name, value);
    }

    public static Auth.Filter createMin(String name, BigDecimal value) {
        return new InnerFilter(InnerFilter.FLAG_MIN, name, value);
    }

    private static class InnerFilter implements Auth.Filter {

        static final int FLAG_MAX = 1;
        static final int FLAG_MIN = 2;

        final int flag;
        final String name;
        final BigDecimal value;

        InnerFilter(int flag, String name, BigDecimal value) {
            this.flag = flag;
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean access(String targetName, String targetValue) {
            try {
                BigDecimal valueB = value;
                BigDecimal targetValueB = new BigDecimal(targetValue);
                if (name.equals(targetName)) {
                    if (flag == FLAG_MAX) return valueB.compareTo(targetValueB) <= 0;
                    if (flag == FLAG_MIN) return valueB.compareTo(targetValueB) >= 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
