package com.mySampleApplication.client.list;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.Constants;
import com.mySampleApplication.client.services.CustomerTypeServiceAsync;
import com.mySampleApplication.shared.model.CustomerType;

import java.util.List;


/**
 * @author zhouguixing
 * @date 2019/4/26 17:42
 * @description
 */
public class CustomerTypeList extends LayoutContainer {
    public CustomerTypeList() {
        setLayout(new FitLayout());
        final CustomerTypeServiceAsync serviceAsync = Registry.get(Constants.CUSTOMER_TYPE_SERVICE);
        serviceAsync.list( new AsyncCallback<List<CustomerType>>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.display("客户类型列表", "加载失败！");
            }
            @Override
            public void onSuccess(List<CustomerType> result) {
                final ListStore<BeanModel> custStore = Registry.get(Constants.CUSTOMER_TYPE_STORE);
                for (CustomerType customerType : result) {
                    BeanModelFactory beanModelFactory = BeanModelLookup.get().getFactory(customerType.getClass());
                    custStore.add(beanModelFactory.createModel(customerType));
                }
            }
        });

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        final ListField<BeanModel> feedList = new ListField<>();
        final ListStore<BeanModel> custStore = Registry.get(Constants.CUSTOMER_STORE);
        feedList.setStore(custStore);
        feedList.setDisplayField("custTypeName");
        add(feedList);
    }
}
