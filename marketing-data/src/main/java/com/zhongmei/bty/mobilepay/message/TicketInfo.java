package com.zhongmei.bty.mobilepay.message;

import com.zhongmei.bty.mobilepay.enums.TicketInfoStatus;
import com.zhongmei.yunfu.util.ValueEnums;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class TicketInfo implements Serializable {

       /* serialNumber	string	团购券码(业务上唯一标识团购券)
        receiptId	int	团购券Id(点评系统上唯一标识团购券)
        dealId	int	套餐Id(唯一标识团购套餐)
        dealVersionId	int	套餐版本号(标记套餐的价格的变动)
        dealGroupId	int	团单Id(团单是套餐的组合,一个团单可能包含多个套餐)
        dealTitle	string	套餐名称
        dealGroupTitle	string	团单简称
        beginDate	string	有效期起始日期
        dealReceiptEndDate	string	有效期结束日期
        marketPrice	bigdecimal	市场价
        price	bigdecimal	售价
        status	int	团购劵状态
        statusDesc	string	团购劵状态说明
        addDate	string	购买时间
        mobile	string	手机号码*/

       /* private TicketInfo ticketInfo;

        public TicketInfo getTicketInfo() {
            return ticketInfo;
        }*/

    private Long code;
    private String code_desc;
    private String serialNumber;
    private Long receiptId;
    private Long dealId;
    private Long dealVersionId;
    private Long dealGroupId;
    private String dealTitle;
    private String dealGroupTitle;
    private String beginDate;
    private String dealReceiptEndDate;
    private BigDecimal marketPrice = BigDecimal.ZERO;
    private BigDecimal price = BigDecimal.ZERO;
    private Integer status;
    private String statusDesc;
    private String addDate;
    private String mobile;

    public Long getCode() {
        return code;
    }

    public String getCode_desc() {
        return code_desc;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public Long getDealId() {
        return dealId;
    }

    public Long getDealVersionId() {
        return dealVersionId;
    }

    public Long getDealGroupId() {
        return dealGroupId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public String getDealGroupTitle() {
        return dealGroupTitle;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getDealReceiptEndDate() {
        return dealReceiptEndDate;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public TicketInfoStatus getStatus() {
        return ValueEnums.toEnum(TicketInfoStatus.class, status);
    }

    public String getStatusdesc() {
        return statusDesc;
    }

    public String getAddDate() {
        return addDate;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TicketInfo other = (TicketInfo) obj;
        if (serialNumber == null) {
            if (other.serialNumber != null)
                return false;
        } else if (!serialNumber.equals(other.serialNumber))
            return false;
        return true;
    }
}
