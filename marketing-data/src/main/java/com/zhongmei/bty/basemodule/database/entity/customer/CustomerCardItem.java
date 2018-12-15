package com.zhongmei.bty.basemodule.database.entity.customer;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.IEcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.yunfu.util.ValueEnums;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员卡实体对象
 * <p>
 * Created by demo on 2018/12/15
 */
public class CustomerCardItem implements IEcCardInfo, Serializable {
    public Long id;
    public Long cardKindId;
    public String cardKindName;
    public String cardNum;
    public int cardStatus;
    public int isNeedPwd;
    public int cardType;
    public BigDecimal remainValue;


    /**
     * 卡是否有储值权限
     */
    public Integer rightStatus;

    @Override
    public EcCardInfo getEcCardInfo() {
        EcCardInfo info = new EcCardInfo();
        info.setId(id);
        info.setCardKindId(cardKindId);
        info.setCardKindName(cardKindName);
        info.setCardNum(cardNum);
        info.setCardStatus(ValueEnums.toEnum(CardStatus.class, cardStatus));
        info.setCardType(cardType);
        if (remainValue != null) {
            info.setRemainValue(remainValue);
        }
        info.setRightStatus(rightStatus);
        return info;
    }
}
