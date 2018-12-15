package com.zhongmei.bty.dinner.Listener;

import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.message.TradeItemResp;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.dinner.orderdish.manager.DinnerDishManager;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date： 2018/2/8
 * @Description:
 * @Version: 1.0
 */
public class OperationDishListener implements ResponseListener<TradeItemResp> {

    private DishOptListener listener;
    private List<DishDataItem> selectedItems;
    private PrintOperationOpType opType;
    private TradeVo tradeVo;
    private List<String> kitchenUUIDs;

    public OperationDishListener(List<DishDataItem> selectedItems,
                                 PrintOperationOpType opType,
                                 TradeVo tradeVo,
                                 List<String> kitchenUUIDs,
                                 DishOptListener listener) {
        this.selectedItems = selectedItems;
        this.opType = opType;
        this.tradeVo = tradeVo;
        this.kitchenUUIDs = kitchenUUIDs;
        this.listener = listener;
    }

    private final List<String> listAddItemUUIDs = new ArrayList<>();
    private final List<String> listModifyUUUIDs = new ArrayList<>();
    private final List<String> listDelItemUUIDs = new ArrayList<>();
    private boolean isFirstPrint = false;
    private boolean isAdd = false;

    /**
     * 准备等叫、起菜前需要打印的客看和厨总单数据
     */
    public void preparePrintData() {
        DinnerDishManager.prepareCustomPrintData(tradeVo, listAddItemUUIDs, listModifyUUUIDs, listDelItemUUIDs);
        //isFirstPrint = DinnerPrintUtil.isFirstPrintCustom(tradeVo);
        //isAdd = DinnerDishManager.getInstance().isAddDishEx(tradeVo.getTradeItemList());
    }

    @Override
    public void onResponse(ResponseObject<TradeItemResp> response) {

        if (ResponseObject.isOk(response)) {
            List<TradeItemOperation> tradeItemOperations = response.getContent().getTradeItemOperations();

            List<String> selectedUUids = DinnerDishManager.getInstance().getSingleAndComboUuids(selectedItems);
            DinnerShoppingCart.getInstance().resetShopcartItemFromDBEx(selectedUUids);

            /*if (!kitchenUUIDs.isEmpty()) {
                if (tradeVo.isUnionSubTrade()) {
                    //打印客看单
                    //DinnerPrintUtil.printCustomTicket(tradeVo.getTrade().getUuid(), listAddItemUUIDs, listModifyUUUIDs, listDelItemUUIDs, isFirstPrint, null);
                    if (tradeVo.getMealShellVo() != null)
                        DinnerPrintUtil.addGroupOrBuffetShell(listAddItemUUIDs, listDelItemUUIDs, listModifyUUUIDs, tradeVo.getMealShellVo().getUuid());
                    IPrintHelper.Holder.getInstance().printCustomerTicket(tradeVo.getTrade().getUuid(), listAddItemUUIDs, listModifyUUUIDs, listDelItemUUIDs, isFirstPrint,
                            new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CUSTOMER));
                    //打印厨总单
                    if (opType == WAKE_UP || opType == RISE_DISH) {
                        IPrintHelper.Holder.getInstance().printKitchenAllTicket(tradeVo.getTrade().getUuid(), kitchenUUIDs, null, null, !isAdd,
                                PRTBatchModifyPrintListener.create(PrintTicketTypeEnum.KITCHENALL));
                    }
                } else if (tradeVo.isUnionMainTrade()) {
                    IPrintHelper.Holder.getInstance().printUnionMainTradeItemOperationCustomerTicket(tradeVo.getTrade().getUuid(),
                            selectedUUids, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CUSTOMER));
                    if (opType == WAKE_UP || opType == RISE_DISH) {
                        IPrintHelper.Holder.getInstance().printUnionMainTradeItemOperationKitchenAllTicket(tradeVo.getTrade().getUuid(),
                                selectedUUids, PRTBatchModifyPrintListener.createUnion(PrintTicketTypeEnum.KITCHENALL));
                    }
                }
            }*/

            if (tradeVo.isUnionMainTrade()) {
                unionPrintRiseOrRemind(tradeItemOperations, selectedItems, opType, tradeVo.getTrade().getUuid());
            } else {
                printRiseOrRemind(tradeItemOperations, selectedItems, opType, tradeVo.getTrade().getUuid());
            }

            ToastUtil.showShortToast(response.getMessage());
            listener.onSuccess(opType, selectedItems);
        } else {
            //清空临时存储的操作数据
            if (listener != null) {
                listener.onFail(opType, selectedItems);
            }
            ToastUtil.showShortToast(response.getMessage());
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (listener != null) {
            listener.onFail(opType, selectedItems);
        }
        ToastUtil.showShortToast(error.getMessage());
    }

    /**
     * 打印催菜／起菜
     *
     * @param tradeItemOperations 对应的菜品操作（催菜／起菜）
     * @param selectedItems       操作的菜品
     * @param opType              操作类型（催菜／起菜）
     * @param tradeUuid           订单uuid
     */
    private void printRiseOrRemind(List<TradeItemOperation> tradeItemOperations, List<DishDataItem> selectedItems,
                                   PrintOperationOpType opType, String tradeUuid) {
        /*List<String> selectedItemUuids = DinnerUtils.getSingleAndComboUuids(selectedItems, opType);
        DinnerShoppingCart shoppingCart = DinnerShoppingCart.getInstance();
        if (Utils.isNotEmpty(selectedItemUuids)
                && !shoppingCart.isOnlySingleAddDish(shoppingCart.getShoppingCartDish(), selectedItemUuids)
                && shoppingCart.getShoppingCartVo().getmTradeVo().getMealShellVo() != null) {
            selectedItemUuids.add(shoppingCart.getShoppingCartVo().getmTradeVo().getMealShellVo().getUuid());
        }

        PrintTicketTypeEnum typeEnum = null;
        if (opType == PrintOperationOpType.WAKE_UP) {
            typeEnum = PrintTicketTypeEnum.WAKEUP;
        } else if (opType == PrintOperationOpType.RISE_DISH) {
            typeEnum = PrintTicketTypeEnum.RISE;
        } else if (opType == PrintOperationOpType.REMIND_DISH) {
            typeEnum = PrintTicketTypeEnum.REMIND;
        } else if (opType == PrintOperationOpType.WAKE_UP_CANCEL) {
            typeEnum = PrintTicketTypeEnum.WAKEUP_CANCEL;
        } else if (opType == PrintOperationOpType.RISE_DISH_CANCEL) {
            typeEnum = PrintTicketTypeEnum.RISE_CANCEL;
        }
        IPrintHelper.Holder.getInstance().printTradeItemOperationTicket(tradeUuid,
                selectedItemUuids, opType, new RiseUpOrRemindDishPrintListener(typeEnum, tradeItemOperations));*/
    }

    /**
     * 联台主单菜品操作打印
     *
     * @param selectedItems
     * @param opType
     * @param tradeUuid
     */
    private void unionPrintRiseOrRemind(List<TradeItemOperation> tradeItemOperations, List<DishDataItem> selectedItems,
                                        PrintOperationOpType opType, String tradeUuid) {
        /*List<String> selectedItemUuids = DinnerUtils.getSingleAndComboUuids(selectedItems, opType);
        PrintTicketTypeEnum typeEnum = null;
        if (opType == PrintOperationOpType.WAKE_UP) {
            typeEnum = PrintTicketTypeEnum.WAKEUP;
        } else if (opType == PrintOperationOpType.RISE_DISH) {
            typeEnum = PrintTicketTypeEnum.RISE;
        } else if (opType == PrintOperationOpType.REMIND_DISH) {
            typeEnum = PrintTicketTypeEnum.REMIND;
        } else if (opType == PrintOperationOpType.WAKE_UP_CANCEL) {
            typeEnum = PrintTicketTypeEnum.WAKEUP_CANCEL;
        } else if (opType == PrintOperationOpType.RISE_DISH_CANCEL) {
            typeEnum = PrintTicketTypeEnum.RISE_CANCEL;
        }
        IPrintHelper.Holder.getInstance().printUnionMainTradeItemOperationTicket(tradeUuid,
                selectedItemUuids, opType, new UnoinMainRiseUpOrRemindDishPrintListener(typeEnum, tradeItemOperations));*/
    }

    /*private static class UnoinMainRiseUpOrRemindDishPrintListener extends PRTBatchOnSimplePrintListener {

        private List<TradeItemOperation> tradeItemOperations;

        public UnoinMainRiseUpOrRemindDishPrintListener(PrintTicketTypeEnum ticketTypeEnum, List<TradeItemOperation> tradeItemOperations) {
            super(ticketTypeEnum);
            this.tradeItemOperations = tradeItemOperations;
        }

        @Override
        public void onResult(int globalCode, LongSparseArray<PRTReturnCashierTicketBean> returnCashierSparseArray, Map<Long, Integer> dishMap, SendData sendData) {
            super.onResult(globalCode, returnCashierSparseArray, dishMap, sendData);
            DinnerModifyPrintStatusUtil.changeMainPrintStatusAfterOperateOrSingleReprint(dishMap, tradeItemOperations, TicketTypeEnum.KITCHENCELL);
        }
    }

    public static class RiseUpOrRemindDishPrintListener extends PRTBatchOnSimplePrintListener {

        private List<TradeItemOperation> tradeItemOperations;

        public RiseUpOrRemindDishPrintListener(PrintTicketTypeEnum ticketTypeEnum, List<TradeItemOperation> tradeItemOperations) {
            super(ticketTypeEnum);
            this.tradeItemOperations = tradeItemOperations;
        }

        @Override
        public void onResult(int globalCode, LongSparseArray<PRTReturnCashierTicketBean> returnCashierSparseArray, Map<Long, Integer> dishMap, SendData sendData) {
            super.onResult(globalCode, returnCashierSparseArray, dishMap, sendData);
            DinnerModifyPrintStatusUtil.changePrintStatusAfterOperateOrSingleReprint(dishMap, tradeItemOperations, TicketTypeEnum.KITCHENCELL);
        }
    }*/
}
