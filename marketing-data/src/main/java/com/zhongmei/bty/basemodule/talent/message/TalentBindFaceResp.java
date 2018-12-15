package com.zhongmei.bty.basemodule.talent.message;

import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountEntity;

public class TalentBindFaceResp {

    private AuthAccountEntity content;

    private String message;

    private String messageId;

    private Integer status;

    public AuthAccountEntity getContent() {
        return content;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public Integer getStatus() {
        return status;
    }
}
