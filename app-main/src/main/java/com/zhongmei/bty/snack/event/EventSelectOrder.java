package com.zhongmei.bty.snack.event;


public class EventSelectOrder {


    private int currentTab;

    private String tradeUuid;

    private boolean isSquareAccountMode;

    private boolean isBindDeliveryUserMode;

    private int mFromType;

    public EventSelectOrder(int currentTab, String tradeUuid, boolean isSquareAccountMode, boolean isBindDeliveryUserMode, int mFromType) {
        this.currentTab = currentTab;
        this.tradeUuid = tradeUuid;
        this.isSquareAccountMode = isSquareAccountMode;
        this.isBindDeliveryUserMode = isBindDeliveryUserMode;
        this.mFromType = mFromType;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }

    public String getTradeUuid() {
        return tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public boolean isSquareAccountMode() {
        return isSquareAccountMode;
    }

    public void setSquareAccountMode(boolean squareAccountMode) {
        isSquareAccountMode = squareAccountMode;
    }

    public boolean isBindDeliveryUserMode() {
        return isBindDeliveryUserMode;
    }

    public int getFromType() {
        return mFromType;
    }

    public void setFromType(int mFromType) {
        this.mFromType = mFromType;
    }
}
