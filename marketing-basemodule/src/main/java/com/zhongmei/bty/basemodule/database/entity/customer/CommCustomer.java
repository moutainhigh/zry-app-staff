package com.zhongmei.bty.basemodule.database.entity.customer;

import java.io.Serializable;

/**
 * @Date： 2016/12/2
 * @Description:Customer所有信息
 * @Version: 1.0
 */
public class CommCustomer implements Serializable {

    private Long id;
    private Long commercialID; //商户id
    private Long brandId; //品牌表的主键ID
    private Long memberID; //会员id
    private Long commercialMemberId; //商家会员id
    private Long groupID;
    private Integer status; //状态
    private Long modifyDateTime; //修改时间
    private Integer sex; //性别
    private String birthday;
    private Integer marriage; //婚姻状况(数据为空)
    private Long createDateTime; //创建时间
    private Integer ctype; //客户类型，0：商家手动添加，1：找位自动添加
    private Integer source; //来源，1为Calm，2为手机app，3为其他系统导入，4为微信，5支付宝，6商家官网，7百度,8
    private Integer channel; //会员渠道：0：，1：点入，2：有米，3：多盟，4：力美,5：大众点评，6：点评团，7：品客团，8：QQ团购
    private Integer isNeedConfirm; //0:不验证,1:需要验证。隐私开关字段，该开关字段用于客户在开桌前，进行手机验证码校验时使用。
    private Integer isAcceptSubscription; //是否接受短信订阅
    private Long levelId; //会员等级表的UUID
    private String upgradeTime; //客户升级成会员的时间
    private Integer bizSource; //1:预订，2:排队，3:来电，4:客户，5:外卖，6.堂食
    private String country;
    private String nation;
    private String invoice; //发票开头
    private String name;
    private String mobile;
    private String tel;
    private String company;
    private String address; //地址
    private String email;
    private String monthlyIncome; //月收入
    private String carDetailed; //车牌号
    private String department; //部门
    private String idCards;
    private String interest; //爱好
    private String memo; //备注
    private String invoiceTitle; //发票开头
    private String uuid; //同步标识.
    private String consumePassword; //会员消费密码
    private String userId; //升级会员时的操作人
    private String upgradeCommercialId; //升级成会员的操作商户ID
    private String memberCard; //会员卡号
    private String ext; //分机号
    private String entityCard; //实体卡号
    private String createUser; //创建用户
    private String environmentHobby; //环境喜好
    private Integer isDisable; //会员停用：1.是停用; 2.否
    private Integer loginType;//'注册类型(0 手机注册顾客，1 微信注册顾客,2 座机注册顾客)',

    private String valueCard;//储值
    private String sendValueCard;//送的值
    //  增加 微信会员登录相关字段
    private Long updatorId;//更新人ID
    private String loginId;//第三方唯一键（微信openId,...）
    private Long customerMainId;//'自关联（customerId）',
    private String wxIconUrl;//'微信头像链接',
    private Long attentionWxTime;//'关注微信时间',
    private Integer bindFlag;//'1-未绑定，2-绑定手机注册顾客'

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommercialID() {
        return commercialID;
    }

    public void setCommercialID(Long commercialID) {
        this.commercialID = commercialID;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getMemberID() {
        return memberID;
    }

    public void setMemberID(Long memberID) {
        this.memberID = memberID;
    }

    public Long getCommercialMemberId() {
        return commercialMemberId;
    }

    public void setCommercialMemberId(Long commercialMemberId) {
        this.commercialMemberId = commercialMemberId;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getMarriage() {
        return marriage;
    }

    public void setMarriage(Integer marriage) {
        this.marriage = marriage;
    }

    public Long getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Long createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Integer getCtype() {
        return ctype;
    }

    public void setCtype(Integer ctype) {
        this.ctype = ctype;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getIsNeedConfirm() {
        return isNeedConfirm;
    }

    public void setIsNeedConfirm(Integer isNeedConfirm) {
        this.isNeedConfirm = isNeedConfirm;
    }

    public Integer getIsAcceptSubscription() {
        return isAcceptSubscription;
    }

    public void setIsAcceptSubscription(Integer isAcceptSubscription) {
        this.isAcceptSubscription = isAcceptSubscription;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public String getUpgradeTime() {
        return upgradeTime;
    }

    public void setUpgradeTime(String upgradeTime) {
        this.upgradeTime = upgradeTime;
    }

    public Integer getBizSource() {
        return bizSource;
    }

    public void setBizSource(Integer bizSource) {
        this.bizSource = bizSource;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getCarDetailed() {
        return carDetailed;
    }

    public void setCarDetailed(String carDetailed) {
        this.carDetailed = carDetailed;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIdCards() {
        return idCards;
    }

    public void setIdCards(String idCards) {
        this.idCards = idCards;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConsumePassword() {
        return consumePassword;
    }

    public void setConsumePassword(String consumePassword) {
        this.consumePassword = consumePassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUpgradeCommercialId() {
        return upgradeCommercialId;
    }

    public void setUpgradeCommercialId(String upgradeCommercialId) {
        this.upgradeCommercialId = upgradeCommercialId;
    }

    public String getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getEntityCard() {
        return entityCard;
    }

    public void setEntityCard(String entityCard) {
        this.entityCard = entityCard;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getEnvironmentHobby() {
        return environmentHobby;
    }

    public void setEnvironmentHobby(String environmentHobby) {
        this.environmentHobby = environmentHobby;
    }

    public Integer getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Integer isDisable) {
        this.isDisable = isDisable;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public String getValueCard() {
        return valueCard;
    }

    public void setValueCard(String valueCard) {
        this.valueCard = valueCard;
    }

    public String getSendValueCard() {
        return sendValueCard;
    }

    public void setSendValueCard(String sendValueCard) {
        this.sendValueCard = sendValueCard;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Long getCustomerMainId() {
        return customerMainId;
    }

    public void setCustomerMainId(Long customerMainId) {
        this.customerMainId = customerMainId;
    }

    public String getWxIconUrl() {
        return wxIconUrl;
    }

    public void setWxIconUrl(String wxIconUrl) {
        this.wxIconUrl = wxIconUrl;
    }

    public Long getAttentionWxTime() {
        return attentionWxTime;
    }

    public void setAttentionWxTime(Long attentionWxTime) {
        this.attentionWxTime = attentionWxTime;
    }

    public Integer getBindFlag() {
        return bindFlag;
    }

    public void setBindFlag(Integer bindFlag) {
        this.bindFlag = bindFlag;
    }
}
