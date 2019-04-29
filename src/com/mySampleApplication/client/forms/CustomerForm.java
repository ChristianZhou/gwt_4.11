package com.mySampleApplication.client.forms;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.Constants;
import com.mySampleApplication.client.services.CustomerServiceRemoteAsync;
import com.mySampleApplication.shared.model.CustomerData;

/**
 * @author zhouguixing
 * @date 2019/4/26 15:20
 * @description     新增窗口
 */
public class CustomerForm extends FormPanel {
    private final TextField<String> tfTitle = new TextField<>();
    private final TextArea taDescription = new TextArea();
    private final TextField<String> tfLink = new TextField<>();
    public CustomerForm() {
        setHeaderVisible(false);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        tfTitle.setFieldLabel("Title");
        taDescription.setFieldLabel("Description");
        tfLink.setFieldLabel("Link");
        tfLink.setAllowBlank(false);
//        tfLink.setRegex("^http\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S*)?$");
//        无效
        tfLink.getMessages().setBlankText("Link is required");
        tfLink.getMessages()
                .setRegexText(
                        "The link field must be a URL e.g. http://www.example.com/rss.xml");
        add(tfTitle);
        add(taDescription);
        add(tfLink);
    }

    public void save(final CustomerData customerData) {
        customerData.setMnemonicCode(tfTitle.getValue());
        customerData.setFax(taDescription.getValue());
        customerData.setCustName(tfLink.getValue());

        final CustomerServiceRemoteAsync feedService = Registry
                .get(Constants.CUSTOMER_SERVICE);
        feedService.save(customerData, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.display("Customer",
                        "Failed to save feed: " + customerData.getMnemonicCode());
            }
            @Override
            public void onSuccess(Void result) {
                Info.display("RSS Reader", "Feed " + customerData.getMnemonicCode()
                        + " saved sucessfully");
//                final ListStore<BeanModel> feedStore = Registry
//                        .get(Constants.CUSTOMER_STORE);
//                BeanModelFactory beanModelFactory =
//                        BeanModelLookup.get().getFactory(customerData.getClass());
//                feedStore.add(beanModelFactory.createModel(customerData));
            }
        });


    }

}
