package com.mySampleApplication.shared.model;


import java.io.Serializable;

public class CustomerTypeData implements Serializable {

  private Long custTypeCode;
  private String custTypeName;

  public Long getCustTypeCode() {
    return custTypeCode;
  }

  public void setCustTypeCode(Long custTypeCode) {
    this.custTypeCode = custTypeCode;
  }

  public String getCustTypeName() {
    return custTypeName;
  }

  public void setCustTypeName(String custTypeName) {
    this.custTypeName = custTypeName;
  }

}
