package com.zhongmei.bty.mobilepay;

import java.io.Serializable;

/**
 * Created by demo on 2018/12/15
 */

public interface IDoLag extends Serializable {

    public void lagOver(int payType);//挂账成功调用

}
