package com.mySampleApplication.client.model;

import com.extjs.gxt.ui.client.data.BeanModelMarker;
import com.extjs.gxt.ui.client.data.BeanModelMarker.BEAN;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.mySampleApplication.shared.model.CustomerData;

import java.io.Serializable;

/**
 * @author zhouguixing
 * @date 2019/4/26 17:03
 * @description
 */
@BEAN(CustomerData.class)
public class CustomerModel  implements BeanModelMarker{
}
