package com.zhongmei.bty.mobilepay.message;

import com.zhongmei.bty.mobilepay.enums.TicketInfoStatus;
import com.zhongmei.yunfu.util.ValueEnums;

import java.io.Serializable;
import java.math.BigDecimal;



public class TicketInfo implements Serializable {





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
