package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.util.ValueEnums;


/**
 * 封装调用估清菜品接口所需的请求数据
 *
 * @version: 1.0
 * @date 2015年8月11日
 */
public class DishClearStatusReq {

    private Integer newClearStatus;
    private Integer oldClearStatus;
    private List<String> uuids;

    public ClearStatus getNewClearStatus() {
        return ValueEnums.toEnum(ClearStatus.class, newClearStatus);
    }

    public void setNewClearStatus(ClearStatus newClearStatus) {
        this.newClearStatus = ValueEnums.toValue(newClearStatus);
    }

    public ClearStatus getOldClearStatus() {
        return ValueEnums.toEnum(ClearStatus.class, oldClearStatus);
    }

    public void setOldClearStatus(ClearStatus oldClearStatus) {
        this.oldClearStatus = ValueEnums.toValue(oldClearStatus);
    }

    public List<String> getUuids() {
        return uuids;
    }

    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }

}
