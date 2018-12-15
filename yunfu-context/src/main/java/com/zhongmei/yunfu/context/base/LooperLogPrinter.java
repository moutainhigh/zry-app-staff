//package com.zhongmei.bty.commonmodule.base;
//
//import android.util.Printer;
//
//import OSLog;
//
//public class LooperLogPrinter implements Printer {
//
//    private String tag;
//    private long lastTime;
//
//    public LooperLogPrinter(String tag) {
//        this.tag = tag;
//    }
//
//    public void println(String x) {
//        //super.println(x);
//        if (x.startsWith(">>>>> Dispatching to ")) {
//            beforePrintln(x);
//        }
//
//        if (x.startsWith("<<<<< Finished to ")) {
//            afterPrintln(x);
//        }
//    }
//
//    protected void afterPrintln(String x) {
//        lastTime = System.currentTimeMillis();
//    }
//
//    protected void beforePrintln(String x) {
//        long diffTime = System.currentTimeMillis() - lastTime;
//        if (diffTime >= 500) {
//            //Log.println(Log.VERBOSE, tag, String.format("[%-4d] %s", diffTime, x));
//            OSLog.getLog("anr").log(String.format("[%-4d] %s", diffTime, x));
//        }
//    }
//}
