package com.mySampleApplication.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.list.CustomerTypeList;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.shared.model.CustomerData;

import java.util.List;

public class NavigationPanel extends ContentPanel {
    public NavigationPanel() {

//        Fit布局
        setLayout(new FitLayout());

//        标题栏
        setHeadingText("客户类型");


//        显示内容
        add(new CustomerTypeList());





    }


}