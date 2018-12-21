package com.zhongmei.bty.dinner.table.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.TradeTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.table.view.OpentablePopWindow;
import com.zhongmei.bty.dinner.vo.DinnertableVo;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2016/7/26
 * @Description:开台操作封装
 */
public class OpenTableManager {
    private DinnertableVo dinnertableVo;
    private DinnerShoppingCart mShoppingCart;
    private int customerNum;
    private Context context;
    private BusinessType mBusinessType;
    Executor mTaskExecutor;

    public OpenTableManager(DinnertableVo dinnertableVo, int customerNum, Context context, BusinessType businessType) {
        this.dinnertableVo = dinnertableVo;
        mShoppingCart = DinnerShoppingCart.getInstance();
        this.customerNum = customerNum;
        this.context = context;
        this.mBusinessType = businessType;
        this.mTaskExecutor = new ThreadPoolExecutor(3, 3, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public void finishOpenTable() {
        final TradeTable tradeTable = createTradeVo();
        new AsyncTask<TradeTable, TradeVo, TradeVo>() {
            @Override
            protected void onPreExecute() {
                mShoppingCart.openTable(tradeTable);
                if (tradeTable == null || mShoppingCart.getShoppingCartVo().getmTradeVo().getTradeTableList() == null) {
                    ToastUtil.showLongToast(R.string.dinnertable_opentable_exception);
                    cancel(true);//解决openTableRequest中的空指针bug
                }
                super.onPreExecute();
            }

            @Override
            protected TradeVo doInBackground(TradeTable... params) {
                mShoppingCart.setOrderBusinessType(mShoppingCart.getShoppingCartVo(), mBusinessType);
                mShoppingCart.setOrderType(mShoppingCart.getShoppingCartVo(), DeliveryType.HERE);
                mShoppingCart.setDinnerOrderType(DeliveryType.HERE);
//                        if(mBusinessType==BusinessType.BUFFET){
//                            //设置默认套菜
//                            List<BuffetComboVo> listComboVo = BuffetManager.getInstance().getBuffetComboList();
//                            if(Utils.isNotEmpty(listComboVo)){
//                                mShoppingCart.setMealShellVo(listComboVo.get(0),null);
//                            }
//                        }
                TradeVo tradeVo = mShoppingCart.createOrder(mShoppingCart.getShoppingCartVo(), false);
                openTableRequest(tradeVo);
                return tradeVo;
            }

            @Override
            protected void onPostExecute(TradeVo tradeVo) {
                tradeVo.setChangedFalse();
                gotoDishWindow(tradeVo);
                super.onPostExecute(tradeVo);
            }
        }.executeOnExecutor(mTaskExecutor, tradeTable);

    }

    /**
     * @Date 2016/7/26
     * @Description:存储开台信息
     * @Param
     * @Return
     */
    private TradeTable createTradeVo() {
        TradeTable tradeTable = new TradeTable();
        tradeTable.setTableId(dinnertableVo.getTableId());
        tradeTable.setTableName(dinnertableVo.getTableName());
        tradeTable.validateCreate();
        String uuid = SystemUtils.genOnlyIdentifier();
        tradeTable.setUuid(uuid);

        tradeTable.setTablePeopleCount(customerNum);
        tradeTable.setMemo("");

        //设置登录操作员为默认服务员
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            tradeTable.setWaiterId(user.getId());
            tradeTable.setWaiterName(user.getName());
        }

        return tradeTable;
    }

    /**
     * @Date 2016/7/26
     * @Description: 调用开台接口
     * @Param
     * @Return
     */
    private void openTableRequest(TradeVo mTradeVo) {
        TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
//        final TradeVo mTradeVo = mShoppingCart.createOrder(mShoppingCart.getShoppingCartVo(), false);
        FragmentActivity activity = (FragmentActivity) context;
        final TableInfoFragment fragment = (TableInfoFragment) activity.getSupportFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
//        gotoDishWindow(mTradeVo);
        final String tableName;
//        if(mTradeVo.getTradeTableList().size() > 0) {
//            tableName = mTradeVo.getTradeTableList().get(0).getTableName();
//        } else {
//            tableName = null;
//        }
        ResponseListener<TradeResp> listener = new EventResponseListener<TradeResp>(UserActionEvent.DINNER_TABLE_OPEN) {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
//                    CLog.i(CLog.FS_TAG_KEY, "桌台 " + tableName + " 开台成功！");
                    //成功后默认选中桌台，方便刷新
//                    DinnertableManager.selectedDinnertableId = dinnertableVo.getDinnertable().getId();
                    ToastUtil.showShortToast(response.getMessage());

                    //保存授权记录
                    List<Trade> trades = response.getContent().getTrades();
                    if (trades != null && !trades.isEmpty()) {
                        Trade trade = trades.get(0);
                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_START_DESK, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                    }

                    //跳转
//                    gotoDishWindow(response.getContent());
                } else {
                    if (response != null && !TextUtils.isEmpty(response.getMessage())) {
//                        CLog.w(CLog.FS_TAG_KEY, "桌台 " + tableName + " 开台失败：" + response.getMessage());
                        ToastUtil.showShortToast(response.getMessage());
                    } else {
//                        CLog.w(CLog.FS_TAG_KEY, "桌台 " + tableName + " 开台失败：没有返回失败原因！");
                        ToastUtil.showShortToast(R.string.diner_submit_failed);
                    }
                    AuthLogManager.getInstance().clear();
//                    TableInfoContentBean.needJumpToDishWindow=false;
                    fragment.enableAddOrderBtn(true);//恢复按钮可用
                }
            }

            @Override
            public void onError(VolleyError error) {
//                CLog.e(CLog.FS_TAG_KEY, "桌台 " + tableName + " 开台失败：" + error.getMessage());
                ToastUtil.showShortToast(error.getMessage());
//                TableInfoContentBean.needJumpToDishWindow=false;
                fragment.enableAddOrderBtn(true);//恢复按钮可用
            }
        };

        IDinnertable dinnertable = dinnertableVo.getDinnertable();

        if (mBusinessType == BusinessType.BUFFET) {
            mTradeOperates.insertBuffet(mTradeVo, dinnertable, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()), true);
        } else {
            mTradeOperates.insertDinner(mTradeVo, dinnertable, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()), true);
        }
    }

    /**
     * @Date 2016/10/27
     * @Description:跳转到点菜界面
     * @Param
     * @Return
     */
    private void gotoDishWindow(TradeResp resp) {
        Trade trade = resp.getTrades().get(0);
        TradeExtra tradeExtra = resp.getTradeExtras().get(0);
        TradeTable tradeTable = resp.getTradeTables().get(0);

        TradeTableInfo tradeTableInfo = new TradeTableInfo(trade, tradeTable,
                DinnertableStatus.UNISSUED, null, null);

        DinnertableTradeModel tradeModel = new DinnertableTradeModel(tradeTableInfo, dinnertableVo.getDinnertable());
//        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
//        String tradeUuid = tradeModel.getTradeUuid();
        try {
            TradeVo tradeVo = new TradeVo();
            tradeVo.setTrade(trade);
            tradeVo.setTradeExtra(tradeExtra);
            List<TradeTable> tradeTableList = new ArrayList<>();
            tradeTableList.add(tradeTable);
            tradeVo.setTradeTableList(tradeTableList);

//          TradeVo tradeVo = tradeDal.findTrade(tradeUuid, false);
            DinnertableTradeInfo info = DinnertableTradeInfo.create(tradeModel, tradeVo);

            DinnerShoppingCart.getInstance().resetOrderFromTable(info, true);
            //((DinnerMainActivity) context).showOrderDish();

            OpentablePopWindow.getInstance(context).hide();
            //TableInfoFragment fragment = (TableInfoFragment) ((DinnerMainActivity) context).getSupportFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
            //fragment.enableAddOrderBtn(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void gotoDishWindow(TradeVo tradeVo) {
        Trade trade = tradeVo.getTrade();
        TradeExtra tradeExtra = tradeVo.getTradeExtra();
        TradeTable tradeTable = tradeVo.getTradeTableList().get(0);

        TradeTableInfo tradeTableInfo = new TradeTableInfo(trade, tradeTable,
                DinnertableStatus.UNISSUED, null, null);

        DinnertableTradeModel tradeModel = new DinnertableTradeModel(tradeTableInfo, dinnertableVo.getDinnertable());
//        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
//        String tradeUuid = tradeModel.getTradeUuid();
        try {
//          TradeVo tradeVo = tradeDal.findTrade(tradeUuid, false);
            DinnertableTradeInfo info = DinnertableTradeInfo.create(tradeModel, tradeVo);

            DinnerShoppingCart.getInstance().resetOrderFromTable(info, true);
            //((TableMainActivity) context).showOrderDish();

            //tradevo恢复为未更改
//            tradeVo.setChangedFalse();//提处到外部

            OpentablePopWindow.getInstance(context).hide();
            //TableInfoFragment fragment = (TableInfoFragment) ((TableMainActivity) context).getSupportFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
            //fragment.enableAddOrderBtn(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
