package com.zhongmei.bty.basemodule.devices.liandipos.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.hardware.usb.UsbDevice;
import android.os.Environment;
import android.util.Log;

/**
 * Created by demo on 2018/12/15
 */
public class LDUtil {

    private static final String TAG = LDUtil.class.getSimpleName();

    private static final int LIAN_DI_VENDOR_ID = 1317;

    private static final int LIAN_PRODUCT_ID = 42151;

    private static final int LIAN_E8_DI_VENDOR_ID = 9969;

    private static final int LIAN_E8_PRODUCT_ID = 22256;

    public static boolean isLDDevice(UsbDevice device) {
        if (device == null) {
            return false;
        }
        return (device.getVendorId() == LIAN_DI_VENDOR_ID && device.getProductId() == LIAN_PRODUCT_ID)
                || (device.getVendorId() == LIAN_E8_DI_VENDOR_ID && device.getProductId() == LIAN_E8_PRODUCT_ID);
    }

    public static String getLDDevicePort() {
        String e5 = getCharNodeByUSBid(LIAN_DI_VENDOR_ID, LIAN_PRODUCT_ID);
        String e8 = getCharNodeByUSBid(LIAN_E8_DI_VENDOR_ID, LIAN_E8_PRODUCT_ID);
        return e5 != null ? e5 : e8;
    }

    private static String getCharNodeByUSBid(int idVendor, int idProduct) {
        final String USB_SERIAL_DIR = "/sys/class/tty/";
        int vid, pid;

        File usbDir = new File(USB_SERIAL_DIR);
        File[] usbFiles = usbDir.listFiles();

        if (null == usbFiles) {
            return null;
        }

        try {
            for (File usbFile : usbFiles) {
                String fileName = usbFile.getName();
                if (fileName.startsWith("ttyACM")) {
                    vid = Integer.parseInt(readDevNode(usbFile.getAbsolutePath() + "/../../../idVendor"), 16);
                    pid = Integer.parseInt(readDevNode(usbFile.getAbsolutePath() + "/../../../idProduct"), 16);
                    if (vid == idVendor && pid == idProduct) {
                        return "/dev/" + fileName;
                    }
                } else if (fileName.startsWith("ttyUSB")) {
                    vid = Integer.parseInt(readDevNode(usbFile.getAbsolutePath() + "/../../../../idVendor"), 16);
                    pid = Integer.parseInt(readDevNode(usbFile.getAbsolutePath() + "/../../../../idProduct"), 16);
                    if (vid == idVendor && pid == idProduct) {
                        return "/dev/" + fileName;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }

        return null;
    }

    private static String readDevNode(String filePath) {
        String content = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            content = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String getPrintFilePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ALLINPAY/Print.txt";
    }

    /**
     * 判断pos机有没有连接
     *
     * @return the boolean
     */
    public static boolean isPosConnected() {
        return getLDDevicePort() != null;
    }
}
