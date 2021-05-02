package index.service;

import index.IndexFactory;
import register.entity.IndexNode;
import register.entity.RegNode;

/**
 * @author haotian
 */
public class IndexServiceImpl implements IndexService{

    @Override
    public boolean addRegNode(RegNode regNode) {
        IndexFactory.regNodes.add(regNode);
        return false;
    }

    @Override
    public boolean deadRegNode(RegNode regNode) {
        IndexFactory.regNodes.remove(regNode);
        return false;
    }

    @Override
    public boolean addNode(IndexNode indexNode) {
        IndexFactory.indexNodes.add(indexNode);
        return false;
    }

    @Override
    public boolean deadNode(IndexNode indexNode) {
        IndexFactory.indexNodes.remove(indexNode);
        return false;
    }
}
