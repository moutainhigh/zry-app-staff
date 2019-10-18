package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "reject_reason")
public class RejectReason extends BaseInfoOld {


    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {


        String type = "type";

        String rejectReasons = "rejectReasons";
        String userDefined = "userDefined";
        String orders = "orders";

    }

        public static final int REASON_TYPE_REFUSE = 1;    public static final int REASON_TYPE_CANCEL_BOOK = 2;    public static final int REASON_TYPE_DELETE_DISH = 3;    public static final int REASON_TYPE_DESTROY = 4;    public static final int REASON_TYPE_REPAY = 5;    public static final int REASON_TYPE_FREE_DISH = 6;    public static final int REASON_TYPE_FREE_ORDER = 7;
        public static final String USERDEFINED_ENABLE = "1";    public static final String USERDEFINED_DISENABLE = "0";
    @DatabaseField
    public int type;     @DatabaseField
    public String rejectReasons;     @DatabaseField
    public int userDefined;     @DatabaseField
    public Integer orders;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRejectReasons() {
        return rejectReasons;
    }

    public void setRejectReasons(String rejectReasons) {
        this.rejectReasons = rejectReasons;
    }

    public int getUserDefined() {
        return userDefined;
    }

    public void setUserDefined(int userDefined) {
        this.userDefined = userDefined;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
}
