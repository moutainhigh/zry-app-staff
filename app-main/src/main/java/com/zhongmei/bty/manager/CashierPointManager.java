package com.zhongmei.bty.manager;

import com.zhongmei.bty.commonmodule.database.entity.PrinterDevice;
import com.zhongmei.util.NetWorkUtil;
import com.zhongmei.yunfu.MainApplication;


public class CashierPointManager {

    private static CashierPointManager instance;
    private static final String TAG = CashierPointManager.class.getName();


    private CashierPointManager() {
    }

    public static synchronized CashierPointManager getInstance() {
        if (instance == null) {
            instance = new CashierPointManager();
        }
        return instance;
    }


    public void modifyCashierPointPrinterDeviceId(final boolean isAutoSet) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                                                String address = NetWorkUtil.getLocalIpv4Address(MainApplication.getInstance());
                PrinterDevice printerDevice;
                Long cashierPointId;
                Long printerDeviceId = null;



            }
        }).start();
    }
}

