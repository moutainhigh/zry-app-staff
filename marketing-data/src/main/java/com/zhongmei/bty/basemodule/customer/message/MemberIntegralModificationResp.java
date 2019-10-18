package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.bty.basemodule.customer.bean.IntegralRecord;




public class MemberIntegralModificationResp extends LoyaltyTransferResp<MemberIntegralModificationResp.IntegralItemData> {

    public class IntegralItemData {

        Long id;

        Integer beforeIntegral;
        Integer addIntegral;
        Integer endIntegral;
        String userId;
        Long commercialId;
        Long commercialGroupId;
        Long commercialMemberId;
        Long createDateTime;
        Long modifyDateTime;
        Integer status;
        String memo;

    }

    public IntegralRecord getIntegralRecord(IntegralItemData itemData) {
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setId(itemData.id);
        integralRecord.setBeforeIntegral(String.valueOf(itemData.beforeIntegral));
        integralRecord.setAddIntegral(String.valueOf(itemData.addIntegral));
        integralRecord.setEndIntegral(String.valueOf(itemData.endIntegral));
        integralRecord.setUserId(String.valueOf(itemData.userId));
        integralRecord.setCommercialId(String.valueOf(itemData.commercialId));
        integralRecord.setCommercialGroupId(String.valueOf(itemData.commercialGroupId));
        integralRecord.setCommercialMemberId(String.valueOf(itemData.commercialMemberId));
        integralRecord.setCreateDateTime(itemData.createDateTime.longValue());
        integralRecord.setModifyDateTime(itemData.modifyDateTime.longValue());
        integralRecord.setStatus(String.valueOf(itemData.beforeIntegral));
        integralRecord.setReason(itemData.memo);
        return integralRecord;
    }
}
