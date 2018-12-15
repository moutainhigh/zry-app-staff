package com.zhongmei.bty.basemodule.talent.message;

//服务器考勤数据
public class ServerAttendanceData {
    /* "id": "122040295515277312",
           "accountId": "100548710299337728",
           "employeeId": "100548710425114624",
           "employeeName": "吴鹏",
           "localPunchTime": "2018-08-03 18:24:32",
           "serverPunchTime": "2018-08-03 18:24:33",
           "deviceId": "0",
           "brandIdenty": "32315",
           "shopIdenty": "810094497",
           "punchDate": "2018-08-03 00:00:00",
           "creatorId": "100548710425114624",
           "serverCreateTime": "2018-08-03 18:24:33",
           "updatorId": "100548710425114624",
           "serverUpdateTime": "2018-08-03 18:24:33",
           "faceId": "jidjfsdifjisfjsdfjidsjfidsjfdjfldsjfkjsfknjfkjsfkjsdfkljsdfklsjdffdjklfjfj",
           "source": 10,
           "macAddress": "28:c2:dd:1c:24:67"*/
    public String id;
    public String accountId;
    public String employeeId;
    public String employeeName;
    public String localPunchTime;
    public String serverPunchTime;
    public String deviceId;
    public String brandIdenty;
    public String shopIdenty;
    public String punchDate;
    public String serverCreateTime;
    public String updatorId;
    public String serverUpdateTime;
    public String faceId;
    public Long source;// 服务器打卡时间
    public Long macAddress;// 服务器创建时间

}
