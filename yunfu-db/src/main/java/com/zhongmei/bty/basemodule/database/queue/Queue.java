package com.zhongmei.bty.basemodule.database.queue;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.EntityBase;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

/**
 * 排队
 */
@DatabaseTable(tableName = "queue")
public class Queue extends EntityBase<String> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ {
        /**
         * id
         */
        String id = "id";

        /**
         * status
         */
        String status = "status";

        String commercialID = "commercial_id";

        String name = "name";

        String mobile = "mobile";

        String queueStatus = "queue_status";

        String createDateTime = "create_date_time";

        String modifyDateTime = "modify_date_time";

        String repastPersonCount = "repast_person_count";

        String queueNumber = "queue_number";

        String sex = "sex";

        String queueSource = "queue_source";

        String queueProof = "queue_proof";

        String inDateTime = "in_date_time";

        String skipDatetime = "skip_datetime";

        String isZeroOped = "is_zero_oped";

        String isOfficial = "is_official";

        String queueLineId = "queue_line_id";

        String isOk = "is_ok";

        String uuid = "uuid";

        String notifyType = "notifyType";

        String remindCount = "remind_count";

        String remindTime = "remind_time";

        String nationalTelCode = "national_tel_code";

        String memo = "memo";

    }

    /**
     * 商户id
     */
    @DatabaseField(columnName = "commercial_id")
    private Long commercialID;

    /**
     * 排队状态 0:排队中;1:入场;-1作废;-2取消
     */
    @DatabaseField(columnName = "queue_status")
    private Integer queueStatus;

    /**
     * 创建时间
     */
    @DatabaseField(columnName = "create_date_time")
    private Long createDateTime;

    /**
     * 更新时间
     */
    @DatabaseField(columnName = "modify_date_time")
    private Long modifyDateTime;

    /**
     * 客户名称
     */
    @DatabaseField(columnName = "name")
    private String name;

    /**
     * 电话
     */
    @DatabaseField(columnName = "mobile")
    private String mobile;

    /**
     * 排队人数
     */
    @DatabaseField(columnName = "repast_person_count")
    private Integer repastPersonCount;

    /**
     * 排队序号
     */
    @DatabaseField(columnName = "queue_number")
    private Integer queueNumber;

    /**
     * 性别 0女 1 男
     */
    @DatabaseField(columnName = "sex")
    private Integer sex;

    /**
     * 排队来源
     */
    @DatabaseField(columnName = "queue_source")
    private String queueSource;

    /**
     * queueProof
     */
    @DatabaseField(columnName = "queue_proof")
    private String queueProof;

    /**
     * 入场时间
     */
    @DatabaseField(columnName = "in_date_time")
    private Long inDateTime;

    /**
     * 过号时间
     */
    @DatabaseField(columnName = "skip_datetime")
    private Long skipDatetime;

    /**
     * 是否清零 0否 1 是
     */
    @DatabaseField(columnName = "is_zero_oped")
    private Integer isZeroOped;

    /**
     * 微信订单来源 0：官微 ，1：商微
     */
    @DatabaseField(columnName = "is_official")
    private Integer isOfficial;

    /**
     * 同步上行是否成功(0成功，-1失败)
     */
    @DatabaseField(columnName = "is_ok")
    private Integer isOk;

    /**
     * 队列编号
     */
    @DatabaseField(columnName = "queue_line_id")
    private Long queueLineId;

    @DatabaseField(columnName = "uuid", id = true, canBeNull = false)
    private String uuid = "uuid";

    @DatabaseField(columnName = "id")
    private Long id;

    @DatabaseField(columnName = "status", canBeNull = false)
    private Integer status;

    /**
     * 提醒状态
     */
    @DatabaseField(columnName = "notifyType")
    private String notifyType;

    @DatabaseField(columnName = "customer_id")
    private Long customerId;

    /**
     * remind_count和remind_time不用，现使用queueExtra里面的值，由服务器记录的
     */
    @DatabaseField(columnName = "remind_count")
    private Integer remindCount;

    @DatabaseField(columnName = "remind_time")
    private Long remindTime;

    @DatabaseField(columnName = "national_tel_code")
    private String nationalTelCode;

    /**
     * 备注
     */
    @DatabaseField(columnName = "memo")
    private String memo;

    //////////////////////以下是扩展字段，不属于数据库值////////////////////
    public Long levelId; //	等级Id

    public QueueExtra queueExtra;

    public String country;//国家名

    public String nation;//国家英文名

    public String getNationalTelCode() {
        return nationalTelCode;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public QueueNotifyType getNotifyType() {
        return ValueEnums.toEnum(QueueNotifyType.class, notifyType);
    }

    public void setNotifyType(QueueNotifyType notifyType) {
        this.notifyType = ValueEnums.toValue(notifyType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public QueueStatus getQueueStatus() {
        return ValueEnums.toEnum(QueueStatus.class, queueStatus);
    }

    public void setQueueStatus(QueueStatus queueStatus) {
        this.queueStatus = ValueEnums.toValue(queueStatus);
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRepastPersonCount() {
        return repastPersonCount;
    }

    public void setRepastPersonCount(Integer repastPersonCount) {
        this.repastPersonCount = repastPersonCount;
    }

    public Integer getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(Integer queueNumber) {
        this.queueNumber = queueNumber;
    }

    public Sex getSex() {
        return ValueEnums.toEnum(Sex.class, sex);
    }

    public void setSex(Sex sex) {
        this.sex = ValueEnums.toValue(sex);
    }

    public QueueOrderSource getQueueSource() {
        return ValueEnums.toEnum(QueueOrderSource.class, queueSource);
    }

    public void setQueueSource(QueueOrderSource queueSource) {
        this.queueSource = ValueEnums.toValue(queueSource);
    }

    public String getQueueProof() {
        return queueProof;
    }

    public void setQueueProof(String queueProof) {
        this.queueProof = queueProof;
    }

    public Long getInDateTime() {
        return inDateTime;
    }

    public void setInDateTime(Long inDateTime) {
        this.inDateTime = inDateTime;
    }

    public Long getSkipDatetime() {
        return skipDatetime;
    }

    public void setSkipDatetime(Long skipDatetime) {
        this.skipDatetime = skipDatetime;
    }

    public YesOrNo getIsZeroOped() {
        return ValueEnums.toEnum(YesOrNo.class, isZeroOped);
    }

    public void setIsZeroOped(YesOrNo isZeroOped) {
        this.isZeroOped = ValueEnums.toValue(isZeroOped);
    }

    public Integer getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(Integer isOfficial) {
        this.isOfficial = isOfficial;
    }

    public SuccessOrFaild getIsOk() {
        return ValueEnums.toEnum(SuccessOrFaild.class, isOk);
    }

    public void setIsOk(SuccessOrFaild isOk) {
        this.isOk = ValueEnums.toValue(isOk);
    }

    public Long getQueueLineId() {
        return queueLineId;
    }

    public void setQueueLineId(Long queueLineId) {
        this.queueLineId = queueLineId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getRemindCount() {
        return remindCount;
    }

    public void setRemindCount(Integer remindCount) {
        this.remindCount = remindCount;
    }

    public Long getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(Long remindTime) {
        this.remindTime = remindTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public Long verValue() {
        return modifyDateTime;
    }

    @Override
    public boolean isValid() {
        return status == 0;
    }

    @Override
    public String pkValue() {
        return uuid;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(uuid, status);
    }

    @Override
    public boolean isChanged() {
        return super.isChanged();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Queue) {
            Queue q = (Queue) o;
            return commercialID != null && q.commercialID != null && commercialID.equals(q.commercialID);
        }
        return false;
    }
}
