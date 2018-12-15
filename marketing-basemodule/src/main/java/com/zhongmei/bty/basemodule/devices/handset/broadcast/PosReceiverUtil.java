//package com.zhongmei.bty.basemodule.devices.handset.broadcast;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.util.Log;
//
///**
// * Created by demo on 2018/12/15
// */
//public class PosReceiverUtil implements BaseBroadCast.ReceiveBoard{
//    private final String TAG = "PosReceiverUtil";
//
//    private BaseBroadCast mBroadCast;
//
//    private IntentFilter mFilter;
//
//    private Context mContext;
//
////    private LocalBroadcastManager mLocalBroadcastManager = null;
//
//    private OnPosReceiver mOnPosReceiver;
//
//    public void setOnPosReceiverListener(OnPosReceiver onPosReceiver){
//        this.mOnPosReceiver = onPosReceiver;
//    }
//
//    public interface OnPosReceiver{
//        void onConnect(String clientAddress);
//        void readBraceletInfo(String json);
//        void readPassword(String json);
//    }
//
//    public PosReceiverUtil(Context context){
//        this.mContext = context;
////        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
//    }
//
//    public void regiestPosReceiver(){
//        this.registerMyReceiver(BroadCastConstants.CONNECT , BroadCastConstants.PASSWORD , BroadCastConstants.GETBRACELETID);
//    }
//
//    public void unregiestPosReceiver(){
//        this.unregisterMyReceiver();
//    }
//
//
//    /**
//     * 添加注册广播
//     *
//     * @param actions
//     *            广播
//     */
//    private void registerMyReceiver(String... actions) {
//        // 更新的广播创建
//        if (mBroadCast == null){
//            mBroadCast = new BaseBroadCast(this);
//        }
//        if (mFilter == null){
//            mFilter = new IntentFilter();
//        }
//        // 循环添加
//        for (String action : actions){
//            mFilter.addAction(action);
//        }
//        mContext.registerReceiver(mBroadCast , mFilter);
////        mLocalBroadcastManager.registerReceiver(mBroadCast, mFilter);
//    }
//
//    protected void unregisterMyReceiver() {
//        if (mBroadCast != null) {
//            mContext.unregisterReceiver(mBroadCast);
//        }
//        if (mFilter != null) {
//            mFilter = null;
//        }
//    }
//
//    @Override
//    public void onReceive(Intent intent) {
//        if (mOnPosReceiver ==null){
//            return;
//        }
//        String msg;
//        if (intent.getAction().equals(BroadCastConstants.CONNECT)){
//            msg = intent.getStringExtra(BroadCastConstants.KEY_CLIENT_IP_ADDRESS);
//            Log.i(TAG , "Broadcast Receiver client msg  ->  clientIpAddress ： " + msg);
//            mOnPosReceiver.onConnect(msg);
//        } else if (intent.getAction().equals(BroadCastConstants.PASSWORD)){
//            msg = intent.getStringExtra(BroadCastConstants.KEY_CLIENT_RECEIVE_PASSWORD);
//            Log.i(TAG , "Broadcast Receiver client msg  ->  password ： " + msg);
//            mOnPosReceiver.readPassword(msg);
//        } else if (intent.getAction().equals(BroadCastConstants.GETBRACELETID)){
//            msg = intent.getStringExtra(BroadCastConstants.KEY_CLIENT_RECEIVE_BRACELET_INFO);
//            Log.i(TAG , "Broadcast Receiver client msg  ->  braceletInfo ： " + msg);
//            mOnPosReceiver.readBraceletInfo(msg);
//        }
//    }
//}
