package com.zhongmei.yunfu.bean.req;

import android.content.Context;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.customer.bean.CustomerStatistic;
import com.zhongmei.bty.basemodule.customer.bean.ICustomerListBean;
import com.zhongmei.bty.basemodule.customer.bean.ICustomerStatistic;
import com.zhongmei.bty.basemodule.database.entity.customer.BeautyCardEntity;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.customer.entity.CrmCustomerLevelRightsDish;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerCardItem;
import com.zhongmei.bty.basemodule.devices.face.FaceFeatureVo;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.yunfu.db.enums.ChargePrivilegeType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerResp  implements Serializable, ICustomerListBean, ICustomerStatistic {

    private static final long serialVersionUID = 2319959790769528162L;
    public static final int SEX_FEMALE = 0;     public static final int SEX_MALE = 1;
    public Long brandId;     public Long memberId;     public Long customerId;     public Long customerMainId;     public String customerName;     public String mobile;     public String tel;
    public Integer sex;     public Long levelId;     public Long level;     public String levelName;     public String memo;     public String hobby;     public String invoice;     public String invoiceTitle;
    public String address;     public String entityCard;     public Long groupId;     public String groupName;     public String birthday;     public Integer isDisable;     public String openId;     public Double remainValue;     public Long integral;     public Integer cardCount;     public Integer coupCount;     public Double creditableValue;     public Double remainCreditValue;     public Double usedCreditValue;     public String password;    public Long commercialId;    public String commercialName;    public List<CustomerCardItem> cardList;
    public Integer hasFaceCode;    public String faceCode;    public String peopleId;    public String cardNo;
    public List<BeautyCardEntity> entityCards;    public Integer storedPrivilegeType;     public BigDecimal storedPrivilegeValue;     public BigDecimal storedFullAmount=BigDecimal.ZERO;
    private CustomerType customerType;




    public int faceGrade;



    public String nation;

    public String country;

    public String nationalTelCode;


    public Long localModifyDateTime;

    public String synFlag;

    public Integer source = 1;

    public Integer loginType;

    public String loginId;
    public Long modifyDateTime;     public Long upgradeTime;
        public CustomerGroupLevel customerLevel;    public CrmCustomerLevelRights customerLevelRights;    public List<Long> customerLevelRightsDishs;
    public EcCard card;     public boolean needRefresh = false;     public List<CustomerInfoResp.Card> otherCardList;     public boolean isLoginByCard;
        public Long id;
    public String name;

    public CustomerLoginType customerLoginType;         public FaceFeatureVo mFaceFeatureVo;

    public boolean isMember() {
        return true;     }

    public boolean isDisabled() {
        return isDisable != null && isDisable == 1;
    }


    public String getCustomerName(Context c) {
        return TextUtils.isEmpty(customerName) ? c.getString(R.string.customer_name_null) : customerName;
    }



    public void queryLevelRightInfos() {
        try {
            if (levelId != null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CustomerGroupLevel getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(CustomerGroupLevel customerLevel) {
        this.customerLevel = customerLevel;
    }

    public ChargePrivilegeType getStoredPrivilegeType() {
        if(storedPrivilegeType==null){
            return null;
        }
        return ValueEnums.toEnum(ChargePrivilegeType.class,storedPrivilegeType);
    }


    public void setCustomerLevelRightsDishs(List<CrmCustomerLevelRightsDish> dishs) {
        if (dishs != null && !dishs.isEmpty()) {
            customerLevelRightsDishs = new ArrayList<>(10);

            for (CrmCustomerLevelRightsDish dish : dishs) {
                if (!customerLevelRightsDishs.contains(dish.getDishBrandId())) {
                    customerLevelRightsDishs.add(dish.getDishBrandId());
                }
            }
        }
    }

    public String getCardNos(){
        if(Utils.isNotEmpty(entityCards)){
            StringBuffer cardNoBuf=new StringBuffer();
            for (BeautyCardEntity beautyCardEntity : entityCards) {
                cardNoBuf.append(beautyCardEntity.getCardNo()+",");
            }
           return  cardNoBuf.substring(0,cardNoBuf.length()-1);
        }
        return "";
    }

    @Override
    public CustomerListResp getCustomerListBean() {
        CustomerListResp bean = new CustomerListResp();
        bean.customerId = customerId;
        bean.groupId = groupId;
        bean.isDisable = isDisable == null ? 2 : Integer.valueOf(isDisable);
        bean.levelId = levelId;
        bean.name = customerName;
        bean.mobile = mobile;
        bean.hasFaceCode = hasFaceCode;
        bean.cardNo=cardNo;
        return bean;
    }

    @Override
    public CustomerStatistic getCustomerStatistic() {
        CustomerStatistic statistic = new CustomerStatistic();
        statistic.memberBalance = remainValue == null ? 0 : remainValue;
        statistic.memberIntegral = integral;
        statistic.memberCoupons = coupCount;
        return statistic;
    }


    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("address", address);

        json.put("bindFlag", 1);
        json.put("ctype", 0);
        if (!TextUtils.isEmpty(birthday)) {
            json.put("birthday", birthday);
        }
        json.put("id", customerId);
        json.put("customerMainId", customerMainId);
        if (upgradeTime == null) {
            json.put("cMemberCreateDateTime", upgradeTime);
        }

        json.put("groupId", groupId == null ? "999999" : groupId + "");
        json.put("interest", hobby);
        json.put("hobby", hobby);
        json.put("invoice", invoice);
        json.put("isDisable", isDisable);
        json.put("isNeedConfirm", 0);
        json.put("modifyDateTime", modifyDateTime);
        if (modifyDateTime != null) {
            json.put("lastSyncMarker", modifyDateTime / 1000);
        }
        json.put("levelId", levelId);
        json.put("loginId", loginId);
        json.put("loginType", loginType);
        json.put("memo", memo);
        json.put("mobile", mobile);
        json.put("name", customerName);
        json.put("serverId", synFlag);
        json.put("sex", sex);
        json.put("source", source);
        json.put("localCreateDateTime", upgradeTime);
        json.put("uuid", synFlag);
        json.put("birthdate", birthday + "");
        json.put("cardNo",cardNo);
        json.put("entityCards",entityCards);
        return json;
    }


    public void setInitialValue() {
        if (integral == null) {
            integral = 0L;
        }
        if (remainValue == null) {
            remainValue = 0D;
        }
        if (cardCount == null) {
            cardCount = 0;
        }
        if (coupCount == null) {
            coupCount = 0;
        }
    }


    public boolean hasCustomerEntityCard() {
        if (cardList == null || cardList.size() == 0) {
            return false;
        } else {
            for (CustomerCardItem item : cardList) {
                if (EntityCardType.GENERAL_CUSTOMER_CARD.value().equals(item.cardType)) {
                    return true;
                }
            }
        }
        return false;
    }

        private boolean isLoginCardNeedPwd() {
        if (otherCardList == null || otherCardList.size() == 0) {
            return false;
        } else {
            for (CustomerInfoResp.Card card : otherCardList) {
                if (EntityCardType.GENERAL_CUSTOMER_CARD.equals(card.getCardType())) {
                    return card.getIsNeedPwd() == 1;
                }
            }
        }
        return false;
    }

    public boolean isNeedPassword(BigDecimal amount) {
                if (this.isLoginByCard) {
            return isLoginCardNeedPwd();
        }
                if (this.customerLoginType == CustomerLoginType.FACE_CODE || this.customerLoginType == CustomerLoginType.WECHAT_MEMBERCARD_ID) {
            return false;
        }
                return !PaySettingCache.isCanPayNoPwd(amount);    }

    public CustomerListResp.LoginType getLoginType() {
        return ValueEnums.toEnum(CustomerListResp.LoginType.class, loginType);
    }

    public CustomerType getCustomerType() {
        if(customerType==null){
            customerType=CustomerType.MEMBER;
        }
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }


    public boolean hasFaceCode() {
        if (hasFaceCode == null) {
            return false;
        } else {
            if (hasFaceCode == 0) {
                return false;
            } else {
                return true;
            }
        }
    }
}
