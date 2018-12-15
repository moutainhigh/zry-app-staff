package com.zhongmei.bty.basemodule.discount.event;

import java.util.List;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp.Card;

/**
 * 切换正餐优惠界面的几个选项
 */
public class ActionDinnerPrilivige {

    private DinnerPriviligeType type;

    private List<Card> cards;

    private String source;

    public CustomerResp getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResp customer) {
        this.customer = customer;
    }

    private CustomerResp customer;

    public ActionDinnerPrilivige(DinnerPriviligeType type) {
        this.type = type;
    }

    public ActionDinnerPrilivige(DinnerPriviligeType type, CustomerResp customer) {
        this.type = type;
        this.customer = customer;
    }

    public DinnerPriviligeType getType() {
        return type;
    }

    public void setType(DinnerPriviligeType type) {
        this.type = type;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public enum DinnerPriviligeType {
        /**
         * 优惠主界面
         */
        PRIVILIGE_ITEMS,
        /**
         * 折扣
         */
        DISCOUNT,
        /**
         * 积分
         */
        INTEGRAL,
        /**
         * 优惠券
         */
        COUPON,
        /**
         * 发券
         */
        CUSTOMER_COUPON,
        /**
         * 附加费
         */
        EXTRA_CHARGE,
        /**
         * 验券码
         */
        COUPON_CODE,
        /**
         * 登录
         */
        LOGIN,
        /**
         * 营销活动
         */
        MARKET_ACTIVITY,
        /**
         * 切换实体卡
         */
        SWITCH_CARD,
        /**
         * 显示总单
         */
        SHOWTAOTALPAGE,

        /**
         * 关闭总单
         */
        CLOSETOTALPAGE,
        /**
         * 顾客喜好/备注
         */
        CUSTOMER_LIKE_REMARK
    }

}
