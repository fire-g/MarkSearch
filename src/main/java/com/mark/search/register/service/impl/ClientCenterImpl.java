package com.mark.search.register.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.register.CenterFactory;
import com.mark.search.register.entity.ClientNode;
import com.mark.search.register.service.ClientCenter;

/**
 * @author haotian
 */
@Service(name = "register")
public class ClientCenterImpl implements ClientCenter {

    @Override
    public boolean register(ClientNode node){
        CenterFactory.regClient(node);
        return true;
    }

    @Override
    public ClientNode[] list() {
        return CenterFactory.listClient();
    }
}
