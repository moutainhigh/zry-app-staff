package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PackageURI;
import com.zhongmei.yunfu.basemodule.R;

public class BeautyApplication extends AuthorizedApplication {
    /**
     * 美业权限  pos:beauty
     */
    public static final String PERMISSION_BEAUTY = "pos:beauty";

    /**
     * 美业收银权限
     */
    public static final String PERMISSION_BEAUTY_CASH = "ORDER_ACCOUNTING";

    /**
     * 美业开单权限
     */
    public static final String PERMISSION_BEAUTY_CREATE_TRADE = "ORDER_CREATE";

    /**
     * 美业开钱箱权限
     */
    public static final String PERMISSION_BEAUTY_MONEYBOX = "pos:bs:moneybox";

    /**
     * 抹零权限
     */
    public static final String PERMISSION_BEAUTY_MALING = "POS_MALING";
    /**
     * 自定义抹零权限
     */
    public static final String PERMISSION_BEAUTY_AUTO_MALING = "POS_AUTOMALING";

    /**
     * 优惠权限
     */
    public static final String PERMISSION_BEAUTY_PRIVILEDGE = "POS_PRIVILEGE";

    /**
     * 打折优惠权限
     */
    public static final String PERMISSION_BEAUTY_PRIVILEDGE_DISCOUNT = "POS_PRIVILEGE_DISCOUNT";

    /**
     * 折让权限
     */
    public static final String PERMISSION_BEAUTY_PRIVILEDGE_REBATE = "POS_PRIVILEGE_REBATE";

    /**
     * 免单权限，针对整单
     */
    public static final String PERMISSION_BEAUTY_PRIVILEDGE_FREE = "POS_PRIVILEGE_FREE";
    /**
     * 赠送权限，针对批量
     */
    public static final String PERMISSION_BEAUTY_PRIVILEDGE_PRESENT = "POS_PRIVILEGE_PRESENT";
    /**
     * 宴请权限
     */
    public static final String PERMISSION_BEAUTY_PRIVILEDGE_BANQUET = "pos:bs:privilege:banquet";

    /**
     * 美业作废权限
     */
    public static final String PERMISSION_BEAUTY_INVALID = "POS_INVALID_TRADE";

    /**
     * 美业退货权限
     */
    public static final String PERMISSION_BEAUTY_REFUND = "POS_REFUND";

    /**
     * 美业交接权限
     */
    public static final String PERMISSION_BEAUTY_HANDOVER = "pos:bs:handover";

    /**
     * 美业历史交接权限
     */
    public static final String PERMISSION_BEAUTY_HANDOVER_LAST = "pos:bs:handover:last";
    /**
     * 美业反结账权限
     */
    public static final String PERMISSION_BEAUTY_REPAY = "pos:bs:repeat";

    /**
     * 收支编辑权限
     */
    public static final String PERMISSION_PAYMENTS_EDIT = "pos:bs:paymentsedit";

    /**
     * 美业关账权限
     */
    public static final String PERMISSION_BEAUTY_CLOSING = "pos:bs:closing";

    /**
     * 美业关账历史权限
     */
    public static final String PERMISSION_BEAUTY_CLOSING_HISTORY = "pos:bs:closing:history";

    /**
     * 正餐接受拒绝权限
     */
    public static final String PERMISSION_BEAUTY_ACCEPT = "pos:bs:accept";

    /**
     * 美业报表中心权限
     */
    public static final String PERMISSION_BEAUTY_REPORT_FORM = "POS_REPORT";


    /**
     * 创建预约开关
     */
    public static final String PERMISSION_BEAUTY_CREATE_RESERVER = "POS_CREATE_RESERVER";


    /**
     * 门店管理
     */
    public static final String PERMISSION_BEAUTY_SHOP_MANAGE = "POS_SHOP_MANAGE";


    /**
     * 接受拒绝第三方预约订单
     */
    public static final String PERMISSION_BEAUTY_ACCEPT_OR_REFUSE_RESERVER = "POS_ACCEPT_REFUSE_RESERVER";

    /**
     * 修改预约
     */
    public static final String PERMISSION_BEAUTY_MODIFY_RESERVER = "POS_REFRESH_RESERVER";

    /**
     * 正餐顾客登录权限（By Phone Number）
     */
    public static final String PERMISSION_CUSTOMER_LOGIN = "USER_LOGIN";

    public BeautyApplication() {
        super(AuthorizedApplication.APP_CODE_BEAUTY);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_BEAUTY);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.beauty_app_name);
    }

    @Override
    public void setIcon() {
        //setIcon(R.drawable.beauty_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        //setLockIcon(R.drawable.beauty_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(100);
    }

    @Override
    public void setPackageName() {
        setPackageName(PackageURI.BTY);
    }

    @Override
    public void setActivityName() {
        setActivityName("com.zhongmei.beauty.BeautyMainActivity_");
    }

    @Override
    public void setHasNewShareKey() {
    }
}
