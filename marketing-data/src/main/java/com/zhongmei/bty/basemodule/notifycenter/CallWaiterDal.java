package com.zhongmei.bty.basemodule.notifycenter;

import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.notifycenter.entity.CallWaiter;
import com.zhongmei.bty.basemodule.notifycenter.enums.BizStatus;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public interface CallWaiterDal extends IOperates {

    /**
     * 根据状态查询服务铃列表
     *
     * @param status
     * @return
     * @throws SQLException
     */
    List<CallWaiter> getCallWaiterList(BizStatus status) throws SQLException, ParseException;

    /**
     * 根据状态查询服务铃总数
     *
     * @param status
     * @return
     * @throws SQLException
     */
    long getCallWaiterCount(BizStatus status) throws SQLException, ParseException;

    /**
     * 更改服务铃
     *
     * @param callWaiter
     * @param listener
     */
    void updateCallWaiter(CallWaiter callWaiter, ResponseListener<CallWaiter> listener);

}
