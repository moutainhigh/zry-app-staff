package com.zhongmei.beauty.booking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongmei.beauty.adapter.BeautyBookingTechnicianAdapter;
import com.zhongmei.beauty.booking.bean.BeautyBookingBoardVo;
import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.booking.constants.BeautyBookingEnum;
import com.zhongmei.beauty.booking.interfaces.BeautyBoardDataListener;
import com.zhongmei.beauty.booking.interfaces.BeautyBookingTradeControlListener;
import com.zhongmei.beauty.booking.list.manager.BeautyBoardManager;
import com.zhongmei.beauty.booking.view.BeautyNoTechnicianTrades;
import com.zhongmei.beauty.booking.interfaces.IHVScrollListener;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.beauty.dialog.BeautyCreateOrEditBookingDialog;
import com.zhongmei.beauty.entity.ReserverItemVo;
import com.zhongmei.beauty.order.view.ReserverView;
import com.zhongmei.beauty.view.BeautyBookingTimeShaft;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.beauty.operates.message.BeautyBookingResp;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarDialog;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarView;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class BeautyReserverBoardFragment extends BasicFragment implements IHVScrollListener, View.OnClickListener, RadioButton.OnCheckedChangeListener, BeautyBoardDataListener, BeautyBookingTradeControlListener, BeautyCreateOrEditBookingDialog.OnBookingListener {
    private final String TAG = BeautyReserverBoardFragment.class.getSimpleName();
    private View rootView;

    private ReserverView mReserverView;
    private RadioGroup rg_date;
    private LinearLayout layout_timeShaft;
    private LinearLayout layout_technicianShaft;
    private BeautyBookingTimeShaft custom_BookingTimeShaft;
    private TextView tv_noTechnicianTradeNumber;
    private LinearLayout layout_allotTechnician;
    private BeautyNoTechnicianTrades custom_notechnicianTrades;

    private RadioButton rb_today;
    private RadioButton rb_tomorrow;
    private RadioButton rb_afterTomorrow;
    private RadioButton rb_customDate;
    private TextView tv_customDate;

    private RecyclerView lv_technicians;

    private CalendarDialog calendarDialog;
    private Date selectedDate;

    private BeautyBoardManager mBeautyManager;
    private BeautyBookingTechnicianAdapter mBoardTechnicianAdapter;
    private List<User> mTechnicians;

    private Float itemTiem = 30 * 60 * 1000f;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.beauty_reserver_board_fragment, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mReserverView = (ReserverView) view.findViewById(R.id.reserverview);
        layout_timeShaft = (LinearLayout) view.findViewById(R.id.layout_time_shaft);
        layout_technicianShaft = (LinearLayout) view.findViewById(R.id.layout_technician_shaft);
        custom_BookingTimeShaft = (BeautyBookingTimeShaft) view.findViewById(R.id.custom_booking_time_shaft);
        rg_date = (RadioGroup) view.findViewById(R.id.rg_date);
        layout_allotTechnician = (LinearLayout) view.findViewById(R.id.layout_allot_technician);
        tv_noTechnicianTradeNumber = (TextView) view.findViewById(R.id.tv_trades_number);
        custom_notechnicianTrades = (BeautyNoTechnicianTrades) view.findViewById(R.id.custom_notechnician_trades);
        rb_today = (RadioButton) view.findViewById(R.id.rb_today);
        rb_tomorrow = (RadioButton) view.findViewById(R.id.rb_tomorrow);
        rb_afterTomorrow = (RadioButton) view.findViewById(R.id.rb_after_tomorrow);
        rb_customDate = (RadioButton) view.findViewById(R.id.rb_custom_date);
        tv_customDate = (TextView) view.findViewById(R.id.tv_custom_date);
        lv_technicians = (RecyclerView) view.findViewById(R.id.lv_technicians);

        LinearLayoutManager manager = new RecyclerLinearLayoutManager(getContext());
        lv_technicians.setLayoutManager(manager);
        lv_technicians.setNestedScrollingEnabled(false);

        rg_date.check(R.id.rb_today);
        mReserverView.setHVScrollListener(this);
        mReserverView.setControlListener(this);
        layout_allotTechnician.setOnClickListener(this);
        rb_today.setOnCheckedChangeListener(this);
        rb_tomorrow.setOnCheckedChangeListener(this);
        rb_afterTomorrow.setOnCheckedChangeListener(this);
        rb_customDate.setOnCheckedChangeListener(this);
        rb_customDate.setOnClickListener(this);
        custom_notechnicianTrades.setControlListener(this);
        tv_customDate.setOnClickListener(this);

        custom_notechnicianTrades.setEnabled(false);
    }

    private void initData() {
        selectedDate = new Date();
        mBeautyManager = new BeautyBoardManager();

        refreshBusinessTime();

        mTechnicians = Session.getFunc(UserFunc.class).getUserByIdentity(TradeUserType.TECHNICIAN.value());
        mBoardTechnicianAdapter = new BeautyBookingTechnicianAdapter(getContext());
        mBoardTechnicianAdapter.setItems(mTechnicians);
        lv_technicians.setAdapter(mBoardTechnicianAdapter);

        mReserverView.initReserverView();

        requestBookindTrade(selectedDate);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mReserverView != null) {
            mReserverView.onSizeChange(custom_BookingTimeShaft.getactualHeight(),getTechniciansHeight(mTechnicians));
        }
    }


    private int getTechniciansHeight(List<User> technicians){
        if(Utils.isNotEmpty(technicians)){
            return technicians.size()* DensityUtil.dip2px(getContext(),50);
        }
        return 0;
    }

    private void loadReserverData(List<User> technicians, Map<Long, ArrayList<ReserverItemVo>> mapReserverItemVos) {
                List<ReserverItemVo> listItemVo = new ArrayList<>();

        if (technicians == null) {
            return;
        }

        for (int i = 0; i < technicians.size(); i++) {
            User technician = technicians.get(i);
            List<ReserverItemVo> reserverItemVos = mapReserverItemVos.get(technician.getId());
            if (Utils.isNotEmpty(reserverItemVos)) {
                listItemVo.addAll(calculateReserverItemPosition(i, reserverItemVos));
            }
        }

        if (mReserverView != null) {
            mReserverView.refreshReserverData(listItemVo);
        }
    }


    private List<ReserverItemVo> calculateReserverItemPosition(int technicianIndex, List<ReserverItemVo> reserverItemVos) {
        Float itemWidth = getResources().getDimension(R.dimen.beauty_reserver_board_item_width);
        Float itemHeight = getResources().getDimension(R.dimen.beauty_reserver_board_item_height);
        Long startTime = mBeautyManager.getStartBusinessType(selectedDate);
        for (ReserverItemVo reserverItemVo : reserverItemVos) {
            Long bookingStartTime = reserverItemVo.getmReserverVo().getBooking().getStartTime();
            long bookingEndTiime = reserverItemVo.getmReserverVo().getBooking().getEndTime();


            Float height = itemHeight;
            Float width = (bookingEndTiime - bookingStartTime) / itemTiem * itemWidth;

            Float offsetX = Math.round((bookingStartTime - startTime) / itemTiem) * itemWidth;
            Float offsetY = technicianIndex * itemHeight-technicianIndex;
            reserverItemVo.setHeight(height);
            reserverItemVo.setWidth(width);
            reserverItemVo.setOffsetX(offsetX);
            reserverItemVo.setOffserY(offsetY);
        }

        return reserverItemVos;
    }


    private void showCalendarDialog() {
        calendarDialog = new CalendarDialog(getActivity(), listner);
        calendarDialog.setDefaultSelected(selectedDate);
        calendarDialog.showDialog();
    }

    CalendarView.OnItemClickListener listner = new CalendarView.OnItemClickListener() {

        @Override
        public void OnItemClick(Date downDate) {
            selectedDate = downDate;
            setCustomDate(downDate);
            calendarDialog.dismiss();
            requestBookindTrade(selectedDate);
        }
    };

    private void setCustomDate(Date date) {
        if (date == null) {
            String custom = getResources().getString(R.string.beauty_custom);
            tv_customDate.setText("(" + custom + ")");
            return;
        }

        String currentTime = DateTimeUtils.formatDate(date);
        tv_customDate.setText("(" + currentTime + ")");

    }

    @Override
    public void scroll(int scrollX, int scrollY) {
        if (layout_timeShaft != null) {
            layout_timeShaft.scrollTo(scrollX, 0);
        }

        if (layout_technicianShaft != null) {
            layout_technicianShaft.scrollTo(0, scrollY);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == layout_allotTechnician) {
                        if (custom_notechnicianTrades.getVisibility() == View.GONE) {
                showNoTechnicianPop(true);
            }
        } else if (v == rb_customDate) {
            showCalendarDialog();
        } else if (v == tv_customDate) {
            rb_customDate.setChecked(true);
            showCalendarDialog();
        }
    }

    private void showNoTechnicianPop(boolean isShow) {
        if (isShow) {
            custom_notechnicianTrades.setVisibility(View.VISIBLE);
            Animation animator = AnimationUtils.loadAnimation(getContext(), R.anim.anim_right_enter_point);
            custom_notechnicianTrades.startAnimation(animator);
        } else {
            custom_notechnicianTrades.setVisibility(View.GONE);
            Animation animator = AnimationUtils.loadAnimation(getContext(), R.anim.anim_right_exit_point);
            custom_notechnicianTrades.startAnimation(animator);
        }

    }

    private void requestBookindTrade(Date date) {
        mBeautyManager.getUnServiceList(getActivity(), this, date);
    }


    private Date getDate(int afterDay) {
        Long curTime = System.currentTimeMillis();
        Long tarTime = curTime + (afterDay * 24 * 60 * 60 * 1000);
        return new Date(tarTime);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }

        if (buttonView == rb_today) {            selectedDate = getDate(0);
        } else if (buttonView == rb_tomorrow) {            selectedDate = getDate(1);
        } else if (buttonView == rb_afterTomorrow) {            selectedDate = getDate(2);
        } else if (buttonView == rb_customDate) {                        setCustomDate(selectedDate);
            return;
        }

                setCustomDate(null);
        requestBookindTrade(selectedDate);
    }


    private void refreshBusinessTime() {
        Long startTime = mBeautyManager.getStartBusinessType(selectedDate);
        Long stopTme = mBeautyManager.getStopBusinessType(selectedDate);
        custom_BookingTimeShaft.setBusinessTime(startTime, stopTme);
    }


    @Override
    public void onSuccess(BeautyBookingBoardVo boardVo) {
        List<BeautyBookingVo> mReserverTradeVos = boardVo.getmNoTechnicianBookingItemVos();
        if (Utils.isEmpty(mReserverTradeVos)) {
            tv_noTechnicianTradeNumber.setText("0");
            layout_allotTechnician.setEnabled(false);
            custom_notechnicianTrades.setVisibility(View.GONE);
        } else {
            tv_noTechnicianTradeNumber.setText(mReserverTradeVos.size() + "");
            layout_allotTechnician.setEnabled(true);
        }
        custom_notechnicianTrades.refreshData(boardVo.getmNoTechnicianBookingItemVos());
        loadReserverData(mTechnicians, boardVo.getmMapBookingItemVos());
    }

    @Override
    public void onFail(String error) {
        layout_allotTechnician.setEnabled(true);
        ToastUtil.showShortToast("获取数据失败！");
    }


    @Override
    public void onCancelListener(@NotNull Booking booking) {
        requestBookindTrade(selectedDate);
    }

    @Override
    public void onCreateListener(@NotNull BeautyBookingResp resp) {
        requestBookindTrade(selectedDate);
    }

    @Override
    public void editTrade(BeautyBookingVo reserverTradeVo) {
        BeautyCreateOrEditBookingDialog dialog = new BeautyCreateOrEditBookingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(BeautyBookingEnum.LAUNCHMODE_BOOKING_DIALOG, BeautyBookingEnum.BookingDialogLaunchMode.EDIT);
        bundle.putSerializable(BeautyBookingEnum.DATA_BOOKING_DIALOG, reserverTradeVo);
        dialog.setArguments(bundle);
        dialog.setOnBookingListener(this);
        dialog.show(getChildFragmentManager(), "BeautyCreateOrEditBookingDialog");
    }

    @Override
    public void onEditListener(@NotNull BeautyBookingVo booking, @NotNull BeautyBookingResp resp) {
        requestBookindTrade(selectedDate);
    }
}
