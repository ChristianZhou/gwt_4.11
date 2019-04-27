package com.mySampleApplication.shared.model;


import java.io.Serializable;

/**
 * @author zhouguixing
 * @date 2019/4/17 17:07
 * @description 客户实体类 表：CUSTOMER
 */
public class CustomerData implements Serializable {

  private Long custCode;//主键、客户代码
  private String setSettlementMethodCode;//结算方式
  private String cusCustTypeCode;//客户类型
  private String custName;//客户名称
  private String mnemonicCode;//助记码
  private String tel;//电话
  private String fax;//传真号
  private String email;//邮箱
  private String address;//地址
  private String workUnitCode;//工作单位
  private String postcode;//邮编
  private String bankAccount;//银行账号
  private Integer tag;//启用标记
  private java.sql.Date settlementDate;//结算日期
  private java.sql.Date birthday;//生日
  private java.sql.Date monthlySettlementDate;//月结日期

  private BankData bankData;//银行

  public CustomerData() {
  }

  public Long getCustCode() {
    return custCode;
  }

  public void setCustCode(Long custCode) {
    this.custCode = custCode;
  }

  public BankData getBankData() {
    return bankData;
  }

  public void setBankData(BankData bankData) {
    this.bankData = bankData;
  }

  public String getSetSettlementMethodCode() {
    return setSettlementMethodCode;
  }

  public void setSetSettlementMethodCode(String setSettlementMethodCode) {
    this.setSettlementMethodCode = setSettlementMethodCode;
  }


  public String getCusCustTypeCode() {
    return cusCustTypeCode;
  }

  public void setCusCustTypeCode(String cusCustTypeCode) {
    this.cusCustTypeCode = cusCustTypeCode;
  }


  public String getCustName() {
    return custName;
  }

  public void setCustName(String custName) {
    this.custName = custName;
  }


  public String getMnemonicCode() {
    return mnemonicCode;
  }

  public void setMnemonicCode(String mnemonicCode) {
    this.mnemonicCode = mnemonicCode;
  }


  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }


  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public Integer getTag() {
    return tag;
  }

  public void setTag(Integer tag) {
    this.tag = tag;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public String getWorkUnitCode() {
    return workUnitCode;
  }

  public void setWorkUnitCode(String workUnitCode) {
    this.workUnitCode = workUnitCode;
  }


  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }


  public java.sql.Date getSettlementDate() {
    return settlementDate;
  }

  public void setSettlementDate(java.sql.Date settlementDate) {
    this.settlementDate = settlementDate;
  }


  public java.sql.Date getBirthday() {
    return birthday;
  }

  public void setBirthday(java.sql.Date birthday) {
    this.birthday = birthday;
  }


  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }


  public java.sql.Date getMonthlySettlementDate() {
    return monthlySettlementDate;
  }

  public void setMonthlySettlementDate(java.sql.Date monthlySettlementDate) {
    this.monthlySettlementDate = monthlySettlementDate;
  }
}
