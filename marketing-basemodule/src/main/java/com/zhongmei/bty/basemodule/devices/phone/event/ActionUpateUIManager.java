package com.zhongmei.bty.basemodule.devices.phone.event;


/**
 * 此类更新UI 动作操作总线，如需要在后台更新UI线程请使用该类
 *
 * @Date：2015-4-17 上午10:24:59
 * @Description: TODO
 * @Version: 1.0
 */
public class ActionUpateUIManager {
    public final static String BluetoothPhoneSettingFragmentUI = "BluetoothPhoneSettingFragmentUI";
    public final static String PhonesceneActivity = "PhonesceneActivity";
    public final static String CalllogFragment = "CalllogFragment";
    //需要更新的UI类 fragment
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ActionUpateUIManager() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ActionUpateUIManager(String action) {
        super();
        this.action = action;
    }


}
