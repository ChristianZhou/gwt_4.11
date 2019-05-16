package com.mySampleApplication.client.services;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mySampleApplication.client.model.CustomerModel;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.KeywordPageData;
import com.mySampleApplication.shared.model.PageData;

import java.util.List;

@RemoteServiceRelativePath("customerServiceRemote")
public interface CustomerServiceRemote extends RemoteService {
    // Sample interface method of remote interface

    String url = "http://localhost:18888/customerService" ;

    CustomerData read(Long var1) throws Exception;

    PageData<CustomerData> listPage(KeywordPageData keywordPageData);
    List<CustomerData> list(KeywordPageData keywordPageData);

    List<CustomerData> listByType(Long custTypeCode);

    PagingLoadResult<CustomerData> listByType(Long custTypeCode, final
    PagingLoadConfig config);


    void save(CustomerData var1);

    void delete(Long var1) throws Exception;

    void update(CustomerData var1);

    List<CustomerData> listByTypeAndKeyword(Long custTypeCode, String keyword);

    PagingLoadResult<CustomerData> findProducts(Long custTypeCode, String keyword, PagingLoadConfig loadConfig);

    //    void list(KeywordPageData keywordPageData, AsyncCallback<List<CustomerModel>> async);
    //

    class App {
        private static CustomerServiceRemoteAsync customerServiceRemoteAsync = GWT.create(CustomerServiceRemote.class);

        public static synchronized CustomerServiceRemoteAsync getInstance() {
            return customerServiceRemoteAsync;
        }
    }

    com.mySampleApplication.shared.model.CustomerData createNewCustomer();

    void saveCustomer(com.mySampleApplication.shared.model.CustomerData customerData);
}
