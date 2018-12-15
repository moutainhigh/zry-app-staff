package com.zhongmei.bty.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by demo on 2018/12/15
 */

public class RouteIntent {

    private RouteIntent() {
    }

    public static void startLogin(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_LOGIN);
        context.startActivity(intent);
    }

    public static void startLogin(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_LOGIN);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startLogin(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_LOGIN);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }


    public static void startLuncher(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_LUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startPhoneCall(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_PHONE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void startPhoneCall(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_PHONE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startSnack(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_SNACK);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startDinner(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_DINNER);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startDelivery(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_DELIVERY_SERVICE);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startQueue(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_QUEUE);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startBooking(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_BOOKING);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startCustomer(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_CUSTOMER);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startPhone(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_PHONE);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startBuffet(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_BUFFET);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startGroup(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_GROUP);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startRetail(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_RETAIL);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startBakery(Context context, int flag, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_BAKERY);
        intent.setFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startSettings(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), PathURI.URI_SETTINGS);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void startNewOrderlistenerService(Context context) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClassName(context, "com.zhongmei.bty.queue.service.NewOrderlistenerService");
        context.startService(serviceIntent);
    }

    public static void startTradeDealService(Context context) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClassName(context, "com.zhongmei.bty.cashier.tradedeal.TradeDealService");
        context.startService(serviceIntent);
    }

    public static void startTVClientService(Context context) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClassName(context, "com.zhongmei.bty.miracast.TVClientService");
        context.startService(serviceIntent);
    }

    public static void startCalmHomeActivity(Context context) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClassName(context, "com.zhongmei.bty.splash.calmlauncher.CalmHomeActivity_");
        context.startService(serviceIntent);
    }
}
