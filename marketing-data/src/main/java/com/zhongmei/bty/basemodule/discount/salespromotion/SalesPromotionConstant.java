package com.zhongmei.bty.basemodule.discount.salespromotion;

public class SalesPromotionConstant {

    /**
     * 适用人群
     */
    public class ApplyCrowd {
        /**
         * 会员
         */
        public static final int MEMBER = 1;
        /**
         * 非会员
         */
        public static final int NONMEMBER = 2;
        /**
         * 不限
         */
        public static final int ALL = 3;
    }

    /**
     * 营销科目类型
     */
    public class MarketSubject {
        /**
         * 全部商品可用
         */
        public static final int ALL_GOODS_USABLE = 101;
        /**
         * 部分商品可用
         */
        public static final int PART_GOODS_USABLE = 102;
        /**
         * 部分商品不可用
         */
        public static final int PART_GOODS_UNUSABLE = 103;
        /**
         * 注册
         */
        public static final int REGISTER = 201;
        /**
         * 登录
         */
        public static final int LOGIN = 202;
        /**
         * 排队
         */
        public static final int QUEUE = 203;
        /**
         * 预定
         */
        public static final int BOOK = 204;
        /**
         * 交易
         */
        public static final int TRADE = 205;
        /**
         * 储值
         */
        public static final int STORE = 206;
    }

    /**
     * 规则科目类型
     */
    public class RuleSubject {
        /**
         * 单次满量
         */
        public static final int MATCH_QUANTITY_ONCE = 11;
        /**
         * 单次满额
         */
        public static final int MATCH_AMOUNT_ONCE = 12;
        /**
         * 捆绑套餐
         */
        public static final int BUNDLE_SALES = 12;
        /**
         * 单一价
         */
        public static final int SINGLE_PRICE = 21;
        /**
         * 累计满量
         */
        public static final int MATCH_QUANTITY_TOTAL = 31;
        /**
         * 累计满额
         */
        public static final int MATCH_AMOUNT_TOTAL = 32;
        /**
         * 累计满次
         */
        public static final int MATCH_TIME_TOTAL = 33;
    }

    /**
     * 规则逻辑类型
     */
    public class RuleLogic {
        public static final int EQ = 1;//等于
        public static final int LTE = 2;//小于等于
        public static final int GTE = 3;//大于等于
        public static final int LT = 7;//小于
        public static final int GT = 8;//大于
    }

    /**
     * 策略科目类型
     */
    public class PolicySubject {
        /**
         * 现金优惠
         */
        public static final int SPECIFIED_GOODS = 11;
        /**
         * 赠送商品
         */
        public static final int GIVE_GOODS = 12;
        /**
         * 下一份优惠
         */
        public static final int NEXT_GOODS = 13;
        /**
         * 单一价
         */
        public static final int SINGLE_PRICE = 14;

        /**
         * 组合优惠
         */
        public static final int MULTIPLE_GOODS = 15;

        /**
         * 加价购商品
         */
        public static final int RAISE_PRICE_BUY_GOODS = 17;

        /**
         * 单品优惠
         */
        public static final int SINGLE_GOODS = 16;

        /**
         * 赠送优惠券
         */
        public static final int GIVE_COUPONS = 21;
    }

    /**
     * 策略细则类型
     */
    public class PolicyDetail {
        /**
         * 现金优惠-一口价
         */
        public static final int SPECIFIED_GOODS_FIXED_PRICE = 111;
        /**
         * 现金优惠-让价
         */
        public static final int SPECIFIED_GOODS_REBATE = 112;
        /**
         * 现金优惠-折低让价
         */
        public static final int SPECIFIED_GOODS_REBATE_LOWEST = 113;
        /**
         * 现金优惠-折扣
         */
        public static final int SPECIFIED_GOODS_DISCOUNT = 114;
        /**
         * 赠送商品-赠送商品
         */
        public static final int GIVE_GOODS = 121;
        /**
         * 下一份优惠-一口价
         */
        public static final int NEXT_GOODS_FIXED_PRICE = 131;
        /**
         * 下一份优惠-让价
         */
        public static final int NEXT_GOODS_REBATE = 132;
        /**
         * 下一份优惠-折扣
         */
        public static final int NEXT_GOODS_DISCOUNT = 133;
        /**
         * 单一价-一口价
         */
        public static final int SINGLE_PRICE_FIXED_PRICE = 141;
        /**
         * 单一价-让价
         */
        public static final int SINGLE_PRICE_REBATE = 142;
        /**
         * 单一价-折扣
         */
        public static final int SINGLE_PRICE_DISCOUNT = 143;
        /**
         * 单一价-加价
         */
        public static final int SINGLE_PRICE_RAISE_PRICE = 144;
        /**
         * 组合优惠-一口价
         */
        public static final int MULTIPLE_GOODS_FIXED_PRICE = 151;
        /**
         * 组合优惠-让价
         */
        public static final int MULTIPLE_GOODS_REBATE = 152;
        /**
         * 组合优惠-折低让价
         */
        public static final int MULTIPLE_GOODS_REBATE_LOWEST = 153;
        /**
         * 组合优惠-折扣
         */
        public static final int MULTIPLE_GOODS_DISCOUNT = 154;
        /**
         * 单品优惠-一口价
         */
        public static final int SINGLE_GOODS_FIXED_PRICE = 161;
        /**
         * 单品优惠-让价
         */
        public static final int SINGLE_GOODS_REBATE = 162;

        /**
         * 单品优惠-折扣
         */
        public static final int SINGLE_GOODS_DISCOUNT = 163;

        /**
         * 加价购商品
         */
        public static final int SINGLE_GOODS_RAISE_PRICE = 171;

        /**
         * 赠送优惠券-赠送优惠券
         */
        public static final int GIVE_COUPONS = 211;

    }

    public class Extra {
        public static final String EXTRA_SALES_PROMOTION = "extra_sales_promotion";
    }
}
