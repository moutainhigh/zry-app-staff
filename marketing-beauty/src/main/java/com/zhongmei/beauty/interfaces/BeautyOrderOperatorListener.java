package com.zhongmei.beauty.interfaces;

import com.zhongmei.yunfu.db.entity.trade.Tables;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface BeautyOrderOperatorListener {
    /**
     * 登陆登出
     */
    void onLoginClick();

    /**
     * 次卡选择
     */
    void onCardClick();

    /**
     * 积分选择
     */
    void onIntegralClick();

    /**
     * 参与者
     */
    void onPartyClick();

    /**
     * 优惠券
     */
    void onCouponClick();

    /**
     * 打折
     */
    void onDiscountClick();

    /**
     * 活动选择
     */
    void onActivityClick();

    /**
     * 加项
     */
    void onExtraClick();

    /**
     * 备注
     */
    void onRemarkClick();

    /**
     * 桌台选择
     */
    void onTableClick();

    /**
     * 选择的桌台传回
     *
     * @param tables
     */
    void onTableChoice(List<Tables> tables);


    void onClearSelected();

}
