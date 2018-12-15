package com.zhongmei.bty.basemodule.customer.action;

/**
 * 会员是否登录
 */
public class EventMemeberIsLogin {
    // true 会员登录， false 退出登录
    Boolean isLogin;
    //会员基本信息
    //Customer customer;

    public EventMemeberIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

	/*public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}*/

}
