

package com.zhongmei.bty.common.util.mime4j.decoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.zhongmei.bty.commonmodule.util.CharsetUtil;


public class DecoderUtil {

    private static String TAG = DecoderUtil.class.getSimpleName();


    @SuppressWarnings("resource")
    public static byte[] decodeBaseQuotedPrintable(String s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            byte[] bytes = s.getBytes("US-ASCII");

            QuotedPrintableInputStream is = new QuotedPrintableInputStream(
                    new ByteArrayInputStream(bytes));

            int b = 0;
            while ((b = is.read()) != -1) {
                baos.write(b);
            }
        } catch (IOException e) {

            Log.e(TAG, e.getLocalizedMessage());
        }

        return baos.toByteArray();
    }


    @SuppressWarnings("resource")
    public static byte[] decodeBase64(String s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            byte[] bytes = s.getBytes("US-ASCII");

            Base64InputStream is = new Base64InputStream(
                    new ByteArrayInputStream(bytes));

            int b = 0;
            while ((b = is.read()) != -1) {
                baos.write(b);
            }
        } catch (IOException e) {

            Log.e(TAG, e.getLocalizedMessage());
        }

        return baos.toByteArray();
    }


    public static String decodeB(String encodedWord, String charset)
            throws UnsupportedEncodingException {

        return new String(decodeBase64(encodedWord), charset);
    }


    public static String decodeQ(String encodedWord, String charset)
            throws UnsupportedEncodingException {


        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < encodedWord.length(); i++) {
            char c = encodedWord.charAt(i);
            if (c == '_') {
                sb.append("=20");
            } else {
                sb.append(c);
            }
        }

        return new String(decodeBaseQuotedPrintable(sb.toString()), charset);
    }


    public static String decodeEncodedWords(String body) {

                                        if (body.indexOf("=?") == -1) {
            return body;
        }

        int previousEnd = 0;
        boolean previousWasEncoded = false;

        StringBuilder sb = new StringBuilder();

        while (true) {
            int begin = body.indexOf("=?", previousEnd);

                                                                                    int endScan = begin + 2;
            if (begin != -1) {
                int qm1 = body.indexOf('?', endScan + 2);
                int qm2 = body.indexOf('?', qm1 + 1);
                if (qm2 != -1) {
                    endScan = qm2 + 1;
                }
            }

            int end = begin == -1 ? -1 : body.indexOf("?=", endScan);
            if (end == -1) {
                if (previousEnd == 0)
                    return body;

                sb.append(body.substring(previousEnd));
                return sb.toString();
            }
            end += 2;

            String sep = body.substring(previousEnd, begin);

            String decoded = decodeEncodedWord(body, begin, end);
            if (decoded == null) {
                sb.append(sep);
                sb.append(body.substring(begin, end));
            } else {
                if (!previousWasEncoded || !CharsetUtil.isWhitespace(sep)) {
                    sb.append(sep);
                }
                sb.append(decoded);
            }

            previousEnd = end;
            previousWasEncoded = decoded != null;
        }
    }

        private static String decodeEncodedWord(String body, int begin, int end) {
        int qm1 = body.indexOf('?', begin + 2);
        if (qm1 == end - 2)
            return null;

        int qm2 = body.indexOf('?', qm1 + 1);
        if (qm2 == end - 2)
            return null;

        String mimeCharset = body.substring(begin + 2, qm1);
        String encoding = body.substring(qm1 + 1, qm2);
        String encodedText = body.substring(qm2 + 1, end - 2);

        String charset = CharsetUtil.toJavaCharset(mimeCharset);
        if (charset == null) {
            return null;
        } else if (!CharsetUtil.isDecodingSupported(charset)) {
            return null;
        }

        if (encodedText.length() == 0) {
            return null;
        }

        try {
            if (encoding.equalsIgnoreCase("Q")) {
                return DecoderUtil.decodeQ(encodedText, charset);
            } else if (encoding.equalsIgnoreCase("B")) {
                return DecoderUtil.decodeB(encodedText, charset);
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage(), e);
                        return null;
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }
}
