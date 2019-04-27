package com.mySampleApplication.server.services;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mySampleApplication.client.model.CustomerModel;
import com.mySampleApplication.client.services.CustomerServiceRemote;
import com.mySampleApplication.client.utils.CglibBeanCopierUtil;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.KeywordPageData;
import com.mySampleApplication.shared.model.PageData;
import com.zgx.bootdemo.entity.Customer;
import com.zgx.bootdemo.entity.KeywordPage;
import com.zgx.bootdemo.entity.Page;
import com.zgx.bootdemo.service.CustomerSerivce;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerServiceRemoteImpl extends RemoteServiceServlet implements CustomerServiceRemote {

    private CustomerSerivce customerService = HttpInvokerProxyUtil.getInstance().doRefer(CustomerSerivce.class, url);

    private Map<String, CustomerData> customerDataMap = new HashMap<String, CustomerData>();
    @Override
    public CustomerData read(Long var1) throws Exception {
        Customer read = customerService.read(var1);
        CustomerData customer = new CustomerData();
        CglibBeanCopierUtil.copyProperties(read, customer);
        return customer;
    }

    @Override
    public PageData<CustomerData> listPage(KeywordPageData keywordPageData) {
        String keyword = keywordPageData.getKeyword()==null?"":keywordPageData.getKeyword();

        Page pageCopy = new Page();

        KeywordPage keywordPage = new KeywordPage();
        keywordPage.setKeyword(keyword);
        keywordPage.setPage(pageCopy);

        Page<Customer> page1 = customerService.listPage(keywordPage);
        PageData<CustomerData> pageData = new PageData<>();
        CglibBeanCopierUtil.copyPageData(page1, pageData);

        return pageData;
    }
    @Override
    public List<CustomerModel> list(KeywordPageData keywordPageData) {
        String keyword = keywordPageData.getKeyword()==null?"":keywordPageData.getKeyword();

        Page pageCopy = new Page();

        KeywordPage keywordPage = new KeywordPage();
        keywordPage.setKeyword(keyword);
        keywordPage.setPage(pageCopy);

        Page<Customer> page1 = customerService.listPage(keywordPage);
        PageData<CustomerData> pageData = new PageData<>();
        CglibBeanCopierUtil.copyPageData(page1, pageData);

        List<CustomerData> list = pageData.getList();
        List<CustomerModel> list1 = new ArrayList<>();
        for (CustomerData customerData : list) {
            CustomerModel customerModel = new CustomerModel();
            CglibBeanCopierUtil.copyProperties(customerData,customerModel);
            list1.add(customerModel);
        }
        return list1;
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

    @Override
    public com.mySampleApplication.shared.model.CustomerData createNewCustomer() {
        com.mySampleApplication.shared.model.CustomerData customerData = new com.mySampleApplication.shared.model.CustomerData();
        customerData.setCustName("Christian");
        return customerData;
    }

    @Override
    public void saveCustomer(com.mySampleApplication.shared.model.CustomerData feed) {
        Element eleRoot = new Element("rss");
        eleRoot.setAttribute(new Attribute("version", "2.0"));

        // Create a document from the feed object
        Document document = new Document(eleRoot);

        Element eleChannel = new Element("channel");
        Element eleTitle = new Element("title");
        Element eleDescription = new Element("description");
        Element eleLink = new Element("link");

        eleTitle.setText(feed.getMnemonicCode());
        eleDescription.setText(feed.getFax());
        eleLink.setText(feed.getCustName());

        eleChannel.addContent(eleTitle);
        eleChannel.addContent(eleDescription);
        eleChannel.addContent(eleLink);

        eleRoot.addContent(eleChannel);

        try {
            XMLOutputter serializer = new XMLOutputter();
            Format prettyFormat = Format.getPrettyFormat();
            serializer.setFormat(prettyFormat);
            serializer.output(document, System.out);
        } catch (IOException e) {
            System.out.println("Error saving feed");
        }
    }
}