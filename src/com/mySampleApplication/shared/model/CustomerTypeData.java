package com.mySampleApplication.shared.model;


import com.extjs.gxt.ui.client.data.BaseModelData;

import java.io.Serializable;

public class CustomerTypeData extends BaseModelData implements Serializable {

  private Long custTypeCode;
  private String custTypeName;

  public Long getCustTypeCode() {
      return custTypeCode;
//    return get("custTypeCode");
  }

  public void setCustTypeCode(Long custTypeCode) {
    set("custTypeCode",custTypeCode);
    this.custTypeCode = custTypeCode;
  }

  public String getCustTypeName() {
//    return get("custTypeName");
    return custTypeName;
  }

  public void setCustTypeName(String custTypeName) {
    set("custTypeName",custTypeName);
    this.custTypeName = custTypeName;
  }

}
