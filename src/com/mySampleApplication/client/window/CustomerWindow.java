package com.mySampleApplication.client.window;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.mySampleApplication.client.forms.CustomerForm;
import com.mySampleApplication.shared.model.CustomerData;

/**
 * @author zhouguixing
 * @date 2019/4/26 14:55
 * @description
 */
public class CustomerWindow extends Window {

    private final CustomerForm customerForm = new CustomerForm();

    public CustomerWindow(final CustomerData customerData) {
        setHeadingText("Customer");
        setWidth(350);
        setHeight(200);
        setResizable(false);
        setLayout(new FitLayout());

        final Button btnSave = new Button("Save");
        btnSave.setIconStyle("save");
        btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent ce) {
                btnSave.setEnabled(false);
                if (customerForm.isValid()) {
                    hide(btnSave);
                    customerForm.save(customerData);
                } else {
                    btnSave.setEnabled(true);
                }
            }
        });
        addButton(btnSave);
    }
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        add(customerForm);
    }


}
