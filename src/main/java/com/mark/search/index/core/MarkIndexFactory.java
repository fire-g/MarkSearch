package com.mark.search.index.core;

import com.mark.search.index.subject.MarkDocModel;
import com.mark.search.index.subject.MarkIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 索引工厂
 * @author HaoTian
 */
public class MarkIndexFactory {
    /**
     * 索引列表
     */
    private static List<MarkIndex> indexes;

    private static void save(){

    }

    /**
     * 获取索引
     * @return 获取索引
     */
    public static MarkIndex get(long id){
        for(MarkIndex index:indexes){
            if(index.getId() == id)
            {
                return index;
            }
        }
        return null;
    }

    /**
     * 构建索引
     * @param model 索引构建模型
     */
    public static void create(MarkDocModel model) throws IOException {
        MarkIndex index = new MarkIndex();
        index.create(model);
        indexes.add(index);
    }

    /**
     * 删除索引
     * @return 是否删除成狗
     */
    public static boolean remove(long id){
        return false;
    }

    /**
     * 获取所有的索引ID
     * @return 索引ID列表
     */
    public static List<Long> getAllId(){
        List<Long> idList=new ArrayList<>(3);
        for(MarkIndex index:indexes){
            idList.add(index.getId());
        }
        return idList;
    }
}
