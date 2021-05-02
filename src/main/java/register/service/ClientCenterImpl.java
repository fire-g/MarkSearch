package register.service;

import register.entity.CenterFactory;
import register.entity.ClientNode;

/**
 * @author haotian
 */
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
