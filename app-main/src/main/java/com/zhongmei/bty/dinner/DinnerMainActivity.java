package com.zhongmei.bty.dinner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.enums.EntranceType;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.DinnerTableInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.cashier.ordercenter.OrderCenterDetailFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.orderdish.Bean.TableTradeInfo;
import com.zhongmei.bty.dinner.orderdish.DinnerOrderDishMainActivity;
import com.zhongmei.bty.dinner.table.DinnertableFragment_;
import com.zhongmei.bty.dinner.table.TableFragment;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.table.view.OpentablePopWindow;

public class DinnerMainActivity extends TableMainActivity implements OrderCenterDetailFragment.OnRepayListener {

    public static void start(Context context) {
        start(context, false, null);
    }

    public static void start(Context context, boolean isOpenOrderDishes, final String tradeUuid) {
        if (isOpenOrderDishes) {
            //Intent dinnerIntent = new Intent(context, DinnerMainActivity.class);
            Intent dishMainIntent = new Intent(context, DinnerOrderDishMainActivity.class);
            //dishMainIntent.putExtra(OpentablePopWindow.QUICK_OPEN_TABLE, false);
            context.startActivity(dishMainIntent);
            //context.startActivities(new Intent[]{dinnerIntent, dishMainIntent});
        } else {
            Intent dinnerIntent = new Intent(context, DinnerMainActivity.class);
            context.startActivity(dinnerIntent);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
//        FaceRecognitionService.startService(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //FaceRecognitionService.stopService(this);
    }

    @Override
    public TableFragment getTableFragment() {
        return new DinnertableFragment_();
    }

    @Override
    public EntranceType getEntranceType() {
        return EntranceType.DINNER;
    }

    @Override
    public TableInfoFragment getTableInfoFragment() {
        return (TableInfoFragment) getSupportFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
    }


    /**
     * @Date 2015年9月21日
     * @Description: 切换到点菜界面
     * @Return void
     */
    @Override
    public void showOrderDish() {
        setLeftMenu(false);

        Intent mIntent = new Intent();
        mIntent.setClass(this, DinnerOrderDishMainActivity.class);
        mIntent.putExtra(OpentablePopWindow.QUICK_OPEN_TABLE, OpentablePopWindow.isQuickOpentable);
        startActivity(mIntent);
    }

    @Override
    protected View getBusinessContentView() {
        return LayoutInflater.from(this).inflate(R.layout.dinner_main, null);
    }

    @Override
    public void onRepayCompleted(TradeVo tradeVo) {
        try {
            TablesDal tableDal = OperatesFactory.create(TablesDal.class);
            DinnerTableInfo dinnerTable = tableDal.getDinnerTableByTradeVo(tradeVo);
            DinnertableTradeInfo dinnertableTradeVo = DinnertableTradeInfo.create(tradeVo, dinnerTable);
            DinnerShoppingCart.getInstance().resetOrderFromTable(dinnertableTradeVo, true);

            //构造点单页面数据显示

            TableTradeInfo tableTradeInfo = new TableTradeInfo();
            tableTradeInfo.setSerailNumber(dinnertableTradeVo.getSerialNumber());
            tableTradeInfo.setSpendTime(dinnertableTradeVo.getSpendTime());
            tableTradeInfo.setStatus(tradeVo);
            tableTradeInfo.setTableSeats(dinnerTable.getTableId() == null ? null : tableDal.listTableSeatsByTableId(dinnerTable.getTableId()));
            tableTradeInfo.setTradeType(dinnertableTradeVo.getTradeType());
            DinnerShoppingCart.getInstance().getDinnertableTradeInfo().setiDinnertableTrade(tableTradeInfo);

            // 显示点菜界面
            Intent mIntent = new Intent();
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.setClass(this, DinnerOrderDishMainActivity.class);
            startActivity(mIntent);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }
}
