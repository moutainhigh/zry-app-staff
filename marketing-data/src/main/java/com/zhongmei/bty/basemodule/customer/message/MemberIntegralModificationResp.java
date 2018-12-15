package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.bty.basemodule.customer.bean.IntegralRecord;


/**
 * Created by demo on 2018/12/15
 */

public class MemberIntegralModificationResp extends LoyaltyTransferResp<MemberIntegralModificationResp.IntegralItemData> {

    public class IntegralItemData {

        Long id;

        Integer beforeIntegral;// 储值之前的积分余额

        Integer addIntegral;// 增加的积分

        Integer endIntegral;// 增加积分之后的余额

        String userId;// 操作员

        Long commercialId;// 商户Id

        Long commercialGroupId;// 品牌编号

        Long commercialMemberId;// 会员编号

        Long createDateTime;// 创建时间

        Long modifyDateTime;// 修改时间

        Integer status;// 有效状态

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
