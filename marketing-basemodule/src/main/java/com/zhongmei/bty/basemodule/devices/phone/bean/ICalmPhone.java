package com.zhongmei.bty.basemodule.devices.phone.bean;

import com.zhongmei.bty.basemodule.devices.phone.event.EventSearchResult;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneException;
import com.zhongmei.bty.basemodule.devices.phone.exception.CalmPhoneStateException;
import com.zhongmei.bty.basemodule.devices.phone.manager.CalmPhoneManager.PhoneType;

/**
 * 所有电话模块可以在外部调用的相关操作<br />
 * 对于一些简单的因为设置带来的异常没有上报, 只做Toast处理, 比如蓝牙未开启等.
 *
 * @date 2014-8-5
 */
public interface ICalmPhone {

    /**
     * 自动连接到上次连接的电话设备<br/>
     * 如果从来没有连接过, 此方法不会有任何效果
     */
    public void autoConnect();

    /**
     * 连接一个外设电话
     *
     * @param calmPhone 由{@link #search()} 方法返回的电话对象, 如果为null则会尝试连接上次连接的设备
     */
    public void connect(CalmPhoneInfo calmPhone);

    /**
     * 拨打电话
     *
     * @param phoneNumber 呼叫号码
     * @throws CalmPhoneStateException  电话机状态处于不能呼叫的状态
     * @throws CalmPhoneException       电话机其他异常
     * @throws IllegalArgumentException 电话号码不合法
     */
    public void dial(String phoneNumber) throws CalmPhoneStateException,
            CalmPhoneException, IllegalArgumentException;

    /**
     * 挂机
     */
    public void hangup();

    /**
     * 断开当前连接, 建议只在全部退出的情况下调用, 其实不调用也不影响</br>
     * 程序过程中调用会造成电话盒无法在不重启进程的情况下重新连接<br/>
     * 应该是厂商提供的jar包有问题, 调用了shutdown后无法重连, 好像把什么东西置空了.
     */
    public void disconnect();

    /**
     * 搜索可用的指定类型的设备, 搜索结果会通过{@link EventSearchResult}返回
     *
     * @param type 电话类型
     */
    public void search(PhoneType type);

}
