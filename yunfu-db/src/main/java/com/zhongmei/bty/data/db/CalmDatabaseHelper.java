package com.zhongmei.bty.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhongmei.bty.basemodule.MultyFileSetting;
import com.zhongmei.bty.basemodule.PosConfig;
import com.zhongmei.bty.basemodule.auth.user.entity.AuthBrandBusiness;
import com.zhongmei.bty.basemodule.booking.bean.BookingGroupInfo;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.booking.BookingSetting;
import com.zhongmei.yunfu.db.entity.booking.BookingTable;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.bty.basemodule.booking.entity.BookingTradeItemProperty;
import com.zhongmei.bty.basemodule.commonbusiness.entity.AppPrivilege;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;
import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialOrderSource;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerDeliveryPlatformConfig;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSettingSwitch;
import com.zhongmei.bty.basemodule.commonbusiness.entity.SyncDict;
import com.zhongmei.bty.basemodule.commonbusiness.entity.TaxRateInfo;
import com.zhongmei.yunfu.db.entity.SyncMark;
import com.zhongmei.bty.basemodule.customer.db.CoupDish;
import com.zhongmei.bty.basemodule.customer.entity.CrmCustomerLevelRightsDish;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerThreshold;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRule;
import com.zhongmei.yunfu.db.entity.crm.CrmLevelStoreRuleDetail;
import com.zhongmei.bty.basemodule.customer.entity.CrmMemberDay;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.db.entity.crm.CustomerLevel;
import com.zhongmei.bty.basemodule.customer.entity.EcCardKindCommercialRel;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTemplet;
import com.zhongmei.bty.basemodule.customer.entity.MemberPriceTempletDetail;
import com.zhongmei.bty.basemodule.customer.entity.RejectReason;
import com.zhongmei.bty.basemodule.customer.entity.TakeawayAddress;
import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.basemodule.database.db.CashHandoverConfig;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentDevice;
import com.zhongmei.bty.basemodule.database.entity.pay.PaymentItemUnionpay;
import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandover;
import com.zhongmei.bty.basemodule.database.entity.shopmanager.CashHandoverItem;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueDetailImage;
import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.bty.basemodule.devices.display.data.ErpPosPushDetail;
import com.zhongmei.bty.basemodule.devices.display.data.MarketActivitySendinfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevel;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardSettingDetail;
import com.zhongmei.yunfu.db.entity.discount.CoupRule;
import com.zhongmei.yunfu.db.entity.discount.Coupon;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.bty.basemodule.discount.entity.DiscountShop;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.entity.ExtraChargeCommercialAreaRef;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivity;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityDish;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityWeekday;
import com.zhongmei.yunfu.db.entity.discount.MarketDynamicCondition;
import com.zhongmei.yunfu.db.entity.discount.MarketPlan;
import com.zhongmei.yunfu.db.entity.discount.MarketPlanCommercialRel;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.bean.ErpMessagePushDetail;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;
import com.zhongmei.bty.basemodule.notifycenter.entity.CallWaiter;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemBatch;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarte;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteDetail;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteNorms;
import com.zhongmei.yunfu.db.entity.dish.DishCyclePeriod;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishSetmealGroup;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItem;
import com.zhongmei.bty.basemodule.orderdish.entity.KdsTradeItemPart;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraTakeaway;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.dish.DishBrandType;
import com.zhongmei.bty.basemodule.pay.entity.ElectronicInvoice;
import com.zhongmei.bty.basemodule.pay.entity.InvoiceTaxRate;
import com.zhongmei.yunfu.db.entity.MobilePaySetting;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.PaymentItemExtra;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGroupon;
import com.zhongmei.bty.basemodule.pay.entity.PaymentItemGrouponDish;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeScene;
import com.zhongmei.yunfu.db.entity.trade.PaymentModeShop;
import com.zhongmei.bty.basemodule.pay.entity.RefundExceptionReason;
import com.zhongmei.bty.basemodule.print.entity.PrintConfigure;
import com.zhongmei.bty.basemodule.print.entity.PrintContent;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.print.entity.PrintOperationLocalStatus;
import com.zhongmei.bty.basemodule.print.entity.PrinterCashierTicketDevice;
import com.zhongmei.bty.basemodule.print.entity.PrinterCashierTicketType;
import com.zhongmei.bty.basemodule.print.entity.PrinterDishDocument;
import com.zhongmei.bty.basemodule.print.entity.PrinterDocument;
import com.zhongmei.bty.basemodule.print.entity.PrinterStyleSetting;
import com.zhongmei.bty.basemodule.print.entity.PrinterTableDocument;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMruleActivityDish;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMruleMarketRule;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMrulePlan;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMrulePolicyDish;
import com.zhongmei.bty.basemodule.session.ver.v1.db.AuthDataPermission;
import com.zhongmei.bty.basemodule.session.ver.v1.db.AuthUserEntity;
import com.zhongmei.bty.basemodule.session.ver.v1.db.AuthUserPermission;
import com.zhongmei.bty.basemodule.shopmanager.closing.entity.ClosingAccountRecord;
import com.zhongmei.bty.basemodule.shopmanager.entity.Brand;
import com.zhongmei.yunfu.db.entity.OpenTime;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;
import com.zhongmei.bty.basemodule.trade.entity.Invoice;
import com.zhongmei.bty.basemodule.trade.entity.TableType;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSetting;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSettingItem;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoiceNo;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeMainSubRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.yunfu.db.entity.trade.TradeReturnInfo;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.context.AppBuildConfig;
import com.zhongmei.yunfu.orm.BeautyDbHelperUtil;
import com.zhongmei.bty.commonmodule.data.db.AccountSubject;
import com.zhongmei.bty.commonmodule.data.db.AccountSubjectDetail;
import com.zhongmei.bty.commonmodule.data.db.DictionaryDetail;
import com.zhongmei.bty.commonmodule.data.db.PaymentModeBrand;
import com.zhongmei.yunfu.orm.SQLiteDatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.AuthBusiness;
import com.zhongmei.bty.commonmodule.database.entity.AuthUserShop;
import com.zhongmei.bty.commonmodule.database.entity.CommercialQueueConfigFile;
import com.zhongmei.yunfu.db.entity.crm.CrmCustomerRightsConfig;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.entity.NewDocumentTemplate;
import com.zhongmei.bty.commonmodule.database.entity.EmployeeIdentity;
import com.zhongmei.bty.commonmodule.database.entity.InitSystem;
import com.zhongmei.bty.commonmodule.database.entity.LydMarketSetting;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.bty.commonmodule.database.entity.Period;
import com.zhongmei.bty.commonmodule.database.entity.PrinterCashierTicket;
import com.zhongmei.bty.commonmodule.database.entity.PrinterDevice;
import com.zhongmei.bty.commonmodule.database.entity.QrCodeInfo;
import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.bty.commonmodule.database.entity.TradeItemLog;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.IEntity;
import com.zhongmei.bty.commonmodule.database.entity.local.PayMenuOrder;
import com.zhongmei.bty.commonmodule.database.entity.local.PosSettlementLog;
import com.zhongmei.bty.commonmodule.database.entity.local.PosTransLog;
import com.zhongmei.yunfu.context.Constant;
import com.zhongmei.bty.data.db.common.BrandExt;
import com.zhongmei.bty.data.db.common.ClientPrinterRel;
import com.zhongmei.bty.data.db.common.Commercial;
import com.zhongmei.bty.data.db.common.CommercialGroupSetting;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.data.db.common.SysFile;
import com.zhongmei.bty.data.db.common.TablePhysicalLayout;
import com.zhongmei.bty.data.db.common.TablePosition;
import com.zhongmei.bty.data.db.common.queue.QueueImage;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 */
public class CalmDatabaseHelper extends SQLiteDatabaseHelper {

    private static final String TAG = CalmDatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "calm.db";

    /**
     * 4011 --> 5.9
     * 4012 --> 5.10
     * 4013 --> 5.11
     * 4014 --> 5.12
     * 4022 --> 5.13
     * 4035 --> 6.0
     * 4036 --> 6.1
     * 4040 --> 6.1.2
     * 4041 --> 6.2
     * 4042 --> 6.3
     * 4043 --> 6.4
     * 4045 --> 6.5
     * 4046 --> 6.6
     * 4050 --> 6.7(实体卡)
     * 4051 --> 6.7.1(加入菜单循环周期表)
     * 4060 --> 6.8(营销活动，菜品操作记录)
     * 4070 --> 6.9(订单押金信息, 商户设置表,打印里加入开启商品和桌台设置开关)
     * 4080 --> 6.10(票据样式表，到期提醒)
     * 4090 --> 6.11(客户端打印配置表，挂账记录表，添加打印机健康状态字段,实体卡种与商户关联表)
     * 4100 --> 6.12(添加商户合同状态表,收支管理科目表,自定义票据三张表:print_configure,print_configure,printer_style_setting)
     * 4110 --> 7.1(添加业务权限表，授权记录表)
     * 4120 --> 7.2(TradeExtra中添加打印状态和上餐状态)
     * 4130 --> 7.3(添加关账时间记录表closing_account_record,添加服务铃表,print_device增加蜂鸣开关字段,添加收银点-出票口表以及与票据对应的关系表,TradeItem扩展表trade_item_extra)
     * 4140 --> 7.4(添加异步网络请求记录表,收银点-设备关系表,设备表,加菜详情表,加菜批次表)
     * 4150 --> 7.5(TradeStatusLog中添加支付状态字段,添加闪惠优惠表)
     * 4160 --> 7.6(PrintOperation添加收银点字段,Invoice表,ElectronicInvoice表,InvoiceTaxRate表,brand品牌表(ps:部分字段),crm_customer_threshold表，新增commercial_group_setting表，customer添加customerMainId、bindFlag字段，queue添加customerId字段,payment 添加shopactualamount)
     * 4170 --> 7.7(新增 LydMarketSetting,ErpPosPushDetail表,ReasonSettingSwitch表,ErpMessagePushDetail)
     * 4180 --> 7.8(新增 PayMenuOrder表,新增表索引)
     * 4190 --> 7.9(printer_cashier_ticket_type表新增堂口单一菜一纸套餐子菜是否打印到一张纸)(remove table AsyncHttpRecord,AuthorizedLog,BaiduSyntheticSpeech,ContractOverdue,TimePrintInfo,PayMenuOrder,PosTransLog,PosSettlementLog to local.DB)
     * 4200 --> 7.9.1(去除customer表，trade_credit_log中添加customer_name和customer_phone两个冗余字段)
     * 4210 --> 7.10(printer_cashier_ticket去除备用打印机两个字段)
     * 4220 --> 7.11(新增QueueExtra)
     * 4230 --> 7.12(TradeReceiveLog表添加接受方记录，AsyncHttpRecord添加label_json字段)
     * 4240 --> 7.13(新增PaymentModeScene, KdsTradeItem, KdsTradeItemPart)
     * 4250 -->7.14 group(新增 菜单模板相关表：DishCarte,DishCarteDetail,DishCarteNorms)、buffet(新增 顾客对象人数表 TradeBuffetPeople,TradeDepositPayRelation 押金订单支付关系表),打印（printer_device表新增printerDeviceModel字段表示打印机型号）
     * 4260 --> 7.15(添加DeliveryOrder订单配送信息表、TradeInvoice、TradePrivilegeExtra:优惠扩展、(printer_document) 增加isShowMark、isShowOrderTime、isShowShopPhone、isShowShopAddress字段;)
     * 4260 -->7.15 (TradePrivilegeExtra:优惠扩展) (添加DeliveryOrder订单配送信息表)
     * 4260 --> 7.15(添加DeliveryOrder订单配送信息表、TradeInvoice、TradePrivilegeExtra:优惠扩展、(printer_document) 增加isShowMark、isShowOrderTime、isShowShopPhone、isShowShopAddress字段),group(新增团餐信息：TradeGroupInfo)
     * 4270 --> 8.0(添加PrintOperationLocalStatus表localPrintStatus本地状态) 新建表BookingTradeItem和BookingTradeItemProperty
     * 4270 --> 8.0(print_operationi添加localPrintStatus本地状态) , BookingTradeGroupInfo:团餐预定表
     * 4270 --> 8.0(print_operationi添加localPrintStatus本地状态) 新建表BookingTradeItem和BookingTradeItemProperty
     * 4280 -->8.1(新增TradeItemExtraDinner表, ErpMessagePushDetail表增加(effectiveDate,expiryDate),tradeUser表,EmployeeIdentity 表）
     * 4290 -->8.2(InitSystem表新增is_kds_device字段，TradeItemExtraDinner新增servingOrder字段,新增CrmCustomerRightsConfig表）
     * 4300 -->8.3(新增ErpCurrency表 ,PaymentItemGroupDish表, tradeGroupInfo 添加 团餐差额字段 ， ec_card_kind 添加 workStatus 卡状态）
     * 4310 -->8.4 (菜品批次表相关：TradeItemMainBatchRel,TradeItemMainBatchRelExtra,TradeMainSubRelation;queue表增加remindCount,remindTime,AsyncHttpRecord删除custom_json、kitchen_json、label_json、添加print_bean_json)
     * 4320 -->8.4.2(数据库升级时移除AsyncHttpRecord)
     * 4330 -->8.5(税率：TaxRateInfo,TradeTax)
     * 4340 -->8.6(品牌logo：MultyFileSetting)
     * 4350 -->8.7(修改身份表:EmployeeIdentity)
     * 4360 -->8.8增加了表QueueDetailImage, QueueImage增加字段uuid、修改了新权限下，资源表level_id的字段类型
     * 4370 -->8.9增加了AppPrivilege id属性
     * 4371 -->8.9.2修改TradeItemPlanActivity、TradeItemExtra、TradeReceiveLog表
     * 4380 -->9.0添加TradeInvoiceNo表,TaxRateInfo增加了(effectType,discountType,taxKind,taxMethod,taxDesc)去除了(isTaxOpen,taxName)属性, TradeTax增加了(effectType,discountType,taxKind,taxMethod)，ExtraCharge增加了(businessType,taxCode,privilegeFlag)，
     * 4390 -->10.0修改TradeItemPlanActivity、TradeItemExtra、TradeReceiveLog表
     * 4400 -->8.11.0异步交接添加CashHandover、CashHandoverItem表，服务费配置TradeInitConfig，添加退款失败原因表RefundExceptionReason
     * 4410 -->8.12.0销售员表EmployeeIdentity添加停用属性enableStatus，CrmLevelStoreRule表添加免密支付配置项字段consumeOrder、groupId、payNoPwd、payNoPwdAmount，添加MobilePaySetting表
     * 4420 -->8.12.0添加异步是否可用配置表PosConfig
     * 4430 -->8.13.0销货单定金表TradeEarnestMoney
     * 4440 -->8.14.0ExtraCharge添加了属性enableTableArea,添加ExtraChargeCommercialAreaRef表,将表document_templet改名为new_document_template
     * 4450 -->8.15.0添加VerifyKoubeiOrder表
     */
    private final int DATABASE_VERSION = AppBuildConfig.VERSION_CODE;

    public static final List<Class<? extends IEntity<?>>> TABLES;

    static {
        ArrayList<Class<? extends IEntity<?>>> tables = new ArrayList<Class<? extends IEntity<?>>>();

        //老表处理
        //tables.add(CashType.class);
        tables.add(CustomerGroupLevel.class);
        tables.add(CustomerLevel.class);
        tables.add(TakeawayMemo.class);
        tables.add(TakeawayAddress.class);
        tables.add(RejectReason.class);

        tables.add(EcCardLevelSetting.class);
        tables.add(EcCardSettingDetail.class);
        tables.add(PrepareTradeRelation.class);
        tables.add(BookingSetting.class);
        tables.add(PrepareTradeRelation.class);
//		tables.add(GlobalSettings.class);
        tables.add(QrCodeInfo.class);
        tables.add(QueueImage.class);
        tables.add(QueueDetailImage.class);
        tables.add(SysFile.class);
        tables.add(CommercialQueueConfigFile.class);
        tables.add(CommercialOrderSource.class);
        tables.add(CashHandoverConfig.class);
        tables.add(OrderNotify.class);
        tables.add(CrmLevelStoreRule.class);
        tables.add(CrmLevelStoreRuleDetail.class);
        tables.add(BrandExt.class);
        tables.add(CrmCustomerLevelRights.class);
        tables.add(CrmCustomerLevelRightsDish.class);
        tables.add(PaymentModeShop.class);
        tables.add(CommercialQueueLine.class);
        tables.add(Queue.class);
        tables.add(QueueExtra.class);
        tables.add(Period.class);
        //tables.add(Period.class);
        tables.add(ErpCommercialRelation.class);
        tables.add(Booking.class);
        tables.add(BookingTable.class);
        tables.add(Tables.class);
        tables.add(TableType.class);
        tables.add(ReasonSetting.class);
        // tables.add(SourceInfo.class);
        tables.add(Trade.class);
        tables.add(TradeExtra.class);
        tables.add(TradeCustomer.class);
        tables.add(TradeTable.class);
        tables.add(TradeItem.class);
        tables.add(TradeItemProperty.class);
        tables.add(TradePrivilege.class);
        tables.add(TradeStatusLog.class);
        tables.add(TradeItemLog.class);
        tables.add(TradeReasonRel.class);
        tables.add(Payment.class);
        tables.add(PaymentItem.class);
        tables.add(PaymentItemExtra.class);
        tables.add(PrinterDevice.class);
        tables.add(PrinterDocument.class);
        tables.add(PrinterDishDocument.class);
        tables.add(PrinterTableDocument.class);
        tables.add(PrintContent.class);
        tables.add(TableNumberSetting.class);
        tables.add(DishUnitDictionary.class);
        tables.add(DishPropertyType.class);
        tables.add(DishBrandType.class);
        tables.add(DishProperty.class);
        tables.add(DishBrandProperty.class);
        tables.add(DishSetmealGroup.class);
        tables.add(DishSetmeal.class);
        tables.add(DishShop.class);
        tables.add(OpenTime.class);
        tables.add(Coupon.class);
        tables.add(CoupRule.class);
        tables.add(CoupDish.class);
        tables.add(TradeDealSetting.class);
        tables.add(TradeDealSettingItem.class);
        tables.add(CommercialArea.class);
        tables.add(TablePhysicalLayout.class);
        tables.add(TablePosition.class);
        tables.add(AuthUserEntity.class);
        tables.add(AuthUserShop.class);
        tables.add(AuthUserPermission.class);
        tables.add(AuthDataPermission.class);

//        --新权限
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountRoleEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionDetailEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthDataPermissionEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthPermissionEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthPermissionResourceEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthResourceEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthRoleEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthRolePermissionEntity.class);
        tables.add(com.zhongmei.bty.basemodule.session.ver.v2.db.AuthUserEntity.class);
//        --新权限

        tables.add(DiscountShop.class);
        tables.add(MemberPriceTemplet.class);
        tables.add(MemberPriceTempletDetail.class);
        tables.add(PaymentItemUnionpay.class);
        tables.add(PaymentDevice.class);
        tables.add(ExtraCharge.class);
        tables.add(PrintOperation.class);
        tables.add(EcCardKind.class);
        tables.add(EcCardLevel.class);
        tables.add(MarketActivitySendinfo.class);
        tables.add(TradeItemOperation.class);

        tables.add(SyncMark.class);
        tables.add(TradeReturnInfo.class);
        tables.add(DishCyclePeriod.class);
        // add 2016-04-21  营销活动相关的表
        tables.add(MarketActivity.class);
        tables.add(MarketActivityRule.class);
        tables.add(MarketDynamicCondition.class);
        tables.add(MarketActivityDish.class);
        tables.add(MarketActivityWeekday.class);
        tables.add(MarketPlan.class);
        tables.add(TradePlanActivity.class);
        tables.add(MarketPlanCommercialRel.class);//add 20160601
        tables.add(TradeItemPlanActivity.class);
        tables.add(TradeDeposit.class);
        tables.add(CommercialCustomSettings.class);
        tables.add(CommercialGroupSetting.class);
        tables.add(PrinterStyleSetting.class);
        tables.add(PrintConfigure.class);
        tables.add(NewDocumentTemplate.class);
        tables.add(ClosingAccountRecord.class); //add 20160921
        tables.add(ClientPrinterRel.class);
        tables.add(TradeCreditLog.class);
        tables.add(EcCardKindCommercialRel.class);
        tables.add(Commercial.class);
        tables.add(AccountSubject.class);
        tables.add(AuthBusiness.class);
        tables.add(AuthBrandBusiness.class);
        tables.add(CallWaiter.class);
        // 增加收银点-出票口及收银点-出票口与票据关系表
        tables.add(PrinterCashierTicket.class);
        tables.add(PrinterCashierTicketType.class);
        tables.add(ClosingAccountRecord.class);
        tables.add(TradeItemExtra.class);
        tables.add(PaymentItemGroupon.class);

        //增加设备表
        tables.add(InitSystem.class);
        tables.add(PrinterCashierTicketDevice.class);
        //新增加菜详情表,加菜批次表
        tables.add(AddItemBatch.class);
        tables.add(AddItemRecord.class);

        // 增加闪惠表
        tables.add(TradePromotion.class);
        tables.add(Invoice.class);
        tables.add(ElectronicInvoice.class);
        tables.add(InvoiceTaxRate.class);
        tables.add(CrmCustomerThreshold.class);
        tables.add(Brand.class);
        tables.add(LydMarketSetting.class);
        tables.add(ErpPosPushDetail.class);
        tables.add(ErpMessagePushDetail.class);
        tables.add(ReasonSettingSwitch.class);
        tables.add(PartnerShopBiz.class);
        tables.add(SyncDict.class);
        tables.add(TradeReceiveLog.class);


        //7.9移到本地库
        //异步网络请求记录表(7.4添加)
        //tables.add(AsyncHttpRecord.class);
        //tables.add(AuthorizedLog.class);
        //tables.add(BaiduSyntheticSpeech.class);
        //tables.add(ContractOverdue.class);
        //tables.add(TimePrintInfo.class);
        //添加支付菜单顺序记录
        // tables.add(PayMenuOrder.class);//20170119
        // liandiPOS刷卡交易记录
        // tables.add(PosTransLog.class);
        //tables.add(PosSettlementLog.class);
        tables.add(PaymentModeScene.class);//20170509,7.11.2添加支付方式场景配置
        tables.add(KdsTradeItem.class);
        tables.add(KdsTradeItemPart.class);

        tables.add(DishCarte.class);
        tables.add(DishCarteDetail.class);
        tables.add(DishCarteNorms.class);
        tables.add(TradeBuffetPeople.class);
        tables.add(TradeDepositPayRelation.class);
        tables.add(DeliveryOrder.class);
        tables.add(PartnerDeliveryPlatformConfig.class);
        tables.add(DeliveryOrderRecord.class);

        tables.add(TradePrivilegeExtra.class);
        tables.add(TradeInvoice.class);
        tables.add(TradeGroupInfo.class); // v 7.15 加入团餐数据
        tables.add(PrintOperationLocalStatus.class);
        tables.add(BookingTradeItem.class);
        tables.add(BookingTradeItemProperty.class);
        tables.add(BookingGroupInfo.class); // v 7.16 加入团餐预定表

        tables.add(TradeItemExtraDinner.class);// v8.1新加
        tables.add(TradeItemExtraTakeaway.class);//v8.1新加 篮子表
        tables.add(TableSeat.class);//座位表
        tables.add(TradeUser.class);//推销员表
        tables.add(EmployeeIdentity.class);//销售员身份信息
        //tables.add(IdentityMapper.class);
        tables.add(CrmCustomerRightsConfig.class);//v8.2 虚拟会员会员价限制开关配置
        tables.add(ErpCurrency.class);//v8.3 添加国籍
        tables.add(PaymentItemGrouponDish.class);//v8.3 菜品与美团券关联关系表
        tables.add(TradeMainSubRelation.class);//v8.3 添加联台订单关联表
        tables.add(TradeItemMainBatchRel.class);////v8.3 添加联台菜品关联表
        tables.add(TradeExtraSecrecyPhone.class);//V8.4 第三方隐私手机号
        tables.add(TradeItemMainBatchRelExtra.class);
        tables.add(TradeTax.class);
        tables.add(TaxRateInfo.class);
        tables.add(MultyFileSetting.class);
        tables.add(AppPrivilege.class); //8.7新增购买服务
        tables.add(TradeInvoiceNo.class); //9.0消费税号表
        tables.add(CashHandover.class);//异步交接:交接表
        tables.add(CashHandoverItem.class);//异步交接:交接明细表
        tables.add(AccountSubjectDetail.class);//异步交接：收支明细表
        tables.add(DictionaryDetail.class);//异步交接：字典表
        tables.add(PaymentModeBrand.class);//异步交接：品牌支付配置表
        tables.add(TradeInitConfig.class);
        tables.add(RefundExceptionReason.class);//退款原因表
        tables.add(MobilePaySetting.class);//add v8.12 移动支付设置
        // v8.12.0 会员价
        tables.add(CrmMemberDay.class);
        tables.add(PosConfig.class);//add 8.12 pos server设置，离线是否可用
        //v8.13
        tables.add(TradeEarnestMoney.class);
        tables.add(ExtraChargeCommercialAreaRef.class);

        //新促销
        tables.add(LoytMrulePlan.class);
        tables.add(LoytMruleActivityDish.class);
        tables.add(LoytMruleMarketRule.class);
        tables.add(LoytMrulePolicyDish.class);

        tables.add(VerifyKoubeiOrder.class);

        BeautyDbHelperUtil.initTables(tables);
        TABLES = Collections.unmodifiableList(tables);
    }


    public CalmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, AppBuildConfig.VERSION_CODE);
    }

    /**
     * 创建SQLite数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        Log.i(TAG, "Creating database...");
        // 创建使用DataBaseInfo的表
        /*for (Class<?> cls : DataBaseTableList.getAllTableList()) {
            DataBaseInfo dataBaseInfo = (DataBaseInfo) ReflectHelper.newInstance(cls.getName());
            String createTableSql = dataBaseInfo.createTableSQL();
            Log.i(TAG, createTableSql);
            sqliteDatabase.execSQL(createTableSql);
            dataBaseInfo.insertDefaultValue(sqliteDatabase);
        }*/

        // 创建使用ORMLite的表
        try {
            for (int i = 0; i < TABLES.size(); i++) {
                TableUtils.createTableIfNotExists(connectionSource, TABLES.get(i));
            }
        } catch (SQLException ex) {
            Log.e(TAG, "Unable to create database", ex);
        }
    }

    /**
     * 更新SQLite数据库
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        Constant.setIsNeedInit(true);
        Log.i(TAG, "Upgrading database from version " + oldVer + " to " + newVer);
        /*for (Class<?> cls : DataBaseTableList.getAllTableList()) {
            sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseUtils.getTableName(cls));
        }*/
        try {
            for (int i = TABLES.size() - 1; i >= 0; i--) {
                Class<? extends IEntity<?>> classType = TABLES.get(i);
                // 银联POS使用的表不直接删除
                if (classType == PosTransLog.class || classType == PosSettlementLog.class || classType == PayMenuOrder.class) {
                    alterTableStructure(oldVer, newVer, classType);
                } else {
                    TableUtils.dropTable(connectionSource, classType, true);
                }
            }
        } catch (SQLException ex) {
            Log.e(TAG, "Unable to drop table", ex);
        }
        onCreate(sqliteDatabase, connectionSource);
    }

    private void alterTableStructure(int oldVer, int newVer, Class<? extends IEntity<?>> classType) {

    }
}
