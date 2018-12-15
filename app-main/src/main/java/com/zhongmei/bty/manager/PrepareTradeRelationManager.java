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

/**
 * 主要处理通过本预定开台的业务，在订单状态变为完成的时候更改此预定的预定状态为已离店
 */
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


    /**
     * 修改会员状态为已离店
     *
     * @param listTradeVo
     * @param manager
     */
    public void doArrival(List<TradeVo> listTradeVo, FragmentManager manager) {
        if (listTradeVo != null && listTradeVo.size() > 0) {
//            /*
//            获取预定订单匹配表里的数据
//             */
////            Map<Long, PrepareTradeRelation> prepareMap = null;
//            try {
//                prepareMap =  prepareTradeRelationDal.findMapByTradeId( PrepareTradeRelationDal.TYPE_VALUE_BOOKING);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            if (prepareMap == null) {
                return;
            }

            for (TradeVo tradeVo : listTradeVo) {
                /*
                   判断是否匹配表里有这个订单，如果有，代表是从预定或者排队开台的
                   从匹配表里找出这个订单对应的预定id，根据预定id找到这个预定所有的开台订单
                   循环判断这些订单状态，如果所有订单都是已完成状态，则更改这个预定的状态为已离店
                */
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

        /**
         * 为了以防有此预定而本地数据库没有同步过来
         */
        if (bookingVo == null) {
            bookingVo = new BookingVo();
            bookingVo.getBooking().setId(bookingid);
        }

        Date now = new Date();
//        bookingVo.getBooking().setShopLeaveTime(now.getTime());
//        bookingVo.getBooking().setShopLeaveUser(Session.getAuthUser().getName());
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


    /**
     * 接收预定表变动的消息
     *
     * @param event
     */
    /*public void onEventBackgroundThread(BookingChangeEvent event) {

        if (event != null) {
            initBookingData();
        }
    }*/

    /**
     * 获取预定订单匹配表里的数据
     */
    private void initBookingData() {

        try {
            prepareMap = prepareTradeRelationDal.findMapByTradeId(PrepareTradeRelationDal.TYPE_VALUE_BOOKING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
