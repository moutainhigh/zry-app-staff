package com.zhongmei.bty.dinner.table.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.async.util.AsyncNetworkUtil;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.dinner.table.TableFragment;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.dinner.util.UnionUtil;
import com.zhongmei.bty.commonmodule.util.SpendTimeFormater;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;


public abstract class TableTradeViewBase extends DirectDragableView {

    private static final String TAG = TableTradeViewBase.class.getSimpleName();

    private IDinnertableTrade mModel;
    protected TextView mTimeTextView;
    private DinnertableStatus statusOfView;
    private boolean enableTransparent;
    protected ImageView preCashIv;
    private boolean enablePreCashDisplay = false;
    private boolean enableTradeMoneyDispay = true;
    private boolean enableHttpRecord = true;


    public TableTradeViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(IDinnertableTrade model) {
        UserActionEvent.start(UserActionEvent.DINNER_TRADE_REFRESH);
        statusOfView = DinnertableStatus.EMPTY;
        mModel = model;
        if (mModel == null) {
            setVisibility(View.INVISIBLE);
        } else {
            int color;
            if (enableTransparent) {
                color = ViewUtils.COLOR_TRADE_EMPTY_TRANSPARENT;
            } else {
                color = ViewUtils.COLOR_TRADE_EMPTY;
            }
                        if (mModel.getTradeStatus() != TradeStatus.UNPROCESSED) {
                statusOfView = mModel.getStatus();
                switch (statusOfView) {
                    case UNISSUED:
                        if (enableTransparent) {
                            color = ViewUtils.COLOR_TRADE_UNISSUED_TRANSPARENT;
                        } else {
                            color = ViewUtils.COLOR_TRADE_UNISSUED;
                        }
                        break;
                    case ISSUED:
                        if (enableTransparent) {
                            color = ViewUtils.COLOR_TRADE_ISSUED_TRANSPARENT;
                        } else {
                            color = ViewUtils.COLOR_TRADE_ISSUED;
                        }
                        break;
                    case SERVING:
                        if (enableTransparent) {
                            color = ViewUtils.COLOR_TRADE_SERVING_TRANSPARENT;
                        } else {
                            color = ViewUtils.COLOR_TRADE_SERVING;
                        }
                        break;
                    case EMPTY:
                        break;
                    default:
                        Log.e(TAG, "The status is wrong! " + statusOfView);
                }
            }


            if (mTimeTextView == null) {
                mTimeTextView = (TextView) findViewById(R.id.time);
            }

                        TextView snTextView = (TextView) findViewById(R.id.sn);

            if (isUnionMainTrade(mModel.getTradeType())) {                color = Color.WHITE;
                mTimeTextView.setVisibility(View.GONE);
                snTextView.setPadding(0, DensityUtil.dip2px(getContext(), 5), 0, 0);
            } else {
                mTimeTextView.setVisibility(View.VISIBLE);
            }


            snTextView.setTextColor(color);
            setSN(enableTradeMoneyDispay, model, snTextView);


            mTimeTextView.setBackgroundColor(color);
            doRefreshSpendTime();

            setVisibility(View.VISIBLE);

            preCashIv = (ImageView) findViewById(R.id.image);
                        if (SharedPreferenceUtil.getSpUtil().getBoolean(TableFragment.SHOW_PRECASH_KEY, false)) {
                if (enablePreCashDisplay && mModel.getPreCashPrintStatus() == YesOrNo.YES) {
                    preCashIv.setVisibility(View.VISIBLE);
                } else {
                    preCashIv.setVisibility(View.GONE);
                }
            } else {
                preCashIv.setVisibility(View.GONE);
            }

            showHttpRecord();
            showUnionTrade();

        }
        UserActionEvent.end(UserActionEvent.DINNER_TRADE_REFRESH);
    }

    DinnertableStatus getStatusOfView() {
        return statusOfView;
    }

    public IDinnertableTrade getModel() {
        return mModel;
    }

    public void setEnableTransparent(boolean value) {
        enableTransparent = value;
    }

    private void setSN(boolean enableSn, IDinnertableTrade model, TextView snTextView) {
        if (enableSn && !UnionUtil.isUnionTrade(model.getTradeType())) {
            if (model.getUpContent() == IDinnertableTrade.UpContentType.SN) {                snTextView.setText(model.getSn());
            } else if (mModel.getUpContent() == IDinnertableTrade.UpContentType.TRADE_AMOUNT) {                snTextView.setText(model.getTradeAmount());
            }
        } else {
            if (TextUtils.isEmpty(model.getSn())) {
                                snTextView.setText("?");
            } else {
                snTextView.setText(model.getSn());
            }
        }
    }

    public void refreshSpendTime() {
        if (mModel != null) {
            mModel.refreshSpendTime();
            doRefreshSpendTime();
        }
    }

    protected void doRefreshSpendTime() {
        if (UnionUtil.isUnionTrade(mModel.getTradeType())) {
            mTimeTextView.setText(mModel.getTableName());
        } else {
            int spendTime = mModel.getSpendTime();
            mTimeTextView.setText(SpendTimeFormater.format(spendTime));
        }
    }

    @Override
    protected ClipData getClipData() {
        ClipData.Item item = new ClipData.Item("trade");
        return new ClipData("trade",
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
    }

    public void enablePreCashDisplay(boolean enable) {
        this.enablePreCashDisplay = enable;
    }

    public void enableTradeMoneyDishpay(boolean enable) {
        this.enableTradeMoneyDispay = enable;
    }

    public void enableHttpRecord(boolean enable) {
        enableHttpRecord = enable;
    }

    private void showUnionTrade() {
        if (isUnionMainTrade(mModel.getTradeType())) {
            setBackgroundResource(R.drawable.bg_table_union_trade);
        } else {
            setBackgroundResource(R.drawable.dinnertable_trade_background);
        }
    }

    protected boolean isUnionMainTrade(TradeType tradeType) {
        return tradeType == TradeType.UNOIN_TABLE_MAIN;
    }


    public void showHttpRecord() {
        if (mModel == null || !(mModel instanceof DinnertableTradeModel)) {
            return;
        }

        LinearLayout tableShadowLL = (LinearLayout) findViewById(R.id.table_trade_shadow_ll);
        TextView messageTv = (TextView) findViewById(R.id.message_tv);
        final Button retryTv = (Button) findViewById(R.id.retry_tv);
        final Button btn_cancel = (Button) findViewById(R.id.btn_cancel);

        final AsyncHttpRecord record = ((DinnertableTradeModel) mModel).getAsyncHttpRecord();

        if (enableHttpRecord && record != null) {

                        retryTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncNetworkUtil.retryModifyOrCasher(record);
                }
            });

            btn_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncNetworkManager.getInstance().cancel(record);
                }
            });

            tableShadowLL.setVisibility(View.VISIBLE);
            String operateType = AsyncNetworkUtil.getType(record.getType());
            String strAppend = "";

            if (record.getStatus() == AsyncHttpState.FAILED) {
                retryTv.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                strAppend = getContext().getString(R.string.dinner_failed);
            } else if (record.getStatus() == AsyncHttpState.EXCUTING) {
                retryTv.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                strAppend = getContext().getString(R.string.dinner_doing);
            } else if (record.getStatus() == AsyncHttpState.RETRING) {
                retryTv.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
                strAppend = getContext().getString(R.string.dinner_retrying);
            }
            messageTv.setText(getResources().getString(R.string.dinner_table_trade_async_http_tip, operateType, strAppend));
        } else {
            tableShadowLL.setVisibility(View.GONE);
        }


    }

}


