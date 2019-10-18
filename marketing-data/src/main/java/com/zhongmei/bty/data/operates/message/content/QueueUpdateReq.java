package com.zhongmei.bty.data.operates.message.content;


public class QueueUpdateReq {

        private String syncFlag;

        private String name;

        private String sex;

        private String mobile;

        private String tel;

        private String tableTypeServerId;

        private String tableTypeName;

        private String queueStatus;

        private String inDateTime;

        private int repastPersonCount;

        private int isZeroOped;

        private String queueSource;

        private String queueProof;

        private String notifyType;

        private long queueLineId;

        private String weixinId;

        private long memberID;

    private int isOfficial;

        private long tradeId;

    private String nationalTelCode;

    private String country;
    private String nation;
    private String memo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(String syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTableTypeServerId() {
        return tableTypeServerId;
    }

    public void setTableTypeServerId(String tableTypeServerId) {
        this.tableTypeServerId = tableTypeServerId;
    }

    public String getTableTypeName() {
        return tableTypeName;
    }

    public void setTableTypeName(String tableTypeName) {
        this.tableTypeName = tableTypeName;
    }

    public String getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(String queueStatus) {
        this.queueStatus = queueStatus;
    }

    public String getInDateTime() {
        return inDateTime;
    }

    public void setInDateTime(String inDateTime) {
        this.inDateTime = inDateTime;
    }

    public int getRepastPersonCount() {
        return repastPersonCount;
    }

    public void setRepastPersonCount(int repastPersonCount) {
        this.repastPersonCount = repastPersonCount;
    }

    public int getIsZeroOped() {
        return isZeroOped;
    }

    public void setIsZeroOped(int isZeroOped) {
        this.isZeroOped = isZeroOped;
    }

    public String getQueueSource() {
        return queueSource;
    }

    public void setQueueSource(String queueSource) {
        this.queueSource = queueSource;
    }

    public String getQueueProof() {
        return queueProof;
    }

    public void setQueueProof(String queueProof) {
        this.queueProof = queueProof;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public long getQueueLineId() {
        return queueLineId;
    }

    public void setQueueLineId(long queueLineId) {
        this.queueLineId = queueLineId;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public long getMemberID() {
        return memberID;
    }

    public void setMemberID(long memberID) {
        this.memberID = memberID;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
