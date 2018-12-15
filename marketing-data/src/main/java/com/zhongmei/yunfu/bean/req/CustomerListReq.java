package com.zhongmei.yunfu.bean.req;

import com.zhongmei.bty.basemodule.customer.message.CustomerListResp;
import com.zhongmei.yunfu.bean.YFPageReq;

/**
 * 顾客列表上传请求
 *
 * @date 2017/3/14 15:17
 */
public class CustomerListReq extends YFPageReq {

    public CustomerListReq(int pageNo, int pageSize) {
        super(pageNo, pageSize);
    }

    public Long brandId; //品牌
    public String nameOrMobile; //姓名或手机号
    public String cardNum; //卡号
    public String openId; //扫码wxopenId
    public String groupId;
    public String customerType;
    public String levelId;
    public String refresh;

    /**
     * 使用服务器返回的查询时间 参见 {@link CustomerListResp}
     */
    public Long queryTime;
}
