package com.zhongmei.bty.offline.backup.backup4qs;

/**
 * Created by demo on 2018/12/15
 */
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

    /**
     * 离线数据是否上传完成,阻塞线程
     *
     * @return
     */
    public static boolean isUploadFinish() {
        return qsBackup.isUploadFinish();
    }

    public static void backup(Object object) {
        qsBackup.backup(object);
    }
}
