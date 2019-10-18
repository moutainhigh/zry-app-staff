package com.zhongmei.bty.mobilepay;



public interface IPayConstParame {

    String EXTRA_TRADE_INFO = "trade_info";

    String EXTRA_TRADE_ID = "trade_id";

    String EXTRA_TRADE_VO = "tradeVo";
    String EXTRA_PARTNER_ID = "partner_id";


    String EXTRA_SCAN_TYPE = "scan_type";


    String EXTRA_QUICK_PAY_TYPE = "quick_pay_type";


    String EXTRA_MENU_DISPLAY_TYPE = "menu_display_type";

    String EXTRA_PAY_ACTION_PAGE = "pay_action_page";

    int ONLIEN_SCAN_TYPE_ACTIVE = 0;
    int ONLIEN_SCAN_TYPE_UNACTIVE = 1;

        int MEMBER_SCAN_CARD_LOGIN = 2;
        int MEMBER_SCAN_FACE_LOGIN = 3;
        int MEMBER_GENERATE_QR_CODE = 4;
        int COUPONS_VALIDATE = 5;

    int MENU_DISPLAY_TYPE_NOMAL = 0;
    int MENU_DISPLAY_TYPE_MEMBER_ENABLE = 1;
    int MENU_DISPLAY_TYPE_MEMBER_DISABLE = 2;

    int SERVER_CODE_SCUESS = 0;
    int SERVER_CODE_DEVICE_EMPTY = 1001;
    int SERVER_CODE_TIME_STAMP_EMPTY = 1020;
    int SERVER_CODE_TRADE_EMPTY = 1040;
    int SERVER_CODE_PAY_FAIL = 2000;
    int SERVER_CODE_TRADE_HAVE_REJECTED = 3102;
    int SERVER_CODE_PAY_AMOUNT_EMPTY = 90000;
    int SERVER_CODE_PAY_AMOUNT_ERROR = 90001;
    int SERVER_CODE_PAY_AUTHCODE_EMPTY = 90100;

    int GATWAY_CODE_PASSWORD_EMPTY = 2401;
    int GATWAY_CODE_PASSWORD_ERROR = 2402;
    int GATWAY_CODE_BALANCE_NOT_ENOUGH = 2403;


    int OP_TYPE_DOPAY = 0;
    int OP_TYPE_REFUND = 1;
    int OP_TYPE_LAG = 2;
    int OP_TYPE_BOOKING = 3;
    int OP_TYPE_DEDUCTION = 4;
    int OP_TYPE_DEDUCTION_REFUND = 5;}
