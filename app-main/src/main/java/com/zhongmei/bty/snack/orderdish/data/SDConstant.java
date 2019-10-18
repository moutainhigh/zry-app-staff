package com.zhongmei.bty.snack.orderdish.data;



public final class SDConstant {
    public static final class FromType {
        public final static int FROM_TYPE_SNACK = -1;        public static final int FROM_TYPE_DINNER = 1;        public static final int FROM_TYPE_BUFFET = 2;        public static final int FROM_TYPE_GROUP = 3;        public static final int FROM_TYPE_RETAIL = 4;        public static final int FROM_OPEN_API = 5;    }

    public static final class Extra {
        public static final String EXTRA_FROM_TYPE = "extra_from_type";
    }


    public static final class MobClick {

        public static final String SNACK_ORDER_GOODS_FEED = "快餐-加料点击量";
        public static final String SNACK_ORDER_GOODS_PRACTICE = "快餐-做法点击量";
        public static final String SNACK_ORDER_GOODS_EXTRA_FEE = "快餐-附加费点击量";
        public static final String SNACK_ORDER_GOODS_REMARK = "快餐-备注点击量";
        public static final String SNACK_ORDER_GOODS_SINGLE_REMARK = "快餐-单品备注使用数";
        public static final String SNACK_ORDER_GOODS_SINGLE_BALE = "快餐-单品打包使用数";

        public static final String SNACK_SHOPCAR_MEMBER_LOGIN = "快餐-购物车会员登录量";
        public static final String SNACK_SHOPCAR_MEMBER_LOGIN_PHONE = "快餐-购物车会员手机登录量";
        public static final String SNACK_SHOPCAR_MEMBER_LOGIN_CARD = "快餐-购物车刷卡登录量";
        public static final String SNACK_SHOPCAR_MEMBER_LOGIN_SCANED = "快餐-购物车会员被扫登录量";
        public static final String SNACK_SHOPCAR_MEMBER_LOGIN_SCAN = "快餐-购物车会员主扫登录量";
        public static final String SNACK_SHOPCAR_MEMBER_REGISTER = "快餐-购物车会员注册量";

        public static final String SNACK_QUICK_PAY_ALIPAY_SCAN = "快餐-支付宝主扫使用量";
        public static final String SNACK_QUICK_PAY_ALIPAY_SCANED = "快餐-支付宝被扫使用量";
        public static final String SNACK_QUICK_PAY_WEIXIN_SCAN = "快餐-微信主扫使用量";
        public static final String SNACK_QUICK_PAY_WEIXIN_SCANED = "快餐-微信被扫使用量";
        public static final String SNACK_QUICK_PAY_MOBILE_SCAN = "快餐-移动主扫使用量";
        public static final String SNACK_QUICK_PAY_MOBILE_SCANED = "快餐-移动被扫使用量";
        public static final String SNACK_QUICK_PAY_MEMBER_SCAN = "快餐-储值主扫使用量";
        public static final String SNACK_QUICK_PAY_MEMBER_SCANED = "快餐-储值被扫使用量";
        public static final String SNACK_QUICK_PAY_MEMBER_CARD = "快餐-储值刷卡使用量";
        public static final String SNACK_QUICK_PAY_MEMBER_FACE = "快餐-储值刷脸使用量";

        public static final String SNACK_PAY_CHARGE = "快餐-收银台储值支付点击量";
        public static final String SNACK_PAY_MEMBER_LOGIN_PHONE = "快餐-收银台会员手机登录量";
        public static final String SNACK_PAY_MEMBER_LOGIN_CARD = "快餐-收银台会员刷卡登录量";
        public static final String SNACK_PAY_MEMBER_LOGIN_FACE = "快餐-收银台会员刷脸登录量";
        public static final String SNACK_PAY_MEMBER_LOGIN_SCAN = "快餐-收银台会员主扫登录量";
        public static final String SNACK_PAY_MEMBER_LOGIN_SCANED = "快餐-收银台会员被扫登录量";
        public static final String SNACK_PAY_MEMBER_LOGIN_CHARGE = "快餐-收银台会员充值";

        public static final String SNACK_PAY_WIPE_ZERO = "快餐-抹零点击量";
        public static final String SNACK_PAY_WIPE_ZERO_FEN = "快餐-抹分点击量";
        public static final String SNACK_PAY_WIPE_ZERO_JIAO = "快餐-抹角点击量";
        public static final String SNACK_PAY_WIPE_ZERO_YUAN = "快餐-抹元点击量";
        public static final String SNACK_PAY_WIPE_ZERO_CUSTOMER = "快餐-自定义抹零点击量";

        public static final String SNACK_CENTER_CATEGORY_UP_DOWN = "快餐-中类上下翻页点击量";
    }

}
