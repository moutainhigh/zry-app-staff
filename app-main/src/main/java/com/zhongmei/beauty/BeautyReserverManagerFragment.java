package com.zhongmei.beauty;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongmei.beauty.booking.BeautyReserverBoardFragment;
import com.zhongmei.beauty.booking.BeautyCancelBookingFragment;
import com.zhongmei.beauty.booking.BeautyOuttimeFragment;
import com.zhongmei.beauty.booking.BeautyUnServiceBookFragment;
import com.zhongmei.beauty.booking.BeautyUndealBookingFragment;
import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.booking.constants.BeautyBookingEnum;
import com.zhongmei.beauty.booking.list.BeautyBookingListFragment;
import com.zhongmei.beauty.dialog.BeautyCreateOrEditBookingDialog;
import com.zhongmei.beauty.entity.BeautyNotifyEntity;
import com.zhongmei.beauty.operates.BeautyNotifyCache;
import com.zhongmei.beauty.widgets.XRadioGroup;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.beauty.operates.message.BeautyBookingResp;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.NotNull;


@EFragment(R.layout.beauty_reserver_manage_fragment)
public class BeautyReserverManagerFragment extends BasicFragment implements BeautyNotifyCache.BeautyNotifyListener,XRadioGroup.OnCheckedChangeListener{

    @ViewById(R.id.rg_reserver_status)
    protected XRadioGroup rb_reserverStatus;

    @ViewById(R.id.tv_undeal_trade_tip)
    protected TextView tv_undealBookingTradeTip;

    protected BeautyBookingListFragment listFragment;

    private BeautyReserverBoardFragment reserverBoardFragment;

    private BeautyNotifyCache mBeautyNotifyCache;

    @AfterViews
    protected void initView() {
        mBeautyNotifyCache=BeautyNotifyCache.getInstance();
        mBeautyNotifyCache.addNotifyListener(this);
        mBeautyNotifyCache.start();
        rb_reserverStatus.setOnCheckedChangeListener(this);
        rb_reserverStatus.check(R.id.rb_reserver_board);
    }



    private void toReserverBoard() {
        reserverBoardFragment = new BeautyReserverBoardFragment();
        replaceChildFragment(R.id.fl_content, reserverBoardFragment, BeautyReserverBoardFragment.class.getSimpleName());
    }


    private void toUnServicePage() {
        listFragment = new BeautyUnServiceBookFragment();
        replaceChildFragment(R.id.fl_content, listFragment, BeautyUnServiceBookFragment.class.getSimpleName());
    }


    private void toOuttimeServicePage() {
        listFragment = new BeautyOuttimeFragment();
        replaceChildFragment(R.id.fl_content, listFragment, BeautyOuttimeFragment.class.getSimpleName());
    }


    private void toCancelServicePage() {
        listFragment = new BeautyCancelBookingFragment();
        replaceChildFragment(R.id.fl_content, listFragment, BeautyCancelBookingFragment.class.getSimpleName());
    }



    private void toUnDealServicePage() {
        listFragment = new BeautyUndealBookingFragment();
        replaceChildFragment(R.id.fl_content, listFragment, BeautyUndealBookingFragment.class.getSimpleName());
    }


    @Click(R.id.btn_create_reserver)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_reserver:                VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_BEAUTY_CREATE_RESERVER, new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        showCreateBookingDialog();
                    }

                });
                break;
        }

    }

    private void showCreateBookingDialog() {
        BeautyCreateOrEditBookingDialog dialog = new BeautyCreateOrEditBookingDialog();
        dialog.setOnBookingListener(bookingListener);
        Bundle bundle = new Bundle();
        bundle.putInt(BeautyBookingEnum.LAUNCHMODE_BOOKING_DIALOG, BeautyBookingEnum.BookingDialogLaunchMode.CREATE);
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "BeautyCreateOrEditBookingDialog");
    }

    BeautyCreateOrEditBookingDialog.OnBookingListener bookingListener = new BeautyCreateOrEditBookingDialog.OnBookingListener() {

        @Override
        public void onEditListener(@NotNull BeautyBookingVo booking, @NotNull BeautyBookingResp resp) {

        }

        @Override
        public void onCancelListener(@NotNull Booking booking) {

        }

        @Override
        public void onCreateListener(@NotNull BeautyBookingResp resp) {
            if (listFragment != null && listFragment.isVisible()) {
                listFragment.onBookingSuccess(resp);
            }

            if (reserverBoardFragment != null && reserverBoardFragment.isVisible()) {
                reserverBoardFragment.onCreateListener(resp);
            }
        }
    };


    @UiThread
    @Override
    public void refreshNotifyNumbers(BeautyNotifyEntity notifyEntity) {
        if(tv_undealBookingTradeTip!=null){
            tv_undealBookingTradeTip.setVisibility(notifyEntity.getUnDealReserverNumber()<=0?View.GONE:View.VISIBLE);
            tv_undealBookingTradeTip.setText(notifyEntity.getUnDealReserverNumber()+"");
        }
    }

    @Override
    public void onCheckedChanged(XRadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_reserver_board:                toReserverBoard();
                break;
            case R.id.rb_reserver_trades:                toUnServicePage();
                break;
            case R.id.rb_reserver_outtime_trades:                toOuttimeServicePage();
                break;
            case R.id.rb_reserver_cancel_trades:                toCancelServicePage();
                break;
            case R.id.rb_reserver_unprocess:                toUnDealServicePage();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBeautyNotifyCache!=null){
            mBeautyNotifyCache.removeNotifyListener(this);
        }
    }
}
