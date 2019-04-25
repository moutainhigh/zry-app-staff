package com.zhongmei.beauty;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhongmei.beauty.booking.bean.BeautyBookingBoardVo;
import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.beauty.booking.constants.BeautyBookingEnum;
import com.zhongmei.beauty.booking.interfaces.BeautyBoardDataListener;
import com.zhongmei.beauty.booking.list.manager.BeautyBoardManager;
import com.zhongmei.beauty.booking.list.manager.BeautyOpenTradeManager;
import com.zhongmei.beauty.dialog.BeautyCreateOrEditBookingDialog;
import com.zhongmei.beauty.operates.message.BeautyBookingResp;
import com.zhongmei.yunfu.R;
import com.zhongmei.beauty.adapter.TodayReserverAdapter;
import com.zhongmei.beauty.adapter.TodayReserverItemView;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.booking.Booking;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.beauty_main_reserver_fragment)
public class BeautyMainReserverFragment extends BasicFragment implements TodayReserverItemView.OnOperateListener, BeautyBoardDataListener {

    @ViewById(R.id.lv_reservers)
    protected RecyclerView mReserverListView;

    @ViewById(R.id.iv_empty_view)
    public View iv_emptyView;

    @Bean
    protected TodayReserverAdapter mReserverAdapter;

    private BeautyBoardManager mBeautyManager;

    @AfterViews
    protected void init() {
        LinearLayoutManager manager = new RecyclerLinearLayoutManager(getActivity());
        mReserverListView.setLayoutManager(manager);

        mReserverAdapter.setOperateListener(this);
        mReserverListView.setAdapter(mReserverAdapter);

        mBeautyManager = new BeautyBoardManager();

        initBookingTrade();//初始化预约订单数据

    }

    /**
     * 初始化预约订单
     */
    private void initBookingTrade() {
        mBeautyManager.getTodayUnServiceList(getActivity(), this, new Date());
    }


    /**
     * 改单
     *
     * @param mReserver
     */
    @Override
    public void reserverModify(BeautyBookingVo mReserver) {
        BeautyCreateOrEditBookingDialog dialog = new BeautyCreateOrEditBookingDialog();
        dialog.setOnBookingListener(new BeautyCreateOrEditBookingDialog.OnBookingListener() {
            @Override
            public void onCreateListener(@NotNull BeautyBookingResp resp) {

            }

            @Override
            public void onEditListener(@NotNull BeautyBookingVo booking, @NotNull BeautyBookingResp resp) {
                //开单成功之后,重新拉去数据
                mBeautyManager.getTodayUnServiceList(getActivity(), BeautyMainReserverFragment.this, new Date());
            }

            @Override
            public void onCancelListener(@NotNull Booking booking) {
                //开单成功之后,重新拉去数据
                mBeautyManager.getTodayUnServiceList(getActivity(), BeautyMainReserverFragment.this, new Date());
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt(BeautyBookingEnum.LAUNCHMODE_BOOKING_DIALOG, BeautyBookingEnum.BookingDialogLaunchMode.EDIT); // 编辑 CREATE创建
        bundle.putSerializable(BeautyBookingEnum.DATA_BOOKING_DIALOG, mReserver);
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "showCreateBookingDialog");
    }

    /**
     * 开单
     *
     * @param mReserver
     */
    @Override
    public void reserverCreateTrade(BeautyBookingVo mReserver) {
        BeautyOpenTradeManager manager = new BeautyOpenTradeManager();
        manager.openTrade(getActivity(), mReserver, new BeautyOpenTradeManager.OpenTradeCallBack() {
            @Override
            public void onOpenTradeSuccess() {
                //开单成功之后,重新拉去数据
                mBeautyManager.getTodayUnServiceList(getActivity(), BeautyMainReserverFragment.this, new Date());
            }
        });
    }

    @Override
    public void onSuccess(BeautyBookingBoardVo boardVo) {
        List<BeautyBookingVo> mReserverTradeVos = boardVo.getmNoTechnicianBookingItemVos();
        if (Utils.isEmpty(mReserverTradeVos)) {
            iv_emptyView.setVisibility(View.VISIBLE);
            mReserverListView.setVisibility(View.GONE);
        } else {
            iv_emptyView.setVisibility(View.GONE);
            mReserverListView.setVisibility(View.VISIBLE);
            mReserverAdapter.setItems(mReserverTradeVos);
            mReserverAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFail(String error) {

    }
}
