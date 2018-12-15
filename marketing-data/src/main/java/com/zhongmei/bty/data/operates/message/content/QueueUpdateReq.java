package com.zhongmei.bty.data.operates.message.content;

/**
 * Created by demo on 2018/12/15
 */
public class QueueUpdateReq {

    //排队唯一标示;
    private String syncFlag;

    //顾客名称
    private String name;

    //性别（0：女；1：男；-1：未知）
    private String sex;

    //手机号码
    private String mobile;

    //电话号码
    private String tel;

    //桌台类型唯一标识
    private String tableTypeServerId;

    //桌台类型名称
    private String tableTypeName;

    //排队状态(0:排队中;1:入场;-1作废;-2取消)
    private String queueStatus;

    //入场时间,queueStatus = 1时必须
    private String inDateTime;

    //就餐人数
    private int repastPersonCount;

    //是否清空(0：不清空；1：清空)
    private int isZeroOped;

    //排队来源,排队来源：Daodian：到店排队，weixin：微信排队，DianHua：电话排队，BaiduMap：百度排队
    private String queueSource;

    //排队凭证
    private String queueProof;

    //提醒方式
    private String notifyType;

    //队列编号
    private long queueLineId;

    //微信id
    private String weixinId;

    //会员id
    private long memberID;

    private int isOfficial;

    //预点菜关联的交易表ID 如果没有进行预点菜则为null或-1
    private long tradeId;

    private String nationalTelCode;

    private String country;//国家名

    private String nation;//国家英文名

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
