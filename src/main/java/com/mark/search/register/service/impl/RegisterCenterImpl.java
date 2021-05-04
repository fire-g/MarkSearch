package com.mark.search.register.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.register.entity.RegNode;
import com.mark.search.register.CenterFactory;
import com.mark.search.register.service.RegisterCenter;

/**
 * @author haotian
 */
@Service(name = "register")
public class RegisterCenterImpl implements RegisterCenter {
    @Override
    public String hello() {
        return "Hello";
    }

    @Override
    public RegNode[] list() {
        return CenterFactory.listRegNode();
    }

    @Override
    public boolean register(RegNode regNode) {
        CenterFactory.regRegNode(regNode);
        return true;
    }
}
