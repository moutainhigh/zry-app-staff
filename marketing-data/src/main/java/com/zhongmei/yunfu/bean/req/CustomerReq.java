package com.zhongmei.yunfu.bean.req;

/**
 * 顾客  信息上传 数据
 *
 * @date 2017/3/14 15:17
 */
public class CustomerReq {
    /**
     * 品牌id
     */
    public Long brandId;
    /**
     * 门店ID
     */
    public Long commercialId;
    /**
     * 客户端请求来源
     */
    public String clientType;
    /**
     * 顾客mainID
     */
    public Long customerId;
    /**
     * 是否挂账查询（1:需要，其他不需要）
     */
    public Integer isNeedCredit;
}
