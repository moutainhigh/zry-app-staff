package com.zhongmei.bty.basemodule.customer.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @Date：2015-8-28 上午11:59:08
 * @Description: 会员等级
 * @Version: 1.0
 * @see com.zhongmei.bty.entity.bean.order.AllReason 替换原始类
 */
@DatabaseTable(tableName = "reject_reason")
public class RejectReason extends BaseInfoOld {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ extends BaseInfoOld.$ {

        /**
         * name
         */
        String type = "type";

        String rejectReasons = "rejectReasons";
        String userDefined = "userDefined";
        String orders = "orders";

    }

    //	1拒绝原因 2取消预订原因 3退菜理由 4作废原因 5反结账原因
    public static final int REASON_TYPE_REFUSE = 1;//拒绝//暂时不做,之前已有
    public static final int REASON_TYPE_CANCEL_BOOK = 2;//取消预订
    public static final int REASON_TYPE_DELETE_DISH = 3;// 菜品删除
    public static final int REASON_TYPE_DESTROY = 4;//作废
    public static final int REASON_TYPE_REPAY = 5;//反结账
    public static final int REASON_TYPE_FREE_DISH = 6;//菜品免单
    public static final int REASON_TYPE_FREE_ORDER = 7;//单子免单

    //	是否允许用户自定义 0不能自定义 1可以自定义
    public static final String USERDEFINED_ENABLE = "1";//可以自定义
    public static final String USERDEFINED_DISENABLE = "0";//不能自定义

    @DatabaseField
    public int type; //标识是否是拒绝原因，删除原因等
    @DatabaseField
    public String rejectReasons; //原因的内容
    @DatabaseField
    public int userDefined; //是否允许用户自定义 0不能自定义 1可以自定义
    @DatabaseField
    public Integer orders; //顺序

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
