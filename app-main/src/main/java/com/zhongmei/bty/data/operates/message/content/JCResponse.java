package com.zhongmei.bty.data.operates.message.content;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;

/**
 * 描述：金城返回
 *
 * @version v7.16 金城
 * @since 2017/8/26
 */

public class JCResponse<T> extends GatewayTransferResp<JsonObject> {

    public T getResult(Class<T> clazz) {
        if (result == null) {
            return null;
        }
        String json = result.toString();
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return new GsonBuilder().create().fromJson(json, clazz);
    }
}