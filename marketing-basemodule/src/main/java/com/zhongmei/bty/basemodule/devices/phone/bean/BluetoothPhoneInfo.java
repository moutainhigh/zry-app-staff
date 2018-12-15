package com.zhongmei.bty.basemodule.devices.phone.bean;

/**
 * 蓝牙电话基础信息
 *
 * @date 2014-8-6
 */
public class BluetoothPhoneInfo extends CalmPhoneInfo {

    /**
     * 设备名称
     */
    private String name;

    public BluetoothPhoneInfo() {
        super();
    }

    public BluetoothPhoneInfo(String name, String mac, boolean isLastConnect) {
        super(mac, isLastConnect);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
