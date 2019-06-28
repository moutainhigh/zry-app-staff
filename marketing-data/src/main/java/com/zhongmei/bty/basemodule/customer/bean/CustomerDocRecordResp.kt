package com.zhongmei.bty.basemodule.customer.bean

import com.zhongmei.yunfu.db.entity.TaskRemind

class CustomerDocRecordResp {

    var id: Long? = null//id
    var serverCreateTime: Long = 0// 创建时间
    var serverUpdateTime: Long = 0// 创建时间
    var type: String? = null
    var title: String? = null
    var content: String? = null //档案内容
    var customerId:Long =0 //会员id
    var customerName:String? = null//会员名称
    var listTask:List<TaskRemind>?=null//任务列表

}