package com.mySampleApplication.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
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
import com.mySampleApplication.client.model.CustomerModel;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.client.services.CustomerTypeServiceAsync;
import com.mySampleApplication.server.services.CglibBeanCopierUtil;
import com.mySampleApplication.shared.model.BankData;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.CustomerTypeData;

import javax.validation.constraints.NotNull;
import java.sql.Date;
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

    public class DetailCustomerTab extends FormPanel {

        private final TextField<String> custCode = new TextField<>();
        private final TextField<String> custName = new TextField<>();
        private final TextField<String> mnemonicCode = new TextField<>();
        private final TextField<String> tel = new TextField<>();
        private final TextField<String> fax = new TextField<>();
        private final TextField<String> email = new TextField<>();
        private final TextField<String> address = new TextField<>();
        private final CheckBoxGroup checkGroup = new CheckBoxGroup();
        private final CheckBox tag = new CheckBox();
        private final ComboBox<CustomerTypeData> customerTypeData = new ComboBox<>();

        private final TextField<String> workUnitCode = new TextField<>();
        private final TextField<String> postcode = new TextField<>();
        private final TextField<String> bankData = new TextField<>();
        private final TextField<String> bankAccount = new TextField<>();
        private final TextField<String> setSettlementMethodCode = new TextField<>();
        private final DateField birthday = new DateField();
        private final DateField settlementDate = new DateField();
        private final DateField monthlySettlementDate = new DateField();
        private final TextArea remark = new TextArea();



        CustomerData customerData = new CustomerData();
        FormBinding binding = new FormBinding(this);

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

                custCode.setFieldLabel("客户代码");
                mnemonicCode.setFieldLabel("助记码");
                tel.setFieldLabel("电话");
                email.setFieldLabel("EMail");
                checkGroup.setFieldLabel("启用标记");
                tag.setBoxLabel("启用");
                checkGroup.add(tag);

                left.add(custCode);
                left.add(mnemonicCode);
                left.add(tel);
                left.add(email);
                left.add(checkGroup);

                LayoutContainer right = new LayoutContainer();
                right.setStyleAttribute("paddingLeft", "10px");
                layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                right.setLayout(layout);
                custName.setFieldLabel("客户名称");
                fax.setFieldLabel("传真");
                address.setFieldLabel("联系地址");
                customerTypeData.setFieldLabel("客户类型");
                customerTypeData.setDisplayField("custTypeName");
//                cbCustomerTypeData.setTriggerAction(ComboBox.TriggerAction.ALL);
                customerTypeData.setStore(customerTypeDataListStore);

                right.add(custName);
                right.add(fax);
                right.add(address);
                right.add(customerTypeData);

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

                workUnitCode.setFieldLabel("工作单位");
                postcode.setFieldLabel("邮编");
                bankData.setFieldLabel("银行");
                settlementDate.setFieldLabel("结账日期");
                remark.setFieldLabel("备注");

                left.add(workUnitCode);
                left.add(postcode);
                left.add(bankData);
                left.add(settlementDate);
                left.add(remark);

                LayoutContainer right = new LayoutContainer();
                right.setStyleAttribute("paddingLeft", "10px");
                layout = new FormLayout();
                layout.setLabelAlign(LabelAlign.RIGHT);
                right.setLayout(layout);

                birthday.setFieldLabel("生日");
                bankAccount.setFieldLabel("银行账号");
                setSettlementMethodCode.setFieldLabel("结算方式");
                monthlySettlementDate.setFieldLabel("月结日期");

                right.add(birthday);
                right.add(bankAccount);
                right.add(setSettlementMethodCode);
                right.add(monthlySettlementDate);

                add(left, new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
                add(right, new com.extjs.gxt.ui.client.widget.layout.ColumnData(.5));
            }
        }

        public void saveCustomer() {
//            if (tfCustomerCode.getValue() != null) {
//                customerData.setCustCode(Long.parseLong(tfCustomerCode.getValue()));
//            }
//            customerModel.setCustName(tfCustomerName.getValue());
//            customerModel.setMnemonicCode(tfMnemonicCode.getValue());
//            customerData.setCustomerTypeData(cbCustomerTypeData.getValue());
//            customerModel.setTel(tfTel.getValue());
//            customerModel.setFax(tfFax.getValue());
//            customerModel.setEmail(tfEmail.getValue());
//            customerModel.setAddress(tfAddress.getValue());
//            customerModel.setTag(checkBoxTag.getValue()?1:0);
//            customerModel.setBirthday(new Date(dfBirthDay.getValue().getTime()));
//            Window.alert(customerData.getProperties().toString());
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
                    Info.display("提示", "新增客户" + customerData.get("custName") + "成功");
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


//            StringBuilder stringBuilder = new StringBuilder();
//            for (Field<?> field : this.getFields()) {
//                stringBuilder.append(field.getId() + field.getName() + field.getValue()+"\n");
//            }
//            Window.alert(stringBuilder.toString());
            binding.addFieldBinding(new FieldBinding(custName, "custName"));
            binding.addFieldBinding(new FieldBinding(customerTypeData, "customerTypeData"));

            binding.autoBind();


            binding.bind(customerData);


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
