package com.zhongmei.bty.basemodule.shopmanager.shoplogo;

import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;

/**
 * Created by demo on 2018/12/15
 * 上传文件
 */
public interface ShopLogoOperates extends IOperates {

    public void requestUrl(ShopLogReq req, ResponseListener<ShopLogoResp> listener);

}
