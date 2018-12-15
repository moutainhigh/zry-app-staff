package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.enums.OperateType;
import com.zhongmei.bty.basemodule.trade.event.ActionTableInfoBottomBarStatus;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.util.DinnerMobClickAgentEvent;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.table.TableInfoContentBean;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.dinner.util.UnionUtil;

import de.greenrobot.event.EventBus;

/**
 * @Description:桌台信息页菜单栏
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class MoreMenuPopWindow extends PopupWindow implements View.OnTouchListener, View.OnClickListener {
    private View parentView;

    private FragmentActivity activity;

    private TextView moveDishTv;//移菜按钮

    private TextView copyDishTv;//复制按钮

    private TextView makeDishTv;//等叫按钮

    private TextView urgeDishTv;//崔菜


    private int contentWidth;

    private TableInfoFragment tableInfoFragment;

    public static OperateType operateType;//是移菜还是复制


    public MoreMenuPopWindow(FragmentActivity activity, View parentView, int contentWidth) {
        super();
        this.activity = activity;
        this.parentView = parentView;
        this.contentWidth = contentWidth;
        initialView(activity);
    }

    private void initialView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.more_menu_popwindow, null);
        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setTouchable(true);
        setTouchInterceptor(this);
        setBackgroundDrawable(new ColorDrawable(0));

        LinearLayout contentView = (LinearLayout) view.findViewById(R.id.content_ll);
        contentView.getLayoutParams().width = contentWidth;

        tableInfoFragment = (TableInfoFragment) activity.getSupportFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
//		setAnimationStyle(R.style.dinner_table_info_waiterwindow_style);

        moveDishTv = (TextView) view.findViewById(R.id.move_dish_tv);
        copyDishTv = (TextView) view.findViewById(R.id.copy_dish_tv);
        makeDishTv = (TextView) view.findViewById(R.id.dish_make_tv);
        urgeDishTv = (TextView) view.findViewById(R.id.dish_urge_tv);

        moveDishTv.setOnClickListener(this);
        copyDishTv.setOnClickListener(this);
        makeDishTv.setOnClickListener(this);
        urgeDishTv.setOnClickListener(this);

        if (tableInfoFragment != null) {
            switch (tableInfoFragment.getBusinessType()) {
                case BUFFET:
                    moveDishTv.setVisibility(View.GONE);
                    copyDishTv.setVisibility(View.GONE);
                    makeDishTv.setVisibility(View.GONE);
                    break;
                default:
                    makeDishTv.setVisibility(View.VISIBLE);

                    if (UnionUtil.isUnionTrade(tableInfoFragment.getTradeType())) {
                        moveDishTv.setVisibility(View.GONE);
                        copyDishTv.setVisibility(View.GONE);
                    } else {
                        moveDishTv.setVisibility(View.VISIBLE);
                        copyDishTv.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }


    }

    public void show() {
        if (parentView != null) {
            if (!isShowing()) {
                showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
            }

        }
    }

    public void hide() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (inRangeOfView(getContentView(), event)) {
            return false;
        } else {
            hide();
            return true;
        }
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        View contentView = getContentView();
        int x = contentView.getPaddingLeft();
        int y = contentView.getPaddingTop();
        View contentView2 = contentView.findViewById(R.id.content_ll);
        int width = contentView2.getWidth();
        int height = contentView2.getHeight();
        if (ev.getX() < x || ev.getX() > (x + width) || ev.getY() < y || ev.getY() > (y + height)) {
            return false;
        }
        return true;
    }

    /**
     * @Date 2016年4月25日
     * @Description:
     * @Param
     */
    @Override
    public void onClick(View v) {
        hide();
        switch (v.getId()) {
            case R.id.move_dish_tv://移菜
                MobclickAgentEvent.onEvent(activity, DinnerMobClickAgentEvent.tableDetailsMovedish);
                VerifyHelper.verifyAlert(activity, DinnerApplication.PERMISSION_DINNER_MOVE_DISH,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                operateType = OperateType.MOVE_DISH;
                                showDishCheckMode();
                            }
                        });
                break;
            case R.id.copy_dish_tv://复制
                MobclickAgentEvent.onEvent(activity, DinnerMobClickAgentEvent.tableDetailsCopydish);
                operateType = OperateType.COPY_DISH;
                showDishCheckMode();
                break;
            case R.id.dish_make_tv://起菜
                MobclickAgentEvent.onEvent(activity, DinnerMobClickAgentEvent.tableDetailsMakedish);
                showDishOperateCheckMode(PrintOperationOpType.RISE_DISH);
                break;
            case R.id.dish_urge_tv://崔菜
                MobclickAgentEvent.onEvent(activity, DinnerMobClickAgentEvent.tableDetailsUrgedish);
                showDishOperateCheckMode(PrintOperationOpType.REMIND_DISH);
                break;
            default:
                break;
        }

    }

    /**
     * @Date 2016年4月26日
     * @Description: 列表展示出checkbox
     * @Return void
     */
    public void showDishCheckMode() {
        TableInfoContentBean tableInfoContentBean = tableInfoFragment.talbeInfoContentBean;
        DinnertableTradeVo dinnertableTradeVo = tableInfoContentBean.getDinnerTableTradeVo();
        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();

        //微信未接收订单，大众点评已付订单不允许移菜
        if (dinnertableTradeVo == null || dinnertableTradeVo.getStatus() == DinnertableStatus.EMPTY) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_emptytrade);
            return;
        } else if (TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.WECHAT)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_wechat_unprocessed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedUnAcceptFromFAMILIAR(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_faimliar_unprocessed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedUnAcceptFromOpenPlatform(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_openplatform_unprocessed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.DIANPING)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_dianping_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.XIN_MEI_DA)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_xinmeida_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.KOU_BEI)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_koubei_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAcceptedFromBAIDURICE(dinnertableTradeVo) || TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_bairice_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedFromShuKe(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_returnguest_payed_trade);
            return;
        }

        //无产品的情况
        if ((tradeVo.getTradeItemList() == null || tradeVo.getTradeItemList().size() == 0)
                && (Utils.isEmpty(tradeVo.getCouponPrivilegeVoList()) || tradeVo.getIntegralCashPrivilegeVo() == null ||
                tradeVo.getTradePrivilege() == null)) {
            ToastUtil.showShortToast(R.string.dinner_move_dish_no_dish);
            return;
        }

        //排除非法项
        boolean hasDish = false;
        for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
            if (itemVo.getTradeItem().getStatusFlag() == StatusFlag.VALID && itemVo.getTradeItem().getQuantity().doubleValue() != 0) {
                hasDish = true;
                break;
            }
        }

        if (hasDish) {
            post(new ActionTableInfoBottomBarStatus(ActionTableInfoBottomBarStatus.BottomBarStatus.MOVE_DISH_CHOOSE_MODE));

        } else {
            ToastUtil.showShortToast(R.string.dinner_move_dish_no_dish);
        }
    }

    public void post(Object obj) {
        EventBus.getDefault().post(obj);
    }

//	public enum OperateType{
//		MOVE_DISH(BaseApplication.sInstance.getResources().getString(R.string.dinner_movedish)),
//		COPY_DISH(BaseApplication.sInstance.getResources().getString(R.string.dinner_copydish)),
//		TRANSFER_TABLE(BaseApplication.sInstance.getResources().getString(R.string.dinner_change_table)),
//		MARGE_TRADE(BaseApplication.sInstance.getResources().getString(R.string.dinner_mix_trade));
//
//		private String typeName;
//
//		private OperateType(String typeName){
//			this.typeName=typeName;
//		}
//
//		public String desc() {
//			return typeName;
//		}
//	}

    /**
     * @Date 2016/10/10
     * @Description:点击等叫、崔菜后的响应
     * @Param
     * @Return
     */
    public void showDishOperateCheckMode(PrintOperationOpType opType) {
        TableInfoContentBean tableInfoContentBean = tableInfoFragment.talbeInfoContentBean;
        DinnertableTradeVo dinnertableTradeVo = tableInfoContentBean.getDinnerTableTradeVo();
        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();

        //微信未接收订单，大众点评已付订单不允许移菜
        if (dinnertableTradeVo == null || dinnertableTradeVo.getStatus() == DinnertableStatus.EMPTY) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_emptytrade);
            return;
        } else if (TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.WECHAT)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_wechat_unprocessed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedUnAcceptFromFAMILIAR(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_familiar_unprocessed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedUnAcceptFromOpenPlatform(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_openplatform_unprocessed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.DIANPING)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_dianping_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.XIN_MEI_DA)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_xinmeida_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.KOU_BEI)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_koubei_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedAcceptedFromBAIDURICE(dinnertableTradeVo) || TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_bairice_payed_trade);
            return;
        } else if (TradeSourceUtils.isTradePayedFromShuKe(dinnertableTradeVo)) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_shuke_payed_trade);
            return;
        }

        //无产品的情况
        if (tradeVo.getTradeItemList() == null || tradeVo.getTradeItemList().size() == 0) {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_no_dish);
            return;
        }

        //排除非法项
        boolean hasDish = false;
        for (TradeItemVo itemVo : tradeVo.getTradeItemList()) {
            if (itemVo.getTradeItem().getStatusFlag() == StatusFlag.VALID && itemVo.getTradeItem().getQuantity().doubleValue() != 0) {
                hasDish = true;
                break;
            }
        }

        if (hasDish) {
            tableInfoFragment.talbeInfoContentBean.showDishOperateCheckMode(true, opType);
        } else {
            ToastUtil.showShortToast(R.string.dinner_dishoperate_no_dish);
        }
    }
}
