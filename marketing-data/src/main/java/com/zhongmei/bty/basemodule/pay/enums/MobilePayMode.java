package com.zhongmei.bty.basemodule.pay.enums;

/**
 * Created by demo on 2018/12/15
 * 移动支付模式常量
 */

public interface MobilePayMode {
    String PAY_MODE_SCANCODE = "PAY_MODE_SCANCODE";//被扫支付（生成二维码让顾客扫）
    String PAY_MODE_SCAN_ONE_CODE = "PAY_MODE_SCAN_ONE_CODE";//被扫一码支付
    String PAY_MODE_SHOWCODE = "PAY_MODE_SHOWCODE";//主扫支付（扫码枪扫顾客付款码）
    /*  String PAY_MODE_WXPLATFORM = "PAY_MODE_WXPLATFORM";//公众号支付
      String PAY_MODE_POS = "PAY_MODE_POS";//pos刷卡
      String PAY_MODE_APP = "PAY_MODE_APP";//app支付
      String PAY_MODE_H5 = "PAY_MODE_H5";//H5支付
      String PAY_MODE_JSAPI = "PAY_MODE_JSAPI";//JSAPI支付
      String PAY_MODE_FIX = "PAY_MODE_FIX";//固定二维码支付
      String PAY_MODE_APPLET = "PAY_MODE_APPLET";//小程序支付*/

}
