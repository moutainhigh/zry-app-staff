package com.zhongmei.yunfu;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.zhongmei.OSLog;
import com.zhongmei.beauty.operates.BeautyOperateData;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.bty.basemodule.notifycenter.manager.BatteryReceiverManager;
import com.zhongmei.bty.basemodule.session.support.IAuthUserProxy;
import com.zhongmei.bty.basemodule.session.support.TradeDeleter;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.data.db.local.LocalDBHelperFunc;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.bty.snack.offline.SnackImpl;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.AppUtils;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.http.RequestObject;
import com.zhongmei.yunfu.http.RequestObjectObservable;
import com.zhongmei.yunfu.init.sync.SyncReceiver;
import com.zhongmei.yunfu.init.sync.SyncServiceUtil;
import com.zhongmei.yunfu.net.HttpConstant;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DataBaseUtils;
import com.zhongmei.yunfu.orm.IDBHelperFunc;
import com.zhongmei.yunfu.orm.YfDBHelperFunc;

import java.util.HashMap;
import java.util.Map;


public class MainApplication extends BaseApplication {
        private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static MainApplication getInstance() {
        return (MainApplication) sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        long starttime = System.currentTimeMillis();
        super.onCreate();
        setStrictModeEnabled(BuildConfig.DEBUG);
        initOperateFactory();
        BatteryReceiverManager.registerReceiver(this);
        RequestObject.getIntercept().registerObserver(new RequestObjectObservable.InterceptCreateObserver() {
            @Override
            public void onChanged(RequestObject<?> request) {
                AuthLogManager.getInstance().setOpsVersion(request.getOpVersionUUID());
            }
        });
                if (this.getPackageName().equals(AppUtils.getCurProcessName(this))) {
            Log.e("MainApplication", "MainApplication  process:" + AppUtils.getCurProcessName(this));
            OSLog.info("onCreate");

            Snack.init(new SnackImpl(this));

            initSession();
            initLocalInfo();
        }
        Log.e("MainApplication", "MainApplication start times: " + (System.currentTimeMillis() - starttime) + "process:" + AppUtils.getCurProcessName(this));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initOperateFactory() {
        AppBuildConfig.init(BuildConfig.class);
        OperatesFactory.init(BeautyOperateData.sOperataDataMap, BuildConfig.MY_BUILD_TYPE);
        SyncReceiver.registerReceiver(this);
    }

    private static void setStrictModeEnabled(boolean enabled) {
        if (enabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
            }
        }
    }


    public Map<String, String> tokenEncrypt() {
        String shopId = ShopInfoCfg.getInstance().shopId;
        String token = SharedPreferenceUtil.getSpUtil().getString(HttpConstant.TOKENENCRYPT, "");
        return HttpConstant.tokenEncrypt(shopId, token);
    }


    @Override
    protected void initCommonInfo() {
        initDBHelperManager();
        SharedPreferenceUtil.init(this, Constant.SP_FILE_NAME);
        DataBaseUtils.init(Constant.DB_AUTHORITY);
    }

    private void initDBHelperManager() {
        HashMap<String, IDBHelperFunc> databaseHelperMap = new HashMap<>();
        databaseHelperMap.put(DBHelperManager.LOCAL_DATABASE_HELPER, new LocalDBHelperFunc());
        databaseHelperMap.put(DBHelperManager.CALM_DATABSE_HELPER, new YfDBHelperFunc());
        DBHelperManager.initDatabaseHelper(databaseHelperMap);
    }


    @Override
    public void onAppExit() {
        super.onAppExit();
        Session.unbind();
                SyncServiceUtil.stopService(this);
        ShopInfoManager.getInstance().logout();
    }


    private void initSession() {
        Session.registerCallback(new Session.BindListener() {
            @Override
            public void onBind(AuthUser user) {
                IAuthUser.Holder.refresh(user);
                ShopInfoCfg.getInstance().login(new IAuthUserProxy(user.getId(), user.getName()));

                                PaySettingCache.doStop();
                MarketRuleCache.doStop();
                                PaySettingCache.doInit(MainApplication.this);
                MarketRuleCache.doStart();

                                TradeDeleter.deleteIfNecessary();
            }

            @Override
            public void onUnbind(AuthUser user) {
                IAuthUser.Holder.refresh(null);
                ShopInfoCfg.getInstance().logout();
            }
        });
    }
}
