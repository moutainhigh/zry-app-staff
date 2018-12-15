package com.zhongmei.yunfu.init.sync;

import android.webkit.URLUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhongmei.yunfu.net.HttpConstant;
import com.zhongmei.yunfu.net.volley.toolbox.HTTPSTrustManager;
import com.zhongmei.yunfu.util.Checks;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @param <Q>
 * @param <R>
 * @version: 1.0
 * @date 2015年4月20日
 */
public class JsonHttpCaller<Q, R> {

    protected static final String METHOD_POST = "POST";
    protected static final String PROPERTY_CONTENT_ENCODING = "Content-Encoding";
    protected static final String PROPERTY_ACCEPT_ENCODING = "Accept-Encoding";
    protected static final String PROPERTY_CONNECTION = "Connection";
    protected static final String PROPERTY_CONTENT_TYPE = "Content-Type";
    protected static final String PROTOCOL_CONTENT_TYPE = "application/json; charset=utf-8";
    protected static final String CONNECTION_CLOSE = "close";
    protected static final String GZIP = "gzip";

    private final String url;
    private final Type returnType;
    private final Map<String, String> httpProperties;
    private int connectTimeout;
    private int readTimeout;
    private GsonBuilder gsonBuilder;

    public JsonHttpCaller(String url, Type returnType) {
        this.url = url;
        this.returnType = returnType;
        httpProperties = new LinkedHashMap<>();
        gsonBuilder = null;
    }

    public void setGsonBuilder(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public void setConnectTimeout(int timeout) {
        this.connectTimeout = timeout;
    }

    public void setReadTimeout(int timeout) {
        this.readTimeout = timeout;
    }

    public void setHttpProperty(String key, String value) {
        Checks.verifyNotNull(key, "key");
        Checks.verifyNotNull(value, "value");
        httpProperties.put(key, value);
    }

    public String getHttpProperty(String key) {
        return httpProperties.get(key);
    }

    public R call(Q requestObj) throws Exception {
        String originalUrl = url;
        URL url = new URL(originalUrl);
        /*String originalHost = url.getHost();
        // 同步接口获取IP
        String ip = DNSCache.getInstance().getIpByHost(originalHost);
        if (ip != null) {
            url = new URL(originalUrl.replaceFirst(originalHost, ip));
        }*/

        HttpURLConnection conn = createConnection(url);
        try {
            Gson gson = createGson();
            String json = gson.toJson(requestObj);
            byte[] body = json.getBytes("UTF-8");
            SyncServiceUtil.info("[%s] -> %s", httpProperties.get(HttpConstant.YF_API_MSG_ID), json);

            boolean acceptCompress = HttpConstant.isAllowCompress(url.toString(), body);
            setConnectionParameters(conn, acceptCompress);
            /*if (ip != null) {
                conn.setRequestProperty("Host", originalHost);
            }*/

            DataOutputStream ops = new DataOutputStream(getOutputStream(conn, acceptCompress, body));
            ops.write(body);
            ops.close();
            SyncServiceUtil.log("同步状态码：" + conn.getResponseCode());

            BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(conn)));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            String responseJson = sb.toString();
            SyncServiceUtil.info(responseJson);
            R response = gson.fromJson(responseJson, returnType);
            return response;
        } finally {
            conn.disconnect();
        }
    }

    private OutputStream getOutputStream(final HttpURLConnection conn, boolean compress, final byte[] body) throws IOException {
        if (compress) {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream()) {
                @Override
                public void close() throws IOException {
                    super.close();
                    SyncServiceUtil.info("[%s]gzip: %d/%d(%.1f%%)",
                            httpProperties.get(HttpConstant.YF_API_MSG_ID),
                            size(),
                            body.length,
                            (body.length - size()) * 100f / body.length);
                }
            };
            return new GZIPOutputStream(out);
        }

        return conn.getOutputStream();
    }

    private InputStream getInputStream(HttpURLConnection conn) throws IOException {
        if (GZIP.equalsIgnoreCase(conn.getContentEncoding())) {
            return new GZIPInputStream(conn.getInputStream());
        }

        return conn.getInputStream();
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        if (URLUtil.isHttpsUrl(url.toString())) {
            HTTPSTrustManager.allowAllSSL();
        }
        return (HttpURLConnection) url.openConnection();
    }

    protected HttpURLConnection setConnectionParameters(HttpURLConnection conn, boolean acceptCompress) throws IOException {
        for (Map.Entry<String, String> entry : httpProperties.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        conn.setRequestMethod(METHOD_POST);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty(PROPERTY_CONNECTION, CONNECTION_CLOSE);
        conn.setRequestProperty(PROPERTY_CONTENT_TYPE, PROTOCOL_CONTENT_TYPE);

        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setRequestProperty(PROPERTY_ACCEPT_ENCODING, GZIP);

        if (acceptCompress) {
            conn.setRequestProperty(HttpConstant.KRY_COMPRESS_TYPE, JsonHttpCaller.GZIP);
        }

        return conn;
    }

    protected Gson createGson() {
        Gson gson;
        if (gsonBuilder == null) {
            gson = new Gson();
        } else {
            gson = gsonBuilder.create();
        }
        return gson;
    }
}
