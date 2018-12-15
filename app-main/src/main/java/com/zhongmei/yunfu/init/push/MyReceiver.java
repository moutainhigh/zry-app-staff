package com.zhongmei.yunfu.init.push;

import android.content.Context;

import com.zhongmei.yunfu.push.JPushReceiver;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends JPushReceiver {

    @Override
    public void onReceivedCustomMessage(Context context, String msgId, String message) {
        super.onReceivedCustomMessage(context, msgId, message);
		/*JSONObject json = new JSONObject(message);
		String api = json.getString("api");
		if (packet.startsWithByHeaderValue("api", "/sync/notify")) {
			String bodyJson = new String(packet.getBody());
			Gson gson = new Gson();
			ModulesBody modulesBody = gson.fromJson(bodyJson, ModulesBody.class);
			SyncService.startService(context, SyncService.SYNC_MODULE_PUSH, modulesBody);
			return;
		}*/
    }
}
