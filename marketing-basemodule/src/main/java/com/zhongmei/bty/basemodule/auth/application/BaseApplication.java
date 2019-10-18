package com.zhongmei.bty.basemodule.auth.application;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.util.ImageUtil;

public abstract class BaseApplication {

    private String title;
    private int icon;
    private int lockIcon;
    private int sort;
    private String packageName;
    private String activityName;
    private boolean hasPermission;
    private String hasNewShareKey;
    private Intent intent;

    private String mainPermission;

    private boolean systemApp;

    public BaseApplication() {
        initMainPermission();
        setTitle();
        setIcon();
        setLockIcon();
        setSort();
        setPackageName();
        setActivityName();
        setHasNewShareKey();
    }

    public abstract void initMainPermission();

    public abstract void setTitle();

    public abstract void setIcon();

    public abstract void setLockIcon();

    public abstract void setSort();

    public abstract void setPackageName();

    public abstract void setActivityName();

    public abstract void setHasNewShareKey();



    public boolean isSystemApp() {
        return systemApp;
    }

    public void setSystemApp(boolean systemApp) {
        this.systemApp = systemApp;
    }

    protected void setMainPermission(String permission) {
        mainPermission = permission;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public String getIconURL() {
        return null;
    }

    public Drawable getIconDrawable() {
        Drawable drawable = com.zhongmei.yunfu.context.base.BaseApplication.sInstance.getResources().getDrawable(getIcon());
        if (drawable instanceof BitmapDrawable) {
            Drawable statelistDrawable = getClickStateListDrawable((BitmapDrawable) drawable);
            return statelistDrawable;
        }
        return drawable;
    }

    @NonNull
    public static Drawable getClickStateListDrawable(BitmapDrawable drawable) {

        if (drawable.getBitmap() == null) {
            return drawable;
        }

        return ImageUtil.setBitmap(drawable.getBitmap()).createStateListDrawable().setStatePressedAlpha(0xaa).getDrawable();
    }

    public int getLockIcon() {
        return lockIcon;
    }

    public Drawable getLockIconDrawable() {
        Drawable drawable = com.zhongmei.yunfu.context.base.BaseApplication.sInstance.getResources().getDrawable(getLockIcon());
        if (drawable instanceof BitmapDrawable) {
            Drawable statelistDrawable = getClickStateListDrawable((BitmapDrawable) drawable);
            return statelistDrawable;
        }
        return drawable;
    }

    public int getSort() {
        return sort;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setTitle(int titleResId) {
        setTitle(com.zhongmei.yunfu.context.base.BaseApplication.sInstance.getString(titleResId));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setLockIcon(int lockIcon) {
        this.lockIcon = lockIcon;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
        if (!TextUtils.isEmpty(activityName)) {
            Intent intent = new Intent();
            if (!TextUtils.isEmpty(packageName)) {
                intent.setClassName(packageName, activityName);
            } else {
                intent.setClassName("com.zhongmei.bty", activityName);
            }
            setIntent(intent);
        }
    }


    public void setHasNewShareKey(String hasNewShareKey) {
        this.hasNewShareKey = hasNewShareKey;
    }

    public String getHasNewShareKey() {
        return hasNewShareKey;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }


    public boolean hasPermission() {
                hasPermission = systemApp || TextUtils.isEmpty(mainPermission) || VerifyHelper.verify(mainPermission);
        return hasPermission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseApplication that = (BaseApplication) o;

        if (icon != that.icon) return false;
        if (!title.equals(that.title)) return false;
        return activityName.equals(that.activityName);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + icon;
        result = 31 * result + activityName.hashCode();
        return result;
    }
}
