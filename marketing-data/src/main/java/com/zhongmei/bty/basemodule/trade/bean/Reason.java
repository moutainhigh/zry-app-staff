package com.zhongmei.bty.basemodule.trade.bean;



public class Reason {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String content;

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
