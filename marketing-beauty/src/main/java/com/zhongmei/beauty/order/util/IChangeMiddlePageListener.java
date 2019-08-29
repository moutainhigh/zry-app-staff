package com.zhongmei.beauty.order.util;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;

/**
 * 美业中间条操作
 * Created by demo on 2018/12/15
 */

public interface IChangeMiddlePageListener {
    //默认页
    public static final int DEFAULT_PAGE = -1;
    //没有与左侧购物车交互的状态
    public static final int OTHER_PAGE = 0;
    //会员卡
    public static final int CARD_PAGE = 1;
    //        积分页
    public static final int INTEGRAL_PAGE = 2;
    //优惠劵
    public static final int COUPON_PAGE = 3;
    //        整单折扣
    public static final int DEFINE_DISCOUNT_PAGE = 4;
    //        批量折扣
    public static final int BATCH_DISCOUNT_PAGE = 5;

    //        营销活动
    public static final int MARKET_ACTIVITY_PAGE = 6;
    //整单页
    public static final int COMMON_DEFINE_PAGE = 7;
    //券验证
    public static final int VERIFY_CODE=8;

    /**
     * 当前所处的页面
     *
     * @param currentPage
     * @param uuid        当前选择的uuid
     */
    void changePage(int currentPage, String uuid);

    /**
     * @param item 新增item才传，其余传null
     */
    void closePage(IShopcartItemBase item);

    /**
     * 显示指定套餐
     *
     * @param item
     */
    void showCombo(ShopcartItem item);

    /**
     * 控制关闭按钮
     *
     * @param isShow
     */
    void showCloseView(boolean isShow);
}
