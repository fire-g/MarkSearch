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
     *  根据<em>id</em>获取索引
     * @return 索引对象或null(当指定id对应的索引不存在时)
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
     * 根据索引模型构建索引
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
