/*
package com.zhongmei.yunfu.core.security

import com.zhongmei.yunfu.context.util.GsonUtil
import java.util.*
import java.util.zip.CRC32

abstract class Token32 {

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
        fun encode(tokenContent: TokenContent, body: ByteArray = ByteArray(0)): String? {
            return getEncoder().encode(tokenContent, body)
        }

        @JvmStatic
        fun decode(token: String): TokenContent {
            var decode = getDecoder().decode(token)
            return UserVo(decode[0].toLong(), decode[1], decode[2], decode[3].toLong(), decode[4], decode[5].toLong(), decode[6].toLong())
        }
    }

    class Encoder {
        fun encode(tokenContent: TokenContent, body: ByteArray = ByteArray(0)): String? {
            val crc32 = CRC32()
            crc32.update(body)
            val value = crc32.value
            val hexString = java.lang.Long.toHexString(value)
            tokenContent.sgin = hexString
            var objectToJson = GsonUtil.objectToJson(tokenContent)
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
*/
