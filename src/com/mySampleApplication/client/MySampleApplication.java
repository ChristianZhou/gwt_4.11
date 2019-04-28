package com.mySampleApplication.client;


import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.mySampleApplication.client.services.CustomerServiceRemote;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.client.services.CustomerTypeService;
import com.mySampleApplication.shared.model.CustomerData;

import java.util.List;

public class MySampleApplication implements EntryPoint {


    public void onModuleLoad() {



        GXT.setDefaultTheme(Theme.BLUE, true);

        Registry.register(Constants.CUSTOMER_SERVICE,
                GWT.create(CustomerServiceRemote.class));
        Registry.register(Constants.CUSTOMER_STORE, new
                ListStore<BeanModel>());
        Registry.register(Constants.CUSTOMER_TYPE_SERVICE,
                GWT.create(CustomerTypeService.class));
        Registry.register(Constants.CUSTOMER_TYPE_STORE, new
                ListStore<BeanModel>());





        Viewport viewport = new Viewport();
//        布局
        final BorderLayout borderLayout = new BorderLayout();
        viewport.setLayout(borderLayout);

//        NORTH
        BorderLayoutData northData = new BorderLayoutData(Style.LayoutRegion.NORTH,20);
        northData.setCollapsible(false);
        northData.setSplit(false);


//        CENTER
        BorderLayoutData centerData = new BorderLayoutData(Style.LayoutRegion.CENTER);
        centerData.setCollapsible(false);

//        WEST
        BorderLayoutData westData = new BorderLayoutData(Style.LayoutRegion.WEST, 200, 150, 300);
        westData.setCollapsible(true);
        westData.setSplit(true);

//        内容
        ContentPanel mainPanel = new MainPanel();
        ContentPanel navPanel = new NavigationPanel();
        HTML headerHtml = new HTML();
        headerHtml.setHTML("客户信息维护");

//        内容添加至布局
        viewport.add(headerHtml, northData);
        viewport.add(navPanel, westData);
        viewport.add(mainPanel, centerData);

//        渲染
        RootPanel.get().add(viewport);
    }

}
