package com.zhongmei.bty.basemodule.devices.mispos.watchdata;

import android.content.Context;
import android.util.Log;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard.IReadCard;
import com.zhongmei.bty.basemodule.devices.mispos.watchdata.readcard.ReadCardResponseListener;

/**
 * Desc
 *
 * @created 2017/9/11
 */
public class OnPosInternalReadCard implements IReadCard, Runnable {

    private static final String TAG = OnPosInternalReadCard.class.getSimpleName();
    //private MemberShipCard mMemberShipCard;
    private static boolean mGobalStatus = false;
    private Thread thread;

    public OnPosInternalReadCard() {
        //this.mMemberShipCard = new MemberShipCard();
    }

    @Override
    public void connectDevice(final Context context, final ReadCardResponseListener listener, boolean isDelay) {
        Log.i(TAG, "connectDevice");
        mGobalStatus = true;
        if (listener != null) {
            listener.onActive();
            listener.onStart();
        }
        startGetMemberCardNumberTrigger();
        /*mMemberShipCard.startGetMemberCardNumber(new MemberShipCard.MemberCardNumberListener() {
            @Override
            public void onMemberCardValue(String s) {
                if ("ERROR_NFC_SEEK_CARD_TIMEOUT".equals(s)) {
                    doGetMemberCardNumberTrigger();
                    return;
                }

                Log.i(TAG, "onMemberCardValue: " + s);
                disconnectDevice();
                if (s.startsWith("ERROR")) {
                    if (listener != null) {
                        listener.onError(s, getErrMsg(context, s));
                    }
                } else {
                    if (listener != null) {
                        listener.onResponse(s);
                    }
                }
            }
        });*/
    }

    private String getErrMsg(Context context, String code) {
        NFCError nfcError = NFCError.toNFCError(code);
        if (nfcError != null) {
            return context.getString(nfcError.errorCode);
        }
        return code;
    }

    @Override
    public void disconnectDevice() {
        Log.i(TAG, "disconnectDevice");
        mGobalStatus = false;
        //mMemberShipCard.stopGetMemberCardNumber();
        stopGetMemberCardNumberTrigger();
    }

    private void startGetMemberCardNumberTrigger() {
        stopGetMemberCardNumberTrigger();
        thread = new Thread(this, "OnPosInternalReadCard");
        thread.start();
    }

    private void stopGetMemberCardNumberTrigger() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public synchronized void doGetMemberCardNumberTrigger() {
        OnPosInternalReadCard.this.notifyAll();
    }

    public synchronized void waitGetMemberCardNumberTrigger() throws InterruptedException {
        wait();
    }

    @Override
    public void run() {
        try {
            while (mGobalStatus) {
                waitGetMemberCardNumberTrigger();
                //mMemberShipCard.doGetMemberCardNumberTrigger();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    enum NFCError {
        ERROR_PSAM_CARD_DOES_NOT_EXIST(R.string.error_psam_card_does_not_exist),
        ERROR_NFC_SEEK_CARD_TIMEOUT(R.string.error_nfc_seek_card_timeout),
        ERROR_NFC_AUTHENTICATION_FAILED(R.string.error_nfc_authentication_failed),
        ERROR_NFC_READ_CARD_FAILED(R.string.error_nfc_read_card_failed),
        ERROR_NFC_BOARD_FAILED(R.string.error_nfc_board_failed),
        ERROR_CODE_UNKNOWN(R.string.error_code_unknown);

        int errorCode;

        NFCError(int errorCode) {
            this.errorCode = errorCode;
        }

        public static NFCError toNFCError(String code) {
            for (NFCError nfcError : values()) {
                if (nfcError.name().equals(code)) {
                    return nfcError;
                }
            }
            return NFCError.ERROR_CODE_UNKNOWN;
        }
    }
}
