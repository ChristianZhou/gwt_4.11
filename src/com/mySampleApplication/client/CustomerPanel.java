package com.mySampleApplication.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.client.services.CustomerTypeServiceAsync;
import com.mySampleApplication.shared.model.BankData;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.CustomerTypeData;

import javax.validation.constraints.NotNull;
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

    CustomerTypeServiceAsync serviceAsync = Registry.get(Constants.CUSTOMER_TYPE_SERVICE);

    RpcProxy<List<CustomerTypeData>> proxy = new RpcProxy<List<CustomerTypeData>>() {
        @Override
        protected void load(Object loadConfig, AsyncCallback<List<CustomerTypeData>> callback) {
            serviceAsync.list(callback);
        }
    };
    ListLoader<ListLoadResult<CustomerTypeData>> loader = new BaseListLoader<>(proxy);

    DetailCustomerTab detailCustomer = new DetailCustomerTab();

    MainCustomerTab mainCustomerTab = new MainCustomerTab();

    LayoutContainer layoutContainer = new LayoutContainer();
    CardLayout cardLayout = new CardLayout();
    final ListField<CustomerTypeData> listField = new ListField<>();

    public class MainCustomerTab extends LayoutContainer{
        private TextField<String> keywordTxtField = new TextField<>();

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
                Button btnAdd = new Button("新增");
                btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        cardLayout.setActiveItem(detailCustomer);
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
        }

        public class LeftPanel extends ContentPanel {
            public class CustomerTypeList extends LayoutContainer {
                public CustomerTypeList() {
                    setLayout(new FitLayout());
                }

                @Override
                protected void onRender(Element parent, int index) {
                    super.onRender(parent, index);
                    ListStore<CustomerTypeData> feedStore = new ListStore<>(loader);
                    listField.setStore(feedStore);
                    listField.setDisplayField("custTypeName");
                    listField.addSelectionChangedListener(new SelectionChangedListener<CustomerTypeData>() {
                        @Override
                        public void selectionChanged(SelectionChangedEvent<CustomerTypeData> se) {
                            CustomerTypeData customerTypeData = se.getSelection().get(0);
                            getCustomerData(customerTypeData.getCustTypeCode(), keywordTxtField.getValue());
                        }
                    });
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

        private final TextField<String> tfCustomerCode = new TextField<>();
        private final TextField<String> tfCustomerName = new TextField<>();
        private final TextField<String> tfMnemonicCode= new TextField<>();
        private final TextField<String> tfCustomerTypeData = new TextField<>();
        private final TextField<String> tfTel = new TextField<>();
        private final TextField<String> tfFax = new TextField<>();
        private final TextField<String> tfEmail = new TextField<>();
        private final TextField<String> tfAddress = new TextField<>();
        private final TextField<String> tfTag = new TextField<>();
        private final ComboBox<CustomerTypeData> cbCustomerTypeData = new ComboBox<>();
        public class ButtonPanel extends ContentPanel {
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);
                setHeadingText("新增客户");
                setLayout(new ColumnLayout());
                Button btnSave = new Button("保存");
                btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        saveCustomer();
                    }
                });
                Button btnBack = new Button("返回");
                btnBack.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        cardLayout.setActiveItem(mainCustomerTab);
                    }
                });
                add(btnSave);
                add(btnBack);
            }
        }
        public class BasicIntoPanel extends FormPanel {
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);
                setHeadingText("基本信息");
                setCollapsible(true);
                setLayout(new ColumnLayout());

                LayoutContainer left = new LayoutContainer();
                left.setStyleAttribute("paddingRight", "10px");
                FormLayout layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                left.setLayout(layout);

                tfCustomerCode .setFieldLabel("客户代码");
                tfMnemonicCode.setFieldLabel("助记码");
                tfTel .setFieldLabel("电话");
                tfEmail .setFieldLabel("EMail");
                tfTag .setFieldLabel("启用标记");
                left.add(tfCustomerCode);
                left.add(tfMnemonicCode);
                left.add(tfTel);
                left.add(tfEmail);
                left.add(tfTag);

                LayoutContainer right = new LayoutContainer();
                right.setStyleAttribute("paddingLeft", "10px");
                layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                right.setLayout(layout);
                tfCustomerName .setFieldLabel("客户名称");
                tfFax .setFieldLabel("传真");
                tfAddress .setFieldLabel("联系地址");
                cbCustomerTypeData.setFieldLabel("客户类型");
                cbCustomerTypeData.setDisplayField("custTypeName");
                cbCustomerTypeData.setTriggerAction(ComboBox.TriggerAction.ALL);
                ListStore<CustomerTypeData> listStore = new ListStore<>(loader);
                cbCustomerTypeData.setStore(listStore);

                right.add(tfCustomerName);
                right.add(tfFax);
                right.add(tfAddress);
                right.add(cbCustomerTypeData);

                add(left,new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
                add(right,new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
            }
        }

        public void saveCustomer() {
            final CustomerData customerData = new CustomerData();
            if (tfCustomerCode.getValue() != null) {
                customerData.setCustCode(Long.parseLong(tfCustomerCode.getValue()));
            }
            customerData.setCustName(tfCustomerName.getValue());
            customerData.setMnemonicCode(tfMnemonicCode.getValue());
            customerData.setCustomerTypeData(cbCustomerTypeData.getValue());
            customerData.setTel(tfTel.getValue());
            customerData.setFax(tfFax.getValue());
            customerData.setEmail(tfEmail.getValue());
            customerData.setAddress(tfAddress.getValue());
            if (tfTag.getValue() != null) {
                customerData.setTag(Integer.parseInt(tfTag.getValue()));
            }
            final CustomerServiceRemoteAsync customerService = Registry
                    .get(Constants.CUSTOMER_SERVICE);
            customerService.save(customerData, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.display("提示","操作失败");
                }
                @Override
                public void onSuccess(Void result) {
                    Info.display("提示","新增客户" + customerData.getCustName()+"成功");
                    cardLayout.setActiveItem(mainCustomerTab);
                }
            });

        }

        @Override
        protected void onRender(Element parent, int index) {
            super.onRender(parent, index);
            setLayout(new RowLayout());
            add(new ButtonPanel());
            add(new BasicIntoPanel());
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

        tapInfo.add(tabMain);

        setLayout(new FitLayout());
        add(tapInfo);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        loader.load();

    }
}
