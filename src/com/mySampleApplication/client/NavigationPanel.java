package com.mySampleApplication.client;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.mySampleApplication.client.list.CustomerTypeList;

public class NavigationPanel extends ContentPanel {
    public NavigationPanel() {

//        Fit布局
        setLayout(new FitLayout());

//        标题栏
        setHeadingText("客户类型");


//        显示内容
        add(new CustomerTypeList());


    }


}