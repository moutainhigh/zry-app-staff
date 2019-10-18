package com.zhongmei.bty.basemodule.database.entity.customer;

import java.io.Serializable;

import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.util.ValueEnums;

public class CustomerV5 implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long customerid;

    private Long commercialid;

    private Long memberid;

    private Long commercialmemberid;

    private Long groupid;

    private String country;

    private String nation;

    private String nationalTelCode;

    private String invoice;

    private String name;

    private Integer status;

    private String modifydatetime;

    private Integer sex;

    private String mobile;

    private String tel;

    private String company;

    private String address;

    private String birthday;

    private String email;

    private Integer marriage;

    private String idcards;

    private String createdatetime;

    private String invoicetitle;

    private Integer ctype;

    private Integer source;

    private Integer channel;

    private String synflag;

    private Long isneedconfirm;

    private Long isacceptsubscription;

    private String consumepassword;

    private String userid;

    private Long levelid;

    private Long brandid;

    private String upgradetime;

    private Long upgradecommercialid;

    private String membercard;

    private String ext;

    private String entitycard;

    private Integer bizsource;

    private String createuser;

    private String environmenthobby;

    private String monthlyincome;

    private String cardetailed;

    private String department;

    private String interest;

    private String memo;

    private Long isdisable;

    private Long updatorid;

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getCommercialid() {
        return commercialid;
    }

    public void setCommercialid(Long commercialid) {
        this.commercialid = commercialid;
    }

    public Long getMemberid() {
        return memberid;
    }

    public void setMemberid(Long memberid) {
        this.memberid = memberid;
    }

    public Long getCommercialmemberid() {
        return commercialmemberid;
    }

    public void setCommercialmemberid(Long commercialmemberid) {
        this.commercialmemberid = commercialmemberid;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getModifydatetime() {
        return modifydatetime;
    }

    public void setModifydatetime(String modifydatetime) {
        this.modifydatetime = modifydatetime;
    }

    public Sex getSex() {
        return ValueEnums.toEnum(Sex.class, sex);
    }

    public void setSex(Sex sex) {
        this.sex = ValueEnums.toValue(sex);
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMarriage() {
        return marriage;
    }

    public void setMarriage(Integer marriage) {
        this.marriage = marriage;
    }

    public String getIdcards() {
        return idcards;
    }

    public void setIdcards(String idcards) {
        this.idcards = idcards;
    }

    public String getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(String createdatetime) {
        this.createdatetime = createdatetime;
    }

    public String getInvoicetitle() {
        return invoicetitle;
    }

    public void setInvoicetitle(String invoicetitle) {
        this.invoicetitle = invoicetitle;
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

    public String getSynflag() {
        return synflag;
    }

    public void setSynflag(String synflag) {
        this.synflag = synflag;
    }

    public Long getIsneedconfirm() {
        return isneedconfirm;
    }

    public void setIsneedconfirm(Long isneedconfirm) {
        this.isneedconfirm = isneedconfirm;
    }

    public Long getIsacceptsubscription() {
        return isacceptsubscription;
    }

    public void setIsacceptsubscription(Long isacceptsubscription) {
        this.isacceptsubscription = isacceptsubscription;
    }

    public String getConsumepassword() {
        return consumepassword;
    }

    public void setConsumepassword(String consumepassword) {
        this.consumepassword = consumepassword;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Long getLevelid() {
        return levelid;
    }

    public void setLevelid(Long levelid) {
        this.levelid = levelid;
    }

    public Long getBrandid() {
        return brandid;
    }

    public void setBrandid(Long brandid) {
        this.brandid = brandid;
    }

    public String getUpgradetime() {
        return upgradetime;
    }

    public void setUpgradetime(String upgradetime) {
        this.upgradetime = upgradetime;
    }

    public Long getUpgradecommercialid() {
        return upgradecommercialid;
    }

    public void setUpgradecommercialid(Long upgradecommercialid) {
        this.upgradecommercialid = upgradecommercialid;
    }

    public String getMembercard() {
        return membercard;
    }

    public void setMembercard(String membercard) {
        this.membercard = membercard;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getEntitycard() {
        return entitycard;
    }

    public void setEntitycard(String entitycard) {
        this.entitycard = entitycard;
    }

    public Integer getBizsource() {
        return bizsource;
    }

    public void setBizsource(Integer bizsource) {
        this.bizsource = bizsource;
    }

    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public String getEnvironmenthobby() {
        return environmenthobby;
    }

    public void setEnvironmenthobby(String environmenthobby) {
        this.environmenthobby = environmenthobby;
    }

    public String getMonthlyincome() {
        return monthlyincome;
    }

    public void setMonthlyincome(String monthlyincome) {
        this.monthlyincome = monthlyincome;
    }

    public String getCardetailed() {
        return cardetailed;
    }

    public void setCardetailed(String cardetailed) {
        this.cardetailed = cardetailed;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public Long getIsdisable() {
        return isdisable != null ? isdisable : 2;
    }

    public void setIsdisable(Long isdisable) {
        this.isdisable = isdisable;
    }

    public Long getUpdatorid() {
        return updatorid;
    }

    public void setUpdatorid(Long updatorid) {
        this.updatorid = updatorid;
    }

    public String getNationalTelCode() {
        return nationalTelCode;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }
}
