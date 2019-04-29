package com.mySampleApplication.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.ClickRepeater;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.client.services.CustomerTypeServiceAsync;
import com.mySampleApplication.client.window.CustomerWindow;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.CustomerTypeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouguixing
 * @date 2019/4/28 18:56
 * @description 客户信息维护
 */

public class CustomerPanel extends LayoutContainer {

    private TabPanel tapInfo = new TabPanel();

    private FormPanel fPanelDetail;

    TabItem tabMain = new TabItem();
    TabItem tabItem = new TabItem();

    Button btnAdd = new Button("新增");
    Button btnBack = new Button("返回");

    DetailCustomerTab detailCustomer = new DetailCustomerTab();

    MainCustomerTab mainCustomerTab = new MainCustomerTab();


    LayoutContainer layoutContainer = new LayoutContainer();
    CardLayout cardLayout = new CardLayout();

    public class MainCustomerTab extends LayoutContainer{
        private TextField<String> keywordTxtField = new TextField<>();
        final ListField<BeanModel> listField = new ListField<>();

        public class TopPanel extends ContentPanel {
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);
                FormPanel formPanel = new FormPanel();
                FormLayout layout = new FormLayout();
                formPanel.setHeaderVisible(false);
                setHeadingText("查询条件");
                layout.setLabelWidth(150);
                layout.setLabelAlign(FormPanel.LabelAlign.RIGHT);
                layout.setLabelPad(20);
                formPanel.setLayout(layout);
                keywordTxtField.setName("keyword");
                keywordTxtField.setFieldLabel("输入代码、助记符、名称");
                keywordTxtField.setWidth(200);
                formPanel.add(keywordTxtField);
                add(formPanel);
            }
        }

        public class CenterPanel extends ContentPanel {
            public class CustomerGrid extends LayoutContainer {
                @Override
                protected void onRender(Element parent, int index) {
                    super.onRender(parent, index);
                    setLayout(new FitLayout());
                    final List<ColumnConfig> columns = new ArrayList<>();
                    columns.add(new ColumnConfig("custCode", "客户代码", 100));
                    columns.add(new ColumnConfig("custName", "客户名称", 100));
                    columns.add(new ColumnConfig("mnemonicCode", "助记码", 100));
                    ColumnConfig e = new ColumnConfig("customerTypeData", "客户类型", 100);
                    e.setRenderer(new GridCellRenderer() {
                        @Override
                        public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                            return ((ModelData) model.get(property)).get("custTypeName");
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
                    grid.setAutoExpandColumn("email");

                    add(grid);

                }
            }

            public CenterPanel() {
//        刘海不可见
                setHeaderVisible(false);

//        布局
                setLayout(new FitLayout());

//        底部分页条
                final PagingToolBar pageBar = new PagingToolBar(5);

//       刷新按钮
                Button btnRefresh = new Button("刷新");
                btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
//                    com.google.gwt.user.client.Window.alert(listField.getValue().get("custTypeCode")+"开发中");
                        Long customerType = null;
                        if (listField.getValue() != null) {
                            Object customerTypeCOde = listField.getValue().get("custTypeCode");
                            if (customerTypeCOde != null) {
                                customerType = Long.parseLong(customerTypeCOde.toString());
                            }
                        }
                        getCustomerData(customerType, keywordTxtField.getValue());
                    }
                });

//        新增按钮

                btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
//                        tapInfo.setSelection(tabItem);
                        cardLayout.setActiveItem(detailCustomer);
//                        createNewFeedWindow();
                    }
                });

//        删除按钮
                Button btnDelete = new Button("删除");

//        顶部按钮条
                final ToolBar btnBar = new ToolBar();
                btnBar.add(btnRefresh);
                btnBar.add(btnAdd);
                btnBar.add(btnDelete);

                setTopComponent(btnBar);
                setBottomComponent(pageBar);
                add(new CenterPanel.CustomerGrid());
            }

            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);

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

        public class LeftPanel extends ContentPanel {
            public class CustomerTypeList extends LayoutContainer {
                public CustomerTypeList() {
                    setLayout(new FitLayout());
                }

                @Override
                protected void onRender(Element parent, int index) {
                    super.onRender(parent, index);

                    final CustomerTypeServiceAsync serviceAsync = Registry.get(Constants.CUSTOMER_TYPE_SERVICE);

                    RpcProxy<List<CustomerTypeData>> proxy = new RpcProxy<List<CustomerTypeData>>() {
                        @Override
                        protected void load(Object loadConfig, AsyncCallback<List<CustomerTypeData>> callback) {
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
                            getCustomerData(customerTypeData.getCustTypeCode(), keywordTxtField.getValue());
                        }
                    });

                    loader.load();

                    add(listField);
                }
            }

            public LeftPanel() {

//        Fit布局
                setLayout(new FitLayout());

//        标题栏
                setHeadingText("客户类型");

//        显示内容
                add(new LeftPanel.CustomerTypeList());
            }
        }

        @Override
        protected void onRender(Element parent, int pos) {
            super.onRender(parent, pos);
            setLayout(new BorderLayout());

//        NORTH
            BorderLayoutData northData = new BorderLayoutData(Style.LayoutRegion.NORTH, 70);
            northData.setCollapsible(false);

//        CENTER
            BorderLayoutData centerData = new BorderLayoutData(Style.LayoutRegion.CENTER);
            centerData.setCollapsible(false);

//        WEST
            BorderLayoutData westData = new BorderLayoutData(Style.LayoutRegion.WEST);
            westData.setCollapsible(true);
            westData.setSplit(true);

//        内容
            CenterPanel centerPanel = new CenterPanel();
            ContentPanel navPanel = new LeftPanel();
            TopPanel topPanel = new TopPanel();
            add(navPanel, westData);
            add(centerPanel, centerData);
            add(topPanel, northData);
        }

        public void getCustomerData(Long customerType, String keyword) {
            if (keyword == null) {
                keyword = "";
            }
            final CustomerServiceRemoteAsync serviceAsync = Registry.get(Constants.CUSTOMER_SERVICE);
            serviceAsync.listByTypeAndKeyword(customerType, keyword, new AsyncCallback<List<CustomerData>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.display("客户列表", "加载失败！");
                }

                @Override
                public void onSuccess(List<CustomerData> result) {
                    ListStore<BeanModel> custStore = Registry.get(Constants.CUSTOMER_STORE);
                    custStore.removeAll();
                    for (CustomerData customerData : result) {
                        BeanModelFactory beanModelFactory = BeanModelLookup.get().getFactory(customerData.getClass());
                        custStore.add(beanModelFactory.createModel(customerData));
                    }
                }
            });
        }
    }

    public class DetailCustomerTab extends LayoutContainer{

        public class ButtonPanel extends ContentPanel {
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);
                setHeaderVisible(false);
                setLayout(new ColumnLayout());
                Button btnSave = new Button("保存");
                btnBack.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
//                        tapInfo.setSelection(tapInfo.getItem(0));
                        cardLayout.setActiveItem(mainCustomerTab);
                    }
                });
                add(btnSave);
                add(btnBack);
            }
        }



        @Override
        protected void onRender(Element parent, int index) {
            super.onRender(parent, index);
            setLayout(new RowLayout());
            add(new ButtonPanel());
        }
    }
    public CustomerPanel() {
        tabMain.setScrollMode(Style.Scroll.NONE);
        tabMain.setBorders(false);
        tabMain.getHeader().setText("客户信息维护");
        tabMain.getHeader().setBorders(false);
        tabMain.setLayout(new RowLayout());

        layoutContainer.setLayout(cardLayout);
        layoutContainer.add(mainCustomerTab);
        layoutContainer.add(detailCustomer);

        tabMain.add(layoutContainer, new RowData(1, 1));
//        tabMain.add(new MainCustomerTab(), new RowData(1, 1));

//        tabItem.setScrollMode(Style.Scroll.NONE);
//        tabItem.setBorders(false);
//        tabItem.getHeader().hide();
//        tabItem.getHeader().setBorders(false);
//        tabItem.setLayout(new RowLayout());
//        tabItem.add(new DetailCustomerTab(), new RowData(1, 1));

        tapInfo.add(tabMain);
//        tapInfo.add(tabItem);
        setLayout(new FitLayout());
        add(tapInfo);
    }
}
