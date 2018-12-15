package com.zhongmei.bty.basemodule.talent.message;

import java.util.List;

public class AttendanceSearchResp {
    public int status;// 是 返回状态码 0：成功
    public String message;// 是 响应中文信息
    public String messageId;//  是 UUID
    public List<AttendanceInfo> data;
}
