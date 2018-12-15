package com.zhongmei.bty.commonmodule.database.entity.base;

import com.zhongmei.yunfu.db.DataEntityBase;

/**
 * Created by demo on 2018/12/15
 * <p>
 * 该类主要是由于目前很多表设计的不规范，主要体现在ID\UUID处理上
 * 由于异步必须依赖于UUID，所以对于没有UUID的表，就用ID的toString
 * 作为其UUID.
 * 目前只是用在DBHelperManager里面对这一类表统一进行处理(只为方便类型判断)
 */
public class OptionEntityBase extends DataEntityBase {

    protected interface $ extends DataEntityBase.$ {

    }

}
