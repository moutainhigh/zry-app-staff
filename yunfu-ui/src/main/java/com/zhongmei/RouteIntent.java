package com.zhongmei;

import android.content.Context;
import android.content.Intent;



public class RouteIntent {

    public static void startLogin(Context context) {
                Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.splash.login.LoginActivity_");
        context.startActivity(intent);
    }

    public static void startForumMessage(Context context) {
                Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.thirdplatform.foru.ForumMessageActivity");
        context.startActivity(intent);
    }

    public static void startCalmHome(Context context) {

        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.splash.calmlauncher.CalmHomeActivity_");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startPhoneCalling(Context context) {
                Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.phone.activity.PhoneCallingActivity_");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void startCallingService(Context context) {

        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), "com.zhongmei.bty.phone.app.call.CallingService_");
        context.startActivity(intent);
    }

    static final String GroupOrderDishActivity_ = "com.zhongmei.bty.group.GroupOrderDishActivity_";
}
