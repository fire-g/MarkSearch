package com.mark.search.index.service.impl;

import com.mark.search.annotation.Inject;
import com.mark.search.annotation.Service;
import com.mark.search.index.annotation.Search;
import com.mark.search.index.core.WriterFactory;
import com.mark.search.index.log.LogFactory;
import com.mark.search.index.log.Logger;
import com.mark.search.index.service.WriterService;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author haotian
 */
@Service(name = "index")
public class WriterServiceImpl implements WriterService {

    private final IndexWriter writer;

    @Inject
    private Logger logger;

    @Inject
    private LogFactory factory;

    public WriterServiceImpl() {
        writer = WriterFactory.getWriter();
    }

    @Override
    public long execute(Object o) {
        logger.add2Log(o);
        try {
            Document document = createDocument(o);
            return add(document);
        } catch (IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public long execute(List<?> list) {
        List<Document> documents = new ArrayList<>();
        for (Object o : list) {
            Document document;
            try {
                document = createDocument(o);
                documents.add(document);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            return add(documents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private long add(List<Document> list) throws IOException {
        writer.addDocuments(list);
        return writer.commit();
    }

    private long add(Document document) throws IOException {
        long l = writer.updateDocument(new Term("url", document.get("url")), document);
//        log.info("影响索引数\t"+l);
        l = writer.commit();
        return l;
//        log.info("影响提交数\t"+l);
    }

    @Override
    public long execute(String[] strings) {
        for (String s : strings) {
            Object index = logger.log2Index(s);
            try {
                Document document = createDocument(index);
                return add(document);
            } catch (IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        }
        factory.redo(strings);
        return 0;
    }

    /**
     * 根据Java实体创建一个文档
     *
     * @param o 文档对应但实体
     * @return lucene文档
     * @throws IllegalAccessException 可能发生的错误
     */
    private Document createDocument(Object o) throws IllegalAccessException {
        Document document = new Document();
        Class<?> clazz = o.getClass();
        java.lang.reflect.Field[] fields = getAllField(clazz);
        for (java.lang.reflect.Field field : fields) {
            field.setAccessible(true);
            //属性值
            Object obj = field.get(o);
            Search index = field.getAnnotation(Search.class);
            Class<?> c = obj.getClass();
            if (index != null) {
                if (index.index()) {
                    if (c.getName().equals(String.class.getName())) {
                        Field f = createIndexField(index.value(), (String) obj, index.participle(), index.store());
                        document.add(f);
                    } else if (c.getName().equals(Integer.class.getName())) {
                        Field f = new IntPoint(index.value(), (int) obj);
                        document.add(f);
                    } else if (c.getName().equals(Date.class.getName())) {
                        assert obj instanceof Date;
                        Field f = new LongPoint(index.value(), ((Date) obj).getTime());
                        document.add(f);
                    } else if (c.getName().equals(Long.class.getName())) {
                        Field f = new LongPoint(index.value(), ((Date) obj).getTime());
                        document.add(f);
                    } else if (c.getName().equals(Short.class.getName())) {
                        Field f = new IntPoint(index.value(), (Short) obj);
                        document.add(f);
                    } else if (c.getName().equals(Float.class.getName())) {
                        Field f = new FloatPoint(index.value(), (Float) obj);
                        document.add(f);
                    } else if (c.getName().equals(Double.class.getName())) {
                        Field f = new DoublePoint(index.value(), (Double) obj);
                        document.add(f);
                    } else if (c.getName().equals(Boolean.class.getName())) {
                        Field f = createIndexField(index.value(), String.valueOf(obj), false, index.store());
                        document.add(f);
                    }
                }
                if (index.store()) {
                    if (c.getName().equals(String.class.getName()) && !index.index()) {
                        Field f = new StoredField(index.value(), (String) obj);
                        document.add(f);
                    } else if (c.getName().equals(Integer.class.getName())) {
                        Field f = new StoredField(index.value(), (Integer) obj);
                        document.add(f);
                    } else if (c.getName().equals(Long.class.getName())) {
                        Field f = new StoredField(index.value(), (Long) obj);
                        document.add(f);
                    } else if (c.getName().equals(Date.class.getName())) {
                        Field f = new StoredField(index.value(), ((Date) obj).getTime());
                        document.add(f);
                    } else if (c.getName().equals(Short.class.getName())) {
                        Field f = new StoredField(index.value(), (Short) obj);
                        document.add(f);
                    } else if (c.getName().equals(Float.class.getName())) {
                        Field f = new StoredField(index.value(), (Float) obj);
                        document.add(f);
                    } else if (c.getName().equals(Double.class.getName())) {
                        Field f = new StoredField(index.value(), (Double) obj);
                        document.add(f);
                    } else if (c.getName().equals(Boolean.class.getName())) {
                        Field f = new StoredField(index.value(), String.valueOf(obj));
                        document.add(f);
                    }
                }
            }
        }
        return document;
    }

    private java.lang.reflect.Field[] getAllField(Class<?> clazz) {
        if (clazz == Object.class) {
            return new java.lang.reflect.Field[0];
        }
        java.lang.reflect.Field[] a = clazz.getDeclaredFields();
        java.lang.reflect.Field[] b = getAllField(clazz.getSuperclass());
        java.lang.reflect.Field[] c = new java.lang.reflect.Field[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    /**
     * 创建索引域
     *
     * @param fieldName  索引域名
     * @param value      值
     * @param participle 是否分词
     * @param store      是否存储
     * @return 域
     */
    private Field createIndexField(String fieldName, String value, boolean participle, boolean store) {
        Field field;
        if (participle) {
            //分词索引并且存储
            if (store) {
                field = new TextField(fieldName, value, Field.Store.YES);
                //分词索引并且不存储
            } else {
                field = new TextField(fieldName, value, Field.Store.NO);
            }
        } else {
            //不分词索引并且存储
            if (store) {
                field = new StringField(fieldName, value, Field.Store.YES);
                //不分词索引并且不存储
            } else {
                field = new StringField(fieldName, value, Field.Store.NO);
            }
        }
        return field;
    }
}
