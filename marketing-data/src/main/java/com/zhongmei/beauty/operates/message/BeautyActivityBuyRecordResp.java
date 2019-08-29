package com.zhongmei.beauty.operates.message;

import com.zhongmei.yunfu.resp.ResponseObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * 小程序活动下行请求
 *
 * @date 2018/6/28
 */
public class BeautyActivityBuyRecordResp extends ResponseObject<List<BeautyActivityBuyRecordResp>> {


    /**
     * 活动名称
     */
    public String marketingName;

    /**
     * 原价
     */
    public BigDecimal costPrice;

    /**
     * 活动价
     */
    public BigDecimal activityPrice;

    /**
     * 活动类型（1拼团  2秒杀  3 砍价,4 增强拼团）
     */
    public Integer type;

    /**
     * 活动开始日期(时间戳)
     */
    public Long planStartDate;

    /**
     * 活动结束日期(时间戳)
     */
    public Long planEndDate;
    /**
     * 使用开始日期(时间戳)
     */
    public Long useStartDate;
    /**
     * 使用结束日期(时间戳)
     */
    public Long useEndDate;
    /**
     * 活动简介
     */
    public String describe;
    /**
     * 活动规则
     */
    public String remark;

    /**
     * 品牌商品id = brandDishId
     */
    public Long dishId;

    /**
     * 商品名称
     */
    public String dishName;

    /**
     * 商品数量
     */
    public Integer goodsNum = new Integer(1);

    public Long id;

    /**
     * 小程序订单编号
     */
    public Long validityPeriod; //使用有效期

    //是否在购物车中选中，本地使用
    public boolean isUsed = false;
}
