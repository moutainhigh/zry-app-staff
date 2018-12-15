package com.zhongmei.yunfu.security;

import com.zhongmei.yunfu.core.security.Password;

import org.junit.Test;

public class PasswordTest {
    @Test
    public void generate() throws Exception {
        String generate = Password.create().generate("1101", "123456");
        System.out.println(generate);
    }
}