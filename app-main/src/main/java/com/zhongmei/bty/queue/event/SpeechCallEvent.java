package com.zhongmei.bty.queue.event;

import android.view.View;

import com.zhongmei.bty.basemodule.database.queue.Queue;

/**
 *

 *
 */
public class SpeechCallEvent {
    public final String num;

    public final View view;

    public final Queue queue;

    public SpeechCallEvent(String num, View view, Queue queue) {
        this.num = num;
        this.view = view;
        this.queue = queue;

    }

}
