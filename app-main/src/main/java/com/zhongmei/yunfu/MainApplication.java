package com.zhongmei.yunfu;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.zhongmei.OSLog;
import com.zhongmei.beauty.operates.BeautyOperateData;
import com.zhongmei.bty.AppDialog;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.bty.basemodule.notifycenter.manager.BatteryReceiverManager;
import com.zhongmei.bty.basemodule.session.support.IAuthUserProxy;
import com.zhongmei.bty.basemodule.session.support.TradeDeleter;
import com.zhongmei.bty.commonmodule.data.operate.BeautyOperatesFactory;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.data.db.local.LocalDBHelperFunc;
import com.zhongmei.bty.data.operates.OperateData;
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

/**
 * @Date：2014年11月4日 上午11:27:38
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MainApplication extends BaseApplication {
    // MainActivty的状态 是否start 0-->stop,1-->start
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    final Handler mHandler = new Handler();

    private AppDialog appDialog;

    public static MainApplication getInstance() {
        return (MainApplication) sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appDialog = new AppDialog(this);
    }

    @Override
    public void onCreate() {
        long starttime = System.currentTimeMillis();
        super.onCreate();
        //UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);//第三个参数为push secret
        setStrictModeEnabled(BuildConfig.DEBUG);
        initOperateFactory();
        BatteryReceiverManager.registerReceiver(this);
        //PrintConfigManager.getInstance().initContext(this);//为打印独立模块初始化Context参数。
        RequestObject.getIntercept().registerObserver(new RequestObjectObservable.InterceptCreateObserver() {
            @Override
            public void onChanged(RequestObject<?> request) {
                AuthLogManager.getInstance().setOpsVersion(request.getOpVersionUUID());
            }
        });
        //如果是主进程(包名和进程名称相同)才做初始化业务 add yutang 20170117
        if (this.getPackageName().equals(AppUtils.getCurProcessName(this))) {
            Log.e("MainApplication", "MainApplication  process:" + AppUtils.getCurProcessName(this));
            OSLog.info("onCreate");

            Snack.init(new SnackImpl(this));

            initSession();
            initLocalInfo();
        }
        Log.e("MainApplication", "MainApplication start times: " + (System.currentTimeMillis() - starttime) + "process:" + AppUtils.getCurProcessName(this));
        // 置入一个不设防的VmPolicy（不设置的话 7.0以上一调用拍照功能就崩溃了）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //DisplaySDK.getInstance().destroy();
    }

    private void initOperateFactory() {
        AppBuildConfig.init(BuildConfig.class);
        OperatesFactory.init(OperateData.sOperataDataMap, BuildConfig.MY_BUILD_TYPE);
        //OperatesRetailFactory.init(OperateRetailData.sOperataDataMap, BuildConfig.MY_BUILD_TYPE);
        BeautyOperatesFactory.init(BeautyOperateData.sOperataDataMap, BuildConfig.MY_BUILD_TYPE);
        //delegateContractOverdue();
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

    /**
     * api安全验证数据信息加密信息设置
     * SHA-256(kry-api-token=登录认证token&kry-api-shop-id=门店ID^512&kry-api-timestamp=客户端时间戳)
     */
    public Map<String, String> tokenEncrypt() {
        String shopId = ShopInfoCfg.getInstance().shopId;
        String token = SharedPreferenceUtil.getSpUtil().getString(HttpConstant.TOKENENCRYPT, "");
        return HttpConstant.tokenEncrypt(shopId, token);
    }

    /**
     * @Title: initSystemUtil
     * @Description: 初始化系统工具相关
     * @Param TODO
     * @Return void 返回类型
     */
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
        //PushServiceManager.stopPushService();
        SyncServiceUtil.stopService(this);
        ShopInfoManager.getInstance().logout();
    }

//    ---------Session

    private void initSession() {
        Session.registerCallback(new Session.BindListener() {
            @Override
            public void onBind(AuthUser user) {
//                刷新IAuthUser
                IAuthUser.Holder.refresh(user);
                ShopInfoCfg.getInstance().login(new IAuthUserProxy(user.getId(), user.getName()));

                // erp设置关闭缓存
                PaySettingCache.doStop();
                MarketRuleCache.doStop();
                // erp设置开启缓存
                PaySettingCache.doInit(MainApplication.this);
                MarketRuleCache.doStart();

                //删除7天的订单记录
                TradeDeleter.deleteIfNecessary();
            }

            @Override
            public void onUnbind(AuthUser user) {
//               把IAuthUser置位空
                IAuthUser.Holder.refresh(null);
                ShopInfoCfg.getInstance().logout();

//                Intent hideIntent = new Intent(Constant.HAVE_NO_LOGINDATA);
//                BaseApplication.sInstance.sendBroadcast(hideIntent);
            }
        });
    }
}
