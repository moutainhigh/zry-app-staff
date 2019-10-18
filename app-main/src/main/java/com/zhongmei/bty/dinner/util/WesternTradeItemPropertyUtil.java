package com.zhongmei.bty.dinner.util;

import android.view.View;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.bty.cashier.shoppingcart.vo.ChooseVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.snack.orderdish.buinessview.SeatView;
import com.zhongmei.bty.snack.orderdish.buinessview.SeatView_;

import java.math.BigDecimal;


public class WesternTradeItemPropertyUtil extends TradeItemPropertyUtil {

        private SeatView seatView;

    @Override
    protected void initData() {
        btnSeat.setEnabled(true);
        btnSeat.setVisibility(View.VISIBLE);
        super.initData();
    }

    @Override
    protected void initSelectData() {
        super.initSelectData();
        if (mDishDataItem.getType() == ItemType.COMBO) {
            ViewUtil.setButtonEnabled(btnSeat, false);
            btnSeat.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initUnselectData() {
        super.initUnselectData();
        ViewUtil.setButtonEnabled(btnSeat, false);
        if (mDishDataItem.getType() == ItemType.COMBO) {
            btnSeat.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_seat:
                showSeatView();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    protected void showSeatView() {
        ViewUtil.setButtonSelected(vActionBar, btnSeat);
        if (seatView == null) {
            seatView = SeatView_.build(mActivity);
            seatView.setListener(new SeatView.SeatClickListener() {
                @Override
                public void onSeatClick(ChooseVo<TableSeat> chooseVo) {
                    TableSeat tableSeat = chooseVo.getProperty();

                    ShopcartItemBase shopcartItemBase = getShopcartItem();
                    TradeItemExtraDinner tradeItemExtraDinner = mShopcartItem.getTradeItemExtraDinner();
                    if (tradeItemExtraDinner != null && tradeItemExtraDinner.getId() != null) {
                                                if (chooseVo.isSelected()) {
                            tradeItemExtraDinner.validateUpdate();
                            tradeItemExtraDinner.setSeatId(tableSeat.getId());
                            tradeItemExtraDinner.setSeatNumber(tableSeat.getSeatName());
                            tradeItemExtraDinner.setClientUpdateTime(System.currentTimeMillis());
                        } else {
                                                        tradeItemExtraDinner.setSeatId(null);
                            tradeItemExtraDinner.setSeatNumber(null);
                            tradeItemExtraDinner.setClientUpdateTime(System.currentTimeMillis());
                        }
                    } else {
                        if (chooseVo.isSelected()) {
                            tradeItemExtraDinner = new TradeItemExtraDinner();
                            tradeItemExtraDinner.validateCreate();
                            tradeItemExtraDinner.setTradeItemId(shopcartItemBase.getId());
                            tradeItemExtraDinner.setTradeItemUuid(shopcartItemBase.getUuid());
                            tradeItemExtraDinner.setSeatId(tableSeat.getId());
                            tradeItemExtraDinner.setSeatNumber(tableSeat.getSeatName());
                            tradeItemExtraDinner.setClientCreateTime(System.currentTimeMillis());
                        } else {
                            tradeItemExtraDinner = null;
                        }
                    }

                    shopcartItemBase.setTradeItemExtraDinner(tradeItemExtraDinner);
                    mShoppingCart.updateDinnerDish(mShopcartItem, false);
                }
            });
        }
        seatView.setTitle(R.string.add_experience_seat);
        seatView.setList(mShoppingCart.getIDinnertableTrade().getTableSeats(), mShopcartItem.getTradeItemExtraDinner());
        showCustomContentView(seatView);
    }


    @Override
    public void onSelectedQtyChanged(BigDecimal selectedQty) {
        if (mSelectedQty.compareTo(selectedQty) == 0) {
            return;
        }
        if (added) {
                        if (mSetmealShopcartItem == null && mShopcartItem.getId() == null && mShopcartItem.hasSetmeal()) {
                                if (mDishSetmealManager != null && mShopcartItem.getDishShop().getSaleType() != SaleType.WEIGHING) {
                                                            for (int i = 1; selectedQty.compareTo(new BigDecimal(i)) > 0; i++) {
                        ShopcartItem shopcartItem = ShopcartItemUtils.shopcartItemCopy(mShopcartItem);
                        if (shopcartItem.getSetmealManager() != null) {
                            shopcartItem.getSetmealManager().loadData();
                        }
                        mShoppingCart.addDishToShoppingCart(shopcartItem, false);
                        vDishQuantity.setTextWithoutListener(mSelectedQty.toString());
                    }
                }
            } else {
                super.doQtyChange(selectedQty);
            }
        }
    }
}
