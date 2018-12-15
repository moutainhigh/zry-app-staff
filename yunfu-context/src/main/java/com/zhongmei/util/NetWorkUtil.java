package com.zhongmei.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;

public class NetWorkUtil {
    public NetWorkUtil() {
    }

    public static String getLocalIpv4Address(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            Method method = ConnectivityManager.class.getMethod("getActiveLinkProperties");
            LinkProperties prop = (LinkProperties) method.invoke(cm);
            return formatIpAddresses(prop);
        } catch (NoSuchMethodException var4) {
            var4.printStackTrace();
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
        } catch (IllegalAccessException var6) {
            var6.printStackTrace();
        }

        return "127.0.0.1";
    }

    private static String formatIpAddresses(LinkProperties prop) {
        if (prop == null) {
            return null;
        } else {
            try {
                Method method = LinkProperties.class.getMethod("getAddresses");
                method.setAccessible(true);
                Collection<InetAddress> addresses = (Collection) method.invoke(prop);
                Iterator var4 = addresses.iterator();

                while (var4.hasNext()) {
                    InetAddress address = (InetAddress) var4.next();
                    if (address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            } catch (NoSuchMethodException var5) {
                var5.printStackTrace();
            } catch (InvocationTargetException var6) {
                var6.printStackTrace();
            } catch (IllegalAccessException var7) {
                var7.printStackTrace();
            }

            return "127.0.0.1";
        }
    }
}

