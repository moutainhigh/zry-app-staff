package com.zhongmei.bty.data.operates.message.content;

import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * Created by demo on 2018/12/15
 */

public class BookingTradeItemReq {

    private Long parentId;//商品是组合明细时，指向该明细的组合商品(即指向套餐主餐)

    private String parentUuid;//指向父记录的uuid，如果是子菜才有值，单菜此字段为空

    private String skuUuid;//商品UUID

    private Integer sort;//排序位

    private BigDecimal price;//单价

    private BigDecimal quantity;//数量

    private BigDecimal amount;//金额，等于 PRICE * QTY

    private BigDecimal propertyAmount;//各种特征的金额合计 AMOUNT + FEATURE_AMOUNT

    private BigDecimal actualAmount;//	实际售价propertyAmount + amount

    private String memo;//备注

    private String uuid;//UUID，本笔记录唯一值

    private Integer statusFlag;//	1:VALID:有效的 2:INVALID:无效的

    private Timestamp clientCreateTime;//PAD本地创建时间

    private Timestamp clientUpdateTime;//PAD本地最后修改时间

    private Long creatorId;//创建者，创建此记录的系统用户

    private String creatorName;//	创建者姓名

    private Long updatorId;//	最后修改此记录的用户

    private String updatorName;//	最后修改者姓名

    private Integer type;//菜品类型 : 菜品种类 0:单菜 1:套餐 2:加料 10:临时卡售卡 11:临时卡储值

    private Long dishSetmealGroupId;//	-1： 无状态 ,1:暂停出单 2:立即出单 3:出单中 4:已出单 5:出单失败

    private String tableId;//	桌台Id

    private String batchNo;//		批次号

    private Integer enableWholePrivilege;//	是否参与整单折扣 1 是 2 否

    private String unitName;//		单位名称

    private Integer saleType;//	销售类型 : 1 称重销售

    private Long relateBookingTradeItemId;//	当此记录是修改其他品项而来时记录被修改的品项ID

    private String relateBookingTradeItemUuid;//当此记录是修改其他品项而来时记录被修改的品项UUID

    private BigDecimal feedsAmount;//		未知

    private Integer invalidType;//	1:被修改，2:被拆单

    private Integer isChangePrice;//		是否允许变价: 1 允许 2不允许

    private Integer servingStatus;//		菜品当前的服务状态：1.未上菜；2.已上菜

    private BigDecimal returnQuantity;//	1 已打印，2 未打印，默认为1 已打印

    private Integer guestPrinted;//		客看单打印状态 1 已打印，2 未打印，默认为1 已打印

    private String booking_uuid;//	预定的 UUID


}
