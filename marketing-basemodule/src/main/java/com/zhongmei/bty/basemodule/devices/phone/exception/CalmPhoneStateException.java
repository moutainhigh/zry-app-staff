package com.zhongmei.bty.basemodule.devices.phone.exception;

/**
 * 跟电话机状态有关的异常
 *
 * @date 2014-8-5
 */
public class CalmPhoneStateException extends CalmPhoneException {

    private static final long serialVersionUID = 1L;

    /**
     * 发生异常时的当前状态
     */
    private int mCurrentState;

    public CalmPhoneStateException(int mCurrentState) {
        this.mCurrentState = mCurrentState;
    }

    public int getmCurrentState() {
        return mCurrentState;
    }


}
