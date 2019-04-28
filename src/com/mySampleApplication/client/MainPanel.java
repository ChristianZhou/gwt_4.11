package com.mySampleApplication.client;

/**
 * @author zhouguixing
 * @date 2019/4/26 12:09
 * @description
 */

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.grids.CustomerGrid;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.client.window.CustomerWindow;
import com.mySampleApplication.shared.model.CustomerData;

import java.util.ArrayList;
import java.util.List;

public class MainPanel extends ContentPanel
{
    public MainPanel()
    {
        final ToggleButton btnCreate = new ToggleButton("create");
        btnCreate.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                createNewFeedWindow();
            }
        });
        addButton(btnCreate);


        final List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig("title", "Title", 200));
        columns.add(new ColumnConfig("description", "Description", 200));

        setLayout(new FitLayout());

        add(new CustomerGrid());
//        add(new CustomerTypeList());
    }

    private void createNewFeedWindow() {
        final CustomerServiceRemoteAsync customerServiceRemote =
                Registry.get(Constants.CUSTOMER_SERVICE);
        customerServiceRemote.createNewCustomer(new AsyncCallback<CustomerData>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.display("新增各户", "无法打开窗口");
            }
            @Override
            public void onSuccess(CustomerData customerData) {
                final Window newFeedWindow = new CustomerWindow(customerData);
                newFeedWindow.show();
            }
        });
    }
}