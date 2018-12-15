package com.zhongmei.bty.data.operates.message.content;

import android.os.Parcel;

import java.math.BigDecimal;

/**
 * 描述：金城会员卡信息返回
 *
 * @version v7.16 金城
 * @since 2017/8/26
 */

public class JCMemberInfo implements android.os.Parcelable {

//    userNo	String	是	金诚用户编号
//    name	String	是	金诚用户名称
//    nickName	String	是	金诚用户昵称
//    totalBalance	bigdecimal	是	账户余额
//    status	int	是	状态，0 未使用；1 已发放；2 已使用”

    private String userNo;

    private String name;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String nickName;

    private BigDecimal totalBalance;

    private int status;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userNo);
        dest.writeString(this.name);
        dest.writeString(this.nickName);
        dest.writeSerializable(this.totalBalance);
        dest.writeInt(this.status);
    }

    public JCMemberInfo() {
    }

    protected JCMemberInfo(Parcel in) {
        this.userNo = in.readString();
        this.name = in.readString();
        this.nickName = in.readString();
        this.totalBalance = (BigDecimal) in.readSerializable();
        this.status = in.readInt();
    }

    public static final Creator<JCMemberInfo> CREATOR = new Creator<JCMemberInfo>() {
        @Override
        public JCMemberInfo createFromParcel(Parcel source) {
            return new JCMemberInfo(source);
        }

        @Override
        public JCMemberInfo[] newArray(int size) {
            return new JCMemberInfo[size];
        }
    };
}
