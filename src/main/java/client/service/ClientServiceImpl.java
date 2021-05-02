package client.service;

import client.ClientFactory;
import register.entity.IndexNode;

/**
 * @author haotian
 */
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
