package com.zhongmei.yunfu.bean.req;

/**
 * @date 2017/3/13 14:54
 */
public class CustomerListResp1 {
    /**
     * 顾客id
     */
    public Long customerId;
    /**
     * 姓名
     */
    public String name;
    /**
     * 手机号
     */
    public String mobile;
    /**
     * 客户类型  0 手机注册顾客，1,微信注册顾客,2,座机注册顾客 ,6，为权益卡号顾客
     */
    public Integer loginType;
    /**
     * 注册标示 对应loginType，0 手机；1 openId；2 座机
     */
    public String loginId;
    /**
     * 会员等级id
     */
    public Long levelId;
    public String levelName;
    /**
     * 分组id
     */
    public Long groupId;
    /**
     * 是否禁用  1 是；2 否
     */
    public int isDisable = 2;
    //是否启用 true启用 false禁用
    public boolean enabledFlag;
    /**
     * 来源
     */
    public int source;
    public String sourceName;
    /**
     * 性别 : 未知：0 ； 男：1女：2；
     */
    public int sex;
    /**
     * 最后更新时间
     */
    public String modifyDateTime;
    /**
     * 是否已人脸认证
     */
    public Integer hasFaceCode;

    /**
     * 卡号
     */
    public String cardNo;
}
