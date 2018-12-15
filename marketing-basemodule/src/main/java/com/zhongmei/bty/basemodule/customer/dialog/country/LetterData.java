package com.zhongmei.bty.basemodule.customer.dialog.country;

/**
 * 存储letter相关的数据
 *
 * @date:2016年6月13日上午9:49:20
 */
public class LetterData {
    // 首字母的顺序
    int section;

    // 一个字母下有多少行
    int lineNum;

    // 一个字母下有多少item
    int count;

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
