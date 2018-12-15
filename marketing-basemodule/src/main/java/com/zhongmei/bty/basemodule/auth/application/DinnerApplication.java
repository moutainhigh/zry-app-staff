package com.zhongmei.bty.basemodule.auth.application;


import com.zhongmei.bty.router.PathURI;
import com.zhongmei.yunfu.basemodule.R;

public class DinnerApplication extends AuthorizedApplication {
    /**
     * 正餐权限
     */
    public static final String PERMISSION_DINNER = "pos:zc";

    /**
     * 正餐开台权限
     */
    public static final String PERMISSION_DINNER_CREATE = "pos:zc:create";

    /**
     * 下个迭代申请新的权限码
     */
    public static final String PERMISSION_DINNER_MERGE = "pos:zc:merge";

    /**
     * 正餐转台权限
     */
    public static final String PERMISSION_DINNER_TRANFER = "pos:zc:tranfer";

    /**
     * 正餐收银权限
     */
    public static final String PERMISSION_DINNER_CASH = "pos:zc:cash";

    /**
     * 正餐开钱箱权限
     */
    public static final String PERMISSION_DINNER_MONEYBOX = "pos:zc:moneybox";

    /**
     * 正餐抹零权限
     */
    public static final String PERMISSION_DINNER_MALING = "pos:zc:maling";
    /**
     * 正餐自定义抹零权限
     */
    public static final String PERMISSION_DINNER_AUTO_MALING = "pos:zc:automaling";

    /**
     * 正餐自定义抹零金额权限
     */
    public static final String PERMISSION_DINNER_MALING_AMOUNT_LIMIT = "pos:zc:maling:amount";
    /**
     * 正餐接受拒绝权限
     */
    public static final String PERMISSION_DINNER_ACCEPT = "pos:zc:accept";

    /**
     * 正餐作废权限
     */
    public static final String PERMISSION_DINNER_INVALID = "pos:zc:invalid";

    /**
     * 正餐退货权限
     */
    public static final String PERMISSION_DINNER_REFUND = "pos:zc:refund";

    /**
     * 正餐交接权限
     */
    public static final String PERMISSION_DINNER_HANDOVER = "pos:zc:handover";

    /**
     * 正餐历史交接权限
     */
    public static final String PERMISSION_DINNER_HANDOVER_LAST = "pos:zc:handover:last";

    /**
     * 正餐优惠权限
     */
    public static final String PERMISSION_DINNER_PRIVILEDGE = "pos:zc:privilege";

    /**
     * 正餐打折优惠权限
     */
    public static final String PERMISSION_DINNER_PRIVILEDGE_DISCOUNT = "pos:zc:privilege:discount";

    /**
     * 正餐折让权限
     */
    public static final String PERMISSION_DINNER_PRIVILEDGE_REBATE = "pos:zc:privilege:rebate";

    /**
     * 正餐免单权限，针对整单
     */
    public static final String PERMISSION_DINNER_PRIVILEDGE_FREE = "pos:zc:privilege:free";
    /**
     * 正餐赠送权限，针对批量
     */
    public static final String PERMISSION_DINNER_PRIVILEDGE_PRESENT = "pos:zc:privilege:present";
    /**
     * 正餐宴请权限
     */
    public static final String PERMISSION_DINNER_PRIVILEDGE_BANQUET = "pos:zc:privilege:banquet";
    /**
     * 正餐反结账权限
     */
    public static final String PERMISSION_DINNER_REPAY = "pos:zc:repeat";

    /**
     * 正餐关账权限
     */
    public static final String PERMISSION_DINNER_CLOSING = "pos:zc:closing";

    /**
     * 正餐关账历史权限
     */
    public static final String PERMISSION_DINNER_CLOSING_HISTORY = "pos:zc:closing:history";

    /**
     * 正餐退菜权限
     */
    public static final String PERMISSION_DINNER_RETURN_GOODS = "pos:zc:return:goods";

    /**
     * 正餐通知中心重试pos交易权限
     */
    public static final String PERMISSION_DINNER_NOTIFY_POS_RETRY = "pos:zc:notify:pos:retry";

    /**
     * 正餐拆单权限
     */
    public static final String PERMISSION_DINNER_SPLIT = "pos:zc:split";

    /**
     * 正餐挂账权限
     */
    public static final String PERMISSION_DINNER_CREDIT = "pos:zc:credit";

    /**
     * 收支编辑权限
     */
    public static final String PERMISSION_PAYMENTS_EDIT = "pos:zc:paymentsedit";

    /**
     * 选择服务员权限
     */
    public static final String PERMISSION_SELECT_WAITER = "pos:zx:selectwaiter";

    /**
     * 移菜权限
     */
    public static final String PERMISSION_DINNER_MOVE_DISH = "pos:zc:movedish";

    /**
     * 报表中心权限
     */
    public static final String PERMISSION_DINNER_REPORT_FORM = "pos:zc:reportform";

    /**
     * 正餐清账权限
     */
    public static final String PERMISSION_DINNER_TAKEOUT_CLEAR = "pos:zc:clear";
    /**
     * 正餐校准权限
     */
    public static final String PERMISSION_DINNER_HANDOVER_CALIBRATE = "pos:zc:handover:calibrate";

    /**
     * 正餐打印预结单设置
     */
    public static final String PERMISSION_DINNER_PREREPRINT_SETTING = "pos:zc:preprint_setting";

    /**
     * 称重商品编辑权限
     */
    public static final String PERMISSION_DINNER_QUANTITY = "pos:zc:weight_input";

    /**
     * 正餐变价
     */
    public static final String PERMISSION_DINNER_CHANGEPRICE = "pos:zc:changeprice";


    public static final String PERMISSION_DISCOUND = "discount";
    public static final String PERMISSION_REBATE = "rebate";


    /**
     * 正餐删菜权限（对于正餐未传送后厨但是生效的菜品）
     */
    public static final String PERMISSION__DINNER_DELETE_DISH = "pos:zc:delete_dish";

    /**
     * 正餐批量删除／作废菜品权限
     */
    public static final String PERMISSION_DINNER_BATCH_DELETE_OR_RETURN = "pos:zc:batch_delete_or_return";

    /**
     * 正餐获取实时概况的权限
     */
    public static final String PERMISSION_DINNER_BUSINESS_CHARGE = "pos:zc:business_charge";

    /**
     * 正餐修改菜品权限
     */
    public static final String PERMISSION_DINNER_MODIFY_DISH = "pos:zc:modify_dish";

    /**
     * 正餐修改价格
     */
    public static final String PERMISSION_DINNER_MODIFY_PRICE = "pos:zc:modify_price";

    public DinnerApplication() {
        super(AuthorizedApplication.APP_CODE_DINNER);
    }


    @Override
    public void initMainPermission() {
        setMainPermission(PERMISSION_DINNER);
        setSystemApp(false);
    }

    @Override
    public void setTitle() {
        setTitle(R.string.dinner_app_name);
    }

    @Override
    public void setIcon() {
        setIcon(R.drawable.dinner_app_icon_selector);
    }

    @Override
    public void setLockIcon() {
        setLockIcon(R.drawable.dinner_app_lock_icon);
    }

    @Override
    public void setSort() {
        setSort(100);
    }

    @Override
    public void setPackageName() {
        setPackageName("com.zhongmei.bty");
    }

    @Override
    public void setActivityName() {
        setActivityName(PathURI.URI_DINNER);
    }

    @Override
    public void setHasNewShareKey() {
    }
}
