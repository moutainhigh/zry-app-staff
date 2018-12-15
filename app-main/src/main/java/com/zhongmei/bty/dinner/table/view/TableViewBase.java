package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.basemodule.async.util.AsyncNetworkUtil;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.table.TableFragment;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.commonmodule.util.SpendTimeFormater;

import java.util.List;

/**
 * 正餐桌台视图
 *
 * @version: 1.0
 * @date 2015年8月26日
 */
public class TableViewBase extends FrameLayout {

//	private final List<DinnertableTradeView> mTradeViews;

    protected DinnertableModel mModel;

    protected DinnertableStatus dinnertableStatus;//桌台最终状态

    protected RelativeLayout tradesGroup;

    protected ImageView reserveIv;//当前就餐类型（团餐／自助餐／正餐）

    protected ImageView bookingIv;//预定图标

//	protected TableTradeViewBase tradeView;

    protected View vDinnerTable;

    protected LinearLayout llTableHeader;

    protected TextView tvTradeCount;

    protected TextView tvTradeAmount;

    protected TextView tvTradeTime;

    protected TextView tvPrepayMark;

    protected ImageView ivEmptyTable;

    protected ImageView ivDoneTable;

    public TableViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
//		mTradeViews = new ArrayList<DinnertableTradeView>();
    }

    public DinnertableModel getModel() {
        return mModel;
    }

    public void setModel(DinnertableModel model) {
//		UserActionEvent.start(UserActionEvent.DINNER_TABLE_REFRESH);
        mModel = model;
        refresh();
//		UserActionEvent.end(UserActionEvent.DINNER_TABLE_REFRESH);
    }

    protected TableTradeViewBase createTradeView(IDinnertableTrade tradeModel) {
        TableTradeViewBase tradeView = ViewUtils.inflateTradeView(getContext());
        tradeView.setEnabledDrag(false);
        tradeView.enablePreCashDisplay(true);
        tradeView.enableHttpRecord(false);
        tradeView.setModel(tradeModel);
        return tradeView;
    }

    @SuppressWarnings("deprecation")
    protected void refresh() {
        if (mModel == null) {
            return;
        }

        vDinnerTable = findViewById(R.id.dinnertable);
        vDinnerTable.setBackgroundResource(R.drawable.dinnertable_bg_shadow);
        llTableHeader = (LinearLayout) findViewById(R.id.ll_table_header);
        tradesGroup = (RelativeLayout) findViewById(R.id.dinnertable_trades_group);
        tradesGroup.setBackgroundDrawable(null);
//		mTradeViews.clear();

//		tradeView=(TableTradeViewBase) findViewById(R.id.dinner_table_order_include);
//		tradeView.setVisibility(View.GONE);

        DinnertableStatus status;
        if (mModel.getTableStatus() == TableStatus.EMPTY) {
            status = DinnertableStatus.EMPTY;
        } else {
            status = DinnertableStatus.DONE;
        }

        setTableNameAndPeopleCount(status);

        List<IDinnertableTrade> tradeModels = mModel.getDinnertableTrades();
        //当订单数大于4时，只显示4个
//		int size = tradeModels.size();
//		if (size > 4) {
//			ArrayList<IDinnertableTrade> subList = new ArrayList<IDinnertableTrade>();
//			for (int i = size - 4; i < size; i++) {
//				subList.add(tradeModels.get(i));
//			}
//			tradeModels = subList;
//		}

//		int max=-1;
//		if(tradeModels!=null){
//			max = tradeModels.size() - 1;
//		}
//
//		for (int i = 0; i <= max; i++) {
//			IDinnertableTrade tradeModel = tradeModels.get(i);
//			DinnertableTradeView tradeView = createTradeView(tradeModel);
//			tradesGroup.addView(tradeView);
//			mTradeViews.add(tradeView);
//			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tradeView.getLayoutParams();
//			layoutParams.leftMargin = (max - i) * 4;
//			layoutParams.topMargin = i * 1;
//			tradeView.setLayoutParams(layoutParams);
//
//			if (status.value > tradeView.getStatusOfView().value) {
//				status = tradeModel.getStatus();
//			}
//		}

        ivEmptyTable = (ImageView) findViewById(R.id.iv_empty_table);
        tvTradeCount = (TextView) findViewById(R.id.tv_trade_count);
        tvTradeAmount = (TextView) findViewById(R.id.tv_trade_amount);
        tvTradeTime = (TextView) findViewById(R.id.tv_trade_time);
        tvPrepayMark = (TextView) findViewById(R.id.tv_prepay_mark);
        ivDoneTable = (ImageView) findViewById(R.id.iv_done_table);

        reserveIv = (ImageView) findViewById(R.id.reserve_iv);
        bookingIv = (ImageView) findViewById(R.id.iv_booking_icon);

//		boolean hasJoinTrade = false;//是否有联台订单
        boolean showTradeMoney = SharedPreferenceUtil.getSpUtil().getBoolean(DinnerTableConstant.SHOW_TRADE_MONEY_KEY, true);//桌台上是否显示金额
        if (Utils.isNotEmpty(tradeModels)) {
            IDinnertableTrade tradeModel = tradeModels.get(0);
//			tradeView.setVisibility(View.VISIBLE);
//			tradeView.setEnabledDrag(false);
//			tradeView.enablePreCashDisplay(true);
//			tradeView.enableHttpRecord(false);
//			tradeView.setModel(tradeModel);
            status = tradeModel.getStatus();

            setTradeCount(tradeModels);

//			for(IDinnertableTrade model : tradeModels){
//				if(model.getTradeType() == TradeType.UNOIN_TABLE_SUB){
//					hasJoinTrade = true;
//					break;
//				}
//			}

            tvTradeAmount.setVisibility(View.VISIBLE);
            tvTradeTime.setVisibility(View.VISIBLE);

            setTradeAmount(tradeModel, showTradeMoney);

            setSpendTime(tradeModel);

            setPrepayMark(tradeModel);
        } else {
            tvTradeCount.setVisibility(View.GONE);
            tvTradeAmount.setVisibility(View.GONE);
            tvTradeTime.setVisibility(View.GONE);
            tvPrepayMark.setVisibility(View.GONE);
        }

        dinnertableStatus = status;

        setTableHeaderAndBackground(status);

//		if(hasJoinTrade) {
//			if(joinDrawable != null){
//				joinDrawable.setBounds(0, 0, joinDrawable.getMinimumWidth(), joinDrawable.getMinimumHeight());
//				tvTradeAmount.setCompoundDrawables(joinDrawable, null, null, null);
//			}
//		} else {
//			if(!showTradeMoney) {
//				if (serialDrawable != null) {
//					serialDrawable.setBounds(0, 0, serialDrawable.getMinimumWidth(), serialDrawable.getMinimumHeight());
//					tvTradeAmount.setCompoundDrawables(serialDrawable, null, null, null);
//				}
//			}
//		}

        setUnprocessMark();

        setBookingMark();

        refreshOpenTableStatus();

        invalidate();
    }

    /**
     * 设置桌台标题和背景演示
     *
     * @param status
     */
    private void setTableHeaderAndBackground(DinnertableStatus status) {
//		Drawable joinDrawable = TableFragment.defaultJoinDrawable;;
//		Drawable amountDrawable = TableFragment.defaultAmountDrawable;
//		Drawable serialDrawable = TableFragment.defaultSerialDrawable;;
        Drawable timeDrawable = TableFragment.defaultTimeDrawable;
        ;
        ivEmptyTable.setVisibility(View.GONE);
        ivDoneTable.setVisibility(GONE);
        switch (status) {
            case EMPTY:
//				tradesGroup.setBackgroundResource(R.drawable.dinnertable_trades_group_backgound);
//				vDinnerTable.setBackgroundResource(R.drawable.dinnertable_bg_empty);
//				vDinnerTable.setBackgroundResource(R.drawable.dinnertable_bg_shadow_empty);
                llTableHeader.setBackgroundColor(getResources().getColor(R.color.transparent));
                ivEmptyTable.setVisibility(View.VISIBLE);
                break;
            case UNISSUED:
                llTableHeader.setBackgroundResource(R.drawable.dinnertable_bg_unissued_top);
//				vDinnerTable.setBackgroundResource(R.drawable.dinnertable_bg_unissued);

                tvTradeAmount.setTextColor(Color.parseColor("#D89001"));
                tvTradeTime.setTextColor(Color.parseColor("#D89001"));

//				joinDrawable = TableFragment.unissueJoinDrawable;
//				amountDrawable = TableFragment.unissueAmountDrawable;
//				serialDrawable = TableFragment.unissueSerialDrawable;
                timeDrawable = TableFragment.unissueTimeDrawable;
                break;
            case ISSUED:
                llTableHeader.setBackgroundResource(R.drawable.dinnertable_bg_issued_top);
//				vDinnerTable.setBackgroundResource(R.drawable.dinnertable_bg_issued);

                tvTradeAmount.setTextColor(Color.parseColor("#0FAAAA"));
                tvTradeTime.setTextColor(Color.parseColor("#0FAAAA"));

//				joinDrawable = TableFragment.issuedJoinDrawable;
//				amountDrawable = TableFragment.issuedAmountDrawable;
//				serialDrawable = TableFragment.issuedSerialDrawable;
                timeDrawable = TableFragment.issuedTimeDrawable;
                break;
            case SERVING:
                llTableHeader.setBackgroundResource(R.drawable.dinnertable_bg_serving_top);
//				vDinnerTable.setBackgroundResource(R.drawable.dinnertable_bg_serving);

                tvTradeAmount.setTextColor(Color.parseColor("#0B64D3"));
                tvTradeTime.setTextColor(Color.parseColor("#0B64D3"));

//				joinDrawable = TableFragment.servedJoinDrawable;
//				amountDrawable = TableFragment.servedAmountDrawable;
//				serialDrawable = TableFragment.servedSerialDrawable;
                timeDrawable = TableFragment.servedTimeDrawable;
                break;
            case DONE:
//				llTableHeader.setBackgroundColor(getResources().getColor(R.color.transparent));
                llTableHeader.setBackgroundResource(R.drawable.dinnertable_bg_done_top);
//				vDinnerTable.setBackgroundResource(R.drawable.dinnertable_bg_done);
                ivDoneTable.setVisibility(VISIBLE);
                break;
        }

        if (timeDrawable != null) {
            timeDrawable.setBounds(0, 0, timeDrawable.getMinimumWidth(), timeDrawable.getMinimumHeight());
            tvTradeTime.setCompoundDrawables(timeDrawable, null, null, null);
        }
    }

    /**
     * 设置桌台名和桌台人数
     */
    private void setTableNameAndPeopleCount(DinnertableStatus status) {
        TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(mModel.getName());
        TextView numberView = (TextView) findViewById(R.id.number);
        numberView.setText(mModel.getNumberOfMeals() + "/" + mModel.getNumberOfSeats());

        nameView.setTextColor(status == DinnertableStatus.EMPTY ? getResources().getColor(R.color.text_black)
                : getResources().getColor(R.color.text_white));
        numberView.setTextColor(status == DinnertableStatus.EMPTY ? getResources().getColor(R.color.text_color_gray)
                : getResources().getColor(R.color.text_white));
    }

    /**
     * 设置订单的桌台数（1单时不显示）
     *
     * @param tradeModels
     */
    private void setTradeCount(List<IDinnertableTrade> tradeModels) {
        if (tradeModels.size() > 1) {
            tvTradeCount.setVisibility(View.VISIBLE);
            tvTradeCount.setText(tradeModels.size() + "");
        } else {
            tvTradeCount.setVisibility(View.GONE);
        }
    }

    /**
     * 显示订单金额／流水号；流水号模式下显示流水号，金额模式下显示订单金额；联台订单显示"联＋主单流水号"（不区分模式）
     *
     * @param tradeModel
     * @param showTradeMoney
     */
    private void setTradeAmount(IDinnertableTrade tradeModel, boolean showTradeMoney) {
        IDinnertableTrade joinMainTrade = mModel.getDinnerUnionMainTrade();
        if (joinMainTrade != null) {
            tvTradeAmount.setText(getResources().getString(R.string.join) + joinMainTrade.getSn());
        } else {
            if (showTradeMoney) {
                tvTradeAmount.setText(tradeModel.getTradeAmount());
            } else {
                tvTradeAmount.setText(tradeModel.getSn());
            }
        }
    }

    /**
     * 设置订单花费时间
     *
     * @param tradeModel
     */
    private void setSpendTime(IDinnertableTrade tradeModel) {
        int spendTime = tradeModel.getSpendTime();
        tvTradeTime.setText(SpendTimeFormater.format(spendTime));
    }

    /**
     * 展示预结单标示
     *
     * @param tradeModel
     */
    private void setPrepayMark(IDinnertableTrade tradeModel) {
        if (SharedPreferenceUtil.getSpUtil().getBoolean(TableFragment.SHOW_PRECASH_KEY, false) &&
                tradeModel.getPreCashPrintStatus() == YesOrNo.YES) {
            tvPrepayMark.setVisibility(View.VISIBLE);
        } else {
            tvPrepayMark.setVisibility(View.GONE);
        }
    }

    /**
     * 展示未处理订单标示
     */
    private void setUnprocessMark() {
        ImageView unProcessNumberView = (ImageView) findViewById(R.id.tv_unprocess_trade_number);
        if (mModel.getUnprocessTradeCount() > 0 || mModel.hasAddDish()) {
            unProcessNumberView.setVisibility(View.VISIBLE);
        } else {
            unProcessNumberView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示桌台预定标示
     */
    private void setBookingMark() {
        if (mModel.isReserved()) {
            bookingIv.setVisibility(View.VISIBLE);
        } else {
            bookingIv.setVisibility(View.GONE);
        }
    }

    void refreshSpendTime() {
//		for (DinnertableTradeView tradeView : mTradeViews) {
//			tradeView.refreshSpendTime();
//		}
//		if(tradeView!=null){
//			tradeView.refreshSpendTime();
//		}

        if (Utils.isNotEmpty(mModel.getDinnertableTrades())) {
            IDinnertableTrade tradeModel = mModel.getDinnertableTrades().get(0);
            tradeModel.refreshSpendTime();
            int spendTime = tradeModel.getSpendTime();
            tvTradeTime.setText(SpendTimeFormater.format(spendTime));
        }
    }

    void select() {
        View shade = findViewById(R.id.dinnertable_shade);
//		shade.setVisibility(View.VISIBLE);
        shade.setBackgroundResource(R.drawable.dinnertable_shape_square);
    }

    void unselect() {
        View shade = findViewById(R.id.dinnertable_shade);
//		shade.setVisibility(View.INVISIBLE);
        shade.setBackground(null);
    }

    public DinnertableStatus getDinnertableStatus() {
        return dinnertableStatus;
    }

    /**
     * @Date 2016/7/21
     * @Description:
     * @Param
     * @Return
     */
    public void showOpenTableButton(boolean show) {
        if (show) {
            ivEmptyTable.setVisibility(View.GONE);
        } else {
            ivEmptyTable.setVisibility(View.VISIBLE);
        }

    }

    /**
     * @Date 2016/8/12
     * @Description:显示预定信息
     * @Param
     * @Return
     */
    void refreshReserveStatus() {
        if (mModel.isReserved()) {
            reserveIv.setVisibility(View.VISIBLE);
        } else {
            reserveIv.setVisibility(View.GONE);
        }
    }

    private void showHttpRecord() {
        AsyncHttpRecord record = mModel.getAsyncHttpRecord();
        showHttpRecord(record);
    }

    public void refreshOpenTableStatus() {
        List<AsyncHttpRecord> listAsyncRecord = mModel.getmListHttpRecord();

        if (Utils.isNotEmpty(listAsyncRecord)) {
            showHttpRecord(listAsyncRecord.get(0));
        } else {
            showHttpRecord(null);//隐藏
        }
    }

    /**
     * @Date 2016/10/13
     * @Description:显示异步请求信息,根据桌台上的订单,在订单上拿到的异步失败的记录
     * @Param
     * @Return
     */
    private void showHttpRecord(final AsyncHttpRecord record) {
        LinearLayout tableShadowLL = (LinearLayout) findViewById(R.id.table_shadow_ll);

        if (record == null) {
            tableShadowLL.setVisibility(View.GONE);
            return;
        }

        tableShadowLL.setVisibility(View.VISIBLE);

        TextView messageTv = (TextView) findViewById(R.id.table_message_tv);
        Button retryTv = (Button) findViewById(R.id.table_retry_tv);
        Button cancelTv = (Button) findViewById(R.id.table_cancel_tv);

        //重试响应
        retryTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgentEvent.onEvent(TableViewBase.this.getContext(), MobclickAgentEvent.dinnerAsyncHttpTableRetry);

                AsyncNetworkUtil.retryAsyncOperate(record, null);
            }
        });

        cancelTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消操作
                AsyncNetworkManager.getInstance().cancel(record);
            }
        });


        String operateType = AsyncNetworkUtil.getType(record.getType());
        String strAppend = "";

        if (record.getStatus() == AsyncHttpState.FAILED) {
            retryTv.setVisibility(View.VISIBLE);
            cancelTv.setVisibility(View.GONE);
            strAppend = getContext().getString(R.string.dinner_failed);
        } else if (record.getStatus() == AsyncHttpState.EXCUTING) {
            retryTv.setVisibility(View.GONE);
            cancelTv.setVisibility(View.VISIBLE);
            strAppend = getContext().getString(R.string.dinner_doing);
        } else if (record.getStatus() == AsyncHttpState.RETRING) {
            retryTv.setVisibility(View.GONE);
            cancelTv.setVisibility(View.VISIBLE);
            strAppend = getContext().getString(R.string.dinner_retrying);
        }

        String strSerialNumber = "";
        if (!TextUtils.isEmpty(record.getSerialNumber())) {
            strSerialNumber = getResources().getString(R.string.dinner_table_serial_number, record.getSerialNumber());
        }

        messageTv.setText(getResources().getString(R.string.dinner_table_async_http_tip, strSerialNumber, operateType + strAppend));
        Log.e("StateCache", messageTv.getText().toString());
    }

}
