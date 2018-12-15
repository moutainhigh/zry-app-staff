package com.zhongmei.bty.basemodule.devices.phone.event;

/**
 * 显示通话对话框
 *
 * @date 2014-10-23
 */
public class ActionShowCallingDialog {

    @Deprecated
    private String phone;
    /**
     * 呼叫类型, 参考 {@link CallHistory#TYPE_INCALL} {@link CallHistory#TYPE_OUTCALL} <br/>
     */
    @Deprecated
    private int type;

    public ActionShowCallingDialog() {

    }

    /**
     * 不传递号码及状态, 用的时候直接从CallHistory中取
     *
     * @param phone
     * @param type
     */
    @Deprecated
    public ActionShowCallingDialog(String phone, int type) {
        super();
        this.phone = phone;
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
