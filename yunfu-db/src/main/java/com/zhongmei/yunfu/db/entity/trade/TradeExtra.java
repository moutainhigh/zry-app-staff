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

/**
 * TradeExtra is a ORMLite bean type. Corresponds to the database table "trade_extra"
 */
@DatabaseTable(tableName = "trade_extra")
public class TradeExtra extends DataEntityBase implements ICreator, IUpdator {

    private static final long serialVersionUID = 1L;

    /**
     * The columns of table "trade_extra"
     */
    public interface $ extends DataEntityBase.$ {

        /**
         * binding_delivery_user_time 绑定外卖员时间
         */
        public static final String bindingDeliveryUserTime = "binding_delivery_user_time";

        /**
         * call_dish_status 取餐状态： 0未取餐， 1已取餐
         */
        public static final String callDishStatus = "call_dish_status";

        /**
         * called 是否已叫号：1:未提示 2:已提示
         */
        public static final String called = "called";

        /**
         * creator_id 创建者，创建此记录的系统用户
         */
        public static final String creatorId = "creator_id";

        /**
         * creator_name 创建者姓名
         */
        public static final String creatorName = "creator_name";

        /**
         * delivery_address 送货地址
         */
        public static final String deliveryAddress = "delivery_address";

        /**
         * delivery_address_id 送货地址ID
         */
        public static final String deliveryAddressId = "delivery_address_id";

        /**
         * delivery_fee 送货费
         */
        public static final String deliveryFee = "delivery_fee";

        /**
         * delivery_man 送餐员
         */
        public static final String deliveryMan = "delivery_man";

        /**
         * delivery_real_time 外卖送餐的真实配送时间
         */
        public static final String deliveryRealTime = "delivery_real_time";

        /**
         * delivery_status 送餐状态：0默认等待送餐，1正在配送，2送餐完成，3已清账
         */
        public static final String deliveryStatus = "delivery_status";

        /**
         * delivery_user_id 外卖送餐员的USERID
         */
        public static final String deliveryUserId = "delivery_user_id";

        /**
         * device_platform 设备平台，比如手持设备等(合作方传来的)
         */
        public static final String devicePlatform = "device_platform";

        /**
         * device_token 设备token，比如手持设备等(合作方传来的)
         */
        public static final String deviceToken = "device_token";

        /**
         * expect_time 顾客期望的收货时间。对于外送单据，表示顾客期望送达的时间，为null表示尽快。
         * 对于自提单据，表示预约的取货时间
         */
        public static final String expectTime = "expect_time";

        /**
         * fix_type 选择桌台的方式：(目前只有微信使用)  1:ASSIGN: 顾客自选  2:SCAN_CODE: 扫码找位
         */
        public static final String fixType = "fix_type";

        /**
         * invoice_title 发票抬头
         */
        public static final String invoiceTitle = "invoice_title";

        /**
         * number_plate 号牌
         */
        public static final String numberPlate = "number_plate";

        /**
         * open_identy 微信号
         */
        public static final String openIdenty = "open_identy";

        /**
         * received_time 实际收货时间，为null表示还未送达(或取货)
         * 对于外送单据，表示实际送达的时间
         * 对于自提单据，表示实际来取货的时间
         */
        public static final String receivedTime = "received_time";

        /**
         * receiver_name 收货人姓名
         */
        public static final String receiverName = "receiver_name";

        /**
         * receiver_phone 收货人电话
         */
        public static final String receiverPhone = "receiver_phone";

        /**
         * receiver_sex 1:MALE 男  0:FEMALE 女,  -1:NONE 未知
         */
        public static final String receiverSex = "receiver_sex";

        /**
         * send_area_id 送餐范围id，到店自提时为空
         */
        public static final String sendAreaId = "send_area_id";

        /**
         * serial_number 流水号
         */
        public static final String serialNumber = "serial_number";

        /**
         * square_up_time 清账时间
         */
        public static final String squareUpTime = "square_up_time";

        /**
         * third_tran_no 第三方交易号
         */
        public static final String thirdTranNo = "third_tran_no";

        /**
         * trade_id 交易ID
         */
        public static final String tradeId = "trade_id";

        /**
         * trade_uuid 关联TRADE表的UUID
         */
        public static final String tradeUuid = "trade_uuid";

        /**
         * updator_id 最后修改此记录的用户
         */
        public static final String updatorId = "updator_id";

        /**
         * updator_name 最后修改者姓名
         */
        public static final String updatorName = "updator_name";

        /**
         * user_identy 百度appId
         */
        public static final String userIdenty = "user_identy";

        /**
         * delivery_platform 外送平台：1商家自送 2百度外卖 3蜂鸟（饿了么）4美团配送 5 达达配送
         */
        public static final String deliveryPlatform = "delivery_platform";

        /**
         * third_serial_no 第三方订单流水号
         */
        public static final String thirdSerialNo = "third_serial_no";

        /**
         * is_printed 总单打印状态，1：未打印，2：已打印,3：部分打印
         */
        public static final String isPrinted = "is_printed";

        /**
         * has_serving 总单服务状态，1,未上菜；2，已上菜；3，部分上菜
         */
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

    //国家英文名称
    private String nation;
    //国家中文名称
    private String country;
    //电话国际区码
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

