package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.beauty.entity.UnpaidTradeVo;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.context.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EViewGroup(R.layout.beauty_unpaid_trade_item)
public class UnpaidTradeItemView extends RelativeLayout implements View.OnClickListener {

    @ViewById(R.id.iv_member_header)
    protected ImageView iv_memberHeader;

    @ViewById(R.id.tv_name)
    protected TextView tv_name;

    @ViewById(R.id.tv_phone)
    protected TextView tv_phone;

    @ViewById(R.id.tv_serial_no)
    protected TextView tv_serialNo;

    @ViewById(R.id.tv_servers)
    protected TextView tv_servers;

    @ViewById(R.id.tv_amount)
    protected TextView tv_amount;

    @ViewById(R.id.tv_status)
    protected TextView tv_status;

    @ViewById(R.id.tv_time)
    protected TextView tv_time;
    @ViewById(R.id.btn_delete)
    protected Button btn_delete;

    @ViewById(R.id.btn_trade_details)
    protected Button btn_tradeDetails;

    private OnOperateListener mOperateListener;

    private UnpaidTradeVo mTradeVo;

    public void setmOperateListener(OnOperateListener mOperateListener) {
        this.mOperateListener = mOperateListener;
    }

    @AfterViews
    public void init() {
        btn_delete.setOnClickListener(this);
        btn_tradeDetails.setOnClickListener(this);
    }

    public void refreshUI(UnpaidTradeVo tradeVo) {
        mTradeVo = tradeVo;
        tv_time.setText(tradeVo.getTradeTime());
        tv_amount.setText(Utils.formatPrice(tradeVo.getTradeAmount()));
        tv_servers.setText(tradeVo.getServices());


        TradeCustomer customer = tradeVo.getMember();
        String customerName = getResources().getString(R.string.beauty_unknow_customer);
        String customerPhone = "";
        Sex customerSex = Sex.UNKNOW;
        if (customer != null) {
            customerName = customer.getCustomerName();
            customerPhone = customer.getCustomerPhone();
            customerSex = customer.getCustomerSex();
            tv_phone.setVisibility(View.VISIBLE);
        } else {
            tv_phone.setVisibility(View.GONE);
        }

        tv_name.setText(customerName);
        tv_phone.setText(customerPhone);
        iv_memberHeader.setImageResource(getCustomerHead(customerSex));

        TradePayStatus status = tradeVo.getTradePayStatus();
        tv_status.setText(getPayStatus(status));

        tv_servers.setText(getTradeItemInfo(tradeVo.getTradeItemList()));

        tv_serialNo.setText(tradeVo.getTrade().getSerialNumber() + "");
    }


    public UnpaidTradeItemView(Context context) {
        super(context);
    }

    public UnpaidTradeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnpaidTradeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                if (mOperateListener != null) {
                    mOperateListener.tradeDelete(mTradeVo);
                }
                break;
            case R.id.btn_trade_details:
                if (mOperateListener != null) {
                    mOperateListener.tradeDetilas(mTradeVo);
                }
                break;
        }
    }

    public interface OnOperateListener {
        void tradeDelete(UnpaidTradeVo tradeVo);

        void tradeDetilas(UnpaidTradeVo tradeVo);
    }

    private String getStatus(TradeStatus status) {
        String statusStr = getResources().getString(R.string.beauty_order_status_unprocessed);
        if (status == TradeStatus.UNPROCESSED) {
            statusStr = getResources().getString(R.string.beauty_order_status_unprocessed);
        } else if (status == TradeStatus.CONFIRMED) {
            statusStr = getResources().getString(R.string.beauty_order_status_confirmed);
        }
        return statusStr;
    }

    private String getPayStatus(TradePayStatus payStatus) {
        String payStatusStr = getResources().getString(R.string.beauty_pay_status_unpaid);
        if (payStatus == TradePayStatus.UNPAID) {
            payStatusStr = getResources().getString(R.string.beauty_pay_status_unpaid);
        } else if (payStatus == TradePayStatus.PAYING) {
            payStatusStr = getResources().getString(R.string.beauty_pay_status_paiding);
        }
        return payStatusStr;
    }

    private int getCustomerHead(Sex sex) {
        int headRes = R.drawable.beauty_customer_default;
        if (sex == Sex.FEMALE) {
            headRes = R.drawable.beauty_customer_female;
        } else if (sex == Sex.MALE) {
            headRes = R.drawable.beauty_customer_male;
        }
        return headRes;
    }

    private String getTradeItemInfo(List<TradeItemVo> tradeItemVos) {
        if (Utils.isEmpty(tradeItemVos)) {
            return getResources().getString(R.string.beauty_no_service);
        }

        int itemLen = tradeItemVos.size() > 3 ? 3 : tradeItemVos.size();

        StringBuffer itemNameBuffer = new StringBuffer();
        for (int i = 0; i < itemLen; i++) {
            TradeItemVo vo = tradeItemVos.get(i);
            itemNameBuffer.append(vo.getTradeItem().getDishName());
            itemNameBuffer.append("x" + vo.getTradeItem().getQuantity());
            itemNameBuffer.append(",\n");
        }

        return itemNameBuffer.substring(0, itemNameBuffer.length() - 2);
    }

    private String getSerialNo(TradeExtra tradeExtra) {
        if (tradeExtra == null) {
            return null;
        }

        return getResources().getString(R.string.beauty_serial_no) + tradeExtra.getSerialNumber();
    }
}
