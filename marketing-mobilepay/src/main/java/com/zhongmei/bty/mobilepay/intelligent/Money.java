package com.zhongmei.bty.mobilepay.intelligent;

public class Money {
    /**
     * @date��2014��12��19�� ����11:20:22
     * @Description:type��0 is default as has cash money,type=1 is other and has
     * no money
     */
    private int type;

    /**
     * @date��2014��12��19�� ����11:20:26
     * @Description:the value of cash money
     */
    private long value;

    public Money(long value) {
        this.value = value;
        this.type = 0;
    }

    public Money(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type;
        result = prime * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Money) {
            Money money = (Money) obj;
            if (money.getValue() == getValue() && money.getType() == getType()) {
                return true;
            }
        }
        return false;
    }

}