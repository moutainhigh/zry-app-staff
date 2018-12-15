//package com.zhongmei.bty.basemodule.devices.handset.broadcast;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
///**
// * Created by demo on 2018/12/15
// */
//public class BaseBroadCast extends BroadcastReceiver {
//
//    private ReceiveBoard mReceiveBoard; // 接口响应事件
//
//    public BaseBroadCast(ReceiveBoard receiveBoard) {
//        // TODO Auto-generated constructor stub
//        mReceiveBoard = receiveBoard;
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        mReceiveBoard.onReceive(intent);
//    }
//
//    /**
//     * 接受广播的接口
//     */
//    public interface ReceiveBoard {
//        void onReceive(Intent intent);
//    }
//}
