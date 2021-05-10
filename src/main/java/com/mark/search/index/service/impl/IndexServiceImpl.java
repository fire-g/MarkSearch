package com.mark.search.index.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.index.IndexContent;
import com.mark.search.index.service.IndexService;
import com.mark.search.register.entity.IndexNode;
import com.mark.search.register.entity.RegNode;

/**
 * @author haotian
 */
@Service(name = "index")
public class IndexServiceImpl implements IndexService {

    @Override
    public boolean addRegNode(RegNode regNode) {
        IndexContent.regNodes.add(regNode);
        return false;
    }

    @Override
    public boolean deadRegNode(RegNode regNode) {
        IndexContent.regNodes.remove(regNode);
        return false;
    }

    @Override
    public boolean addNode(IndexNode indexNode) {
        IndexContent.indexNodes.add(indexNode);
        return false;
    }

    @Override
    public boolean deadNode(IndexNode indexNode) {
        IndexContent.indexNodes.remove(indexNode);
        return false;
    }
}
