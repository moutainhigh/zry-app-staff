package com.zhongmei.bty.offline.backup.core.common;

import com.zhongmei.bty.offline.backup.backup4qs.IQSBackup;


public final class Entity {

    public static final int STATE_FREE = 0;
    public static final int STATE_WAITING = 1;
    public static final int STATE_STORE = 2;
    public static final int STATE_SUCCESS = 3;
    public static final int STATE_ERROR = 4;

    private long id;
    private String identity;
    private int state;
    private long modifyTime;

    private String content;

    public static Entity newInstance(String identity) {
        return new Entity(identity);
    }

    private Entity(String identity) {
        this.identity = identity;
        this.state = STATE_FREE;
        modifyTime();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        modifyTime();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getIdentity() {
        return identity;
    }

    private void modifyTime() {
        if (state == STATE_FREE || state == STATE_WAITING) {
            this.modifyTime = ct();
        }
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        switch (state) {
            case STATE_FREE:
            case STATE_WAITING:
            case STATE_STORE:
            case STATE_SUCCESS:
            case STATE_ERROR:
                this.state = state;
                modifyTime();
                break;
            default:
                throw new IllegalArgumentException("Invalid state value");
        }
    }

    private static long ct() {
        return System.currentTimeMillis();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("identity:" + identity);
        sb.append("  ");
        sb.append("state:" + state);
        sb.append("  ");
        sb.append("modifyTime:" + modifyTime);
        sb.append("  ");
        sb.append("content:" + content.toString());
        return sb.toString();
    }
}
