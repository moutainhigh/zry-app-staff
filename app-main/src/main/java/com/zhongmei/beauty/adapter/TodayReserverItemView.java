package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.bty.basemodule.booking.bean.BookingTradeItemVo;
import com.zhongmei.yunfu.db.entity.booking.BookingTradeItem;
import com.zhongmei.beauty.entity.BookingTradeItemUser;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EViewGroup(R.layout.beauty_reserver_item)
public class TodayReserverItemView extends RelativeLayout implements View.OnClickListener {

    @ViewById(R.id.iv_member_header)
    protected ImageView iv_memberHeader;

    @ViewById(R.id.tv_name)
    protected TextView tv_name;

    @ViewById(R.id.tv_phone)
    protected TextView tv_phone;

    @ViewById(R.id.tv_servers)
    protected TextView tv_servers;

    @ViewById(R.id.tv_technician)
    protected TextView tv_technician;

    @ViewById(R.id.tv_time)
    protected TextView tv_time;
    @ViewById(R.id.btn_modify)
    protected Button btn_modify;

    @ViewById(R.id.btn_create_trade)
    protected Button btn_createTrade;

    private OnOperateListener mOperateListener;

    private BeautyBookingVo mReserver;

    public void setmOperateListener(OnOperateListener mOperateListener) {
        this.mOperateListener = mOperateListener;
    }

    @AfterViews
    public void init() {
        btn_modify.setOnClickListener(this);
        btn_createTrade.setOnClickListener(this);
    }

    public void refreshUI(BeautyBookingVo reserver) {
        mReserver = reserver;
                iv_memberHeader.setBackgroundResource(getCustomerHead(mReserver.getBooking().getCommercialGender()));
        tv_name.setText(mReserver.getBooking().getCommercialName());
        tv_phone.setText(mReserver.getBooking().getCommercialPhone());
        tv_servers.setText(getTradeItemInfo(mReserver.getBookingTradeItemVos()));
        tv_time.setText("到店时间:" + DateUtil.fomatDayTime(mReserver.getBooking().getStartTime()));
        tv_technician.setText(getTechnicial(reserver.getBookingTradeItemVos()));
    }



    public String getTechnicial(List<BookingTradeItemVo> tradeItemVos) {
        if (Utils.isEmpty(tradeItemVos) || Utils.isEmpty(tradeItemVos.get(0).getBookingTradeItemUsers())) {
            return "未指定";
        } else {
            return tradeItemVos.get(0).getBookingTradeItemUsers().get(0).getUserName();
        }
    }



    private int getCustomerHead(Sex sex) {
        int headRes = com.zhongmei.yunfu.beauty.R.drawable.beauty_customer_default;
        if (sex == Sex.FEMALE) {
            headRes = com.zhongmei.yunfu.beauty.R.drawable.beauty_customer_female;
        } else if (sex == Sex.MALE) {
            headRes = com.zhongmei.yunfu.beauty.R.drawable.beauty_customer_male;
        }
        return headRes;
    }


    private String getTradeItemInfo(List<BookingTradeItemVo> tradeItemVos) {
        if (Utils.isEmpty(tradeItemVos)) {
            return getContext().getResources().getString(R.string.beauty_no_service);
        }

        int itemLen = tradeItemVos.size() > 3 ? 3 : tradeItemVos.size();

        StringBuffer itemNameBuffer = new StringBuffer();
        for (int i = 0; i < itemLen; i++) {
            BookingTradeItem vo = tradeItemVos.get(i).getTradeItem();
            itemNameBuffer.append(vo.getDishName());
            itemNameBuffer.append("x" + vo.getQuantity());
            itemNameBuffer.append(",\n");
        }

        return itemNameBuffer.substring(0, itemNameBuffer.length() - 2);
    }


    public TodayReserverItemView(Context context) {
        super(context);
    }

    public TodayReserverItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TodayReserverItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_modify:
                if (mOperateListener != null) {
                    mOperateListener.reserverModify(mReserver);
                }
                break;
            case R.id.btn_create_trade:
                if (mOperateListener != null) {
                    mOperateListener.reserverCreateTrade(mReserver);
                }
                break;
        }
    }

    public interface OnOperateListener {
        void reserverModify(BeautyBookingVo mReserver);

        void reserverCreateTrade(BeautyBookingVo mReserver);
    }
}
