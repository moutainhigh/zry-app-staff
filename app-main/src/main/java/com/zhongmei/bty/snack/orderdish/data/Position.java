package com.zhongmei.bty.snack.orderdish.data;

public class Position {
    int first;

    int second;

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public Position(int first, int second) {
        super();
        this.first = first;
        this.second = second;
    }

}