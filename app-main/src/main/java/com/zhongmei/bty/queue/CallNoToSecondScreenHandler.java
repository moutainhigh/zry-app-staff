package com.zhongmei.bty.queue;

/**
 * Created by demo on 2018/12/15
 */

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

/**
 * 用延迟时间通知第二屏取消显示正在叫的号
 */
public class CallNoToSecondScreenHandler extends Handler {
    //让第二屏叫的号消失时的what
    public final static int disappearNoWhat = 1;

    FragmentActivity fragmentActivity;

    public CallNoToSecondScreenHandler(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.obj != null) {
            //DisplayServiceManager.doShowQueueCallNo(fragmentActivity, (String) msg.obj, DisplayOnCallNo.DISAPPEAR);
        }
    }
}
