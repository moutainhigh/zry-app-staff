package com.zhongmei.bty.basemodule.devices.phone;

import com.zhongmei.bty.basemodule.devices.phone.entity.CallHistory;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public interface CallService extends CallDBHelper.Service {

    void insert(CallHistory callHistory);

    int countUnreadNoAnswer();

    void clearUnreadNoAnswer();

    void deleteTimeOutLog();

    List<CallHistory> listNoAnswer();

    List<CallHistory> list();

    List<CallHistory> list(String phoneNum);

    List<CallHistory> list(long pageNum, long pageSize);

    List<CallHistory> list(long pageNum, long pageSize, String phoneNum);

    List<CallHistory> list(long pageNum, long pageSize, String phoneNum, int status);
}
