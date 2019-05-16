package com.mySampleApplication.server.services;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mySampleApplication.client.services.CustomerServiceRemote;
import com.mySampleApplication.server.comparator.ComparatorBaseModelData;
import com.mySampleApplication.shared.model.*;
import com.zgx.bootdemo.entity.*;
import com.zgx.bootdemo.service.CustomerSerivce;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        String keyword = keywordPageData.getKeyword() == null ? "" : keywordPageData.getKeyword();

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
    public List<CustomerData> list(KeywordPageData keywordPageData) {
        String keyword = keywordPageData.getKeyword() == null ? "" : keywordPageData.getKeyword();
        PageData pageData = keywordPageData.getPageData() == null ? new PageData() : keywordPageData.getPageData();
        Page pageCopy = new Page();
//        PageData pageData1 = new PageData();
        CglibBeanCopierUtil.copyPage(pageData, pageCopy);

        KeywordPage keywordPage = new KeywordPage();
        keywordPage.setKeyword(keyword);
        keywordPage.setPage(pageCopy);

        Page<Customer> page1 = customerService.listPage(keywordPage);

        CglibBeanCopierUtil.copyPageData(page1, pageData);

        List<CustomerData> list = pageData.getList();
        return list;
    }

    @Override
    public List<CustomerData> listByType(Long custTypeCode) {
        List<Customer> list = customerService.listByType(custTypeCode);
        List<CustomerData> list1 = new ArrayList<>();
        for (Customer customer : list) {
            CustomerData customerData = new CustomerData();
            CglibBeanCopierUtil.copyProperties(customer, customerData);

            CustomerTypeData customerType = new CustomerTypeData();
            CglibBeanCopierUtil.copyProperties(customer.getCustomerType(), customerType);
            customerData.setCustomerTypeData(customerType);

            BankData bankData = new BankData();
            Bank bank = customer.getBank();
            if (bank == null) bank = new Bank();
            CglibBeanCopierUtil.copyProperties(bank, bankData);
            customerData.setBankData(bankData);

            list1.add(customerData);
        }
        return list1;
    }


    @Override
    public List<CustomerData> listByTypeAndKeyword(Long custTypeCode, String keyword) {
        List<Customer> list = customerService.listByTypeAndKeyword(custTypeCode, keyword);
        List<CustomerData> list1 = new ArrayList<>();
        for (Customer customer : list) {
            CustomerData customerData = new CustomerData();
            CglibBeanCopierUtil.copyProperties(customer, customerData);

            CustomerTypeData customerTypeData = new CustomerTypeData();
            CustomerType customerType = customer.getCustomerType();
            if (customerType != null)
                CglibBeanCopierUtil.copyProperties(customerType, customerTypeData);
            customerData.setCustomerTypeData(customerTypeData);
            BankData bankData = new BankData();
            Bank bank = customer.getBank();
            if (bank == null) bank = new Bank();
            CglibBeanCopierUtil.copyProperties(bank, bankData);
            customerData.setBankData(bankData);

            list1.add(customerData);
        }
        return list1;
    }


    @Override
    public PagingLoadResult<CustomerData> findProducts(Long custTypeCode, String keyword, PagingLoadConfig loadConfig) {
        if (keyword == null) {
            keyword = "";
        }
        List<Customer> list = customerService.listByTypeAndKeyword(custTypeCode, keyword);
        List<CustomerData> list1 = new ArrayList<>();
        for (Customer customer : list) {
            CustomerData customerData = new CustomerData();
            CglibBeanCopierUtil.copyProperties(customer, customerData);

            CustomerTypeData customerTypeData = new CustomerTypeData();
            CustomerType customerType = customer.getCustomerType();
            if (customerType != null)
                CglibBeanCopierUtil.copyProperties(customerType, customerTypeData);
            customerData.setCustomerTypeData(customerTypeData);
            BankData bankData = new BankData();
            Bank bank = customer.getBank();
            if (bank == null) bank = new Bank();
            CglibBeanCopierUtil.copyProperties(bank, bankData);
            customerData.setBankData(bankData);

            list1.add(customerData);
        }

        //loadConfig提取参数
        int offset = loadConfig.getOffset();
        int limit = loadConfig.getLimit();
        String sortField = loadConfig.getSortField();
        String sortType = loadConfig.getSortDir().name();
        String uuid = loadConfig.get("uuid");
        List<CustomerData> returnList = new ArrayList<CustomerData>();
        if (sortField == null) {
            sortField = "productReference";
            sortType = "ASC";
        }
        ComparatorBaseModelData comparator = new ComparatorBaseModelData(sortField, sortType);
        Collections.sort(list1, comparator);

        int count = list1.size();
        for (int i = offset; i < offset + limit; i++) {
            if (i >= count) {
                break;
            }
            CustomerData data = list1.get(i);
            returnList.add(data);
        }
        return new BasePagingLoadResult<CustomerData>(returnList, offset, count);
    }

    @Override
    public PagingLoadResult<CustomerData> listByType(Long custTypeCode, PagingLoadConfig config) {
        List<CustomerData> list = listByType(custTypeCode);
        return getPagingLoadResult(list, config);
    }

    @Override
    public void save(CustomerData customerData) {
        Customer customer = new Customer();
        CglibBeanCopierUtil.copyProperties(customerData, customer);

        CustomerType customerType = new CustomerType();
        CustomerTypeData customerTypeData = customerData.getCustomerTypeData();
        if (customerTypeData != null)
            CglibBeanCopierUtil.copyProperties(customerTypeData, customerType);
        customer.setCustomerType(customerType);

        Bank bank = new Bank();
        BankData bankData = customerData.getBankData();
        if (bankData != null)
            CglibBeanCopierUtil.copyProperties(bankData, bank);
        customer.setBank(bank);

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


    private PagingLoadResult<CustomerData> getPagingLoadResult(List<CustomerData> list,
                                                               PagingLoadConfig config) {
        //定义pageItems，存储Item，作为返回的数据源
        List<CustomerData> pageItems = new ArrayList<CustomerData>();
        //通过PagingLoadConfig，获得相关参数（offset）
        int offset = config.getOffset();
        //获得全部数据大小
        int limit = list.size();
        //根据offset获得limit
        if (config.getLimit() > 0) {
            limit = Math.min(offset + config.getLimit(), limit);
        }
        //定义好边界之后，开始读取数据
        for (int i = offset; i < limit; i++) {
            pageItems.add(list.get(i));
        }
        //通过pageItems，转化成BasePagingLoadResult，同时赋值上offset和totalLength
        return new BasePagingLoadResult<>(pageItems, offset, list.size());

    }
}