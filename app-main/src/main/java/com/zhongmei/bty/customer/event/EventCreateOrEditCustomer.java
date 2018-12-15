package com.zhongmei.bty.customer.event;

import com.zhongmei.yunfu.bean.req.CustomerListResp;

/**
 * 顾客创建或者编辑了 顾客
 */
public class EventCreateOrEditCustomer {
    public int type;

    public CustomerListResp bean;

//	public CustomerNew2 customer;

    /**
     * @param type 创建 or  编辑
     * @param bean 列表实体
     */
    public EventCreateOrEditCustomer(int type, CustomerListResp bean) {
        this.type = type;
        this.bean = bean;
    }

//	/**
//	 * @param type 创建 or  编辑
//	 * @param bean 列表实体
//	 * @param customer 顾客对象
//     */
//	public EventCreateOrEditCustomer(int type , CustomerListBean bean ) {
//		this.type = type;
//		this.bean = bean;
//		this.customer = customer;
//	}
}
