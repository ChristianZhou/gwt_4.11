package com.mySampleApplication.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.model.CustomerModel;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.KeywordPageData;
import com.mySampleApplication.shared.model.PageData;

import java.util.List;

public interface CustomerServiceRemoteAsync {
    void read(Long var1, AsyncCallback<CustomerData> async);

//    void listPage(KeywordPageData keywordPageData, AsyncCallback<PageData<CustomerData>> async);
    void save(CustomerData var1, AsyncCallback<Void> async);

    void delete(Long var1, AsyncCallback<Void> async);

    void update(CustomerData var1, AsyncCallback<Void> async);


    void createNewCustomer(AsyncCallback<com.mySampleApplication.shared.model.CustomerData> async);

    void saveCustomer(com.mySampleApplication.shared.model.CustomerData customerData, AsyncCallback<Void> async);


    void list(KeywordPageData keywordPageData, AsyncCallback<List<CustomerModel>> async);

    void listPage(KeywordPageData keywordPageData, AsyncCallback<PageData<CustomerData>> async);


//    void listPage(KeywordPageData keywordPageData, AsyncCallback<PageData<CustomerData>> async);
}
