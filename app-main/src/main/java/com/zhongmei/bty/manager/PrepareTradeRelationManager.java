package com.zhongmei.bty.manager;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.zhongmei.bty.basemodule.booking.bean.BookingVo;
import com.zhongmei.bty.basemodule.booking.message.BookingResp;
import com.zhongmei.bty.basemodule.booking.operates.BookingDal;
import com.zhongmei.bty.basemodule.booking.operates.BookingOperates;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PrepareTradeRelation;
import com.zhongmei.bty.basemodule.commonbusiness.operates.PrepareTradeRelationDal;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.db.enums.BookingOrderStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class PrepareTradeRelationManager {
    private final static String TAG = PrepareTradeRelationManager.class.getSimpleName();

    private PrepareTradeRelationDal prepareTradeRelationDal = OperatesFactory.create(PrepareTradeRelationDal.class);
    private TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
    private BookingDal bookingDal = OperatesFactory.create(BookingDal.class);
    private Map<Long, PrepareTradeRelation> prepareMap;

    private static PrepareTradeRelationManager instance = new PrepareTradeRelationManager();


    private PrepareTradeRelationManager() {
        EventBus.getDefault().register(this);

        initBookingData();
    }


    public static PrepareTradeRelationManager getInstance() {

        if (instance == null) {

            instance = new PrepareTradeRelationManager();
        }
        return instance;
    }



    public void doArrival(List<TradeVo> listTradeVo, FragmentManager manager) {
        if (listTradeVo != null && listTradeVo.size() > 0) {
            if (prepareMap == null) {
                return;
            }

            for (TradeVo tradeVo : listTradeVo) {

                if (prepareMap.containsKey(tradeVo.getTrade().getId())) {

                    Long bookingid = prepareMap.get(tradeVo.getTrade().getId()).getRelatedId();

                    if (bookingid == null) {
                        return;
                    }

                    List<Long> tradeIdList = new ArrayList<>();
                    Collection<PrepareTradeRelation> preprarTableList = prepareMap.values();
                    for (PrepareTradeRelation pre : preprarTableList) {

                        if (pre.getRelatedId().equals(bookingid)) {

                            tradeIdList.add(pre.getTradeId());
                        }
                    }

                    if (tradeIdList.size() > 0) {
                        boolean isAllFinish = true;
                        for (Long id : tradeIdList) {
                            TradeVo vo = getTradeVoByTradeId(id);

                            if (vo != null && vo.getTrade().getTradeStatus() != TradeStatus.FINISH && vo.getTrade().getTradeStatus() != TradeStatus.CREDIT) {
                                isAllFinish = false;
                                break;
                            }
                        }
                        if (!isAllFinish) {
                            return;
                        }
                    }

                    updateBooking(getBookingVoByTradeId(bookingid), manager);
                }
            }
        }
    }

    private TradeVo getTradeVoByTradeId(long tradeid) {
        TradeVo tradeVo = null;
        try {
            tradeVo = tradeDal.findTrade(tradeid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tradeVo;
    }


    private BookingVo getBookingVoByTradeId(long bookingid) {

        BookingVo bookingVo = null;
        try {
            bookingVo = bookingDal.findBookingVoByid(bookingid);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (bookingVo == null) {
            bookingVo = new BookingVo();
            bookingVo.getBooking().setId(bookingid);
        }

        Date now = new Date();
        bookingVo.getBooking().setOrderStatus(BookingOrderStatus.LEAVE);

        return bookingVo;
    }

    private void updateBooking(BookingVo bookingVo, FragmentManager manager) {

        BookingOperates operates = OperatesFactory.create(BookingOperates.class);

        ResponseListener<BookingResp> listener = new ResponseListener<BookingResp>() {

            @Override
            public void onResponse(ResponseObject<BookingResp> response) {
                if (ResponseObject.isOk(response)) {
                    Log.d(TAG, "修改预定状态为已离店成功");
                } else {
                    Log.d(TAG, "修改预定状态为已离店失败 erro：" + response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.d(TAG, "修改预定状态为已离店失败 erro：" + error.getMessage());
            }

        };
        operates.insertOrModify(bookingVo, LoadingResponseListener.ensure(listener, manager));
    }






    private void initBookingData() {

        try {
            prepareMap = prepareTradeRelationDal.findMapByTradeId(PrepareTradeRelationDal.TYPE_VALUE_BOOKING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
