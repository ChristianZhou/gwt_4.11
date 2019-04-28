package com.mySampleApplication.client.grids;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.Constants;
import com.mySampleApplication.client.model.CustomerTypeModel;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.server.services.CglibBeanCopierUtil;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.CustomerTypeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouguixing
 * @date 2019/4/26 23:39
 * @description
 */
public class CustomerGrid extends LayoutContainer {
    //    private  CustomerData customerData;
//    private Grid<ModelData> grid;
    public CustomerGrid() {
        setLayout(new FitLayout());
//        this.customerData = customerData;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        final List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig("custCode", "客户代码", 100));
        columns.add(new ColumnConfig("custName", "客户名称", 100));
        columns.add(new ColumnConfig("mnemonicCode", "助记码", 100));
        ColumnConfig e = new ColumnConfig("customerTypeData", "客户类型", 100);
        e.setRenderer(new GridCellRenderer() {
            @Override
            public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                return ((ModelData)model.get(property)).get("custTypeName");
            }
        });
        columns.add(e);
        columns.add(new ColumnConfig("tel", "电话", 100));
        columns.add(new ColumnConfig("fax", "传真", 100));
        columns.add(new ColumnConfig("email", "EMail", 100));
        columns.add(new ColumnConfig("tag", "启用标记", 100));

        final ColumnModel columnModel = new ColumnModel(columns);
        final ListStore<BeanModel> custStore = Registry.get(Constants.CUSTOMER_STORE);

        Grid<BeanModel> grid = new Grid<>(custStore, columnModel);
        grid.setBorders(true);
//        grid.setAutoExpandColumn("customerTypeData");

        add(grid);

    }
}
