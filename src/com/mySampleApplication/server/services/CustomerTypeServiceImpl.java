package com.mySampleApplication.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mySampleApplication.client.services.CustomerTypeService;
import com.mySampleApplication.shared.model.CustomerTypeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouguixing
 * @date 2019/4/27 4:34
 * @description
 */
public class CustomerTypeServiceImpl extends RemoteServiceServlet implements CustomerTypeService {

    private com.zgx.bootdemo.service.CustomerTypeService serivce = HttpInvokerProxyUtil.getInstance().doRefer(com.zgx.bootdemo.service.CustomerTypeService.class, this.url);

    @Override
    public List<CustomerTypeData> list() {
        List<com.zgx.bootdemo.entity.CustomerType> list = serivce.list();
        List<CustomerTypeData> list1 = new ArrayList<>();
        for (com.zgx.bootdemo.entity.CustomerType customerType : list) {
            CustomerTypeData customerTypeData1 = new CustomerTypeData();
            CglibBeanCopierUtil.copyProperties(customerType, customerTypeData1);
            list1.add(customerTypeData1);
        }
        return list1;
    }
}
