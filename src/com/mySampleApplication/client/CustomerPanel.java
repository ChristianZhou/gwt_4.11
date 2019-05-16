package com.mySampleApplication.client;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.model.CustomerModel;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.client.services.CustomerTypeServiceAsync;
import com.mySampleApplication.client.util.GWTUtil;
import com.mySampleApplication.server.services.CglibBeanCopierUtil;
import com.mySampleApplication.shared.model.BankData;
import com.mySampleApplication.shared.model.CustomerData;
import com.mySampleApplication.shared.model.CustomerTypeData;
import org.apache.tools.ant.taskdefs.rmic.WLRmic;

import javax.swing.border.Border;
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
    private Button btnRefresh;

    private TreePanel<CustomerTypeData> trPanelCategory;
    private FormPanel fPanelDetail;

    TabItem tabMain = new TabItem();
    TabItem tabItem = new TabItem();

    private PagingLoader<PagingLoadResult<CustomerData>> loaderProduct;

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
    private SimpleComboBox<String> sbbEnable;

    public class MainCustomerTab extends LayoutContainer {
        final ListStore<BeanModel> custStore = Registry.get(Constants.CUSTOMER_STORE);
        private ListStore<CustomerData> storeProduct;


        ColumnModel columnModel;

        Grid<BeanModel> grid;

        Grid<CustomerData> newGrid;


        public class TopPanel extends ContentPanel {
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);
                setLayout(new ColumnLayout());
                setHeadingHtml("查询条件");
                setBorders(false);
                setBodyBorder(false);
                getHeader().setBorders(false);

                class TMPLayoutContainer extends LayoutContainer {
                    public TMPLayoutContainer(int labelWidth, int defaultWidth) {
                        super();
                        setStyleAttribute("paddingLeft", "10px");
                        setStyleAttribute("paddingTop", "10px");
                        FormLayout formLayout = new FormLayout();
                        formLayout.setLabelWidth(labelWidth);
                        formLayout.setDefaultWidth(defaultWidth);
                        formLayout.setLabelAlign(FormPanel.LabelAlign.RIGHT);
                        setLayout(formLayout);
                    }
                }

                LayoutContainer lcnLeft = new TMPLayoutContainer(180, 150);
                keywordTxtField.setName("keyword");
                keywordTxtField.setFieldLabel("输入代码、助记符、名称");
                lcnLeft.add(keywordTxtField, new FormData());

                LayoutContainer lcnCenter = new TMPLayoutContainer(100, 120);
                sbbEnable = new SimpleComboBox<String>();
                sbbEnable.setFieldLabel("启用标记");
                sbbEnable.setTriggerAction(ComboBox.TriggerAction.ALL);
                sbbEnable.add("启用");
                sbbEnable.add("不启用");
                sbbEnable.setSimpleValue("启用");
                lcnCenter.add(sbbEnable, new FormData());

                add(lcnLeft, new com.extjs.gxt.ui.client.widget.layout.ColumnData(350));
                add(lcnCenter, new com.extjs.gxt.ui.client.widget.layout.ColumnData(240));
            }
        }

        public class CenterPanel extends ContentPanel {

            public CenterPanel() {



                setHeaderVisible(false);
                setLayout(new BorderLayout());
                class TMPButton extends Button{
                    public TMPButton(String html, int width) {
                        setHtml(html);
                        setWidth(width);
                    }
                }

//       刷新按钮
                btnRefresh = new TMPButton("刷新",75);
                btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
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
                Button btnAdd = new TMPButton("新增",75);
                btnAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        cardLayout.setActiveItem(detailCustomer);
                    }
                });

//        删除按钮
                Button btnDelete = new TMPButton("删除",75);
                btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        final BeanModel model = grid.getSelectionModel().getSelectedItem();
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

                Button btnMore = new TMPButton("更多功能",75);

                Menu menuMore = new Menu();
                btnMore.setMenu(menuMore);

                MenuItem menuExport = new MenuItem("导出");
                menuMore.add(menuExport);
                menuExport.addSelectionListener(new SelectionListener<MenuEvent>() {
                    @Override
                    public void componentSelected(MenuEvent ce) {
                        Window.alert("开发中");
                    }
                });

                MenuItem menuImport = new MenuItem("导入");
                menuMore.add(menuImport);
                menuImport.addSelectionListener(new SelectionListener<MenuEvent>() {
                    @Override
                    public void componentSelected(MenuEvent ce) {
                        Window.alert("开发中");
                    }
                });

                MenuItem menuBatch = new MenuItem("批量修改属性");
                menuMore.add(menuBatch);
                menuBatch.addSelectionListener(new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        Window.alert("开发中");
                    }
                });


//        顶部按钮条
                LayoutContainer lcnButton = new LayoutContainer();
                HBoxLayout hBoxLayout = new HBoxLayout();
                hBoxLayout.setPadding(new Padding(3));
                hBoxLayout.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.TOP);
                lcnButton.setLayout(hBoxLayout);
                HBoxLayoutData hBoxLayoutData = new HBoxLayoutData(new Margins(0, 5, 0, 0));
                lcnButton.add(btnRefresh,hBoxLayoutData);
                lcnButton.add(btnAdd,hBoxLayoutData);
                lcnButton.add(btnDelete,hBoxLayoutData);
                lcnButton.add(btnMore,hBoxLayoutData);

                add(lcnButton,new BorderLayoutData(Style.LayoutRegion.NORTH,28));

                class CustomerGrid extends LayoutContainer {

                    @Override
                    protected void onRender(Element parent, int index) {
                        super.onRender(parent, index);
                        setLayout(new BorderLayout());

                        final List columns = new ArrayList();

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

                        class NoPageGrid extends LayoutContainer{
                            @Override
                            protected void onRender(Element parent, int index) {
                                super.onRender(parent, index);
                                NoPageGrid.this.setLayout(new FitLayout());
                                columnModel = new ColumnModel(columns);
                                grid = new Grid<>(custStore, columnModel);
                                grid.setBorders(true);
                                add(grid);
                            }
                        }
                        class YesPageGrid extends ContentPanel{
                            public YesPageGrid() {
                                RpcProxy<PagingLoadResult<CustomerData>> newProxy = new RpcProxy<PagingLoadResult<CustomerData>>() {
                                    @Override
                                    protected void load(Object loadConfig,
                                                        AsyncCallback<PagingLoadResult<CustomerData>> callback) {
                                        customerServiceRemoteAsync.findProducts(customerType,keywordTxtField.getValue(),(PagingLoadConfig)loadConfig,callback);
                                    }
                                };

                                loaderProduct = new BasePagingLoader<PagingLoadResult<CustomerData>>(newProxy);
                                loaderProduct.setRemoteSort(true);

                                storeProduct = new ListStore<CustomerData>(loaderProduct);

                                List columns1 = new ArrayList();

                                columns1.add(new ColumnConfig("custCode", "客户代码", 100));
                                columns1.add(new ColumnConfig("custName", "客户名称", 100));
                                columns1.add(new ColumnConfig("mnemonicCode", "助记码", 100));
                                ColumnConfig e1 = new ColumnConfig("customerTypeData", "客户类型", 100);
                                e1.setRenderer(new GridCellRenderer() {
                                    @Override
                                    public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                                        return ((ModelData) model.get(property)).get("custTypeName");
                                    }
                                });
                                columns1.add(e1);
                                columns1.add(new ColumnConfig("tel", "电话", 100));
                                columns1.add(new ColumnConfig("fax", "传真", 100));
                                columns1.add(new ColumnConfig("email", "EMail", 100));
                                columns1.add(new ColumnConfig("tag", "启用标记", 100));
                                columnModel = new ColumnModel(columns1);
                                newGrid = new Grid<CustomerData>(storeProduct, columnModel);
                                newGrid.setBorders(false);
                                newGrid.setStripeRows(true);
                                newGrid.setLoadMask(true);
                                PagingToolBar pageBar = new PagingToolBar(5);
                                pageBar.bind(loaderProduct);
//                Loader加监听
                                loaderProduct.addLoadListener(GWTUtil.getLoadListener(btnRefresh, pageBar, newGrid));
                                loaderProduct.load(0, 5);
                                setBottomComponent(pageBar);
                                add(newGrid,new BorderLayoutData(Style.LayoutRegion.CENTER));
                            }

                            @Override
                            protected void onRender(Element parent, int pos) {
                                super.onRender(parent, pos);
                                YesPageGrid.this.setLayout(new BorderLayout());
//                                setBottomComponent(new Button("asasd"));
                            }
                        }

                        add(new NoPageGrid(),new BorderLayoutData(Style.LayoutRegion.NORTH));

                        YesPageGrid yes = new YesPageGrid();

                        add(yes,new BorderLayoutData(Style.LayoutRegion.CENTER));



                    }
                }

                CustomerGrid widget = new CustomerGrid();
                add(widget,new BorderLayoutData(Style.LayoutRegion.CENTER));
            }
            @Override
            protected void onRender(Element parent, int pos) {
                super.onRender(parent, pos);

            }
        }

        public class LeftPanel extends ContentPanel {
            public class CustomerTypeList extends LayoutContainer {
                public CustomerTypeList() {
                    setLayout(new BorderLayout());
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
                            } else {
                                getCustomerData(null, keywordTxtField.getValue());
                            }
                        }
                    });
                    add(customerTypeDataListField);
                }
            }

            public LeftPanel() {
                LayoutContainer lcnTreeBtn = new LayoutContainer();
                lcnTreeBtn.setStyleAttribute("backgroundColor", "white");
                HBoxLayout itemLayout = new HBoxLayout();
                itemLayout.setPadding(new Padding(3));
                itemLayout.setHBoxLayoutAlign(HBoxLayout.HBoxLayoutAlign.TOP);
                lcnTreeBtn.setLayout(itemLayout);
                Button btnTreeAdd = new Button("新增");
                btnTreeAdd.setWidth(55);
                btnTreeAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        Window.alert("新增类别开发中");
                    }
                });

                Button btnTreeEdit = new Button("修改");
                btnTreeEdit.setWidth(55);
                btnTreeEdit.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        Window.alert("修改类别，开发中");
                    }
                });

                Button btnTreeRemove = new Button("删除");
                btnTreeRemove.setWidth(55);
                btnTreeRemove.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        Window.alert("删除类别，开发中");
                    }
                });
                lcnTreeBtn.add(btnTreeAdd, new HBoxLayoutData(new Margins(0, 5, 0,
                        0)));
                lcnTreeBtn.add(btnTreeEdit, new HBoxLayoutData(new Margins(0, 5, 0,
                        0)));
                lcnTreeBtn.add(btnTreeRemove, new HBoxLayoutData(new Margins(0, 5, 0,
                        0)));

                setCollapsible(false);
                setAnimCollapse(false);
                setLayout(new BorderLayout());

//        标题栏
                setHeadingHtml("客户类型");
                setBorders(false);
                setBodyBorder(false);
                getHeader().setBorders(false);

                add(lcnTreeBtn, new BorderLayoutData(Style.LayoutRegion.NORTH, 28));
//
//                trPanelCategory.fireEvent(Events.SelectionChange);
//                add(trPanelCategory, new BorderLayoutData(Style.LayoutRegion.CENTER));

//        显示内容
                add(new LeftPanel.CustomerTypeList(), new BorderLayoutData(Style.LayoutRegion.CENTER));
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

                loaderProduct.load(0, 5);
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
