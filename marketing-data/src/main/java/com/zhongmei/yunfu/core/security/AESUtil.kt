package com.zhongmei.yunfu.core.security

import org.apache.shiro.codec.Base64
import org.apache.shiro.codec.Hex
import java.nio.charset.Charset
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

/**
 * @version V1.0
 * *
 * @desc AES 加密工具类
 */
object AESUtil {

    private val KEY_ALGORITHM = "AES"
    private val DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"//默认的加密算法

    val StandardCharsets_UTF_8 = Charset.forName("UTF-8")

    /**
     * AES 加密操作

     * @param content  待加密内容
     * *
     * @param password 加密密码
     * *
     * @return 返回Base64转码后的加密数据
     */
    fun encrypt(content: String, password: String): String? {
        return try {
            val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)// 创建密码器
            val byteContent = content.toByteArray(StandardCharsets_UTF_8)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password))// 初始化为加密模式的密码器
            val result = cipher.doFinal(byteContent)// 加密
            //return Base64.encodeToString(result);//通过Base64转码返回
            Hex.encodeToString(result)
        } catch (ex: Exception) {
            Logger.getLogger(AESUtil::class.java.name).log(Level.SEVERE, null, ex)
            null
        }

    }

    /**
     * AES 解密操作

     * @param content
     * *
     * @param password
     * *
     * @return
     */
    fun decrypt(content: String, password: String): String? {
        return try {
            //实例化
            val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password))
            var decodeContent = Hex.decode(content) //Base64.decode(content)
            val result = cipher.doFinal(decodeContent)
            result.toString(StandardCharsets_UTF_8)
        } catch (ex: Exception) {
            Logger.getLogger(AESUtil::class.java.name).log(Level.SEVERE, null, ex)
            null
        }

    }

    /**
     * 生成加密秘钥

     * @return
     */
    private fun getSecretKey(password: String): SecretKeySpec? {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        var kg: KeyGenerator? = null
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM)
            //AES 要求密钥长度为 128
            kg!!.init(128, SecureRandom(password.toByteArray()))
            //生成一个密钥
            val secretKey = kg.generateKey()
            return SecretKeySpec(secretKey.encoded, KEY_ALGORITHM)// 转换为AES专用密钥
        } catch (ex: NoSuchAlgorithmException) {
            Logger.getLogger(AESUtil::class.java.name).log(Level.SEVERE, null, ex)
        }

        return null
    }

    fun decodeToString(src: String): ByteArray {
        return if (src.isEmpty()) ByteArray(0) else Base64.decode(src)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val token = Calendar.getInstance().timeInMillis.toString() + "#100100#" + DigestUtils.encode("123456")
        println("原文:" + token)

        val key = "124784883"
        val s1 = encrypt(token, key)
        println("密文:" + s1!!)

        val dec = decrypt(s1, key)
        println("解密:" + dec!!)

        val strings = dec.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (strings.size == 3) {
            println("解密成功")
        }
    }

}
