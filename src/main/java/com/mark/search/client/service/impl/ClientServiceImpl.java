package com.mark.search.client.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.client.ClientFactory;
import com.mark.search.client.service.ClientService;
import com.mark.search.register.entity.IndexNode;

/**
 * @author haotian
 */
@Service(name = "client")
public class ClientServiceImpl implements ClientService {

    @Override
    public boolean addNode(IndexNode indexNode) {
        ClientFactory.addNode(indexNode);
        return true;
    }

    @Override
    public boolean deadNode(IndexNode indexNode) {
        ClientFactory.deadNode(indexNode);
        return false;
    }

}
