package com.mySampleApplication.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mySampleApplication.client.services.CustomerTypeService;
import com.mySampleApplication.client.utils.CglibBeanCopierUtil;
import com.mySampleApplication.shared.model.CustomerType;

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
    public List<CustomerType> list() {
        List<com.zgx.bootdemo.entity.CustomerType> list = serivce.list();
        List<CustomerType> list1 = new ArrayList<>();
        for (com.zgx.bootdemo.entity.CustomerType customerType : list) {
            CustomerType customerType1 = new CustomerType();
            CglibBeanCopierUtil.copyProperties(customerType, customerType1);
            list1.add(customerType1);
        }
        return list1;
    }
}
