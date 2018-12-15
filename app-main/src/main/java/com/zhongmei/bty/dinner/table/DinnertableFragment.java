package com.zhongmei.bty.dinner.table;

import android.view.MotionEvent;
import android.view.View;

import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.database.enums.TicketTypeEnum;
import com.zhongmei.bty.basemodule.orderdish.bean.DishQuantityBean;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.fragment.DishQuantityAndSeatEditDialogFragment;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.enums.OperateType;
import com.zhongmei.bty.basemodule.trade.utils.DinnerUtils;
import com.zhongmei.bty.cashier.util.TradeStatusUtil;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.view.MoreMenuPopWindow;
import com.zhongmei.bty.dinner.table.view.TableViewBase;
import com.zhongmei.bty.dinner.util.UnionUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: 1.0
 * @date 2015年9月15日
 */
@EFragment(R.layout.dinnertable_manager)
public class DinnertableFragment extends TableFragment {

    //@ViewById(R.id.iv_customer_arrive_dot)
    //ViewGroup layout_customer_arrive;
    //ImageView iv_customer_arrive_dot;
    //ArriveCustomerListFragment customerListFragment;

    @Override
    public void init() {
        //modify start 20170828 屏蔽顾客到店列表
        //layout_customer_arrive = findViewById(R.id.layout_customer_arrive);
        //iv_customer_arrive_dot = findViewById(R.id.iv_customer_arrive_dot);
		/*boolean isGoinRemind = SpHelper.getDefault().getBoolean(SpHelper.CUSTOMER_GOIN_REMIND);
		layout_customer_arrive.setVisibility(isGoinRemind ? View.VISIBLE : View.GONE);
		showCustomerArriveDot();
		layout_customer_arrive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (customerListFragment == null || !customerListFragment.isVisible()) {
					hideCustomerArriveDot();
					customerListFragment = ArriveCustomerListFragment_.builder().build();
					customerListFragment.show(getChildFragmentManager(), ArriveCustomerListFragment.class.getSimpleName());
				}
			}
		});*/
        //modify end 20170828 屏蔽顾客到店列表
        //tvJoinTrade.setVisibility(View.GONE);
        super.init();
    }

    @Override
    protected TableInfoFragment initInfoFragment() {
        return new DinnerTableInfoFragment_();
    }

    @Override
    public boolean hideComboSelectView(MotionEvent event) {
        return false;
    }

    @Override
    public BusinessType getBussinessType() {
        return BusinessType.DINNER;
    }

    @Override
    public void mergeTrade(final IDinnertableTrade orginal, final IDinnertableTrade dest) {
        if (orginal.getBusinessType() != dest.getBusinessType()) {
            ToastUtil.showShortToast(getString(R.string.dinner_merge_trade_error_bussinesstype));
            return;
        }

        if (!TradeStatusUtil.checkPayStatus(infoFragment.getDinnerTableTradeVo())) {
            ToastUtil.showShortToast(getString(R.string.dinner_paying));
            return;
        }

        //目标点处于支付中状态
        if (dest.getTradePayStatus() == TradePayStatus.PAYING) {
            ToastUtil.showShortToast(getString(R.string.dinner_target_paying));
            return;
        }

        if (UnionUtil.isUnionTrade(orginal.getTradeType()) || UnionUtil.isUnionTrade(dest.getTradeType())) {
            ToastUtil.showShortToast(getString(R.string.dinner_cant_merge_trade));
            return;
        }

        VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_MERGE,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        doMerge(orginal, dest);
                    }
                });
    }

    @Override
    public void transferTable(final IDinnertableTrade orginal, final IDinnertable dest) {
        if (!dest.isCurBusinessType(getBussinessType())) {
            ToastUtil.showShortToast(getString(R.string.dinner_merge_trade_error_bussinesstype));
            return;
        }

        if (!TradeStatusUtil.checkPayStatus(orginal)) {
            ToastUtil.showShortToast(getString(R.string.dinner_paying));
            return;
        }

        if (UnionUtil.isUnionTrade(orginal.getTradeType())) {
            ToastUtil.showShortToast(getString(R.string.dinner_cant_transfer_table));
            return;
        }
        VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_TRANFER, new VerifyHelper.Callback() {

            @Override
            public void onPositive(User user, String code, Auth.Filter filter) {
                if (orginal.getTradeStatus() == TradeStatus.UNPROCESSED) {
                    ToastUtil.showLongToast(R.string.dinner_table_transfer_unprocess_toast);
                    return;
                }

                checkMergeOrTransfer(orginal.getTradeUuid(), null, dest.getId(), TicketTypeEnum.TRANSFERTABLE, new Runnable() {
                    public void run() {
                        if (getInfoFragment() != null && getInfoFragment().getDinnerTableTradeVo() != null) {//add 20171204
                            List<IShopcartItem> shopcartItems = getInfoFragment().getDinnerTableTradeVo().getItems();
                            if (Utils.isEmpty(shopcartItems) || !DinnerUtils.isWestStyle()) {//如果没有菜品，直接合单
                                mManager.transfer(null, orginal, dest);
                            } else {
                                transferTableDialog(orginal, dest);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void abolishTrade(final IDinnertableTrade dinnertableTrade) {
        if (!TradeStatusUtil.checkPayStatus(dinnertableTrade)) {
            ToastUtil.showShortToast(getString(R.string.dinner_paying));
            return;
        }

//		if(isUnionTrade(dinnertableTrade.getTradeType())){
//			ToastUtil.showShortToast(getString(R.string.dinner_cant_abolish));
//			return;
//		}
//
//		if(dinnertableTrade.getTradeType()== TradeType.UNOIN_TABLE_SUB || dinnertableTrade.getTradeType()== TradeType.UNOIN_TABLE_MAIN ){
//			ToastUtil.showShortToast(getString(R.string.diner_abolish_union_trade));
//			return;
//		}

        if (dinnertableTrade != null && UnionUtil.isUnionSubTrade(dinnertableTrade.getTradeType())) {
            ToastUtil.showLongToast(R.string.diner_abolish_union_trade);
            return;
        }

        VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_INVALID,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        doDelete(dinnertableTrade);
                    }
                });
    }

    @Override
    public void doMergeMoveDishDialog(final IDinnertableTradeMoveDish orginal, final IDinnertableTrade dest) {
        String headTitle = MoreMenuPopWindow.operateType == OperateType.MOVE_DISH ? getActivity().getString(R.string.move_dishes) : getActivity().getString(R.string.dinner_copydish);
        String keyWord = MoreMenuPopWindow.operateType == OperateType.MOVE_DISH ? getActivity().getString(R.string.dinner_movedish) : getActivity().getString(R.string.dinner_copydish);
        String title = getActivity().getString(R.string.dinner_move_dish_merge_dialog_title, orginal.getSn(), dest.getSn(), keyWord);


        final DishQuantityAndSeatEditDialogFragment fragment = bulildDishQuantitySeatEditDialog(headTitle, title, R.drawable.commonmodule_dialog_icon_warning, R.string.dinner_ok, R.string.dinner_cancel);

        fragment.setPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(final View arg0) {// 确定
                final List<DishQuantityBean> dishItes = fragment.mDishQuantityBeans;
                final boolean copyProperty = fragment.copyDishPropertyCb.isChecked();
                final boolean printKitcen = fragment.printKitchenCb.isChecked();
                checkMergeOrTransfer(orginal.getTradeUuid(), getMoveDishItems(orginal), dest.getId(), TicketTypeEnum.MOVEDISH, new Runnable() {
                    public void run() {

                        if (MoreMenuPopWindow.operateType == OperateType.MOVE_DISH) {
                            UserActionEvent.start(UserActionEvent.DINNER_DISHES_MOVE);
                            mManager.mergeMoveDish(dishItes, orginal, dest, 1, true, false);
                        } else {
                            UserActionEvent.start(UserActionEvent.DINNER_DISHES_COPY);
                            mManager.mergeMoveDish(dishItes, orginal, dest, 2, copyProperty, printKitcen);
                        }
                    }
                });
            }
        });


        fragment.setData(orginal, dest.getTableSeats());
        fragment.setmOperateType(MoreMenuPopWindow.operateType);

        fragment.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void doTransferMoveDishDialog(final IDinnertableTradeMoveDish orginal, final IDinnertable dest) {
        String headTitle = MoreMenuPopWindow.operateType == OperateType.MOVE_DISH ? getActivity().getString(R.string.move_dishes) : getActivity().getString(R.string.dinner_copydish);
        String keyWord = MoreMenuPopWindow.operateType == OperateType.MOVE_DISH ? getActivity().getString(R.string.dinner_movedish) : getActivity().getString(R.string.dinner_copydish);

        String title = getActivity().getString(R.string.dinner_move_dish_transfer_dialog_title, orginal.getSn(), dest.getName(), keyWord);

        final DishQuantityAndSeatEditDialogFragment fragment = bulildDishQuantitySeatEditDialog(headTitle, title, R.drawable.commonmodule_dialog_icon_warning, R.string.dinner_ok, R.string.dinner_cancel);

        fragment.setPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(final View arg0) {// 确定
                final List<DishQuantityBean> dishItes = fragment.mDishQuantityBeans;
                final boolean copyProperty = fragment.copyDishPropertyCb.isChecked();
                final boolean printKitcen = fragment.printKitchenCb.isChecked();
                checkMergeOrTransfer(orginal.getTradeUuid(), getMoveDishItems(orginal), dest.getId(), TicketTypeEnum.MOVEDISH, new Runnable() {
                    public void run() {
                        if (MoreMenuPopWindow.operateType == OperateType.MOVE_DISH) {
                            UserActionEvent.start(UserActionEvent.DINNER_DISHES_MOVE);
                            mManager.transferMoveDish(dishItes, orginal, dest, 1, true, false);
                        } else {
                            UserActionEvent.start(UserActionEvent.DINNER_DISHES_COPY);
                            mManager.transferMoveDish(dishItes, orginal, dest, 2, copyProperty, printKitcen);
                        }
                    }
                });
            }
        });

        fragment.setData(orginal, dest.getTableSeats());
        fragment.setmOperateType(MoreMenuPopWindow.operateType);

        fragment.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void doMergeDialog(final IDinnertableTrade orginal, final IDinnertableTrade dest) {
        List<IShopcartItem> shopcartItems = getInfoFragment().getDinnerTableTradeVo().getItems();
        if (Utils.isEmpty(shopcartItems) || !DinnerUtils.isWestStyle()) {//如果没有菜品，直接合单
            super.doMergeDialog(orginal, dest);
            return;
        }


        String headTitle = getActivity().getString(R.string.merge_table_ticket);
        String keyWord = getActivity().getString(R.string.dinner_mix_trade);

        String title = getActivity().getString(R.string.dinner_move_dish_merge_dialog_title, orginal.getSn(), dest.getSn(), keyWord);
        final DishQuantityAndSeatEditDialogFragment fragment = bulildDishQuantitySeatEditDialog(headTitle, title, R.drawable.commonmodule_dialog_icon_warning, R.string.dinner_ok, R.string.dinner_cancel);

        fragment.setPositiveListener(new View.OnClickListener() {

            @Override
            public void onClick(final View arg0) {
                String tradeTableUuid = dest.getUuid();
                try {
                    TradeTable tradeTable = DBHelperManager.queryById(TradeTable.class, tradeTableUuid);
                    final List<DishQuantityBean> dishItes = fragment.mDishQuantityBeans;
                    if (tradeTable != null) {
                        checkMergeOrTransfer(orginal.getTradeUuid(), null, tradeTable.getTableId(), TicketTypeEnum.MERGETABLE, new Runnable() {
                            public void run() {
                                List<TradeItemExtraDinner> seats = buildTradeItemExtraDinners(getInfoFragment().getDinnerTableTradeVo(), dishItes);
                                mManager.merge(seats, orginal, dest);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        if (DinnerUtils.isWestStyle()) {
            fragment.setData(getInfoFragment().getDinnerTableTradeVo().getItems(), dest.getTableSeats());
        } else {
            fragment.setData(getInfoFragment().getDinnerTableTradeVo().getItems(), null);
        }

        fragment.setmOperateType(OperateType.MARGE_TRADE);

        fragment.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void transferTableDialog(final IDinnertableTrade orginal, final IDinnertable dest) {
        String headTitle = getActivity().getString(R.string.transfer_table_ticket);
        String keyWord = getActivity().getString(R.string.dinner_change_table);
        String title = getActivity().getString(R.string.dinner_move_dish_transfer_dialog_title, orginal.getSn(), dest.getName(), keyWord);

        final DishQuantityAndSeatEditDialogFragment fragment = bulildDishQuantitySeatEditDialog(headTitle, title, R.drawable.commonmodule_dialog_icon_warning, R.string.dinner_ok, R.string.dinner_cancel);

        fragment.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<DishQuantityBean> dishItes = fragment.mDishQuantityBeans;
                List<TradeItemExtraDinner> seats = buildTradeItemExtraDinners(getInfoFragment().getDinnerTableTradeVo(), dishItes, false);
                mManager.transfer(seats, orginal, dest);
            }
        });


        fragment.setData(getInfoFragment().getDinnerTableTradeVo().getItems(), dest.getTableSeats());
        fragment.setmOperateType(OperateType.TRANSFER_TABLE);

        fragment.show(getFragmentManager(), DIALOG_TAG);
    }


    @Override
    protected boolean onTableSelect(DinnertableModel model, TableViewBase dinnertableView) {
        if (model.isGroup() || model.isBuffet()) {
            mZoneView.hideControl();
            mZoneView.setSelectState(model.getId());
            return false;
        }

        return super.onTableSelect(model, dinnertableView);
    }


    //modify  20170828 屏蔽顾客到店列表
	/*public void onEventMainThread(CustomerArriveEvent event) {
		showCustomerArriveDot();
	}

	private void showCustomerArriveDot() {
		boolean isShowDot = SpHelper.getDefault().getBoolean(SpHelper.CUSTOMER_ARRIVE_DOT);
		iv_customer_arrive_dot.setVisibility(isShowDot ? View.VISIBLE : View.GONE);
	}

	private void hideCustomerArriveDot() {
		SpHelper.getDefault().putBoolean(SpHelper.CUSTOMER_ARRIVE_DOT, false);
		showCustomerArriveDot();
	}*/
    //modify  20170828 屏蔽顾客到店列表
    private List<TradeItemExtraDinner> buildTradeItemExtraDinners(DinnertableTradeVo dinnertableTradeVo, List<DishQuantityBean> listDishItem) {
        return buildTradeItemExtraDinners(dinnertableTradeVo, listDishItem, true);
    }

    private List<TradeItemExtraDinner> buildTradeItemExtraDinners(DinnertableTradeVo dinnertableTradeVo, List<DishQuantityBean> listDishItem, boolean disabledStatusFlag) {
        List<TradeItemExtraDinner> listSeats = new ArrayList<TradeItemExtraDinner>();

        Map<Long, TradeItemExtraDinner> mapSeats = new HashMap<Long, TradeItemExtraDinner>();

        List<TradeItemVo> listTradeItemVos = dinnertableTradeVo.getTradeVo().getTradeItemList();

        for (TradeItemVo tradeItemVo : listTradeItemVos) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            if (tradeItem.getType() != DishType.SINGLE && tradeItem.getType() != DishType.COMBO) {
                continue;
            }

            if (tradeItemVo.getTradeItemExtraDinner() != null) {
                tradeItemVo.getTradeItemExtraDinner().setClientUpdateTime(System.currentTimeMillis());
                tradeItemVo.getTradeItemExtraDinner().setChanged(true);
                if (disabledStatusFlag) {
                    tradeItemVo.getTradeItemExtraDinner().setStatusFlag(StatusFlag.INVALID);
                }
                mapSeats.put(tradeItemVo.getTradeItem().getId(), tradeItemVo.getTradeItemExtraDinner());
                listSeats.add(tradeItemVo.getTradeItemExtraDinner());
            }
        }

        AuthUser user = Session.getAuthUser();

        for (DishQuantityBean dishQuantity : listDishItem) {
            if (dishQuantity.tableSeat == null) {
                continue;
            }

            if (mapSeats.containsKey(dishQuantity.shopcartItem.getId())) {
                TradeItemExtraDinner seat = mapSeats.get(dishQuantity.shopcartItem.getId());
                seat.setSeatId(dishQuantity.tableSeat.getId());
                seat.setSeatNumber(dishQuantity.tableSeat.getSeatName());
                seat.setUpdatorName(user.getName());
                seat.setUpdatorId(user.getId());
                seat.setClientUpdateTime(System.currentTimeMillis());
                seat.setStatusFlag(StatusFlag.VALID);
                seat.setChanged(true);
            } else {
                listSeats.add(buildTradeItemExtraDinner(dishQuantity.shopcartItem, dishQuantity.tableSeat));
            }
        }

        return listSeats;
    }


    private TradeItemExtraDinner buildTradeItemExtraDinner(IShopcartItem shopcartItem, TableSeat tableSeat) {
        if (tableSeat == null) {
            return null;
        }
        TradeItemExtraDinner tradeItemExtraDinner = new TradeItemExtraDinner();
        tradeItemExtraDinner.setStatusFlag(StatusFlag.VALID);
        tradeItemExtraDinner.setChanged(true);
        tradeItemExtraDinner.setTradeItemId(shopcartItem.getId());
        tradeItemExtraDinner.setTradeItemUuid(shopcartItem.getUuid());
        tradeItemExtraDinner.setSeatId(tableSeat.getId());
        tradeItemExtraDinner.setSeatNumber(tableSeat.getSeatName());
        tradeItemExtraDinner.setClientCreateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setClientUpdateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeItemExtraDinner.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeItemExtraDinner.setCreatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setCreatorName(Session.getAuthUser().getName());
        tradeItemExtraDinner.setUpdatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setUpdatorName(Session.getAuthUser().getName());
        return tradeItemExtraDinner;
    }

}
