package com.zhongmei.bty.data.operates.message.content;

import java.math.BigDecimal;
import java.security.Timestamp;

/**
 * Created by demo on 2018/12/15
 */

public class BookingTradeItemPropertyReq {

    private String uuid;//bookingTradeItemUUID

    private String bookingTradeItemUuid;//关联BOOKING_TRADE_ITEM的UUID

    private Integer propertyType;//er	属性类别：(待口味、做法等统一后可以考虑取消此字段) 1:TASTE:口味 2:RECIPE:做法

    private String propertyUuid;//	属性UUID，对应口味或做法的主键id（uuid）

    private String propertyName;//	属性名称

    private BigDecimal price;//	单价

    private BigDecimal quantity;//数量

    private BigDecimal amount;//	金额，等于 PRICE * QTY

    private Long brandIdenty;//	品牌标识

    private Long shopIdenty;//	门店标识

    private String deviceIdenty;//设备标识

//     private String uuid;//	UUID，本笔记录唯一值

    private Integer statusFlag;//1:VALID:有效的 2:INVALID:无效的

    private Timestamp clientCreateTime;//	PAD本地创建时间

    private Timestamp clientUpdateTime;//PAD本地最后修改时间

    private Timestamp serverCreateTime;//	服务端创建时间

    private Timestamp serverUpdateTime;//服务端最后修改时间

    private Long creatorId;//创建者，创建此记录的系统用户

    private String creatorName;//	创建者姓名

    private Long updatorId;//最后修改此记录的用户

    private String updatorName;//最后修改者姓名

}
