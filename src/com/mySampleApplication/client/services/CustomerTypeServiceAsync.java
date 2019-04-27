package com.mySampleApplication.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.shared.model.CustomerType;

import java.util.List;

public interface CustomerTypeServiceAsync {
    void list(AsyncCallback<List<CustomerType>> async);
}
