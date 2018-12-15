package com.zhongmei.yunfu;

/**
 * @Date：2014年11月4日 上午11:43:44
 * @Description: 公用的静态变量声明
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class Constant {

    /**
     * @date：2014年11月7日 上午12:01:22
     * @Description:sharepreference里面文件的名称
     */
    public static final String SP_FILE_NAME = "calm";

    /**
     * @date：2014年11月7日 上午12:01:47
     * @Description:设备的硬件地址，一般是wifi的mac地址
     */
    public static final String SP_DEVICE_ID = "device_id";
    /**
     * 七牛 bucket name
     */
    public static final String QINIU_BUCKET_NAME = "kry-onoslog";


    public static final String SP_LOG_SWITCH = "log_switch";

    public static final String SP_PRINT_IP_ADDRESS = "current_print_ip_address";

    //读卡设备
    public static final String SP_READ_CARD_DEVICE = "sp_read_card_device";

    // bundle传递--跳转之前的页面index
    public static final String EXTRA_LAST_PAGE = "extraLastPage";

    // bundle传递--shopcartItemUuid的key
    public static final String EXTRA_SHOPCART_ITEM_UUID = "extraShopcartItemUuid";

    public static final String EXTRA_SHOPCART_ITEM_PARENT_UUID = "extraShopcartItemParentUuid";

    //bundle传递，判断来源 -1为快餐,1为正餐
    public static final String EXTRA_FROM_TYPE = "extra_from_type";

    //bundle传递，判断订单中心当前Tab栏位
    public static final String EXTRA_CURRENT_TAB = "extra_current_tab";

    //bundle传递，判断订单中心当前在销货-已结账tab下，是否开启清账模式
    public static final String EXTRA_SQUARE_MOD = "extra_current_square_mod";

    // 切换界面时是否需要验证
    public static final String NONEEDCHECK = "noNeedCheck";

    // 搜索关键字
    public static final String SP_SEARCH_KEYWORD = "search_keyword";

    // POS机的终端号
    public static final String SP_POS_DEVICIE_ID = "devices";

    // oneapm 的token 测试：42933BC607FE8DC1F6A8E8B2E865327909
    // 正式：ACEC5F42A6BB02704FCB8501CA9F7C0E09
    //压测环境：final String ONEAPM_TOKEN = FF3D2F457410B16BEB1E1A78D33BA75B09
    public static final String ONEAPM_TOKEN = "42933BC607FE8DC1F6A8E8B2E865327909";

    // 自动拒绝开关
    public static final String SP_AUTO_REFUSE_SWITCH = "auto_refuse_switch";

    public static final String SP_AUTO_ACCEPT_SWITCH = "auto_accept_switch";

    public static final String DINNER_AUTO_ACCEPT_SWITCH = "dinner_auto_accept_switch";

    /**
     * 跳转的activit
     */
    public static final String ACTIVITY_NAME = "activityName";

    public static final String HANDOVER_BUSINESSTYPE = "handover_businesstype";

    /**
     * 与当前版本匹配的打印服务的版本号.
     */
    public static final int MATCHING_PRINT_SERVER_VERSION_CODE = 2110020700;

    public static final String SP_LOGIN_USER_ID = "login_user_id";

    //跳转到打印服务故障订单页面
    public static final String ACTION_TASK_PAGE = "PAGE_PRINT_TASK_LIST";

    public static final String SP_PRINT_SERVICE_ERROR_ORDER = "print_service_error_order_list";

    //是否允许打印多张预结单
    public static final String ALLOW_MULTI_PREPAY_TICKET = "allow_multi_prepay_ticket";

    //结账后是否打印后厨
    public static final String PRINT_KITCHEN_AFTER_PAY = "print_kitchen_after_pay";

    //系统默认语言
    public static final String DEFAULTLANGUAGE = "defaultLanguage";

    //api安全验证码token
    //public static final String TOKENENCRYPT = "tokenEncrypt";

    //是否允许组合(分步)支付
    public static final String SUPPORT_GROUP_PAY = "allow_group_pay";

    //是否允许一码支付
    public static final String SUPPORT_ONECODE_PAY = "allow_onecode_pay";//add v8.12
    //打印的包名
    public static final String PRINT_SERVICE_PACKAGE_NAME = "com.demo.print";
    //数据库的authority
    public static final String DB_AUTHORITY = "com.zhongmei.calm.provider.Calm";

    //微信订单接受自动传后厨
    public static final String WEIXIN_ACCEPT_TRANSFER_KITCHEN = "weixin_accept_transfer_kitchen";

    //未登录用户
    public static final String HAVE_NO_LOGINDATA = "com.zhongmei.bty.haveno.logindata";

    /**
     * 西餐菜序显示方式设置
     */
    public static final String SP_WEST_EUROPEAN_FOOD_SORT = "sp_west_dish_sort";

    public static final String TOKEN_SHOP_ID = "token_shop_id";

    /**
     * erp 初始化  商户国籍id
     */
    //public static final String ERP_ARED_CODE = "erp_area_code";
    /**
     * erp 初始化  face
     */
    public static final String ERP_FACE = "erp_face";

    /**
     * Usb标签打印配置项
     */
    public static final String SETTING_PRINT_USB_LABEL_PRINTER = "setting_print_usb_label_config";

    /**
     * 称重自动进位开关
     */
    public static final String SETTING_SUPPORT_WEIGH_SELF_INSTRUCTED = "setting_allow_self_instructed";
}
