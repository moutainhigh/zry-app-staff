package com.zhongmei.bty.commonmodule.util;

import android.content.Context;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.UserActionCode;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * ument数据统计
 * Created by demo on 2018/12/15
 */

public class CalmClickAgent {

    public static void openActivityDurationTrack(boolean enabled) {
        MobclickAgent.openActivityDurationTrack(enabled);
    }

    /**
     * 统计时长开始
     *
     * @param context
     */
    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    /**
     * 统计时长结束
     *
     * @param context
     */
    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    /**
     * 页面访问路径
     *
     * @param context
     * @param tag
     */
    public static void onResumePageStart(Context context, String tag) {
        MobclickAgent.onPageStart(tag);
        MobclickAgent.onResume(context);
    }

    /**
     * 页面访问路径
     *
     * @param context
     * @param tag
     */
    public static void onPausePageEnd(Context context, String tag) {
        MobclickAgent.onPageEnd(tag);
        MobclickAgent.onPause(context);
    }

    public static void onPageStart(String tag) {
        MobclickAgent.onPageStart(tag);
    }

    public static void onPageEnd(String tag) {
        MobclickAgent.onPageEnd(tag);
    }

    public static void onEvent(UserActionCode eventCode) {
        onEvent(BaseApplication.sInstance, eventCode.name());
    }

    /**
     * 统计发生次数
     *
     * @param context
     * @param eventId
     */
    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEventValue(Context context, String tag, int duration) {
        String shopId = ShopInfoCfg.getInstance().shopId;
        //eventCat(shopId, tag, duration);
        if (duration > 500) {
            Map<String, String> map_value = new HashMap<>();
            //map_value.put("tag", tag);
            //map_value.put("shopId", BaseApplication.getInstance().getShopInfo().get(ShopInfo.ID_KEY));
            map_value.put(tag, String.format("%s_%s", shopId, getTimeSecond(duration)));
            MobclickAgent.onEventValue(context, "app_start_duration", map_value, duration);
            //暂时关闭界面启动时间记录，后期自己开发接口上传
        }
    }


    public static int getTimeSecond(int duration) {
        double a = BigDecimal.valueOf(duration).divide(BigDecimal.valueOf(1000)).doubleValue();
        int b = (int) Math.floor(a);
        double c = BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).doubleValue();
        if (c >= 0.8) {
            return b * 1000 + 1000;
        }
        if (c >= 0.3) {
            return b * 1000 + 500;
        }
        return b * 1000;
    }
}
