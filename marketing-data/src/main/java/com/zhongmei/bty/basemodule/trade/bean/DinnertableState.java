package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.enums.TableStatus;

import java.io.Serializable;

/**
 * @version: 1.0
 * @date 2015年9月25日
 */
public class DinnertableState implements Serializable {

    private Long id;
    private Long modifyDateTime;
    private Integer tableStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public TableStatus getTableStatus() {
        return ValueEnums.toEnum(TableStatus.class, tableStatus);
    }

    public void setTableStatus(TableStatus tableStatus) {
        this.tableStatus = ValueEnums.toValue(tableStatus);
    }

}
