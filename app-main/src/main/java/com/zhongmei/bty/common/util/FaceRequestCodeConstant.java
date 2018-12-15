package com.zhongmei.bty.common.util;

/**
 * 人脸识别 startActivityForResult 中的requestCode
 * <p>
 * 自己定义自己的
 * <p>
 * 用于人脸回调是 onActivityResult 接受数据
 * <p>
 * Created by demo on 2018/12/15
 */
public class FaceRequestCodeConstant {

    /**
     * 支付界面储值 人脸登录 调用face
     */
    public final static int RC_PAY_LOGIN = 0x001;
    /**
     * 登录弹出框 调用face
     */
    public final static int RC_DINNER_CUSTOMER_LOGIN = 0x002;
    /**
     * 详情绑定人脸 调用face
     */
    public final static int RC_CUSTOMER_DETAIL_BIND = 0x003;
    /**
     * 创建会员注册人脸 调用face
     */
    public final static int RC_CUSTOMER_CREATE_REGIEST = 0x004;
    /**
     * 登录弹框注册人脸 调用face
     */
    public final static int RC_DINNER_CUSTOMER_REGIEST = 0x005;
    /**
     * 结算界面 人脸绑定 调用face
     */
    public final static int RC_PAY_BIND_FACE = 0x006;

}
