package com.zhongmei.bty.data.operates.message.content;



public class BuffetFinishDepositRefundReq {
    private Long tradeId;
    private Long operateId;
    private String operateName;
    private BuffetDepositRefundReq refundDeposit;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public BuffetDepositRefundReq getRefundDeposit() {
        return refundDeposit;
    }

    public void setRefundDeposit(BuffetDepositRefundReq refundDeposit) {
        this.refundDeposit = refundDeposit;
    }
}
