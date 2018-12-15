package com.zhongmei.yunfu.db.entity.booking;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.enums.BookingOrderSource;
import com.zhongmei.yunfu.db.enums.BookingOrderStatus;
import com.zhongmei.yunfu.db.enums.BookingType;
import com.zhongmei.yunfu.db.enums.Sex;

import java.math.BigDecimal;

/**
 * 预订单
 */
@DatabaseTable(tableName = "booking")
public class Booking extends DataEntityBase {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public interface $ extends DataEntityBase.$ {
        /**
         * commercial_id
         */
        String customerID = "customer_id";

        String customerName = "customer_name";

        String customerGender = "customer_gender";//新增

        String customerMobile = "customer_phone";//更新过

        String startTime = "start_time";//新增

        String endTime = "end_time";//新增

        String tableId = "table_id";//新增

        String tableName = "table_name";//新增

        String customerNumber = "customer_number";//新增

        String orderStatus = "order_status";

        String remark = "remark";//更新

        String cancelOrderTime = "cancel_order_time";

        String bookingResource = "booking_source";//更新 orderSource

        String bookingType = "booking_type";

        String confirmed = "confirmed";

        String brandIdenty = "brand_identy"; //新增

        String shopIdenty = "shop_identy"; //新增

        String creatorID = "creator_id";

        String creatorName = "creator_name";

        String updatorID = "updator_id";

        String updatorName = "updator_name";


        String commercialID = "commercial_id";

        String customerGroup = "customer_group";

        String customerSynflag = "customer_synflag";

        String orderTime = "order_time";

        String orderNumber = "order_number";

        String consumerStandard = "consumer_standard";

        String innerOrderperson = "inner_order_person";

        String envFavorite = "env_favorite";

        String shopArriveTime = "shop_arrive_time";

        String shopArriveUser = "shop_arrive_user";

        String shopLeaveTime = "shop_leave_time";

        String shopLeaverUser = "shop_leaver_user";

        String cancelOrderUser = "cancel_order_user";

        String realConsumeTime = "real_consume_time";

        String realConsume = "real_consume";

        String realConsumeUser = "real_consume_user";

        String refusalReason = "refusal_reason";

        String isImportant = "is_important";

        String periodServerId = "periodServerId";


    }

    /**
     * 会员id
     */
    @DatabaseField(columnName = "commercialId")
    private Long commercialId;

    /**
     * 会员名称
     */
    @DatabaseField(columnName = "commercialName")
    private String commercialName;

    /**
     * 客户手机号
     */
    @DatabaseField(columnName = "commercialPhone")
    private String commercialPhone;

    /**
     * 性别 0女 1 男
     */
    @DatabaseField(columnName = "commercialGender")
    private Integer commercialGender;

    /**
     * 排序
     */
    @DatabaseField(columnName = "sort")
    private Integer sort;

    /**
     * 预订开始时间
     */
    @DatabaseField(columnName = "start_time")
    private Long startTime;

    /**
     * 预订结束时间
     */
    @DatabaseField(columnName = "end_time")
    private Long endTime;

    /**
     * 桌台id
     */
    @DatabaseField(columnName = "table_id")
    private Long tableId;

    /**
     * 桌台名称
     */
    @DatabaseField(columnName = "table_name")
    private String tableName;

    /**
     * 预定人数
     */
    @DatabaseField(columnName = "customer_num")
    private Integer customerNum;

    /**
     * 订单状态
     * -1:用户未到店,1:用户到店,2:用户离店,9:已取消,-2:未处理,-3:已拒绝,-4:逾期未到店',
     */
    @DatabaseField(columnName = "order_status")
    private Integer orderStatus;

    /**
     * 备注
     */
    @DatabaseField(columnName = "remark")
    private String remark;

    /**
     * 订单来源
     */
    @DatabaseField(columnName = "booking_source")
    private Integer bookingSource;

    /**
     * 订单类型 1美业
     */
    @DatabaseField(columnName = "booking_type")
    private Integer bookingType;

//	@DatabaseField(columnName = "confirmed")
//	private boolean confirmed; //标记用户预定确认. 未确认:0 已确认: 1

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;


    /**
     * 0普通 1重点
     */
    @DatabaseField(columnName = "is_important")
    private Integer isImportant;

    /**
     * 时段id
     */
    @DatabaseField(columnName = "period_id")
    private Long periodID;

    /**
     * 对应客户表的synFlay
     */
    @DatabaseField(columnName = "customer_synflag")
    private String customerSynflag;

    /**
     * 客户分组
     */
    @DatabaseField(columnName = "customer_group")
    private String commercialGroup;


    /**
     * 消费标准
     */
    @DatabaseField(columnName = "consumer_standard")
    private String consumeStandard;


    /**
     * 内部预订代订人
     */
    @DatabaseField(columnName = "inner_order_person")
    private String innerOrderPerson;


    /**
     * 客户环境喜好
     */
    @DatabaseField(columnName = "env_favorite")
    private String envFavorite;

    /**
     * 取消预订操作人
     */
    @DatabaseField(columnName = "cancel_order_user")
    private String cancelOrderUser;

    /**
     * 消费时间
     */
    @DatabaseField(columnName = "real_consume_time")
    private Long realConsumeTime;

    /**
     * 消费金额
     */
    @DatabaseField(columnName = "real_consume")
    private BigDecimal realConsume;

    /**
     * 消费操作人人
     */
    @DatabaseField(columnName = "real_consume_user")
    private String realConsumeUser;

    /**
     * 拒绝原因
     */
    @DatabaseField(columnName = "refusal_reason")
    private String refusalReason;


    @DatabaseField(columnName = "nationalTelCode")
    private String nationalTelCode;    //电话国际区码(为空默认中国)	否	String


    /**
     * 备注
     */
    public String orderDesc;

    //////////////////////////////以下是扩展字段////////////////////////////
    public Long levelId; //	等级Id
    public boolean collectPrepayment; //是否有预付金

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String customerName) {
        this.commercialName = customerName;
    }

    public String getCommercialPhone() {
        return commercialPhone;
    }

    public void setCommercialPhone(String customerPhone) {
        this.commercialPhone = customerPhone;
    }

    public Long getOrderTime() {
        return startTime;
    }


    public String getConsumeStandard() {
        return consumeStandard;
    }

    public void setConsumeStandard(String consumeStandard) {
        this.consumeStandard = consumeStandard;
    }

    public String getInnerOrderPerson() {
        return innerOrderPerson;
    }

    public void setInnerOrderPerson(String innerOrderPerson) {
        this.innerOrderPerson = innerOrderPerson;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getMemo() {
        return remark;
    }

    public void setMemo(String memo) {
        this.remark = memo;
    }

    public String getEnvFavorite() {
        return envFavorite;
    }

    public void setEnvFavorite(String envFavorite) {
        this.envFavorite = envFavorite;
    }

    public BookingOrderStatus getOrderStatus() {
        return ValueEnums.toEnum(BookingOrderStatus.class, orderStatus);
    }

    public void setOrderStatus(BookingOrderStatus orderStatus) {
        this.orderStatus = ValueEnums.toValue(orderStatus);
    }

    public String getCancelOrderUser() {
        return cancelOrderUser;
    }

    public void setCancelOrderUser(String cancelOrderUser) {
        this.cancelOrderUser = cancelOrderUser;
    }

    public Long getRealConsumeTime() {
        return realConsumeTime;
    }

    public void setRealConsumeTime(Long realConsumeTime) {
        this.realConsumeTime = realConsumeTime;
    }

    public BigDecimal getRealConsume() {
        return realConsume;
    }

    public void setRealConsume(BigDecimal realConsume) {
        this.realConsume = realConsume;
    }

    public String getRefusalReason() {
        return refusalReason;
    }

    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    public BookingOrderSource getBookingSource() {
        return ValueEnums.toEnum(BookingOrderSource.class, bookingSource);
    }

    public void setBookingSource(BookingOrderSource orderSource) {
        this.bookingSource = ValueEnums.toValue(orderSource);
    }

    public String getCustomerSynflag() {
        return customerSynflag;
    }

    public void setCustomerSynflag(String customerSynflag) {
        this.customerSynflag = customerSynflag;
    }

    public String getCommercialGroup() {
        return commercialGroup;
    }

    public void setCommercialGroup(String customerGroup) {
        this.commercialGroup = customerGroup;
    }

    public Long getCommercialId() {
        return commercialId;
    }

    public void setCommercialId(Long customerId) {
        this.commercialId = customerId;
    }

    public Long getPeriodID() {
        return periodID;
    }

    public void setPeriodID(Long periodID) {
        this.periodID = periodID;
    }

    public String getRealConsumeUser() {
        return realConsumeUser;
    }

    public void setRealConsumeUser(String realConsumeUser) {
        this.realConsumeUser = realConsumeUser;
    }

    public Sex getCommercialGender() {
        return ValueEnums.toEnum(Sex.class, commercialGender);
    }

    public void setCommercialGender(Sex sex) {
        this.commercialGender = ValueEnums.toValue(sex);
    }

    public Integer getIsImportant() {
        return isImportant;
    }

    public boolean isImportant() {
        return isImportant != null && isImportant == 1;
    }

    public void setIsImportant(Integer isImportant) {
        this.isImportant = isImportant;
    }

    public String getNationalTelCode() {
        return nationalTelCode;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }

    public boolean isConfirmed() {
        return true;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setConfirmed(boolean confirmed) {
//		this.confirmed = confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
//		this.confirmed = confirmed;
    }

    @Override
    public Long verValue() {
        return getServerUpdateTime();
    }

    public BookingType getBookingType() {
        return ValueEnums.toEnum(BookingType.class, bookingType);
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = ValueEnums.toValue(bookingType);
    }

    public Integer getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(Integer customerNum) {
        this.customerNum = customerNum;
    }

    public boolean isDinnerGroup() {
        return getBookingType() != null && getBookingType() == BookingType.GROUP;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /*public String getCreatorName() {
		List<User> authUserList = Session.getFunc(UserFunc.class).getUsers();
		if (authUserList != null) {
			for (User user : authUserList) {
				if (creatorID != null && creatorID.equals(user.getId())) {
					return user.getName();
				}
			}
		}
		return null;
	}*/

    /**
     * 内部预订代订人
     */
	/*public String getInnerOrderPersonName() {
		List<User> authUserList = Session.getFunc(UserFunc.class).getUsers();
		if (authUserList != null) {
			for (User user : authUserList) {
				if (innerOrderPerson != null && innerOrderPerson.equals(user.getId())) {
					return user.getName();
				}
			}
		}
		return null;
	}*/

    /**
     * 判断是否老数据预订
     *
     * @return
     */
    public boolean isOldInterface() {
        return getBookingType() == BookingType.NORMAL;
    }
}
