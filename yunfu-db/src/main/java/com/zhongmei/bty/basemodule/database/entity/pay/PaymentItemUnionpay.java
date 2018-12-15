package com.zhongmei.bty.basemodule.database.entity.pay;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.context.session.auth.IAuthUser;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.UuidEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.bty.commonmodule.database.enums.PosBusinessType;

/**
 * Model class of 银联刷卡记录.
 *
 * @version $Id$
 */
@DatabaseTable(tableName = "payment_item_unionpay")
public class PaymentItemUnionpay extends UuidEntityBase implements ICreator {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "payment_item_unionpay"
     */
    public interface $ extends UuidEntityBase.$ {
        /**
         * status_flag
         */
        String statusFlag = "status_flag";

        /**
         * brand_identy
         */
        String brandIdenty = "brand_identy";

        /**
         * id
         */
        String id = "id";

        /**
         * server_create_time
         */
        String serverCreateTime = "server_create_time";

        /**
         * server_update_time
         */
        String serverUpdateTime = "server_update_time";

        /**
         * shop_identy
         */
        String shopIdenty = "shop_identy";

        /**
         * device_identy
         */
        String deviceIdenty = "device_identy";

        /**
         * creator_id
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name
         */
        public static final String creatorName = "creator_name";

        public static final String paymentItemId = "payment_item_id";

        public static final String paymentItemUuid = "payment_item_uuid";

        public static final String transDate = "trans_date";

        public static final String transType = "trans_type";

        public static final String hostSerialNumber = "host_serial_number";

        public static final String posTraceNumber = "pos_trace_number";

        public static final String batchNumber = "batch_number";

        //public static final String terminalNumber = "terminal_number";

        public static final String posChannelId = "pos_channel_id";

        public static final String paymentCardId = "payment_card_id";

        public static final String paymentDeviceId = "payment_device_id";

        public static final String usefulAmount3 = "useful_amount";

    }

    /**
     * 状态
     */
    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;

    /**
     * 品牌Identy
     */
    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;

    /**
     * 门店Identy
     */
    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;

    /**
     * 设备Identy
     */
    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    @DatabaseField(columnName = "id")
    private Long id;

    /**
     * 服务器创建时间
     */
    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;

    /**
     * 服务器最后修改时间
     */
    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "payment_item_id")
    private Long paymentItemId;

    /**
     * 支付明细uuid.
     */
    @DatabaseField(columnName = "payment_item_uuid")
    private String paymentItemUuid;

    /**
     * 交易日期.
     */
    @DatabaseField(columnName = "trans_date")
    private Long transDate;

    /**
     * 交易类型.
     */
    @DatabaseField(columnName = "trans_type")
    private Integer transType;

    /**
     * 金额.
     */
    @DatabaseField(columnName = "amount")
    private Long amount;

    /**
     * 费率.
     */
    @DatabaseField(columnName = "rates")
    private Double rates;

    /**
     * 手续费.
     */
    @DatabaseField(columnName = "fee")
    private Double fee;

    /**
     * 系统参考号.
     */
    @DatabaseField(columnName = "host_serial_number")
    private String hostSerialNumber;

    /**
     * 流水号.
     */
    @DatabaseField(columnName = "pos_trace_number")
    private String posTraceNumber;

    /**
     * 批次号.
     */
    @DatabaseField(columnName = "batch_number")
    private String batchNumber;

    /** 终端号. *//*
	@DatabaseField(columnName = "terminal_number")
	private String terminalNumber;*/

    /**
     * pos渠道id.
     */
    @DatabaseField(columnName = "pos_channel_id")
    private Long posChannelId;

    /**
     * 银行卡id.
     */
    @DatabaseField(columnName = "payment_card_id")
    private Long paymentCardId;

    /**
     * 刷卡pos设备id.
     */
    @DatabaseField(columnName = "payment_device_id")
    private Long paymentDeviceId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    /**
     * 刷卡pos设备id.
     */
    @DatabaseField(columnName = "appname")
    private String appname;

    public StatusFlag getStatusFlag() {
        return ValueEnums.toEnum(StatusFlag.class, statusFlag);
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        this.statusFlag = ValueEnums.toValue(statusFlag);
    }

    public Long getBrandIdenty() {
        return brandIdenty;
    }

    public void setBrandIdenty(Long brandIdenty) {
        this.brandIdenty = brandIdenty;
    }

    public Long getShopIdenty() {
        return shopIdenty;
    }

    public void setShopIdenty(Long shopIdenty) {
        this.shopIdenty = shopIdenty;
    }

    public String getDeviceIdenty() {
        return deviceIdenty;
    }

    public void setDeviceIdenty(String deviceIdenty) {
        this.deviceIdenty = deviceIdenty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Long serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    /**
     * Set the 支付明细uuid.
     *
     * @param paymentItemUuid 支付明细uuid
     */
    public void setPaymentItemUuid(String paymentItemUuid) {
        this.paymentItemUuid = paymentItemUuid;
    }

    /**
     * Get the 支付明细uuid.
     *
     * @return 支付明细uuid
     */
    public String getPaymentItemUuid() {
        return this.paymentItemUuid;
    }

    /**
     * Set the 金额.
     *
     * @param amount 金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * Get the 金额.
     *
     * @return 金额
     */
    public Long getAmount() {
        return this.amount;
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

    public Long getPaymentItemId() {
        return paymentItemId;
    }

    public void setPaymentItemId(Long paymentItemId) {
        this.paymentItemId = paymentItemId;
    }

    public Double getRates() {
        return rates;
    }

    public void setRates(Double rates) {
        this.rates = rates;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Long getPosChannelId() {
        return posChannelId;
    }

    public void setPosChannelId(Long posChannelId) {
        this.posChannelId = posChannelId;
    }

    public Long getPaymentCardId() {
        return paymentCardId;
    }

    public void setPaymentCardId(Long paymentCardId) {
        this.paymentCardId = paymentCardId;
    }

    public Long getPaymentDeviceId() {
        return paymentDeviceId;
    }

    public void setPaymentDeviceId(Long paymentDeviceId) {
        this.paymentDeviceId = paymentDeviceId;
    }

    public String getAppName() {
        return appname;
    }

    public void setAppName(String appName) {
        this.appname = appName;
    }

    public Long getTransDate() {
        return transDate;
    }

    public void setTransDate(Long transDate) {
        this.transDate = transDate;
    }

    public PosBusinessType getTransType() {
        return ValueEnums.toEnum(PosBusinessType.class, transType);
    }

    public void setTransType(PosBusinessType transType) {
        this.transType = ValueEnums.toValue(transType);
    }

    public String getHostSerialNumber() {
        return hostSerialNumber;
    }

    public void setHostSerialNumber(String hostSerialNumber) {
        this.hostSerialNumber = hostSerialNumber;
    }

    public String getPosTraceNumber() {
        return posTraceNumber;
    }

    public void setPosTraceNumber(String posTraceNumber) {
        this.posTraceNumber = posTraceNumber;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
	
	/*public String getTerminalNumber() {
		return terminalNumber;
	}
	
	public void setTerminalNumber(String terminalNumber) {
		this.terminalNumber = terminalNumber;
	}*/

    @Override
    public Long verValue() {
        return serverUpdateTime;
    }

    @Override
    public boolean isValid() {
        return ValueEnums.equalsValue(StatusFlag.VALID, statusFlag);
    }

    public void validateCreate() {
        setStatusFlag(StatusFlag.VALID);
        setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
        if (this instanceof ICreator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                ICreator creator = (ICreator) this;
                creator.setCreatorId(user.getId());
                creator.setCreatorName(user.getName());
            }
        }
        validateUpdate();
    }

    public void validateUpdate() {
        setChanged(true);
        if (this instanceof IUpdator) {
            IAuthUser user = IAuthUser.Holder.get();
            if (user != null) {
                IUpdator updator = (IUpdator) this;
                updator.setUpdatorId(user.getId());
                updator.setUpdatorName(user.getName());
            }
        }
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(statusFlag, brandIdenty, shopIdenty);
    }
}
