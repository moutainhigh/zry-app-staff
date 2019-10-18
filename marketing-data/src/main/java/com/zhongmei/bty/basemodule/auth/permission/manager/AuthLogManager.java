package com.zhongmei.bty.basemodule.auth.permission.manager;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.auth.permission.operates.AuthLogDal;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.bty.basemodule.auth.permission.message.AuthLogResp;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.AuthType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;



public class AuthLogManager {

    private static AuthLogManager authLogManager = null;
    private Map<AuthType, AuthorizedLog> authLogMap = new HashMap<AuthType, AuthorizedLog>();
    private AtomicInteger lock = new AtomicInteger();
        private String opsVersionUuid;

    public static AuthLogManager getInstance() {
        if (authLogManager == null) {
            authLogManager = new AuthLogManager();
        }
        return authLogManager;
    }


    public synchronized void putAuthLog(AuthType authType, String authDesc, User user) {
        if (user == null || authType == null) {
            return;
        }
        AuthorizedLog authLog = authLogMap.get(authType);
        if (authLog == null) {
            authLog = new AuthorizedLog();
            authLog.setUuid(SystemUtils.genOnlyIdentifier());
            synchronized (lock) {
                authLogMap.put(authType, authLog);
            }
        }
        authLog.setStatusFlag(StatusFlag.VALID);
        authLog.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        authLog.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        authLog.setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
        authLog.setAuthType(authType);
        authLog.setAuthTypeDesc(authDesc);
        authLog.setAuthUserId(user.getId());
        authLog.setAuthUserName(user.getName());
        authLog.setAuthTime(System.currentTimeMillis());
        authLog.setChanged(true);
    }


    public void removeAuthLog(AuthType authType) {
        synchronized (lock) {
            authLogMap.remove(authType);
        }
    }

    public AuthorizedLog get(AuthType authType) {
        return authLogMap.get(authType);
    }

    public void setOpsVersion(String versionUuid) {
        if (authLogMap.isEmpty()) {
            return;
        }
        this.opsVersionUuid = versionUuid;
    }


    public synchronized void flush(final OrderActionEnum orderAction, final Long tradeId, final String tradeUuid, final Long updateTime) {
        if (authLogMap.isEmpty() || TextUtils.isEmpty(opsVersionUuid)) {
            return;
        }
                final List<AuthorizedLog> authorizedLogList = new ArrayList<AuthorizedLog>();
        authorizedLogList.addAll(authLogMap.values());
        synchronized (lock) {
            authLogMap.clear();
        }
        final String newopsVersionUuid = opsVersionUuid;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final AuthUser loginUser = Session.getAuthUser();
                if (loginUser == null) {
                    return;
                }
                for (AuthorizedLog authorizedLog : authorizedLogList) {
                    authorizedLog.setClientCreateTime(System.currentTimeMillis());
                    authorizedLog.setClientUpdateTime(updateTime);
                    authorizedLog.setOrderType(orderAction);
                    authorizedLog.setTradeId(tradeId);
                    authorizedLog.setTradeUuid(tradeUuid);
                    authorizedLog.setChanged(true);
                    authorizedLog.setVersionUuid(newopsVersionUuid);
                    authorizedLog.setOperateId(loginUser.getId());
                    authorizedLog.setOperateName(loginUser.getName());
                }
                saveToDatabase(authorizedLogList);
            }
        }).start();
    }



    public synchronized void flushNoQuest(final OrderActionEnum orderAction, final Long tradeId, final String tradeUuid, final Long updateTime) {
        setOpsVersion(SystemUtils.genOnlyIdentifier());
        flush(orderAction, tradeId, tradeUuid, updateTime);
        opsVersionUuid = null;
    }


    public void saveToDatabase(List<AuthorizedLog> authorizedLogList) {
        AuthLogDal dal = OperatesFactory.create(AuthLogDal.class);
        dal.batchSave(authorizedLogList);
    }


    public void deleteLogs(final List<AuthorizedLog> authLogList) {
        if (authLogList == null || authLogList.isEmpty()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                AuthLogDal dal = OperatesFactory.create(AuthLogDal.class);
                dal.batchDelete(authLogList);
            }
        }).start();
    }

    public void deleteLogs(AuthLogResp resp) {
        if (resp == null || resp.getAuthorizedLogs() == null) {
            return;
        }
        deleteLogs(resp.getAuthorizedLogs());
    }


    public List<AuthorizedLog> queryList(long count) {
        AuthLogDal dal = OperatesFactory.create(AuthLogDal.class);
        return dal.queryList(count);
    }


    public void clear() {
        synchronized (lock) {
            authLogMap.clear();
        }
        opsVersionUuid = null;
    }


    public void destory() {
        clear();
        authLogManager = null;
    }
}
