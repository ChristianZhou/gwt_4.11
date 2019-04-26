package com.mySampleApplication.client.data;

import java.io.Serializable;

/**
 * @author zhouguixing
 * @date 2019/4/23 15:05
 * @description 银行
 */
public class BankData implements Serializable {
    private String bankCode;
    private String bankName;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
