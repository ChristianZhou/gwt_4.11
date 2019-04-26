package com.mySampleApplication.client;

/**
 * @author zhouguixing
 * @date 2019/4/25 18:37
 * @description 分页表格
 */

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.data.CustomerDataModelData;
import com.mySampleApplication.client.data.KeywordPageData;
import com.mySampleApplication.shared.service.CustomerServiceRemoteAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PagingGrid extends LayoutContainer {

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        final CustomerServiceRemoteAsync service = Registry.get(Constants.CUSTOMER_SERVICE);

        FlowLayout layout = new FlowLayout(10);
        setLayout(layout);

        RpcProxy<KeywordPageData> proxy = new RpcProxy<KeywordPageData>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<KeywordPageData> callback) {
//                service.listPage((KeywordPageData) loadConfig, callback);
            }
        };

        // loader
        final PagingLoader<PagingLoadResult<ModelData>> loader = new BasePagingLoader<>(proxy);
        loader.setRemoteSort(true);

        ListStore<CustomerDataModelData> store = new ListStore<>(loader);

        final PagingToolBar toolBar = new PagingToolBar(50);
        toolBar.bind(loader);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig("custCode", "客户代码", 150));
        columns.add(new ColumnConfig("custName", "客户名称", 100));
        columns.add(new ColumnConfig("custType", "客户类型", 200));
//        ColumnConfig date = new ColumnConfig("date", "Date", 100);
//        date.setDateTimeFormat(DateTimeFormat.getFormat("MM/dd/y"));
//        columns.add(date);

        ColumnModel cm = new ColumnModel(columns);

        final Grid<CustomerDataModelData> grid = new Grid<>(store, cm);
        grid.setStateId("pagingGridExample");
        grid.setStateful(true);
        grid.addListener(Events.Attach, new Listener<GridEvent<CustomerDataModelData>>() {
            public void handleEvent(GridEvent<CustomerDataModelData> be) {
                PagingLoadConfig config = new BasePagingLoadConfig();
                config.setOffset(0);
                config.setLimit(50);
                Map<String, Object> state = grid.getState();
                if (state.containsKey("offset")) {
                    int offset = (Integer)state.get("offset");
                    int limit = (Integer)state.get("limit");
                    config.setOffset(offset);
                    config.setLimit(limit);
                }
                if (state.containsKey("sortField")) {
                    config.setSortField((String)state.get("sortField"));
//                    config.setSortDir(SortDir.valueOf((String)state.get("sortDir")));
                }
                loader.load(config);
            }
        });
        grid.setLoadMask(true);
        grid.setBorders(true);
        grid.setAutoExpandColumn("forum");

        ContentPanel panel = new ContentPanel();
        panel.setFrame(true);
        panel.setCollapsible(true);
        panel.setAnimCollapse(false);
//        panel.setIcon(Resources.ICONS.table());
        panel.setHeadingHtml("Paging Grid");
        panel.setLayout(new FitLayout());
        panel.add(grid);
        panel.setSize(600, 350);
        panel.setBottomComponent(toolBar);
        grid.getAriaSupport().setLabelledBy(panel.getId());
        add(panel);
    }

}