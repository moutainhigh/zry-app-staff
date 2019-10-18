package com.zhongmei.yunfu.db.entity.trade;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.DataEntityBase;
import com.zhongmei.yunfu.db.ICreator;
import com.zhongmei.yunfu.db.IUpdator;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.FixType;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.TakeDishStatus;
import com.zhongmei.yunfu.db.enums.TradePrintStatus;
import com.zhongmei.yunfu.db.enums.TradeServingStatus;


@DatabaseTable(tableName = "trade_extra")
public class TradeExtra extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;


    public interface $ extends DataEntityBase.$ {


        public static final String bindingDeliveryUserTime = "binding_delivery_user_time";


        public static final String callDishStatus = "call_dish_status";


        public static final String called = "called";


        public static final String creatorId = "creator_id";


        public static final String creatorName = "creator_name";


        public static final String deliveryAddress = "delivery_address";


        public static final String deliveryAddressId = "delivery_address_id";


        public static final String deliveryFee = "delivery_fee";


        public static final String deliveryMan = "delivery_man";


        public static final String deliveryRealTime = "delivery_real_time";


        public static final String deliveryStatus = "delivery_status";


        public static final String deliveryUserId = "delivery_user_id";


        public static final String devicePlatform = "device_platform";


        public static final String deviceToken = "device_token";


        public static final String expectTime = "expect_time";


        public static final String fixType = "fix_type";


        public static final String invoiceTitle = "invoice_title";


        public static final String numberPlate = "number_plate";


        public static final String openIdenty = "open_identy";


        public static final String receivedTime = "received_time";


        public static final String receiverName = "receiver_name";


        public static final String receiverPhone = "receiver_phone";


        public static final String receiverSex = "receiver_sex";


        public static final String sendAreaId = "send_area_id";


        public static final String serialNumber = "serial_number";


        public static final String squareUpTime = "square_up_time";


        public static final String thirdTranNo = "third_tran_no";


        public static final String tradeId = "trade_id";


        public static final String tradeUuid = "trade_uuid";


        public static final String updatorId = "updator_id";


        public static final String updatorName = "updator_name";


        public static final String userIdenty = "user_identy";


        public static final String deliveryPlatform = "delivery_platform";


        public static final String thirdSerialNo = "third_serial_no";


        public static final String isPrinted = "is_printed";


        public static final String hasServing = "has_serving";

    }

    @DatabaseField(columnName = "binding_delivery_user_time")
    private Long bindingDeliveryUserTime;

    @DatabaseField(columnName = "call_dish_status")
    private Byte callDishStatus;

    @DatabaseField(columnName = "called")
    private Byte called;

    @DatabaseField(columnName = "creator_id")
    private Long creatorId;

    @DatabaseField(columnName = "creator_name")
    private String creatorName;

    @DatabaseField(columnName = "delivery_address")
    private String deliveryAddress;

    @DatabaseField(columnName = "delivery_address_id")
    private Long deliveryAddressId;

    @DatabaseField(columnName = "delivery_fee")
    private java.math.BigDecimal deliveryFee;

    @DatabaseField(columnName = "delivery_man")
    private String deliveryMan;

    @DatabaseField(columnName = "delivery_real_time")
    private Long deliveryRealTime;

    @DatabaseField(columnName = "delivery_status")
    private Integer deliveryStatus;

    @DatabaseField(columnName = "delivery_user_id")
    private String deliveryUserId;

    @DatabaseField(columnName = "device_platform")
    private String devicePlatform;

    @DatabaseField(columnName = "device_token")
    private String deviceToken;

    @DatabaseField(columnName = "expect_time")
    private Long expectTime;

    @DatabaseField(columnName = "fix_type")
    private Integer fixType;

    @DatabaseField(columnName = "invoice_title")
    private String invoiceTitle;

    @DatabaseField(columnName = "number_plate")
    private String numberPlate;

    @DatabaseField(columnName = "open_identy")
    private String openIdenty;

    @DatabaseField(columnName = "received_time")
    private Long receivedTime;

    @DatabaseField(columnName = "receiver_name")
    private String receiverName;

    @DatabaseField(columnName = "receiver_phone")
    private String receiverPhone;

    @DatabaseField(columnName = "receiver_sex")
    private Integer receiverSex;

    @DatabaseField(columnName = "send_area_id")
    private Long sendAreaId;

    @DatabaseField(columnName = "serial_number")
    private String serialNumber;

    @DatabaseField(columnName = "square_up_time")
    private Long squareUpTime;

    @DatabaseField(columnName = "third_tran_no")
    private String thirdTranNo;

    @DatabaseField(columnName = "trade_id", index = true)
    private Long tradeId;

    @DatabaseField(columnName = "trade_uuid", canBeNull = false)
    private String tradeUuid;

    @DatabaseField(columnName = "updator_id")
    private Long updatorId;

    @DatabaseField(columnName = "updator_name")
    private String updatorName;

    @DatabaseField(columnName = "user_identy")
    private Long userIdenty;

    @DatabaseField(columnName = "delivery_platform", canBeNull = false)
    private Integer deliveryPlatform = DeliveryPlatform.MERCHANT.value();

    @DatabaseField(columnName = "third_serial_no")
    private String thirdSerialNo;

    @DatabaseField(columnName = "is_printed")
    private Integer isPrinted;

    @DatabaseField(columnName = "has_serving")
    private Integer hasServing;

        private String nation;
        private String country;
        private String nationalTelCode;

    public Long getBindingDeliveryUserTime() {
        return bindingDeliveryUserTime;
    }

    public void setBindingDeliveryUserTime(Long bindingDeliveryUserTime) {
        this.bindingDeliveryUserTime = bindingDeliveryUserTime;
    }

    public TakeDishStatus getCallDishStatus() {
        return ValueEnums.toEnum(TakeDishStatus.class, callDishStatus);
    }

    public void setCallDishStatus(TakeDishStatus callDishStatus) {
        this.callDishStatus = ValueEnums.toValue(callDishStatus);
    }

    public Byte getCalled() {
        return called;
    }

    public void setCalled(Byte called) {
        this.called = called;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public java.math.BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(java.math.BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(String deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public Long getDeliveryRealTime() {
        return deliveryRealTime;
    }

    public void setDeliveryRealTime(Long deliveryRealTime) {
        this.deliveryRealTime = deliveryRealTime;
    }

    public DeliveryStatus getDeliveryStatus() {
        return ValueEnums.toEnum(DeliveryStatus.class, deliveryStatus);
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = ValueEnums.toValue(deliveryStatus);
    }

    public String getDeliveryUserId() {
        return deliveryUserId;
    }

    public void setDeliveryUserId(String deliveryUserId) {
        this.deliveryUserId = deliveryUserId;
    }

    public String getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(String devicePlatform) {
        this.devicePlatform = devicePlatform;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Long getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(Long expectTime) {
        this.expectTime = expectTime;
    }

    public FixType getFixType() {
        return ValueEnums.toEnum(FixType.class, fixType);
    }

    public void setFixType(FixType fixType) {
        this.fixType = ValueEnums.toValue(fixType);
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getOpenIdenty() {
        return openIdenty;
    }

    public void setOpenIdenty(String openIdenty) {
        this.openIdenty = openIdenty;
    }

    public Long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Sex getReceiverSex() {
        return ValueEnums.toEnum(Sex.class, receiverSex);
    }

    public void setReceiverSex(Sex receiverSex) {
        this.receiverSex = ValueEnums.toValue(receiverSex);
    }

    public Long getSendAreaId() {
        return sendAreaId;
    }

    public void setSendAreaId(Long sendAreaId) {
        this.sendAreaId = sendAreaId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getSquareUpTime() {
        return squareUpTime;
    }

    public void setSquareUpTime(Long squareUpTime) {
        this.squareUpTime = squareUpTime;
    }

    public String getThirdTranNo() {
        return thirdTranNo;
    }

    public void setThirdTranNo(String thirdTranNo) {
        this.thirdTranNo = thirdTranNo;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
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

    public Long getUserIdenty() {
        return userIdenty;
    }

    public void setUserIdenty(Long userIdenty) {
        this.userIdenty = userIdenty;
    }

    public DeliveryPlatform getDeliveryPlatform() {
        return ValueEnums.toEnum(DeliveryPlatform.class, deliveryPlatform);
    }

    public void setDeliveryPlatform(DeliveryPlatform deliveryPlatform) {
        this.deliveryPlatform = ValueEnums.toValue(deliveryPlatform);
    }

    public String getThirdSerialNo() {
        return thirdSerialNo;
    }

    public void setThirdSerialNo(String thirdSerialNo) {
        this.thirdSerialNo = thirdSerialNo;
    }

    public TradePrintStatus getIsPrinted() {
        return ValueEnums.toEnum(TradePrintStatus.class, isPrinted);
    }

    public void setIsPrinted(TradePrintStatus isPrinted) {
        this.isPrinted = ValueEnums.toValue(isPrinted);
    }

    public TradeServingStatus getHasServing() {
        return ValueEnums.toEnum(TradeServingStatus.class, hasServing);
    }

    public void setHasServing(TradeServingStatus hasServing) {
        this.hasServing = ValueEnums.toValue(hasServing);
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationalTelCode() {
        return nationalTelCode;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }

    @Override
    public boolean checkNonNull() {
        return super.checkNonNull() && checkNonNull(tradeUuid, deliveryPlatform);
    }
}

