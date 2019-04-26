package com.mySampleApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mySampleApplication.client.data.CustomerData;
import com.mySampleApplication.client.data.KeywordPageData;
import com.mySampleApplication.client.data.PageData;
import com.mySampleApplication.shared.service.CustomerServiceRemote;
import com.mySampleApplication.client.utils.CglibBeanCopierUtil;
import com.zgx.bootdemo.entity.Customer;
import com.zgx.bootdemo.entity.KeywordPage;
import com.zgx.bootdemo.entity.Page;
import com.zgx.bootdemo.service.CustomerSerivce;

public class CustomerServiceRemoteImpl extends RemoteServiceServlet implements CustomerServiceRemote {

    private CustomerSerivce customerService = HttpInvokerProxyUtil.getInstance().doRefer(CustomerSerivce.class, url);

    @Override
    public CustomerData read(Long var1) throws Exception {
        Customer read = customerService.read(var1);
        CustomerData customer = new CustomerData();
        CglibBeanCopierUtil.copyProperties(read, customer);
        return customer;
    }

    @Override
    public PageData<CustomerData> listPage(KeywordPageData keywordPageData) {
        String keyword = keywordPageData.getKeyword();
        PageData page = keywordPageData.getPage();

        Page pageCopy = new Page();
        CglibBeanCopierUtil.copyPageData(page, pageCopy);

        KeywordPage keywordPage = new KeywordPage();
        keywordPage.setKeyword(keyword);
        keywordPage.setPage(pageCopy);

        Page<Customer> page1 = customerService.listPage(keywordPage);
        PageData<CustomerData> pageData = new PageData<>();
        CglibBeanCopierUtil.copyPageData(page1, pageData);


        return pageData;
    }

    @Override
    public void save(CustomerData customerData) {
        Customer customer = new Customer();
        CglibBeanCopierUtil.copyProperties(customerData, customer);
        customerService.save(customer);
    }

    @Override
    public void delete(Long var1) throws Exception {
        customerService.delete(var1);
    }

    @Override
    public void update(CustomerData customerData) {
        Customer customer = new Customer();
        CglibBeanCopierUtil.copyProperties(customerData, customer);
        customerService.save(customer);
    }
}