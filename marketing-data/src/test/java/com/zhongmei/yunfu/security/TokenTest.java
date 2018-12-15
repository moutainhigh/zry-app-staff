package com.zhongmei.yunfu.security;

import com.zhongmei.yunfu.core.security.Password;
import com.zhongmei.yunfu.core.security.Token;
import com.zhongmei.yunfu.core.security.TokenUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.zip.CRC32;

public class TokenTest {

    @Test
    public void test() {
        String generate = Password.create().generate("admin", "123456");
        System.out.println(generate);
        String encode = Token.getEncoder().encode("admin", generate, "1", "测试店长", "1", "1");
        System.out.println(encode);
        String[] decode = Token.getDecoder().decode(encode);
        System.out.println(Arrays.toString(decode));

        String encode1 = Token.getEncoder().encode("admin", generate);
        System.out.println(encode1);
        String[] decode1 = Token.getDecoder().decode(encode1);
        System.out.println(Arrays.toString(decode1));
    }

    @Test
    public void crc32() {
        String json = "{\n" +
                "\t\"appType\": \"5\",\n" +
                "\t\"brandID\": 32315,\n" +
                "\t\"content\": {\n" +
                "\t\t\"kitchenTradeItemOperations\": [{\n" +
                "\t\t\t\"creatorId\": 106075385630067712,\n" +
                "\t\t\t\"creatorName\": \"???\",\n" +
                "\t\t\t\"opType\": 8,\n" +
                "\t\t\t\"printStatus\": 2,\n" +
                "\t\t\t\"tradeItemId\": 159326586036888576,\n" +
                "\t\t\t\"deviceIdenty\": \"14:5f:94:bb:0a:72\",\n" +
                "\t\t\t\"shopIdenty\": 810094497,\n" +
                "\t\t\t\"brandIdenty\": 32315,\n" +
                "\t\t\t\"statusFlag\": 1,\n" +
                "\t\t\t\"changed\": true\n" +
                "\t\t}, {\n" +
                "\t\t\t\"creatorId\": 106075385630067712,\n" +
                "\t\t\t\"creatorName\": \"???\",\n" +
                "\t\t\t\"opType\": 8,\n" +
                "\t\t\t\"printStatus\": 2,\n" +
                "\t\t\t\"tradeItemId\": 159326586041082880,\n" +
                "\t\t\t\"deviceIdenty\": \"14:5f:94:bb:0a:72\",\n" +
                "\t\t\t\"shopIdenty\": 810094497,\n" +
                "\t\t\t\"brandIdenty\": 32315,\n" +
                "\t\t\t\"statusFlag\": 1,\n" +
                "\t\t\t\"changed\": true\n" +
                "\t\t}],\n" +
                "\t\t\"tradeItemOperations\": [{\n" +
                "\t\t\t\"printStatus\": 2,\n" +
                "\t\t\t\"serverUpdateTime\": 1542181620000,\n" +
                "\t\t\t\"id\": 159326595620873216,\n" +
                "\t\t\t\"changed\": false\n" +
                "\t\t}, {\n" +
                "\t\t\t\"printStatus\": 2,\n" +
                "\t\t\t\"serverUpdateTime\": 1542181620000,\n" +
                "\t\t\t\"id\": 159326595633456128,\n" +
                "\t\t\t\"changed\": false\n" +
                "\t\t}],\n" +
                "\t\t\"tradeItems\": [{\n" +
                "\t\t\t\"guestPrinted\": 1,\n" +
                "\t\t\t\"issueStatus\": 4,\n" +
                "\t\t\t\"type\": 0,\n" +
                "\t\t\t\"itemSource\": 1,\n" +
                "\t\t\t\"id\": 159326586036888576,\n" +
                "\t\t\t\"serverUpdateTime\": 1542181618260,\n" +
                "\t\t\t\"changed\": false\n" +
                "\t\t}, {\n" +
                "\t\t\t\"guestPrinted\": 1,\n" +
                "\t\t\t\"issueStatus\": 4,\n" +
                "\t\t\t\"type\": 0,\n" +
                "\t\t\t\"itemSource\": 1,\n" +
                "\t\t\t\"id\": 159326586041082881,\n" +
                "\t\t\t\"serverUpdateTime\": 1542181618275,\n" +
                "\t\t\t\"changed\": false\n" +
                "\t\t}]\n" +
                "\t},\n" +
                "\t\"deviceID\": \"14:5f:94:bb:0a:72\",\n" +
                "\t\"opVersionUUID\": \"c1f49a276a164db5af1ab992a52f7331\",\n" +
                "\t\"reqMarker\": \"ee3f0e1d19334086be0efde63bfa9f21\",\n" +
                "\t\"shopID\": 810094497,\n" +
                "\t\"systemType\": \"android\",\n" +
                "\t\"timeZone\": \"Asia/Shanghai\",\n" +
                "\t\"versionCode\": \"2110090100\",\n" +
                "\t\"versionName\": \"9.1.0\"\n" +
                "}";
        long aa = System.currentTimeMillis();
        byte[] bytes = json.getBytes();
        byte[] body = new byte[bytes.length + 4];
        byte[] bodySize = TokenUtils.intToByteArray(bytes.length);
        System.arraycopy(bodySize, 0, body, 0, 4);
        System.arraycopy(bytes, 0, body, 4, bytes.length);
        CRC32 crc32_zip = new CRC32();
        crc32_zip.update(body);
        long value = crc32_zip.getValue();
        String hexString = Long.toHexString(value);
        System.out.println(((System.currentTimeMillis() - aa) / 1000f) + " : " + hexString + " -> " + value);
    }
}