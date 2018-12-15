package com.zhongmei.bty.dinner.Listener;

import android.support.v4.app.FragmentManager;

import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.dinner.orderdish.manager.DinnerDishManager;
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
public class OperationModifyDishListener implements ResponseListener<TradeResp> {

    private DishOptListener listener;
    private List<DishDataItem> selectedItems;
    private PrintOperationOpType opType;
    private TradeVo tradeVo;
    private List<String> kitchenUUIDs;
    private FragmentManager fragmentManager;

    public OperationModifyDishListener(PrintOperationOpType opType,
                                       TradeVo tradeVo,
                                       List<DishDataItem> selectedItems,
                                       List<String> kitchenUUIDs,
                                       FragmentManager fragmentManager, DishOptListener listener) {
        this.selectedItems = selectedItems;
        this.opType = opType;
        this.tradeVo = tradeVo;
        this.fragmentManager = fragmentManager;
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
//        DinnerShoppingCart.getInstance().resetShopcartItemFromDBEx(listAddItemUUIDs);
        //isFirstPrint = DinnerPrintUtil.isFirstPrintCustom(tradeVo);
        isAdd = DinnerDishManager.getInstance().isAddDishEx(tradeVo.getTradeItemList());
//        DinnerShoppingCart.getInstance().resetShopcartItemFromDBEx(listAddItemUUIDs);
    }

    @Override
    public void onResponse(ResponseObject<TradeResp> response) {
        if (!ResponseObject.isOk(response)) {
            if (listener != null) {
                listener.onFail(opType, selectedItems);
            }
            ToastUtil.showShortToast(response.getMessage());
            return;
        }

        DinnerShoppingCart.getInstance().resetShopcartItemFromDBEx(listAddItemUUIDs);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tradeVo.getMealShellVo() != null)
                    DinnerPrintUtil.addGroupOrBuffetShell(listAddItemUUIDs, listDelItemUUIDs, listModifyUUUIDs, tradeVo.getMealShellVo().getUuid());
                IPrintHelper.Holder.getInstance().printCustomerTicket(tradeVo.getTrade().getUuid(), listAddItemUUIDs, listModifyUUUIDs, listDelItemUUIDs, isFirstPrint,
                        new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CUSTOMER));
                if (opType == WAKE_UP || opType == RISE_DISH) {
                    IPrintHelper.Holder.getInstance().printKitchenAllTicket(tradeVo.getTrade().getUuid(), kitchenUUIDs, null, null, !isAdd,
                            PRTBatchModifyPrintListener.create(PrintTicketTypeEnum.KITCHENALL));
                }
            }
        }, 3000);*/


        DinnerDishManager.getInstance().addSelectedTradeItemOperations(selectedItems, opType);
        DinnerDishManager.getInstance().setDishItemsId(response.getContent().getTradeItems(), selectedItems);
        OperationDishListener oplistener = new OperationDishListener(selectedItems, opType, tradeVo, kitchenUUIDs, listener);
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        tradeOperates.operationDish(response.getContent().getTrades().get(0).getId(), opType, selectedItems, LoadingResponseListener.ensure(oplistener, fragmentManager));
    }

    @Override
    public void onError(VolleyError error) {
        if (listener != null) {
            listener.onFail(opType, selectedItems);
        }
        ToastUtil.showShortToast(error.getMessage());
    }
}
