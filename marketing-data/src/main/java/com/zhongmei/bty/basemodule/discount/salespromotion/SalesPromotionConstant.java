package com.zhongmei.bty.basemodule.discount.salespromotion;

public class SalesPromotionConstant {


    public class ApplyCrowd {

        public static final int MEMBER = 1;

        public static final int NONMEMBER = 2;

        public static final int ALL = 3;
    }


    public class MarketSubject {

        public static final int ALL_GOODS_USABLE = 101;

        public static final int PART_GOODS_USABLE = 102;

        public static final int PART_GOODS_UNUSABLE = 103;

        public static final int REGISTER = 201;

        public static final int LOGIN = 202;

        public static final int QUEUE = 203;

        public static final int BOOK = 204;

        public static final int TRADE = 205;

        public static final int STORE = 206;
    }


    public class RuleSubject {

        public static final int MATCH_QUANTITY_ONCE = 11;

        public static final int MATCH_AMOUNT_ONCE = 12;

        public static final int BUNDLE_SALES = 12;

        public static final int SINGLE_PRICE = 21;

        public static final int MATCH_QUANTITY_TOTAL = 31;

        public static final int MATCH_AMOUNT_TOTAL = 32;

        public static final int MATCH_TIME_TOTAL = 33;
    }


    public class RuleLogic {
        public static final int EQ = 1;        public static final int LTE = 2;        public static final int GTE = 3;        public static final int LT = 7;        public static final int GT = 8;    }


    public class PolicySubject {

        public static final int SPECIFIED_GOODS = 11;

        public static final int GIVE_GOODS = 12;

        public static final int NEXT_GOODS = 13;

        public static final int SINGLE_PRICE = 14;


        public static final int MULTIPLE_GOODS = 15;


        public static final int RAISE_PRICE_BUY_GOODS = 17;


        public static final int SINGLE_GOODS = 16;


        public static final int GIVE_COUPONS = 21;
    }


    public class PolicyDetail {

        public static final int SPECIFIED_GOODS_FIXED_PRICE = 111;

        public static final int SPECIFIED_GOODS_REBATE = 112;

        public static final int SPECIFIED_GOODS_REBATE_LOWEST = 113;

        public static final int SPECIFIED_GOODS_DISCOUNT = 114;

        public static final int GIVE_GOODS = 121;

        public static final int NEXT_GOODS_FIXED_PRICE = 131;

        public static final int NEXT_GOODS_REBATE = 132;

        public static final int NEXT_GOODS_DISCOUNT = 133;

        public static final int SINGLE_PRICE_FIXED_PRICE = 141;

        public static final int SINGLE_PRICE_REBATE = 142;

        public static final int SINGLE_PRICE_DISCOUNT = 143;

        public static final int SINGLE_PRICE_RAISE_PRICE = 144;

        public static final int MULTIPLE_GOODS_FIXED_PRICE = 151;

        public static final int MULTIPLE_GOODS_REBATE = 152;

        public static final int MULTIPLE_GOODS_REBATE_LOWEST = 153;

        public static final int MULTIPLE_GOODS_DISCOUNT = 154;

        public static final int SINGLE_GOODS_FIXED_PRICE = 161;

        public static final int SINGLE_GOODS_REBATE = 162;


        public static final int SINGLE_GOODS_DISCOUNT = 163;


        public static final int SINGLE_GOODS_RAISE_PRICE = 171;


        public static final int GIVE_COUPONS = 211;

    }

    public class Extra {
        public static final String EXTRA_SALES_PROMOTION = "extra_sales_promotion";
    }
}
