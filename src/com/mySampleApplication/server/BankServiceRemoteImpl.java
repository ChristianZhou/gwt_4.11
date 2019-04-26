package com.mySampleApplication.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mySampleApplication.client.data.BankData;
import com.mySampleApplication.shared.service.BankServiceRemote;
import com.mySampleApplication.client.utils.CglibBeanCopierUtil;
import com.zgx.bootdemo.entity.Bank;
import com.zgx.bootdemo.service.BankSerivce;

import java.util.List;

public class BankServiceRemoteImpl extends RemoteServiceServlet implements BankServiceRemote {

    private BankSerivce bankSerivce = HttpInvokerProxyUtil.getInstance().doRefer(BankSerivce.class, this.url);

    @Override
    public BankData read(Long s) {
        Bank bank  = bankSerivce.read(s);
        BankData bankData = new BankData();
        CglibBeanCopierUtil.copyProperties(bank,bankData);
        return bankData;
    }

    @Override
    public List list() {
        return bankSerivce.list();
    }

    @Override
    public void save(BankData bankData) {
        Bank bank = new Bank();
        CglibBeanCopierUtil.copyProperties(bankData,bank);
        bankSerivce.save(bank);
    }

    @Override
    public void delete(Long s) {
        bankSerivce.delete(s);
    }

    @Override
    public void update(BankData bankData) {
        Bank bank = new Bank();
        CglibBeanCopierUtil.copyProperties(bankData,bank);
        bankSerivce.update(bank);
    }
}