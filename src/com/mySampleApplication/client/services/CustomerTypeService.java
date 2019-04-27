package com.mySampleApplication.client.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mySampleApplication.shared.model.CustomerType;

import java.util.List;

/**
 * @author zhouguixing
 * @date 2019/4/27 4:23
 * @description
 */
@RemoteServiceRelativePath("customerTypeService")
public interface CustomerTypeService extends RemoteService {
    String url ="http://localhost:18888/customerTypeService" ;

    List<CustomerType> list();

    class App {
        private static CustomerTypeServiceAsync customerTypeServiceAsync = GWT.create(CustomerTypeService.class);

        public static synchronized CustomerTypeServiceAsync getInstance() {
            return customerTypeServiceAsync;
        }
    }
}
