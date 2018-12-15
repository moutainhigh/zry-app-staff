package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.bean.ICustomer;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevel;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardSettingDetail;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcIntegralAccount;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcValueCardAccount;
import com.zhongmei.bty.basemodule.devices.mispos.data.EctempAccount;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardLoginResp.Result;

import java.util.List;

public class CardLoginResp extends CardBaseResp<Result> implements ICustomer {

    @Override
    public CustomerResp getCustomer() {
        CustomerResp customerNew = new CustomerResp();
        CustomerV5 customerV5 = getResult().getCustomer();
        customerNew.brandId = customerV5.getBrandid();
        customerNew.birthday = customerV5.getBirthday();
        customerNew.address = customerV5.getAddress();
        customerNew.customerId = customerV5.getCustomerid();
        customerNew.customerName = customerV5.getName();
        customerNew.interest = customerV5.getInterest();
        customerNew.groupId = customerV5.getGroupid();
        customerNew.memberId = customerV5.getMemberid();
        customerNew.mobile = customerV5.getMobile();
        customerNew.invoice = customerV5.getInvoice();
        customerNew.levelId = customerV5.getLevelid();
        customerNew.sex = customerV5.getSex().value();
        customerNew.memo = customerV5.getMemo();
        customerNew.card = getResult().cardInstance;
        customerNew.isDisable = customerV5.getIsdisable().intValue();
        EcIntegralAccount integralAccount = getResult().integralAccount;
        EcValueCardAccount valueCardAccount = getResult().valueCardAccount;
        if (integralAccount != null && customerNew.card != null) {
            customerNew.card.setIntegralAccount(integralAccount);
        }
        if (valueCardAccount != null) {
            customerNew.card.setValueCardAccount(valueCardAccount);
        }
        return customerNew;
    }

    public class Result {

        private EcCard cardInstance;

        private EcCardKind cardKind;

        private EcCardLevel cardLevel;

        private EcCardLevelSetting cardLevelSetting;

        private List<EcCardSettingDetail> cardSettingDetails;

        private EcIntegralAccount integralAccount;

        private EcValueCardAccount valueCardAccount;

        private EctempAccount tempAccount;

        private CustomerV5 customer;

        public EcCard getCardInstance() {
            return cardInstance;
        }

        public void setCardInstance(EcCard cardInstance) {
            this.cardInstance = cardInstance;
        }

        public EcCardKind getCardKind() {
            return cardKind;
        }

        public void setCardKind(EcCardKind cardKind) {
            this.cardKind = cardKind;
        }

        public EcCardLevel getCardLevel() {
            return cardLevel;
        }

        public void setCardLevel(EcCardLevel cardLevel) {
            this.cardLevel = cardLevel;
        }

        public EcCardLevelSetting getCardLevelSetting() {
            return cardLevelSetting;
        }

        public void setCardLevelSetting(EcCardLevelSetting cardLevelSetting) {
            this.cardLevelSetting = cardLevelSetting;
        }

        public List<EcCardSettingDetail> getCardSettingDetails() {
            return cardSettingDetails;
        }

        public void setCardSettingDetails(List<EcCardSettingDetail> cardSettingDetails) {
            this.cardSettingDetails = cardSettingDetails;
        }

        public EcIntegralAccount getIntegralAccount() {
            return integralAccount;
        }

        public void setIntegralAccount(EcIntegralAccount integralAccount) {
            this.integralAccount = integralAccount;
        }

        public EcValueCardAccount getValueCardAccount() {
            return valueCardAccount;
        }

        public void setValueCardAccount(EcValueCardAccount valueCardAccount) {
            this.valueCardAccount = valueCardAccount;
        }

        public CustomerV5 getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerV5 customer) {
            this.customer = customer;
        }

        public EctempAccount getTempAccount() {
            return tempAccount;
        }

        public void setTempAccount(EctempAccount tempAccount) {
            this.tempAccount = tempAccount;
        }
    }

}
