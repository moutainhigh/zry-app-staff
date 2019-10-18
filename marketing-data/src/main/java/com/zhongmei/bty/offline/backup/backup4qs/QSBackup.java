package com.zhongmei.bty.offline.backup.backup4qs;


public class QSBackup {

    private static IQSBackup qsBackup;

    public static void init(IQSBackup qsBackup) {
        QSBackup.qsBackup = qsBackup;
    }

    public static void registerCallback(ITransporter.Callback callback) {
        if (qsBackup != null) {
            qsBackup.registerCallback(callback);
        }
    }

    public static void unRegisterCallback(ITransporter.Callback callback) {
        if (qsBackup != null) {
            qsBackup.unRegisterCallback(callback);
        }
    }


    public static boolean isUploadFinish() {
        return qsBackup.isUploadFinish();
    }

    public static void backup(Object object) {
        qsBackup.backup(object);
    }
}
