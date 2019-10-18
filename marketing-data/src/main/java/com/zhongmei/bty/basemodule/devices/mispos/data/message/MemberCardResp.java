package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.customer.message.MemberCreateResp.MemberInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardResp.MemberCard;

import java.util.List;


public class MemberCardResp extends CardBaseResp<MemberCard> {

    public class MemberCard {

        private MemberInfo customerVo;

        private List<EcCardInfo> cardList;

        public MemberInfo getCustomerVo() {
            return customerVo;
        }

        public void setCustomerVo(MemberInfo customerVo) {
            this.customerVo = customerVo;
        }

        public List<EcCardInfo> getCardList() {
            return cardList;
        }

        public void setCardList(List<EcCardInfo> cardList) {
            this.cardList = cardList;
        }

    }

}
