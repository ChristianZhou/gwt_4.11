/*
 * Copyright 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.mySampleApplication.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.mySampleApplication.client.data.CustomerData;
import com.mySampleApplication.client.data.KeywordPageData;
import com.mySampleApplication.client.data.PageData;
import com.mySampleApplication.client.example.DynaTableDataProvider;
import com.mySampleApplication.client.example.DynaTableWidget;
import com.mySampleApplication.shared.service.CustomerServiceRemote;
import com.mySampleApplication.shared.service.CustomerServiceRemoteAsync;

import java.util.List;

public class MyTableWidget extends Composite {


    public class CustomerProvider implements DynaTableDataProvider {

        private final CustomerServiceRemoteAsync customerServiceRemoteAsync;
        private int lastMaxRows = -1;
        private CustomerData[] lastCustomers;
        private int lastStartRow = -1;

        CustomerProvider() {
            customerServiceRemoteAsync = CustomerServiceRemote.App.getInstance();
            ServiceDefTarget target = (ServiceDefTarget) customerServiceRemoteAsync;
            String moduleRelativeURL = GWT.getModuleBaseURL() + "customerServiceRemote";
            target.setServiceEntryPoint(moduleRelativeURL);
        }

        public void updateRowData(final int startRow, final int maxRows,
                                  final RowDataAcceptor acceptor) {
            if (startRow == lastStartRow) {
                if (maxRows == lastMaxRows) {
                    pushResults(acceptor, startRow, lastCustomers);
                    return;
                }
            }
            KeywordPageData keywordPageData = new KeywordPageData();
            keywordPageData.setKeyword("");
            PageData<CustomerData> customerDataPageData = new PageData<>();
            customerDataPageData.setSortField("custCode");
            customerDataPageData.setSortBy(-1);
            customerDataPageData.setOffset(startRow);
            customerDataPageData.setLimit(maxRows);
            keywordPageData.setPage(customerDataPageData);

            customerServiceRemoteAsync.listPage(keywordPageData, new AsyncCallback<PageData<CustomerData>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }
                @Override
                public void onSuccess(PageData<CustomerData> result) {
                    lastStartRow = startRow;
                    lastMaxRows = maxRows;
                    List<CustomerData> list = result.getList();
                    lastCustomers = new CustomerData[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        lastCustomers[i] = list.get(i);
                    }
                    pushResults(acceptor, startRow, lastCustomers);
                }
            });
        }

        private void pushResults(RowDataAcceptor acceptor, int startRow,
                                 CustomerData[] customerDatas) {
            String[][] rows = new String[customerDatas.length][];
            for (int i = 0, n = rows.length; i < n; i++) {
                CustomerData customerData = lastCustomers[i];
                rows[i] = new String[3];
                rows[i][0] = String.valueOf(customerData.getCustCode());
                rows[i][1] = customerData.getCustName();
                rows[i][2] = customerData.getCusCustTypeCode();
            }
            acceptor.accept(startRow, rows);
        }


    }

    private final DynaTableWidget dynaTable;

    MyTableWidget(int visibleRows) {
        String[] columns = new String[]{"客户代码", "客户名称", "客户类型"};
        String[] styles = new String[]{"custCode", "custName", "cusCustTypeCode"};
        CustomerProvider customerProvider = new CustomerProvider();
        dynaTable = new DynaTableWidget(customerProvider, columns, styles, visibleRows);
        initWidget(dynaTable);
    }

    @Override
    protected void onLoad() {
        dynaTable.refresh();
    }

}
