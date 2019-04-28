package com.mySampleApplication.client.list;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.Constants;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.client.services.CustomerTypeServiceAsync;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.CustomerTypeData;

import java.util.List;


/**
 * @author zhouguixing
 * @date 2019/4/26 17:42
 * @description     客户类型列表
 */
public class CustomerTypeList extends LayoutContainer {
    public CustomerTypeList() {
        setLayout(new FitLayout());
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        final ListField<BeanModel> listField = new ListField<>();

        final CustomerTypeServiceAsync serviceAsync = Registry.get(Constants.CUSTOMER_TYPE_SERVICE);

        RpcProxy<List<CustomerTypeData>> proxy = new RpcProxy<List<CustomerTypeData>>() {
            @Override
            protected void load(Object loadConfig,AsyncCallback<List<CustomerTypeData>> callback) {
                serviceAsync.list(callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();
        ListLoader<ListLoadResult<BeanModel>> loader = new BaseListLoader<>(proxy, reader);
        ListStore<BeanModel> feedStore = new ListStore<>(loader);
        listField.setStore(feedStore);
        listField.setDisplayField("custTypeName");

        listField.addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                CustomerTypeData customerTypeData = se.getSelection().get(0).getBean();
                final CustomerServiceRemoteAsync serviceAsync = Registry.get(Constants.CUSTOMER_SERVICE);
                serviceAsync.listByType(customerTypeData.getCustTypeCode(), new AsyncCallback<List<CustomerData>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.display("客户列表", "加载失败！");
                    }
                    @Override
                    public void onSuccess(List<CustomerData> result) {
                        CustomerTypeData customerTypeData1 = new CustomerTypeData();
                        customerTypeData1.setCustTypeName("zhou ");
                        customerTypeData1.setCustTypeCode(1L);
                        result.get(0).setCustomerTypeData(customerTypeData1);
                        ListStore<BeanModel> custStore = Registry.get(Constants.CUSTOMER_STORE);
                        custStore.removeAll();
                        for (CustomerData customerData : result) {
                            BeanModelFactory beanModelFactory = BeanModelLookup.get().getFactory(customerData.getClass());
                            custStore.add(beanModelFactory.createModel(customerData));
                        }
                    }
                });
            }
        });

        loader.load();

        add(listField);
    }
}
