package com.zhongmei.yunfu.core.security

import org.apache.shiro.crypto.hash.Sha1Hash

class PasswordImpl : Password() {

    override fun generate(account: String, password: String): String {
        /*val s1 = AESUtil.encrypt(String.format("%s#%s", account, password), account)
        val encode = DigestUtils.encode(s1!!)
        return encode!!.substring(2, 18) + encode.substring(22, 38)*/
        return Sha1Hash(password, account, 100).toHex()
    }
}
