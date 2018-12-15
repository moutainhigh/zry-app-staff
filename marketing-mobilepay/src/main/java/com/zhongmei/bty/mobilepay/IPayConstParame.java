package com.zhongmei.bty.mobilepay;

/**
 * Created by demo on 2018/12/15
 */

public interface IPayConstParame {
    /*  third flatform trade info Parame*/
    String EXTRA_TRADE_INFO = "trade_info";

    String EXTRA_TRADE_ID = "trade_id";

    String EXTRA_TRADE_VO = "tradeVo";//add v8.10.1

    String EXTRA_PARTNER_ID = "partner_id";

    /*  online pay scan type Parame */
    String EXTRA_SCAN_TYPE = "scan_type";

    /*  快捷支付 Parame*/
    String EXTRA_QUICK_PAY_TYPE = "quick_pay_type";

    /* 储值支付显示 Params */
    String EXTRA_MENU_DISPLAY_TYPE = "menu_display_type";

    String EXTRA_PAY_ACTION_PAGE = "pay_action_page";

    int ONLIEN_SCAN_TYPE_ACTIVE = 0;//扫码枪主扫

    int ONLIEN_SCAN_TYPE_UNACTIVE = 1;//二维码被扫


    // v8.6.0 快捷支付 会员扫卡登录
    int MEMBER_SCAN_CARD_LOGIN = 2;
    // v8.6.0 快捷支付 会员扫脸登录
    int MEMBER_SCAN_FACE_LOGIN = 3;
    // v8.6.0 快捷支付 储值生成二维码
    int MEMBER_GENERATE_QR_CODE = 4;
    // v8.6.0 快捷支付 糯米验券
    int COUPONS_VALIDATE = 5;

    int MENU_DISPLAY_TYPE_NOMAL = 0; // 全部可用

    int MENU_DISPLAY_TYPE_MEMBER_ENABLE = 1; // 只有储值可用

    int MENU_DISPLAY_TYPE_MEMBER_DISABLE = 2; // 除了储值都可用

    /* 服务器返回支付code 码 */
    int SERVER_CODE_SCUESS = 0;//成功

    int SERVER_CODE_DEVICE_EMPTY = 1001;//设备为空

    int SERVER_CODE_TIME_STAMP_EMPTY = 1020;//时间戳为空

    int SERVER_CODE_TRADE_EMPTY = 1040;//订单为空

    int SERVER_CODE_PAY_FAIL = 2000;//支付失败

    int SERVER_CODE_TRADE_HAVE_REJECTED = 3102;//已经退货

    int SERVER_CODE_PAY_AMOUNT_EMPTY = 90000;//支付金额为空

    int SERVER_CODE_PAY_AMOUNT_ERROR = 90001;//支付金额验证失败

    int SERVER_CODE_PAY_AUTHCODE_EMPTY = 90100;//付款码为空


    int GATWAY_CODE_PASSWORD_EMPTY = 2401;//密码为空

    int GATWAY_CODE_PASSWORD_ERROR = 2402;//密码出错

    int GATWAY_CODE_BALANCE_NOT_ENOUGH = 2403;//余额不足


    /*支付成功操作类别*/
    int OP_TYPE_DOPAY = 0;//收银，

    int OP_TYPE_REFUND = 1;//退货

    int OP_TYPE_LAG = 2;//挂账,

    int OP_TYPE_BOOKING = 3;//预定成功

    int OP_TYPE_DEDUCTION = 4;//抵扣成功

    int OP_TYPE_DEDUCTION_REFUND = 5;//抵扣退款成功
}
