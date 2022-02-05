package com.mark.search.index.service.impl;

import com.mark.search.annotation.Service;
import com.mark.search.index.core.MarkIndexFactory;
import com.mark.search.index.service.MarkIndexService;
import com.mark.search.index.subject.MarkDocModel;
import com.mark.search.index.subject.MarkIndex;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 索引相关服务
 */
@Service
public class MarkIndexServiceImpl implements MarkIndexService {

    @Override
    public MarkDocModel[] list() {
        List<Long> list = MarkIndexFactory.getAllId();
        List<MarkDocModel> docModels = new LinkedList<>();
        for(long l:list){
            MarkIndex markIndex = MarkIndexFactory.get(l);
            if(markIndex != null){
                docModels.add(markIndex.getDocModel());
            }
        }
        return docModels.toArray(new MarkDocModel[0]);
    }

    @Override
    public boolean create(MarkDocModel docModel) {
        try {
            MarkIndexFactory.create(docModel);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        return MarkIndexFactory.remove(id);
    }
}
