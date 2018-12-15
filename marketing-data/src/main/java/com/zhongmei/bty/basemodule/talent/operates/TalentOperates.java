package com.zhongmei.bty.basemodule.talent.operates;

import com.zhongmei.bty.basemodule.talent.message.AttendanceReq;
import com.zhongmei.bty.basemodule.talent.message.AttendanceResp;
import com.zhongmei.bty.basemodule.talent.message.AttendanceSearchResp;
import com.zhongmei.bty.basemodule.talent.message.TalentBindFaceResp;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.Map;

public interface TalentOperates extends IOperates {

    /**
     * 员工绑定人脸
     */
    void bindFace(Long accountId, String faceCode, ResponseListener<TalentBindFaceResp> listener);

    /**
     * 提交员工考勤
     */
    void addAttendance(AttendanceReq req, ResponseListener<AttendanceResp> listener);

    /**
     * 查询员工考勤
     */
    void searAttendance(Map<String, String> params, ResponseListener<AttendanceSearchResp> listener);

}
