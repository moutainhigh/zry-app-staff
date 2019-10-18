package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.customer.bean.IntegralRecord;

import java.util.ArrayList;
import java.util.List;

public class CustomerIntegralResp {

    Long aggregateCount;
    Long integralCount;
    List<NewIntegralRecord> items;

    public List<NewIntegralRecord> getItems() {
        return items;
    }

    class NewIntegralRecord {

        Long id;
        Integer recordType;
        Long beforeIntegral;
        Long addIntegral;
        Long endIntegral;
        Long createDateTime;
        Long modifyDateTime;
        Integer status;
        String reason;
        String userName;


    }

    public List<IntegralRecord> getMemberIntegral(List<NewIntegralRecord> result) {
        List<IntegralRecord> memberIntegrals = new ArrayList<>();
        IntegralRecord record;
        if (result != null && result.size() != 0) {
            for (NewIntegralRecord n : result) {
                record = new IntegralRecord();
                record.setId(n.id);
                record.setRecordType(n.recordType);
                record.setBeforeIntegral(String.valueOf(n.beforeIntegral));
                record.setAddIntegral(String.valueOf(n.addIntegral));
                record.setEndIntegral(String.valueOf(n.endIntegral));
                record.setUserId(n.userName);
                record.setModifyDateTime(n.modifyDateTime);
                record.setStatus(String.valueOf(n.status));
                record.setReason(n.reason);
                record.setAggregateCount(String.valueOf(aggregateCount));
                memberIntegrals.add(record);
            }
        }
        return memberIntegrals;
    }

}
