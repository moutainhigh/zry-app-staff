package com.zhongmei.bty.basemodule.commonbusiness.bean;


public class MediaPlayerFileVo {
    private int fileType;    private String filePath;
    private int resId;

    public MediaPlayerFileVo(int fileType, String filePath) {
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public MediaPlayerFileVo(int fileType, int resId) {
        this.fileType = fileType;
        this.resId = resId;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
