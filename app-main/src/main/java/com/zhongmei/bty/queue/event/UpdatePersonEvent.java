package com.zhongmei.bty.queue.event;

/**
 * 人数改变时调用
 *
 * @date:2015年9月1日上午11:41:20
 */
public class UpdatePersonEvent {
    public final int personCount;

    public UpdatePersonEvent(int personCount) {
        this.personCount = personCount;
    }

}
