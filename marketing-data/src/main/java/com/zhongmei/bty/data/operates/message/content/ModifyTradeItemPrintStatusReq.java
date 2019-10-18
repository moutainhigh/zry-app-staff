package com.zhongmei.bty.data.operates.message.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.context.util.Utils;

public class ModifyTradeItemPrintStatusReq {

    private List<TradeItem> tradeItems;

    private List<PrintOperation> printOperations;

    private List<TradeItemOperation> tradeItemOperations;
    private List<TradeItemOperation> kitchenTradeItemOperations;
    public ModifyTradeItemPrintStatusReq(List<TradeItem> tis, List<PrintOperation> pos, List<TradeItemOperation> tios) {
                List<Long> tradeItemIdList = new ArrayList<>();
        if (Utils.isNotEmpty(tis)) {
            tradeItems = new ArrayList<TradeItem>();
            for (TradeItem ti : tis) {
                tradeItems.add(filterTradeItemFields(ti));
                                if (ti.getIssueStatus() == IssueStatus.FINISHED || ti.getIssueStatus() == IssueStatus.FAILED) {
                    tradeItemIdList.add(ti.getId());
                }
            }
        }
        if (Utils.isNotEmpty(pos)) {
            printOperations = new ArrayList<PrintOperation>();
            for (PrintOperation po : pos) {
                printOperations.add(filterPrintOperationFields(po));
            }
        }
        if (Utils.isNotEmpty(tios)) {
            tradeItemOperations = new ArrayList<TradeItemOperation>();
            for (TradeItemOperation tio : tios) {
                tradeItemOperations.add(filterTradeItemFields(tio));
                                if (tio.getOpType() == PrintOperationOpType.WAKE_UP) {
                    tradeItemIdList.remove(tio.getTradeItemId());
                }
            }
        }
        makeKitchenPrintTradeItemOperation(tradeItemIdList);
    }


    private void makeKitchenPrintTradeItemOperation(Collection<Long> tradeItemIdList) {
        if (Utils.isNotEmpty(tradeItemIdList)) {
            kitchenTradeItemOperations = new ArrayList<>();
            for (Long tradeItemId : tradeItemIdList) {
                TradeItemOperation operation = new TradeItemOperation();
                operation.validateCreate();
                operation.setTradeItemId(tradeItemId);
                operation.setOpType(PrintOperationOpType.KITCHEN_PRINT);
                operation.setPrintStatus(PrintStatus.FINISHED);
                kitchenTradeItemOperations.add(operation);
            }
        }
    }


    private TradeItem filterTradeItemFields(TradeItem ti) {
        TradeItem tradeItem = new TradeItem();
        tradeItem.setId(ti.getId());
        tradeItem.setServerUpdateTime(ti.getServerUpdateTime());
        tradeItem.setIssueStatus(ti.getIssueStatus());
        tradeItem.setGuestPrinted(ti.getGuestPrinted());

        return tradeItem;
    }


    private PrintOperation filterPrintOperationFields(PrintOperation po) {
        PrintOperation printOperation = new PrintOperation();
        printOperation.setId(po.getId());
        printOperation.setServerUpdateTime(po.getServerUpdateTime());
        printOperation.setPrintStatus(po.getPrintStatus());

        return printOperation;
    }


    private TradeItemOperation filterTradeItemFields(TradeItemOperation tio) {
        TradeItemOperation tradeItemOperation = new TradeItemOperation();
        tradeItemOperation.setId(tio.getId());
        tradeItemOperation.setServerUpdateTime(tio.getServerUpdateTime());
        tradeItemOperation.setPrintStatus(tio.getPrintStatus());

        return tradeItemOperation;
    }

    public List<TradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<TradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public List<PrintOperation> getPrintOperations() {
        return printOperations;
    }

    public void setPrintOperations(List<PrintOperation> printOperations) {
        this.printOperations = printOperations;
    }

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

}
