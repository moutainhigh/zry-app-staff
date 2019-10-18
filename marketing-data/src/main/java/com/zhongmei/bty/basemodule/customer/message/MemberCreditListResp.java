package com.zhongmei.bty.basemodule.customer.message;

import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

import java.util.List;

public class MemberCreditListResp extends LoyaltyTransferResp<MemberCreditListResp.CreditResp> {



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
        public Long customerId;            public Long customerMainId;        public String mobile;        public String name;        public Integer sex;            public String birthday;        public Long levelId;        public String levelName;        public Integer isDisable;            public String tradeNo;        public Double beforeValue;                        public Double addValue;                        public Double endValue;                        public Integer opType;                            public Long payModeId;                        public String payModeName;                        public String bizDate;                            public String memo;                            public Long userId;                            public String userName;                        public String serverCreateTime;          }

}
