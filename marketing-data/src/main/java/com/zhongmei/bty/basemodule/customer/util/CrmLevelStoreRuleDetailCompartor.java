package com.zhongmei.bty.basemodule.customer.util;

import java.io.Serializable;
import java.util.Comparator;

import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRuleDetail;

public class CrmLevelStoreRuleDetailCompartor implements Comparator<CrmLevelStoreRuleDetail>, Serializable {

    @Override
    public int compare(CrmLevelStoreRuleDetail lhs, CrmLevelStoreRuleDetail rhs) {
        if (lhs != null && rhs != null && lhs.getFullValue() != null && rhs.getFullValue() != null) {
            if (lhs.getFullValue().floatValue() > rhs.getFullValue().floatValue()) {
                return 1;
            } else if (lhs.getFullValue().floatValue() == rhs.getFullValue().floatValue()) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
