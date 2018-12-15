package com.zhongmei.bty.dinner.action;

import com.zhongmei.bty.commonmodule.database.entity.PrinterCashierTicket;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.ArrayList;

public class ActionReprintType {

    /**
     * 客看单
     */
    private boolean isPrintCustomer = false;

    /**
     * 预结单
     */
    private boolean isPrintPrecash = false;

    /**
     * 结账单
     */
    private boolean isPrintCash = false;

    /**
     * 作废单
     */
    private boolean isPrintCancel = false;

    /**
     * 退货单
     */
    private boolean isPrintRefund = false;

    /**
     * 消费清单
     */
    private boolean isPrintReceipt = false;

    /**
     * 厨打总单
     */
    private boolean isPrintKitchenAll = false;


    /**
     * 挂账单
     */
    private boolean isPrintCredit = false;

    /**
     * 打印的堂口单据的出票口列表
     */
    private ArrayList<PrinterCashierTicket> mPrintKitchenCellList;

    /**
     * 押金单
     */
    private boolean isPrintDeposit = false;

    /**
     * 标签
     */
    private boolean isPrintLabel = false;

    private Trade trade;

    private String printTag;

    public ActionReprintType(Trade trade, String printTag) {
        this.trade = trade;
        this.printTag = printTag;
    }

    public boolean isPrintCustomer() {
        return isPrintCustomer;
    }

    public void setPrintCustomer(boolean isPrintCustomer) {
        this.isPrintCustomer = isPrintCustomer;
    }

    public boolean isPrintPrecash() {
        return isPrintPrecash;
    }

    public void setPrintPrecash(boolean isPrintPrecash) {
        this.isPrintPrecash = isPrintPrecash;
    }

    public boolean isPrintCash() {
        return isPrintCash;
    }

    public void setPrintCash(boolean isPrintCash) {
        this.isPrintCash = isPrintCash;
    }

    public boolean isPrintCancel() {
        return isPrintCancel;
    }

    public void setPrintCancel(boolean isPrintCancel) {
        this.isPrintCancel = isPrintCancel;
    }

    public boolean isPrintRefund() {
        return isPrintRefund;
    }

    public void setPrintRefund(boolean isPrintRefund) {
        this.isPrintRefund = isPrintRefund;
    }

    public boolean isPrintKitchenAll() {
        return isPrintKitchenAll;
    }

    public boolean isPrintReceipt() {
        return isPrintReceipt;
    }

    public void setPrintReceipt(boolean isPrintReceipt) {
        this.isPrintReceipt = isPrintReceipt;
    }

    public void setPrintKitchenAll(boolean isPrintKitchenAll) {
        this.isPrintKitchenAll = isPrintKitchenAll;
    }

    public boolean isPrintCredit() {
        return isPrintCredit;
    }

    public void setPrintCredit(boolean printCredit) {
        isPrintCredit = printCredit;
    }

    public ArrayList<PrinterCashierTicket> getmPrintKitchenCellList() {
        return mPrintKitchenCellList;
    }

    public void setmPrintKitchenCellList(ArrayList<PrinterCashierTicket> mPrintKitchenCellList) {
        this.mPrintKitchenCellList = mPrintKitchenCellList;
    }

    public ArrayList<PrinterCashierTicket> getPrintKitchenCellList() {
        return mPrintKitchenCellList;
    }

    public void addPrintKitchenCellList(PrinterCashierTicket printerCashierTicket) {
        if (printerCashierTicket != null) {
            if (mPrintKitchenCellList == null) {
                mPrintKitchenCellList = new ArrayList<>();
            }
            mPrintKitchenCellList.add(printerCashierTicket);
        }
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public String getPrintTag() {
        return printTag;
    }

    public void setPrintTag(String printTag) {
        this.printTag = printTag;
    }

    public boolean isPrintDeposit() {
        return isPrintDeposit;
    }

    public void setPrintDeposit(boolean printDeposit) {
        isPrintDeposit = printDeposit;
    }

    public boolean isPrintLabel() {
        return isPrintLabel;
    }

    public void setPrintLabel(boolean printLabel) {
        isPrintLabel = printLabel;
    }
}
