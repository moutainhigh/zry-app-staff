package com.zhongmei.yunfu.context.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.hardware.usb.UsbDevice;
import android.util.Log;


@SuppressLint("NewApi")
public class DeviceNodeUtil {

    private static final String TAG = DeviceNodeUtil.class.getSimpleName();

    private static final int VENDOR_ID = 1659;

    private static final int PRODUCT_ID = 8963;

    public static boolean isEwsDevice(UsbDevice device) {
        if (device == null) {
            return false;
        }
        return device.getVendorId() == VENDOR_ID && device.getProductId() == PRODUCT_ID;
    }

    public static String getDeviceNode() {
        return getCharNodeByUSBid(VENDOR_ID, PRODUCT_ID);
    }

    public static String getCharNodeByUSBid(int idVendor, int idProduct) {
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

    public static String readDevNode(String filePath) {
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


    public static boolean isDeviceConnected() {
        return getDeviceNode() != null;
    }


    public static String getDeviceNodeAddress() {
        return getDeviceNode();
    }
}
