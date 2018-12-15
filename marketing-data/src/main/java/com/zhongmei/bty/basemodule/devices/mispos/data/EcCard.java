package com.zhongmei.bty.basemodule.devices.mispos.data;

import android.util.Log;

import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.database.operates.EcCardDal;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.bty.commonmodule.database.enums.CardRechagingStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class EcCard implements Serializable {

    /**
     * @date：2016-3-21 下午3:14:52
     */
    private static final long serialVersionUID = 1L;

    private static final String TAG = EcCard.class.getSimpleName();

    private String serverCreateTime;

    private String serverUpdateTime;

    private Long creatorId;

    private String creatorName;

    private Long updatorId;

    private String updatorName;

    private Integer statusFlag;

    private Long tradeId;

    private Long tradeItemId;

    private Long id;

    private Long cardKindId;

    private Long cardLevelId;

    private Long brandId;

    private String batchCode;

    private String cardNum;

    private String cardPwd;

    private Integer cardStatus;

    private Long customerId;

    private Long openPerson;

    private Long openCommercialId;

    private String openTime;

    private Long invalidPerson;

    private String invalidTime;

    private String invalidMemo;

    private String name;

    private Integer cardType;

    private EcCardLevel cardLevel;

    public Integer priceLimit;

    private EcCardLevelSetting cardLevelSetting;

    private List<EcCardSettingDetail> cardSettingDetails;

    private EcIntegralAccount integralAccount;

    private EcValueCardAccount valueCardAccount;

    private EcCardKind cardKind;

    private CustomerV5 customer;

    private EctempAccount ectempAccount;//临时卡信息

    /**
     * 卡是否有储值权限
     */
    private Integer rightStatus;

    private BigDecimal remainValue;

    private Integer integral;

    public BigDecimal getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(BigDecimal remainValue) {
        this.remainValue = remainValue;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public void setRightStatus(Integer rightStatus) {
        this.rightStatus = rightStatus;
    }

    public void setRightStatus(CardRechagingStatus rightStatus) {
        this.rightStatus = rightStatus.value();
    }

    public CardRechagingStatus getRightStatus() {
        return ValueEnums.toEnum(CardRechagingStatus.class, rightStatus);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardKindId() {
        return cardKindId;
    }

    public void setCardKindId(Long cardKindId) {
        this.cardKindId = cardKindId;
    }

    public Long getCardLevelId() {
        return cardLevelId;
    }

    public void setCardLevelId(Long cardLevelId) {
        this.cardLevelId = cardLevelId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }

    public CardStatus getCardStatus() {
        return ValueEnums.toEnum(CardStatus.class, cardStatus);
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = ValueEnums.toValue(cardStatus);
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOpenPerson() {
        return openPerson;
    }

    public void setOpenPerson(Long openPerson) {
        this.openPerson = openPerson;
    }

    public Long getOpenCommercialId() {
        return openCommercialId;
    }

    public void setOpenCommercialId(Long openCommercialId) {
        this.openCommercialId = openCommercialId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public Long getInvalidPerson() {
        return invalidPerson;
    }

    public void setInvalidPerson(Long invalidPerson) {
        this.invalidPerson = invalidPerson;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getInvalidMemo() {
        return invalidMemo;
    }

    public void setInvalidMemo(String invalidMemo) {
        this.invalidMemo = invalidMemo;
    }

    public String getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(String serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public String getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(String serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(Long tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public EcCardKind getCardKind() {
        return cardKind;
    }

    public void setCardKind(EcCardKind cardKind) {
        this.cardKind = cardKind;
    }

    public EntityCardType getCardType() {
        return ValueEnums.toEnum(EntityCardType.class, cardType);
    }

    public void setCardType(EntityCardType cardType) {
        this.cardType = ValueEnums.toValue(cardType);
    }


    public EctempAccount getEctempAccount() {
        return ectempAccount;
    }

    public void setEctempAccount(EctempAccount ectempAccount) {
        this.ectempAccount = ectempAccount;
    }

    public void queryLevelSettingInfo() {
        try {
            if (cardKindId != null) {
                cardKind = DBHelperManager.queryById(EcCardKind.class, cardKindId);
            }
            if (cardLevelId != null) {
                cardLevel = DBHelperManager.queryById(EcCardLevel.class, cardLevelId);
                EcCardDal ecCardDal = OperatesFactory.create(EcCardDal.class);
                cardLevelSetting = ecCardDal.findEcCardLevelSetting(cardLevelId);
                cardSettingDetails = ecCardDal.findEcCardSettingDetail(cardLevelId);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 获取 EcCardInfo 卡信息
     *
     * @return
     */
    public EcCardInfo getEcCardInfo() {
        EcCardInfo info = new EcCardInfo();
        info.setCardNum(this.cardNum);
        info.setCardKindId(this.cardKindId);
        info.setCardKindName(this.cardKind.getCardKindName());
        info.setCardType(this.cardType);
        info.setCardStatus(ValueEnums.toEnum(CardStatus.class, this.cardStatus));
        if (valueCardAccount != null) {
            info.setRemainValue(BigDecimal.valueOf(this.valueCardAccount.getRemainValue()));
        } else {
            info.setRemainValue(BigDecimal.ZERO);
        }
        if (integralAccount != null) {
            info.setIntegral(BigDecimal.valueOf(this.integralAccount.getIntegral()));
        } else {
            info.setIntegral(BigDecimal.ZERO);
        }
        info.setRightStatus(this.rightStatus);
        return info;
    }
}
