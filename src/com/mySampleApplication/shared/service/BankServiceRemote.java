package com.mySampleApplication.shared.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mySampleApplication.client.data.BankData;

import java.util.List;

@RemoteServiceRelativePath("bankServiceRemote")
public interface BankServiceRemote extends RemoteService{
    // Sample interface method of remote interface

    String url ="http://localhost:18888/bankService" ;

    BankData read(Long var1);

    List<BankData> list();

    void save(BankData var1);

    void delete(Long var1);

    void update(BankData var1);

    class App {
        private static BankServiceRemoteAsync bankServiceRemoteAsync = GWT.create(BankServiceRemote.class);

        public static synchronized BankServiceRemoteAsync getInstance() {
            return bankServiceRemoteAsync;
        }
    }
}
