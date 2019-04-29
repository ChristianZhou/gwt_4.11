package com.mySampleApplication.client.tab;

/**
 * @author zhouguixing
 * @date 2019/4/28 11:41
 * @description tab eg
 */
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

public class BasicTabExample extends LayoutContainer {

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(10);


        TabPanel folder = new TabPanel();
        folder.setWidth(450);
        folder.setAutoHeight(true);

        TabItem shortText = new TabItem("Short Text");
        folder.add(shortText);

        TabItem longText = new TabItem("Long Text");
        folder.add(longText);

        TabPanel panel = new TabPanel();
        panel.setPlain(true);
        panel.setSize(450, 250);

        TabItem normal = new TabItem("Normal");
        normal.addText("Just a plain old tab");
        panel.add(normal);

        TabItem iconTab = new TabItem("Icon CustomerTab");
        iconTab.addText("Just a plain old tab with an icon");
        panel.add(iconTab);

        TabItem ajax1 = new TabItem("Ajax CustomerTab");
        ajax1.setAutoLoad(new RequestBuilder(RequestBuilder.GET, GWT.getHostPageBaseURL() + "data/ajax1.html"));
        panel.add(ajax1);

        TabItem eventTab = new TabItem("Event CustomerTab");
        eventTab.addListener(Events.Select, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
                Window.alert("Event CustomerTab Was Selected");
            }
        });
        eventTab.addText("I am tab 4's content. I also have an event listener attached.");
        panel.add(eventTab);


        vp.add(folder);
        vp.add(panel);
        add(vp);
    }

}