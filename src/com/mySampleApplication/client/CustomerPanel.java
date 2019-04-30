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
import com.mySampleApplication.server.services.CglibBeanCopierUtil;
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


    CustomerTypeServiceAsync customerTypeServiceAsync = Registry.get(Constants.CUSTOMER_TYPE_SERVICE);
    final CustomerServiceRemoteAsync customerServiceRemoteAsync = Registry.get(Constants.CUSTOMER_SERVICE);
    RpcProxy<List<CustomerTypeData>> proxy = new RpcProxy<List<CustomerTypeData>>() {
        @Override
        protected void load(Object loadConfig, AsyncCallback<List<CustomerTypeData>> callback) {
            customerTypeServiceAsync.list(callback);
        }
    };
    ListLoader<ListLoadResult<CustomerTypeData>> customerTypeDataLoader = new BaseListLoader<>(proxy);
    ListStore<CustomerTypeData> customerTypeDataListStore = new ListStore<>(customerTypeDataLoader);


    DetailCustomerTab detailCustomer = new DetailCustomerTab();

    MainCustomerTab mainCustomerTab = new MainCustomerTab();

    LayoutContainer layoutContainer = new LayoutContainer();
    CardLayout cardLayout = new CardLayout();
    final ListField<CustomerTypeData> customerTypeDataListField = new ListField<>();
    Long customerType = null;
    private TextField<String> keywordTxtField = new TextField<>();

    public class MainCustomerTab extends LayoutContainer {
        final ListStore<BeanModel> custStore = Registry.get(Constants.CUSTOMER_STORE);
        final List<ColumnConfig> columns = new ArrayList<>();
        ColumnModel columnModel;

        Grid<BeanModel> grid;


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


                    columnModel = new ColumnModel(columns);
                    grid = new Grid<>(custStore, columnModel);
                    grid.setBorders(true);
//                    grid.setAutoExpandColumn("email");
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
                        if (customerTypeDataListField.getValue() != null) {
                            Long customerTypeCOde = customerTypeDataListField.getValue().getCustTypeCode();
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
                btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
//                        Window.alert("开发中");
                        final BeanModel model = grid.getSelectionModel().getSelectedItem();
//                        Window.alert(model.getProperties().toString());
                        if (model != null) {
                            Object customerCodeObj = model.get("custCode");
                            if (customerCodeObj != null) {
                                Long customerCodeL = Long.valueOf(customerCodeObj.toString());
                                customerServiceRemoteAsync.delete(customerCodeL, new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        Info.display("错误", "删除客户" + model.get("custName") + "失败");
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        Info.display("成功", "删除客户" + model.get("custName") + "成功");
                                        if (customerTypeDataListField.getValue() != null) {
                                            Object customerTypeCOde = customerTypeDataListField.getValue().get("custTypeCode");
                                            if (customerTypeCOde != null) {
                                                customerType = Long.parseLong(customerTypeCOde.toString());
                                            }
                                        }
                                        getCustomerData(customerType, keywordTxtField.getValue());
                                    }
                                });
                            } else {
                                Info.display("错误", "客户主键为空");
                            }
                        }
                    }
                });

//        顶部按钮条
                final ToolBar btnBar = new ToolBar();
                btnBar.add(btnRefresh);
                btnBar.add(btnAdd);
                btnBar.add(btnDelete);

                setTopComponent(btnBar);
                setBottomComponent(pageBar);
                add(new CustomerGrid());
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
                    customerTypeDataListField.setStore(customerTypeDataListStore);
                    customerTypeDataListField.setDisplayField("custTypeName");
                    customerTypeDataListField.addSelectionChangedListener(new SelectionChangedListener<CustomerTypeData>() {
                        @Override
                        public void selectionChanged(SelectionChangedEvent<CustomerTypeData> se) {
                            CustomerTypeData selectedItem = se.getSelectedItem();
                            if (selectedItem != null) {
                                Long custTypeCode = selectedItem.getCustTypeCode();
                                getCustomerData(custTypeCode, keywordTxtField.getValue());
                            }else {
                                getCustomerData(null, keywordTxtField.getValue());
                            }
                        }
                    });
                    add(customerTypeDataListField);
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
            northData.setCollapsible(true);

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


    }

    public void getCustomerData(Long customerType, String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        customerServiceRemoteAsync.listByTypeAndKeyword(customerType, keyword, new AsyncCallback<List<CustomerData>>() {
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

    public class DetailCustomerTab extends LayoutContainer {

        private final TextField<String> tfCustomerCode = new TextField<>();
        private final TextField<String> tfCustomerName = new TextField<>();
        private final TextField<String> tfMnemonicCode = new TextField<>();
        private final TextField<String> tfCustomerTypeData = new TextField<>();
        private final TextField<String> tfTel = new TextField<>();
        private final TextField<String> tfFax = new TextField<>();
        private final TextField<String> tfEmail = new TextField<>();
        private final TextField<String> tfAddress = new TextField<>();
        private final TextField<String> tfTag = new TextField<>();
        private final CheckBoxGroup checkGroup = new CheckBoxGroup();
        private final CheckBox checkBoxTag = new CheckBox();
        private final ComboBox<CustomerTypeData> cbCustomerTypeData = new ComboBox<>();

        private final TextField<String> tfWorkUnit = new TextField<>();
        private final TextField<String> tfPostcode = new TextField<>();
        private final TextField<String> tfBankDataCode = new TextField<>();
        private final TextField<String> tfBankAccount = new TextField<>();
        private final TextField<String> tfSetSettlementMethodCode = new TextField<>();
        private final DateField dfBirthDay = new DateField();
        private final DateField dfSettlementDate = new DateField();
        private final DateField dfMonthlySettlementDate = new DateField();
        private final TextArea taRemark = new TextArea();

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

        public class BasicInfoPanel extends FormPanel {
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);
                setHeadingText("基本信息");
                setCollapsible(true);
                setLayout(new ColumnLayout());
                BasicInfoPanel.this.setWidth(800);

                LayoutContainer left = new LayoutContainer();
                left.setStyleAttribute("paddingRight", "10px");
                FormLayout layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                left.setLayout(layout);

                tfCustomerCode.setFieldLabel("客户代码");
                tfMnemonicCode.setFieldLabel("助记码");
                tfTel.setFieldLabel("电话");
                tfEmail.setFieldLabel("EMail");
                checkGroup.setFieldLabel("启用标记");
                checkBoxTag.setBoxLabel("启用");
                checkGroup.add(checkBoxTag);

                left.add(tfCustomerCode);
                left.add(tfMnemonicCode);
                left.add(tfTel);
                left.add(tfEmail);
                left.add(checkGroup);

                LayoutContainer right = new LayoutContainer();
                right.setStyleAttribute("paddingLeft", "10px");
                layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                right.setLayout(layout);
                tfCustomerName.setFieldLabel("客户名称");
                tfFax.setFieldLabel("传真");
                tfAddress.setFieldLabel("联系地址");
                cbCustomerTypeData.setFieldLabel("客户类型");
                cbCustomerTypeData.setDisplayField("custTypeName");
//                cbCustomerTypeData.setTriggerAction(ComboBox.TriggerAction.ALL);
                cbCustomerTypeData.setStore(customerTypeDataListStore);

                right.add(tfCustomerName);
                right.add(tfFax);
                right.add(tfAddress);
                right.add(cbCustomerTypeData);

                add(left, new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
                add(right, new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
            }
        }

        public class DetailInfoPanel extends FormPanel {
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);
                DetailInfoPanel.this.setWidth(800);
                setHeadingText("详细信息");
                setCollapsible(true);
                setLayout(new ColumnLayout());

                LayoutContainer left = new LayoutContainer();
                left.setStyleAttribute("paddingRight", "10px");
                FormLayout layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                left.setLayout(layout);

                tfWorkUnit.setFieldLabel("工作单位");
                tfPostcode.setFieldLabel("邮编");
                tfBankDataCode.setFieldLabel("银行");
                dfSettlementDate.setFieldLabel("结账日期");
                taRemark.setFieldLabel("备注");

                left.add(tfWorkUnit);
                left.add(tfPostcode);
                left.add(tfBankDataCode);
                left.add(dfSettlementDate);
                left.add(taRemark);

                LayoutContainer right = new LayoutContainer();
                right.setStyleAttribute("paddingLeft", "10px");
                layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                right.setLayout(layout);

                dfBirthDay.setFieldLabel("生日");
                tfBankAccount.setFieldLabel("银行账号");
                tfSetSettlementMethodCode.setFieldLabel("结算方式");
                dfMonthlySettlementDate.setFieldLabel("月结日期");

                right.add(dfBirthDay);
                right.add(tfBankAccount);
                right.add(tfSetSettlementMethodCode);
                right.add(dfMonthlySettlementDate);

                add(left, new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
                add(right, new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
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
                customerData.setTag(checkBoxTag.getValue()?1:0);
            final CustomerServiceRemoteAsync customerService = Registry
                    .get(Constants.CUSTOMER_SERVICE);
            customerService.save(customerData, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.display("提示", "操作失败");
                }

                @Override
                public void onSuccess(Void result) {
                    if (customerTypeDataListField.getValue() != null) {
                        Object customerTypeCOde = customerTypeDataListField.getValue().get("custTypeCode");
                        if (customerTypeCOde != null) {
                            customerType = Long.parseLong(customerTypeCOde.toString());
                        }
                    }
                    getCustomerData(customerType, keywordTxtField.getValue());
                    Info.display("提示", "新增客户" + customerData.getCustName() + "成功");
                    cardLayout.setActiveItem(mainCustomerTab);
                }
            });

        }

        @Override
        protected void onRender(Element parent, int index) {
            super.onRender(parent, index);
            setLayout(new RowLayout());
            add(new ButtonPanel());
            add(new BasicInfoPanel());
            add(new DetailInfoPanel());
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
        customerTypeDataLoader.load();
    }
}
