package com.mySampleApplication.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mySampleApplication.client.data.BankData;

import java.util.List;

public interface BankServiceRemoteAsync {

    void list(AsyncCallback<List> async);

    void read(Long var1, AsyncCallback<BankData> async);

    void save(BankData var1, AsyncCallback<Void> async);

    void delete(Long var1, AsyncCallback<Void> async);

    void update(BankData var1, AsyncCallback<Void> async);

}
