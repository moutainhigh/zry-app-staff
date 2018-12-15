package com.zhongmei.bty.commonmodule.database.enums;

import com.zhongmei.yunfu.db.EntityBase;

/**
 * 针对设备表的特殊EntityBase:无ID,UUID,StatusFlag字段
 */
public abstract class InitSystemEntityBase extends EntityBase<Long> {
}
