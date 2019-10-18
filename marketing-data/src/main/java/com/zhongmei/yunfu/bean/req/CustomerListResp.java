package com.zhongmei.yunfu.bean.req;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.util.ValueEnums;



public class CustomerListResp extends CustomerListResp1 implements Parcelable {

    public CustomerListResp() {
    }

    protected CustomerListResp(Parcel pl) {
        customerId = pl.readLong();
        name = pl.readString();
        mobile = pl.readString();
        loginType = pl.readInt();
        loginId = pl.readString();
        levelId = pl.readLong();
        levelName = pl.readString();
        groupId = pl.readLong();
        isDisable = pl.readInt();
        enabledFlag = pl.readInt() == 1;
        source = pl.readInt();
        sourceName = pl.readString();
        sex = pl.readInt();
        modifyDateTime = pl.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(customerId);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeInt(loginType);
        dest.writeString(loginId);
        dest.writeLong(levelId);
        dest.writeString(levelName);
        dest.writeLong(groupId);
        dest.writeInt(isDisable);
        dest.writeInt(enabledFlag ? 1 : 2);
        dest.writeInt(source);
        dest.writeString(sourceName);
        dest.writeInt(sex);
        dest.writeString(modifyDateTime);
    }


    public static final Creator<CustomerListResp> CREATOR = new Creator<CustomerListResp>() {
                @Override
        public CustomerListResp createFromParcel(Parcel source) {
            return new CustomerListResp(source);
        }

        @Override
        public CustomerListResp[] newArray(int size) {
            return new CustomerListResp[size];
        }

    };


    public enum LoginType implements ValueEnum<Integer> {


        MOBILE(1),

        WECHAT(2),

        PHONE(2),

        CUSTOMER_ENTITY_CARD(6),


        @Deprecated
        __UNKNOWN__;

        private final Helper<Integer> helper;

        private LoginType(Integer value) {
            helper = Helper.valueHelper(value);
        }

        private LoginType() {
            helper = Helper.unknownHelper();
        }

        @Override
        public Integer value() {
            return helper.value();
        }

        @Override
        public boolean equalsValue(Integer value) {
            return helper.equalsValue(this, value);
        }

        @Override
        public boolean isUnknownEnum() {
            return helper.isUnknownEnum();
        }

        @Override
        public void setUnknownValue(Integer value) {
            helper.setUnknownValue(value);
        }

        @Override
        public String toString() {
            return "" + value();
        }
    }


    public enum IsDisable implements ValueEnum<Integer> {


        YES(1),

        NO(2),

        @Deprecated
        __UNKNOWN__;

        private final Helper<Integer> helper;

        private IsDisable(Integer value) {
            helper = Helper.valueHelper(value);
        }

        private IsDisable() {
            helper = Helper.unknownHelper();
        }

        @Override
        public Integer value() {
            return helper.value();
        }

        @Override
        public boolean equalsValue(Integer value) {
            return helper.equalsValue(this, value);
        }

        @Override
        public boolean isUnknownEnum() {
            return helper.isUnknownEnum();
        }

        @Override
        public void setUnknownValue(Integer value) {
            helper.setUnknownValue(value);
        }

        @Override
        public String toString() {
            return "" + value();
        }
    }


    public IsDisable getIsDisable() {
        return ValueEnums.toEnum(IsDisable.class, isDisable);
    }


    public LoginType getLoginType() {
        return ValueEnums.toEnum(LoginType.class, loginType);
    }
}
