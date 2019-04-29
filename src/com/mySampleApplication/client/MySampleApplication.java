package com.mySampleApplication.client;


import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.mySampleApplication.client.services.CustomerServiceRemote;
import com.mySampleApplication.client.services.CustomerTypeService;

public class MySampleApplication implements EntryPoint {

    public void onModuleLoad() {

        GXT.setDefaultTheme(Theme.BLUE, true);

        Registry.register(Constants.CUSTOMER_SERVICE,
                GWT.create(CustomerServiceRemote.class));
        Registry.register(Constants.CUSTOMER_STORE, new
                ListStore<BeanModel>());
        Registry.register(Constants.CUSTOMER_TYPE_SERVICE,
                GWT.create(CustomerTypeService.class));

        Viewport viewport = new Viewport();
        viewport.setLayout(new FitLayout());

        viewport.add(new CustomerPanel());
//        渲染
        RootPanel.get().add(viewport);
    }
}
