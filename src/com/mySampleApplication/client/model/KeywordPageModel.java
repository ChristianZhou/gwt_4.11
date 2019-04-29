package com.mySampleApplication.client.model;

import com.extjs.gxt.ui.client.data.BeanModelMarker;
import com.extjs.gxt.ui.client.data.BeanModelMarker.BEAN;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.KeywordPageData;

import java.io.Serializable;

/**
 * @author zhouguixing
 * @date 2019/4/26 17:03
 * @description
 */
@BEAN(KeywordPageData.class)
public class KeywordPageModel implements BeanModelMarker,Serializable{
}
