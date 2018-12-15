package com.zhongmei.bty.basemodule.talent.impl;

import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.session.ver.v2.db.AuthAccountEntity;
import com.zhongmei.bty.basemodule.talent.message.AttendanceReq;
import com.zhongmei.bty.basemodule.talent.message.AttendanceResp;
import com.zhongmei.bty.basemodule.talent.message.AttendanceSearchReq;
import com.zhongmei.bty.basemodule.talent.message.AttendanceSearchResp;
import com.zhongmei.bty.basemodule.talent.message.TalentBindFaceReq;
import com.zhongmei.bty.basemodule.talent.message.TalentBindFaceResp;
import com.zhongmei.bty.basemodule.talent.operates.TalentOperates;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.Map;

public class TalentOperatesImpl extends AbstractOpeartesImpl implements TalentOperates {

    public TalentOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void bindFace(Long accountId, String faceCode, ResponseListener<TalentBindFaceResp> listener) {
        String transferUrl = ServerAddressUtil.getInstance().psAccountTransfer();

        String url = ServerAddressUtil.getInstance().talentBindFace();
        final TalentBindFaceReq req = new TalentBindFaceReq();
        req.setAccountId(accountId);
        req.setFaceCode(faceCode);
        req.setShopID(Utils.toLong(ShopInfoCfg.getInstance().shopId));
        TransferReq<TalentBindFaceReq> transferReq = new TransferReq<>();
        transferReq.setPostData(req);
        transferReq.setUrl(url);
        OpsRequest.Executor<TransferReq<TalentBindFaceReq>, TalentBindFaceResp> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq)
                .responseClass(TalentBindFaceResp.class)
                .responseProcessor(new OpsRequest.DatabaseResponseProcessor<TalentBindFaceResp>() {
                    @Override
                    protected void transactionCallable(DatabaseHelper helper, TalentBindFaceResp resp) throws Exception {
                        if (resp != null && resp.getContent() != null) {
                            DBHelperManager.saveEntities(helper, AuthAccountEntity.class, resp.getContent());
                        }
                    }
                })
                .execute(listener, "bind_face");
    }

    @Override
    public void addAttendance(AttendanceReq req, ResponseListener<AttendanceResp> listener) {
        String transferUrl = ServerAddressUtil.getInstance().talentTransfer();
        String url = ServerAddressUtil.getInstance().talentAddAttendanceUrl();
        TransferReq<AttendanceReq> transferReq = new TransferReq<>();
        transferReq.setPostData(req);
        transferReq.setUrl(url);
        OpsRequest.Executor<TransferReq<AttendanceReq>, AttendanceResp> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq).responseClass(AttendanceResp.class).execute(listener, "addAttendance");
    }

    /**
     * 查询员工考勤
     */
    @Override
    public void searAttendance(Map<String, String> params, ResponseListener<AttendanceSearchResp> listener) {
        String transferUrl = ServerAddressUtil.getInstance().talentTransfer();
        String url = ServerAddressUtil.getInstance().talentSearchAttendanceUrl();
        TransferReq<AttendanceSearchReq> transferReq = new TransferReq<>();
//        transferReq.setPostData(req);
        transferReq.setUrl(Utils.createGetUrl(url, params));
        transferReq.setMethod("GET");
        OpsRequest.Executor<TransferReq<AttendanceSearchReq>, AttendanceSearchResp> executor = OpsRequest.Executor.create(transferUrl);
        executor.requestValue(transferReq).responseClass(AttendanceSearchResp.class).execute(listener, "searAttendance_face");
    }
}
