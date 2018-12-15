package com.zhongmei.bty.data.operates;

import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.data.operates.message.content.TokenReq;
import com.zhongmei.bty.data.operates.message.content.TokenResp;

/**
 * Created by demo on 2018/12/15
 * 上传文件
 */
public interface UploadOperates extends IOperates {

    public void requestToken(TokenReq req, ResponseListener<TokenResp> listener);

}
