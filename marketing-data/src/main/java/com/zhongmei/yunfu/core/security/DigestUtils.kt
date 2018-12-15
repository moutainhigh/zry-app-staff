package com.zhongmei.yunfu.core.security

import java.security.MessageDigest

object DigestUtils {

    private val HEX = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    private fun getFormattedText(bytes: ByteArray): String {
        val len = bytes.size
        val buf = StringBuilder(len * 2)
        // 把密文转换成十六进制的字符串形式
        for (j in 0 until len) {
            buf.append(HEX[bytes[j].toInt() shr 4 and 0x0f])
            buf.append(HEX[bytes[j].toInt() and 0x0f])
        }
        return buf.toString()
    }

    fun encode(str: String): String? {
        try {
            val messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.update(str.toByteArray())
            return getFormattedText(messageDigest.digest())
        } catch (e: Exception) {
            return null
        }
    }

    fun byteArrayToInt(b: ByteArray): Int {
        return b[3].toInt() and 0xFF or (
                b[2].toInt() and 0xFF shl 8) or (
                b[1].toInt() and 0xFF shl 16) or (
                b[0].toInt() and 0xFF shl 24)
    }

    fun intToByteArray(a: Int): ByteArray {
        return byteArrayOf((a shr 24 and 0xFF).toByte(), (a shr 16 and 0xFF).toByte(), (a shr 8 and 0xFF).toByte(), (a and 0xFF).toByte())
    }
}
