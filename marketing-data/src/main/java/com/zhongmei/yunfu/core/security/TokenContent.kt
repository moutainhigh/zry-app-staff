package com.zhongmei.yunfu.core.security

import java.util.*

//token SHA1({header}\nCRC32(body))
class TokenContent {

    val timeInMillis = Calendar.getInstance().timeInMillis
    var msgid: String? = null
    var deviceMac: String? = null
    var userId: Long? = null
    var userName: String? = null
    var shopId: Long? = null
    var brandId: Long? = null
    var sgin: String? = null //CRC32(body-len#body-array)
}
