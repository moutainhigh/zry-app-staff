package com.zhongmei.yunfu.http;

import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.util.List;

/**
 * 实时请求数据的封装类
 *
 * @param <T>
 */
public class RequestObject<T> implements NoProGuard {

    private static final transient RequestObjectObservable mInterceptCreate = new RequestObjectObservable();

    public static RequestObjectObservable getIntercept() {
        return mInterceptCreate;
    }

    private Long brandID;
    private Long shopID;
    private String systemType;
    private String deviceID;
    private String versionCode;
    private String versionName;
    private String appType;
    private String timeZone;
    //每一次请求的操作唯一标识
    private String opVersionUUID;
    public Long userId;
    public String userName;
    public String reqMarker;
    private T content;

    private List<NationInfo> nationInfos;

    public Long getBrandID() {
        return brandID;
    }

    public void setBrandID(Long brandID) {
        this.brandID = brandID;
    }

    public Long getShopID() {
        return shopID;
    }

    public void setShopID(Long shopID) {
        this.shopID = shopID;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getOpVersionUUID() {
        return opVersionUUID;
    }

    public void setOpVersionUUID(String opVersionUUID) {
        this.opVersionUUID = opVersionUUID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static <T> RequestObject<T> create(T content) {
        return create(content, false);
    }

    public static <T> RequestObject<T> create(T content, boolean needReqMarker) {
        RequestObject<T> request = new RequestObject<T>();
        if (content != null &&
                content instanceof IContent) {
            IContent iContent = (IContent) content;
            request.setUserId(iContent.getUserId());
            request.setUserName(iContent.getUserName());
        }
        request.setBrandID(BaseApplication.sInstance.getBrandIdenty());
        request.setShopID(BaseApplication.sInstance.getShopIdenty());
        request.setDeviceID(BaseApplication.sInstance.getDeviceIdenty());
        request.setVersionCode(SystemUtils.getVersionCode());
        request.setVersionName(SystemUtils.getVersionName());
        request.setAppType(SystemUtils.getAppType());
        request.setSystemType(SystemUtils.getSystemType());
        request.setTimeZone(ShopInfoCfg.getInstance().timeZone);
        String opsUuid = SystemUtils.genOnlyIdentifier();
        request.setOpVersionUUID(opsUuid);
        //AuthLogManager.getInstance().setOpsVersion(opsUuid);
        if (needReqMarker) {
            request.reqMarker = SystemUtils.genOnlyIdentifier();//幂等重试标签，目前只针对部分接口开放
        }
        request.setContent(content);
        if (content instanceof NationInfoInterface) {
            NationInfoInterface nationInfoInterface = (NationInfoInterface) content;
            request.nationInfos = nationInfoInterface.getNationInfos();
        }

        mInterceptCreate.notifyChanged(request);
        return request;
    }

    public interface IContent {
        Long getUserId();

        String getUserName();
    }
}
