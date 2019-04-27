package com.mySampleApplication.client.grids;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.mySampleApplication.client.Constants;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;

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
        columns.add(new ColumnConfig("custCode", "客户代码", 200));
        columns.add(new ColumnConfig("custName", "客户名称", 200));
        columns.add(new ColumnConfig("mnemonicCode", "助记码", 200));
        columns.add(new ColumnConfig("cusCustTypeCode", "客户类型", 200));
        columns.add(new ColumnConfig("tel", "电话", 200));
        columns.add(new ColumnConfig("fax", "传真", 200));
        columns.add(new ColumnConfig("email", "EMail", 200));
        columns.add(new ColumnConfig("tag", "启用标记", 200));

        final ColumnModel columnModel = new ColumnModel(columns);
        final CustomerServiceRemoteAsync customerServiceRemoteAsync = Registry.get(Constants.CUSTOMER_SERVICE);
//        RpcProxy<List<CustomerModel>> proxy = new RpcProxy<List<CustomerModel>>() {
//            @Override
//            protected void load(Object loadConfig,
//                                AsyncCallback<List<CustomerModel>> callback) {
//                customerServiceRemoteAsync.list(new KeywordPageData(),callback);
//            }
//        };
//        ListLoader<ListLoadResult<BeanModel>> loader = new BaseListLoader<>(proxy);
//
//        ListStore<BeanModel> store = new ListStore<>(loader);
//
//        Grid<BeanModel> grid = new Grid<>(store, columnModel);
//        grid.setBorders(true);
//        grid.setAutoExpandColumn("custName");
//
//        loader.load();
//        add(grid);

    }
}
