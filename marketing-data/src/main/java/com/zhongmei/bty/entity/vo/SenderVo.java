package com.zhongmei.bty.entity.vo;

import com.zhongmei.yunfu.context.session.core.user.User;

import java.math.BigDecimal;

/**
 * 外卖员信息
 *
 * @Date：2015-5-28 上午9:37:44
 * @Description: TODO
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class SenderVo {
    //private User user;// 用户基本信息

    private User authUser;// 新权限用户基本信息

    //private UserPrivilege userPrivilege;// 用户权限,目前没用

    private int unSquareUpCount;// 未清账的订单数

    private BigDecimal unSquareUpAmount;//未清账的金额
	
	/*public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}*/
	
	/*public UserPrivilege getUserPrivilege() {
		return userPrivilege;
	}
	
	public void setUserPrivilege(UserPrivilege userPrivilege) {
		this.userPrivilege = userPrivilege;
	}*/

    public int getUnSquareUpCount() {
        return unSquareUpCount;
    }

    public void setUnSquareUpCount(int unSquareUpCount) {
        this.unSquareUpCount = unSquareUpCount;
    }

    public BigDecimal getUnSquareUpAmount() {
        return unSquareUpAmount;
    }

    public void setUnSquareUpAmount(BigDecimal unSquareUpAmount) {
        this.unSquareUpAmount = unSquareUpAmount;
    }

    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }
}
