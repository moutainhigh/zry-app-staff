package com.zhongmei.beauty.interfaces;

import com.zhongmei.yunfu.db.entity.trade.Tables;

import java.util.List;



public interface BeautyOrderOperatorListener {

    void onLoginClick();


    void onCardClick();


    void onIntegralClick();


    void onPartyClick();


    void onCouponClick();


    void onDiscountClick();


    void onActivityClick();


    void onExtraClick();


    void onRemarkClick();


    void onTableClick();


    void onTableChoice(List<Tables> tables);


    void onClearSelected();

}
