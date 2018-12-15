package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

import java.util.List;

public class MemberCreditListResp extends LoyaltyTransferResp<MemberCreditListResp.CreditResp> {

    /**
     * currentPage	当前页	int
     * pageSize	每页记录数	int
     * totalRows	总记录数	int
     * startRow	开始记录数	int
     * showPageNums	需要展现的页码	int
     * totalPage	总页码数	int
     * items	数据对象	json
     */

    public class CreditResp {

        private Integer currentPage;

        private Integer pageSize;

        private Integer totalRows;

        private Integer startRow;

        private Integer totalPage;

        private List<Credit> items;

        public Integer getCurrentPage() {
            return currentPage;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public Integer getTotalRows() {
            return totalRows;
        }

        public Integer getStartRow() {
            return startRow;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public List<Credit> getItems() {
            return items;
        }
    }

    public class Credit {
        public Long customerId;    //顾客ID
        public Long customerMainId;//	顾客MainID
        public String mobile;//	顾客手机号码
        public String name;//	顾客名字	String
        public Integer sex;    //性别（-1 未知 0女 1男)
        public String birthday;//	出生日期(yyyy-MM-dd）
        public Long levelId;//	会员等级ID
        public String levelName;//	当前会员等级名称
        public Integer isDisable;    //会员停用Code(1-是 2-否)	int
        public String tradeNo;//	订单号	String
        public Double beforeValue;                //操作前顾客已挂账金额	double
        public Double addValue;                //本次操作金额	double
        public Double endValue;                //操作后顾客已挂账金额	double
        public Integer opType;                    //操作类型 1：挂账，2：销帐,3：挂账反结账，4：销帐反结账	int
        public Long payModeId;                //销帐支付标记Id	int
        public String payModeName;                //销帐支付标记名称	String
        public String bizDate;                    //销帐时间	Date
        public String memo;                    //挂账理由	String
        public Long userId;                    //挂账人ID	Long
        public String userName;                //挂账人名称	String
        public String serverCreateTime;      //销账记录产生时间
    }

}
