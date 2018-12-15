package com.zhongmei.bty.basemodule.trade.bean;

/**
 * Created by demo on 2018/12/15
 */

public class Reason {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String content;

    // v8.12.0 口碑拒绝理由码
    private String contentCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentCode() {
        return contentCode;
    }

    public void setContentCode(String contentCode) {
        this.contentCode = contentCode;
    }
}
