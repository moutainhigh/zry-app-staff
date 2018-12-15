package com.zhongmei.bty.queue.event;

/**
 * @Date： 2018/7/5
 * @Description:
 * @Version: 1.0
 */
public class TableSelectEvent {
    public Integer minPersonCount;
    public Integer maxPersonCount;
    public String name;

    public TableSelectEvent(Integer minPersonCount, Integer maxPersonCount) {
        this.minPersonCount = minPersonCount;
        this.maxPersonCount = maxPersonCount;
    }

    public void setName(String name) {
        this.name = name;
    }
}
