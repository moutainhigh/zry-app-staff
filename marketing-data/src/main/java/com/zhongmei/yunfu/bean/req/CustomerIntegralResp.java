package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.customer.bean.IntegralRecord;

import java.util.ArrayList;
import java.util.List;

public class CustomerIntegralResp {

    Long aggregateCount;//累计积分总量

    Long integralCount;//当前可用积分总量

    List<NewIntegralRecord> items;

    public List<NewIntegralRecord> getItems() {
        return items;
    }

    class NewIntegralRecord {

        Long id;//积分记录ID

        Integer recordType; //记录类型1储值、2消费

        Long beforeIntegral;//之前积分

        Long addIntegral;//本次新增积分

        Long endIntegral;//操作后积分

        Long createDateTime;//创建时间

        Long modifyDateTime;//修改时间

        Integer status;//状态

        String reason;//操作原因

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
