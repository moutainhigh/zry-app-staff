package com.zhongmei.beauty.customer.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 常量枚举类
 * <p>
 * Created by demo on 2018/12/15
 */
public class BeautyCustomerConstants {

    public static final int UNKNOWN = -999;//未知类型统一入口

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
        int CREATE_CUSTOMER = 1; // 创建新会员
        int EDIT_CUSTOMER = 2;//编辑会员
    }

    @IntDef({CustomerDetailFrom.DIALOG_TO_DETAIL, CustomerDetailFrom.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerDetailFrom {
        int DIALOG_TO_DETAIL = 2; // dialog详情
        int OTHER = 1;// 普通
    }

    @IntDef({CustomerLoginLaunchMode.RECHARGE, CustomerLoginLaunchMode.LOGIN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerLoginLaunchMode {
        int RECHARGE = 2; // 充值
        int LOGIN = 1;// 登录
    }

    @IntDef({CustomerReiestLaunchMode.RECHARGE_REGIEST, CustomerReiestLaunchMode.NORMAL_REGIEST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerReiestLaunchMode {
        int RECHARGE_REGIEST = 2; // 注册并充值
        int NORMAL_REGIEST = 1;// 普通注册
    }

    @IntDef({CustomerEditPage.MAIN, CustomerEditPage.DETAIL_DIALOG, CustomerEditPage.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerEditPage {
        int MAIN = 1; // 首页
        int DETAIL_DIALOG = 2; // 详情弹框
        int OTHER = 3;// 其他
    }

    @IntDef({CustomerSearchType.SEARCHTYPE_NOMAL,
            CustomerSearchType.SEARCHTYPE_POSTCARD,
            CustomerSearchType.SEARCHTYPE_SCANCODE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerSearchType {
        int SEARCHTYPE_NOMAL = 0x01;//关键字查询
        int SEARCHTYPE_POSTCARD = 0x02; //刷卡查询
        int SEARCHTYPE_SCANCODE = 0x03; //扫码查询
    }

    @IntDef({CustomerPage.UNKONW, CustomerPage.PAGE_CUSTOMER, CustomerPage.PAGE_CARD, CustomerPage.PAGE_ORDER, CustomerPage.PAGE_SHOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerPage {
        int UNKONW = -1;
        int PAGE_CUSTOMER = 1; // 顾客
        int PAGE_CARD = 2; // 实体卡管理
        int PAGE_ORDER = 3; // 订单
        int PAGE_SHOP = 4; // 门店管理
    }

    @IntDef({CustomerStartFlag.CUSTOMER_LIST, CustomerStartFlag.ENTITY_CARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CustomerStartFlag {
        int CUSTOMER_LIST = 1;
        int ENTITY_CARD = 2; // 实体卡
        int ORDER_RECORD = 3; // 订单记录
    }

}
