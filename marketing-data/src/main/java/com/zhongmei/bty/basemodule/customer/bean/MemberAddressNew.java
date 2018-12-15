package com.zhongmei.bty.basemodule.customer.bean;

import java.io.Serializable;

/**
 * @Date： 2017/3/16
 * @Description:会员常用地址
 * @Version: 1.0
 */
public class MemberAddressNew implements Serializable {

    private Long memberID;//会员memberID

    private String address;//地址内容

    private Integer isDefault;//是否默认送餐地址，1是，0否

    private String areaId;//区域id

    private String street;//街道地址

    public Long getMemberID() {
        return memberID;
    }

    public void setMemberID(Long memberID) {
        this.memberID = memberID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
