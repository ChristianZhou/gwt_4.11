package com.mySampleApplication.shared.model;


import java.io.Serializable;

public class CustomerType implements Serializable {

  private String custTypeCode;
  private String custTypeName;


  public String getCustTypeCode() {
    return custTypeCode;
  }

  public void setCustTypeCode(String custTypeCode) {
    this.custTypeCode = custTypeCode;
  }


  public String getCustTypeName() {
    return custTypeName;
  }

  public void setCustTypeName(String custTypeName) {
    this.custTypeName = custTypeName;
  }

}
