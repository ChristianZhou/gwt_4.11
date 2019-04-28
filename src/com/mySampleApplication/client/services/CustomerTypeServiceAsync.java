package com.mySampleApplication.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.shared.model.CustomerTypeData;

import java.util.List;

public interface CustomerTypeServiceAsync {
    void list(AsyncCallback<List<CustomerTypeData>> async);
}
