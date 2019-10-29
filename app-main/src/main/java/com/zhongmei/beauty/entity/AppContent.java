package com.zhongmei.beauty.entity;

public class AppContent {
    private String moduleName;
    private String packageName="com.zhongmei.yunfu";
    private String activityClass= "com.zhongmei.beauty.BeautyMainActivity_";
    private int moduleIcon;
    private int pageNo;

    public AppContent(String name,int pageNo,int appIcon){
        this.moduleName=name;
        this.pageNo=pageNo;
        this.moduleIcon=appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(String activityClass) {
        this.activityClass = activityClass;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getModuleIcon() {
        return moduleIcon;
    }

    public void setModuleIcon(int moduleIcon) {
        this.moduleIcon = moduleIcon;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
