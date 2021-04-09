package register.service;

import register.entity.CenterFactory;
import register.entity.RegNode;

/**
 * @author haotian
 */
public class RegisterCenterImpl implements RegisterCenter{
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
