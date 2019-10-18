package com.zhongmei.beauty.customer.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class BeautyCustomerConstants {

    public static final int UNKNOWN = -999;
    public static final String KEY_CUSTOMER_LOGIN_FLAG = "login_flag";

    public static final String KEY_CUSTOMER_REGIEST_FLAG = "login_flag";

    public static final String KEY_CUSTOMER_ID = "customerId";

    public static final String KEY_CUSTOMER_PAGE_FROM = "from";

    public static final String KEY_CUSTOMER_INFO = "customerNew";

    public static final String KEY_CUSTOMER_EDIT_PAGE = "customer_edit_page";

    public final static String KEY_CUSTOMER = "key_customer";

    @IntDef({CustomerEditLaunchMode.CREATE_CUSTOMER, CustomerEditLaunchMode.EDIT_CUSTOMER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerEditLaunchMode {
        int CREATE_CUSTOMER = 1;         int EDIT_CUSTOMER = 2;    }

    @IntDef({CustomerDetailFrom.DIALOG_TO_DETAIL, CustomerDetailFrom.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerDetailFrom {
        int DIALOG_TO_DETAIL = 2;         int OTHER = 1;    }

    @IntDef({CustomerLoginLaunchMode.RECHARGE, CustomerLoginLaunchMode.LOGIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerLoginLaunchMode {
        int RECHARGE = 2;         int LOGIN = 1;    }

    @IntDef({CustomerReiestLaunchMode.RECHARGE_REGIEST, CustomerReiestLaunchMode.NORMAL_REGIEST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerReiestLaunchMode {
        int RECHARGE_REGIEST = 2;         int NORMAL_REGIEST = 1;    }

    @IntDef({CustomerEditPage.MAIN, CustomerEditPage.DETAIL_DIALOG, CustomerEditPage.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerEditPage {
        int MAIN = 1;         int DETAIL_DIALOG = 2;         int OTHER = 3;    }

    @IntDef({CustomerSearchType.SEARCHTYPE_NOMAL,
            CustomerSearchType.SEARCHTYPE_POSTCARD,
            CustomerSearchType.SEARCHTYPE_SCANCODE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerSearchType {
        int SEARCHTYPE_NOMAL = 0x01;        int SEARCHTYPE_POSTCARD = 0x02;         int SEARCHTYPE_SCANCODE = 0x03;     }

    @IntDef({CustomerPage.UNKONW, CustomerPage.PAGE_CUSTOMER, CustomerPage.PAGE_CARD, CustomerPage.PAGE_ORDER, CustomerPage.PAGE_SHOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerPage {
        int UNKONW = -1;
        int PAGE_CUSTOMER = 1;         int PAGE_CARD = 2;         int PAGE_ORDER = 3;         int PAGE_SHOP = 4;     }

    @IntDef({CustomerStartFlag.CUSTOMER_LIST, CustomerStartFlag.ENTITY_CARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerStartFlag {
        int CUSTOMER_LIST = 1;
        int ENTITY_CARD = 2;         int ORDER_RECORD = 3;     }

}
