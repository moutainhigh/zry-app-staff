package com.zhongmei.bty.commonmodule.database.entity.local;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.IUpdator;

/**
 * Created by demo on 2018/12/15
 */
@DatabaseTable(tableName = "pay_menu_order")
public class PayMenuOrder extends EntityBase<Long> implements IUpdator {
    private static final long serialVersionUID = 1L;

    @DatabaseField(columnName = "pay_menu_id", id = true, canBeNull = false)
    private Long payMenuId;//支付菜单id


    @DatabaseField(columnName = "pay_menu_order")
    private Integer order;//序号

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    public interface $ {
        /**
         * uuid
         */
        public static final String payMenuId = "pay_menu_id";
        public static final String order = "pay_menu_order";
    }

    public Long getPayMenuId() {
        return payMenuId;
    }

    public void setPayMenuId(Long payMenuId) {
        this.payMenuId = payMenuId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public Long getUpdatorId() {
        return updatorId;
    }

    @Override
    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    @Override
    public String getUpdatorName() {
        return updatorName;
    }

    @Override
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Long pkValue() {
        return payMenuId;
    }

    @Override
    public Long verValue() {
        return payMenuId;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(payMenuId);
    }
}
