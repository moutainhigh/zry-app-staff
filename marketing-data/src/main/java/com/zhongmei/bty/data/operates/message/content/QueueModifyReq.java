package com.zhongmei.bty.data.operates.message.content;

/**
 * 封装排队请求
 */
public class QueueModifyReq {

    private String synFlag;

    private Long modifyDateTime;

    private String name;

    private String sex;

    private String mobile;

    private String tel;

    private Integer repastPersonCount;

    private String nationalTelCode;

    private String country;

    private String nation;

    private String memo;

    public void setSynFlag(String synFlag) {
        this.synFlag = synFlag;
    }

    public void setModifyDateTime(Long modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setRepastPersonCount(Integer repastPersonCount) {
        this.repastPersonCount = repastPersonCount;
    }

    public void setNationalTelCode(String nationalTelCode) {
        this.nationalTelCode = nationalTelCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
