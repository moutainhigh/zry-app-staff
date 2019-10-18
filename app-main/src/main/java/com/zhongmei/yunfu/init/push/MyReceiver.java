package com.zhongmei.yunfu.init.push;

import android.content.Context;

import com.zhongmei.yunfu.push.JPushReceiver;



public class MyReceiver extends JPushReceiver {

    @Override
    public void onReceivedCustomMessage(Context context, String msgId, String message) {
        super.onReceivedCustomMessage(context, msgId, message);

    }
}
