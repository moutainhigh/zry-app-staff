package com.zhongmei.yunfu.core.security

abstract class Password {

    fun generate(content: String): String {
        return generate(content, content)
    }

    abstract fun generate(account: String, password: String): String

    companion object {
        @JvmStatic
        fun create(): Password {
            return PasswordImpl()
        }
    }
}
