package com.zhongmei.yunfu.context.data;


public class ReceiptInfo {

    public String title;
    public String jumpUrl;
    public int receiptType;

    public enum Type {
        None(-9999),
        Queue(1);

        int value;

        Type(int value) {
            this.value = value;
        }

        public Type valueOf(int value) {
            for (Type t : values()) {
                if (t.value == value) {
                    return t;
                }
            }
            return None;
        }
    }
}
