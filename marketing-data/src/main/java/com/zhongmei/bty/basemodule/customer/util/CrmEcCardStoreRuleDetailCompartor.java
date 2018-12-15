package com.zhongmei.bty.basemodule.customer.util;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardSettingDetail;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 实体卡规则排序
 */
public class CrmEcCardStoreRuleDetailCompartor implements Comparator<EcCardSettingDetail>, Serializable {

    @Override
    public int compare(EcCardSettingDetail lhs, EcCardSettingDetail rhs) {
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
