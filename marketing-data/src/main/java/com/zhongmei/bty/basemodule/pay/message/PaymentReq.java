package com.zhongmei.bty.basemodule.pay.message;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.PaymentType;

import java.util.List;

/**
 * 封装Payment相关的实时请求的Request数据
 *
 * @version: 1.0
 * @date 2015年4月15日
 */
public class PaymentReq {

    private Long relateId;
    private String relateUuid;
    private Integer paymentType;
    private Long serverUpdateTime;
    private Long updatorId;
    private String updatorName;
    private List<PaymentTo> payments;
    private Long tradePayForm = 1L;//add 2015.12.3

    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public String getRelateUuid() {
        return relateUuid;
    }

    public void setRelateUuid(String relateUuid) {
        this.relateUuid = relateUuid;
    }

    public PaymentType getPaymentType() {
        return ValueEnums.toEnum(PaymentType.class, paymentType);
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = ValueEnums.toValue(paymentType);
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public List<PaymentTo> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentTo> payments) {
        this.payments = payments;
    }

    public Long getTradePayForm() {
        return tradePayForm;
    }

    public void setTradePayForm(Long tradePayForm) {
        this.tradePayForm = tradePayForm;
    }

    /**
     *

     * @version: 1.0
     * @date 2015年4月16日
     *
     */
    /*	public static class PaymentTo extends Payment {

     *//**
     *
     *//*
		private static final long serialVersionUID = 1L;

		private List<PaymentItemTo> paymentItems;
		private List<PaymentItemUnionpayVoReq> paymentCards;// 银联刷卡记录  add 20160218
		public List<PaymentItemTo> getPaymentItems() {
			return paymentItems;
		}

		public void setPaymentItems(List<PaymentItemTo> paymentItems) {
			this.paymentItems = paymentItems;
		}

		public List<PaymentItemUnionpayVoReq> getPaymentCards() {
			return paymentCards;
		}

		public void setPaymentCards(List<PaymentItemUnionpayVoReq> paymentCards) {
			this.paymentCards = paymentCards;
		}

	}*/

    /**
     *

     * @version: 1.0
     * @date 2015年4月21日
     *
     *//*
	public static class PaymentItemTo extends PaymentItem {
		
		*//**
     *
     *//*
		private static final long serialVersionUID = 1L;
		
		private String password;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public boolean equals(Object obj) {
			return this == obj || super.equals(obj);
		}
	}
*/
}
