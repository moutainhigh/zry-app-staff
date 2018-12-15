package com.zhongmei.bty.basemodule.talent.message;

//提交考勤接口返回数据
public class AttendanceResp {
    public int status;//返回状态码 200成功
    public int code;//返回状态码 0：成功
    public String message;// 是 响应中文信息
    public ServerAttendanceData data;

    public boolean isOk() {
        return status == 200;
    }
}
