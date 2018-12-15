package com.zhongmei.yunfu.bean.req;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.util.ValueEnums;


/**
 * @date 2017/3/13 14:54
 */
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

    /**
     * 将对象序列化为一个Parcel对象 可以将Parcel看成是一个流，通过writeToParcel把对象写到流里面,
     * 再通过createFromParcel从流里读取对象 注意:写的顺序和读的顺序必须一致。
     */
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

    /**
     * 实例化静态内部对象CREATOR实现接口Parcelable.Creator public static
     * final一个都不能少，内部对象CREATOR的名称也不能改变，必须全部大写
     */
    public static final Creator<CustomerListResp> CREATOR = new Creator<CustomerListResp>() {
        // 将Parcel对象反序列化为CopyOfSoftInfo
        @Override
        public CustomerListResp createFromParcel(Parcel source) {
            return new CustomerListResp(source);
        }

        @Override
        public CustomerListResp[] newArray(int size) {
            return new CustomerListResp[size];
        }

    };

    /**
     * 登录状态
     */
    public enum LoginType implements ValueEnum<Integer> {

        /**
         * 手机
         */
        MOBILE(1),
        /**
         * 微信
         */
        WECHAT(2),
        /**
         * 座机
         */
        PHONE(2),
        /**
         * 匿名卡顾客
         */
        CUSTOMER_ENTITY_CARD(6),

        /**
         * 未知的值
         *
         * @deprecated 为了避免转为enum出错而设置，不应直接使用
         */
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

    /**
     * 冻结状态
     */
    public enum IsDisable implements ValueEnum<Integer> {

        /**
         * 手机
         */
        YES(1),
        /**
         * 微信
         */
        NO(2),
        /**
         * 未知的值
         *
         * @deprecated 为了避免转为enum出错而设置，不应直接使用
         */
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

    /**
     * 获取冻结状态
     *
     * @return
     */
    public IsDisable getIsDisable() {
        return ValueEnums.toEnum(IsDisable.class, isDisable);
    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public LoginType getLoginType() {
        return ValueEnums.toEnum(LoginType.class, loginType);
    }
}
