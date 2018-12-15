package com.zhongmei.yunfu.context.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.context.R;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

public class NetworkUtil {

    private final static String TAG = NetworkUtil.class.getSimpleName();

    private HashMap<String, String> paramsMap = null; // 用以保存所有的参数对

	/*private DefaultHttpClient httpClient = null;

	private HttpResponse response = null;

	private HttpParams httpParams = null;*/

    private Context mContext;

    public NetworkUtil(Context context) {
        this.mContext = context;
    }

    /*
     * 增加Post参数
     */
    public void addParams(String strParamName, String strParamValue) {
        if (paramsMap == null)
            paramsMap = new HashMap<String, String>();
        paramsMap.put(strParamName, strParamValue);
    }

    public void setParams(HashMap<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    @SuppressWarnings("deprecation")
	/*public Map<String, String> doPostRequestGZip(String strUrl) {
		List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
		paramPairs.add(new BasicNameValuePair("type", "0"));
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if (paramsMap != null) {
			Set<String> keys = paramsMap.keySet();
			for (Iterator<String> itr = keys.iterator(); itr.hasNext();) {
				String key = (String) itr.next();
				paramPairs.add(new BasicNameValuePair(key, paramsMap.get(key)));
			}
		}

		OSLog.info("http gzip post request url=" + strUrl
				+ "&" + paramPairs.toString());
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 60 * 1000);// 设置连接超时时间1分钟;
		HttpConnectionParams.setSoTimeout(httpParams, 60 * 1000);
		HttpClientParams.setRedirecting(httpParams, true);// 重定向为true;

		try {
			HttpPost httpRequest = new HttpPost(strUrl);
			httpRequest.setEntity(new UrlEncodedFormEntity(paramPairs,
					HTTP.UTF_8));

			if (httpClient == null) {
				httpClient = new DefaultHttpClient(httpParams);
				String proxyHost = android.net.Proxy.getDefaultHost();
				if (proxyHost != null) {
					HttpHost phost = null;
					if (!isUsedWifi()) {
						phost = new HttpHost(
								android.net.Proxy.getDefaultHost(),
								android.net.Proxy.getDefaultPort(), "http");
						httpClient.getParams().setParameter(
								ConnRoutePNames.DEFAULT_PROXY, phost);
					}
				}

			}
			httpClient.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);
			response = httpClient.execute(httpRequest);
			if (response != null) {
				// 200返回码表示成功，其余的表示失败
				int iStatusCode = response.getStatusLine().getStatusCode();
				// 判断返回码是否是200，如果是200，说明连接成功;
				if (iStatusCode == 200) {
					resultMap.put("errno", "0");
					boolean gzipResponse = false;
					Header[] headers = response.getHeaders("Content-Encoding");
					if (headers != null && headers.length > 0) {
						Header header = headers[0];
						String values = header.getValue();
						gzipResponse = values.toLowerCase().indexOf("gzip") > -1;
					}
					if (gzipResponse) {
						OSLog.info("GZip response.");
						InputStream gzipin = response.getEntity().getContent();
						BufferedReader in = new BufferedReader(
								new InputStreamReader(new GZIPInputStream(
										gzipin), "utf8"));
						StringBuffer sb = new StringBuffer();
						String line = "";
						while ((line = in.readLine()) != null) {
							sb.append(line);
						}
						in.close();
						gzipin.close();
						resultMap.put("content", sb.toString());
					} else {
						OSLog.info("Common response.");
						String strContent = EntityUtils.toString(response
								.getEntity());
						resultMap.put("content", strContent);
					}
				} else {
					resultMap.put("errno", "1");
					resultMap.put("content", BaseApplication.sInstance.getString(R.string.commonmodule_server_return_fail) + iStatusCode);
				}
			}
		} catch (SocketTimeoutException e) {
			Log.e(TAG, e.getMessage(), e);
			resultMap.put("errno", "-100");
			resultMap.put("content", BaseApplication.sInstance.getString(R.string.commonmodule_server_connect_time_out));
		} catch (UnknownHostException e) {
			Log.e(TAG, e.getMessage(), e);
			resultMap.put("errno", "-100");
			resultMap.put("content", BaseApplication.sInstance.getString(R.string.commonmodule_not_found_server));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			resultMap.put("errno", "-100");
			resultMap.put("content", BaseApplication.sInstance.getString(R.string.commonmodule_read_server_error));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			resultMap.put("errno", "-100");
			resultMap.put("content", e.getLocalizedMessage());
		} finally {
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}

		return resultMap;
	}

	public String compress(String content) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gos = new GZIPOutputStream(baos);
		byte[] inContent = content.getBytes(CharsetUtil.UTF_8);
		gos.write(inContent, 0, inContent.length);
		gos.finish();
		byte[] output = baos.toByteArray();
		baos.flush();
		baos.close();
		return new String(output, CharsetUtil.UTF_8);
	}*/

    private boolean isUsedWifi() {
        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        boolean isUsedWifi = false;// wifiManager.isWifiEnabled();
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            isUsedWifi = true;
        }
        if (isUsedWifi && wifiManager.isWifiEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断本地网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        Context context = BaseApplication.sInstance;
        if (context == null) {
            Log.i(TAG, context.getString(R.string.commonmodule_network_error_context_null));
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo != null && networkInfo.length > 0) {
            for (int i = 0; i < networkInfo.length; i++) {
                // 判断当前网络状态是否为连接状态
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        Log.i(TAG, "网络未连接！");
        return false;
    }


    public static void saveIpAddress(Context context) {
        String ipAddress = getIp(context);
        if (!TextUtils.isEmpty(ipAddress)) {
            SpHelper.getDefault().saveIpAddress(ipAddress);
        }
    }


    private static String getIp(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }

        }
        return null;
    }

    private static String intIP2StringIP(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

}
