package com.zhongmei;

import android.content.Context;
import android.content.Intent;

/**
 * Created by demo on 2018/12/15
 */

public class RouteIntent {

    public static void startLogin(Context context) {
        //LoginActivity.start(getActivity()); //"com.zhongmei.bty"
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.splash.login.LoginActivity_");
        context.startActivity(intent);
    }

    public static void startForumMessage(Context context) {
        //ForumMessageActivity.start(mActivity);
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.thirdplatform.foru.ForumMessageActivity");
        context.startActivity(intent);
    }

    public static void startCalmHome(Context context) {
        /*Intent intent = new Intent(MainApplication.getInstance(), CalmHomeActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getInstance().startActivity(intent);*/
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.splash.calmlauncher.CalmHomeActivity_");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startPhoneCalling(Context context) {
        //PhoneCallingActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP).start();
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.phone.activity.PhoneCallingActivity_");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void startCallingService(Context context) {
        /*Intent intent = new Intent(context, CallingService_.class);
        context.startService(intent);*/
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.phone.app.call.CallingService_");
        context.startActivity(intent);
    }

    static final String GroupOrderDishActivity_ = "com.zhongmei.bty.group.GroupOrderDishActivity_";
}
