package com.mySampleApplication.shared.model;


import com.extjs.gxt.ui.client.data.BaseModelData;

import java.io.Serializable;

/**
 * @author zhouguixing
 * @date 2019/4/17 17:07
 * @description 客户实体类 表：CUSTOMER
 */
public class CustomerData extends BaseModelData implements Serializable {

  private Long custCode;//主键、客户代码
  private String setSettlementMethodCode;//结算方式
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

  private CustomerTypeData customerTypeData;//客户类型

  public CustomerTypeData getCustomerTypeData() {
    return get("customerTypeData");
  }

  public void setCustomerTypeData(CustomerTypeData customerTypeData) {
    this.customerTypeData = customerTypeData;
    set("customerTypeData", customerTypeData);
  }

  public Long getCustCode() {
    return get("custCode");
  }

  public void setCustCode(Long custCode) {
    this.custCode = custCode;
    set("custCode", custCode);
  }

  public BankData getBankData() {
    return get("bankData");
  }

  public void setBankData(BankData bankData) {
    this.bankData = bankData;
    set("bankData", bankData);
  }

  public String getSetSettlementMethodCode() {
    return get("setSettlementMethodCode");
  }

  public void setSetSettlementMethodCode(String setSettlementMethodCode) {
    this.setSettlementMethodCode = setSettlementMethodCode;
    set("setSettlementMethodCode", setSettlementMethodCode);
  }

  public String getCustName() {
    return get("custName");
  }

  public void setCustName(String custName) {
    this.custName = custName;
    set("custName", custName);
  }


  public String getMnemonicCode() {
    return get("mnemonicCode");
  }

  public void setMnemonicCode(String mnemonicCode) {
    this.mnemonicCode = mnemonicCode;
    set("mnemonicCode", mnemonicCode);
  }


  public String getTel() {
    return get("tel");
  }

  public void setTel(String tel) {
    this.tel = tel;
    set("tel", tel);
  }


  public String getFax() {
    return get("fax");
  }

  public void setFax(String fax) {
    this.fax = fax;
    set("fax", fax);
  }


  public String getEmail() {
    return get("email");
  }

  public void setEmail(String email) {
    this.email = email;
    set("email", email);
  }


  public Integer getTag() {
    return get("tag");
  }

  public void setTag(Integer tag) {
    this.tag = tag;
    set("tag", tag);
  }


  public String getAddress() {
    return get("address");
  }

  public void setAddress(String address) {
    this.address = address;
    set("address", address);
  }


  public String getWorkUnitCode() {
    return get("workUnitCode");
  }

  public void setWorkUnitCode(String workUnitCode) {
    this.workUnitCode = workUnitCode;
    set("workUnitCode", workUnitCode);
  }


  public String getPostcode() {
    return get("postcode");
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
    set("postcode", postcode);
  }


  public java.sql.Date getSettlementDate() {
    return get("settlementDate");
  }

  public void setSettlementDate(java.sql.Date settlementDate) {
    this.settlementDate = settlementDate;
    set("settlementDate", settlementDate);
  }


  public java.sql.Date getBirthday() {
    return get("birthday");
  }

  public void setBirthday(java.sql.Date birthday) {
    this.birthday = birthday;
    set("birthday", birthday);
  }


  public String getBankAccount() {
    return get("bankAccount");
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
    set("bankAccount", bankAccount);
  }


  public java.sql.Date getMonthlySettlementDate() {
    return get("monthlySettlementDate");
  }

  public void setMonthlySettlementDate(java.sql.Date monthlySettlementDate) {
    this.monthlySettlementDate = monthlySettlementDate;
    set("monthlySettlementDate", monthlySettlementDate);
  }
}
