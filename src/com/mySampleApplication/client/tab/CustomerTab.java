package com.mySampleApplication.client.tab;

/**
 * @author zhouguixing
 * @date 2019/4/28 11:41
 * @description tab
 */

import com.extjs.gxt.ui.client.widget.*;
import com.google.gwt.user.client.Element;

public class CustomerTab extends ContentPanel {

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        VerticalPanel vp = new VerticalPanel();

        TabPanel panel = new TabPanel();
        panel.setPlain(true);
        panel.setAutoHeight(true);

        TabItem custTypeTab = new TabItem("客户类型信息维护");
        TabItem custTab = new TabItem("客户信息维护");

        panel.add(custTypeTab);
        panel.add(custTab);

        vp.add(panel);
        add(vp);
    }

}