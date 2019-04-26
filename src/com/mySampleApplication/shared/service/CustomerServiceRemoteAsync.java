package com.mySampleApplication.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.data.CustomerData;
import com.mySampleApplication.client.data.KeywordPageData;
import com.mySampleApplication.client.data.PageData;

public interface CustomerServiceRemoteAsync {
    void read(Long var1, AsyncCallback<CustomerData> async);

    void listPage(KeywordPageData keywordPageData, AsyncCallback<PageData<CustomerData>> async);
    void save(CustomerData var1, AsyncCallback<Void> async);

    void delete(Long var1, AsyncCallback<Void> async);

    void update(CustomerData var1, AsyncCallback<Void> async);


//    void listPage(KeywordPageData keywordPageData, AsyncCallback<PageData<CustomerData>> async);
}
