package com.zhongmei.bty.basemodule.discount.event;

import java.util.List;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp.Card;


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

        PRIVILIGE_ITEMS,

        DISCOUNT,

        INTEGRAL,

        COUPON,

        CUSTOMER_COUPON,

        EXTRA_CHARGE,

        COUPON_CODE,

        LOGIN,

        MARKET_ACTIVITY,

        SWITCH_CARD,

        SHOWTAOTALPAGE,


        CLOSETOTALPAGE,

        CUSTOMER_LIKE_REMARK
    }

}
