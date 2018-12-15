package com.zhongmei.yunfu.context.skin;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

public final class CalmSkinDelegate {

    public static int getResId(Context context, int defaultResId) {
        int retRedId = defaultResId;
        if (Build.MODEL.startsWith("HY")) {//适配红云设备
            Resources resource = context.getResources();
            String pkgName = resource.getResourcePackageName(defaultResId);
            String typeName = resource.getResourceTypeName(defaultResId);
            String entryName = resource.getResourceEntryName(defaultResId);
            String hyName = "hy_" + entryName;//可适配资源以hy_开头
            retRedId = resource.getIdentifier(hyName, typeName, pkgName);
            if (retRedId <= 0) {//未适配的资源采用默认
                retRedId = defaultResId;
            }
        }
        return retRedId;
    }
}
