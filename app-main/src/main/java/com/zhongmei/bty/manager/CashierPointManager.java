package com.zhongmei.bty.manager;

import com.zhongmei.bty.commonmodule.database.entity.PrinterDevice;
import com.zhongmei.util.NetWorkUtil;
import com.zhongmei.yunfu.MainApplication;

/**
 * 修复收银点打印机信息Manager
 */
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
                //PLog.w(PLog.TAG_KEY, "{info:[修复收银点打印机信息]是否自动激活" + isAutoSet + ",  position:" + TAG + "->modifyCashierPointPrinterDeviceId()}");
                //PrintSettingDal dal = OperatesFactory.create(PrintSettingDal.class);
                String address = NetWorkUtil.getLocalIpv4Address(MainApplication.getInstance());
                PrinterDevice printerDevice;
                Long cashierPointId;
                Long printerDeviceId = null;
                /*try {
                    cashierPointId = dal.getCashierPointId(MainApplication.getInstance().getDeviceIdenty());
                    if (cashierPointId == null) {
                        PLog.w(PLog.TAG_KEY, "{info:[修复收银点打印机信息]是否自动激活" + isAutoSet + ", 该收银点打印机信息已经修复过或者没查询到需要修复的收银点 ,position:" + TAG + "->modifyCashierPointPrinterDeviceId()}");
                        return;
                    }
                    if (!isAutoSet) {
                        printerDevice = dal.findPrinterbyIp(address);
                        if (printerDevice == null || printerDevice.getId() == null) {
                            PLog.w(PLog.TAG_KEY, "{info:[修复收银点打印机信息] 不是自动激活,查找本机打印机失败,不进行修复,直接返回 ,position:" + TAG + "->modifyCashierPointPrinterDeviceId()}");
                            return;
                        }
                        printerDeviceId = printerDevice.getId();
                    }
                } catch (Exception e) {
                    PLog.w(PLog.TAG_KEY, "{info:[修复收银点打印机信息]是否自动激活" + isAutoSet + ", 查询收银点ID或者打印机信息失败,不进行修复,直接返回" + e.getMessage() + " ,position:" + TAG + "->modifyCashierPointPrinterDeviceId()}");
                    return;
                }*/

                /*ResponseListener<TransferListResp<StoreTicketOutletSettingResp>> listener = new ResponseListener<TransferListResp<StoreTicketOutletSettingResp>>() {

                    @Override
                    public void onResponse(ResponseObject<TransferListResp<StoreTicketOutletSettingResp>> response) {
                        if (ResponseObject.isOk(response)) {
                            PLog.w(PLog.TAG_KEY, "{info:[修复收银点打印机信息]是否自动激活" + isAutoSet + ", 成功 ,position:" + TAG + "->modifyCashierPointPrinterDeviceId()}");
                        } else {
                            PLog.w(PLog.TAG_KEY, "{info:[修复收银点打印机信息]是否自动激活" + isAutoSet + ", 失败 ,position:" + TAG + "->modifyCashierPointPrinterDeviceId()}");
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        PLog.w(PLog.TAG_KEY, "{info:[修复收银点打印机信息]是否自动激活" + isAutoSet + ", 失败(超时) " + error.getMessage() + " ,position:" + TAG + "->modifyCashierPointPrinterDeviceId()}");
                    }

                };
                if (isAutoSet) {
                    dal.modifyCashierPointDeviceId(cashierPointId, address, null, listener);
                } else {
                    dal.modifyCashierPointDeviceId(cashierPointId, null, printerDeviceId, listener);
                }*/
            }
        }).start();
    }
}

