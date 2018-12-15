package com.zhongmei.bty.common.util;

import android.app.Activity;

import com.zhongmei.bty.dinner.DinnerMainActivity;
import com.zhongmei.bty.dinner.DinnerPayActivity;
import com.zhongmei.bty.dinner.DinnerPayActivity_;
import com.zhongmei.bty.dinner.orderdish.DinnerOrderDishMainActivity;

/**
 * Created by demo on 2018/12/15
 */

public class CalmActivityUtil {
    /**
     * //	 * activity是不是正餐相关的activity
     * //	 * @param activity
     * //	 * @return
     * //
     */
    public static boolean isDinnerActivity(Activity activity) {
        return activity instanceof DinnerMainActivity || activity instanceof DinnerOrderDishMainActivity
                || activity instanceof DinnerPayActivity;
    }

    /**
     * activity是不是正餐相关的activity
     *
     * @param activityClazz
     * @return
     */
    public static boolean isDinnerActivity(Class activityClazz) {
        return activityClazz != null && (activityClazz.equals(DinnerMainActivity.class)
                || activityClazz.equals(DinnerOrderDishMainActivity.class)
                || activityClazz.equals(DinnerPayActivity_.class)
                //|| activityClazz.equals(BuffetMainActivity.class)
                //|| activityClazz.equals(BuffetOrderDishMainActivity.class)
        );
    }

    /**
     * activity是不是正餐和其他模块共用的类型（目前收银相关的几个dialog是共用的）
     *
     * @param activityClazz
     * @returno
     */
    public static boolean isDinnerShareActivity(Class activityClazz) {
        return false;
    }
}
