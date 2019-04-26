package com.mySampleApplication.client;


import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mySampleApplication.client.example.RssMainPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class MySampleApplication implements EntryPoint {

    public void onModuleLoad() {

        GXT.setDefaultTheme(Theme.GRAY, true);
        Viewport viewport = new Viewport();
        BorderLayout borderLayout = new BorderLayout();
        viewport.setLayout(borderLayout);

        MyTableWidget myTableWidget = new MyTableWidget(10);

//        PagingGrid pagingGrid = new PagingGrid();
        viewport.add(myTableWidget);

        viewport.setLayout(borderLayout);
        RootPanel.get().add(viewport);

    }

}
