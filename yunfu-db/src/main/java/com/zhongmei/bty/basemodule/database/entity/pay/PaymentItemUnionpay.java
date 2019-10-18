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


@DatabaseTable(tableName = "payment_item_unionpay")
public class PaymentItemUnionpay extends UuidEntityBase implements ICreator {


    private static final long serialVersionUID = 1L;


    public interface $ extends UuidEntityBase.$ {

        String statusFlag = "status_flag";


        String brandIdenty = "brand_identy";


        String id = "id";


        String serverCreateTime = "server_create_time";


        String serverUpdateTime = "server_update_time";


        String shopIdenty = "shop_identy";


        String deviceIdenty = "device_identy";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";

        public static final String paymentItemId = "payment_item_id";

        public static final String paymentItemUuid = "payment_item_uuid";

        public static final String transDate = "trans_date";

        public static final String transType = "trans_type";

        public static final String hostSerialNumber = "host_serial_number";

        public static final String posTraceNumber = "pos_trace_number";

        public static final String batchNumber = "batch_number";


        public static final String posChannelId = "pos_channel_id";

        public static final String paymentCardId = "payment_card_id";

        public static final String paymentDeviceId = "payment_device_id";

        public static final String usefulAmount3 = "useful_amount";

    }


    @DatabaseField(columnName = "status_flag", canBeNull = false)
    private Integer statusFlag;


    @DatabaseField(columnName = "brand_identy", canBeNull = false)
    private Long brandIdenty;


    @DatabaseField(columnName = "shop_identy", canBeNull = false)
    private Long shopIdenty;


    @DatabaseField(columnName = "device_identy")
    private String deviceIdenty;

    @DatabaseField(columnName = "id")
    private Long id;


    @DatabaseField(columnName = "server_create_time")
    private Long serverCreateTime;


    @DatabaseField(columnName = "server_update_time")
    private Long serverUpdateTime;

    @DatabaseField(columnName = "payment_item_id")
    private Long paymentItemId;


    @DatabaseField(columnName = "payment_item_uuid")
    private String paymentItemUuid;


    @DatabaseField(columnName = "trans_date")
    private Long transDate;


    @DatabaseField(columnName = "trans_type")
    private Integer transType;


    @DatabaseField(columnName = "amount")
    private Long amount;


    @DatabaseField(columnName = "rates")
    private Double rates;


    @DatabaseField(columnName = "fee")
    private Double fee;


    @DatabaseField(columnName = "host_serial_number")
    private String hostSerialNumber;


    @DatabaseField(columnName = "pos_trace_number")
    private String posTraceNumber;


    @DatabaseField(columnName = "batch_number")
    private String batchNumber;




    @DatabaseField(columnName = "pos_channel_id")
    private Long posChannelId;


    @DatabaseField(columnName = "payment_card_id")
    private Long paymentCardId;


    @DatabaseField(columnName = "payment_device_id")
    private Long paymentDeviceId;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;


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


    public void setPaymentItemUuid(String paymentItemUuid) {
        this.paymentItemUuid = paymentItemUuid;
    }


    public String getPaymentItemUuid() {
        return this.paymentItemUuid;
    }


    public void setAmount(Long amount) {
        this.amount = amount;
    }


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
