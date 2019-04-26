package com.mySampleApplication.shared.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mySampleApplication.client.data.CustomerData;
import com.mySampleApplication.client.data.KeywordPageData;
import com.mySampleApplication.client.data.PageData;

@RemoteServiceRelativePath("customerServiceRemote")
public interface CustomerServiceRemote extends RemoteService {
    // Sample interface method of remote interface

    String url = "http://localhost:18888/customerService" ;

    CustomerData read(Long var1) throws Exception;



    PageData<CustomerData> listPage(KeywordPageData keywordPageData);



    void save(CustomerData var1);

    void delete(Long var1) throws Exception;

    void update(CustomerData var1);

    class App {
        private static CustomerServiceRemoteAsync customerServiceRemoteAsync = GWT.create(CustomerServiceRemote.class);

        public static synchronized CustomerServiceRemoteAsync getInstance() {
            return customerServiceRemoteAsync;
        }
    }
}
