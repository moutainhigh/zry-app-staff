package com.zhongmei.beauty.order.util;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;



public interface IChangeMiddlePageListener {
        public static final int DEFAULT_PAGE = -1;
        public static final int OTHER_PAGE = 0;
        public static final int CARD_PAGE = 1;
        public static final int INTEGRAL_PAGE = 2;
        public static final int COUPON_PAGE = 3;
        public static final int DEFINE_DISCOUNT_PAGE = 4;
        public static final int BATCH_DISCOUNT_PAGE = 5;

        public static final int MARKET_ACTIVITY_PAGE = 6;
        public static final int COMMON_DEFINE_PAGE = 7;
        public static final int VERIFY_CODE=8;


    void changePage(int currentPage, String uuid);


    void closePage(IShopcartItemBase item);


    void showCombo(ShopcartItem item);


    void showCloseView(boolean isShow);
}
