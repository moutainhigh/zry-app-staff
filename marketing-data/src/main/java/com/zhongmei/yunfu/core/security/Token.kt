package com.zhongmei.yunfu.core.security

import java.util.*

abstract class Token {

    companion object {
        internal const val KEY = "k\te\ty"
        internal const val JOIN_SEPARATOR = "\n"

        @JvmStatic
        fun getEncoder(): Encoder {
            return Encoder()
        }

        @JvmStatic
        fun getDecoder(): Decoder {
            return Decoder()
        }

        @JvmStatic
        fun encode(account: String, password: String, userId: Long, userName: String, shopId: Long, brandId: Long): String? {
            return getEncoder().encode(account, password, userId.toString(), userName, shopId.toString(), brandId.toString())
        }

        @JvmStatic
        fun decode(token: String): UserVo {
            var decode = getDecoder().decode(token)
            return UserVo(decode[0].toLong(), decode[1], decode[2], decode[3].toLong(), decode[4], decode[5].toLong(), decode[6].toLong())
        }
    }

    class UserVo(val timeInMillis: Long,
                 val account: String,
                 val password: String,
                 val userId: Long? = null,
                 val userName: String? = null,
                 val shopId: Long? = null,
                 val brandId: Long? = null
    )

    class Encoder {
        fun encode(account: String, password: String, vararg extra: String?): String? {
            val joinToString = extra?.joinToString(JOIN_SEPARATOR)
            var arrayOf = arrayOf(Calendar.getInstance().timeInMillis, account, password, joinToString)
            val token = arrayOf.joinToString(JOIN_SEPARATOR)
            return AESUtil.encrypt(token, KEY)
        }
    }

    class Decoder {
        fun decode(token: String): Array<String> {
            val dec = AESUtil.decrypt(token, KEY)
            return dec!!.split(JOIN_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
    }


}
